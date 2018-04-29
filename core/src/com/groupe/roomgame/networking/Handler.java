import java.net.DatagramPacket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Handler extends Thread {
	private int id;
	private DatagramPacket packet;
	private ConcurrentLinkedQueue<String> queue;

	public Handler(int id, DatagramPacket packet, ConcurrentLinkedQueue<String> queue){
		this.id = id;
		this.packet = packet;
		this.queue = queue;
	}

	public void run(){
		String ip = packet.getAddress().getHostAddress();
		queue.add(ip);
	}
}