package kr.member.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.controller.Action;
import kr.member.dao.MemberDAO;
import kr.member.vo.MemberVO;

public class DetailUserFormAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpSession session = request.getSession();
		Integer user_num = (Integer)session.getAttribute("user_num");
		
		if(user_num == null) { // 로그인 X
			return "redirect:/member/loginForm.do";
		}
		
		Integer user_auth = (Integer)session.getAttribute("user_auth");
		
		if(user_auth < 3) { // 관리자가 아닌경우
			return "/WEB-INF/views/common/notice.jsp";
		}
		
		//[관리자]회원관리 페이지에서 하나의 회원 레코드를 클릭 시 get방식으로 넘겨진 회원 번호
		int mem_num = Integer.parseInt(request.getParameter("mem_num"));
		
		MemberDAO dao = MemberDAO.getInstance();
		MemberVO member = dao.getMember(mem_num);
		
		request.setAttribute("member", member);
		
		// jsp 파일 경로 반환
		return "/WEB-INF/views/member/detailUserForm.jsp";
	}	
}