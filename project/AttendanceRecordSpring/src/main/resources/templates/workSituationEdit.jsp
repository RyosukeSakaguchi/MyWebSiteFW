<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.List"%>
<%@ page import="beans.UserBeans"%>
<%@ page import="beans.WorkSituationBeans"%>
<!DOCTYPE html>
<html class="no-js">

	<head>
		<!-- 共通header.jsp読み込み-->
		<jsp:include page="header.jsp" />
		<title>勤務状況編集</title>
		<!-- オリジナルCSS読み込み -->
		<link href="/AttendanceRecord/jsp/css/original/common.css" rel="stylesheet">
	</head>

	<body>
		<div class="fh5co-loader"></div>
		<div id="fh5co-page" style="width:95%">
			<ul class="pull-left left-menu">
				<br>	　　　　　<a class="btn btn-default" style="border: 2px solid black;" href="DailyWorkCheck?id=${userInfo.getId()}&year=${year}&month=${month}&date=${date}">戻る</a>
			</ul>
			<ul class="pull-right center-right">
				<br><a class="btn btn-default" style="border: 2px solid black;" href="Logout">ログアウト</a>
			</ul>
		</div>

		<section id="fh5co-projects">
			<div class="fh5co-overlay"></div>
			<div class="container" style="width: 90%;">
				<div align="center">
					<br><font size="5" color="red">${errMsg}</font>
				</div>
				<div style="text-align: center;">${year}年${month}月${date}日 ${name}</div>
				<br><br>
				<div class="fh5co-intro js-fullheight">
					<div class="fh5co-intro-text">
						<div class="fh5co-center-position">
							<div class="row">
								<div>
								<form action="WorkSituationEdit" method="post">
								<input type="hidden" value="${userInfo.getId()}" name="id" onblur="isEmpty(this)" >
								<input type="hidden" value="${year}" name="year" onblur="isEmpty(this)" >
								<input type="hidden" value="${month}" name="month" onblur="isEmpty(this)" >
								<input type="hidden" value="${date}" name="date" onblur="isEmpty(this)" >
									<table class="table table-striped">
										<thead>
											<tr>
												<th style="text-align: center;">勤務状況</th>
												<th style="text-align: center;">勤務開始時間</th>
												<th style="text-align: center;">勤務終了時間</th>
												<th style="text-align: center;">休憩時間</th>
												<th style="text-align: center;">勤務時間</th>
												<th style="text-align: center;">残業時間</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach var="obj" items="${workSituationList}" >
												<tr>
													<td style="text-align: center;">
														${obj.getWorkSitu()}
														<input type="hidden" value="${obj.getId()}" name="workSituationId" onblur="isEmpty(this)" >
													</td>
													<td >
														<input type="time" step="1" value="${obj.getWorkStart()}" class="form-control" id="inputName" name="workStart"  style="background: white; height: 35px; width: 150px; margin:auto;">
														<input type="hidden" value="${obj.getWorkStart()}" name="workStartBefore"  >
													</td>
													<td >
														<input type="time" step="1" value="${obj.getWorkEnd()}" class="form-control" id="inputName" name="workEnd"  style="background: white; height: 35px; width: 150px; margin:auto;">
														<input type="hidden" value="${obj.getWorkEnd()}" name="workEndBefore"  >
													</td>
													<td >
														<input type="time" step="1" value="${obj.getBreakTime()}" class="form-control" id="inputName" name="breakTime" style="background: white; height: 35px; width: 150px; margin:auto;">
														<input type="hidden" value="${obj.getBreakTime()}" name="breakTimeBefore" >
													</td>
													<td style="text-align: center;">
														${obj.getWorkTime()}
													</td>
													<td style="text-align: center;">
														${obj.getOvertime()}
													</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
									<div class=button_wrapper ><br><br><br><br>
										<button class="btn btn-info" type="submit" >登録</button>
									</div>
									</form>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</section>

		<!-- 共通footer.jsp読み込み-->
		<jsp:include page="footer.jsp" />

	</body>

</html>

