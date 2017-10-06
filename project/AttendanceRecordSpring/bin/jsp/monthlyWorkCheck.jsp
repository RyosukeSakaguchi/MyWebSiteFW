<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="beans.UserBeans"%>
<%@ page import="beans.WorkSituationBeans"%>
<%@ page import="java.util.List"%>
<%@ page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html>
<html class="no-js">

	<head>
		<!-- 共通header.jsp読み込み-->
		<jsp:include page="header.jsp" />
		<title>月別勤務状況</title>
		<!-- オリジナルCSS読み込み -->
		<link href="/AttendanceRecord/jsp/css/original/common.css" rel="stylesheet">
	</head>

	<body>
		<div id="fh5co-page" style="width:95%">
			<ul class="pull-left left-menu">
				<br>　　　　　<a class="btn btn-default" style="border: 2px solid black;" href="UserDetail?id=${userInfo.getId()}">戻る</a>
			</ul>
			<ul class="pull-right center-right">
				<br><a class="btn btn-default" style="border: 2px solid black;" href="Logout">ログアウト</a>
			</ul>
		</div>

		<section id="fh5co-projects">
			<div class="fh5co-overlay"></div><br>
			<div class="container" style="width: 100%;">
				<div style="text-align: center;">${year}年${month}月 ${name}</div><br>
					<div align="center">
						<br><font size="5" color="red">${errMsg}</font><br><br>
					</div>
				<div class="fh5co-intro js-fullheight">
					<div class="fh5co-intro-text">
						<div class="fh5co-center-position">
							<div class="container" style="width: 90%;">
								<div class="row">
									<div>
										<div class="wrapper">
											<div>
												<a class="btn btn-danger" href="WorkSituationDelete?id=${userInfo.getId()}&year=${year}&month=${month}">All Delete</a>
											</div>
											<div>
												<form action="OutputCSV" method="post">
													<button class="btn btn-warning" type="submit">csvに出力</button>
													<input type="hidden" value="${userInfo.getId()}" name="id">
													<input type="hidden" value="${year}" name="year">
													<input type="hidden" value="${month}" name="month">
												</form>
											</div>
										</div><br>
										<table class="table table-striped">
											<thead>
												<tr>
													<th style="text-align: center;">day</th>
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
														<td style="text-align: center;"><c:out value="${obj.getFormatCreateDate()}"/></td>
														<td style="text-align: center;"><c:out value="${obj.getWorkSitu()}"/></td>
														<td style="text-align: center;"><c:out value="${obj.getWorkStart()}"/></td>
														<td style="text-align: center;"><c:out value="${obj.getWorkEnd()}"/></td>
														<td style="text-align: center;"><c:out value="${obj.getBreakTime()}"/></td>
														<td style="text-align: center;"><c:out value="${obj.getWorkTime()}"/></td>
														<td style="text-align: center;"><c:out value="${obj.getOvertime()}"/></td>
													</tr>
												</c:forEach>
												<tr>
													<td></td>
													<td></td>
													<td></td>
													<td></td>
													<td></td>
													<td style="text-align: center;">総勤務時間 ${titalWorkTime}</td>
													<td style="text-align: center;">総残業時間 ${titalOvertime}</td>
												</tr>
											</tbody>
										</table>
									</div>
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

