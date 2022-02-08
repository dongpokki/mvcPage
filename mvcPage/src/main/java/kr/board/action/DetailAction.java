package kr.board.action;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.board.dao.BoardDAO;
import kr.board.vo.BoardVO;
import kr.controller.Action;
import kr.util.StringUtil;

public class DetailAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpSession session = request.getSession();
		Integer user_num = (Integer)session.getAttribute("user_num");
		Integer user_auth = (Integer)session.getAttribute("user_auth");
		
		//전달된 게시글 번호 반환
		int board_num = Integer.parseInt(request.getParameter("board_num"));
		
		BoardDAO dao = BoardDAO.getInstance();

		Cookie viewCookie=null;
		Cookie[] cookies=request.getCookies();

        if(cookies !=null) {
			for (int i = 0; i < cookies.length; i++) {
                //만들어진 쿠키들을 확인하며, 만약 들어온 적 있다면 생성되었을 쿠키가 있는지 확인
				if(cookies[i].getName().equals("|"+board_num+"|")) {
					viewCookie=cookies[i]; //찾은 쿠키를 변수에 저장
				}
			}
		}
        
		//만들어진 쿠키가 없음을 확인
		if(viewCookie==null) {
			//이 페이지에 왔다는 증거용(?) 쿠키 생성
			Cookie newCookie=new Cookie("|"+board_num+"|","readCount");
			newCookie.setMaxAge(24*60*60);
			response.addCookie(newCookie);
                
            //쿠키가 없으니 조회수 증가 로직 진행
			dao.plusHit(board_num);
		}
		
		//게시글 상세정보 받아오기
		BoardVO board = dao.getDetail(board_num);
		
		//HTML태그를 허용하지 않음
		board.setTitle(StringUtil.useNoHtml(board.getTitle()));
		//HTML태그를 허용하지 않으면서 줄바꿈처리
		board.setContent(StringUtil.useBrNoHtml(board.getContent()));

		//상세 게시글 이전,다음글 정보 받아오기
		int array_num[] = dao.getPrevNext(board_num);
		int prev = array_num[0];
		int next = array_num[1];
		
		//게시글 상세정보,세션(회원번호/등급) 저장
		request.setAttribute("board", board);
		request.setAttribute("user_num", user_num);
		request.setAttribute("user_auth", user_auth);
		request.setAttribute("prev", prev);
		request.setAttribute("next", next);

		//jsp 경로 반환
		return "/WEB-INF/views/board/detail.jsp";
	}
}