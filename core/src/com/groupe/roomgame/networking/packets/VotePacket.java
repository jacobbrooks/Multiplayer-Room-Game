package com.groupe.roomgame.networking.packets;

/*
* @author Dominic Dewhurst
*/

import java.nio.ByteBuffer;

public class VotePacket extends ElectionPacket {

	/* Packet used for voting a certain member to be the leader 
	 * 1 byte for opCode and 1 byte for yes or no vote
	*/

	private byte votedFor;
	
	/* create new vote packet */
	public VotePacket(byte votedFor){
		this.opCode = 1;
		this.size = 2 * (Byte.SIZE / 8);
		this.votedFor = votedFor;
		
		this.packet = new byte[size];

		ByteBuffer buffer = ByteBuffer.allocate(size);
		buffer.mark();
		buffer.put(opCode).put(votedFor).reset();
		buffer.get(packet);
	}

	/* used by receiver to unwrap received vote packet */
	public VotePacket(byte[] receivedPacket){
		ByteBuffer buffer = ByteBuffer.wrap(receivedPacket);
		this.opCode = buffer.get();
		this.votedFor = buffer.get();
	}

	/* return a 1 if candidate was voted for, 0 otherwise */
	public byte wasVotedFor(){
		return this.votedFor;
	}
}
