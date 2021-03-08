package reservation1;


public class Bus {
	String[] seat;
	int max;
	int count;

	public Bus() {
	      this.max = 2;
	      seat = new String[max];
	      //버스 예약
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
	
	//예약 좌석 현황 출력
	public void getSeatStatus() {
		System.out.println("[버스 예약 현황]");
		System.out.print("|| ");
		for(int i = 0; i < max; i++) {
			System.out.print("[" + (i+1) + "] " + seat[i] + " || ");
		}
		System.out.println();
	}
	
	//예약하는 메서드
	public boolean reserve(String clientId, int seatNum) {
//		if(seatNum <= 0 && seatNum >= max) {
		if(seatNum <= 0 && seatNum > max) {
			System.out.println("예약할 수 없는 좌석 번호입니다.");
			return false;
		}else if(!seat[seatNum-1].equals("0")) {
			System.out.println(seat[0]);
			System.out.println(seat[1]);
			System.out.println("이미 예약된 좌석 입니다.");
			return false;
		}else {
			seat[seatNum-1] = clientId;
			System.out.println("예약을 완료하였습니다.");
		}
		count++;
		return true;
	} 
	
	//해당 id값에 대한 예약 확인 메서드 (성공시 해당 seatnum 반환, 실패시 -1) 
	public int getReservedClient(String clientId) {
		
		for(int i = 0; i < max; i++){
			if(seat[i].equals(clientId)) {
				return i+1;
			}
		}
		
		return -1;
	}

	//해당 인덱스에 대한 예약 취소(취소 자리는 0으로 )
	public void cancelReserve(int seatNum) {
		if(seatNum <= 0 && seatNum >= max) {
			System.out.println("예약할 수 없는 좌석 번호입니다.");
		}
		else {
			seat[seatNum-1] = "0";
			count--;
		}
	}

}
