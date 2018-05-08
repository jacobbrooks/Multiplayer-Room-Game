package com.groupe.roomgame.networking.states;

/*
* @author Dominic Dewhurst
*/

import java.net.InetAddress;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;

import java.io.IOException;

import java.util.concurrent.ThreadLocalRandom;

import com.groupe.roomgame.networking.IPs;
import com.groupe.roomgame.networking.Sender;
import com.groupe.roomgame.networking.packets.*;

public class Follower {

	/* State of member when election has yet to start 
	 * First to timeout becomes a candidate
	 * Open first candidacy packet receieved vote that member as the leader
	 */
	
	public InetAddress leader;
	private DatagramSocket socket;
	
	private int port, votePort, currentTerm;
	private boolean running, voted, isCandidate;

	public Follower(int port, int votePort) throws IOException {
		this.port = port;
		this.votePort = votePort;
		this.socket = new DatagramSocket(port);
		this.running = true;
		this.voted = false;
		this.isCandidate = false;
		this.currentTerm = 1;
		this.socket.setSoTimeout(ThreadLocalRandom.current().nextInt(2000, 10000));
		System.out.println(socket.getSoTimeout());
	}

	public boolean run() throws IOException {
		while (running){
			try {
				if (isCandidate){
					System.out.println("I became a candidate.");
					if (new Candidate(votePort).receiveVotes())
						return new Leader().sendConfirmations();	
				}

				byte[] buffer = new byte[256];
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				socket.receive(packet);

				byte opCode = new ElectionPacket(packet.getData()).getOpCode();

				switch (opCode){
					case 0:
						CandidacyPacket candidacyPacket = new CandidacyPacket(packet.getData());
						System.out.println("Received candidacyPacket: " + candidacyPacket);
						byte votedFor = voted ? (byte) 0 : (byte) 1;
						vote(candidacyPacket, votedFor, packet.getAddress().getHostAddress());
						break;
					case 2:
						System.out.println("Leader has been elected: " + leader.getHostAddress());
						return false;
				}
			} catch (SocketTimeoutException e){
				if(!isCandidate)
					becomeCandidate();
				else
					running = false;
			} catch (IOException ex){
				ex.printStackTrace();
			}
		}
		
		return false;
	}

	/* received candidacy packets before timeout */
	public void vote(CandidacyPacket candidacyPacket, byte votedFor, String ip) throws IOException {
		this.currentTerm = candidacyPacket.getTerm();

		InetAddress canidate = InetAddress.getByName(ip);
		leader = voted ? leader : canidate;

		VotePacket vote = new VotePacket(votedFor);
		DatagramPacket votePacket = new DatagramPacket(vote.getPacket(), vote.getPacket().length, canidate, votePort);
		socket.send(votePacket);
		System.out.println("Sent vote packet to: " + ip);
	}

	/* timed out before receiving any candidacy packets - therefore becomes a potential candidate */
	public void becomeCandidate() throws IOException {
		CandidacyPacket candidacyPacket = new CandidacyPacket(++currentTerm);

		for (int i = 0; i < IPs.getIPs.length; i++)
			new Sender(candidacyPacket, IPs.getIPs[i], port).start();
		isCandidate = true;
	}
}
