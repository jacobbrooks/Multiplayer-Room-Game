package com.groupe.roomgame.networking;

/*
* @author Dominic Dewhurst
*/

import java.net.InetAddress;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import java.io.IOException;

import com.groupe.roomgame.networking.packets.ElectionPacket;

public class Sender extends Thread {

	/* Class that can be used to send packets concurrently */
	
	private byte[] bytes;
	private InetAddress address;
	private DatagramSocket socket;
	private int port;

	public Sender(ElectionPacket packet, InetAddress address, int port) throws IOException {
		this.bytes = packet.getPacket();
		this.address = address;
		this.port = port;
		this.socket = new DatagramSocket();
	}

	public void run() {
		try {
			DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, port);
			socket.send(packet);
			System.out.println("Sent packet.");
		} catch (IOException e){
			e.printStackTrace();
		}
	}
}
