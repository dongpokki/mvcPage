package kr.member.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.controller.Action;
import kr.member.dao.MemberDAO;
import kr.member.vo.MemberVO;

public class RegisterUserAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// 회원가입 입력폼에서 전송된 데이터 인코딩 처리
		request.setCharacterEncoding("utf-8");
		
		// 자바빈(VO) 객체 생성
		MemberVO member = new MemberVO();
		
		// 회원가입 입력폼에서 전송된 데이터 반환하여 자바빈 객체에 set
		member.setId(request.getParameter("id"));
		member.setName(request.getParameter("name"));
		member.setPasswd(request.getParameter("passwd"));
		member.setPhone(request.getParameter("phone"));
		member.setEmail(request.getParameter("email"));
		member.setZipcode(request.getParameter("zipcode"));
		member.setAddress1(request.getParameter("address1"));
		member.setAddress2(request.getParameter("address2"));
		
		MemberDAO dao = MemberDAO.getInstance();
		dao.insertMember(member);
		
		// jsp 경로 반환
		return "/WEB-INF/views/member/registerUser.jsp";
	}
}