package com.groupe.roomgame.networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.groupe.roomgame.networking.packets.DataPacket;
import com.groupe.roomgame.networking.election.IPs;
import com.groupe.roomgame.networking.election.FindOwnIP;

public class Updater {

	private DatagramSocket socket;
	
	public Updater() {
		try {
			this.socket = new DatagramSocket();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void update(DataPacket packet, boolean isLeader) {
		try {
			byte[] bytes = packet.getBytes();
			if (isLeader){
				for (InetAddress addr : IPs.getIPs){
					if (!addr.getHostAddress().equals(FindOwnIP.getMyIP())){
						DatagramPacket dgPacket = new DatagramPacket(bytes, bytes.length, addr, 6145);
						socket.send(dgPacket);
					}
				}
			} else {
				DatagramPacket dgPacket = new DatagramPacket(bytes, bytes.length, IPs.leader, 6145);
				socket.send(dgPacket);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
