package com.example.demo.web;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.common.UtilLogic;
import com.example.demo.form.UserListForm;
import com.example.demo.model.PositionMaster;
import com.example.demo.model.User;
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
				int pageNumber = userListForm.getPageNumber();
				Page<User> page = userRepository.findByIdIsNot(1, new PageRequest(pageNumber, userNumberPerPage));

				// リクエストスコープに保存
				model.addAttribute("login_id", userListForm.getLoginId());
				model.addAttribute("name", userListForm.getName());
				model.addAttribute("position", userListForm.getPosition());
				model.addAttribute("birth_date_from", userListForm.getBirthDateFrom());
				model.addAttribute("birth_date_to", userListForm.getBirthDateTo());
				model.addAttribute("workSituation", userListForm.getWorkSituation());
				model.addAttribute("page", page);

			} else {
				int pageNumber = 0;
				Page<User> page = userRepository.findByIdIsNot(1, new PageRequest(pageNumber, userNumberPerPage));

				// リクエストスコープに保存
				model.addAttribute("page", page);
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
		int userNumberPerPage = 5;
		int pageNumber = userListForm.getPageNumber();
		// ユーザーを検索し、userListに代入

		Page<User> page = userRepository.findAll(Specifications.where(UtilLogic.loginIdIs(userListForm.getLoginId()))
						.and(UtilLogic.nameContains(userListForm.getName()))
						.and(UtilLogic.positionIs(userListForm.getPosition()))
						.and(UtilLogic.birthDateBetween(userListForm.getBirthDateFrom(), userListForm.getBirthDateTo()))
						.and(UtilLogic.workSituationIs(userListForm.getWorkSituation()))
						.and(UtilLogic.idIsNot(1)),
				new PageRequest(pageNumber, userNumberPerPage));

		// リクエストパラメータを保存
		int totalPageNumber = UtilLogic.getTotalPageNumber(userList, userNumberPerPage);

		model.addAttribute("userList", userList);
		model.addAttribute("userNumberPerPage", userNumberPerPage);
		model.addAttribute("totalPageNumber", totalPageNumber);
		model.addAttribute("pageNumber", pageNumber);
		model.addAttribute("loginId", userListForm.getLoginId());
		model.addAttribute("name", userListForm.getName());
		model.addAttribute("position", userListForm.getPosition());
		model.addAttribute("birthDateFrom", userListForm.getBirthDateFrom());
		model.addAttribute("birthDateTo", userListForm.getBirthDateTo());
		model.addAttribute("workSituation", userListForm.getWorkSituation());
		model.addAttribute("workSituationRepository", workSituationRepository);
		model.addAttribute("page", page);

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
