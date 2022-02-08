package kr.member.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import kr.member.vo.MemberVO;
import kr.util.DBUtil;

public class MemberDAO {
	// 싱글턴 패턴
	private static MemberDAO instance = new MemberDAO();
	
	public static MemberDAO getInstance() {
		return instance;
	}
	
	private MemberDAO() {}
	
	/*								 [일반회원]							*/
	// 회원가입
	public void insertMember(MemberVO member)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		ResultSet rs = null;
		String sql = null;
		int num = 0; // 시퀀스 번호 저장
		
		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			
			//오토커밋 해제(sql문이 1개가 아니므로 커밋을 수작업으로 해줘야함)
			conn.setAutoCommit(false);
			
			//회원번호(mem_num)생성 (존재하는 테이블컬럼이 아닌 특수한 작업을 할 경우 dual 테이블 이용)
			//두 테이블에 같은 회원번호를 넣기 위해 동일한 하나의 회원번호(시퀀스)먼저 조회(생성)
			sql = "select zmember_seq.nextval from dual";
			
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			//SQL문 테이블에 반영하고 결과행을 ResultSet에 담음
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				num = rs.getInt(1); // 컬럼인덱스 명시(1부터 시작)
			}
			
			//zmember 테이블에 데이터 저장
			sql = "insert into zmember (mem_num,id) values(?,?)"; // auth 컬럼은 default 2
			
			//PreparedStatemet 객체 생성
			pstmt2 = conn.prepareStatement(sql);
			
			//?에 데이터 바인딩
			pstmt2.setInt(1, num);
			pstmt2.setString(2, member.getId());
			
			//SQL 문장 실행
			pstmt2.executeUpdate();
			
			//zmember_detail 테이블에 데이터 저장
			sql = "insert into zmember_detail (mem_num,name,passwd,phone,email,zipcode,address1,address2) values (?,?,?,?,?,?,?,?)";
			
			//PreparedStatemet 객체 생성
			pstmt3 = conn.prepareStatement(sql);
			
			//?에 데이터 바인딩
			pstmt3.setInt(1, num);
			pstmt3.setString(2, member.getName());
			pstmt3.setString(3, member.getPasswd());
			pstmt3.setString(4, member.getPhone());
			pstmt3.setString(5, member.getEmail());
			pstmt3.setString(6, member.getZipcode());
			pstmt3.setString(7, member.getAddress1());
			pstmt3.setString(8, member.getAddress2());
			
			//SQL 문장 실행
			pstmt3.executeUpdate();
			
			//SQL문 실행 시 모두 성공하면 commit (sql문이 2개이상일 경우 커밋을 수작업으로 해줘야함)
			conn.commit();
			
		}catch(Exception e) {
			//SQL문이 하나라도 실패하면 rollback (sql문이 2개이상일 경우 롤백 수작업으로 해줘야함)
			conn.rollback();
			
			//예외 던지기
			throw new Exception(e);
		}finally {
			// 자원정리는 객체 생성 역순으로 (pstmt는 3개 생성했으므로, 자원정리도 3번)
			DBUtil.executeClose(null, pstmt3, null);
			DBUtil.executeClose(null, pstmt2, null);
			DBUtil.executeClose(rs, pstmt, conn);
		}
		
	}
	
	
	// ID중복 체크 및 로그인 처리
	public MemberVO checkMember(String id)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		MemberVO member = null;
		String sql = null;
		
		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			
			//sql문 작성 (zmember와 zmember_detail 테이블을 조인. zmember의 누락된 데이터가 보여야 id 중복 체크 가능)
			sql = "select * from zmember m LEFT OUTER JOIN zmember_detail d ON m.mem_num=d.mem_num where m.id=?";
			
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			//?에 데이터 바인딩
			pstmt.setString(1, id);
			
			//sql문 수행하여 결과집합을 rs에 담음
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				member = new MemberVO();
				member.setMem_num(rs.getInt("mem_num"));
				member.setId(rs.getString("id"));
				member.setAuth(rs.getInt("auth"));
				member.setPasswd(rs.getString("passwd"));
				member.setPhoto(rs.getString("photo"));
			}
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			//자원정리
			DBUtil.executeClose(rs, pstmt, conn);
		}
		
		return member;
	}
	
	
	// 회원 상세정보
	public MemberVO getMember(int mem_num)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		MemberVO member = null;
		String sql = null;
		
		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			
			//sql문 작성
			sql = "select * from zmember m join zmember_detail d on m.mem_num=d.mem_num where m.mem_num=?";
			
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			//?에 데이터 바인딩
			pstmt.setInt(1, mem_num);
			
			//sql문을 실행해서 결과행을 ResultSet에 담음
			rs = pstmt.executeQuery();
			if(rs.next()) {
				member = new MemberVO();
				member.setMem_num(rs.getInt("mem_num"));
				member.setId(rs.getString("id"));
				member.setAuth(rs.getInt("auth"));
				member.setPasswd(rs.getString("passwd"));
				member.setName(rs.getString("name"));
				member.setPhone(rs.getString("phone"));
				member.setEmail(rs.getString("email"));
				member.setZipcode(rs.getString("zipcode"));
				member.setAddress1(rs.getString("address1"));
				member.setAddress2(rs.getString("address2"));
				member.setPhoto(rs.getString("photo"));
				member.setReg_date(rs.getDate("reg_date"));
				member.setModify_date(rs.getDate("modify_date"));
			}
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		
		return member;
	}
	
	// 회원 정보수정
	public void updateMember(MemberVO member)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		
		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			
			//sql문 작성
			sql = "update zmember_detail set name=?,phone=?,email=?,zipcode=?,address1=?,address2=?,modify_date=sysdate where mem_num=?";
			
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			//?에 데이터 바인딩
			pstmt.setString(1, member.getName());
			pstmt.setString(2, member.getPhone());
			pstmt.setString(3, member.getEmail());
			pstmt.setString(4, member.getZipcode());
			pstmt.setString(5, member.getAddress1());
			pstmt.setString(6, member.getAddress2());
			pstmt.setInt(7, member.getMem_num());
			
			//sql문 수행
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}	
	}
	
	
	// 비밀번호 수정
	public void updatePassword(String passwd,Integer mem_num)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		
		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			
			//sql문 작성
			sql = "update zmember_detail set passwd=? where mem_num=?";
			
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			//?에 데이터 바인딩
			pstmt.setString(1, passwd);
			pstmt.setInt(2, mem_num);
			
			//sql문 수행
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	
	
	// 프로필 사진 수정
	public void updateMyPhoto(String photo,int mem_num)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		
		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			
			//sql문 작성
			sql = "update zmember_detail set photo=? where mem_num=?";
			
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			//?에 데이터 바인딩
			pstmt.setString(1, photo);
			pstmt.setInt(2, mem_num);
			
			//sql문 수행
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	
	// 회원탈퇴(회원 정보 삭제)
	public void deleteMember(int mem_num)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		String sql = null;
		
		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			
			//auto commit 해제
			conn.setAutoCommit(false);
			
			// zmember의 auth 값 변경
			// 정책상 탈퇴회원의 아이디로 재가입 할수 없기 때문에, zmember 테이블에 탈퇴유저의 데이터를 남겨놓는다.
			sql = "update zmember set auth=0 where mem_num =?";
			
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			//?에 데이터 바인딩
			pstmt.setInt(1, mem_num);
			
			//SQL문 실행
			pstmt.executeUpdate();
			
			// zmember_detail의 데이터 삭제
			sql = "delete from zmember_detail where mem_num =?";
			
			//PreparedStatement 객체 생성
			pstmt2 = conn.prepareStatement(sql);
			
			//?에 데이터 바인딩
			pstmt2.setInt(1, mem_num);
			
			//SQL문 실행
			pstmt2.executeUpdate();
			
			//모든 sql 수행 성공 시 커밋
			conn.commit();
		} catch (Exception e) {
			//sql 수행 실패 시 롤백
			conn.rollback();
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt2, null);
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	
	/*								 [관리자] 							*/
	// 총 회원 수
	public int getMemberCountByAdmin(String keyfield, String keyword)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String sub_sql = ""; // 검색어가 있는경우 검색어를 입력해서 검색할 where절을 작성할 문자열 형태의 변수
		int count = 0; // select 결과행의 레코드 개수
		
		try {
			//커넥션풀로부터 커넥션을 할당받음
			conn = DBUtil.getConnection();
		
			if(keyword != null && !"".equals(keyword)) { // 키워드(검색어)로 입력한 값이 있다면
				//검색글 처리
				if(keyfield.equals("1")) {
					sub_sql = "where id LIKE ?";
				}else if(keyfield.equals("2")) {
					sub_sql = "where name LIKE ?";
				}else if(keyfield.equals("3")) {
					sub_sql = "where email LIKE ?";
				}
			}
			
			//sql문 작성 (전체 또는 검색 레코드 개수)
			//조인 안해도 됨 select count(*) from zmember
			sql = "select count(*) from zmember m left outer join zmember_detail d using(mem_num) " + sub_sql;
			//												                       검색어가 없는 경우 빈문자열이 들어가기 때문에 전체 검색
			//																	   검색어가 있는 경우 sub_sql이 삽입 되어서 where 절 붙음
			
			//PreparedStatement객체 생성
			pstmt = conn.prepareStatement(sql);
			
			if(keyword != null && !"".equals(keyword)) { // 키워드(검색어)로 입력한 값이 있다면
				pstmt.setString(1, "%" + keyword + "%");
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
	
	
	// 회원 목록 조회
	public List<MemberVO> getListMemberByAdmin(int startRow, int endRow, String keyfield, String keyword) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<MemberVO> list = null;
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
					sub_sql = "where name LIKE ?";
				}else if(keyfield.equals("3")) {
					sub_sql = "where email LIKE ?";
				}
			}
			
			//SQL문 작성
			sql = "select * from (select a.*, rownum rnum from (select * from zmember m left outer join zmember_detail d using(mem_num) " + sub_sql + " order by reg_date desc nulls last)a) where rnum >=? and rnum <=?";
			
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
			list = new ArrayList<MemberVO>();
			
			while(rs.next()) {
				MemberVO member = new MemberVO();
				member.setMem_num(rs.getInt("mem_num"));
				member.setId(rs.getString("id"));
				member.setAuth(rs.getInt("auth"));
				member.setPasswd(rs.getString("passwd"));
				member.setName(rs.getString("name"));
				member.setPhone(rs.getString("phone"));
				member.setEmail(rs.getString("email"));
				member.setZipcode(rs.getString("zipcode"));
				member.setAddress1(rs.getString("address1"));
				member.setAddress2(rs.getString("address2"));
				member.setPhoto(rs.getString("photo"));
				member.setReg_date(rs.getDate("reg_date"));
				member.setModify_date(rs.getDate("modify_date"));
				
				//자바빈(VO)을 ArrayList에 저장
				list.add(member);
			}
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		
		return list;
	}

	
	// 회원 정보 수정
	public void updateMemberByAdmin(MemberVO member)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		String sql = null;
		
		try {
			//커넥션풀롭터 커넥션을 할당 받음
			conn = DBUtil.getConnection();
			
			//오토커밋 해제 (sql문 2개이상이므로)
			conn.setAutoCommit(false);
			
			//sql문 작성
			sql = "update zmember set auth=? where mem_num =?";
			
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			//?에 데이터 바인딩
			pstmt.setInt(1,member.getAuth());
			pstmt.setInt(2, member.getMem_num());
			
			//sql문 수행
			pstmt.executeUpdate();
			
			sql = "update zmember_detail set name=?,phone=?,email=?,zipcode=?,address1=?,address2=?,modify_date=sysdate where mem_num=?";
			
			//PreparedStatement 객체 생성
			pstmt2 = conn.prepareStatement(sql);
			
			//?에 데이터 바인딩
			pstmt2.setString(1, member.getName());
			pstmt2.setString(2, member.getPhone());
			pstmt2.setString(3, member.getEmail());
			pstmt2.setString(4, member.getZipcode());
			pstmt2.setString(5, member.getAddress1());
			pstmt2.setString(6, member.getAddress2());
			pstmt2.setInt(7,member.getMem_num());
			
			//sql문 수행
			pstmt2.executeUpdate();
			
			//모든 sql문이 정상적으로 실행되면 커밋
			conn.commit();
			
		}catch(Exception e) {
			// 하나라도 sql문이 예외처리 발생시 전체 롤백
			conn.rollback();
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt2, null);
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	
}