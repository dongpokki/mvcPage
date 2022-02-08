<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판 글쓰기</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.6.0.min.js"></script>
<script type="text/javascript">
	$(function(){
		$('#write_form').submit(function(){
			if($('#title').val().trim() == ''){
				alert('제목을 입력해주세요.');
				$('#title').val('').focus();
				return false;
			}
			if($('#content').val().trim() == ''){
				alert('내용을 입력해주세요.');
				$('#content').val('').focus();
				return false;
			}
		});
	});
</script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<h2>게시판 글쓰기</h2>
	<form id="write_form" action="write.do" method="post" enctype="multipart/form-data">
		<ul>
			<li>
				<label for="title">제목</label>
				<input type="text" id="title" name="title" maxlength="50">
			</li>
			<br>
			<li>
				<label for="content">내용</label>
				<textarea rows="5" cols="30" id="content" name="content"></textarea>
			</li>
			<li>
				<label for="filename">파일</label>
				<input type="file" name="filename" id="filename" accept="image/gif,image/png,image/jpeg"> <!-- jpg 파일형식은 jpeg로 명시해야함 -->
			</li>
		</ul>
		<div class="align-center">
			<input type="submit" value="등록">
			<input type="button" value="목록" onclick="location.href='list.do';">
		</div>
	</form>
</div>
</body>
</html>