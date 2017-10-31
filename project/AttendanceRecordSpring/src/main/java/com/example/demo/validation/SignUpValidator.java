package com.example.demo.validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.demo.form.SignUpForm;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Component
public class SignUpValidator implements Validator {
	@Autowired
	private UserRepository userRepository;

	@Override
	public boolean supports(Class<?> clazz) {
		return SignUpForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		SignUpForm signUpForm = (SignUpForm) target;
		String loginId = signUpForm.getLoginId();

		List<User> userList = userRepository.findAll();
		for (User user : userList) {
			if (loginId.equals(user.getLoginId())) {
				errors.rejectValue("loginId", "NewLoginIdValidator.signUpForm.loginId", "・そのログインIDは使用できません");
			}
		}



		String birthDate = signUpForm.getBirthDate();

		if (birthDate != "") {

			Date now = new Date();
			SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date birthDateDate = null;
			try {
				birthDateDate = sdFormat.parse(birthDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			if (birthDateDate.compareTo(now) == 1) {
				errors.rejectValue("birthDate", null, "・生年月日が正しくありません");
			}
		}



		String password = signUpForm.getPassword();
		String passwordConf = signUpForm.getPasswordConf();

		if (password != "" && passwordConf != "") {
			if (!password.equals(passwordConf)) {
				errors.rejectValue("password", "PasswordEqualsValidator.signUpForm.password", "・パスワードとパスワード(確認)が異なります");
			}
		}

	}
}
