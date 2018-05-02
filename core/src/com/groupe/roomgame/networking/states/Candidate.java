package com.groupe.roomgame.networking.states;

/*
* @author Dominic Dewhurst
*/

import java.net.InetAddress;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import java.io.IOException;

import com.groupe.roomgame.networking.IPs;
import com.groupe.roomgame.networking.VoteCounter;

public class Candidate {

	/* State when follower timedout and wants to become the leader 
	 * Start by giving yourself one vote
	*/

	private DatagramSocket socket;
	private VoteCounter[] voteCounters;
	private AtomicInteger votes;

	public Candidate(int port) throws IOException {
		this.socket = new DatagramSocket(port);
		this.voteCounters = new VoteCounter[IPs.getIPs.length - 1];
		this.votes = new AtomicInteger(1);
	}

	/* Concurrently receive vote packets */
	public boolean receiveVotes() throws IOException {
		for (int i = 0; i < voteCounters.length; i++) {
			byte[] buffer = new byte[256];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			socket.receive(packet);
			System.out.println("Received vote packet.");
			voteCounters[i] = new VoteCounter(packet, votes);
			voteCounters[i].start();
		}

		for (int i = 0; i < voteCounters.length; i++){
			try{
				voteCounters[i].join();
			} catch (InterruptedException e){
				e.printStackTrace();
			}
		}
		return processVotes();
	}

	/* Determine if you got majority of the votes to become leader */
	public boolean processVotes(){
		if (votes.get() > Math.ceil((IPs.getIPs.length) / 2))
			return true;
		return false;
	}
}
