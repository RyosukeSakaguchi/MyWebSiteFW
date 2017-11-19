package com.example.demo.common;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.example.demo.model.SalaryMaster;
import com.example.demo.model.User;
import com.example.demo.model.WorkSituation;
import com.example.demo.model.WorkSituationEdit;
import com.example.demo.repository.SalaryRepository;
import com.example.demo.repository.TimeRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WorkSituationEditRepository;
import com.example.demo.repository.WorkSituationRepository;

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
	public static int getMonthlySalary(String loginId, String position, int year, int month,
			WorkSituationRepository workSituationRepository, SalaryRepository salaryRepository) {
		List<WorkSituation> workSituationList = new ArrayList<WorkSituation>();
		workSituationList = workSituationRepository.findByLoginIdIsAndCreateYearIsAndCreateMonthIs(loginId, year,
				month);

		// 月の総勤務時間と総残業時間を変数に代入
		String totalWorkTime = totalWorkTime(workSituationList);
		String totalOvertime = totalOvertime(workSituationList);

		// それらをint型に直して(ex : 21:34:52 →
		// 213452)給与を計算。給料の計算の際はdouble型で行うが、給料は整数なのでint型にキャスト
		int totalWorkTimeInt = stringTimeToInt(totalWorkTime);
		int totalOvertimeInt = stringTimeToInt(totalOvertime);
		int diffTotalTimeInt = timeSubtraction(totalWorkTimeInt, totalOvertimeInt);
		int salary = (int) (calSalary(diffTotalTimeInt, position, salaryRepository)
				+ calOvertimeSalary(totalOvertimeInt, position, salaryRepository));

		return salary;
	}

	/**
	 * 総勤務時間から月の給料を計算
	 *
	 * @param timeInt
	 * @param position
	 * @return double
	 */
	public static double calSalary(int timeInt, String position, SalaryRepository salaryRepository) {

		// int型の6桁の変数の下2桁を取り出す。(ex : 093452 → 52)
		int underTwo = timeInt % 100;

		// int型の6桁の変数の真ん中の2桁*100を取り出す。(ex : 093452 → 3400)
		int middle = (timeInt - underTwo) % 10000;

		// int型の6桁の変数の真ん中の2桁*100から2桁取得。(ex : 3400 → 34)
		int middleTwo = middle / 100;

		// int型の6桁の変数の上2桁を取り出す。(ex : 093452 → 9)
		int topTwo = (timeInt - middle - underTwo) / 10000;

		// 給与マスターテーブルから受け取った役職に対するSalaryBeans型のsalaryInfoを取得
		SalaryMaster salaryInfo = salaryRepository.findByPositionIs(position);

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
	public static double calOvertimeSalary(int timeInt, String position, SalaryRepository salaryRepository) {

		// int型の6桁の変数の下2桁を取り出す。(ex : 093452 → 52)
		int underTwo = timeInt % 100;

		// int型の6桁の変数の真ん中の2桁*100を取り出す。(ex : 093452 → 3400)
		int middle = (timeInt - underTwo) % 10000;

		// int型の6桁の変数の真ん中の2桁*100から2桁取得。(ex : 3400 → 34)
		int middleTwo = middle / 100;

		// int型の6桁の変数の上2桁を取り出す。(ex : 093452 → 9)
		int topTwo = (timeInt - middle - underTwo) / 10000;

		// 給与マスターテーブルから受け取った役職に対するSalaryBeans型のsalaryInfoを取得
		SalaryMaster salaryInfo = salaryRepository.findByPositionIs(position);

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
		int totalPageNumber = 1;
		if (userNumber % userNumberPerPage == 0) {
			if(userNumber != 0) {
				totalPageNumber = userNumber / userNumberPerPage;
			}
		} else {
			totalPageNumber = userNumber / userNumberPerPage + 1;
		}
		return totalPageNumber;
	}

	public static boolean isWorking(String loginId, WorkSituationRepository workSituationRepository) {
		// 今日の日付をyyyy-MM-ddの形でtodayStringに代入
		Date today = new Date(System.currentTimeMillis());

		WorkSituation WorkSituation = workSituationRepository.findByLoginIdIsAndCreateDateIs(loginId, today);

		if (WorkSituation != null) {
			if (WorkSituation.getWorkSitu().length() == 2) {
				return true;
			}
		}

		return false;
	}

	public static void workStart(String loginId, Time workStartMaster,
			WorkSituationRepository workSituationRepository) {

		// 今日の日付を時間を取得
		Date today = new Date(Calendar.getInstance().getTimeInMillis());
		Time now = new Time(Calendar.getInstance().getTimeInMillis());

		// Time型の今の時刻nowと時間マスターテーブルの勤務開始時間workStartMasterをint型にする
		int nowInt = UtilLogic.timeToInt(now);
		int workStartMasterInt = UtilLogic.timeToInt(workStartMaster);

		// 現在の時刻と時間マスターテーブルの勤務開始時間を比べ遅刻したか、通常の出席で分岐
		String workSitu;
		if (nowInt > workStartMasterInt) {
			workSitu = "遅刻";
		} else {
			workSitu = "出席";
		}

		WorkSituation workSituation = new WorkSituation();
		workSituation.setLoginId(loginId);
		workSituation.setCreateDate(today);
		workSituation.setWorkSitu(workSitu);
		workSituation.setWorkStart(now);
		workSituation.setWorkEnd(Time.valueOf("00:00:00"));
		workSituation.setBreakTime(Time.valueOf("00:00:00"));
		workSituation.setWorkTime(Time.valueOf("00:00:00"));
		workSituation.setOvertime(Time.valueOf("00:00:00"));

		workSituationRepository.save(workSituation);

	}

	public static void workEnd(String loginId, Time breakTime, Time workEndMaster, Time workTimeMaster,
			WorkSituationRepository workSituationRepository) {
		// 今日の日付を時間を取得
		Date today = new Date(Calendar.getInstance().getTimeInMillis());
		Time now = new Time(Calendar.getInstance().getTimeInMillis());

		// 今日の日付と受け取ったログインIDに関する勤務開始時間をworkStartに代入
		Time workStart = workSituationRepository.findByLoginIdIsAndCreateDateIs(loginId, today).getWorkStart();

		int workTimeInt;
		if (breakTime == null) {
			// workTimeIntを計算
			workTimeInt = UtilLogic.timeSubtraction(UtilLogic.timeToInt(now), UtilLogic.timeToInt(workStart));
		} else {
			// workTimeIntを計算
			workTimeInt = UtilLogic.timeSubtraction(
					UtilLogic.timeSubtraction(UtilLogic.timeToInt(now), UtilLogic.timeToInt(workStart)),
					UtilLogic.timeToInt(breakTime));
		}

		// 勤務時間が時間マスターテーブルの勤務時間を超えていたら、残業時間を計算し代入

		int overtimeInt = 0;
		int workTimeMasterInt = UtilLogic.timeToInt(workTimeMaster);
		if (workTimeMasterInt < workTimeInt) {
			overtimeInt = UtilLogic.timeSubtraction(workTimeInt, workTimeMasterInt);
		}

		// 現在の時間と時間マスターの勤務終了時間をint型にする
		int nowInt = UtilLogic.timeToInt(now);
		int workEndMasterInt = UtilLogic.timeToInt(workEndMaster);

		// 今日の日付とログインIDを代入して、対応する勤務状況のリストに代入
		String yearAndMonthAndDate = new SimpleDateFormat("yyyy-MM-dd").format(today);
		List<WorkSituation> workSituationList = workSituationRepository
				.findByLoginIdIsAndCreateYearIsAndCreateMonthIsAndCreateDateIs(loginId,
						UtilLogic.yearAndMonthAndDateToYear(yearAndMonthAndDate),
						UtilLogic.yearAndMonthAndDateToMonth(yearAndMonthAndDate),
						UtilLogic.yearAndMonthAndDateToDate(yearAndMonthAndDate));

		// 勤務終了時間が時間マスターテーブルの勤務終了時間より、遅かったら帰宅、早かったら早退
		String workSitu = "";
		for (WorkSituation w : workSituationList) {
			workSitu = w.getWorkSitu();
		}
		if (nowInt >= workEndMasterInt) {
			workSitu = workSitu + " → 帰宅";
		} else {
			workSitu = workSitu + " → 早退";
		}

		WorkSituation workSituation = new WorkSituation();

		workSituation.setId(workSituationRepository.findByLoginIdIsAndCreateDateIs(loginId, today).getId());
		workSituation.setLoginId(loginId);
		workSituation.setCreateDate(today);
		workSituation.setWorkSitu(workSitu);
		workSituation
				.setWorkStart(workSituationRepository.findByLoginIdIsAndCreateDateIs(loginId, today).getWorkStart());
		workSituation.setWorkEnd(now);
		workSituation.setBreakTime(breakTime);
		workSituation.setWorkTime(Time.valueOf(UtilLogic.intToStringTime(workTimeInt)));
		workSituation.setOvertime(Time.valueOf(UtilLogic.intToStringTime(overtimeInt)));
		workSituationRepository.save(workSituation);

	}

	public static boolean workSituationEdit(int workSituationId, String workStart, String workEnd, String breakTime,
			WorkSituationRepository workSituationRepository, TimeRepository timeRepository) {

		boolean result = true;

		// 勤務開始時間、終了時間と時間マスターの開始時間、終了時間をint型にする(ex : 09:53:23→95323)
		int workStartInt = UtilLogic.stringTimeToInt(workStart);
		int workEndInt = UtilLogic.stringTimeToInt(workEnd);
		Time workStartMaster = timeRepository.findByIdIs(1).getWorkStart();
		int workStartMasterInt = UtilLogic.timeToInt(workStartMaster);
		Time workEndMaster = timeRepository.findByIdIs(1).getWorkEnd();
		int workEndMasterInt = UtilLogic.timeToInt(workEndMaster);

		// 初めは勤務終了時間が入力されていない段階での編集では、開始時間がマスターの開始時間より早いかそうでないかで分岐
		// 勤務終了時間が入力されている段階での編集でも、開始時間がマスターの開始時間より早いかそうでないかで分岐
		String workSitu;
		if (workEndInt == 0) {
			if (workStartInt > workStartMasterInt) {
				workSitu = "遅刻";
			} else {
				workSitu = "出席";
			}
		} else {
			if (workStartInt > workStartMasterInt) {
				if (workEndInt >= workEndMasterInt) {
					workSitu = "遅刻 → 退社";
				} else {
					workSitu = "遅刻 → 早退";
				}
			} else {
				if (workEndInt >= workEndMasterInt) {
					workSitu = "出席 → 退社";
				} else {
					workSitu = "出席 → 早退";
				}
			}
		}

		// 勤務終了時間が記入されているか、いないかで分岐
		int workTimeInt = 0;
		int overtimeInt = 0;
		if (UtilLogic.stringTimeToInt(workEnd) != 0) {

			// 勤務時間をint型で時間計算(ex : 194323-094212-10000→090111)
			workTimeInt = UtilLogic.timeSubtraction(
					UtilLogic.timeSubtraction(UtilLogic.stringTimeToInt(workEnd), workStartInt),
					UtilLogic.stringTimeToInt(breakTime));

			// int型の勤務時間が負の時は入力に誤りがあるのでfalseをreturn
			if (workTimeInt < 0) {
				result = false;
				return result;
			}

			// 時間マスターから勤務時間を取得し、その勤務時間より時間が長かったらその差分を残業時間に代入
			Time workTimeMaster = timeRepository.findByIdIs(1).getWorkTime();
			int workTimeMasterInt = UtilLogic.timeToInt(workTimeMaster);
			if (workTimeMasterInt < workTimeInt) {
				overtimeInt = UtilLogic.timeSubtraction(workTimeInt, workTimeMasterInt);
			}
		}

		WorkSituation workSituation = new WorkSituation();
		workSituation.setId(workSituationId);
		workSituation.setLoginId(workSituationRepository.findByIdIs(workSituationId).getLoginId());
		workSituation.setCreateDate(workSituationRepository.findByIdIs(workSituationId).getCreateDate());
		workSituation.setWorkSitu(workSitu);
		workSituation.setWorkStart(Time.valueOf(workStart));
		workSituation.setWorkEnd(Time.valueOf(workEnd));
		workSituation.setBreakTime(Time.valueOf(breakTime));
		workSituation.setWorkTime(Time.valueOf(UtilLogic.intToStringTime(workTimeInt)));
		workSituation.setOvertime(Time.valueOf(UtilLogic.intToStringTime(overtimeInt)));

		workSituationRepository.save(workSituation);

		return result;
	}

	public static void setEditHistory(String loginId, String time, String timeBefore, int createDateYear,
			int createDateMonth, int createDateDate, WorkSituationEditRepository workSituationEditRepository,
			String timeName) {

		// テーブルへの挿入は時間を変更した時のみ行う
		if (!time.equals(timeBefore)) {

			// 今日の日付と時間を"yyy-MM-dd HH:mm:ss"の形でtodayStringに代入
			Date today = new Date(System.currentTimeMillis());
			SimpleDateFormat d = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
			String todayString = d.format(today);

			WorkSituationEdit workSituationEdit = new WorkSituationEdit();

			workSituationEdit.setLoginId(loginId);
			workSituationEdit.setEditTime(Timestamp.valueOf(todayString));
			workSituationEdit.setEditContent(createDateYear + "年" + createDateMonth + "月" + createDateDate + "日の"
					+ UtilLogic.timeNameJa(timeName) + "を" + timeBefore + "から" + time + "に変更しました。");

			workSituationEditRepository.save(workSituationEdit);

		}
	}

	public static List<User> search(String loginId, String name, String position, Date birthDateFrom, Date birthDateTo,
			UserRepository userRepository) {
		List<User> userList = userRepository.findAll();
		return userList;
	}

	/**
	 * 指定文字がログインIDと一致するユーザーを検索する。
	 */
	public static Specification<User> loginIdIs(String loginId) {
		return StringUtils.isEmpty(loginId) ? null : (root, query, cb) -> {
			return cb.equal(root.get("loginId"), loginId);
		};
	}

	/**
	 * 指定文字をユーザー名に含むユーザーを検索する。
	 */
	public static Specification<User> nameContains(String name) {
		return StringUtils.isEmpty(name) ? null : (root, query, cb) -> {
			return cb.like(root.get("name"), "%" + name + "%");
		};
	}

	/**
	 * 指定文字が役職と一致するユーザーを検索する。
	 */
	public static Specification<User> positionIs(String position) {
		return StringUtils.isEmpty(position) ? null : (root, query, cb) -> {
			return cb.equal(root.get("position"), position);
		};
	}

	/**
	 * 指定文字が役職と一致するユーザーを検索する。
	 */
	public static Specification<User> birthDateBetween(String birthDateFrom, String birthDateTo) {
		return StringUtils.isEmpty(birthDateFrom) ? null : (root, query, cb) -> {
			return cb.between(root.get("position"), birthDateFrom, birthDateTo);
		};
	}


}
