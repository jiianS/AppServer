<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<title>update</title>

<style type="text/css">
input {
	padding: 5px;
	margin: 5px 0;
}

#submit {width: 185px;}


</style>
<script type="text/javascript"
	src="webjars/jquery/3.3.1/dist/jquery.min.js"></script>

<script>
 
      $(document).ready(function(){
    	 var delData=[];
         var boardNo = "${param.boardNo}";
         
         if(boardNo == "") {
            console.log("누구세용");
            location.href = "/app";
            
         }else{
            
             $.ajax({
                type: "post",
                url: "bld",
                data: {"boardNo" : boardNo}
              }).done(function(data) {
                 var d = JSON.parse(data);
                 var boardData = d.boardData;
                 var filesData = d.filesData;
                 
                 boardHTML(boardData);
                 filesHTML(filesData);
             });
         }	//글 내용 갖고오기(ajax이용)
         
         
         // 글 수정 누르고 돌아가는!!
         $("form").submit(function(e){
            e.preventDefault();
            
             $.ajax({
                 type: "post",
                 url: "http://FileServer/FileUpload/jian",
                 data: new FormData($(this)[0]),
                 contentType: false,
                 cache: false,
                 processData: false
              }).done(function(data) {
                 var d = JSON.parse(data);
                 update(d);
              });
         }); 	//form.submit
  
             
       //함수들
       /************************************************************/
          function update() {
         	 $.ajax({
                  type: "post",
                  url: "bud",
                  data: {
						"boardNo" : boardNo,
						"boardTitle" : $("form input").eq(0).val(), 
                      	"boardContents" : $("form input").eq(1).val(),
                        "data" : JSON.stringify(d.upload)
                        "delDate" : JSON.stringify(delDate)
					}
         	 
               }).done(function(data){
                  var d = JSON.parse(data);
                  alert(d.msg);
                  
                  if(d.status == 1) {
                     location.href="bSelect?boardNo=" + d.boardNo;
                  }                    
               });
			}	//function _ update

		/************************************************************/          
         function boardHTML(data) {
			console.log("boardHTML", data);
			var title = data.boardTitle;
			var contents = data.boardContents;
			$("#form input").eq(0).val(title);
			$("#form input").eq(1).val(contents);
         }
         /************************************************************/  
         function filesHTML(data){
             console.log("filesHTML",data);
             $("#files").empty();
             
             for(var i =0; i<data.length;i++){
                var fileURL=data[i].fileURL;
                var html="<img src='"+fileURL+"' width='100' height='100'><br>";
                $("#files").append(html);
             }
             
             $("img").on("click", function() {
          		var index =  $("img").index(this);
          	   	var fileNo = data[index].fileNo;
          	   	
          	 	delData.push({"fileNo":fileNo});
          	   	data.splice(index,1);
          	   	$("img").eq(index).remove();
          	   
   			});
           }	//filesHTML()
           
         /************************************************************/
      }); //document
    
   </script>

</head>
<body>
	<h1>board _ 글 수정</h1>
	<form action="bUpdate" method="post" enctype="multipart/form-data">
		<input type="text" name="boardTitle" placeholder="제목을 입력하세여"><br>
		<input type="text" name="boardContents" placeholder="내용을 입력하세요"><br>
		<input type="file" name="file" multiple="multiple"><br> <input
			type="submit" id="submit" value="글 수정"><br>

	</form>
	<ul id="files"></ul>

</body>

</html>
