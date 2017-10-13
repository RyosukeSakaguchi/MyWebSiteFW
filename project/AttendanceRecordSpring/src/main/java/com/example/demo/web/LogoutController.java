package com.example.demo.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.form.LoginScreenForm;
import com.example.demo.model.User;

@RequestMapping("/Logout")
@Controller
public class LogoutController {

	@Autowired
	HttpSession session;

	@GetMapping
	public String get(@ModelAttribute LoginScreenForm loginScreenForm, Model model, RedirectAttributes attributes) {
		User loginUser = (User) session.getAttribute("loginUser");

		if (loginUser != null) {
			// セッションの情報を消去
			session.removeAttribute("loginUser");
			attributes.addFlashAttribute("logout", "ログアウトしました");
		}

		// LoginScreenへリダイレクト
		return "redirect:/LoginScreen";

	}

}
