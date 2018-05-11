package com.groupe.roomgame.networking.election;

/*
* @author Dominic Dewhurst
*/

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import com.groupe.roomgame.networking.packets.ElectionPacket;
import com.groupe.roomgame.networking.packets.VotePacket;
import com.groupe.roomgame.networking.packets.CandidacyPacket;

public class HoldElection {
	
	private DatagramSocket socket;
	public static boolean running;
	private boolean voted, isLeader;
	public InetAddress leader;
	private int port, currentTerm;

	public static AtomicInteger votes;

	public HoldElection(int port) throws IOException {	
		this.port = port;
		this.votes = new AtomicInteger(1);
		this.socket = new DatagramSocket(port);
		this.voted = false;
		this.running = true;
		this.isLeader = false;
		this.currentTerm = 0;
		this.socket.setSoTimeout(ThreadLocalRandom.current().nextInt(500, 1000));
		System.out.println(socket.getSoTimeout());
	}

	public boolean run() throws IOException {
		while (running){
			try {
				byte[] buffer = new byte[256];
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				socket.receive(packet);

				byte opCode = new ElectionPacket(packet.getData()).getOpCode();

				switch (opCode){
					case 0:
						CandidacyPacket candidacyPacket = new CandidacyPacket(packet.getData());
						System.out.println("Current Term: " + currentTerm + " - Received candidacyPacket: " + candidacyPacket + " from " + packet.getAddress().getHostAddress());
						byte votedFor = voted || !(candidacyPacket.getTerm() >= currentTerm) ? (byte) 0 : (byte) 1;

						if (candidacyPacket.getTerm() > currentTerm)
							currentTerm = candidacyPacket.getTerm();

						vote(candidacyPacket, votedFor, packet.getAddress().getHostAddress());
						continue;
					case 1:
						IPs.needToVote.remove(packet.getAddress().getHostAddress());
						VotePacket vote = new VotePacket(packet.getData());
						System.out.println("Current Term: " + currentTerm + " - Received vote packet (" + vote + " vote: " + vote.wasVotedFor() + ") from " + packet.getAddress().getHostAddress());
						if (vote.wasVotedFor() == 1 && vote.getTerm() == currentTerm)
							votes.getAndIncrement();
						break;
					case 2:
						System.out.println("Leader has been elected: " + leader.getHostAddress());
						running = false;
						isLeader = false;
						IPs.leader = leader;
						break;
				}

				System.out.println("votes: " + votes.get() + " - needed: " + (int) Math.ceil(IPs.getIPsAsList.size() / 2.0));
				if (votes.get() > (int) Math.ceil(IPs.getIPsAsList.size())){
					new Leader().sendConfirmations();
					running = false;
					isLeader = true;
				}

			} catch (SocketTimeoutException e){
				System.out.println("Became candidate: " + currentTerm);
				IPs.needToVote = new CopyOnWriteArrayList<String>(IPs.needToVoteStored);
				System.out.println(IPs.needToVote + " - " + IPs.needToVoteStored);
				becomeCandidate();
			} catch (IOException ex){
				ex.printStackTrace();
			}
		}

		socket.close();
		return isLeader;
	}

	public void vote(CandidacyPacket candidacyPacket, byte votedFor, String ip) throws IOException {
		/* received canidate vote before timeout */
		this.currentTerm = candidacyPacket.getTerm();

		InetAddress canidate = InetAddress.getByName(ip);
		leader = voted ? leader : canidate;

		VotePacket vote = new VotePacket(votedFor, currentTerm);
		DatagramPacket votePacket = new DatagramPacket(vote.getPacket(), vote.getPacket().length, canidate, port);
		socket.send(votePacket);
		System.out.println("Sent vote packet (" + vote + " vote: " + vote.wasVotedFor() + ") to: " + ip);
	}

	public void becomeCandidate() throws IOException {
		CandidacyPacket candidacyPacket = new CandidacyPacket(++currentTerm);
		votes.getAndSet(1);

		for (int i = 0; i < IPs.needToVote.size(); i++)
			new Sender(candidacyPacket, InetAddress.getByName(IPs.needToVote.get(i)), port).start();
	}
}
