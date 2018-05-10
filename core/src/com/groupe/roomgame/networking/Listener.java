package com.groupe.roomgame.networking;

import java.net.*;
import java.io.*;
import java.util.concurrent.ConcurrentHashMap;

import com.groupe.roomgame.networking.packets.DataPacket;
import com.groupe.roomgame.networking.election.FindOwnIP;
import com.groupe.roomgame.networking.election.IPs;

import com.groupe.roomgame.objects.Animal;
import com.groupe.roomgame.objects.Character;
import com.groupe.roomgame.objects.NPC;
import com.groupe.roomgame.objects.Player;
import com.groupe.roomgame.objects.Room;
import com.badlogic.gdx.physics.box2d.World;
import java.nio.ByteBuffer;

public class Listener {

	private DatagramSocket socket;
	private ConcurrentHashMap<Integer, Character> gameState;
	private Room[] rooms;
	private World world;
	private boolean isLeader;
	private DataPacket initPacket;
	private Updater updater;

	public Listener(ConcurrentHashMap<Integer, Character> gameState, Room[] rooms, World world, boolean isLeader) {
		this.rooms = rooms;
		this.gameState = gameState;
		this.world = world;
		try {
			socket = new DatagramSocket(6145);
			if (!isLeader)
				socket.setSoTimeout(1000);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		this.isLeader = isLeader;
		
	}

	public boolean initialListen() {
		if (!isLeader) {
			return receiveInitPacket();
		} else {
			return receiveCharacterInitialization();
		}
	}

	private boolean receiveInitPacket(){
		byte[] buffer = new byte[512];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		try {
			socket.receive(packet);
		} catch (IOException e) {
			return false;
		}

		ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);		

		for (int i = 0; i < 15 + IPs.getIPs.length; i++){
			int type = byteBuffer.getInt();
			int id = byteBuffer.getInt();
			float x = byteBuffer.getFloat();
			float y = byteBuffer.getFloat();
			
			if (!gameState.containsKey(id))
				addCharacter(type, id, x, y);
		}

		for (int i = 0; i < 6; i++){
			int id = byteBuffer.getInt();
			int state = byteBuffer.getInt();
			updateRoom(id, state);
		}
		return true;
	}

	private boolean receiveCharacterInitialization(){
		byte[] buffer = new byte[512];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		try {
			socket.receive(packet);
		} catch (IOException e) {
			return false;
		}

		ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);		

		int type = byteBuffer.getInt();
		int id = byteBuffer.getInt();
		float x = byteBuffer.getFloat();
		float y = byteBuffer.getFloat();

		addCharacter(type, id, x, y);
		return true;
	}
	
	private void updateRoom(int id, int state) {
		rooms[id - 1].setRoomState(state);
	}

	private void addCharacter(int type, int id, float x, float y) {
		Character c;
		if (type == Character.PC) {
			c = new Player(id, x, y, world);
		} else if (type == Character.ANIMAL) {
			c = new Animal(id, x, y, world);
		} else {
			c = new NPC(id, x, y, world);
		}

		gameState.put(id, c);
	}

	public void updateListen() {
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					byte[] buffer = new byte[256];
					DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
					try {
						socket.receive(packet);
						if (isLeader)
							sendOut(packet, buffer);

						ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
						int packetType = byteBuffer.getInt();
						int id = byteBuffer.getInt();
						int respect = byteBuffer.getInt();
						float dx = byteBuffer.getFloat();
						float dy = byteBuffer.getFloat();
						// System.out.println("vx: " + vx + ", vy: " + vy);
						gameState.get(id).update(dx, dy);
					} catch (IOException e) {
						System.out.println("Time out.");
					}
				}
			}
		}).start();
	}

	private void sendOut(DatagramPacket received, byte[] bytes) throws IOException {
		for (InetAddress addr : IPs.getIPsAsList){
			if (!addr.getHostAddress().equals(FindOwnIP.getMyIP()) && !addr.getHostAddress().equals(received.getAddress().getHostAddress())){
				System.out.println("Sending update packet to: " + addr.getHostAddress());
				DatagramPacket packet = new DatagramPacket(bytes, bytes.length, addr, 6145);
			}
		}
	}

	public void setInitPacket(DataPacket initPacket){
		this.initPacket = initPacket;
	}

	public void setUpdater(Updater updater){
		this.updater = updater;
	}

	public void setLeader(boolean isLeader){
		this.isLeader = isLeader;
	}

}