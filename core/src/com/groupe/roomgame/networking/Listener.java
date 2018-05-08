package com.groupe.roomgame.networking;

import java.net.*;
import java.io.*;
import java.util.concurrent.ConcurrentHashMap;
import com.groupe.roomgame.objects.Character;
import com.groupe.roomgame.objects.Player;
import com.badlogic.gdx.physics.box2d.World;
import java.nio.ByteBuffer;

public class Listener{

	private DatagramSocket socket;
	private ConcurrentHashMap<Integer, Character> gameState;
	private World world;

	public Listener(ConcurrentHashMap<Integer, Character> gameState, World world){
		this.gameState = gameState;
		this.world = world;
		try{
			socket = new DatagramSocket(6145);
		}catch(SocketException e){
			e.printStackTrace();
		}
	}

	public void initialListen(){
		byte[] buffer = new byte[256];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

		try{
			socket.receive(packet);
			ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
			int id = byteBuffer.getInt();
			float x = byteBuffer.getFloat();
			float y = byteBuffer.getFloat();
			Character c = new Player(id, x, y, world);
			gameState.put(id, c);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void updateListen(){
		new Thread(new Runnable(){
			public void run(){
				while(true){
					byte[] buffer = new byte[256];
					DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
					try{
						socket.receive(packet);
						ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
						int id = byteBuffer.getInt();
						float dx = byteBuffer.getFloat();
						float dy = byteBuffer.getFloat();
						//System.out.println("vx: " + vx + ", vy: " + vy);
						gameState.get(id).update(dx, dy);
					}catch(IOException e){
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

}