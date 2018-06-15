<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
<title>Home</title>



</head>
<body>
	<h1>Main _ App Server</h1>

	<a href="login"> Login </a>
	<br>
	<a href="user"> User Join </a><br>
	<a href="bInsert"> boardInsert </a>
	<br>
	
	

	<p>
		<c:if test="${data.status == 1}">
			${data.userName} 
		</c:if>

		<c:if test="${data.status == 0}">
			로그인한 계정은 가입이 되어있지 않습니다
		</c:if>

	</p>



</body>
</html>

