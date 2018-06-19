<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>글내용</title>
	<meta charset="UTF-8">
	<script type="text/javascript" src="webjars/jquery/3.3.1/dist/jquery.min.js"></script>
	<script>
		$(document).ready(function(){
			var boardNo = "${param.boardNo}";
			
			if(boardNo == ""){
				alert("누구세요?");
				location.href = "/app";
			}else {
				$.ajax({
					  type : "post",
					  url : "bld",
					  data : {"boardNo" : boardNo}
				  }).done(function(data){
					  var d = JSON.parse(data)
					  var boardData = d.boardData;
					  var filesData = d.filesData;
					  
					  boardHTML(boardData);
					  filesHTML(filesData);
				  });
			}
		});
		function boardHTML(data){
			console.log("boardHTML", data);
			var title = data.boardTitle;
			var contents = data.boardContents;
			$("#title").text(title);
			$("#contents").text(contents);
		}
		
		function filesHTML(data){
			console.log("filesHTML", data);
			$("#files").empty();
			for(var i = 0; i < data.length; i++){
				var fileURL = data[i].fileURL;
				var html = "<img src='"+fileURL+"'><br>";
				console.log(fileURL, html);
				$("#files").append(html);
			}
		}
	</script>
</head>
<body>
	<h1 id="title"></h1>
	<p id="contents"></p>
	<div id="files"></div>
	<div>
		<a href="bList">목록</a>
		<a href="bUpdate?boardNo=${param.boardNo}">수정</a>
		<a href="bDel?boardNo=${param.boardNo}">삭제</a>
	</div>
</body>
</html>
