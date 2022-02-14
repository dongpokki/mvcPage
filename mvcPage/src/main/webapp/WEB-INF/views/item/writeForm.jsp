<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>상품 등록</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.6.0.min.js"></script>
<script type="text/javascript">
	$(function(){
		$('#write_form').submit(function(){
			if($('input[type="radio"]:checked').length <1){ // .length : radio 체크 갯수 구하기
				alert('상품 표시 여부를 지정하세요.');
				return false;
			}
			if($('#name').val().trim() == ''){
				alert('상품명을 입력하세요.');
				$('#name').val('').focus();
				return false;
			}
			if($('#price').val() == ''){
				alert('가격을 입력하세요.');
				$('#price').focus();
				return false;
			}
			if($('#quantity').val() == ''){
				alert('수량을 입력하세요.');
				$('#quantity').focus();
				return false;
			}
			if($('#photo1').val() == ''){
				alert('상품사진1을 선택하세요.');
				$('#photo1').focus();
				return false;
			}
			if($('#photo2').val() == ''){
				alert('상품사진2를 선택하세요.');
				$('#photo2').focus();
				return false;
			}
			if($('#detail').val().trim() == ''){
				alert('상품설명을 입력하세요.');
				$('#detail').val('').focus();
				return false;
			}
		});
	});
</script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<h2>상품 등록</h2>
	<form action="write.do" method="post" enctype="multipart/form-data" id="write_form">
		<ul>
			<li>
				<label>상품 표시 여부</label>
				<input type="radio" name="status" value="1" id="status1">미표시
				<input type="radio" name="status" value="2" id="status2">표시
			</li>
			<li>
				<label for="name">상품명</label>
				<input type="text" name="name" id="name" maxlength="10">
			</li>
			<li>
				<label for="price">가격</label>
				<input type="number" name="price" id="price" min="1" max="99999999">
			</li>
			<li>
				<label for="quantity">수량</label>
				<input type="number" name="quantity" id="quantity" min="0" max="99999">
			</li>
			<li>
				<label for="photo1">상품사진1</label>
				<input type="file" name="photo1" id="photo1" accept="image/gif,image/png,image/jpeg">
			</li>
			<li>
				<label for="photo2">상품사진2</label>
				<input type="file" name="photo2" id="photo2" accept="image/gif, image/png, image/jpeg">
			</li>
			<li>
				<label for="detail">상품설명</label>
				<textarea name="detail" id="detail" cols="30" rows="5"></textarea>
			</li>
		</ul>
		<div class="align-center">
			<input type="submit" value="등록">
			<input type="button" value="목록으로" onclick="location.href='list.do';">
		</div>
	</form>
</div>
</body>
</html>