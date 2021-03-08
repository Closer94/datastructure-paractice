package reservation1;


public class MyLinkedQueue {
	private MyDList list = new MyDList();

	public boolean put(Object data){
		list.push_back(data);
		return true;
	}
	public Object Get() throws Exception {
		if(IsEmpty()) {
//			throw new Exception("비어있다");
			return null;
		}
		Object data = list.getHead().data;
		list.erase_front();
		return data;
	}
	public boolean IsEmpty() {
		return null == list.getHead();
	}
	public void PrintAll() {
		System.out.print("[front] ");  
		list.Select_NextAll();		
		System.out.print(" [rear]");
	}

	public void Clear() {
		list.Clear();
	}
	

}
