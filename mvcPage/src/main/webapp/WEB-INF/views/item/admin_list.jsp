<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>상품 목록</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.6.0.min.js"></script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<h2>상품 목록</h2>
	<form id="search_form" action="list.do" method="get">
		<ul class="search">
			<li>
				<select name="keyfield">
					<option value="1">상품명</option>
					<option value="2">내용</option>
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
		<input type="button" value="상품등록" onclick="location.href='writeForm.do';">
		<input type="button" value="목록으로" onclick="location.href='list.do';">
		<input type="button" value="홈으로" onclick="location.href='${pageContext.request.contextPath}/main/main.do';">
	</div>	
	
	<c:if test="${count == 0}">
		<div class="result-display">
			표시할 상품이 없습니다.
		</div>
	</c:if>
	<c:if test="${count > 0}">
		<table>
			<tr>
				<th>상품명</th>
				<th>가격</th>
				<th>재고</th>
				<th>등록일</th>
				<th>상태</th>
			</tr>
			<c:forEach var="item" items="${list}">
				<tr>
					<td><a href="modifyForm.do?item_num=${item.item_num}">${item.name}</a></td>
					<td><fmt:formatNumber value="${item.price}"/></td>
					<td><fmt:formatNumber value="${item.quantity}"/></td>
					<td>${item.reg_date}</td>
					<td>
						<c:if test="${item.status == 1}">미표시</c:if>
						<c:if test="${item.status == 2}">표시</c:if>
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