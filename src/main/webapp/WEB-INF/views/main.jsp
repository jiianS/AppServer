<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%-- <%@ page session="false" %> --%>
<html>
<head>
	<title>Home</title>
	<meta charset="UTF-8">
</head>
<body>
	<h1>Hello App Server! </h1>
	<a href="bList">게시판</a><br>
	<c:if test="${empty sessionScope}">
		<a href="login">로그인</a>
		<a href="user">회원가입</a>
	</c:if>
		
	<c:if test="${sessionScope.user.status == 1}">
		${sessionScope.user.userName}
		<a href="logout">로그아웃</a>
	</c:if>
	
	<c:if test="${sessionScope.user.status == 0}">
		<a href="login">로그인</a>
		<a href="user">회원가입</a>
		로그인한 계정은 가입이 되어 있지않습니다.
	</c:if>
</body>
</html>
