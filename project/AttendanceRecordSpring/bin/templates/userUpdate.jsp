<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="beans.UserBeans"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="beans.PositionBeans"%>
<%@ page import="dao.DaoUtil"%>
<%@ page import="java.util.List "%>
<!DOCTYPE html>
<html class="no-js">

	<head>
		<!-- 共通header.jsp読み込み-->
		<jsp:include page="header.jsp" />
		<title>ユーザー情報更新</title>
		<!-- オリジナルCSS読み込み -->
		<link href="/AttendanceRecord/jsp/css/original/common.css" rel="stylesheet">
	</head>

	<body>
		<!-- Loader -->
		<div class="fh5co-loader"></div>
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
							</li>
						</ul>
					</nav>
				</div>
			</section>

			<section id="fh5co-hero" style="background-image: url(/AttendanceRecord/jsp/images/full_image_2.jpg); height: 700px;" data-next="yes">
				<div class="fh5co-overlay"></div>
				<div class="container">
					<div class="fh5co-intro " style="height: 750px">
						<div class="fh5co-intro-text">
							<div class="wrapper2">
								<div class="fh5co-left-position"><br><br><br><br>
									<h2 class="animate-box">User Update</h2>
									<h2 class="animate-box"><font size="5" color="white">${errMsg}</font></h2>
								</div>
								<div class="fh5co-right-position">
									<form action="/AttendanceRecord/UserUpdate" method="post">
										<label for="inputName" class="col-sm-2 control-label" style="width: 200px;">ログインID</label>
										<div class="col-sm-3">${userInfo.getLoginId()}</div><br><br>
										<label for="inputName" class="col-sm-2 control-label" style="width: 200px;">ユーザー名</label>
										<div class="col-sm-3">
											<input type="text" class="form-control" id="inputName" value="${userInfo.getName()}" name="name"
											onblur="isEmpty(this)" style="background: white; height: 35px; width: 200px;">
										</div>
										<br><br>
										<label for="inputName" class="col-sm-2 control-label" style="width: 200px;">パスワード</label>
										<div class="col-sm-3">
											<input type="password" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}"
											 title="8文字以上で1文字以上の数字、小文字アルファベット、大文字アルファベットがそれぞれ含まれていること"
											 class="form-control" id="inputName" name="password" onblur="isEmpty(this)" style="background: white; height: 35px; width: 200px;">
										</div>
										<br><br>
										<label for="inputName" class="col-sm-2 control-label" style="width: 200px;">パスワード(確認)</label>
										<div class="col-sm-3">
											<input type="password" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}"
											title="8文字以上で1文字以上の数字、小文字アルファベット、大文字アルファベットがそれぞれ含まれていること"
											class="form-control" id="inputName" name="passwordConf" onblur="isEmpty(this)" style="background: white; height: 35px; width: 200px;">
										</div>
										<br><br>
										<label for="inputName" class="col-sm-2 control-label" style="width: 200px;"> 役職</label>
										<div class="col-sm-3">
											<select class="form-control" id="inputName" name="position"  style="background: white; height: 35px; width: 200px;">
												<c:forEach var="obj" items="${positonList}">
													<c:choose>
														<c:when test="${obj.getPosition().equals(userInfo.getPosition())}">
															<option value="${obj.getPosition()}" selected >${obj.getPosition()}</option>
														</c:when>
														<c:otherwise>
															<option value="${obj.getPosition()}">${obj.getPosition()}</option>
														</c:otherwise>
													</c:choose>
												</c:forEach>
											</select>
										</div>
										<br><br>
										<label for="inputName" class="col-sm-2 control-label" style="width: 200px;">誕生日</label>
										<div class="col-sm-3">
											<input type="date" class="form-control" id="inputName" name="birthDate" onblur="isEmpty(this)" value="${userInfo.getBirthDate()}" style="background: white; height: 35px; width: 200px;">
										</div>
										<br> <br> <br>
										<input type="hidden" value="${userInfo.getId()}" name="id">
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

