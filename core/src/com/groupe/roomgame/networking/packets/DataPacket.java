package com.groupe.roomgame.networking.packets;

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Random;

import com.groupe.roomgame.objects.Character;
import com.groupe.roomgame.objects.Room;

public class DataPacket {
	
	/*
	 * Packet types
	 */
	public static final int CHARACTER_INIT = 0;
	public static final int CHARACTER_UPDATE = 1;
	public static final int ROOM_UPDATE = 2;
	public static final Random rand = new Random();
	
	private byte[] bytes;
	
	/*
	 * Character initialization packet : CHARACTER_INIT
	 */
	public void createInitPacket(Character pc, Character[] characters, Room[] rooms, ConcurrentHashMap<Integer, Character> gameState) {
		int size = (characters.length + 1) * (2 * (Integer.SIZE / 8)) + (2 * (Float.SIZE / 8)) + (rooms.length * (2 * (Integer.SIZE / 8)));
		ByteBuffer buffer = ByteBuffer.allocate(size);

		buffer.putInt(Character.PC).putInt(pc.getId()).putFloat(pc.getBody().getPosition().x * 100).putFloat(pc.getBody().getPosition().y * 100);
		
		for (Character c : characters){
			buffer.putInt(c.getType()).putInt(c.getId()).putFloat(c.getBody().getPosition().x * 100).putFloat(c.getBody().getPosition().y * 100);
			gameState.put(c.getId(), c);
		}
		
		for (Room r : rooms){
			r.setRoomState(rand.nextInt(3));
			buffer.putInt(r.getID()).putInt(r.getRoomState());
		}

		buffer.flip();
		bytes = buffer.array();
	}

	public void createCharacterInitPacket(Character pc){
		int size = (2 * (Integer.SIZE / 8)) + (2 * (Float.SIZE / 8));
		ByteBuffer buffer = ByteBuffer.allocate(size);
		buffer.putInt(Character.PC).putInt(pc.getId()).putFloat(pc.getBody().getPosition().x * 100).putFloat(pc.getBody().getPosition().y * 100).flip();
		bytes = buffer.array();
	}
	
	/*
	 * Character update packet : CHARACTER_UPDATE
	 */
	public void createCharacterUpdatePacket(int id, int respect, float dx, float dy) {
		int size = (3 * (Integer.SIZE / 8)) + (2 * (Float.SIZE / 8));
		ByteBuffer buffer = ByteBuffer.allocate(size);
		buffer.putInt(CHARACTER_UPDATE).putInt(id).putInt(respect).putFloat(dx).putFloat(dy).flip();
		bytes = buffer.array();
	}
	
	/*
	 * Room update packet : ROOM_UPDATE
	 */
	public void createRoomUpdatePacket(int roomID, int roomState) {
		int size = (3 * (Integer.SIZE / 8));
		ByteBuffer buffer = ByteBuffer.allocate(size);
		buffer.putInt(ROOM_UPDATE).putInt(roomID).putInt(roomState).flip();
		bytes = buffer.array();
	}
	
	public byte[] getBytes() {
		return bytes;
	}
}
