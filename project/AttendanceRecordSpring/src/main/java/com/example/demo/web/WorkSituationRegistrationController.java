package com.example.demo.web;

import java.sql.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.User;
import com.example.demo.repository.WorkSituationRepository;

@RequestMapping("/WorkSituationRegistration")
@Controller
public class WorkSituationRegistrationController {

	@Autowired
	HttpSession session;
	@Autowired
	private WorkSituationRepository workSituationRepository;

	@GetMapping
	public String get(Model model) {

		User loginUser = (User) session.getAttribute("loginUser");
		// セッションにログイン情報があるかないかで分岐
		if (loginUser == null) {
			// LoginScreenへリダイレクト
			return "redirect:/LoginScreen";
		} else if (loginUser.getId() == 1) {
			// LoginScreenへリダイレクト
			return "redirect:/UserList";
		} else {
			Date today = new Date(System.currentTimeMillis());
			// ユーザーが勤務前の時はresult1がtrue(他はfalse)
			// ユーザーが勤務中の時はresult2がtrue(他はfalse)
			// ユーザーが帰宅の時はresult3がtrue(他はfalse)
			// これらをリクエストスコープに保存
			boolean result1 = false;
			boolean result2 = false;
			boolean result3 = false;
			if (workSituationRepository.countByLoginIdIsAndCreateDateIs(loginUser.getLoginId(), today) == 0) {
				result1 = true;
			}
			if (!result1) {
				if (workSituationRepository.findWorksituationByLoginIdIsAndCreateDateIs(loginUser.getLoginId(), today)
						.length() == 2) {
					result2 = true;
				}
				if (workSituationRepository.findWorksituationByLoginIdIsAndCreateDateIs(loginUser.getLoginId(), today)
						.length() == 7) {
					result3 = true;
				}
			}
			model.addAttribute("result1", result1);
			model.addAttribute("result2", result2);
			model.addAttribute("result3", result3);

			model.addAttribute("loginUser", loginUser);

			// ユーザーが帰宅中の時、現在の時間が時間マスターの勤務開始時間より遅れていたら確認メッセージを保存
			// if (WorkSituationDao.isOverTime("work_start")) {
			// model.addAttribute("overStartTimeMsg", "今日の勤務開始時間がまだ入力されていません");
			// }

			// 勤務中の時、現在の時間が時間マスターの勤務終了時間より遅れていたら確認メッセージを保存
			// if (WorkSituationDao.isOverTime("work_end")) {
			// model.addAttribute("overEndTimeMsg", "今日の勤務終了時間と休憩時間がまだ入力されていません");
			// }

			// 今月の勤務時間が50時間を超えていたら、確認メッセージを保存
			// SimpleDateFormat y = new SimpleDateFormat("yyyy");
			// SimpleDateFormat m = new SimpleDateFormat("MM");
			// int year = Integer.parseInt(y.format(today));
			// int month = Integer.parseInt(m.format(today));
			// String titalOvertime = UtilLogic
			// .totalOvertime(WorkSituationDao.findAll(loginUser.getLoginId(), year,
			// month));
			// int titalOvertimeInt = UtilLogic.stringTimeToInt(titalOvertime);
			// if (titalOvertimeInt >= 500000) {
			// model.addAttribute("confMsg", "今月の残業時間が50時間を超えています");
			// }

			// workSituationRegistration.htmlへフォワード
			return "workSituationRegistration";

		}
	}

}
