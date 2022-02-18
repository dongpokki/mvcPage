package kr.order.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import kr.order.vo.OrderDetailVO;
import kr.order.vo.OrderVO;
import kr.util.DBUtil;

public class OrderDAO {
	//싱글턴 패턴
	private static OrderDAO instance = new OrderDAO();
	
	public static OrderDAO getinstance() {
		return instance;
	}
	
	private OrderDAO() {}

	// 주문 등록
	public void inserOrder(OrderVO order, List<OrderDetailVO> orderDetailList) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		PreparedStatement pstmt4 = null;
		PreparedStatement pstmt5 = null;
		ResultSet rs = null;
		String sql = null;
		int order_num = 0;
		
		try {
			
			//커넥션풀로부터 커넥션 할당받음
			conn = DBUtil.getConnection();
			
			//오토 커밋 해제
			conn.setAutoCommit(false);
			
			//sql문 작성
			sql = "select zorder_seq.nextval from dual";
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);			
			//sql문 수행해서 결과행을 rs에 담음
			rs = pstmt.executeQuery();
			if(rs.next()) {
				order_num = rs.getInt(1);
			}
			
			// zorder에 주문 정보 삽입
			sql = "insert into zorder (order_num, item_name, order_total, payment, receive_name, receive_post, receive_address1, receive_address2, receive_phone, notice, mem_num) values(?,?,?,?,?,?,?,?,?,?,?)";
			//PreparedStatement 객체 생성
			pstmt2 = conn.prepareStatement(sql);			
			//?에 데이터 바인딩
			pstmt2.setInt(1, order_num);
			pstmt2.setString(2, order.getItem_name());
			pstmt2.setInt(3, order.getOrder_total());
			pstmt2.setInt(4, order.getPayment());
			pstmt2.setString(5, order.getReceive_name());
			pstmt2.setString(6, order.getReceive_post());
			pstmt2.setString(7, order.getReceive_address1());
			pstmt2.setString(8, order.getReceive_address2());
			pstmt2.setString(9, order.getReceive_phone());
			pstmt2.setString(10, order.getReceive_notice());
			pstmt2.setInt(11, order.getMem_num());
			//insert sql문 수행
			pstmt2.executeUpdate();
			
			// zorder_detail에 주문 상세 정보 삽입
			sql = "insert into zorder_detail (detail_num, item_num, item_name, item_price, item_total, order_quantity, order_num) values(zdetail_seq.nextval,?,?,?,?,?,?)";
			//PreparedStatement 객체 생성
			pstmt3 = conn.prepareStatement(sql);			
			for(int i=0; i<orderDetailList.size(); i++) {
				OrderDetailVO orderDetail = orderDetailList.get(i);
				//?에 데이터 바인딩
				pstmt3.setInt(1, orderDetail.getItem_num());
				pstmt3.setString(2, orderDetail.getItem_name());
				pstmt3.setInt(3, orderDetail.getItem_price());
				pstmt3.setInt(4, orderDetail.getItem_total());
				pstmt3.setInt(5, orderDetail.getOrder_quantity());
				pstmt3.setInt(6, order_num);
				pstmt3.addBatch(); //쿼리를 메모리에 올림
				
				//계속 추가하면 outOfMemory 발생, 1000개 단위로 executeBatch()
				if(i % 1000 == 0) { // 수행해야할 쿼리문의 데이터 바인딩이 1000개가 넘는 경우 executeBatch() 메서드를 한번씩 해줘야한다.
									// 현재 데이터 바인딩이 6개 이므로 이 작업은 생략 가능하다
					pstmt3.executeBatch();
				}
					
			}
			
			pstmt3.executeBatch(); // addBatch로 메모리에 올린 쿼리를 전송
			
			//상품의 재고수 차감
			sql = "update zitem set quantity=quantity-? where item_num=?";
			//PreparedStatement 객체 생성
			pstmt4 = conn.prepareStatement(sql);
			
			for(int i=0; i<orderDetailList.size(); i++) {
				OrderDetailVO orderDetail = orderDetailList.get(i);
				//?에 데이터 바인딩
				pstmt4.setInt(1, orderDetail.getOrder_quantity());
				pstmt4.setInt(2, orderDetail.getItem_num());
				pstmt4.addBatch(); //쿼리를 메모리에 올림
				
				//계속 추가하면 outOfMemory 발생, 1000개 단위로 excuteBatch()
				if(i % 1000 == 0) {
					pstmt4.executeBatch();
				}
			}
			pstmt4.executeBatch();
			
			//카트에서 주문 상품 삭제
			sql = "delete from zcart where mem_num=?";
			//PreparedStatement 객체 생성
			pstmt5 = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt5.setInt(1, order.getMem_num());
			//sql문 실행
			pstmt5.executeUpdate();
			
