package com.example.demo.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.form.DownloadCsvForm;
import com.example.demo.model.DailyWorkSituationCsv;
import com.example.demo.model.User;
import com.example.demo.model.WorkSituation;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WorkSituationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

@RequestMapping("/DownloadDailyWorkSituationCsv.csv")
@Controller
public class DownloadDailyWorkSituationCsvController {

	@Autowired
	HttpSession session;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private WorkSituationRepository workSituationRepository;

	@GetMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
			+ "; charset=Shift_JIS; Content-Disposition: attachment")
	@ResponseBody
	public Object getCsv(@ModelAttribute DownloadCsvForm downloadCsvForm, Model model) throws JsonProcessingException {
		// HttpSessionインスタンスの取得
		User loginUser = (User) session.getAttribute("loginUser");
		// セッションにログイン情報があるかないかで分岐
		if (loginUser == null) {
			// LoginScreenへリダイレクト
			return "redirect:/LoginScreen";
		} else {
			// リクエストスコープからパラメーターを取得
			int date = downloadCsvForm.getDate();
			int id = downloadCsvForm.getId();
			int year = downloadCsvForm.getYear();
			int month = downloadCsvForm.getMonth();

			// パラメータidに対応するworkSituationListインスタンスをリクエストスコープに保存
			User user = new User();
			user = userRepository.findByIdIs(id);
			List<WorkSituation> workSituationList = new ArrayList<WorkSituation>();
			workSituationList = workSituationRepository.findByLoginIdIsAndCreateYearIsAndCreateMonthIsAndCreateDateIs(
					user.getLoginId(), year, month, date);
			model.addAttribute("workSituationList", workSituationList);

			List<DailyWorkSituationCsv> dailyWorkSituationCsvList = new ArrayList<DailyWorkSituationCsv>();
			for (WorkSituation workSituation : workSituationList) {
				dailyWorkSituationCsvList.add(new DailyWorkSituationCsv(year + "年" + month + "月" + date + "日", user.getName(),
						workSituation.getWorkSitu(), workSituation.getWorkStart(), workSituation.getWorkEnd(),
						workSituation.getBreakTime(), workSituation.getWorkTime(), workSituation.getOvertime()));
			}

			// 日の勤務状況をcsvに出力
			CsvMapper mapper = new CsvMapper();
			CsvSchema schema = mapper.schemaFor(DailyWorkSituationCsv.class).withHeader();
			return mapper.writer(schema).writeValueAsString(dailyWorkSituationCsvList);

		}

	}

}
