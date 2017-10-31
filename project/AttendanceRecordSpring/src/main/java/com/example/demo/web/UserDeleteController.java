package com.example.demo.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.form.UserListForm;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@RequestMapping("/UserDelete")
@Controller
public class UserDeleteController {

	@Autowired
	HttpSession session;
	@Autowired
	private UserRepository userRepository;

	@GetMapping
	public String get(@ModelAttribute UserListForm userListForm, @RequestParam String id, Model model) {
		// HttpSessionインスタンスの取得
		User loginUser = (User) session.getAttribute("loginUser");
		// セッションにログイン情報があるかないかで分岐
		if (loginUser == null) {
			// LoginScreenへリダイレクト
			return "redirect:/LoginScreen";
		} else if (id == null) {
			// リクエストスコープからパラメータを取得
			int delListId[] = userListForm.getDelListId();

			// パラメータがnullかそうでないかで分岐
			if (delListId == null) {
				// チェックをつけていないメッセージをリクエストスコープに保存
				model.addAttribute("noCheckMsg", "未選択です");


				// UserListへリダイレクト
				return "redirect:/UserList";
//				// UserListのdoGetメソッドを実行
//				UserListController userList = new UserListController();
//				userList.get(userListForm, model);
//				return;

			} else {

				// UserBeans型のインスタンスの配列を生成し、パラメーターの配列delListId[]に対応するユーザーリストをリクエストスコープに保存
				List<User> userList = new ArrayList<User>();
				for (int i = 0; i < delListId.length; i++) {
					User userInfo = new User();
					userInfo = userRepository.findByIdIs(delListId[i]);
					userList.add(userInfo);
				}
				model.addAttribute("userList", userList);

				// userDelete.htmlへフォワード
				return "userDelete";
			}

		} else if (id.equals("all")) {
			// userDelete.htmlへフォワード
			return "userDelete";
		} else {

			// リクエストパラメータの取得
			int idInt = Integer.parseInt(id);

			// リクエストスコープに保存するインスタンス(JavaBeans)の生成
			User user = new User();

			// ユーザーを探してuserInfoに代入
			user = userRepository.findByIdIs(idInt);
			model.addAttribute("user", user);

			// userDelete.htmlへフォワード
			return "userDelete";
		}

	}

}
