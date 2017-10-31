package com.example.demo.common;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import com.example.demo.model.SalaryMaster;
import com.example.demo.model.User;
import com.example.demo.model.WorkSituation;
import com.example.demo.repository.WorkSituationRepository;

import dao.SalaryMasterDao;
import dao.WorkSituationDao;

/**
 * @author sakaguchimikiya
 *
 */
/**
 * @author sakaguchimikiya
 *
 */
@Component
@ComponentScan("common")
public class UtilLogic {

	/**
	 * パスワードを暗号化するメソッド
	 *
	 * @param password
	 * @return 暗号化されたパスワード
	 * @throws NoSuchAlgorithmException
	 */
	public static String encrpt(String password) throws NoSuchAlgorithmException {
		// ハッシュを生成したい元の文字列
		String source = password;
		// ハッシュ生成前にバイト配列に置き換える際のCharset
		Charset charset = StandardCharsets.UTF_8;
		// ハッシュアルゴリズム
		String algorithm = "MD5";

		// ハッシュ生成処理
		byte[] bytes = MessageDigest.getInstance(algorithm).digest(source.getBytes(charset));
		String result = DatatypeConverter.printHexBinary(bytes);

		return result;
	}

	/**
	 * + * 渡された文字列がnullまたは空文字だった場合trueを返す * @param str * @return boolean
	 */
	public static boolean isEmpty(String str) {
		if (null == str || str == "") {
			return true;
		}
		return false;
	}

	/**
	 * Time型の変数(HH:mm:ss)を受け取り、int型(HHmmss)を返す
	 *
	 * @param time
	 * @return int
	 */
	public static int timeToInt(Time time) {
		return Integer.parseInt(time.toString().replaceAll(":", ""));
	}

	/**
	 * String型の変数(HH:mm:ss)を受け取り、int型(HHmmss)を返す
	 *
	 * @param timeString
	 * @return int
	 */
	public static int stringTimeToInt(String timeString) {
		return Integer.parseInt(timeString.replaceAll(":", ""));
	}

	/**
	 * int型の変数(HHmmss)を受け取り、String型(HH:mm:ss)で返す
	 *
	 * @param timeInt
	 * @return String
	 */
	public static String intToStringTime(int timeInt) {
		// int型からString型に変更
		String timeString = String.valueOf(timeInt);

		// timeStringの桁数が6桁になるまで、0を先頭につける(ex : 51234 → 051234)
		while (true) {
			if (timeString.length() == 6) {
				break;
			}
			timeString = "0" + timeString;
		}

		// StringBuilder型で、int型の変数(HHmmss)に「:」を挿入し、(HH:mm:ss)という形にする
		StringBuilder timeStringBuilder = new StringBuilder(timeString);
		timeStringBuilder.insert(2, ":");
		timeStringBuilder.insert(5, ":");

		return timeStringBuilder.toString();
	}

	/**
	 * 2つのint型の変数を時間的な引き算をする。(ex : 182312 - 073452 → 104820)
	 *
	 * @param timeInt1
	 * @param timeInt2
	 * @return int
	 */
	public static int timeSubtraction(int timeInt1, int timeInt2) {

		// int型の6桁の変数の下2桁を取り出す。(ex : 093452 → 52)
		int underTwo1 = timeInt1 % 100;
		int underTwo2 = timeInt2 % 100;

		// int型の6桁の変数の真ん中の2桁*100を取り出す。(ex : 093452 → 3400)
		int middle1 = (timeInt1 - underTwo1) % 10000;
		int middle2 = (timeInt2 - underTwo2) % 10000;

		// 時間の計算における補正を行う。(ex : 182312 - 073452 → 104820)
		if (underTwo1 < underTwo2 && middle1 < middle2) {
			return timeInt1 - timeInt2 - 4040;
		} else if (middle1 < middle2) {
			return timeInt1 - timeInt2 - 4000;
		} else if (underTwo1 < underTwo2) {
			return timeInt1 - timeInt2 - 40;
		} else {
			return timeInt1 - timeInt2;
		}
	}

