package kr.member.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.controller.Action;
import kr.member.dao.MemberDAO;
import kr.member.vo.MemberVO;
import kr.util.PagingUtil;

public class MemberListAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpSession session = request.getSession();
		Integer user_num = (Integer)session.getAttribute("user_num");
		
		if(user_num == null) { // 로그인 X
			return "redirect:/member/loginForm.do";
		}
		
		// 관리자인지 일반회원인지 체크
		Integer user_auth = (Integer)session.getAttribute("user_auth");
		
		if(user_auth < 3) { // 관리자(3)이 아닌 경우
			return "/WEB-INF/views/common/notice.jsp";
		}
		

		String pageNum = request.getParameter("pageNum"); // 현재 페이지 넘버 값 받아오기
		if(pageNum == null) { // 현재 페이지 넘버 없으면 (최초 진입)
			pageNum = "1"; // 페이지 넘버 1 지정
		}

		// 리스트 페이지에서 검색어 입력 후 검색 클릭 시 memberList.do(현재 페이지)에 get방식으로 데이터 넘겨주는 값 받아오기
		String keyfield = request.getParameter("keyfield"); // 현재 페이지 keyfield 값 받아오기
		String keyword = request.getParameter("keyword"); // 현재 페이지 keyword 값 받아오기
		if(keyfield == null) { // 현재 keyfield 없으면 (최초 진입)
			keyfield = "";
		}
		if(keyword == null) { // 현재 keyword 없으면 (최초 진입)
			keyword = "";
		}
		
		MemberDAO dao = MemberDAO.getInstance();
		int count = dao.getMemberCountByAdmin(keyfield, keyword);
		// 검색종류 선택하고 검색어 입력했으면, 값이 인자값으로 전달되고 특정 검색 완료한 결과 값(count)가 변수에 담김
		
		//페이징 처리
		//keyfield,keyword,currentPage,count,rowCount,pageCount,url
		//검색 종류, 검색어, 현재 페이지넘버, 레코드 개수, 한페이지 최대 노출 레코드 개수, 페이지 넘버링 개수, 페이지넘버링 표시할 페이지 주소
		PagingUtil page = new PagingUtil(keyfield,keyword,Integer.parseInt(pageNum),count,20,10,"memberList.do");
		
		List<MemberVO> list = null;
		if(count >0) { // 레코드가 있는 경우
			list = dao.getListMemberByAdmin(page.getStartCount(), page.getEndCount(), keyfield, keyword);
		}
		
		// 사용자 요청작업에 대한 데이터 처리 완료 후 데이터 request에 저장 
		request.setAttribute("count", count);
		request.setAttribute("list", list);
		request.setAttribute("pagingHtml", page.getPagingHtml());
		
		// jsp 파일 경로 반환
		return "/WEB-INF/views/member/memberList.jsp";
	}
}