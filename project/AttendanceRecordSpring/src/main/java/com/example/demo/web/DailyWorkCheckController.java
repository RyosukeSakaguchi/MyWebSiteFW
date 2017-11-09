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

import com.example.demo.form.DailyWorkCheckForm;
import com.example.demo.model.User;
import com.example.demo.model.WorkSituation;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WorkSituationRepository;

@RequestMapping("/DailyWorkCheck")
@Controller
public class DailyWorkCheckController {

	@Autowired
	HttpSession session;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private WorkSituationRepository workSituationRepository;

	@GetMapping
	public String get(@ModelAttribute DailyWorkCheckForm dailyWorkCheckForm, Model model) {
		// HttpSessionインスタンスの取得
		User loginUser = (User) session.getAttribute("loginUser");
		// セッションにログイン情報があるかないかで分岐
		if (loginUser == null) {
			// LoginScreenへリダイレクト
			return "redirect:/LoginScreen";
		} else {

			// パラメータidに対応するUserBeans型のuserInfoインスタンスとユーザーの名前をリクエストスコープに保存
			User user = new User();
			user = userRepository.findByIdIs(dailyWorkCheckForm.getId());
			model.addAttribute("user", user);
			model.addAttribute("name", user.getName());


			// 勤務状況のリストを取得し、workSituationListインスタンスをリクエストスコープに保存
			String loginId = user.getLoginId();
			List<WorkSituation> workSituationList = new ArrayList<WorkSituation>();
			workSituationList = workSituationRepository.findByLoginIdIsAndCreateYearIsAndCreateMonthIsAndCreateDateIs(loginId, dailyWorkCheckForm.getYear(), dailyWorkCheckForm.getMonth(), dailyWorkCheckForm.getDate());
			model.addAttribute("workSituationList", workSituationList);


			// リクエストスコープに保存
			model.addAttribute("year", dailyWorkCheckForm.getYear());
			model.addAttribute("month", dailyWorkCheckForm.getMonth());
			model.addAttribute("date", dailyWorkCheckForm.getDate());

			// dailyWorkCheck.htmlへフォワード
			return "dailyWorkCheck";

		}
	}
}
