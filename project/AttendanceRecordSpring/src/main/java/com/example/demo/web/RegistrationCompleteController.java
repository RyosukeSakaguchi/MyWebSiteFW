package com.example.demo.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.User;

@RequestMapping("/RegistrationComplete")
@Controller
public class RegistrationCompleteController {

	@Autowired
	HttpSession session;

	@GetMapping
	public String get(@RequestParam String situation, Model model) {
		// セッションにログイン情報があるかないかで分岐
		if ((User) session.getAttribute("loginUser") == null) {
			// LoginScreenへリダイレクト
			model.addAttribute("LoginScreen");
		} else {
			// 勤務開始か終了かで分岐し、リクエストスコープにパラメータを保存
			if (situation.equals("start")) {
				model.addAttribute("scsMsg", "今日も一日頑張りましょー");
			} else {
				model.addAttribute("scsMsg", "今日も一日お疲れ様でした");
			}
		}
		// registrationComplete.htmlへフォワード
		return "registrationComplete";
	}

}
