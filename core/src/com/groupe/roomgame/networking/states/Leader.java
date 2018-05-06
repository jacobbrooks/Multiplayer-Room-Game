package com.groupe.roomgame.networking.states;

/*
* @author Dominic Dewhurst
*/

import java.io.IOException;

import com.groupe.roomgame.networking.IPs;
import com.groupe.roomgame.networking.Sender;
import com.groupe.roomgame.networking.packets.ConfirmationPacket;

public class Leader {

	/* State when a candidate has been elected leader 
	 * Has to send confirmation packets to everyone in the group 
	 */
	
	private int port;

	public Leader(){
		this.port = 2703;
	}

	/* Concurrently send confirmation packets to all members in the group */
	public boolean sendConfirmations() throws IOException {
		ConfirmationPacket confirmationPacket = new ConfirmationPacket();
		System.out.println("I became leader.");

		for (int i = 0; i < IPs.getIPs.length; i++)
			new Sender(confirmationPacket, IPs.getIPs[i], port).start();

		System.out.println("Sent confirmationPackets");
		return true;
	}
}
