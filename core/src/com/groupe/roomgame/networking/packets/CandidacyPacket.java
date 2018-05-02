package com.groupe.roomgame.networking.packets;

import java.nio.ByteBuffer;

/*
* @author Dominic Dewhurst
*/

public class CandidacyPacket extends ElectionPacket {
	
	/* Packet used when group member times out during election,
	 * claiming to everyone else in the group that he should be the leader */

	private int size = (Byte.SIZE / 8) + Integer.SIZE / 8;

	public CandidacyPacket(int term){
		super(term);
		this.opCode = 0;
		this.packet = new byte[size];

		ByteBuffer buffer = ByteBuffer.allocate(size);
		buffer.mark();
		buffer.put(opCode);
		buffer.putInt(term).reset();
		buffer.get(packet);
	}

	public CandidacyPacket(byte[] packet){
		ByteBuffer buffer = ByteBuffer.wrap(packet);
		this.opCode = buffer.get();
		this.term = buffer.getInt();
	}
}
