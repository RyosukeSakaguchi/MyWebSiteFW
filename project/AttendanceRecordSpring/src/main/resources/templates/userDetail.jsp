<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="beans.UserBeans"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html class="no-js">

	<head>
		<!-- 共通header.jsp読み込み-->
		<jsp:include page="header.jsp" />
		<title>ユーザー情報参照</title>
		<!-- オリジナルCSS読み込み -->
		<link href="/AttendanceRecord/jsp/css/original/common.css" rel="stylesheet">
		<!-- カレンダー  -->
		<link rel="stylesheet" type="text/css" href="/AttendanceRecord/jsp/css2/calendar.css">
		<link rel="stylesheet" type="text/css" href="/AttendanceRecord/jsp/css2/custom_2.css">
		<link rel="stylesheet" type="text/css" href="/AttendanceRecord/jsp/css2/demo.css">
	</head>

	<body>
		<!-- Loader -->
		<div id="fh5co-page">
			<section id="fh5co-header">
				<div class="container">
					<nav role="navigation">
						<ul class="pull-left left-menu">
							<li class="fh5co-cta-btn">
								<c:choose>
									<c:when test="${loginUser.getId() == 1}"><a href="UserList">戻る</a></c:when>
									<c:otherwise><a href="WorkSituationRegistration">戻る</a></c:otherwise>
								</c:choose>
								　　<a href="UserUpdate?id=${userInfo.getId()}">更新</a>
							</li>
						</ul>
					</nav>
				</div>
			</section>

			<section id="fh5co-hero" class="no-js-fullheight" style="background-image: url(/AttendanceRecord/jsp/images/full_image_2.jpg);" data-next="yes">
				<div class="container">
					<div class="fh5co-intro no-js-fullheight">
						<div class="fh5co-intro-text">
							<div class="wrapper2">
								<div class="fh5co-left-position"><br><br>
									<h2 class="animate-box">User Detail</h2>
								</div>
								<div class="animate-box" style="color:white">
									ログイン : ${userInfo.getLoginId()}<br>
									<br> 名前 : ${userInfo.getName()}<br>
									<br> 役職 : ${userInfo.getPosition()}<br>
									<br> 生年月日 : ${userInfo.getFormatBirthDate()}<br>
									<br> 登録日時 : ${userInfo.getFormatCreateDate()}<br>
									<br> 更新日時 : ${userInfo.getFormatUpdateDate()}<br>
									<br>
								</div>
							</div>
						</div>
					</div>
				</div>
			</section>
			<section id="fh5co-features">
				<div class="container">
					<form name="form1" action="WorkSituationEditHistory" method="get">
					<input type="hidden" name="id" value="${userInfo.getId()}">
					<div class="button_wrapper" style="text-align:left;">
						<button class="btn btn-default" style="border: 2px solid black;">勤務状況編集履歴</button>
					</div>
					</form>
					<div class="fh5co-center-position">
						<div class="fh5co-lead animate-box fadeInUp animated" aline="center">Calendar</div><br>
					</div>
					<div class="custom-calendar-wrap">
						<div id="custom-inner" class="custom-inner">
						<form action="DailyWorkCheck" method="get">
						<input type="hidden" name="id" value="${userInfo.getId()}">
							<div class="custom-header clearfix">
								<nav>
									<span id="custom-prev" class="custom-prev"></span>
									<span id="custom-next" class="custom-next"></span>
								</nav>
								<h2 id="custom-month" class="custom-month"></h2>
								<h3 id="custom-year" class="custom-year"></h3>
							</div>
							<div id="calendar" class="fc-calendar-container"></div>
							</form>
						</div>
					</div>
				</div>
			</section>
		</div>

		<!-- 共通footer.jsp読み込み-->
		<jsp:include page="footer.jsp" />

		<script type="text/javascript" src="/AttendanceRecord/jsp/js/jquery.calendario.js"></script>
		<script type="text/javascript" src="/AttendanceRecord/jsp/js/data.js"></script>
		<script type="text/javascript" src="/AttendanceRecord/jsp/js/calendario-click.js"></script>

	</body>

</html>

