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

import com.example.demo.common.UtilLogic;
import com.example.demo.form.MonthlyWorkCheckForm;
import com.example.demo.model.User;
import com.example.demo.model.WorkSituation;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WorkSituationRepository;

@RequestMapping("/MonthlyWorkCheck")
@Controller
public class MonthlyWorkCheckController {

	@Autowired
	HttpSession session;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private WorkSituationRepository workSituationRepository;

	@GetMapping
	public String get(@ModelAttribute MonthlyWorkCheckForm monthlyWorkCheckForm, Model model) {
		// HttpSessionインスタンスの取得
		User loginUser = (User) session.getAttribute("loginUser");
		// セッションにログイン情報があるかないかで分岐
		if (loginUser == null) {
			// LoginScreenへリダイレクト
			return "redirect:/LoginScreen";
		} else {

			// パラメータidに対応するUserBeans型のuserInfoインスタンスとユーザーの名前をリクエストスコープに保存
			User user = new User();
			user = userRepository.findByIdIs(monthlyWorkCheckForm.getId());
			model.addAttribute("user", user);
			model.addAttribute("name", user.getName());

			// 勤務状況のリストを取得し、workSituationListインスタンスをリクエストスコープに保存
			String loginId = user.getLoginId();
			List<WorkSituation> workSituationList = new ArrayList<WorkSituation>();
			workSituationList = workSituationRepository.findByLoginIdIsAndCreateYearIsAndCreateMonthIs(loginId, monthlyWorkCheckForm.getYear(), monthlyWorkCheckForm.getMonth());
			model.addAttribute("workSituationList", workSituationList);

			// ユーザーの総勤務時間と総残業時間を取得し、リクエストスコープに保存
			String titalWorkTime = UtilLogic.totalWorkTime(workSituationList);
			String titalOvertime = UtilLogic.totalOvertime(workSituationList);
			model.addAttribute("titalWorkTime", titalWorkTime);
			model.addAttribute("titalOvertime", titalOvertime);

			// リクエストスコープにパラメータを保存
			model.addAttribute("year", monthlyWorkCheckForm.getYear());
			model.addAttribute("month", monthlyWorkCheckForm.getMonth());

			//  monthlyWorkCheck.htmlへフォワード
			return "monthlyWorkCheck";

		}

	}

}
