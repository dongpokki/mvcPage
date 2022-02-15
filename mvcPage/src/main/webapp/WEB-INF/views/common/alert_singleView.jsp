<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<script type="text/javascript">
	<%-- jsp파일에서만 el문법 사용 가능 / js 파일에서는 불가능 --%>
	alert('${notice_msg}');
	location.href='${notice_url}';
</script>