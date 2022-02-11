package kr.item.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import kr.item.vo.ItemVO;
import kr.util.DBUtil;

public class ItemDAO {
	private static ItemDAO instance = new ItemDAO();
	
	public static ItemDAO getInstance() {
		return instance;
	}
	
	private ItemDAO() {}
	
	// 관리자 - 상품 등록
	public void insertItem(ItemVO item)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		
		try {
			//커넥션풀로부터 커넥션 할당 받음
			conn = DBUtil.getConnection();
			
			//sql문 작성
			sql = "insert into zitem (item_num,name,price,quantity,photo1,photo2,detail,status) values(zitem_seq.nextval,?,?,?,?,?,?,?)";
			
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			//?에 데이터 바인딩
			pstmt.setString(1, item.getName());
			pstmt.setInt(2, item.getPrice());
			pstmt.setInt(3, item.getQuantity());
			pstmt.setString(4, item.getPhoto1());
			pstmt.setString(5, item.getPhoto2());
			pstmt.setString(6, item.getDetail());
			pstmt.setInt(7, item.getStatus());
			
			//sql문 수행
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			//자원정리
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	
	
	// 관리자/사용자 - 전체 상품 갯수/검색 상품 갯수
	public int getItemCount(String keyfield,String keyword,int status)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String sub_sql = "";
		int count = 0;
		int cnt = 0;
		
		try {
			//커넥션풀로부터 커넥션 할당 받음
			conn = DBUtil.getConnection();
			
			if(keyword != null && !"".equals(keyword)) {
				//검색 처리
				if(keyfield.equals("1")) {
					sub_sql = "AND name LIKE ?";
				}else if(keyfield.equals("2")) {
					sub_sql = "AND detail LIKE ?";
				}
			}
			
			//sql문 작성
			sql = "select count(*) from zitem where status > ? " + sub_sql;
			
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			//?에 데이터 바인딩
			pstmt.setInt(++cnt,status);
			
			if(keyword != null && !"".equals(keyword)) {
				pstmt.setString(++cnt, "%"+keyword+"%");
			}
			
			//sql문 수행 - 결과행을 rs에 담음
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
	
	
	// 관리자/사용자 - 전체 목록/검색 목록
	public List<ItemVO> getListItem(int startRow, int endRow, String keyfield, String keyword, int status)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<ItemVO> list = null;
		String sql = null;
		String sub_sql = "";
		int cnt = 0;
		
		try {
			//커넥션풀로부터 커넥션 할당받음
			conn = DBUtil.getConnection();
			
			if(keyword != null && !"".equals(keyword)) {
				//검색 처리
				if(keyfield.equals("1")) {
					sub_sql = "and name like ?";
				}else if(keyfield.equals("2")) {
					sub_sql = "and detail like ?";
				}
			}
			
			//sql문 작성
			sql = "select * from (select a.*, rownum rnum from (select * from zitem where status > ? " + sub_sql + " order by item_num desc)a) where rnum>= ? and rnum <= ?";
			
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			//?에 데이터 바인딩
			pstmt.setInt(++cnt, status);
			if(keyword != null && !"".equals(keyword)) {
				pstmt.setString(++cnt, "%"+keyword+"%");
			}
			pstmt.setInt(++cnt, startRow);
			pstmt.setInt(++cnt, endRow);
			
			//sql문을 실행해서 결과행들을 rs에 담음
			rs = pstmt.executeQuery();
			
			list = new ArrayList<ItemVO>();
			while(rs.next()) {
				ItemVO item = new ItemVO();
				item.setItem_num(rs.getInt("item_num"));
				item.setName(rs.getString("name"));
				item.setPrice(rs.getInt("price"));
				item.setQuantity(rs.getInt("quantity"));
				item.setPhoto1(rs.getString("photo1"));
				item.setPhoto2(rs.getString("photo2"));
				item.setReg_date(rs.getDate("reg_date"));
				item.setModify_date(rs.getDate("modify_date"));
				item.setStatus(rs.getInt("status"));
				
				list.add(item);
			}
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		
		return list;
	}
	
	// 관리자/사용자 - 상품 상세
	// 관리자 - 상품 수정
	// 관리자 - 상품 삭제
}