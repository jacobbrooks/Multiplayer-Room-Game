package com.groupe.roomgame.networking;

/*
* @author Dominic Dewhurst
*/

import java.net.DatagramPacket;
import java.util.concurrent.atomic.AtomicInteger;

import com.groupe.roomgame.networking.packets.VotePacket;

public class VoteCounter extends Thread {

	/* Used by the current candidate to count votes concurrently */

	private AtomicInteger votes;
	private DatagramPacket packet;

	public VoteCounter(DatagramPacket packet, AtomicInteger votes){
		this.packet = packet;
		this.votes = votes;
	}

	public void run(){
		if (new VotePacket(packet.getData()).wasVotedFor() == 1)
			votes.getAndIncrement();
	}
}
