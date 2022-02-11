<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- header 시작 -->
<h1 class="align-center"><a href="${pageContext.request.contextPath}/main/main.do">회원제 게시판</a></h1>
<div class="align-right">
	<c:if test="${!empty user_num && !empty user_photo}"> <!-- user_num,user_photo가 있는경우 (로그인이 되어있고 사진이 있는경우) -->
		<img src="${pageContext.request.contextPath}/upload/${user_photo}" width="25" height="25" class="my-photo">
	</c:if>
	<c:if test="${!empty user_num && empty user_photo}"> <!-- 사진은 없지만, 로그인 된 경우 -->
		<img src="${pageContext.request.contextPath}/images/face.png" width="25" height="25" class="my-photo">
	</c:if>
	<a href="${pageContext.request.contextPath}/board/list.do" class="float-left">게시판</a>
	<c:if test="${!empty user_num}"> <!--  로그인 된 경우 -->
		[<span><b>${user_id}</b></span>]
		<a href="${pageContext.request.contextPath}/member/logout.do">로그아웃</a>
	</c:if>
	<c:if test="${empty user_num}"> <!--  로그인 안되어 있는 경우 -->
		<a href="${pageContext.request.contextPath}/member/registerUserForm.do">회원가입</a>
		<a href="${pageContext.request.contextPath}/member/loginForm.do">로그인</a>
	</c:if>
	<c:if test="${!empty user_num && user_auth == 2}"> <!--  일반 회원으로 로그인 한 경우 -->
		<a href="${pageContext.request.contextPath}/member/myPage.do">마이페이지</a>
	</c:if>
	<c:if test="${!empty user_num && user_auth ==3}"> <!--  관리자로 로그인 한 경우 -->
		<a href="${pageContext.request.contextPath}/member/memberList.do">회원관리</a>
		<a href="${pageContext.request.contextPath}/item/list.do">상품관리</a>
	</c:if>
</div>
<hr noshade="noshade">
<!-- header 끝 -->