<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="beans.UserBeans"%>
<%@ page import="beans.WorkSituationBeans"%>
<%@ page import="beans.WorkSituationEditBeans"%>
<%@ page import="java.util.List"%>
<%@ page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html>
<html class="no-js">

	<head>
		<!-- 共通header.jsp読み込み-->
		<jsp:include page="header.jsp" />
		<title>勤務状況編集履歴</title>
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
			<div class="fh5co-overlay"></div>
			<div class="container" style="width: 100%;">
				<div style="text-align: center;">${name}の編集履歴${dispMsg}</div><br><br>
				<div class="fh5co-intro js-fullheight">
					<div class="fh5co-intro-text">
						<div class="fh5co-center-position">
							<div class="container" style="width: 90%;">
								<div class="row">
									<div>
										<table class="table table-striped">
											<thead>
												<tr>
													<th style="text-align: center;">編集日時</th>
													<th style="text-align: center;">編集内容</th>
												</tr>
											</thead>
											<tbody>
												<c:choose>
													<c:when test="${result}">
														<c:choose>
															<c:when test="${workSituationEditList.size() < 20}">
																<c:forEach var="obj" items="${workSituationEditList}"  >
																	<tr>
																		<td style="text-align: center;">${obj.getFormatEditDate()}</td>
																		<td style="text-align: center;">${obj.getEditContent()}</td>
																	</tr>
																</c:forEach>
															</c:when>
															<c:otherwise>
																<c:forEach var="i" begin="0" end="19"  >
																	<tr>
																		<td style="text-align: center;">${workSituationEditList.get(i).getFormatEditDate()}</td>
																		<td style="text-align: center;">${workSituationEditList.get(i).getEditContent()}</td>
																	</tr>
																</c:forEach>
															</c:otherwise>
														</c:choose>
													</c:when>
													<c:otherwise>
														<c:choose>
															<c:when test="${workSituationEditList.size() < 5}">
																<c:forEach var="obj" items="${workSituationEditList}"  >
																	<tr>
																		<td style="text-align: center;">${obj.getFormatEditDate()}</td>
																		<td style="text-align: center;">${obj.getEditContent()}</td>
																	</tr>
																</c:forEach>
															</c:when>
															<c:otherwise>
																<c:forEach var="i" begin="0" end="4">
																	<tr>
																		<td style="text-align: center;">${workSituationEditList.get(i).getFormatEditDate()}</td>
																		<td style="text-align: center;">${workSituationEditList.get(i).getEditContent()}</td>
																	</tr>
																</c:forEach>
															</c:otherwise>
														</c:choose>
													</c:otherwise>
												</c:choose>
											</tbody>
										</table><br><br>
										<div class=button_wrapper >
											<c:choose>
												<c:when test="${result}">
													<a class="btn btn-primary" href="WorkSituationEditHistory?id=${userInfo.getId()}" >最新5件を表示</a><br>
												</c:when>
												<c:otherwise>
													<a class="btn btn-primary" href="WorkSituationEditHistory?id=${userInfo.getId()}&disp=20" >最新20件を表示</a><br>
												</c:otherwise>
											</c:choose>
										</div>
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

