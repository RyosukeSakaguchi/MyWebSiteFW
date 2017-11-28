package com.example.demo.web;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.common.UtilLogic;
import com.example.demo.form.WorkSituationRegistrationForm;
import com.example.demo.model.User;
import com.example.demo.repository.TimeRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WorkSituationRepository;
import com.example.demo.validation.WorkSituationRegistrationValidator;

@RequestMapping("/WorkSituationRegistration")
@Controller
public class WorkSituationRegistrationController {

	@Autowired
	HttpSession session;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private WorkSituationRepository workSituationRepository;
	@Autowired
	private TimeRepository timeRepository;
	@Autowired
	WorkSituationRegistrationValidator workSituationRegistrationValidator;

	@InitBinder("workSituationRegistrationForm")
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(workSituationRegistrationValidator);
	}

	@GetMapping
	public String get(@ModelAttribute WorkSituationRegistrationForm workSituationRegistrationForm, Model model) {

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
				if (workSituationRepository.findByLoginIdIsAndCreateDateIs(loginUser.getLoginId(), today).getWorkSitu()
						.length() == 2) {
					result2 = true;
				}
				if (workSituationRepository.findByLoginIdIsAndCreateDateIs(loginUser.getLoginId(), today).getWorkSitu()
						.length() == 7) {
					result3 = true;
				}
			}
			model.addAttribute("result1", result1);
			model.addAttribute("result2", result2);
			model.addAttribute("result3", result3);

			model.addAttribute("loginUser", loginUser);
			model.addAttribute("breakTime", timeRepository.findByIdIs(1).getBreakTime());
			if(workSituationRegistrationForm.getBreakTime() == null) {
				workSituationRegistrationForm.setBreakTime(UtilLogic.intToStringTime(UtilLogic.timeToInt(timeRepository.findByIdIs(1).getBreakTime())));
			}

			Time now = new Time(Calendar.getInstance().getTimeInMillis());

			// ユーザーが帰宅中の時、現在の時間が時間マスターの勤務開始時間より遅れていたら確認メッセージを保存
			if (workSituationRepository.countByLoginIdIsAndCreateDateIs(loginUser.getLoginId(), today) == 0) {
				if (UtilLogic.timeToInt(now) > UtilLogic.timeToInt(timeRepository.findByIdIs(1).getWorkStart())) {
					model.addAttribute("overStartTimeMsg", "今日の勤務開始時間がまだ入力されていません");
				}
			}

			// 勤務中の時、現在の時間が時間マスターの勤務終了時間より遅れていたら確認メッセージを保存
			if (workSituationRepository.countByLoginIdIsAndCreateDateIs(loginUser.getLoginId(), today) == 1) {
				if (UtilLogic.timeToInt(now) > UtilLogic.timeToInt(timeRepository.findByIdIs(1).getWorkEnd())) {
					model.addAttribute("overEndTimeMsg", "今日の勤務終了時間と休憩時間がまだ入力されていません");
				}
			}

			// 今月の勤務時間が50時間を超えていたら、確認メッセージを保存
			SimpleDateFormat y = new SimpleDateFormat("yyyy");
			SimpleDateFormat m = new SimpleDateFormat("MM");
			int year = Integer.parseInt(y.format(today));
			int month = Integer.parseInt(m.format(today));
			String titalOvertime = UtilLogic.totalOvertime(workSituationRepository
					.findByLoginIdIsAndCreateYearIsAndCreateMonthIs(loginUser.getLoginId(), year, month));
			int titalOvertimeInt = UtilLogic.stringTimeToInt(titalOvertime);
			if (titalOvertimeInt >= 500000) {
				model.addAttribute("confMsg", "今月の残業時間が50時間を超えています");
			}

			// workSituationRegistration.htmlへフォワード
			return "workSituationRegistration";

		}
	}

	@PostMapping
	public String post(@Validated @ModelAttribute WorkSituationRegistrationForm workSituationRegistrationForm,
			BindingResult result, Model model) {

		if (result.hasErrors()) {
			return get(workSituationRegistrationForm, model);
		}

		// リクエストパラメータの取得
		String situation = workSituationRegistrationForm.getSituation();

		// HttpSessionインスタンスの取得
		User loginUser = (User) session.getAttribute("loginUser");
		String loginId = loginUser.getLoginId();

		// 勤務開始なのか終了なのかで分岐
		if (situation.equals("start")) {
			// 勤務状況をテーブルに保存
			Time workStartMaster = timeRepository.findByIdIs(1).getWorkStart();
			UtilLogic.workStart(loginId, workStartMaster, workSituationRepository, userRepository);
			// RegistrationCompleteへリダイレクト
			return "redirect:/RegistrationComplete?situation=start";

		} else {

			// 入力が正しいとき、勤務状況をテーブルに保存
			Time breakTime = Time.valueOf(workSituationRegistrationForm.getBreakTime());
			Time workEndMaster = timeRepository.findByIdIs(1).getWorkEnd();
			Time workTimeMaster = timeRepository.findByIdIs(1).getWorkTime();
			UtilLogic.workEnd(loginId, breakTime, workEndMaster, workTimeMaster, workSituationRepository, userRepository);

			// RegistrationCompleteへリダイレクト
			return "redirect:/RegistrationComplete?situation=end";

		}
	}

}
