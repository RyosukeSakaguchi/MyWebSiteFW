package com.example.demo.web;

import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.common.UtilLogic;
import com.example.demo.form.UserUpdateForm;
import com.example.demo.model.PositionMaster;
import com.example.demo.model.User;
import com.example.demo.repository.PositionRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.validation.UserUpdateValidator;

@RequestMapping("/UserUpdate")
@Controller
public class UserUpdateController {

	@Autowired
	UserUpdateValidator userUpdateValidator;
	@Autowired
	HttpSession session;
	@Autowired
	private PositionRepository positionRepository;
	@Autowired
	private UserRepository userRepository;


	@InitBinder("userUpdateForm")
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(userUpdateValidator);
	}

	@GetMapping
	public String get(@RequestParam int id, @ModelAttribute UserUpdateForm userUpdateForm, Model model) {

		// HttpSessionインスタンスの取得
		User loginUser = (User) session.getAttribute("loginUser");
		// セッションにログイン情報があるかないかで分岐
		if (loginUser == null) {
			// LoginScreenへリダイレクト
			return "redirect:/LoginScreen";
		} else {

			// リクエストスコープに保存するインスタンス(JavaBeans)の生成
			User user = new User();

			// ユーザーを探してuserInfoに代入
			user = userRepository.findByIdIs(id);
			model.addAttribute("user", user);


			List<PositionMaster> positionList = positionRepository.findAll();
			// リクエストパラメーターを保存
			model.addAttribute("positionList", positionList);
			model.addAttribute("loginUser", loginUser);

			// userUpdate.htmlへフォワード
			return "userUpdate";

		}
	}

	@PostMapping
	public String post(@Validated @ModelAttribute UserUpdateForm userUpdateForm, BindingResult result, Model model) {

		if (result.hasErrors()) {
			return get(userUpdateForm.getId(), userUpdateForm, model);
		}


		// リクエストパラメータの取得
		int id = userUpdateForm.getId();
		String password = userUpdateForm.getPassword();
		String name = userUpdateForm.getName();
		String birthDate = userUpdateForm.getBirthDate();
		String position = userUpdateForm.getPassword();

		Timestamp now = new Timestamp(System.currentTimeMillis());

		// 暗号化されたパスワードとパスワード(確認)を生成
		String encPass = null;
		try {
			encPass = UtilLogic.encrpt(password);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		if (password.length() == 0) {
			// ユーザー情報を更新
			userRepository.setFixedNameAndPositionAndBirthDateAndUpdateDateFor(name, position, Date.valueOf(birthDate), now, id);
		} else {
			// ユーザー情報を更新
			userRepository.setFixedNameAndPositionAndBirthDateAndPasswordAndUpdateDateFor(name, position, Date.valueOf(birthDate), encPass, now, id);
		}

		List<PositionMaster> positionList = positionRepository.findAll();
		// リクエストパラメーターを保存
		model.addAttribute("positionList", positionList);

		// HttpSessionインスタンスの取得
		User loginUser = (User) session.getAttribute("loginUser");

		if (loginUser.getId() == 1) {
			// UserListへリダイレクト
			return "redirect:/UserList";
		} else {
			// WorkSituationRegistrationへリダイレクト
			return "redirect:/WorkSituationRegistration";
		}

	}

}
