package com.groupe.roomgame.networking.packets;

import java.nio.ByteBuffer;

public class DataPacket {
	
	/*
	 * Packet types
	 */
	public static final int CHARACTER_INIT = 0;
	public static final int CHARACTER_UPDATE = 1;
	public static final int ROOM_UPDATE = 2;
	
	private byte[] bytes;
	
	/*
	 * Character initialization packet : CHARACTER_INIT
	 */
	public void createCharacterInitPacket(int characterType, int id, float x, float y) {
		int size = (3 * (Integer.SIZE / 8)) + (2 * (Float.SIZE / 8));
		ByteBuffer buffer = ByteBuffer.allocate(size);
		buffer.putInt(CHARACTER_INIT).putInt(characterType).putInt(id).putFloat(x).putFloat(y).flip();
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
