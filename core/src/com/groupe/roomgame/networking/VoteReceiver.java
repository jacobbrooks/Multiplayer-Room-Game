import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.ArrayList;
import java.io.IOException;

public class VoteReceiver extends Thread {
	
	private MulticastSocket socket;
	private InetAddress address;
	private ConcurrentLinkedQueue<String> queue;
	private ArrayList<Handler> members;
	public static boolean running = true;

	public VoteReceiver(int port, String inetAddress, ConcurrentLinkedQueue<String> queue) throws IOException {
		this.socket = new MulticastSocket(port);
		this.address = InetAddress.getByName(inetAddress);
		this.queue = queue;
		this.members = new ArrayList<Handler>();
		this.socket.joinGroup(address);
		this.socket.setSoTimeout(3000);
	}

	public void run(){
		byte[] buffer = new byte[256];
		int i = 0;
		while (running){
			try {
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				socket.receive(packet);
				members.add(i, new Handler(i, packet, queue));
				members.get(i++).start();

				String msg = new String(packet.getData());
				System.out.println("Received msg: " + msg);
			} catch (SocketTimeoutException e){
				if (!running)
					return;
			} catch (IOException ex){
				ex.printStackTrace();
			}
		}
	}
}