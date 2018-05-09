package com.groupe.roomgame.networking.election;

/*
* @author Dominic Dewhurst
*/

import java.net.InetAddress;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

public class IPs {

	public static String nuc1String = "129.3.20.61";
	public static String nuc2String = "129.3.20.62";
	public static String nuc3String = "129.3.20.63";
	public static String nuc7String = "129.3.20.67";

	public static InetAddress nuc1; 
	public static InetAddress nuc2;
	public static InetAddress nuc3;
	public static InetAddress nuc7;

	public static CopyOnWriteArrayList<String> needToVote;
	public static CopyOnWriteArrayList<String> needToVoteStored;

	public static InetAddress[] getIPs;
	public static String[] getIPsAsString;
	public static CopyOnWriteArrayList<InetAddress> getIPsAsList;

	public static InetAddress leader;

	public static void set1() throws IOException {
		nuc1 = InetAddress.getByName(nuc1String);
	}

	public static void set2() throws IOException {
		nuc2 = InetAddress.getByName(nuc2String);
		//nuc7 = InetAddress.getByName(nuc7String);
	}

	public static void set3() throws IOException {
		nuc3 = InetAddress.getByName(nuc3String);
		getIPs = new InetAddress[]{nuc1, nuc2, nuc3};
		getIPsAsString = new String[]{nuc1String, nuc2String, nuc3String};
		//getIPs = new InetAddress[]{nuc1, nuc7};
		getIPsAsList = new CopyOnWriteArrayList<InetAddress>(getIPs);
		//getIPsAsString = new String[]{nuc1String, nuc7String};
		needToVote = new CopyOnWriteArrayList<String>(getIPsAsString);
		needToVoteStored = new CopyOnWriteArrayList<String>(needToVote);
	}
}
