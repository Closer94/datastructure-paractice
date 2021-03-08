package reservation0;

import java.util.Scanner;

import common.Commons;

public class App {

	public Manager manager;
	private String id;

	public App() {

	}

	void init() {
		manager = new Manager();
	}

	void run() {

		while (true) {

			System.out.print("1.좌석예약  2.좌석취소  3.좌석현황 : ");
			int num = Commons.sc.nextInt();
			Commons.sc.nextLine();
			switch (num) {

			case 1:
				System.out.print("아이디를 입력하세요 : ");
				id = Commons.sc.nextLine();
				if (manager.getReserveStatus(id) != -1) {
					System.out.println("이미 예약한 사용자입니다.");
				} else {
					manager.signin(id);
					manager.doReserve(id);
				}
				break;
			case 2:
				System.out.print("아이디를 입력하세요 : ");
				id = Commons.sc.nextLine();
				if (manager.getReserveStatus(id) == -1) {
					System.out.println("예약한 사용자가 아닙니다.");
				} else {
					manager.doCancel(id);
				}
				break;
			case 3:
				manager.showSeats();
				break;
			default:
				break;
			}
			System.out.println();
			System.out.println();
		}

	}

	void exit() {
		System.out.println("프로그램을 종료합니다.");
	}

}
