<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 등록 완료</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css">
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<h2>게시글 등록 완료</h2>
	<div class="reuslt-display">
		<div class="align-center">
			게시글이 등록되었습니다.
			<p>
			<input type="button" value="목록" onclick="location.href='${pageContext.request.contextPath}/board/list.do';">
			<input type="button" value="홈으로" onclick="location.href='${pageContext.request.contextPath}/main/main.do';">
		</div>
	</div>
</div>
</body>
</html>