package com.groupe.roomgame.networking.states;

/*
* @author Dominic Dewhurst
*/

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import java.io.IOException;

public class Candidate {

	private DatagramSocket socket;
	private Handler[] voteCounters;
	private AtomicInteger votes;

	public Candidate(int port) throws IOException {
		this.socket = new DatagramSocket(port);
		this.voteCounters = new Handler[IPs.getIPs.length];
		this.votes = new AtomicInteger(1);
	}

	public boolean receiveVotes() throws IOException {
		for (int i = 0; i < voteCounters.length - 1; i++) {
			byte[] buffer = new byte[256];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			socket.receive(packet);
			System.out.println("Received vote packet.");
			voteCounters[i] = new Handler(packet, votes);
			voteCounters[i].start();
		}

		for (int i = 0; i < voteCounters.length - 1; i++){
			try{
				voteCounters[i].join();
			} catch (InterruptedException e){
				e.printStackTrace();
			}
		}
		return processVotes();
	}

	public boolean processVotes(){
		if (votes.get() > Math.ceil((IPs.getIPs.length) / 2))
			return true;
		return false;
	}
}
