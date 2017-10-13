<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="beans.UserBeans"%>
<%@ page import="beans.WorkSituationBeans"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html>
<html class="no-js">

	<head>
		<!-- 共通header.jsp読み込み-->
		<jsp:include page="header.jsp" />
		<title>勤務情報消去</title>
		<!-- オリジナルCSS読み込み -->
		<link href="/AttendanceRecord/jsp/css/original/common.css" rel="stylesheet">
	</head>

	<body>
		<div class="fh5co-loader"></div>
		<section id="fh5co-hero" class="no-js-fullheight" style="background-image: url(/AttendanceRecord/jsp/images/full_image_2.jpg);" data-next="yes">
			<div class="fh5co-overlay"></div>
			<div class="container">
				<div class="fh5co-intro no-js-fullheight">
					<div class="fh5co-intro-text">
						<div class="fh5co-center-position">
							<h2 class="animate-box">
								<font size="5">${confMsg1}<font color="red">${confMsg2}</font>${confMsg3}</font>
							</h2><br><br>
							<p class="animate-box">
							<div class="wrapper">
								<div>
									<c:choose>
										<c:when test="${date == null}">
											<a href="MonthlyWorkCheck?id=${userInfo.getId()}&year=${year}&month=${month}" class="btn btn-outline "><i class="icon-play2"></i> Cancel</a>
										</c:when>
										<c:otherwise>
											<a href="DailyWorkCheck?id=${userInfo.getId()}&year=${year}&month=${month}&date=${date}" class="btn btn-outline "><i class="icon-play2"></i> Cancel</a>
										</c:otherwise>
									</c:choose>
								</div>
								<div>
									<form action="WorkSituationDelete" method="post">
										<input type="hidden" name="id" value="${userInfo.getId()}" >
										<input type="hidden" name="year" value="${year}" >
										<input type="hidden" name="month" value="${month}" >
										<input type="hidden" name="date" value="${date}" >
										<c:forEach var="obj" items="${workSituationList}">
											<input type="hidden" name="workSituationIdList[]" value="${obj.getId()}" >
										</c:forEach>
										<button class="btn btn-outline"><i class="icon-play2"></i> OK</button>
									</form>
								</div>
							</div>
							</p>
						</div>
					</div>
				</div>
			</div>
		</section>

		<!-- 共通footer.jsp読み込み-->
		<jsp:include page="footer.jsp" />

	</body>

</html>

