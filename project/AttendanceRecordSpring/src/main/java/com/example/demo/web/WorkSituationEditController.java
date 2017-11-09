package com.example.demo.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.User;
import com.example.demo.model.WorkSituation;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WorkSituationRepository;

import attendanceRecord.DailyWorkCheck;
import dao.UserInfoDao;
import dao.WorkSituationDao;
import dao.WorkSituationEditDao;

@RequestMapping("/WorkSituationEdit")
@Controller
public class WorkSituationEditController {

	@Autowired
	HttpSession session;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private WorkSituationRepository workSituationRepository;

	@GetMapping
	public String get(@RequestParam int id, @RequestParam int year, @RequestParam int month, @RequestParam int date, Model model) {

		// HttpSessionインスタンスの取得
		User loginUser = (User) session.getAttribute("loginUser");
		// セッションにログイン情報があるかないかで分岐
		if (loginUser == null) {
			// LoginScreenへリダイレクト
			return "redirect:/LoginScreen";
		} else {

			// パラメータidに対応するUserBeans型のuserInfoインスタンスとユーザーの名前をリクエストスコープに保存
			User user = new User();
			user = userRepository.findByIdIs(id);
			model.addAttribute("user", user);
			model.addAttribute("name", user.getName());

			// 勤務状況のリストを取得し、workSituationListインスタンスをリクエストスコープに保存
			String loginId = user.getLoginId();
			List<WorkSituation> workSituationList = new ArrayList<WorkSituation>();
			workSituationList = workSituationRepository
					.findByLoginIdIsAndCreateYearIsAndCreateMonthIsAndCreateDateIs(loginId, year, month, date);
			model.addAttribute("workSituationList", workSituationList);

			// リクエストパラメータを保存
			model.addAttribute("year", year);
			model.addAttribute("month", month);
			model.addAttribute("date", date);

			// 勤務状況のリストがない場合はエラーメッセージとともにDailyWorkCheckへフォワード
			if (workSituationList.size() == 0) {
				model.addAttribute("errMsg", "編集するデータがありません");

				// workSituationEdit.jspへフォワード
				RequestDispatcher dispatcher = request.getRequestDispatcher("DailyWorkCheck");
				dispatcher.forward(request, response);
				return;
			}

			// workSituationEdit.htmlへフォワード
			return "workSituationEdit";

		}

	}

	@PostMapping
	public String post(Model model) {

		// リクエストパラメータの取得
		int id = Integer.parseInt(request.getParameter("id"));
		int year = Integer.parseInt(request.getParameter("year"));
		int month = Integer.parseInt(request.getParameter("month"));
		int date = Integer.parseInt(request.getParameter("date"));
		int workSituationId = Integer.parseInt(request.getParameter("workSituationId"));
		String workStart = request.getParameter("workStart");
		String workEnd = request.getParameter("workEnd");
		String breakTime = request.getParameter("breakTime");
		String workStartBefore = request.getParameter("workStartBefore");
		String workEndBefore = request.getParameter("workEndBefore");
		String breakTimeBefore = request.getParameter("breakTimeBefore");

		// それぞれの時間が5桁(ex : 19:00)の時、「:00」を付け加える
		if (workStart.length() == 5) {
			workStart = workStart + ":00";
		}
		if (workEnd.length() == 5) {
			workEnd = workEnd + ":00";
		}
		if (breakTime.length() == 5) {
			breakTime = breakTime + ":00";
		}

		// 編集が正しく行われた時はtrueを返す
		boolean result = WorkSituationDao.workSituationEdit(workSituationId, workStart, workEnd, breakTime);

		// リクエストパラメータを保存
		model.addAttribute("id", id);
		model.addAttribute("year", year);
		model.addAttribute("month", month);
		model.addAttribute("date", date);

		// ユーザーを探してuserInfoに代入
		User user = UserInfoDao.findAll(id);
		model.addAttribute("user", user);

		// 編集が正しく行われたか、そうでないかで分岐
		if (result) {
			// 行った編集の履歴を作成
			WorkSituationEditDao.setEditHistory(user.getLoginId(), workStart, workStartBefore, year, month, date,
					"work_start");
			WorkSituationEditDao.setEditHistory(user.getLoginId(), workEnd, workEndBefore, year, month, date,
					"work_end");
			WorkSituationEditDao.setEditHistory(user.getLoginId(), breakTime, breakTimeBefore, year, month, date,
					"break_time");

			// 編集成功メッセージを保存し、DailyWorkCheckクラスのdoGetメソッドを実行
			model.addAttribute("scsMsg", "変更しました");
			DailyWorkCheck dailyWorkCheck = new DailyWorkCheck();
			dailyWorkCheck.doGet(request, response);
			return;
		} else {
			model.addAttribute("errMsg", "入力内容に誤りがあります");

			// workSituationEdit.jspへフォワード
			RequestDispatcher dispatcher = request.getRequestDispatcher("jsp/workSituationEdit.jsp");
			dispatcher.forward(request, response);
		}

	}

}
