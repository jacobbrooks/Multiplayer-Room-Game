package com.groupe.roomgame.networking.packets;

import java.nio.ByteBuffer;

public class DataPacket {
	
	private int size = Integer.SIZE / 8 + (2 * (Float.SIZE / 8));
	private byte[] bytes;
	
	public DataPacket(int id, float dx, float dy) {
		ByteBuffer buffer = ByteBuffer.allocate(size);
		buffer.putInt(id).putFloat(dx).putFloat(dy).flip();
		bytes = buffer.array();
	}
	
	public byte[] getBytes() {
		return bytes;
	}
}
