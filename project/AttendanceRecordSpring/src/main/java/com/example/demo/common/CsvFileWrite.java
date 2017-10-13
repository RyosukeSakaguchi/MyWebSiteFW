package com.example.demo.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import beans.UserBeans;
import beans.WorkSituationBeans;

public class CsvFileWrite extends UtilLogic {

	/** 全ユーザーの月給をcsvファイルに出力する
	 * @param response
	 * @param userList
	 * @param year
	 * @param month
	 * @throws ServletException
	 * @throws IOException
	 */
	public static void getSalary(HttpServletResponse response, List<UserBeans> userList, int year, int month)
			throws ServletException, IOException {

		try {
			// ダウンロードファイル名を生成する
			String filename = year + "年" + month + "月.csv";

			// コンテキストにダウンロードファイル情報を設定する
			response.setContentType("application/csv;charset=Shift-JIS");
			response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));

			PrintWriter pw = response.getWriter();

			// 内容を指定する
			pw.print("ユーザー名");
			pw.print(",");
			pw.print("月給");
			pw.println();

			for (UserBeans user : userList) {
				if (user.getId() != 1) {
					pw.print(user.getName());
					pw.print(",");
					pw.print(getMonthlySalary(user.getLoginId(), user.getPosition(), year, month));
					pw.print("円");
					pw.println();
				}
			}
			pw.close();
		} catch (IOException ex) {
			// 例外時処理
			ex.printStackTrace();
		}
	}

	/** ユーザーの月の勤務状況をcsvファイルに出力
	 * @param response
	 * @param workSituationList
	 * @param userName
	 * @param year
	 * @param month
	 * @param titalWorkTime
	 * @param titalOvertime
	 * @throws ServletException
	 * @throws IOException
	 */
	public static void getMonthlyWorkSituation(HttpServletResponse response, List<WorkSituationBeans> workSituationList,
			String userName, int year, int month, String titalWorkTime, String titalOvertime)
			throws ServletException, IOException {

		try {
			// ダウンロードファイル名を生成する
			String filename = year + "年" + month + "月" + userName + ".csv";

			// コンテキストにダウンロードファイル情報を設定する
			response.setContentType("application/csv;charset=Shift-JIS");
			response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));

			PrintWriter pw = response.getWriter();

			// 内容を指定する
			pw.print(year + "年" + month + "月");
			pw.print(",");
			pw.print(userName);
			pw.println();

			pw.print("日付");
			pw.print(",");
			pw.print("勤務状況");
			pw.print(",");
			pw.print("勤務開始時間");
			pw.print(",");
			pw.print("勤務終了時間");
			pw.print(",");
			pw.print("休憩時間");
			pw.print(",");
			pw.print("勤務時間");
			pw.print(",");
			pw.print("残業時間");
			pw.println();

			for (WorkSituationBeans workSituation : workSituationList) {

				SimpleDateFormat sdf = new SimpleDateFormat("dd");
				String CreateDate = sdf.format(workSituation.getCreateDate());

				pw.print(CreateDate);
				pw.print(",");
				pw.print(workSituation.getWorkSitu());
				pw.print(",");
				pw.print(workSituation.getWorkStart());
				pw.print(",");
				pw.print(workSituation.getWorkEnd());
				pw.print(",");
				pw.print(workSituation.getBreakTime());
				pw.print(",");
				pw.print(workSituation.getWorkTime());
				pw.print(",");
				pw.print(workSituation.getOvertime());
				pw.println();
			}

			pw.print("総勤務時間");
			pw.print(",");
			pw.print(titalWorkTime);
			pw.println();

			pw.print("総残業時間");
			pw.print(",");
			pw.print(titalOvertime);
			pw.println();

			pw.close();
		} catch (IOException ex) {
			// 例外時処理
			ex.printStackTrace();
		}

	}

	/** ユーザーの日の勤務状況をcsvファイルに出力
	 * @param response
	 * @param workSituationList
	 * @param userName
	 * @param year
	 * @param month
	 * @param date
	 * @throws ServletException
	 * @throws IOException
	 */
	public static void getDailyWorkSituation(HttpServletResponse response, List<WorkSituationBeans> workSituationList,
			String userName, int year, int month, int date) throws ServletException, IOException {

		try {
			// ダウンロードファイル名を生成する
			String filename = year + "年" + month + "月" + date + "日" + userName + ".csv";

			// コンテキストにダウンロードファイル情報を設定する
			response.setContentType("application/csv;charset=Shift-JIS");
			response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));

			PrintWriter pw = response.getWriter();

			// 内容を指定する
			pw.print(year + "/" + month + "/" + date );
			pw.print(",");
			pw.print(userName);
			pw.println();

			pw.print("勤務状況");
			pw.print(",");
			pw.print("勤務開始時間");
			pw.print(",");
			pw.print("勤務終了時間");
			pw.print(",");
			pw.print("休憩時間");
			pw.print(",");
			pw.print("勤務時間");
			pw.print(",");
			pw.print("残業時間");
			pw.println();

			for (WorkSituationBeans workSituation : workSituationList) {

				pw.print(workSituation.getWorkSitu());
				pw.print(",");
				pw.print(workSituation.getWorkStart());
				pw.print(",");
				pw.print(workSituation.getWorkEnd());
				pw.print(",");
				pw.print(workSituation.getBreakTime());
				pw.print(",");
				pw.print(workSituation.getWorkTime());
				pw.print(",");
				pw.print(workSituation.getOvertime());
				pw.println();
			}
			// ファイルに書き出す
			pw.close();
		} catch (IOException ex) {
			// 例外時処理
			ex.printStackTrace();
		}
	}

}
