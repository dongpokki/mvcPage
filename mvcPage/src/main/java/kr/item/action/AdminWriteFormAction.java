package kr.item.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.controller.Action;

public class AdminWriteFormAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		HttpSession session = request.getSession();
		Integer user_num = (Integer)session.getAttribute("user_num");
		
		if(user_num == null) { // 로그인이 안되어 있다면
			return "redirect:/member/loginForm.do";
		}
		
		Integer user_auth = (Integer)session.getAttribute("user_auth");
		if(user_auth < 3) { // 관리자로 로그인 하지 않은 경우
			return "/WEB-INF/views/common/notice.jsp";
		}
		
		// 관리자로 로그인 한 경우
		return "/WEB-INF/views/item/admin_writeForm.jsp";
	}
}