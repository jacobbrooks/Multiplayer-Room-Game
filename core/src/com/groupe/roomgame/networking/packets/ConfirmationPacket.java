package com.groupe.roomgame.networking.packets;

/*
* @author Dominic Dewhurst
*/

import java.nio.ByteBuffer;

public class ConfirmationPacket extends ElectionPacket {
	/* Packet sent out by leader to indicate to everyone he was voted leader */

	private int size = Byte.SIZE / 8;
	public ConfirmationPacket(){
		this.opCode = 2;
		this.packet = new byte[size];

		ByteBuffer buffer = ByteBuffer.allocate(size);
		buffer.mark();
		buffer.put(opCode).reset();
		buffer.get(packet);
	}
}
