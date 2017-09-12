
public class ThreadSmartS extends Thread{
	
	public ThreadSmartS(String name) {
		super(name);
	}
	public void run() {
		for(int i=0;i<10;i++);
	}
}
