package reservation0;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;

import common.Client;
import common.Commons;
import common.DB;
import reservation1.Reservation;

public class Manager {
	
	private Reservation reservation;
	
	
	public Manager() {
		reservation = new Reservation();
	}
	
	public void signin(String id) {
		
		reservation.signin(id);
	}
	
	public void doReserve(String id) {
		// id Ȯ�� �� id ���
		
		if(reservation.isFull() == false) {
			reservation.showSeats();
			System.out.print("���� �¼��� �����ϼ��� : ");
			int choice = Commons.sc.nextInt();
			Commons.sc.nextLine();		
			reservation.setReservationInfo(id, choice);			
		}else {
			reservation.standby(id);
		}
	}
	
	public void doCancel(String id) {
		try {
			reservation.cancel(id);
			reservation.showSeats();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getSeatStatus(String id) {
		reservation.getReservationResult(id);
	}
	
	public void showSeats() {
		reservation.showSeats();
	}

	public boolean checkSignIn(String id) {
		if(reservation.isSignIn(id)) {
			return true;			
		}else {
			return false;
		}		
	}
	public int getReserveStatus(String id) {
		return reservation.getReservationResult(id);
	}
}
