<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
<title>list</title>

<style type="text/css">

</style>
<script type="text/javascript"
	src="webjars/jquery/3.3.1/dist/jquery.min.js"></script>

<script>
	$(document).ready(function() {
		$.ajax({
			type : "post",
			url : "bbld"
		}).done(function(data) {
			var d = JSON.parse(data)
			console.log(d);

			//list작성하기(ul)
			var list = d.list;
			$("#list").empty();
			for (var i = 0; i < list.length; i++) {
				var html = "<li>";
				/*****************************/
				html += "<a href='bSelect?boardNo=";
				html += list[i].boardNo;
				html += "'>";
				html += list[i].boardTitle + "</a>";
				/*****************************/
				html = html + "</li>";
				$("#list").append(html);
			}

		});
	}); //document(ready)
</script>

</head>
<body>
	<h1>board _ 글 목록</h1>
	<p>
		<a href="bInsert">글 작성</a>
	</p>
	<hr>
	
	<ul id="list"></ul>
</body>
</html>
