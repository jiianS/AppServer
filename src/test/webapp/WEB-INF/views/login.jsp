<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<title>Login</title>

<style type="text/css">
input {
	padding: 5px;
	margin: 5px 0;
}

#submit {
	width: 185px;
}
</style>
</head>
<body>
	<h1>Login</h1>
	<form action="userSelect" method="post">
		<input type="email" name="userEmail" placeholder="이메일을 입력하세요" maxlength="100" required="required"><br> 
		<input type="password" name="userPassword" placeholder="비밀번호를 입력하세요" maxlength="10" required="required" min="8"><br> 
		<input type="submit" id="submit" value="로그인">
	</form>

</body>
</html>
