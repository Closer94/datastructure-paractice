package reservation1;

import java.sql.SQLException;
import java.util.HashMap;

import common.DB;

public class Reservation {
	// 버스 객체, 대기자 큐
	private DB db;
	// 예약 취소 기능. 성공시 true를 반환합니다.

	public Reservation() {
		try {
			db = new DB();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean cancel(String clientId) throws Exception {
		// 버스 좌석에서 사용자의 ID를 검색합니다. ID를 검색하지 못하면 -1을 반환하게 됩니다.
		// ID가 위치한 index를 받게 됩니다.

		int seatnum = db.getReservedSeatById(clientId);
		System.out.println("예약취소하고자하는 좌석번호 : " + seatnum);

		if (seatnum == -1) {
			System.out.println("error in reservation cancel");
			return false;
		} else {
			// 검색해서 찾아낸 좌석 번호를 통해 예약을 취소합니다.
			db.deleteReserveById(clientId);
			db.setSeatStatus(seatnum, false);
			System.out.println("좌석 " + seatnum + " 취소 완료");

			// 대기자가 있으면 대기자의 이름으로 남은 좌석을 예약합니다
			String waitingClient = db.getWaitingClient();
			if (waitingClient != null) {
				db.deleteWaitngClientById(waitingClient);
				System.out.println("대기자 삭제완료");

				setReservationInfo(waitingClient, seatnum);
				System.out.println("대기자 예약완료");
				return true;
			} else {
				return false;
			}
		}
	}

	// 대기 기능. 다른 클래스에서 호출할 필요가 없기에 private으로 설정했습니다.
	public void standby(String clientId) {
		if (db.setWatingClient(clientId)) {
			System.out.println("만석이라 대기자 명단에 들어갑니다.");
		} else {
			System.out.println("error in standby");
		}
	}

	// 예약 정보를 파악합니다.
	public int getReservationResult(String id) {
		int seatNum = db.getReservedSeatById(id);
		if (seatNum == -1) {
//			System.out.println(id + "님은 예약된 정보가 존재하지 않습니다");
		} else {
//			System.out.println(id + "님의 예약된 좌석번호 = " + seatNum);
		}
		return seatNum;
	}

	// 좌석이 다 차지 않았다면 좌석을 예약합니다. 찼다면 대기열에 등록합니다.
	public void setReservationInfo(String clientId, int seatNum) {
		if (db.getSeatStatus(seatNum) == 0) {
			if (db.setReserved(clientId, seatNum)) {
				if (db.setSeatStatus(seatNum, true) == false) {
					System.out.println("error in setSeatStatus");
				}
				System.out.printf("%s님, %d번 좌석 예약 되었습니다\n", clientId, seatNum);
			} else {
				System.out.println("error in setReservationInfo");
			}
		} else {
			System.out.println("이미 예약된 좌석입니다. 다른 좌석을 골라주세요.");
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
			System.out.printf("반갑습니다 %s님\n", id);
		} else {
			if (db.setClientInfo(id)) {
				System.out.println(id + "님 회원등록 하셨습니다.");
			} else {
				System.out.println("error in signin");
			}
		}
	}

	public boolean isSignIn(String id) {
		return db.getClient(id);
	}
}
