package com.groupe.roomgame.networking;

import java.net.*;
import java.io.*;
import java.util.concurrent.ConcurrentHashMap;

import com.groupe.roomgame.networking.packets.DataPacket;
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

	public void initialListen() {
		if (!isLeader) {
			// leader will send info for 20 randomly generated characters + 1 for himself, hence, i < 21
			for (int i = 0; i < 21; i++) {
				if (!receiveCharacterInitialization()){
					i = 0;
				}
			}
			
			//leader will send the room state of all 6 rooms
			for(int i = 0; i < 6; i++) {
				receiveRoomUpdate();
			}
		} else {
			receiveCharacterInitialization();
		}
	}
	
	private void receiveRoomUpdate() {
		byte[] buffer = new byte[256];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

		try {
			socket.receive(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}

		ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
		int packetType = byteBuffer.getInt(); 
		int roomID = byteBuffer.getInt();
		int roomState = byteBuffer.getInt();

		rooms[roomID - 1].setRoomState(roomState);
	}

	private boolean receiveCharacterInitialization() {
		byte[] buffer = new byte[256];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

		try {
			socket.receive(packet);
		} catch (IOException e) {
			updater.update(initPacket, isLeader);
			return false;
		}

		ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
		int packetType = byteBuffer.getInt(); 
		int characterType = byteBuffer.getInt();
		int id = byteBuffer.getInt();
		float x = byteBuffer.getFloat();
		float y = byteBuffer.getFloat();

		Character c;
		if (characterType == Character.PC) {
			c = new Player(id, x, y, world);
		} else if (characterType == Character.ANIMAL) {
			c = new Animal(id, x, y, world);
		} else {
			c = new NPC(id, x, y, world);
		}

		gameState.put(id, c);
		return true;
	}

	public void updateListen() {
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					byte[] buffer = new byte[256];
					DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
					try {
						socket.receive(packet);
						ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
						int packetType = byteBuffer.getInt();
						int id = byteBuffer.getInt();
						int respect = byteBuffer.getInt();
						float dx = byteBuffer.getFloat();
						float dy = byteBuffer.getFloat();
						// System.out.println("vx: " + vx + ", vy: " + vy);
						gameState.get(id).update(dx, dy);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	public void setInitPacket(DataPacket initPacket){
		this.initPacket = initPacket;
	}

	public void setUpdater(Updater updater){
		this.updater = updater;
	}

}