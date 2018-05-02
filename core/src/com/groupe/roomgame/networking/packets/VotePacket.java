package com.groupe.roomgame.networking.packets;

/*
* @author Dominic Dewhurst
*/

import java.nio.ByteBuffer;

public class VotePacket extends ElectionPacket {

	/* Packet used for voting a certain member to be the leader */

	private byte votedFor;
	private int size = 2 * (Byte.SIZE / 8);
	
	public VotePacket(byte votedFor){
		this.votedFor = votedFor;
		this.opCode = 1;
		this.packet = new byte[size];

		ByteBuffer buffer = ByteBuffer.allocate(size);
		buffer.mark();
		buffer.put(opCode).put(votedFor).reset();
		buffer.get(packet);
	}

	public VotePacket(byte[] receivedPacket){
		ByteBuffer buffer = ByteBuffer.wrap(receivedPacket);
		this.opCode = buffer.get();
		this.votedFor = buffer.get();
	}

	/* return a 1 if candidate was voted for, 0 otherwise */
	public byte wasVotedFor(){
		return this.votedFor;
	}

	public String toString(){
		return "opCode: " + this.opCode + " | term: " + this.term;
	}
}
