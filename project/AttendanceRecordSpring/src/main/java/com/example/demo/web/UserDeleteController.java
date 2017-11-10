package com.example.demo.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.form.UserDeleteForm;
import com.example.demo.form.UserListForm;
import com.example.demo.model.User;
import com.example.demo.model.WorkSituation;
import com.example.demo.model.WorkSituationEdit;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WorkSituationEditRepository;
import com.example.demo.repository.WorkSituationRepository;

@RequestMapping("/UserDelete")
@Controller
public class UserDeleteController {

	@Autowired
	HttpSession session;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private WorkSituationRepository workSituationRepository;
	@Autowired
	private WorkSituationEditRepository workSituationEditRepository;

	@GetMapping
	public String get(@ModelAttribute UserListForm userListForm, @RequestParam String id, Model model) {
		// HttpSessionインスタンスの取得
		User loginUser = (User) session.getAttribute("loginUser");
		// セッションにログイン情報があるかないかで分岐
		if (loginUser == null) {
			// LoginScreenへリダイレクト
			return "redirect:/LoginScreen";
		} else if (id.equals("0")) {
			// リクエストスコープからパラメータを取得
			int delListId[] = userListForm.getDelListId();

			// パラメータがnullかそうでないかで分岐
			if (delListId == null) {
				// チェックをつけていないメッセージをリクエストスコープに保存
				model.addAttribute("noCheckMsg", "未選択です");

				// UserListへリダイレクト
				return "redirect:/UserList";

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

	@PostMapping
	public String post(@ModelAttribute UserDeleteForm userDeleteForm, Model model) {

		// リクエストスコープにパラメーターがあるかで分岐
		if (String.valueOf(userDeleteForm.getId()).length() == 0) {

			// パラメータがnullかそうでないかで分岐
			if (userDeleteForm.getIdList() != null) {

				// 消去するユーザーのidリストに対応する勤務状況と勤務状況編集履歴とユーザー情報を削除
				int[] idList = userDeleteForm.getIdList();
				for (int i = 0; i < idList.length; i++) {
					User user = new User();
					user = userRepository.findByIdIs(idList[i]);
//					WorkSituationDao.userSituDel(user.getLoginId());
//					WorkSituationEditDao.userSituEditDel(user.getLoginId());
//					UserInfoDao.userDel(idList[i]);

					// ユーザー消去成功のメッセージをリクエストスコープに保存
					model.addAttribute("sucMsg", "ユーザー情報の削除に成功しました");

				}
			} else {
				// // 全ての勤務状況と勤務状況編集履歴とユーザー情報を削除
				// WorkSituationDao.allUserSituDel();
				// WorkSituationEditDao.allUserSituEditDel();
				// UserInfoDao.allUserDel();

				// ユーザー消去成功のメッセージをリクエストスコープに保存
				model.addAttribute("sucMsg", "全ユーザー情報の削除に成功しました");
			}

			// UserListへリダイレクト
			return "redirect:/UserList";
		} else {


			// 消去するユーザーのidに対応する勤務状況と勤務状況編集履歴とユーザー情報を削除
			User user = new User();
			user = userRepository.findByIdIs(userDeleteForm.getId());

			List<WorkSituation> workSituationList = new ArrayList<WorkSituation>();
			workSituationList = workSituationRepository.findByLoginIdIs(user.getLoginId());
			for(WorkSituation workSituation : workSituationList) {
				workSituationRepository.delete(workSituation);
			}

			List<WorkSituationEdit> workSituationEditList = new ArrayList<WorkSituationEdit>();
			workSituationEditList = workSituationEditRepository.findByLoginIdIs(user.getLoginId());
			for(WorkSituationEdit workSituationEdit : workSituationEditList) {
				workSituationEditRepository.delete(workSituationEdit);
			}

			userRepository.delete(userRepository.findByIdIs(userDeleteForm.getId()));

			// ユーザー消去成功のメッセージをリクエストスコープに保存
			model.addAttribute("sucMsg", "ユーザー情報の削除に成功しました");

			// UserListへリダイレクト
			return "redirect:/UserList";

		}
	}

}
