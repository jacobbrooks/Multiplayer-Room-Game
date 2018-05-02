package com.groupe.roomgame.networking.packets;

/*
* @author Dominic Dewhurst
*/

import java.nio.ByteBuffer;

public class ElectionPacket {
	
	/*
	 * Packets used when holding an election to
	 * determine the leader of the group
	 *
	 * opCodes:
	 *	0 - Candidacy Packet
	 *	1 - Vote Packet
	 *	2 - Leader Confirmation Packet 
	 */

	protected byte opCode;
	protected byte[] packet;

	protected int term, size;

	public ElectionPacket(){
	}

	/* create a new election packet */
	public ElectionPacket(int term){
		this.term = term;
	}

	/* used by receiver to unwrap received election packets */
	public ElectionPacket(byte[] packet){
		ByteBuffer buffer = ByteBuffer.wrap(packet);
		this.opCode = buffer.get();
	}

	/* get the term of the election packet */
	public int getTerm(){
		return term;
	}

	/* return opCode of this packet */
	public byte getOpCode(){
		return opCode;
	}
	/* send packet with current term, voting yourself as the leader */
	public byte[] getPacket() {
		return packet;
	}

	public String toString(){
		return "opCode: " + this.opCode + " | Term: " + this.term;
	}
}
