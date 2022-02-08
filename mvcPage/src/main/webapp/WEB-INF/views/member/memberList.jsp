<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원목록</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.6.0.min.js"></script>
<script type="text/javascript">
	$(function(){
		$('#search_form').submit(function(){
			if($('#keyword').val().trim()==''){
				alert('검색어를 입력하세요');
				$('#keyword').val('').focus();
				return false;
			}
		});
	});
</script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<h2>회원목록(관리자 전용)</h2>
	<form id="search_form" action="memberList.do" method="get">
		<ul class="search">
			<li>
				<select name="keyfield">
					<option value="1">ID</option>
					<option value="2">이름</option>
					<option value="3">이메일(email)</option>
				</select>
			</li>
			<li>
				<input type="search" size="16" name="keyword" id="keyword">
			</li>
			<li>
				<input type="submit" value="검색">
			</li>
		</ul>
	</form>
	<div class="list-space align-right">
		<input type="button" value="목록" onclick="location.href='memberList.do';">
		<input type="button" value="홈으로" onclick="location.href='${pageContext.request.contextPath}/main/main.do';">
	</div>
	<c:if test="${count == 0}">
		<div class="result-display">
		표시할 내용이 없습니다.
		</div>
	</c:if>
	<c:if test="${count > 0}">
		<table>
			<tr>
				<th>ID</th>
				<th>이름</th>
				<th>이메일(email)</th>
				<th>전화번호</th>
				<th>가입일</th>
				<th>회원등급</th>
			</tr>
			<c:forEach var="member" items="${list}">
				<tr>
					<td>
						<c:if test="${member.auth != 0}"> <%-- 탈퇴회원(0)이 아닌 경우 --%>
							<a href="detailUserForm.do?mem_num=${member.mem_num}">${member.id}</a>
						</c:if>
						<c:if test="${member.auth == 0}"> <%-- 탈퇴회원(0)인 경우 --%>
							${member.id}
						</c:if>
					</td>
					<td>${member.name}</td>
					<td>${member.email}</td>
					<td>${member.phone}</td>
					<td>${member.reg_date}</td>
					<td>
						<c:if test="${member.auth == 0}">탈퇴회원</c:if>
						<c:if test="${member.auth == 1}">정지회원</c:if>
						<c:if test="${member.auth == 2}">일반회원</c:if>
						<c:if test="${member.auth == 3}">관리자</c:if>
					</td>
				</tr>
			</c:forEach>
		</table>
		<div class="align-center">
			${pagingHtml}
		</div>
	</c:if>
</div>
</body>
</html>