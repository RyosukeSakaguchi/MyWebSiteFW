package com.example.demo.validation;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.demo.common.UtilLogic;
import com.example.demo.form.LoginScreenForm;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Component
public class LoginScreenValidator implements Validator {

	@Autowired
	private UserRepository userRepository;

	@Override
	public boolean supports(Class<?> clazz) {
		return LoginScreenForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		LoginScreenForm loginScreenForm = (LoginScreenForm) target;
		String loginId = loginScreenForm.getLoginId();
		String password = loginScreenForm.getPassword();

		if (loginId == "") {
			// must be checked by @NotNull
			return;
		}

		if (password == "") {
			// must be checked by @NotNull
			return;
		}

		// 暗号化されたパスワードを生成
		String encPass = null;
		try {
			encPass = UtilLogic.encrpt(password);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		List<User> userList = userRepository.findAll();
		boolean result = true;
		for (User user : userList) {
			if (loginId.equals(user.getLoginId()) && encPass.equals(user.getPassword())) {
				result = false;
			}
		}

		if(result) {
			errors.rejectValue("loginId", null, "ログインIDまたはパスワードが異なります");
		}
	}

}
