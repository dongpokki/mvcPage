package kr.board.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.board.dao.BoardDAO;
import kr.board.vo.BoardVO;
import kr.controller.Action;

public class UpdateFormAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpSession session = request.getSession();
		Integer user_num = (Integer)session.getAttribute("user_num");
		Integer user_auth = (Integer)session.getAttribute("user_auth");
		if(user_num == null) { // 로그인 X
			return "redirect:/member/loginForm.do";
		}
		
		// 상세페이지에서 get 방식으로 전달받은 게시글 번호 반환
		int board_num = Integer.parseInt(request.getParameter("board_num"));
		
		// DAO 객체 생성
		BoardDAO dao = BoardDAO.getInstance();
		BoardVO board = dao.getDetail(board_num);
		
		// 해당 게시글의 작성자 회원번호 반환
		int mem_num = board.getMem_num();

		
		if(user_auth !=3) { // 관리자로 접속하지 않은 경우
			if(mem_num != user_num) { // 게시글을 작성한 계정과 현재 로그인된 계정이 일치하지 않는 경우
				return "/WEB-INF/views/common/notice.jsp";
			}			
		}
		
		request.setAttribute("board", board);

		// jsp 파일 경로 반환
		return "/WEB-INF/views/board/updateForm.jsp";
	}
}