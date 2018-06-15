<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<title>insert</title>

<style type="text/css">
input {	padding: 5px;	margin: 5px 0; }
#submit {width: 185px;}
</style>
<script type="text/javascript" src="webjars/jquery/3.3.1/dist/jquery.min.js"></script>


<script>
	$(document).ready(function(){
		$("form").submit(function(e) {
			e.preventDefault();
			
			$.ajax({
				type:"post",
				url : "http://fileServer/FileUpload/jian",
				data : new FormData($(this)[0]),
				contentType:false,
				cache:false,
				processData:false
				
			}).done(function(data) {
				var d = JSON.parse(data)
				console.log(d);
				
				$.ajax({
					type:"post",
					url: "bid",
					data:{
						"boardTitle": $("form input").eq(0).val(),
						"boardContents": $("form input").eq(1).val(),
						"data": JSON.stringify(d.upload)	//data를 스트링값 -> json 타입
						
					}
				}).done(function(data){					
					var d = JSON.parse(data)	
					alert(d.msg);
					
					if(d.status == 1){
						//글 작성 성공시
						location.href ="bSelect?boardNo=" + d.boardNo;
						
					} 
				});	// ajax_ 글작성 완료 
			});
		}); //form.submit 
	}); //document(ready)

	
</script>

</head>
<body>
	<h1>board _ 글 작성</h1>
	<form action="bInsert" method="post" enctype="multipart/form-data">
		<input type="text" name="boardTitle" placeholder="제목을 입력하세여"><br> 
		<input type="text" name="boardContents" placeholder="내용을 입력하세요"><br>
		<input type="file" name="file" multiple="multiple"><br>
		<input type="submit" id="submit" value="글 작성"><br>
		
	</form>

</body>
</html>
