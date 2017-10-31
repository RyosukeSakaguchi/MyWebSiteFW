package com.example.demo.web;

import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import com.example.demo.common.UtilLogic;
import com.example.demo.form.SignUpForm;
import com.example.demo.model.PositionMaster;
import com.example.demo.model.User;
import com.example.demo.repository.PositionRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.validation.SignUpValidator;

@RequestMapping("/SignUp")
@Controller
public class SignUpController {
	@Autowired
	SignUpValidator signUpValidator;
	@Autowired
	HttpSession session;
	@Autowired
	private PositionRepository positionRepository;
	@Autowired
	private UserRepository userRepository;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(signUpValidator);
	}

	@GetMapping
	public String get(@ModelAttribute SignUpForm signUpForm, Model model) {
		// HttpSessionインスタンスの取得
		User loginUser = (User) session.getAttribute("loginUser");
		// セッションにログイン情報があるかないかで分岐
		if (loginUser == null) {
			// LoginScreenへリダイレクト
			return "redirect:/LoginScreen";
		} else if (loginUser.getId() != 1) {
			// WorkSituationRegistrationへリダイレクト
			return "redirect:/WorkSituationRegistration";
		} else {
			List<PositionMaster> positionList = positionRepository.findAll();
			// リクエストパラメーターを保存
			model.addAttribute(signUpForm);
			model.addAttribute("positionList", positionList);
			// signUp.htmlへフォワード
			return "signUp";
		}
	}

	@PostMapping
	public String post(@Validated @ModelAttribute SignUpForm signUpForm, BindingResult result, Model model) {

		if (result.hasErrors()) {
			return get(signUpForm, model);
		}

		// リクエストパラメータの取得
		String loginId = signUpForm.getLoginId();
		String password = signUpForm.getPassword();
		String name = signUpForm.getName();
		String birthDate = signUpForm.getBirthDate();
		String position = signUpForm.getPosition();

		// 現在時刻を読みやすい文字列形式でdateStrに代入
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = f1.format(date);

		// 暗号化されたパスワードとパスワード(確認)を生成
		String encPass = null;
		try {
			encPass = UtilLogic.encrpt(password);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		List<PositionMaster> positionList = positionRepository.findAll();
		// リクエストパラメーターを保存
		model.addAttribute("positonList", positionList);

		User user = new User();
		user.setLoginId(loginId);
		user.setPassword(encPass);
		user.setName(name);
		user.setBirthDate(Date.valueOf(birthDate));
		try {
			user.setCreateDate(new Timestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr).getTime()));
			user.setUpdateDate(new Timestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr).getTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		user.setPosition(position);

		userRepository.save(user);

		// UserListへリダイレクト
		return "redirect:/UserList";

	}

}
