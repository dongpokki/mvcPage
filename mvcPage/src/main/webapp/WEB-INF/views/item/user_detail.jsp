<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>상품 상세</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.6.0.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.number.min.js"></script>
<script type="text/javascript">
	$(function(){
		//장바구니 상품 담기
		$('#item_cart').submit(function(){
			if($('#order_quantity').val() == ''){
				alert('수량을 입력하세요');
				$('#order_quantity').focus();
				return false;
			}
		});
		
		//주문 수량 변경
		$('#order_quantity').on('keyup mouseup',function(){
			if($('#order_quantity').val() == ''){
				$('#item_total_txt').text('총주문 금액 : 0원');
				return;
				}
				
			if(Number($('#item_quantity').val()) < $('#order_quantity').val()){
				alert('수량이 부족합니다.');
				$('#order_quantity').val('');
				$('#item_total_txt').text('총주문 금액 : 0원')
				return;
			}
			
			let total = $('#item_price').val() * $('#order_quantity').val();
			$('#item_total_txt').text('총주문 금액 : ' + $.number(total) + '원');
			
		});
	});
</script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<c:if test="${item.status == 1}">
	<div class="result-display">
		<div class="align-center">
			본 상품은 판매 중지되었습니다.
			<br>
			<input type="button" value="상품 목록" onclick="location.href='itemList.do'">
		</div>
	</div>
	</c:if>
	<c:if test="${item.status == 2}">
	<h3 class="align-center">${item.name}</h3>
	<div class="item-image">
		<img src="${pageContext.request.contextPath}/upload/${item.photo2}" width="400">
	</div>
	<div class="item-detail">
		<form id="item_cart" action="${pageContext.request.contextPath}/cart/write.do" method="post">
			<input type="hidden" name="item_num" value="${item.item_num}" id="item_num">
			<input type="hidden" name="item_price" value="${item.price}" id="item_price">
			<input type="hidden" name="item_quantity" value="${item.quantity}" id="item_quantity">
			<ul>
				<li>
					가격 : <b><fmt:formatNumber value="${item.price}"/>원</b>
				</li>
				<li>
					재고 : <span><fmt:formatNumber value="${item.quantity}"/>개</span>
				</li>
				<c:if test="${item.quantity > 0}">
					<li>
						<label for="order_quantity">구매수량</label>
						<input type="number" name="order_quantity" min="1" max="${item.quantity}" id="order_quantity" class="quantity-width">
					</li>
					<li>
						<span id="item_total_txt">총주문 금액 : 0원</span>
					</li>
					<li>
						<input type="submit" value="장바구니에 담기">
					</li>
				</c:if>
				<c:if test="${item.quantity <= 0}">
				<li class="align-center">
					<span class="sold-out">품절</span>
				</li>
				</c:if>
			</ul>
		</form>
	</div>
	<hr size="1" noshade="noshade" width="100%">
	<p>
		${item.detail}
	</p>
	</c:if>
</div>
</body>
</html>