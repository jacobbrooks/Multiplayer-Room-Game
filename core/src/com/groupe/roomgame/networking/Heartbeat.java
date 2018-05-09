/**
 * Michael E Anilonis
 * Apr 26, 2018
 */
package com.groupe.roomgame.networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.CopyOnWriteArrayList;

import com.groupe.roomgame.networking.election.FindOwnIP;

/**
 * @author manil
 *
 */
public class Heartbeat implements Runnable {
	private static final int port = 2700;
	private CopyOnWriteArrayList<InetAddress> ips;
	private boolean isLeader;

	/*public Heartbeat(CopyOnWriteArrayList<String> ip, boolean leader) {
		ips = Heartbeat.stringToInet(ip);
		isLeader = leader;
	}*/

	public Heartbeat(CopyOnWriteArrayList<InetAddress> ip, boolean leader) {
		ips = ip;
		isLeader = leader;
	}

	public Heartbeat(String ip, boolean leader) {
		ips = new CopyOnWriteArrayList<InetAddress>();
			try {
				ips.add(InetAddress.getByName(ip));
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		isLeader = leader;
	}

	public void run() {
		if (!isLeader) {
			DatagramSocket socket = null;
			try {
				socket = new DatagramSocket(port);
				DatagramPacket packet;
				socket.setSoTimeout(100);

				int timeouts = 0;
				while (!Thread.interrupted()) {
					packet = new DatagramPacket(makeHeartBeatPacket(), 1);
					try {
						socket.receive(packet);
						System.out.println("Packet Received");
						if (packet.getData()[0] != 100)
							throw new Error();
					} catch (SocketTimeoutException se) {
						timeouts++;
					}
					if (timeouts >= 3)
						break;
				}
				socket.close();
			} catch (IOException ie) {
				ie.printStackTrace();
			}
		} else {
			DatagramSocket socket = null;
			try {
				socket = new DatagramSocket(port);
				DatagramPacket packet;

				while (!Thread.interrupted()) {
					for (InetAddress ia : ips) {
						System.out.println(ia.getHostAddress());
						if (!ia.getHostAddress().equals(FindOwnIP.getMyIP())){
							packet = new DatagramPacket(makeHeartBeatPacket(), 1, ia, port);
							socket.send(packet);
							System.out.println("Packet Sent");
						}
					}
					
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			} catch (IOException ie) {
				ie.printStackTrace();
			}
		}
	}

	private byte[] makeHeartBeatPacket() {
		return new byte[] { 100 };
	}

	private static CopyOnWriteArrayList<InetAddress> stringToInet(CopyOnWriteArrayList<String> strs) {
		CopyOnWriteArrayList<InetAddress> r = new CopyOnWriteArrayList<InetAddress>();
		for (String str : strs) {
			try {
				r.add(InetAddress.getByName(str));
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		return r;
	}
}
