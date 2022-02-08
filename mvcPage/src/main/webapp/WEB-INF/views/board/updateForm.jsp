<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 수정</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.6.0.min.js"></script>
<script type="text/javascript">
	$(function(){
		$('#update_form').submit(function(){
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
	<h2>게시글 수정</h2>
	<form id="update_form" action="update.do" method="post" enctype="multipart/form-data">
		<input type="hidden" id="board_num" name="board_num" value="${board.board_num}">
		<ul>
			<li>
				<label for="title">제목</label>
				<input type="text" id="title" name="title" maxlength="50" value="${board.title}">
			</li>
			
			<br>
			
			<li>
				<label for="content">내용</label>
				<textarea rows="5" cols="30" id="content" name="content">${board.content}</textarea>
			</li>
			
			<li>
				<label for="filename">파일</label>
				<input type="file" name="filename" id="filename" accept="image/gif,image/png,image/jpeg"> <!-- jpg 파일형식은 jpeg로 명시해야함 -->
				<c:if test="${!empty board.filename}">
					<br>
					<span id="file_detail">
						<hr>
							첨부되어 있는 파일 | ${board.filename} <input type="button" value="파일삭제" id="file_del">
						<hr>
					</span>
<script type="text/javascript">
	$(function(){
		$('#file_del').click(function(){
			let choice = confirm('업로드된 파일을 삭제하시겠습니까?');
			if(choice){
				$.ajax({
					url:'deleteFile.do',
					type:'post',
					data:{board_num:${board.board_num}},
					dataType:'json',
					cache:false,
					timeout:30000,
					success:function(param){
						if(param.result == 'logout'){ // user_num 세션이 없는경우 - 로그인 하지 않은 경우
							alert('로그인 후 사용하세요');
						}else if(param.result == 'success'){
							$('#file_detail').hide();
						}else if(param.result == 'wrongAccess'){ // 로그인한 회원의 회원번호와 게시글 작성자의 회원번호가 일치하지 않는 경우
							alert('잘못된 접속입니다.');
						}else{
							alert('파일 삭제 오류 발생');
						}
					},
					error:function(){ // ajax 처리 X
						alert('네트워크 오류');
					}
				});
			}
		});
	});
</script>
				</c:if>
			</li>
		</ul>
		<div class="align-center">
			<input type="submit" value="수정">
			<input type="button" value="목록" onclick="location.href='list.do';">
		</div>
	</form>
</div>
</body>
</html>