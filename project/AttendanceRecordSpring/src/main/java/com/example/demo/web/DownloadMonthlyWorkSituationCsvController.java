package com.example.demo.web;

import java.text.SimpleDateFormat;
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

import com.example.demo.common.UtilLogic;
import com.example.demo.form.DownloadCsvForm;
import com.example.demo.model.MonthlyWorkSituationCsv;
import com.example.demo.model.User;
import com.example.demo.model.WorkSituation;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WorkSituationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

@RequestMapping("/DownloadMonthlyWorkSituationCsv.csv")
@Controller
public class DownloadMonthlyWorkSituationCsvController {

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
			int id = downloadCsvForm.getId();
			int year = downloadCsvForm.getYear();
			int month = downloadCsvForm.getMonth();

			// パラメータidに対応するworkSituationListインスタンスをリクエストスコープに保存
			User user = new User();
			user = userRepository.findByIdIs(id);
			List<WorkSituation> workSituationList = new ArrayList<WorkSituation>();
			workSituationList = workSituationRepository
					.findByLoginIdIsAndCreateYearIsAndCreateMonthIs(user.getLoginId(), year, month);
			model.addAttribute("workSituationList", workSituationList);

			// ユーザーの総勤務時間と総残業時間を取得
			String titalWorkTime = UtilLogic.totalWorkTime(workSituationList);
			String titalOvertime = UtilLogic.totalOvertime(workSituationList);

			List<MonthlyWorkSituationCsv> monthlyWorkSituationCsvList = new ArrayList<MonthlyWorkSituationCsv>();
			boolean result = true;
			for (WorkSituation workSituation : workSituationList) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd");
				int createDate = Integer.parseInt(sdf.format(workSituation.getCreateDate()));

				if (result) {
					monthlyWorkSituationCsvList.add(new MonthlyWorkSituationCsv(year + "年" + month + "月", String.valueOf(createDate),
							user.getName(), workSituation.getWorkSitu(), workSituation.getWorkStart(),
							workSituation.getWorkEnd(), workSituation.getBreakTime(), workSituation.getWorkTime(),
							workSituation.getOvertime()));
					result = false;
				} else {
					monthlyWorkSituationCsvList.add(new MonthlyWorkSituationCsv(String.valueOf(createDate), workSituation.getWorkSitu(),
							workSituation.getWorkStart(), workSituation.getWorkEnd(), workSituation.getBreakTime(),
							workSituation.getWorkTime(), workSituation.getOvertime()));
				}

			}
			monthlyWorkSituationCsvList.add(new MonthlyWorkSituationCsv(titalWorkTime, titalOvertime));

			// 月の勤務状況をcsvに出力
			CsvMapper mapper = new CsvMapper();
			CsvSchema schema = mapper.schemaFor(MonthlyWorkSituationCsv.class).withHeader();
			return mapper.writer(schema).writeValueAsString(monthlyWorkSituationCsvList);
		}

	}

}
