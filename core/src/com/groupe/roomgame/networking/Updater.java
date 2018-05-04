package com.groupe.roomgame.networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.groupe.roomgame.networking.packets.DataPacket;

public class Updater {

	private DatagramSocket socket;
	private InetAddress address;
	
	public Updater() {
		try {
			this.socket = new DatagramSocket();
			this.address = InetAddress.getByName("pi.cs.oswego.edu");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void update(DataPacket packet) {
		try {
			byte[] bytes = packet.getBytes();
			DatagramPacket dgPacket = new DatagramPacket(bytes, bytes.length, address, 6145);
			socket.send(dgPacket);
			System.out.println("sent.");
		} catch (IOException e) {
			
		}
	}
}
