<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
						//                  mvcPage/main/main.do 
						// 왜 views가 없지? : 서블릿이 forward 방식으로 jsp를 호출하기 때문에 정확한 경로는 겉으로 표기 X
	response.sendRedirect(request.getContextPath()+"/main/main.do");
%>