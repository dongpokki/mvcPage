package kr.item.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;

import kr.controller.Action;
import kr.item.dao.ItemDAO;
import kr.item.vo.ItemVO;
import kr.util.FileUtil;

public class AdminWriteAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		HttpSession session = request.getSession();
		Integer user_num = (Integer)session.getAttribute("user_num");
		
		if(user_num == null) { // 로그인 되지 않은 경우
			return "redirect:/member/loginForm.do";
		}
		
		Integer user_auth = (Integer)session.getAttribute("user_auth");
		
		if(user_auth < 3) { // 관리자로 로그인 하지 않은 경우
			return "/WEB-INF/views/common/notice.jsp";
		}
		
		//관리자로 로그인 한 경우
		
		//파일업로드를 위한 MiltipartRequest 객체 생성
		MultipartRequest multi = FileUtil.createFile(request);

		ItemVO item = new ItemVO();
		item.setName(multi.getParameter("name"));
		item.setPrice(Integer.parseInt(multi.getParameter("price")));
		item.setQuantity(Integer.parseInt(multi.getParameter("quantity")));
		item.setPhoto1(multi.getFilesystemName("photo1"));
		item.setPhoto2(multi.getFilesystemName("photo2"));
		item.setDetail(multi.getParameter("detail"));
		item.setStatus(Integer.parseInt(multi.getParameter("status")));
		
		ItemDAO dao = ItemDAO.getInstance();
		dao.insertItem(item);
		
		return "redirect:/item/list.do";
	}
}