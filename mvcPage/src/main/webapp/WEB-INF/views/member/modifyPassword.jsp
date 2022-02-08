<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:choose>
	<c:when test="${check}">
		<!DOCTYPE html>
		<html>
		<head>
		<meta charset="UTF-8">
		<title>비밀번호 수정</title>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css">
		</head>
		<body>
			<div class="page-main">
				<jsp:include page="/WEB-INF/views/common/header.jsp"/>
				<h2>비밀번호 수정</h2>
				<div class="result-display">
					<div class="align-center">
						비밀번호 수정이 완료되었습니다.
						<p>
						<input type="button" value="홈으로" onclick="location.href='${pageContext.request.contextPath}/main/main.do';">
						<input type="button" value="마이페이지" onclick="location.href='${pageContext.request.contextPath}/member/myPage.do';">
					</div>
				</div>
			</div>
		</body>
		</html>
	</c:when>
	<c:otherwise>
		<script type="text/javascript">
			alert('입력하신 현재 아이디 또는 비밀번호가 불일치 합니다.');
			history.go(-1);
		</script>
	</c:otherwise>
</c:choose>