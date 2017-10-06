<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.List"%>
<%@ page import="beans.PositionBeans"%>
<%@ page import="dao.DaoUtil"%>
<!DOCTYPE html>
<html class="no-js">

	<head>
		<!-- 共通header.jsp読み込み-->
		<jsp:include page="header.jsp" />
		<title>新規登録</title>
		<!-- オリジナルCSS読み込み -->
		<link href="/AttendanceRecord/jsp/css/original/common.css" rel="stylesheet">
	</head>

	<body>
		<div class="fh5co-loader"></div>
		<div id="fh5co-page">

			<section id="fh5co-header">
				<div class="container">
					<nav role="navigation">
						<ul class="pull-left left-menu">
							<li class="fh5co-cta-btn">
							<a href="UserList">戻る</a>
						</ul>
					</nav>
				</div>
			</section>

			<section id="fh5co-hero" style="background-image: url(/AttendanceRecord/jsp/images/full_image_2.jpg); height: 700px" data-next="yes">
				<div class="container">
					<div class="fh5co-intro " style="height: 750px">
						<div class="fh5co-intro-text">
							<div class="wrapper2">
								<div class="fh5co-left-position"><br><br><br><br>
									<h2 class="animate-box">Sign Up</h2>
									<h2 class="animate-box"><font size="5" color="red">${errMsg}</font></h2>
								</div>
								<div class="fh5co-right-position">
									<form action="SignUp" method="post">
										<div class="form-group">
											<label for="inputName" class="col-sm-2 control-label" style="width: 200px;">ログインID</label>
											<div class="col-sm-3">
												<input type="text" value="${loginId}" pattern="^[0-9A-Za-z]+$" title="使用できるのは半角英数字のみ" class="form-control" name="loginId" style="background: white; height: 35px; width: 200px;">
											</div>
										</div><br><br>
										<div class="form-group">
											<label for="inputName" class="col-sm-2 control-label" style="width: 200px;">ユーザー名</label>
											<div class="col-sm-3">
												<input type="text" value="${name}" class="form-control" name="name" style="background: white; height: 35px; width: 200px;">
											</div>
										</div><br><br>
										<div class="form-group">
											<label for="inputName" class="col-sm-2 control-label" style="width: 200px;">パスワード</label>
											<div class="col-sm-3">
												<input type="password" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}" title="8文字以上で1文字以上の数字、小文字アルファベット、大文字アルファベットがそれぞれ含まれていること" class="form-control" name="password" style="background: white; height: 35px; width: 200px;">
											</div>
										</div><br><br>
										<div class="form-group">
											<label for="inputName" class="col-sm-2 control-label" style="width: 200px;">パスワード(確認)</label>
											<div class="col-sm-3">
												<input type="password" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}" title="8文字以上で1文字以上の数字、小文字アルファベット、大文字アルファベットがそれぞれ含まれていること" class="form-control" name="passwordConf" style="background: white; height: 35px; width: 200px;">
											</div>
										</div><br><br>
										<div class="form-group">
											<label for="inputName" class="col-sm-2 control-label" style="width: 200px;">役職</label>
											<div class="col-sm-3">
												<select class="form-control" name="position" style="background: white; height: 35px; width: 200px;">
													<c:choose>
														<c:when test="${position == null}">
															<c:forEach var="obj" items="${positonList}">
																<option value="${obj.position}" >${obj.position}</option>
															</c:forEach>
														</c:when>
														<c:otherwise>
															<c:forEach var="obj" items="${positonList}" >
																<c:choose>
																	<c:when test="${obj.getPosition().equals(position)}">
																		<option value="${obj.getPosition()}" selected >${obj.getPosition()}</option>
																	</c:when>
																	<c:otherwise>
																		<option value="${obj.getPosition()}">${obj.getPosition()}</option>
																	</c:otherwise>
																</c:choose>
															</c:forEach>
														</c:otherwise>
													</c:choose>
												</select>
											</div>
										</div><br><br>
										<div class="form-group">
											<label for="inputName" class="col-sm-2 control-label" style="width: 200px;">誕生日</label>
											<div class="col-sm-3">
												<input type="date" value="${birthDate}" class="form-control" name="birthDate" style="background: white; height: 35px; width: 200px;">
											</div>
										</div><br> <br> <br>
										<div class="button_wrapper">
											<button class="btn btn-info" type="submit">登録</button>
										</div>
									</form>
								</div>
							</div>
						</div>
					</div>
				</div>
			</section>
		</div>

		<!-- 共通footer.jsp読み込み-->
		<jsp:include page="footer.jsp" />

	</body>

</html>

