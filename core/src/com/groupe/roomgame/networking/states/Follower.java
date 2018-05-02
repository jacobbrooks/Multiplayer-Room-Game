package com.groupe.roomgame.networking.states;

/*
* @author Dominic Dewhurst
*/

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

import java.util.concurrent.ThreadLocalRandom;
import java.io.IOException;

public class Follower {
	
	private DatagramSocket socket;
	public static boolean running = true;
	private boolean voted, isCandidate;
	public InetAddress leader;
	private int port;
	private int votePort;

	private int currentTerm;

	public Follower(int port, int votePort) throws IOException {
		this.port = port;
		this.votePort = votePort;
		this.socket = new DatagramSocket(port);
		this.voted = false;
		this.isCandidate = false;
		this.currentTerm = 1;
		this.socket.setSoTimeout(ThreadLocalRandom.current().nextInt(2000, 10000));
		System.out.println(socket.getSoTimeout());
	}

	public void run() throws IOException {
		while (running){
			try {
				if (isCandidate){
					System.out.println("I became a candidate.");
					if (new Candidate(votePort).receiveVotes())
						new Leader().sendConfirmations();	
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
						running = false;
						break;
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
	}

	public void vote(CandidacyPacket candidacyPacket, byte votedFor, String ip) throws IOException {
		/* received canidate vote before timeout */
		this.currentTerm = candidacyPacket.getTerm();

		InetAddress canidate = InetAddress.getByName(ip);
		leader = voted ? leader : canidate;

		VotePacket vote = new VotePacket(votedFor);
		DatagramPacket votePacket = new DatagramPacket(vote.getPacket(), vote.getPacket().length, canidate, votePort);
		socket.send(votePacket);
		System.out.println("Sent vote packet to: " + ip);
	}

	public void becomeCandidate() throws IOException {
		CandidacyPacket candidacyPacket = new CandidacyPacket(++currentTerm);

		for (int i = 0; i < IPs.getIPs.length; i++)
			new Sender(candidacyPacket, IPs.getIPs[i], port).start();

		isCandidate = true;
	}
}
