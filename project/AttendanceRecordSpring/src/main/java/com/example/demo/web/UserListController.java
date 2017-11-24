package com.example.demo.web;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.common.UtilLogic;
import com.example.demo.form.UserListForm;
import com.example.demo.model.PositionMaster;
import com.example.demo.model.User;
import com.example.demo.model.WorkSituation;
import com.example.demo.repository.PositionRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WorkSituationRepository;

@RequestMapping("/UserList")
@Controller
public class UserListController {

	@Autowired
	HttpSession session;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PositionRepository positionRepository;
	@Autowired
	private WorkSituationRepository workSituationRepository;

	@GetMapping
	public String get(@ModelAttribute UserListForm userListForm, Model model) {

		// HttpSessionインスタンスの取得
		User loginUser = (User) session.getAttribute("loginUser");
		// セッションにログイン情報があるかないかで分岐
		if (loginUser == null) {
			// LoginScreenへリダイレクト
			return "redirect:/LoginScreen";
		} else if (loginUser.getId() != 1) {
			// WorkSituationRegistrationへリダイレクト
			return "redirect:/WorkSituationRegistration";
		} else {

			// 1ページ毎に表示するユーザー数を5ユーザーにする
			int userNumberPerPage = 5;

			// リクエストパラメーターがnullかどうかで分岐
			if (userListForm.getPageNumber() != 0) {

				// リクエストパラメータがnullでない時、idリストのパラメータに対応するユーザーリストを生成
				List<User> userList = new ArrayList<User>();
				int[] userIdList = userListForm.getUserIdList();
				for (int i = 0; i < userIdList.length; i++) {
					User userInfo = userRepository.findByIdIs(userIdList[i]);
					userList.add(userInfo);
				}
				int pageNumber = userListForm.getPageNumber();

				// リクエストスコープに保存
				int totalPageNumber = UtilLogic.getTotalPageNumber(userList, userNumberPerPage);
				model.addAttribute("userNumberPerPage", userNumberPerPage);
				model.addAttribute("totalPageNumber", totalPageNumber);
				model.addAttribute("pageNumber", pageNumber);
				model.addAttribute("userList", userList);
				model.addAttribute("login_id", userListForm.getLoginId());
				model.addAttribute("name", userListForm.getName());
				model.addAttribute("position", userListForm.getPosition());
				model.addAttribute("birth_date_from", userListForm.getBirthDateFrom());
				model.addAttribute("birth_date_to", userListForm.getBirthDateTo());
				model.addAttribute("workSituation", userListForm.getWorkSituation());
			} else {
				// userテーブルにある全てのユーザーを取り出し、勤務中のユーザー」を先に並べ、
				// 「帰宅しているユーザー」を後に並べかえ、 さらに管理者がリストに入っている場合は取り除く
				List<User> userList = userRepository.findAll();
				userList = UtilLogic.userListSort(userList, workSituationRepository);

				// リクエストスコープに保存
				int totalPageNumber = UtilLogic.getTotalPageNumber(userList, userNumberPerPage);
				int pageNumber = 1;
				model.addAttribute("userNumberPerPage", userNumberPerPage);
				model.addAttribute("totalPageNumber", totalPageNumber);
				model.addAttribute("pageNumber", pageNumber);
				model.addAttribute("userList", userList);
			}

			Date now = new Date(System.currentTimeMillis());
			SimpleDateFormat y = new SimpleDateFormat("yyyy");
			SimpleDateFormat m = new SimpleDateFormat("MM");
			int year = Integer.parseInt(y.format(now));
			int month = Integer.parseInt(m.format(now));
			String startDateString = year + "-" + month + "-01";
			String endDateString = year + "-" + month + "-31";
			Date startDate = Date.valueOf(startDateString);
			Date endDate = Date.valueOf(endDateString);

			List<PositionMaster> positionList = positionRepository.findAll();
			// リクエストパラメーターを保存
			model.addAttribute("utilLogic", new UtilLogic());
			model.addAttribute("positionList", positionList);
			model.addAttribute("year", year);
			model.addAttribute("month", month);
			model.addAttribute("startDate", startDate);
			model.addAttribute("endDate", endDate);
			model.addAttribute("workSituationRepository", workSituationRepository);

			// userList.htmlへフォワード
			return "userList";
		}
	}

