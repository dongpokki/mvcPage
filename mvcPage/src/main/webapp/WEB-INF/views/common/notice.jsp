<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>안내</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css">
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<h2>안내</h2>
	<div class="result-display">
		<div class="align-center">
			<c:if test="${!empty accessMsg}"> <%-- 잘못된 접근 시 메세지 관리할거면 request 데이터 값 넣어주기 --%>
				${accessMsg}
			</c:if>
			<c:if test="${empty accessMsg}">
				잘못된 접속입니다.
			</c:if>
			<p>
			<c:if test="${!empty accessUrl}"> <%-- 잘못된 접근 후 보낼 주소 관리할거면 request 데이터 값 넣어주기 --%>
				<input type="button" value="이동" onclick="location.href='${accessUrl}';">
			</c:if>
			<c:if test="${empty accessUrl}">
				<input type="button" value="홈으로" onclick="location.href='${pageContext.request.contextPath}/main/main.do';">
			</c:if>
		</div>
	</div>
</div>
</body>
</html>