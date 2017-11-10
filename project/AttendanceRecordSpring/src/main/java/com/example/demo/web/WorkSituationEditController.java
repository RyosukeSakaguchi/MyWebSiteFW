package com.example.demo.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.common.UtilLogic;
import com.example.demo.form.WorkSituationEditForm;
import com.example.demo.model.User;
import com.example.demo.model.WorkSituation;
import com.example.demo.repository.TimeRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WorkSituationEditRepository;
import com.example.demo.repository.WorkSituationRepository;

@RequestMapping("/WorkSituationEdit")
@Controller
public class WorkSituationEditController {

	@Autowired
	HttpSession session;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private WorkSituationRepository workSituationRepository;
	@Autowired
	private WorkSituationEditRepository workSituationEditRepository;
	@Autowired
	private TimeRepository timeRepository;

	@GetMapping
	public String get(@RequestParam("id") int id, @RequestParam("year") int year, @RequestParam("month") int month, @RequestParam("date") int date, Model model) {

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
			model.addAttribute("name", user.getName());

			// 勤務状況のリストがない場合はエラーメッセージとともにDailyWorkCheckへフォワード
			if (workSituationList.size() == 0) {
				model.addAttribute("errMsg", "編集するデータがありません");

				// dailyWorkCheck.htmlへフォワード
				return "dailyWorkCheck";
			}

			// workSituationEdit.htmlへフォワード
			return "workSituationEdit";

		}

	}

	@PostMapping
	public String post(@ModelAttribute WorkSituationEditForm workSituationEditForm, Model model, RedirectAttributes attr) {

		// リクエストパラメータの取得
		int id = workSituationEditForm.getId();
		int year = workSituationEditForm.getYear();
		int month = workSituationEditForm.getMonth();
		int date = workSituationEditForm.getDate();
		int workSituationId = workSituationEditForm.getWorkSituationId();
		String workStart = workSituationEditForm.getWorkStart();
		String workEnd = workSituationEditForm.getWorkEnd();
		String breakTime = workSituationEditForm.getBreakTime();
		String workStartBefore = workSituationEditForm.getWorkStartBefore();
		String workEndBefore = workSituationEditForm.getWorkEndBefore();
		String breakTimeBefore = workSituationEditForm.getBreakTimeBefore();

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
		boolean result = UtilLogic.workSituationEdit(workSituationId, workStart, workEnd, breakTime, workSituationRepository, timeRepository);

		// リクエストパラメータを保存
		model.addAttribute("id", id);
		model.addAttribute("year", year);
		model.addAttribute("month", month);
		model.addAttribute("date", date);

		// ユーザーを探してuserInfoに代入
		User user = userRepository.findByIdIs(id);
		model.addAttribute("user", user);

		// 編集が正しく行われたか、そうでないかで分岐
		if (result) {
			// 行った編集の履歴を作成
			UtilLogic.setEditHistory(user.getLoginId(), workStart, workStartBefore, year, month, date, workSituationEditRepository,
					"work_start");
			UtilLogic.setEditHistory(user.getLoginId(), workEnd, workEndBefore, year, month, date, workSituationEditRepository,
					"work_end");
			UtilLogic.setEditHistory(user.getLoginId(), breakTime, breakTimeBefore, year, month, date, workSituationEditRepository,
					"break_time");

			// 編集成功メッセージを保存し、DailyWorkCheckクラスのdoGetメソッドを実行
			attr.addFlashAttribute("scsMsg", "変更しました");
			// WorkSituationEditへリダイレクト
			return "redirect:/WorkSituationEdit?id=" + id + "&year=" + year + "&month=" + month +"&date=" + date;

		} else {
			model.addAttribute("errMsg", "入力内容に誤りがあります");

			// workSituationEdit.htmlへフォワード
			return "workSituationEdit";
		}

	}

}
