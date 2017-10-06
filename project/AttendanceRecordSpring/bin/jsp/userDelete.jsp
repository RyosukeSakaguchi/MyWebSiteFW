<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="beans.UserBeans"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html class="no-js">

	<head>
		<!-- 共通header.jsp読み込み-->
		<jsp:include page="header.jsp" />
		<title>ユーザー情報消去</title>
		<!-- オリジナルCSS読み込み -->
		<link href="/AttendanceRecord/jsp/css/original/common.css" rel="stylesheet">
	</head>

	<body>
		<section id="fh5co-hero" class="no-js-fullheight" style="background-image: url(/AttendanceRecord/jsp/images/full_image_2.jpg);" data-next="yes">
			<div class="container">
				<div class="fh5co-intro no-js-fullheight">
					<div class="fh5co-intro-text">
						<div class="fh5co-center-position">
							<c:choose>
								<c:when test="${userInfo != null}">
									<h2 class="animate-box">
										<font size="5">本当にログインID&nbsp;:&nbsp;<font color="red">${ userInfo.getLoginId()}</font>を削除してもよろしいでしょうか。</font>
									</h2>
								</c:when>
								<c:when test="${userList != null}">
									<div class="animate-box" style="color:white;">
										<font size="5">本当にログインID&nbsp;:&nbsp;
											<c:forEach var="obj" items="${userList}" varStatus="status" >
												<c:choose>
													<c:when test="${status.index == userList.size() - 1}">
														<font color="red">${obj.getLoginId()}</font>
													</c:when>
													<c:otherwise>
														<font color="red">${obj.getLoginId()}</font>,
													</c:otherwise>
												</c:choose>
											</c:forEach>
											 を削除してもよろしいでしょうか。
										</font>
									</dix>
								</c:when>
								<c:otherwise>
									<h2 class="animate-box">
										<font size="5">本当に<font color="red">全ユーザー</font>を削除してもよろしいでしょうか。</font>
									</h2>
								</c:otherwise>
							</c:choose><br><br>
							<p class="animate-box">
								<div class="wrapper">
									<div>
										<a href="UserList" class="btn btn-outline "><i class="icon-play2"></i> Cancel</a>
									</div>
									<div>
										<form action="UserDelete" method="post">
											<c:choose>
												<c:when test="${userInfo != null}">
													<input type="hidden" value="${ userInfo.getId()}" name="id">
													</c:when>
												<c:when test="${userList != null}">
													<c:forEach var="obj" items="${userList}" varStatus="status" >
														<input type="hidden" value="${obj.getId()}" name="idList[]">
													</c:forEach>
												</c:when>
											</c:choose>
											<button  class="btn btn-outline" type="submit"><i class="icon-play2"></i> OK</button>
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