	/**
	 * 2つのint型の変数を時間的な足し算をする。(ex : 184212 + 073452 → 261704)
	 *
	 * @param timeInt1
	 * @param timeInt2
	 * @return int
	 */
	public static int timeAddition(int timeInt1, int timeInt2) {

		// int型の6桁の変数の下2桁を取り出す。(ex : 093452 → 52)
		int underTwo1 = timeInt1 % 100;
		int underTwo2 = timeInt2 % 100;

		// int型の6桁の変数の真ん中の2桁*100を取り出す。(ex : 093452 → 3400)
		int middle1 = (timeInt1 - underTwo1) % 10000;
		int middle2 = (timeInt2 - underTwo2) % 10000;

		// 時間の計算における補正を行う。(ex : 184212 + 073452 → 261704)
		if (underTwo1 + underTwo2 >= 60 && middle1 + middle2 >= 5900) {
			return timeInt1 + timeInt2 + 4040;
		} else if (middle1 + middle2 >= 6000) {
			return timeInt1 + timeInt2 + 4000;
		} else if (underTwo1 + underTwo2 >= 60) {
			return timeInt1 + timeInt2 + 40;
		} else {
			return timeInt1 + timeInt2;
		}
	}

	/**
	 * 月の総勤務時間を計算し、時間をString型で返す(HH:mm:ss)
	 *
	 * @param workSituationList
	 * @return String
	 */
	public static String totalWorkTime(List<WorkSituation> workSituationList) {
		int totalIntTime = 0;

		// workSituationListの数だけfor文を回し、時間の足し算を行うtimeAdditionを用いて勤務時間の足し算を行う
		for (WorkSituation workSituation : workSituationList) {
			int WorkTimeInt = timeToInt(workSituation.getWorkTime());
			totalIntTime = timeAddition(totalIntTime, WorkTimeInt);
		}
		return intToStringTime(totalIntTime);
	}

	/**
	 * 月の総残業時間を計算し、時間をString型で返す(HH:mm:ss)
	 *
	 * @param workSituationList
	 * @return Stirng
	 */
	public static String totalOvertime(List<WorkSituation> workSituationList) {
		int totalIntTime = 0;

		// workSituationListの数だけfor文を回し、時間の足し算を行うtimeAdditionを用いて残業時間の足し算を行う
		for (WorkSituation workSituation : workSituationList) {
			int OvertimeInt = timeToInt(workSituation.getOvertime());
			totalIntTime = timeAddition(totalIntTime, OvertimeInt);
		}
		return intToStringTime(totalIntTime);
	}

	/**
	 * SQL文のカラム名を受け取り、それに対応する日本語名を返す
	 *
	 * @param timeName
	 * @return String
	 */
	public static String timeNameJa(String timeName) {

		// 受け取ったカラム名で分岐
		if (timeName.equals("work_start")) {
			return "勤務開始時間";
		} else if (timeName.equals("work_end")) {
			return "勤務終了時間";
		} else if (timeName.equals("break_time")) {
			return "休憩時間";
		}
		return null;
	}

	/**
	 * ユーザーの月の給与を計算する
	 *
	 * @param loginId
	 * @param position
	 * @param year
	 * @param month
	 * @return int
	 */
	public static int getMonthlySalary(String loginId, String position, int year, int month) {
		List<WorkSituation> workSituationList = new ArrayList<WorkSituation>();
		workSituationList = WorkSituationDao.findAll(loginId, year, month);

		// 月の総勤務時間と総残業時間を変数に代入
		String totalWorkTime = totalWorkTime(workSituationList);
		String totalOvertime = totalOvertime(workSituationList);

		// それらをint型に直して(ex : 21:34:52 →
		// 213452)給与を計算。給料の計算の際はdouble型で行うが、給料は整数なのでint型にキャスト
		int totalWorkTimeInt = stringTimeToInt(totalWorkTime);
		int totalOvertimeInt = stringTimeToInt(totalOvertime);
		int diffTotalTimeInt = timeSubtraction(totalWorkTimeInt, totalOvertimeInt);
		int salary = (int) (calSalary(diffTotalTimeInt, position) + calOvertimeSalary(totalOvertimeInt, position));

		return salary;
	}

