<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html lang="en-US">

	<head>
		<meta charset="utf-8">
		<title>ログイン画面</title>
		<link rel="stylesheet" href="/AttendanceRecord/jsp/css/original/LoginScreen.css">
	</head>

	<body>
		<div align="center">
			<br><br><br>
			<font size="5" color="white">${errMsg}</font>
			<font size="5" color="white">${logout}</font>
		</div>
		<div id="login">
			<form name='form-login' action="LoginScreen" method="post">
				<span class="fontawesome-user"></span><input type="text" name="loginId" value="${loginId}" placeholder="Username">
				<span class="fontawesome-lock"></span><input type="password" name="password" placeholder="Password">
				<input type="submit" value="ログイン">
			</form>
		</div>
	</body>

</html>