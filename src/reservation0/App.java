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

			System.out.print("1.�¼�����  2.�¼����  3.�¼���Ȳ : ");
			int num = Commons.sc.nextInt();
			Commons.sc.nextLine();
			switch (num) {

			case 1:
				System.out.print("���̵� �Է��ϼ��� : ");
				id = Commons.sc.nextLine();
				if (manager.getReserveStatus(id) != -1) {
					System.out.println("�̹� ������ ������Դϴ�.");
				} else {
					manager.signin(id);
					manager.doReserve(id);
				}
				break;
			case 2:
				System.out.print("���̵� �Է��ϼ��� : ");
				id = Commons.sc.nextLine();
				if (manager.getReserveStatus(id) == -1) {
					System.out.println("������ ����ڰ� �ƴմϴ�.");
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
		System.out.println("���α׷��� �����մϴ�.");
	}

}
