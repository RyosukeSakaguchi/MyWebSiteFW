package com.example.demo.web;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.form.WorkSituationDeleteForm;
import com.example.demo.model.User;
import com.example.demo.model.WorkSituation;
import com.example.demo.model.WorkSituationEdit;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WorkSituationEditRepository;
import com.example.demo.repository.WorkSituationRepository;

@RequestMapping("/WorkSituationDelete")
@Controller
public class WorkSituationDeleteController {

	@Autowired
	HttpSession session;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private WorkSituationRepository workSituationRepository;
	@Autowired
	private WorkSituationEditRepository workSituationEditRepository;

	@GetMapping
	public String get(@RequestParam int id, @RequestParam int year, @RequestParam int month, @RequestParam String date, Model model) {


		// HttpSessionインスタンスの取得
		User loginUser = (User) session.getAttribute("loginUser");
		// セッションにログイン情報があるかないかで分岐
		if (loginUser == null) {
			// LoginScreenへリダイレクト
			return "redirect:/LoginScreen";
		} else if (date.equals("nothing")) {

			// パラメータidに対応するUserBeans型のuserInfoインスタンスをリクエストスコープに保存
			User user = new User();
			user = userRepository.findByIdIs(id);
			model.addAttribute("user", user);

			// 勤務状況のリストを取得し、workSituationListインスタンスをリクエストスコープに保存
			String loginId = user.getLoginId();
			List<WorkSituation> workSituationList = new ArrayList<WorkSituation>();
			workSituationList = workSituationRepository.findByLoginIdIsAndCreateYearIsAndCreateMonthIs(loginId, year,
					month);
			model.addAttribute("workSituationList", workSituationList);

			// リクエストスコープにパラメータを保存
			model.addAttribute("id", id);
			model.addAttribute("year", year);
			model.addAttribute("month", month);
			model.addAttribute("name", user.getName());

			// 勤務状況のリストがない場合はエラーメッセージとともにMonthlyWorkCheckへフォワード
			if (workSituationList.size() == 0) {
				model.addAttribute("errMsg", "削除するデータがありません");

				// monthlyWorkCheck.htmlへフォワード
				return "monthlyWorkCheck";


			}

			// 削除確認メッセージをリクエストスコープに保存
			model.addAttribute("confMsg1", "本当に");
			model.addAttribute("confMsg2", year + "年" + month + "月");
			model.addAttribute("confMsg3", "のデータを全て削除してもよろしいでしょうか");

			// workSituationDelete.htmlへフォワード
			return "workSituationDelete";

		} else {

			// パラメータidに対応するUserBeans型のuserInfoインスタンスをリクエストスコープに保存
			User user = new User();
			user = userRepository.findByIdIs(id);
			model.addAttribute("user", user);

			// 勤務状況のリストを取得し、workSituationListインスタンスをリクエストスコープに保存
			String loginId = user.getLoginId();
			List<WorkSituation> workSituationList = new ArrayList<WorkSituation>();
			workSituationList = workSituationRepository
					.findByLoginIdIsAndCreateYearIsAndCreateMonthIsAndCreateDateIs(loginId, year, month, Integer.parseInt(date));
			model.addAttribute("workSituationList", workSituationList);

			// リクエストスコープにパラメータを保存
			model.addAttribute("id", id);
			model.addAttribute("year", year);
			model.addAttribute("month", month);
			model.addAttribute("date", Integer.parseInt(date));
			model.addAttribute("name", user.getName());

			// 勤務状況のリストがない場合はエラーメッセージとともにDailyWorkCheckへフォワード
			if (workSituationList.size() == 0) {
				model.addAttribute("errMsg", "削除するデータがありません");

				// dailyWorkCheck.htmlへフォワード
				return "dailyWorkCheck";
			}

			// 削除確認メッセージをリクエストスコープに保存
			model.addAttribute("confMsg1", "本当に");
			model.addAttribute("confMsg2", year + "年" + month + "月" + date + "日");
			model.addAttribute("confMsg3", "のデータを削除してもよろしいでしょうか");

			// workSituationDelete.htmlへフォワード
			return "workSituationDelete";

		}
	}

	@PostMapping
	public String post(@ModelAttribute WorkSituationDeleteForm workSituationDeleteForm, BindingResult result, Model model) {

		// ユーザーを探してuserInfoに代入
		User user = new User();
		user = userRepository.findByIdIs(workSituationDeleteForm.getId());
		Timestamp now = new Timestamp(System.currentTimeMillis());

		// リクエストパラメータがnullかそうでないかで分岐
		if (workSituationDeleteForm.getDate() == 0) {
			// 削除履歴を作成
			WorkSituationEdit workSituationEdit = new WorkSituationEdit();
			workSituationEdit.setLoginId(user.getLoginId());
			workSituationEdit.setEditTime(now);
			workSituationEdit.setEditContent(
					workSituationDeleteForm.getYear() + "年" + workSituationDeleteForm.getMonth() + "月の全勤務データを削除しました");
			workSituationEditRepository.save(workSituationEdit);

			// 勤務状況を削除
			for (int i = 0; i < workSituationDeleteForm.getWorkSituationIdList().length; i++) {
				WorkSituation WorkSituation = new WorkSituation();
				WorkSituation = workSituationRepository.findByIdIs(workSituationDeleteForm.getWorkSituationIdList()[i]);
				workSituationRepository.delete(WorkSituation);
			}
		} else {
			// 削除履歴を作成
			WorkSituationEdit workSituationEdit = new WorkSituationEdit();
			workSituationEdit.setLoginId(user.getLoginId());
			workSituationEdit.setEditTime(now);
			workSituationEdit.setEditContent(workSituationDeleteForm.getYear() + "年" + workSituationDeleteForm.getMonth() + "月" + workSituationDeleteForm.getDate() +  "日の勤務データを削除しました");
			workSituationEditRepository.save(workSituationEdit);

			// 勤務状況を削除
			for (int i = 0; i < workSituationDeleteForm.getWorkSituationIdList().length; i++) {
				WorkSituation WorkSituation = new WorkSituation();
				WorkSituation = workSituationRepository.findByIdIs(workSituationDeleteForm.getWorkSituationIdList()[i]);
				workSituationRepository.delete(WorkSituation);
			}
		}

		// リクエストスコープにパラメータを保存
		model.addAttribute("id", workSituationDeleteForm.getId());

		// UserListへリダイレクト
		return "redirect:/UserList";

	}

}
