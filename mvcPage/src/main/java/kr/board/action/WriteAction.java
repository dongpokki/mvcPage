package kr.board.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;

import kr.board.dao.BoardDAO;
import kr.board.vo.BoardVO;
import kr.controller.Action;
import kr.util.FileUtil;

public class WriteAction implements Action{

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
		
		// 자바빈(VO) 객체 생성
		BoardVO board = new BoardVO();
		
		// 게시글 등록 입력폼에서 전송된 데이터 반환하여 자바빈 객체에 set
		board.setTitle(title);
		board.setContent(content);
		board.setFilename(filename);
		board.setIp(request.getRemoteAddr());
		board.setMem_num(user_num);
		
		BoardDAO dao = BoardDAO.getInstance();
		dao.insertBoard(board);
		
		// jsp 파일 반환
		return "/WEB-INF/views/board/write.jsp";
	}
}