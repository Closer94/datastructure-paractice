package common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class DB {
	// DB����(Connection ��ü ����)
	private static Connection conn = null;

	public DB() throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
//			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bus?serverTimezone=UTC", "root", "369369");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bus?serverTimezone=UTC", "root", "369369");
			System.out.println("������ ���̽� ���� ����");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		// "com.mysql.jdbc.Driver"
	}

	// ��� ���� �¼� ��Ȳ ���
	public void getSeatStatusAll() {
		HashMap<Integer, String> seatStatus = new HashMap<>();
		try {
			String sql = "select s.seatnum,ifnull(r.clientid, '���డ��') from seat s LEFT OUTER JOIN reserve r ON s.seatnum = r.seatnum;";

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
			System.out.println("[���� ���� ��Ȳ]");
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

	// �ش� �¼���ȣ�� ���� �¼����� ����
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

	// �ش� �¼���ȣ�� ����� �¼����� ���°� ����
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
	// ���� �� ���
	public boolean setClientInfo(String clientId) {
		try {

			String sql = "insert into client(clientid) values(?);";

			PreparedStatement pst = null;

			pst = conn.prepareStatement(sql);
			pst.setString(1, clientId);

			int result = pst.executeUpdate();
			pst.close();

			if (result > 0) {
//				System.out.println("��ϼ���");
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

	// ���� ���
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

	// ����� �� ���
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

	// ---------2.���� ��� �κ� by ����ö
	// ----------------------------------------------------

	// select clientid ,seatnum from reserve where clientid = "������id";
	// ���� ������ select seatnum from reserve where clientid = "������id"���� �����Ͽ� ����;
	// ���̵�� ������ �¼���ȣ�� �ִ��� ã�� �Լ�. ���̵� ������ �¼���ȣ�� ������ -1��ȯ
	public int getReservedSeatById(String clientId)  {

		// �Ѹ��� ������ �������� �� �� �����Ƿ� limit1�� �ؼ� ������� ������ �¼���ȣ�� ��ȯ�Ѵ�.
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

	// ���̺��� clientid�� ã�Ƽ� ����� ���� ����� �Լ�

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


	// id�� ã�� ���� ����� �Լ�
	// ***** ����� ���� �ٸ� ���� �������¼��� �ִ��� �˾ƺ����� by ���� �Լ� �߿� �ϳ��� ���ؼ�. �ٸ� ������ �¼��� �ִٸ� ��¥��
	// ���������� ��������!!!
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

	// �߰��� �Լ�
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
			System.out.println("����");
			return true;
//			e.printStackTrace();

		}
	}


}
