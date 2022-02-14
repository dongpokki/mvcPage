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
<script type="text/javascript">
	$(function(){
		$('#search_form').submit(function(){
			if($('#keyword').val().trim()==''){
				alert('검색어를 입력하세요.');
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
	<h2>상품 목록</h2>
	<form action="itemList.do" method="get" id="search_form">
		<ul class="search">
			<li>
				<select name="ketfield">
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
	<c:if test="${count == 0}">
		<div class="result-display">
			표시할 상품이 없습니다.
		</div>
	</c:if>
	<c:if test="${count > 0}">
		<div class="item-space">
			<c:forEach var="item" items="${list}">
				<div class="horizontal-area">
					<a href="detail.do?item_num=${item.item_num}">
						<img src="${pageContext.request.contextPath}/upload/${item.photo1}">
						<br>
						<span>${item.name}
						<br>
						<b><fmt:formatNumber value="${item.price}"/>원</b>
						</span>
					</a>
				</div>
			</c:forEach>
			<div class="float-clear align-center">
				${pagingHtml}
			</div>
		</div>
	</c:if>
</div>
</body>
</html>