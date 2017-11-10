package com.example.demo.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.common.CsvFileWrite;
import com.example.demo.common.UtilLogic;
import com.example.demo.form.DownloadCsvForm;
import com.example.demo.model.User;
import com.example.demo.model.WorkSituation;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WorkSituationRepository;

import attendanceRecord.UserList;
import dao.WorkSituationDao;

@RequestMapping("/DownloadCsv")
@Controller
public class DownloadCsvController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private WorkSituationRepository workSituationRepository;

	@PostMapping
	public String post(@ModelAttribute DownloadCsvForm downloadCsvForm ,Model model) {

		// リクエストスコープからパラメーターを取得
		String yearAndMonth = downloadCsvForm.getYearAndMonth();
		int date = downloadCsvForm.getDate();

		// yearAndMonthがnullかそうでないかで分岐し、さらにdateStringがnullかそうでないかで分岐
		if (yearAndMonth != null) {

			// 年と月が入力されていない時はUserListへリダイレクト
			if (yearAndMonth.replaceAll("-", "") == "") {
				model.addAttribute("salaryErrMsg", "入力に誤りがあります");

				// UserListへリダイレクト
				UserList userList = new UserList();
				userList.doGet(request, response);
				return;
			}

			// ユーザーリストと年と月に対応する月給をcsvに出力
			int year = UtilLogic.yearAndMonthToYear(yearAndMonth);
			int month = UtilLogic.yearAndMonthToMonth(yearAndMonth);
			List<User> userList = new ArrayList<User>();
			userList = userRepository.findAll();
			CsvFileWrite.getSalary(response, userList, year, month);

		} else if (String.valueOf(date) == "") {
			// リクエストスコープからパラメーターを取得
			int id = downloadCsvForm.getId();
			int year = downloadCsvForm.getYear();
			int month = downloadCsvForm.getMonth();

			// パラメータidに対応するworkSituationListインスタンスをリクエストスコープに保存
			User user = new User();
			user = userRepository.findByIdIs(id);
			List<WorkSituation> workSituationList = new ArrayList<WorkSituation>();
			workSituationList = WorkSituationDao.findAll(user.getLoginId(), year, month);
			model.addAttribute("workSituationList", workSituationList);

			// ユーザーの総勤務時間と総残業時間を取得
			String titalWorkTime = UtilLogic.totalWorkTime(workSituationList);
			String titalOvertime = UtilLogic.totalOvertime(workSituationList);

			// 月の勤務状況をcsvに出力
			CsvFileWrite.getMonthlyWorkSituation(response, workSituationList, user.getName(), year, month,
					titalWorkTime, titalOvertime);

		} else {
			// リクエストスコープからパラメーターを取得
			int id = downloadCsvForm.getId();
			int year = downloadCsvForm.getYear();
			int month = downloadCsvForm.getMonth();

			// パラメータidに対応するworkSituationListインスタンスをリクエストスコープに保存
			User user = new User();
			user = userRepository.findByIdIs(id);
			List<WorkSituation> workSituationList = new ArrayList<WorkSituation>();
			workSituationList = workSituationRepository.findByLoginIdIsAndCreateYearIsAndCreateMonthIsAndCreateDateIs(user.getLoginId(), year, month, date);
			model.addAttribute("workSituationList", workSituationList);

			// 日の勤務状況をcsvに出力
			CsvFileWrite.getDailyWorkSituation(response, workSituationList, user.getName(), year, month, date);
		}
	}

}
