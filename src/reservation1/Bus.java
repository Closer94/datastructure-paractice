package reservation1;


public class Bus {
	String[] seat;
	int max;
	int count;

	public Bus() {
	      this.max = 2;
	      seat = new String[max];
	      //���� ����
	      for(int i = 0; i < max; i++){
	         seat[i] = "0";
	      }
	      count = 0;
	   }

	public boolean isFull() {
		if(count >= max) {
			return true;
		}
		return false;
	}
	
	//���� �¼� ��Ȳ ���
	public void getSeatStatus() {
		System.out.println("[���� ���� ��Ȳ]");
		System.out.print("|| ");
		for(int i = 0; i < max; i++) {
			System.out.print("[" + (i+1) + "] " + seat[i] + " || ");
		}
		System.out.println();
	}
	
	//�����ϴ� �޼���
	public boolean reserve(String clientId, int seatNum) {
//		if(seatNum <= 0 && seatNum >= max) {
		if(seatNum <= 0 && seatNum > max) {
			System.out.println("������ �� ���� �¼� ��ȣ�Դϴ�.");
			return false;
		}else if(!seat[seatNum-1].equals("0")) {
			System.out.println(seat[0]);
			System.out.println(seat[1]);
			System.out.println("�̹� ����� �¼� �Դϴ�.");
			return false;
		}else {
			seat[seatNum-1] = clientId;
			System.out.println("������ �Ϸ��Ͽ����ϴ�.");
		}
		count++;
		return true;
	} 
	
	//�ش� id���� ���� ���� Ȯ�� �޼��� (������ �ش� seatnum ��ȯ, ���н� -1) 
	public int getReservedClient(String clientId) {
		
		for(int i = 0; i < max; i++){
			if(seat[i].equals(clientId)) {
				return i+1;
			}
		}
		
		return -1;
	}

	//�ش� �ε����� ���� ���� ���(��� �ڸ��� 0���� )
	public void cancelReserve(int seatNum) {
		if(seatNum <= 0 && seatNum >= max) {
			System.out.println("������ �� ���� �¼� ��ȣ�Դϴ�.");
		}
		else {
			seat[seatNum-1] = "0";
			count--;
		}
	}

}
