package com.groupe.roomgame.networking.packets;

/*
* @author Dominic Dewhurst
*/

import java.nio.ByteBuffer;

public class ConfirmationPacket extends ElectionPacket {
	/* Packet sent out by leader to indicate to everyone he was voted leader 
	 * Contains no data besides the opCode indicated that a leader was been agreed on
	 */

	public ConfirmationPacket(){
		this.opCode = 2;
		this.size = Byte.SIZE / 8;
		this.packet = new byte[size];

		ByteBuffer buffer = ByteBuffer.allocate(size);
		buffer.mark();
		buffer.put(opCode).reset();
		buffer.get(packet);
	}
}
