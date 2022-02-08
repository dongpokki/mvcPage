package kr.board.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import kr.board.vo.BoardReplyVO;
import kr.board.vo.BoardVO;
import kr.member.vo.MemberVO;
import kr.util.DBUtil;
import kr.util.DurationFromNow;
import kr.util.StringUtil;

public class BoardDAO {
	//싱글턴패턴
	private static BoardDAO instance = new BoardDAO();
	
	public static BoardDAO getInstance() {
		return instance;
	}
	
	private BoardDAO() {}
	
	
	//게시글 등록
	public void insertBoard(BoardVO board) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		
		try {
			// 커넥션풀로부터 커넥션풀 할당
			conn = DBUtil.getConnection();
			
			// sql문 작성
			sql = "insert into zboard (board_num,title,content,filename,ip,mem_num) values (zboard_seq.nextval,?,?,?,?,?)";
			
			// PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// ?에 데이터 바인딩
			pstmt.setString(1, board.getTitle());
			pstmt.setString(2, board.getContent());
			pstmt.setString(3, board.getFilename());
			pstmt.setString(4, board.getIp());
			pstmt.setInt(5, board.getMem_num());
			
			// SQL문 수행
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	
	
	//총 레코드 수 (검색 레코드 수)
	public int getBoardCount(String keyfield,String keyword)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String sub_sql = "";
		int count = 0;
		
		try {
			conn = DBUtil.getConnection();
			
			if(keyword != null && !"".equals(keyword)) {
				if(keyfield.equals("1")) {
					sub_sql = "where id like ?"; 
				}else if(keyfield.equals("2")) {
					sub_sql = "where title like ?";
				}else if(keyfield.equals("3")) {
					sub_sql = "where content like ?";
				}
			}
			
			sql = "select count(*) from zboard b join zmember m using(mem_num) " + sub_sql;
			
			pstmt = conn.prepareStatement(sql);
			
			if(keyword != null && !"".equals(keyword)) {
				pstmt.setString(1, "%" + keyword  +"%");
			}
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				count = rs.getInt(1);
			}
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		
		return count;
	}
	
	
	//게시글 목록
	public List<BoardVO> getList(int startRow, int endRow, String keyfield, String keyword) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BoardVO> list = null;
		String sql = null;
		String sub_sql = "";
		int cnt = 0; // 검색어가 있을경우, PreparedStatement 객체 내 ?에 데이터 바인딩을 해야하는데.. 순번이 가변적으로 바뀔 수 있어서 cnt값 변수를 사용
		
		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			
			if(keyword!=null && !"".equals(keyword)) { // keyword(검색어) 입력값이 있다면
				if(keyfield.equals("1")) {
					sub_sql = "where id LIKE ?";
				}else if(keyfield.equals("2")) {
					sub_sql = "where title LIKE ?";
				}else if(keyfield.equals("3")) {
					sub_sql = "where content LIKE ?";
				}
			}
			
			//SQL문 작성
			sql = "select * from (select a.*, rownum rnum from (select * from zmember m join zboard b using(mem_num) " + sub_sql + " order by reg_date desc nulls last)a) where rnum >=? and rnum <=?";
			
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			if(keyword!=null && !"".equals(keyword)) {
				pstmt.setString(++cnt, "%" + keyword + "%");
			}
			
			pstmt.setInt(++cnt, startRow);
			pstmt.setInt(++cnt, endRow);
			
			//sql문 수행하고 결과집합을 rs에 담음
			rs = pstmt.executeQuery();
			
			//ArrayList 객체 생성
			list = new ArrayList<BoardVO>();
			
