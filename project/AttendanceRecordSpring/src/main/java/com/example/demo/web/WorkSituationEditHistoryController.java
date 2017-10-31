package com.example.demo.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.User;
import com.example.demo.model.WorkSituationEdit;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WorkSituationEditRepository;


@RequestMapping("/WorkSituationEditHistory")
@Controller
public class WorkSituationEditHistoryController {

	@Autowired
	HttpSession session;
	@Autowired
	private WorkSituationEditRepository workSituationEditRepository;
	@Autowired
	private UserRepository userRepository;

	@GetMapping
	public String get(@RequestParam int id, @RequestParam int disp, Model model) {

		// セッションにログイン情報があるかないかで分岐
		if ((User) session.getAttribute("loginUser") == null) {
			// LoginScreenへリダイレクト
			return "redirect:/LoginScreen";
		} else {

			// 5件表示なのか20件表示なのかはresultで区別し、resultをリクエストパラメータに保存
			boolean result = true;
			if (disp == 5) {
				result = false;
				model.addAttribute("dispMsg", "(最新5件)");
			} else {
				model.addAttribute("dispMsg", "(最新20件)");
			}
			model.addAttribute("result", result);

			// パラメータidに対応するUserBeans型のuserInfoインスタンスとユーザーの名前をリクエストスコープに保存
			User user = new User();
			user = userRepository.findByIdIs(id);
			model.addAttribute("user", user);
			model.addAttribute("name", user.getName());

			// 勤務状況のリストを取得し、workSituationListインスタンスをリクエストスコープに保存
			String loginId = user.getLoginId();
			List<WorkSituationEdit> workSituationEditList = new ArrayList<WorkSituationEdit>();
			workSituationEditList = workSituationEditRepository.findByLoginIdIs(loginId);
			model.addAttribute("workSituationEditList", workSituationEditList);

			//  workSituationEditHistory.htmlへフォワード
			return "workSituationEditHistory";

		}

	}

}