	/**
	 * 総勤務時間から月の給料を計算
	 *
	 * @param timeInt
	 * @param position
	 * @return double
	 */
	public static double calSalary(int timeInt, String position) {

		// int型の6桁の変数の下2桁を取り出す。(ex : 093452 → 52)
		int underTwo = timeInt % 100;

		// int型の6桁の変数の真ん中の2桁*100を取り出す。(ex : 093452 → 3400)
		int middle = (timeInt - underTwo) % 10000;

		// int型の6桁の変数の真ん中の2桁*100から2桁取得。(ex : 3400 → 34)
		int middleTwo = middle / 100;

		// int型の6桁の変数の上2桁を取り出す。(ex : 093452 → 9)
		int topTwo = (timeInt - middle - underTwo) / 10000;

		// 給与マスターテーブルから受け取った役職に対するSalaryBeans型のsalaryInfoを取得
		SalaryMaster salaryInfo = SalaryMasterDao.getSalaryInfo(position);

		// 受け取った時給salaryInfo.getHourlyWage()を用いて月給を計算し、return
		return (salaryInfo.getHourlyWage()) * (topTwo + middleTwo / 60.0 + underTwo / 3600.0);
	}

	/**
	 * 総残業時間から月の残業代を計算
	 *
	 * @param timeInt
	 * @param position
	 * @return double
	 */
	public static double calOvertimeSalary(int timeInt, String position) {

		// int型の6桁の変数の下2桁を取り出す。(ex : 093452 → 52)
		int underTwo = timeInt % 100;

		// int型の6桁の変数の真ん中の2桁*100を取り出す。(ex : 093452 → 3400)
		int middle = (timeInt - underTwo) % 10000;

		// int型の6桁の変数の真ん中の2桁*100から2桁取得。(ex : 3400 → 34)
		int middleTwo = middle / 100;

		// int型の6桁の変数の上2桁を取り出す。(ex : 093452 → 9)
		int topTwo = (timeInt - middle - underTwo) / 10000;

		// 給与マスターテーブルから受け取った役職に対するSalaryBeans型のsalaryInfoを取得
		SalaryMaster salaryInfo = SalaryMasterDao.getSalaryInfo(position);

		// 受け取った残業代salaryInfo.getOvertimeHourlyWage()を用いて月の残業代を計算し、return
		return (salaryInfo.getOvertimeHourlyWage()) * (topTwo + middleTwo / 60.0 + underTwo / 3600.0);
	}

	/**
	 * 年月(ex : 2017-09)を受け取り、年(ex : 2017)を返す
	 *
	 * @param yearAndMonth
	 * @return int
	 */
	public static int yearAndMonthToYear(String yearAndMonth) {

		// 年月(ex : 2017-09)から年(ex : 2017)を取り出す
		int yearAndMonthInt = Integer.parseInt(yearAndMonth.replaceAll("-", ""));
		int year = (yearAndMonthInt - yearAndMonthToMonth(yearAndMonth)) / 100;

		// 取り出した年(ex : 2017)を返す
		return year;
	}

	/**
	 * 年月(ex : 2017-09)を受け取り、月(ex : 9)を返す
	 *
	 * @param yearAndMonth
	 * @return int
	 */
	public static int yearAndMonthToMonth(String yearAndMonth) {

		// 年月(ex : 2017-09)から月(ex : 9)を取り出す
		int yearAndMonthInt = Integer.parseInt(yearAndMonth.replaceAll("-", ""));
		int month = yearAndMonthInt % 100;

		// 取り出した月(ex : 9)を返す
		return month;
	}

	/**
	 * 年月日(ex : 2017-09-28)を受け取り、年(ex : 2017)を返す
	 *
	 * @param yearAndMonthAndDate
	 * @return int
	 */
	public static int yearAndMonthAndDateToYear(String yearAndMonthAndDate) {

		// 年月日(ex : 2017-09-28)から年(ex : 2017)を取り出す
		int yearAndMonthAndDateInt = Integer.parseInt(yearAndMonthAndDate.replaceAll("-", ""));
		int year = (yearAndMonthAndDateInt - yearAndMonthAndDateToMonth(yearAndMonthAndDate)
				- yearAndMonthAndDateToDate(yearAndMonthAndDate)) / 10000;

		// 取り出した年(ex : 2017)を返す
		return year;
	}

