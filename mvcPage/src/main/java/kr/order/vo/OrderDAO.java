package kr.order.vo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

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


	// [관리자]목록/검색글 목록


	// [사용자]전체 글 갯수/검색글 갯수


	// [사용자]목록/검색글 목록


	// 상세 상품 정보 목록


	// 주문 상세


	// 주문 수정


	// 주문 삭제
	
	
	// 
}