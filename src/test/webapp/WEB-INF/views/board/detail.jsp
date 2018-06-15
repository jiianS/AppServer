<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
<title>detail</title>

<style type="text/css">
div{width = 200px height=200px;}

</style>
<script type="text/javascript"
	src="webjars/jquery/3.3.1/dist/jquery.min.js"></script>


<script>
	$(document).ready(function() {
		var boardNo = "${param.boardNo}";
		console.log(boardNo);
	
		if(boardNo == ""){
			alert("누구양");
			location.href = "/app"
		}else{
			$.ajax({		
				type : "post",
				url : "bld",
				data : {"boardNo" : boardNo}
			}).done(function(data) {
				var d = JSON.parse(data)
				var boardData = d.boardData;
				var filesData = d.filesData;
				console.log("test =  " + boardData);
				console.log("test =  " + filesData);
				boardHTML(boardData);
				filesHTML(filesData);
			}); //_ boardData_ 리스트 갖고 오기 위한 ajax
		}
		
	}); //document(ready)

	function boardHTML(data) {
		var title = data.boardTitle;
		var contents = data.boardContents;
		
		$("#title").text(title);
		$("#contents").text(contents);
		
	}
	
	function filesHTML(data){
        console.log("filesHTML +++++ ",data);
        $("#files").empty();
        for(var i =0; i<data.length;i++){
           var fileURL=data[i].fileURL;
           var html="<img src='" + fileURL + "'><br>";
           $("#files").append(html);
        }
        
     }
</script>


</head>
<body>
	<h1 id="title">board _ 글 내용</h1>
	
	
	<hr>
	<p id="contents"></p>
	<div id="files"></div>
	
	<div >
		<a href="bUpdate?boardNo=${param.boardNo}"> 수정</a>
		<a href="bList">삭제</a>
		<a href="bList">글 목록으로</a>
	</div>
</body>
</html>
