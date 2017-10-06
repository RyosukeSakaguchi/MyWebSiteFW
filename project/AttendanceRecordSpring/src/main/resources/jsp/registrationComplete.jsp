<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html class="no-js">

	<head>
		<!-- 共通header.jsp読み込み-->
		<jsp:include page="header.jsp" />
		<title>登録完了</title>
	</head>

	<body>
		<div id="fh5co-page">
			<section id="fh5co-header">
				<div class="container">
					<nav role="navigation">
						<ul class="pull-left left-menu">
							<li class="fh5co-cta-btn"><a href="WorkSituationRegistration">戻る</a>
						</ul>
					</nav>
				</div>
			</section>

			<section id="fh5co-hero" class="no-js-fullheight" style="background-image: url(/AttendanceRecord/jsp/images/full_image_2.jpg);" data-next="yes">
				<div class="container">
					<div class="fh5co-intro no-js-fullheight">
						<div class="fh5co-intro-text">
							<div class="fh5co-center-position">
								<h2 class="animate-box">Registration Complete !</h2><br><br>
								<h3 class="animate-box">${scsMsg}</h3>
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

