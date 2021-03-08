package reservation1;

//DNode 가 MyDList에서만 사용되는 전용 클래스!
public class MyDList {
	public class DNode {
		Object data;	//저장
		DNode next;		//다음노드
		DNode prev;		//이전노드
		
		public DNode(Object data) {
			this.data = data;
			next = null;
			prev = null;
		}
	}
	
	//맴버 필드
	private DNode head;
	private DNode tail;
	private int   size;
	
	//생성자
	public MyDList() {
		head = null;
		tail = null;
		size = 0;
	}
	public void Clear() {
		head = tail = null;
		size = 0;
	}
	//get 메서드
	public DNode getHead() {
		return head;
	}
	
	//insert --------------------------------------------------------------
	public boolean push_front(Object data) {
		DNode newnode = new DNode(data);
		
		if(head == null) {  // head == null && tail == null 
			head = newnode;
			tail = newnode;
		}
		else {
			newnode.next = head;
			head.prev = newnode;
			head = newnode;
		}
		size++;
		return true;
	}

	public boolean push_back(Object data) {
		DNode newnode = new DNode(data);
		
		if(head == null) {  // head == null && tail == null 
			head = newnode;
			tail = newnode;
		}
		else {
			newnode.prev = tail;
			tail.next = newnode;
			tail = newnode;
		}
		size++;
		return true;
	}
	
	public boolean push_random(DNode cur, Object data) {		
		
		if(cur.next == null) {
			//마지막 노드 연결과정...
			return push_back(data);
		}
		
		//노드와 노드 중간에 연결하는 상황
		DNode newnode = new DNode(data);
		
		newnode.prev = cur;
		newnode.next = cur.next;
		cur.next = newnode;
		newnode.next.prev = newnode;
		
		size++;
		return true;		
	}
	
	//전체 순회 -----------------------------------------------------------
	public void Select_NextAll() {
		System.out.print("[" + size + "] ");
		
		DNode cur = head;	 //<---------------	1)
		while(cur != null) {
			//------------------------------
			int data = (int)cur.data;			//언박싱!(자동) : 기본형타입 = 레퍼런스타입
			System.out.print(data + " -> ");
			//------------------------------
			cur = cur.next;  //<-----------------2)
		}
		
		System.out.println();
	}
	
	public void Select_PrevAll() {
		System.out.print("[" + size + "] ");
		
		DNode cur = tail;	 //<---------------	1)
		while(cur != null) {
			//------------------------------
			//int data = (int)cur.data;			
			System.out.print(cur.data + " -> "); //언박싱!(자동) : 기본형타입 = 레퍼런스타입
			//------------------------------
			cur = cur.prev;  //<-----------------2)
		}
		
		System.out.println();
	}

	//검색(검색데이터 --> Node반환(실패: null)
	public DNode Select(Object key) {
		DNode cur = head;		
		while(cur != null) {
			//------------------------------
			int data = (int)cur.data;			//언박싱!(자동) : 기본형타입 = 레퍼런스타입
			if(data == (int)key){
				return cur;
			}
			//------------------------------
			cur = cur.next;
		}
		return null;
	}

	//delete -------------------------------------------------------------
	public boolean erase_front() {
		if( head == null) {		//비어있을 때
			return false;
		}
		else if(head.next == null) { //노드가 하나인상황  tail.prev == null
			head = tail = null;
		}
		else {  //노드가 2개 이상인 상황
			DNode del = head;
			
			head = del.next;
			head.prev = null;		
			
			//del.next.prev = null;
			//head = del.next;
		}
		size--;
		return true;
	}

	public boolean erase_back() {
		if( head == null) {		//비어있을 때
			return false;
		}
		else if(head.next == null) { //노드가 하나인상황  tail.prev == null
			head = tail = null;
		}
		else {  //노드가 2개 이상인 상황
			DNode delprev = tail.prev;
			delprev.next =null;
			tail = delprev;			
		}
		size--;
		return true;
	}

	//해당 노드 삭제!
	public boolean erase_random(DNode del) {
		if( del == null)
			return false;
		
		//삭제 연산
		if(del.prev == null) {		//첫번째 노드
			return erase_front();
		}
		else if( del.next == null) { //두번째 노드
			return erase_back();
		}
		else {
			del.prev.next = del.next;
			del.next.prev = del.prev;
		}
		size--;
		return true;		
	}
}