			//모든 sql문이 성공하면 commit
			conn.commit();
		} catch (Exception e) {
			//sql문이 하나라도 실패하면 rollback
			conn.rollback();
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt5, null);
			DBUtil.executeClose(null, pstmt4, null);
			DBUtil.executeClose(null, pstmt3, null);
			DBUtil.executeClose(null, pstmt2, null);
			DBUtil.executeClose(rs, pstmt, conn);
		}
		
	}

	// [관리자]전체 글 갯수/검색글 갯수
	public int getOrderCount(String keyfield, String keyword)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String sub_sql = "";
		int count = 0;
		
		try {
			//커넥션 풀로부터 커넥션을 할당
			conn = DBUtil.getConnection();
			
			if(keyword != null && !"".equals(keyword)) {
				if(keyfield.equals("1")) {
					sub_sql = "where order_num = ?";
				}else if(keyfield.equals("2")) {
					sub_sql = "where id like ?";
				}else if(keyfield.equals("3")) {
					sub_sql = "where item_name like ?";
				}	
			}
			
			sql = "select count(*) from zorder o join zmember m on o.mem_num = m.mem_num " + sub_sql;
			
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			//?에 데이터 바인딩
			if(keyword != null && !"".equals(keyword)) {
				if(keyfield.equals("1")) {
					pstmt.setString(1, keyword);
				}else {
					pstmt.setString(1, "%" + keyword + "%");
				}
			}
			
			//sql문을 실행해서 결과행을 rs에 담음
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

	// [관리자]목록/검색글 목록
	public List<OrderVO> getListOrder(int startRow, int endRow, String keyfield, String keyword)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<OrderVO> list = null;
		String sql = null;
		String sub_sql = "";
		int cnt = 0;
		
		try {
			//커넥션풀로부터 커넥션 할당 받음
			conn = DBUtil.getConnection();
			
			if(keyword != null && !"".equals(keyword)) {
				if(keyfield.equals("1")) {
					sub_sql = "where order_num = ?";
				}else if(keyfield.equals("2")) {
					sub_sql = "where id like ?";
				}else if(keyfield.equals("3")) {
					sub_sql = "where item_name like ?";
				}
			}
			
			//sql문 작성
			sql = "select * from (select a.*, rownum rnum from (select * from zorder o join zmember m on o.mem_num = m.mem_num " + sub_sql + " order by order_num desc)a) where rnum>=? and rnum<=?";

			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			//?에 데이터 바인딩
			if(keyword != null && !"".equals(keyword)) {
				if(keyfield.equals("1")) {
					pstmt.setString(++cnt, keyword);
				}else {
					pstmt.setString(++cnt, "%"+ keyword +"%");
				}
			}
			pstmt.setInt(++cnt, startRow);
			pstmt.setInt(++cnt, endRow);
			
			//sql문을 실행해서 결과행들을 rs에 담음
			rs = pstmt.executeQuery();
			
			list = new ArrayList<OrderVO>();
			while(rs.next()) {
				OrderVO order = new OrderVO();
				order.setOrder_num(rs.getInt("order_num"));
				order.setItem_name(rs.getString("item_name"));
				order.setOrder_total(rs.getInt("order_total"));
				order.setStatus(rs.getInt("status"));
				order.setReg_date(rs.getDate("reg_date"));
				order.setId(rs.getString("id"));
				
				list.add(order);
			}
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		
		return list;
	}

	// [사용자]전체 글 갯수/검색글 갯수


	// [사용자]목록/검색글 목록


	// 개별 상품 목록
	public List<OrderDetailVO> getListOrderDetail(int order_num)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<OrderDetailVO> list = null;
		String sql = null;
		
		try {
			//커넥션풀로부터 커넥션 할당 받음
			conn = DBUtil.getConnection();
			
			//sql문
			sql = "select * from zorder_detail where order_num = ? order by item_num desc";
			
			//PreparedStatement객체 생성
			pstmt = conn.prepareStatement(sql);
			
			//?에 데이터를 바인딩
			pstmt.setInt(1, order_num);
			
			//sql문을 실행해서 결과행들을 rs에 담음
			rs = pstmt.executeQuery();
			
			list = new ArrayList<OrderDetailVO>();
			while(rs.next()) {
				OrderDetailVO detail = new OrderDetailVO();
				detail.setItem_name(rs.getString("item_name"));
				detail.setItem_price(rs.getInt("item_price"));
				detail.setItem_total(rs.getInt("item_total"));
				detail.setOrder_quantity(rs.getInt("order_quantity"));
				
				list.add(detail);
			}
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		
		return list;
	}

	// [관리자/사용자] 주문 상세
	public OrderVO getOrder(int order_num)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		OrderVO order = null;
		String sql = null;
		
		try {
			//커넥션풀로부터 커넥션 할당 받음
			conn = DBUtil.getConnection();
			
		
			//sql문
			sql = "select * from zorder where order_num = ? ";
			
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			//?에 데이터 바인딩
			pstmt.setInt(1, order_num);
			
			//sql문 수행하여 결과집합 rs에 담음
			rs = pstmt.executeQuery();
			if(rs.next()) {
				order = new OrderVO();
				order.setOrder_num(rs.getInt("order_num"));
				order.setItem_name(rs.getString("item_name"));
				order.setOrder_total(rs.getInt("order_total"));
				order.setPayment(rs.getInt("payment"));
				order.setStatus(rs.getInt("status"));
				order.setReceive_name(rs.getString("receive_name"));
				order.setReceive_post(rs.getString("receive_post"));
				order.setReceive_phone(rs.getString("receive_phone"));
				order.setReceive_address1(rs.getString("receive_address1"));
				order.setReceive_address2(rs.getString("receive_address2"));
				order.setReceive_notice(rs.getString("notice"));
				order.setReg_date(rs.getDate("reg_date"));
				order.setMem_num(rs.getInt("mem_num"));
			}
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		
		return order;
	}

	// 주문 수정
	public void updateOrder(OrderVO order)throws Exception{
		
	}

	// 주문 삭제
	public void deleteOrder(int order_num)throws Exception{
		
	}
	 
}