	@PostMapping
	public String post(@Validated @ModelAttribute UserListForm userListForm, Model model) {

		List<User> userList = new ArrayList<User>();
		Date now = new Date(System.currentTimeMillis());
		if (StringUtils.isEmpty(userListForm.getWorkSituation())) {
			// ユーザーを検索し、userListに代入
			userList = userRepository.findAll(Specifications.where(UtilLogic.loginIdIs(userListForm.getLoginId()))
					.and(UtilLogic.nameContains(userListForm.getName()))
					.and(UtilLogic.positionIs(userListForm.getPosition()))
					.and(UtilLogic.birthDateBetween(userListForm.getBirthDateFrom(), userListForm.getBirthDateTo())));
		} else if(userListForm.getWorkSituation().equals("勤務中")){
			List<User> userList1 = userRepository.findAll(Specifications
					.where(UtilLogic.loginIdIs(userListForm.getLoginId()))
					.and(UtilLogic.nameContains(userListForm.getName()))
					.and(UtilLogic.positionIs(userListForm.getPosition()))
					.and(UtilLogic.birthDateBetween(userListForm.getBirthDateFrom(), userListForm.getBirthDateTo())));

			List<WorkSituation> workSituationList = workSituationRepository.findByCreateDateIsAndWorkSituLengthIs(now, 6);
			for (User user : userList1) {
				boolean result = false;
				for (WorkSituation workSituation : workSituationList) {
					if (workSituation.getLoginId().equals(user.getLoginId())) {
						result = true;
					}
				}
				if (result) {
					userList.add(userRepository.findByLoginIdIs(user.getLoginId()));
				}
			}

		}else {
			List<User> userList1 = userRepository.findAll(Specifications
					.where(UtilLogic.loginIdIs(userListForm.getLoginId()))
					.and(UtilLogic.nameContains(userListForm.getName()))
					.and(UtilLogic.positionIs(userListForm.getPosition()))
					.and(UtilLogic.birthDateBetween(userListForm.getBirthDateFrom(), userListForm.getBirthDateTo())));

			List<WorkSituation> workSituationList = workSituationRepository.findByCreateDateIsAndWorkSituLengthIs(now, 6);

			for (User user : userList1) {
				boolean result = true;
				for (WorkSituation workSituation : workSituationList) {
					if (workSituation.getLoginId().equals(user.getLoginId())) {
						result = false;
					}
				}
				if (result) {
					userList.add(userRepository.findByLoginIdIs(user.getLoginId()));
				}
			}

		}

		userList = UtilLogic.userListSort(userList, workSituationRepository);

		// リクエストパラメータを保存
		int userNumberPerPage = 5;
		int totalPageNumber = UtilLogic.getTotalPageNumber(userList, userNumberPerPage);
		int pageNumber = 1;
		model.addAttribute("userList", userList);
		model.addAttribute("userNumberPerPage", userNumberPerPage);
		model.addAttribute("totalPageNumber", totalPageNumber);
		model.addAttribute("pageNumber", pageNumber);
		model.addAttribute("login_id", userListForm.getLoginId());
		model.addAttribute("name", userListForm.getName());
		model.addAttribute("position", userListForm.getPosition());
		model.addAttribute("birth_date_from", userListForm.getBirthDateFrom());
		model.addAttribute("birth_date_to", userListForm.getBirthDateTo());
		model.addAttribute("workSituation", userListForm.getWorkSituation());
		model.addAttribute("workSituationRepository", workSituationRepository);
		model.addAttribute("utilLogic", new UtilLogic());

		SimpleDateFormat y = new SimpleDateFormat("yyyy");
		SimpleDateFormat m = new SimpleDateFormat("MM");
		int year = Integer.parseInt(y.format(now));
		int month = Integer.parseInt(m.format(now));
		String startDateString = year + "-" + month + "-01";
		String endDateString = year + "-" + month + "-31";
		Date startDate = Date.valueOf(startDateString);
		Date endDate = Date.valueOf(endDateString);

		List<PositionMaster> positionList = positionRepository.findAll();
		// リクエストパラメーターを保存
		model.addAttribute("positionList", positionList);
		model.addAttribute("year", year);
		model.addAttribute("month", month);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);

		// userList.htmlへフォワード
		return "userList";
	}

}
