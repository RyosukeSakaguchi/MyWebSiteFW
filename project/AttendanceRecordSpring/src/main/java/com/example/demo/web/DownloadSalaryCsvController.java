package com.example.demo.web;

import java.util.ArrayList;
import java.util.List;

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
import com.example.demo.model.MonthlySalaryCsv;
import com.example.demo.model.User;
import com.example.demo.repository.SalaryRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WorkSituationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

@RequestMapping("/DownloadSalaryCsv.csv")
@Controller
public class DownloadSalaryCsvController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private WorkSituationRepository workSituationRepository;
	@Autowired
	private SalaryRepository salaryRepository;

	@GetMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
			+ "; charset=Shift_JIS; Content-Disposition: attachment")
	@ResponseBody
	public Object getCsv(@ModelAttribute DownloadCsvForm downloadCsvForm, Model model) throws JsonProcessingException {
		// リクエストスコープからパラメーターを取得
		String yearAndMonth = downloadCsvForm.getYearAndMonth();

		// 年と月が入力されていない時はUserListへリダイレクト
		if (yearAndMonth.replaceAll("-", "") == "") {
			model.addAttribute("salaryErrMsg", "入力に誤りがあります");

			// UserListへリダイレクト
			return "redirect:/UserList";
		}

		// ユーザーリストと年と月に対応する月給をcsvに出力
		int year = UtilLogic.yearAndMonthToYear(yearAndMonth);
		int month = UtilLogic.yearAndMonthToMonth(yearAndMonth);
		List<User> userList = new ArrayList<User>();
		userList = userRepository.findAll();
		userList = UtilLogic.userListSort(userList, workSituationRepository);
		List<MonthlySalaryCsv> monthlySalaryList = new ArrayList<MonthlySalaryCsv>();

		for (User user : userList) {
			monthlySalaryList.add(new MonthlySalaryCsv(user.getName(), UtilLogic.getMonthlySalary(user.getLoginId(),
					user.getPosition(), year, month, workSituationRepository, salaryRepository)));
		}

		CsvMapper mapper = new CsvMapper();
		CsvSchema schema = mapper.schemaFor(MonthlySalaryCsv.class).withHeader();
		return mapper.writer(schema).writeValueAsString(monthlySalaryList);
	}

}
