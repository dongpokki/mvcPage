<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 상세</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.6.0.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/board-reply.js"></script>
<script type="text/javascript">
	$(function(){
		$('#delete_btn').click(function(){
			let check = confirm('정말 삭제하시겠습니까?');
			if(check){
				location.href='delete.do?board_num=${board.board_num}';
				// location.replace='delete.do?board_num=${board.board_num}';
				// replace 사용하면 히스토리 삭제 (기능은 같음)
			}
		});
	});
</script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<h2>게시글 상세</h2>
	<ul>
		<li>게시글 번호 : ${board.board_num}</li>
		<li>제목 : ${board.title}</li>
		<li>작성자 : ${board.id}</li>
		<li>조회수 : ${board.hit}</li>
		<c:if test="${!empty board.modify_date}">
			<li>최근 수정일 : ${board.modify_date}</li>
		</c:if>
		<li>작성일 : ${board.reg_date}</li>
	</ul>
	<hr width="100%" size="1" noshade="noshade">
	<c:if test="${!empty board.filename}">
		<div class="align-center">
			<img src="${pageContext.request.contextPath}/upload/${board.filename}" class="detail-img">
		</div>
	</c:if>
	<p>
		${board.content}
	</p>
	<hr width="100%" size="1" noshade="noshade">
	| 첨부된 파일 | 
	<c:if test="${!empty board.filename}">
		<a download href="${pageContext.request.contextPath}/upload/${board.filename}">${board.filename}</a>
	</c:if>
	<c:if test="${empty board.filename}">첨부된 파일이 없습니다.</c:if>
	<hr width="100%" size="1" noshade="noshade">
	<div class="align-right">
		<c:choose>
			<c:when test="${prev == -1}">
				<input class="float-left" type="button" value="이전" disabled="disabled">
				<input class="float-left" type="button" value="다음" onclick="location.href='detail.do?board_num=${next}';">		
			</c:when>
			<c:when test="${next == -2}">
				<input class="float-left" type="button" value="이전" onclick="location.href='detail.do?board_num=${prev}';">
				<input class="float-left" type="button" value="다음" disabled="disabled">
			</c:when>
			<c:otherwise>
				<input class="float-left" type="button" value="이전" onclick="location.href='detail.do?board_num=${prev}';">
				<input class="float-left" type="button" value="다음" onclick="location.href='detail.do?board_num=${next}';">		
			</c:otherwise>
		</c:choose>
		<c:if test="${user_num == board.mem_num || user_auth == 3}">
			<input type="button" value="수정" onclick="location.href='updateForm.do?board_num=${board.board_num}';">
			<input type="button" value="삭제" id="delete_btn">
		</c:if>
		<input type="button" value="목록" onclick="location.href='list.do';">
	</div>
	
	<p>
	
	<!-- 댓글 시작 -->
	<div id="reply_div">
		<span class="re-title">댓글 달기</span>
		<form id="re_form">
			<input type="hidden" name="board_num" value="${board.board_num}" id="board_num">
			<textarea rows="3" cols="50" name="re_content" id="re_content" class="rep-content" <c:if test="${empty user_num}">disabled</c:if>><c:if test="${empty user_num}">로그인해야 작성할 수 있습니다.</c:if></textarea>
			<c:if test="${!empty user_num}">
				<div id="re_first">
					<span class="letter-count">300/300</span>
				</div>
				<div id="re_second" class="align-right">
					<input type="submit" value="전송">
				</div>
			</c:if>
		</form>
	</div>
	
	<!--  댓글 목록 출력 시작 -->
	<div id="output"></div>
	<div class="paging-button" style="display: none">
		<input type="button" value="다음글 보기">
	</div>
	<div id="loading" style="display: none">
		<img src="${pageContext.request.contextPath}/images/ajax-loader.gif">
	</div>
	<!--  댓글 목록 출력 끝 -->
	
	<!-- 댓글 끝 -->
</div>
</body>
</html>