	/**
	 * 年月日(ex : 2017-09-28)を受け取り、月(ex : 9)を返す
	 *
	 * @param yearAndMonthAndDate
	 * @return int
	 */
	public static int yearAndMonthAndDateToMonth(String yearAndMonthAndDate) {

		// 年月日(ex : 2017-09-28)から月(ex : 9)を取り出す
		int yearAndMonthAndDateInt = Integer.parseInt(yearAndMonthAndDate.replaceAll("-", ""));
		int month = ((yearAndMonthAndDateInt - yearAndMonthAndDateToDate(yearAndMonthAndDate)) % 10000) / 100;

		// 取り出した月(ex : 9)を返す
		return month;
	}

	/**
	 * 年月日(ex : 2017-09-28)を受け取り、日(ex : 28)を返す
	 *
	 * @param yearAndMonthAndDate
	 * @return int
	 */
	public static int yearAndMonthAndDateToDate(String yearAndMonthAndDate) {

		// 年月日(ex : 2017-09-28)から日(ex : 28)を取り出す
		int yearAndMonthAndDateInt = Integer.parseInt(yearAndMonthAndDate.replaceAll("-", ""));
		int date = yearAndMonthAndDateInt % 100;

		// 取り出した日(ex : 28)を返す
		return date;
	}

	/**
	 * 受け取ったユーザーリストで「勤務中のユーザー」を先に並べ、「帰宅しているユーザー」を後に並べかえ、 さらに管理者がリストに入っている場合は取り除く
	 *
	 * @param userList
	 * @return List<UserBeans>
	 */
	public static List<User> userListSort(List<User> userList, WorkSituationRepository workSituationRepository) {

		// UserBeans型のArrayListのインスタンスを生成
		ArrayList<User> users = new ArrayList<User>();

		// 今日の日付を"yyyy-MM-dd"というフォーマットでyearAndMonthAndDateに代入
		Date today = new Date(Calendar.getInstance().getTimeInMillis());

		// はじめに勤務中のユーザーをusersに加える
		for (User user : userList) {
			WorkSituation workSituation = workSituationRepository.findByLoginIdIsAndCreateDateIs(user.getLoginId(),
					today);
			if (workSituation != null) {
				if (workSituation.getWorkSitu().length() == 2) {
					users.add(user);
				}
			}
		}

		// 次に勤務中以外のユーザー(管理者を除く)をusersに加える
		for (User user1 : userList) {
			boolean result = true;
			for (User user2 : users) {
				if (user1.equals(user2)) {
					result = false;
				}
			}
			if (result && user1.getId() != 1) {
				users.add(user1);
			}
		}

		// ソートされたusersを返す
		return users;
	}

	/**
	 * ユーザーリストと1ページに掲載するのユーザーの数を受け取り、必要なページ数を返す
	 *
	 * @param userList
	 * @param userNumberPerPage
	 * @return int
	 */
	public static int getTotalPageNumber(List<User> userList, int userNumberPerPage) {

		// ユーザーの数をuserNumberに代入
		int userNumber = userList.size();

		// 必要なページ数を計算しreturn
		int totalPageNumber = 0;
		if (userNumber % userNumberPerPage == 0) {
			totalPageNumber = userNumber / userNumberPerPage;
		} else {
			totalPageNumber = userNumber / userNumberPerPage + 1;
		}
		return totalPageNumber;
	}

	public static boolean isWorking(String loginId, WorkSituationRepository workSituationRepository) {
		// 今日の日付をyyyy-MM-ddの形でtodayStringに代入
		Date today = new Date(System.currentTimeMillis());

		WorkSituation WorkSituation = workSituationRepository.findByLoginIdIsAndCreateDateIs(loginId, today);

		if(WorkSituation != null) {
			if(WorkSituation.getWorkSitu().length() == 2) {
				return true;
			}
		}

		return false;
	}


}
