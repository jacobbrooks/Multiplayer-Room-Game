package com.groupe.roomgame.networking;

/*
* @author Dominic Dewhurst
*/

import java.net.DatagramPacket;
import java.util.concurrent.atomic.AtomicInteger;

public class Handler extends Thread {
	private DatagramPacket packet;
	private int totalMembers;
	private AtomicInteger votes;

	public Handler(DatagramPacket packet, AtomicInteger votes){
		this.packet = packet;
		this.votes = votes;
		this.totalMembers = IPs.getIPs.length;
	}

	public void run(){
		VotePacket vote = new VotePacket(packet.getData());
		if (vote.wasVotedFor() == 1)
			votes.getAndIncrement();
	}
}
