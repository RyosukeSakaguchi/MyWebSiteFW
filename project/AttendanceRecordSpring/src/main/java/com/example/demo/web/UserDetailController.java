package com.example.demo.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@RequestMapping("/UserDetail")
@Controller
public class UserDetailController {

	@Autowired
	HttpSession session;
	@Autowired
	private UserRepository userRepository;

	@GetMapping
	public String get(@RequestParam String id, Model model) {

		// HttpSessionインスタンスの取得
		User loginUser = (User) session.getAttribute("loginUser");
		// セッションにログイン情報があるかないかで分岐
		if (loginUser == null) {
			// LoginScreenへリダイレクト
			return "redirect:/LoginScreen";
		} else {
			// リクエストパラメータの取得
			int idInt = Integer.parseInt(id);

			// リクエストスコープに保存するインスタンス(JavaBeans)の生成
			User user = new User();

			// ユーザーを探してuserInfoに代入
			user = userRepository.findByIdIs(idInt);
			model.addAttribute("user", user);

			model.addAttribute("loginUser", loginUser);

			// userDetail.htmlへフォワード
			return "userDetail";
		}

	}

}
