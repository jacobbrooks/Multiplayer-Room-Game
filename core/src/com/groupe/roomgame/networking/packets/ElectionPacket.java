package com.groupe.roomgame.networking.packets;

/*
* @author Dominic Dewhurst
*/

import java.nio.ByteBuffer;

public class ElectionPacket {
	
	protected byte opCode;
	protected byte[] packet;

	protected int term;

	public ElectionPacket(){
		
	}

	public ElectionPacket(int term){
		this.term = term;
	}

	public ElectionPacket(byte[] packet){
		ByteBuffer buffer = ByteBuffer.wrap(packet);
		this.opCode = buffer.get();
	}

	public int getTerm(){
		return term;
	}

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
