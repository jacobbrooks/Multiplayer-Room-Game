import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class HoldElection {

	private static final int PORT = 2703;
	private static final String INET_ADDR = "224.0.03";
	private static ConcurrentLinkedQueue<String> queue;
	
	public static void main(String[] args) throws IOException {
		queue = new ConcurrentLinkedQueue<String>();
		VoteReceiver receiver = new VoteReceiver(PORT, INET_ADDR, queue);
		receiver.start();

		InetAddress address = InetAddress.getByName(INET_ADDR);

		Scanner sc = new Scanner(System.in);
		System.out.println("Enter msg: ");
		String message = sc.nextLine();
		
		DatagramSocket sendSocket = new DatagramSocket();
		byte[] bytes = message.getBytes();
		DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, PORT);
		sendSocket.send(packet);

		System.out.println("Press q: ");
		String quit = sc.nextLine();
		VoteReceiver.running = false;
		
		try {
			receiver.join();
		} catch (InterruptedException e){
			e.printStackTrace();
		}

		System.out.println(queue);
	}
}
