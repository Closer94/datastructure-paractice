package reservation1;

import java.sql.SQLException;
import java.util.HashMap;

import common.DB;

public class Reservation {
	// ���� ��ü, ����� ť
	private DB db;
	// ���� ��� ���. ������ true�� ��ȯ�մϴ�.

	public Reservation() {
		try {
			db = new DB();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean cancel(String clientId) throws Exception {
		// ���� �¼����� ������� ID�� �˻��մϴ�. ID�� �˻����� ���ϸ� -1�� ��ȯ�ϰ� �˴ϴ�.
		// ID�� ��ġ�� index�� �ް� �˴ϴ�.

		int seatnum = db.getReservedSeatById(clientId);
		System.out.println("��������ϰ����ϴ� �¼���ȣ : " + seatnum);

		if (seatnum == -1) {
			System.out.println("error in reservation cancel");
			return false;
		} else {
			// �˻��ؼ� ã�Ƴ� �¼� ��ȣ�� ���� ������ ����մϴ�.
			db.deleteReserveById(clientId);
			db.setSeatStatus(seatnum, false);
			System.out.println("�¼� " + seatnum + " ��� �Ϸ�");

			// ����ڰ� ������ ������� �̸����� ���� �¼��� �����մϴ�
			String waitingClient = db.getWaitingClient();
			if (waitingClient != null) {
				db.deleteWaitngClientById(waitingClient);
				System.out.println("����� �����Ϸ�");

				setReservationInfo(waitingClient, seatnum);
				System.out.println("����� ����Ϸ�");
				return true;
			} else {
				return false;
			}
		}
	}

	// ��� ���. �ٸ� Ŭ�������� ȣ���� �ʿ䰡 ���⿡ private���� �����߽��ϴ�.
	public void standby(String clientId) {
		if (db.setWatingClient(clientId)) {
			System.out.println("�����̶� ����� ��ܿ� ���ϴ�.");
		} else {
			System.out.println("error in standby");
		}
	}

	// ���� ������ �ľ��մϴ�.
	public int getReservationResult(String id) {
		int seatNum = db.getReservedSeatById(id);
		if (seatNum == -1) {
//			System.out.println(id + "���� ����� ������ �������� �ʽ��ϴ�");
		} else {
//			System.out.println(id + "���� ����� �¼���ȣ = " + seatNum);
		}
		return seatNum;
	}

	// �¼��� �� ���� �ʾҴٸ� �¼��� �����մϴ�. á�ٸ� ��⿭�� ����մϴ�.
	public void setReservationInfo(String clientId, int seatNum) {
		if (db.getSeatStatus(seatNum) == 0) {
			if (db.setReserved(clientId, seatNum)) {
				if (db.setSeatStatus(seatNum, true) == false) {
					System.out.println("error in setSeatStatus");
				}
				System.out.printf("%s��, %d�� �¼� ���� �Ǿ����ϴ�\n", clientId, seatNum);
			} else {
				System.out.println("error in setReservationInfo");
			}
		} else {
			System.out.println("�̹� ����� �¼��Դϴ�. �ٸ� �¼��� ����ּ���.");
		}
	}

	public void showSeats() {
		db.getSeatStatusAll();
	}

	public boolean isFull() {
		return db.isFull();
	}

	public void signin(String id) {
		if (db.getClient(id) == true) {
			System.out.printf("�ݰ����ϴ� %s��\n", id);
		} else {
			if (db.setClientInfo(id)) {
				System.out.println(id + "�� ȸ����� �ϼ̽��ϴ�.");
			} else {
				System.out.println("error in signin");
			}
		}
	}

	public boolean isSignIn(String id) {
		return db.getClient(id);
	}
}
