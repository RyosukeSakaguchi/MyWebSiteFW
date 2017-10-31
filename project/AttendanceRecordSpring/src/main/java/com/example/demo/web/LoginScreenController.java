package com.example.demo.web;

import java.security.NoSuchAlgorithmException;

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
import com.example.demo.form.LoginScreenForm;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.validation.LoginScreenValidator;

@RequestMapping("/LoginScreen")
@Controller
public class LoginScreenController {

	@Autowired
	HttpSession session;
	@Autowired
	LoginScreenValidator loginScreenValidator;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(loginScreenValidator);
	}

	@GetMapping
	public String get(@ModelAttribute LoginScreenForm loginScreenForm, Model model) {

		User loginUser = (User) session.getAttribute("loginUser");
		// セッションにログイン情報があるかないかで分岐
		if (loginUser == null) {
			// セッションにログイン情報がないときはloginScreen.htmlへフォワード
			return "loginScreen";
		} else {
			if (loginUser.getId() == 1) {
				// ログインユーザーが管理者のとき、UserListへリダイレクト
				return "redirect:/UserList";
			} else {
				// ログインユーザーが一般ユーザーのとき、WorkSituationRegistrationへリダイレクト
				return "redirect:/WorkSituationRegistration";
			}
		}
	}

	@Autowired
	private UserRepository userRepository;

	@PostMapping
	public String post(@Validated @ModelAttribute LoginScreenForm loginScreenForm, BindingResult result, Model model) {

		if (result.hasErrors()) {
			return get(loginScreenForm, model);
		}

		// リクエストパラメータの取得
		String loginId = loginScreenForm.getLoginId();
		String password = loginScreenForm.getPassword();

		// 暗号化されたパスワードを生成
		String encPass = null;
		try {
			encPass = UtilLogic.encrpt(password);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		// ユーザーを探し出し、userInfoに代入
		User userInfo = userRepository.findByLoginIdIsAndPasswordIs(loginId, encPass);

		// セッションスコープにインスタスを保存
		session.setAttribute("loginUser", userInfo);

		// 管理者か一般ユーザーかで分岐
		if (userInfo.getId() == 1) {
			// UserListへリダイレクト
			return "redirect:/UserList";
		} else {
			// WorkSituationRegistrationへリダイレクト
			return "redirect:/WorkSituationRegistration";
		}

	}

}
