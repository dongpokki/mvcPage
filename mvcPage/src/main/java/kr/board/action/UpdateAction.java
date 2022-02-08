package kr.board.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;

import kr.board.dao.BoardDAO;
import kr.board.vo.BoardVO;
import kr.controller.Action;
import kr.util.FileUtil;

public class UpdateAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		HttpSession session = request.getSession();
		Integer user_num = (Integer)session.getAttribute("user_num");
		
		if(user_num == null) { //회원 X
			return "redirect:/member/loginForm.do";
		}
		
		// post 방식으로 온 데이터 인코딩
		request.setCharacterEncoding("utf-8");
		
		// MultipartRequest 객체 생성
		MultipartRequest multi = FileUtil.createFile(request);
		
		// post 방식으로 온 데이터 반환
		String title = multi.getParameter("title");
		String content = multi.getParameter("content");
		String filename = multi.getFilesystemName("filename"); 
		int board_num = Integer.parseInt(multi.getParameter("board_num"));
		
		BoardDAO dao = BoardDAO.getInstance();
		BoardVO db_board = dao.getDetail(board_num); // 해당 게시글번호를 가진 게시글 조회
		
		if(user_num != db_board.getMem_num()) { //로그인한 회원번호와 게시글 작성자 회원번호가 불일치
			FileUtil.removeFile(request, filename); //업로드된 파일이 있으면 파일 삭제
			return "/WEB-INF/views/common/notice.jsp"; //접근 실패 페이지로 이동
		}
		
		//로그인한 회원번호와 작성자 회원번호가 일치하면 아래 작업들 수행
		BoardVO board = new BoardVO();
		
		// 게시글 등록 입력폼에서 전송된 데이터 반환하여 자바빈 객체에 set
		board.setBoard_num(board_num);
		board.setTitle(title);
		board.setContent(content);
		board.setIp(request.getRemoteAddr());
		board.setFilename(filename);
		
		//게시글 수정
		dao.updateBoard(board);
		
		//전송된 파일이 있을 경우 이전 파일 삭제
		if(filename != null) {
			FileUtil.removeFile(request, db_board.getFilename());
		}
		
		// 수정 완료페이지를 작성할 거면 request 저장 소스랑 jsp 파일 경로 반환 코드 작성
		// request.setAttribute("board", board);
		//return "/WEB-INF/views/board/update.jsp";
		
		// 수정 완료 페이지 작성 없이 바로 상세 페이지로 리다이렉트
		return "redirect:/board/detail.do?board_num="+board_num;
	}
}