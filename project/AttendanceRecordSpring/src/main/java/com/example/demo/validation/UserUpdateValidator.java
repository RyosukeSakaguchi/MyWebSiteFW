package com.example.demo.validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.demo.form.UserUpdateForm;

@Component
public class UserUpdateValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return UserUpdateForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		UserUpdateForm userUpdateForm = (UserUpdateForm) target;
		String password = userUpdateForm.getPassword();
		String passwordConf = userUpdateForm.getPasswordConf();

		if (password == "" || passwordConf == "") {
			// must be checked by @NotNull
			return;
		}
		if (!password.equals(passwordConf)) {
			errors.rejectValue("password", "PasswordEqualsValidator.signUpForm.password", "・パスワードとパスワード(確認)が異なります");
		}

		String birthDate = userUpdateForm.getBirthDate();

		if (birthDate == "") {
			// must be checked by @NotNull
			return;
		}

		Date now = new Date();
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date birthDateDate = null;
		try {
			birthDateDate = sdFormat.parse(birthDate);
		} catch (ParseException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		if (birthDateDate.compareTo(now) == 1) {
			errors.rejectValue("birthDate", null, "・生年月日が正しくありません");
		}

	}

}
