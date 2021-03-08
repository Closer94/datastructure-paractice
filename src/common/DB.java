package common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class DB {
	// DB연결(Connection 객체 생성)
	private static Connection conn = null;

	public DB() throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
//			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bus?serverTimezone=UTC", "root", "369369");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bus?serverTimezone=UTC", "root", "369369");
			System.out.println("데이터 베이스 연결 성공");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		// "com.mysql.jdbc.Driver"
	}

	// 모든 예약 좌석 현황 출력
	public void getSeatStatusAll() {
		HashMap<Integer, String> seatStatus = new HashMap<>();
		try {
			String sql = "select s.seatnum,ifnull(r.clientid, '예약가능') from seat s LEFT OUTER JOIN reserve r ON s.seatnum = r.seatnum;";

			ResultSet rs = null;
			Statement st = null;

			st = conn.createStatement();
			rs = st.executeQuery(sql);

			while (rs.next()) {
				int seatNum = rs.getInt(1);
				String reserveStatus = rs.getString(2);
				seatStatus.put(seatNum, reserveStatus);
			}

			rs.close();
			st.close();
			System.out.println("[버스 예약 현황]");
			System.out.print("|| ");
			for (int seatIdx : seatStatus.keySet()) {
				String name = seatStatus.get(seatIdx);
				System.out.print("[" + (seatIdx) + "] " + name + " || ");
			}
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}

	}

	// 해당 좌석번호에 대한 좌석예약 유무
	public int getSeatStatus(int selectNum) {
		int isReserved = -1;
		String sql = "select * from seat where seatnum = " + selectNum + ";";
		try {
			ResultSet rs = null;
			Statement st = null;
			
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			
			while (rs.next()) {
				isReserved = rs.getInt(2);
			}
			
			rs.close();
			st.close();
			
		} catch (Exception e) {
			System.out.println("error");
		}

		return isReserved;
	}

	// 해당 좌석번호에 예약시 좌석예약 상태값 변경
	public boolean setSeatStatus(int selectNum,boolean isReserve) {
		try {

			String sql = "update seat set reservestatus = ? where seatnum = ?;";

			PreparedStatement pst = null;

			pst = conn.prepareStatement(sql);
			if(isReserve) {
				pst.setInt(1, 1);
			}else {
				pst.setInt(1, 0);
			}
			pst.setInt(2, selectNum);

			int result = pst.executeUpdate();
			pst.close();

			if (result > 0) {
				return true;
			} else {
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean getClient(String clientId) {
		int isReserved = -1;
		String sql = "select * from client where clientid = ?;";
		try {
			PreparedStatement pst = null;
			pst = conn.prepareStatement(sql);
			pst.setString(1, clientId);
			ResultSet rs = null;
			rs = pst.executeQuery();
			boolean result = rs.next();
			rs.close();
			pst.close();
			if(result) {
				return true;
			}else {
				return false;				
			}			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("error in getclient");
		}
		return false;
	}
	public String getWaitingClient() {
		String sql = "select clientid from waiting limit 1;";
		try {
			PreparedStatement pst = null;
			pst = conn.prepareStatement(sql);
			ResultSet rs = null;
			rs = pst.executeQuery();
			boolean result = rs.next();
			if(result) {
				String client = rs.getString(1);
				rs.close();
				pst.close();
				return client;
			}else {
				rs.close();
				pst.close();
				return null;				
			}			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("error in getwaitingclient");
		}
		return null;
	}
	// 예약 고객 등록
	public boolean setClientInfo(String clientId) {
		try {

			String sql = "insert into client(clientid) values(?);";

			PreparedStatement pst = null;

			pst = conn.prepareStatement(sql);
			pst.setString(1, clientId);

			int result = pst.executeUpdate();
			pst.close();

			if (result > 0) {
//				System.out.println("등록성공");
				return true;
			} else {
				return false;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	// 예약 등록
	public boolean setReserved(String clientId, int seatNum) {
		try {

			String sql = "insert into reserve(clientid, seatnum) values(?, ?)";

			PreparedStatement pst = null;

			pst = conn.prepareStatement(sql);
			pst.setString(1, clientId);
			pst.setInt(2, seatNum);

			int result = pst.executeUpdate();
			pst.close();

			if (result > 0) {
				return true;
			} else {
				return false;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	// 대기자 고객 등록
	public boolean setWatingClient(String clientId) {
		try {

			String sql = "insert into waiting(clientid) values(?)";

			PreparedStatement pst = null;

			pst = conn.prepareStatement(sql);
			pst.setString(1, clientId);

			int result = pst.executeUpdate();
			pst.close();

			if (result > 0) {
				return true;
			} else {
				return false;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	// ---------2.예약 취소 부분 by 변재철
	// ----------------------------------------------------

	// select clientid ,seatnum from reserve where clientid = "예약자id";
	// 위의 문장을 select seatnum from reserve where clientid = "예약자id"으로 수정하여 구현;
	// 아이디로 예약한 좌석번호가 있는지 찾는 함수. 아이디가 있으면 좌석번호를 없으면 -1반환
	public int getReservedSeatById(String clientId)  {

		// 한명이 여러개 구매했을 수 도 있으므로 limit1로 해서 먼저샀던 순으로 좌석번호를 반환한다.
		String sql = "select seatnum from reserve where clientId = ? limit 1;";
		ResultSet rs = null;
		PreparedStatement pst = null;
		int seatnum = -1;
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, clientId);
			rs = pst.executeQuery();
			boolean result = rs.next();
			if(result) {
				seatnum = rs.getInt(1);				
			}
			rs.close();
			pst.close();
			return seatnum;
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return -1;
	}

	// 테이블에서 clientid로 찾아서 대기자 한줄 지우는 함수

	public void deleteWaitngClientById(String clientId) {
		try {

			String sql = "delete from waiting where clientid = ?;";

			PreparedStatement pst = null;
			pst = conn.prepareStatement(sql);
			pst.setString(1, clientId);
			pst.executeUpdate();
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void deleteReserveById(String clientId) {
		try {
			String sql = "delete from reserve where clientid = ?;";			
			PreparedStatement pst = null;
			pst = conn.prepareStatement(sql);
			pst.setString(1, clientId);
			pst.executeUpdate();
			pst.close();
		} catch (SQLException e) {
			System.out.println("error in deleteReserveById");
			e.printStackTrace();
		}
	}


	// id로 찾아 고객을 지우는 함수
	// ***** 지우기 전에 다른 버스 예약한좌석이 있는지 알아봐야함 by 위에 함수 중에 하나를 통해서. 다른 구매한 좌석이 있다면 어짜피
	// 지워지지도 않을것임!!!
	public void deleteClientById(String clientId) {
		try {

			String sql = "delete from client where clientid = '?';";

			PreparedStatement pst = null;
			pst = conn.prepareStatement(sql);
			pst.setString(1, clientId);
			pst.executeUpdate();
			pst.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}

	// 추가된 함수
	public boolean isFull() {
		try {
			String sql = "select * from seat where reservestatus = '0';";

			ResultSet rs = null;
			Statement st = null;

			st = conn.createStatement();
			rs = st.executeQuery(sql);

			if(rs.next()) {
				return false;
			}else {
				return true;
			}
		} catch (SQLException e) {
			System.out.println("예외");
			return true;
//			e.printStackTrace();

		}
	}


}
