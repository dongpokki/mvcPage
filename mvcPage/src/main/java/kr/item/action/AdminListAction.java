package kr.item.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.controller.Action;
import kr.item.dao.ItemDAO;
import kr.item.vo.ItemVO;
import kr.util.PagingUtil;

public class AdminListAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		Integer user_num = (Integer)session.getAttribute("user_num");
		
		if(user_num == null) { // 회원이 아니라면
			return "redirect:/member/loginForm.do";
		}
		
		Integer user_auth = (Integer)session.getAttribute("user_auth");
		
		if(user_auth < 3) { // 관리자가 아니라면
			return "/WEB-INF/views/common/notice.jsp";
		}
		
		//관리자로 로그인 한 경우
		String pageNum = request.getParameter("pageNum");
		if(pageNum == null) {
			pageNum = "1";
		}
		
		String keyfield = request.getParameter("keyfield");
		String keyword = request.getParameter("keyword");
		
		ItemDAO dao = ItemDAO.getInstance();
		//status가 0이면 미표시(1), 표시(2) 모두 표시
		int count = dao.getItemCount(keyfield, keyword, 0);
		
		//페이지 처리
		PagingUtil page = new PagingUtil(keyfield, keyword, Integer.parseInt(pageNum), count, 20, 10, "list.do");
		
		List<ItemVO> list = null;
		if(count > 0) {
			list = dao.getListItem(page.getStartCount(),page.getEndCount(),keyfield,keyword,0);
		}
		
		request.setAttribute("count", count);
		request.setAttribute("list", list);
		request.setAttribute("pagingHtml", page.getPagingHtml());
		
		return "/WEB-INF/views/item/admin_list.jsp";
	}
}