			while(rs.next()) {
				BoardVO board = new BoardVO();
				board.setMem_num(rs.getInt("mem_num"));
				board.setId(rs.getString("id"));
				board.setBoard_num(rs.getInt("board_num"));
				//HTML태그를 허용하지 않음
				board.setTitle(StringUtil.useNoHtml(rs.getString("title")));
				//board.setTitle(rs.getString("title"));
				board.setContent(StringUtil.useNoHtml(rs.getString("content")));
				//board.setContent(rs.getString("content"));
				board.setHit(rs.getInt("hit"));
				board.setReg_date(rs.getDate("reg_date"));
				board.setModify_date(rs.getDate("modify_date"));
				board.setFilename(rs.getString("filename"));
				board.setIp(rs.getString("ip"));
				
				//자바빈(VO)을 ArrayList에 저장
				list.add(board);
			}
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		
		return list;
	}
	
	
	//게시글 상세
	public BoardVO getDetail(int board_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		BoardVO board = null;
		
		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			
			//sql문 작성
			sql = "select * from zmember m join zboard b using(mem_num) where board_num = ?";
			
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			//?에 데이터바인딩
			pstmt.setInt(1, board_num);
			
			//sql문 수행하고 결과집합을 rs에 담음
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				board = new BoardVO();
				board.setBoard_num(rs.getInt("board_num"));
				board.setTitle(rs.getString("title"));
				board.setContent(rs.getString("content"));
				board.setId(rs.getString("id"));
				board.setHit(rs.getInt("hit"));
				board.setReg_date(rs.getDate("reg_date"));
				board.setModify_date(rs.getDate("modify_date"));
				board.setFilename(rs.getString("filename"));
				board.setIp(rs.getString("ip"));
				board.setMem_num(rs.getInt("mem_num"));
			}
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return board;
	}
	
	
	//이전, 다음글 구하기
	public int[] getPrevNext(int board_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		int[] array_num = {0,0};
		
		try {
			conn = DBUtil.getConnection();
			
			sql = "select prevnum,nextnum from(select title,content,board_num,LAG(board_num,1,-1) OVER(ORDER BY board_num ASC) prevnum, LEAD(board_num,1,-2) OVER(ORDER BY board_num ASC) nextnum from zboard) where board_num = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1,board_num);
			
			rs =  pstmt.executeQuery();
						
			if(rs.next()) {
				array_num[0] = rs.getInt("prevnum");
				array_num[1] = rs.getInt("nextnum");
			}
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		
		return array_num;
	}
	
	
	//조회수 증가
	public void plusHit(int board_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		
		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			
			//sql문 작성
			sql = "update zboard set hit=hit+1 where board_num = ?";
			
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			//?에 데이터바인딩
			pstmt.setInt(1, board_num);
			
			//sql 문 수행
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	
	
	//게시글 수정
	public void updateBoard(BoardVO board) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		String sub_sql = "";
		int count = 0;
		
		try {
			// 커넥션풀로부터 커넥션풀 할당
			conn = DBUtil.getConnection();
			
			if(board.getFilename() != null) {
				sub_sql = ",filename=?";
			}
			
			// sql문 작성
			sql = "update zboard set title=?,content=?,modify_date=sysdate" + sub_sql + ",ip=? where board_num =?";
			
			// PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
						
			// ?에 데이터 바인딩
			pstmt.setString(++count, board.getTitle());
			pstmt.setString(++count, board.getContent());
			if(board.getFilename() != null) {
				pstmt.setString(++count, board.getFilename());
			}
			pstmt.setString(++count, board.getIp());
			pstmt.setInt(++count, board.getBoard_num());
			
			// SQL문 수행
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	
	
	//파일 삭제
	public void deleteFile(int board_num)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		
		try {
			//커넥션풀로부터 커넥션풀 할당
			conn = DBUtil.getConnection();
			
			//sql문 작성
			sql = "update zboard set filename='' where board_num=?";
			
			//PreparedStatement객체 생성
			pstmt = conn.prepareStatement(sql);
			
			//?에 데이터 바인딩
			pstmt.setInt(1, board_num);
			
			//sql문 실행
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	
	
	//게시글 삭제
	public void deleteBoard(int board_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		String sql = null;
		
		try {
			// 커넥션풀로부터 커넥션풀 할당
			conn = DBUtil.getConnection();
			
			// 오토커밋 해제
			conn.setAutoCommit(false);
			
			//댓글 삭제
			sql = "delete from zboard_reply where board_num=?";
			
			// PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
						
			// ?에 데이터 바인딩
			pstmt.setInt(1, board_num);
						
			// SQL문 수행
			pstmt.executeUpdate();
			
			// 부모글 삭제 sql문 작성
			sql = "delete from zboard where board_num=?";
			
			// PreparedStatement 객체 생성
			pstmt2 = conn.prepareStatement(sql);
			
			// ?에 데이터 바인딩
			pstmt2.setInt(1, board_num);
			
			// SQL문 수행
			pstmt2.executeUpdate();
			
			//정상적으로 sql문 모두 수행 성공 시 
			conn.commit();
		} catch (Exception e) {
			//sql문이 하 나라도 실패하면
			conn.rollback();
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt2, null);
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	
	//댓글 등록
	public void insertReplyBoard(BoardReplyVO boardReply)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		
		try {
			// 커넥션풀로부터 커넥션풀 할당
			conn = DBUtil.getConnection();
			
			//댓글 삭제
			sql = "insert into zboard_reply (re_num,re_content,re_ip,mem_num,board_num) values (zboard_reply_seq.nextval,?,?,?,?)";
			
			// PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
						
			// ?에 데이터 바인딩
			pstmt.setString(1, boardReply.getRe_content());
			pstmt.setString(2, boardReply.getRe_ip());
			pstmt.setInt(3, boardReply.getMem_num());
			pstmt.setInt(4, boardReply.getBoard_num());
						
			// SQL문 수행
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	
	//댓글 갯수
	public int getReplyBoardCount(int board_num)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		int count = 0;
		
		try {
			// 커넥션풀로부터 커넥션풀 할당
			conn = DBUtil.getConnection();
			
			//댓글 삭제
			// sql = "select count(*) from zboard_reply b join zmember m on using(mem_num) where b.board_num = ?";
			sql = "select count(*) from zobard_reply where board_num = ?";
			
			// PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
						
			// ?에 데이터 바인딩
			pstmt.setInt(1, board_num);
						
			// SQL문 수행하여 결과 집합을 rs에 담음
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				count = rs.getInt(1);
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		
		return count;
	}
	
	//댓글 목록
	public List<BoardReplyVO> getListReplyBoard(int startRow, int endRow, int board_num)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BoardReplyVO> list = null;
		String sql = null;
		
		try {
			//커넥션풀로부터 커넥션풀 할당받음
			conn = DBUtil.getConnection();
			
			//sql문 작성
			sql = "select * from (select a.*, rownum rnum from (select b.re_num, TO_CHAR(b.re_date,'YYYY-MM-DD HH24:MI:SS') re_date, TO_CHAR(b.re_modifydate,'YYYY-MM-DD HH24:MI:SS') re_modifydate, b.re_content, b.board_num, mem_num,m.id from zboard_reply b join zmember m using(mem_num) where b.board_num=? order by b.re_num desc)a) where rnum>=? and rnum<=?";
			
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			//?에 데이터 바인딩
			pstmt.setInt(1, board_num);
			pstmt.setInt(2, startRow);
			pstmt.setInt(3, endRow);
			
			//sql문 수행하고 결과집합을 rs에 담음
			rs = pstmt.executeQuery();
			
			// list 객체 생성
			list = new ArrayList<BoardReplyVO>();
	
			while(rs.next()) {
				// BoardReplyVO 객체 생성
				BoardReplyVO reply = new BoardReplyVO();
				reply.setRe_num(rs.getInt("re_num")); // 댓글 PK
				
				// 날짜 - > 1분전,1시간전,1일전 형식의 문자열로 변환
				reply.setRe_date(DurationFromNow.getTimeDiffLabel(rs.getString("re_date")));
				if(rs.getString("re_modifydate") != null) { // 수정일이 있는 경우
					reply.setRe_modifydate(DurationFromNow.getTimeDiffLabel(rs.getString("re_modifydate")));
				}
				
				reply.setRe_content(StringUtil.useBrNoHtml(rs.getString("re_content")));
				reply.setBoard_num(rs.getInt("board_num"));
				reply.setMem_num(rs.getInt("mem_num"));
				reply.setId(rs.getString("id"));
				
				list.add(reply);
			}
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		
		return list;
	}
	
	//댓글 상세
	
	//댓글 수정
	
	//댓글 삭제
	
}