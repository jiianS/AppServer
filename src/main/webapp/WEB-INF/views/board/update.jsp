<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
	<title>글수정</title>
	<meta charset="UTF-8">
	<script type="text/javascript" src="webjars/jquery/3.3.1/dist/jquery.min.js"></script>
	<script>
		$(document).ready(function(){
			var delData = [];
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
			
			
			$("form").submit(function(e){
				e.preventDefault();
				
				$.ajax({
					type: "post",
					url : "http://FileServer/FileUpload/kkb",
					data: new FormData($(this)[0]),
					contentType: false,
					cache: false,
					processData: false
				}).done(function(data) {
					  var d = JSON.parse(data)
					  update(d);
				});
			});
			
			function update(d){
				$.ajax({
					  type : "post",
					  url : "bud",
					  data : {
						  "boardNo" : boardNo,
						  "boardTitle" : $("form input").eq(0).val(),
						  "boardContents" : $("form input").eq(1).val(),
						  "data" : JSON.stringify(d.upload),
						  "delData" : JSON.stringify(delData)
					  }
				  }).done(function(data){
					  var d = JSON.parse(data);
					  alert(d.msg);
					  if(d.status == 1){
						location.href = "bSelect?boardNo=" + d.boardNo;
					  }
				  });
			}
			
			function boardHTML(data){
				var title = data.boardTitle;
				var contents = data.boardContents;
				
				$("form input").eq(0).val(title);
				$("form input").eq(1).val(contents);
			}
			
			function filesHTML(data){
				$("#files").empty();
				for(var i = 0; i < data.length; i++){
					var fileURL = data[i].fileURL;
					var html = "<img src='"+fileURL+"' width='50' height='50'><br>";
					$("#files").append(html);
				}
				
				$("img").on("click", function(){
					var index = $("img").index(this);
					var fileNo = data[index].fileNo;
					delData.push({"fileNo" : fileNo});
					data.splice(index, 1);
					$("img").eq(index).remove();
				});
			}
		});		
	</script>
</head>
<body>
	<h1>글수정! </h1>
	<form action="" method="post" enctype="multipart/form-data">
		<input type="text" name="boardTitle" placeholder="제목를 입력하세요."><br>
		<input type="text" name="boardContents" placeholder="내용을 입력하세요."><br>
		<input type="file" name="file" multiple="multiple"><br>
		<input type="submit"     value="글수정">
	</form>
	<div id="files"></div>
</body>
</html>
