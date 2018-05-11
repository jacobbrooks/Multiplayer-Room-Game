package com.groupe.roomgame.networking.packets;

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Random;
import java.util.Iterator;

import com.groupe.roomgame.objects.Character;
import com.groupe.roomgame.objects.Room;
import com.groupe.roomgame.networking.election.IPs;

public class DataPacket {
	
	/*
	 * Packet types
	 */
	public static final int CHARACTER_UPDATE = 0;
	public static final int ROOM_UPDATE = 1;
	
	private byte[] bytes;
	
	/*
	 * Character initialization packet : CHARACTER_INIT
	 */
	public void createInitPacket(Room[] rooms, ConcurrentHashMap<Integer, Character> gameState) {
		int charCount = gameState.keySet().size();
		int numInts = 2 * (Integer.SIZE / 8);
		int numFloats = 2 * (Float.SIZE / 8);
		int numRooms = rooms.length;

		int size = (charCount * (numInts + numFloats)) + (numRooms * numInts);

		ByteBuffer buffer = ByteBuffer.allocate(size);

		Iterator<Integer> it = gameState.keySet().iterator();
		while (it.hasNext()){
			Character c = gameState.get(it.next());
			buffer.putInt(c.getType()).putInt(c.getId()).putFloat(c.getBody().getPosition().x * 100).putFloat(c.getBody().getPosition().y * 100);
		}
		
		for (Room r : rooms){
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
	public void createCharacterUpdatePacket(int id, int respect, float x, float y) {
		int size = (3 * (Integer.SIZE / 8)) + (2 * (Float.SIZE / 8));
		ByteBuffer buffer = ByteBuffer.allocate(size);
		buffer.putInt(CHARACTER_UPDATE).putInt(id).putInt(respect).putFloat(x).putFloat(y).flip();
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
