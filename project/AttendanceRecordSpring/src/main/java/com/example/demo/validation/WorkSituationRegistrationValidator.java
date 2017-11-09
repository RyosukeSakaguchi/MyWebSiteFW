package com.example.demo.validation;

import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.demo.common.UtilLogic;
import com.example.demo.form.WorkSituationRegistrationForm;
import com.example.demo.model.User;
import com.example.demo.repository.WorkSituationRepository;

@Component
public class WorkSituationRegistrationValidator implements Validator {

	@Autowired
	HttpSession session;
	@Autowired
	private WorkSituationRepository workSituationRepository;

	@Override
	public boolean supports(Class<?> clazz) {
		return WorkSituationRegistrationForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		WorkSituationRegistrationForm workSituationRegistrationForm = (WorkSituationRegistrationForm) target;

		// 今日の日付を時間を取得
		Date today = new Date(Calendar.getInstance().getTimeInMillis());
		Time now = new Time(Calendar.getInstance().getTimeInMillis());

		// HttpSessionインスタンスの取得
		User loginUser = (User) session.getAttribute("loginUser");
		String loginId = loginUser.getLoginId();

		// 今日の日付と受け取ったログインIDに関する勤務開始時間をworkStartに代入
		Time workStart = workSituationRepository.findByLoginIdIsAndCreateDateIs(loginId, today).getWorkStart();

		// workTimeIntを計算
		int workTimeInt = UtilLogic.timeSubtraction(
				UtilLogic.timeSubtraction(UtilLogic.timeToInt(now), UtilLogic.timeToInt(workStart)),
				UtilLogic.timeToInt(workSituationRegistrationForm.getBreakTime()));

		if (workTimeInt < 0) {
			errors.rejectValue("breakTime", null, "休憩時間が正しくありません");
		}

	}

}
