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

	public static String nuc18String = "129.3.20.78";
	public static String nuc14String = "129.3.20.74";
	public static String nuc27String = "129.3.20.87";


	public static InetAddress nuc1; 
	public static InetAddress nuc2;
	public static InetAddress nuc3;
	public static InetAddress nuc7;

	public static InetAddress nuc18;
	public static InetAddress nuc14;
	public static InetAddress nuc27;

	public static CopyOnWriteArrayList<String> needToVote;
	public static CopyOnWriteArrayList<String> needToVoteStored;

	public static InetAddress[] getIPs;
	public static String[] getIPsAsString;
	public static CopyOnWriteArrayList<InetAddress> getIPsAsList;

	public static InetAddress leader;

	public static void set1() throws IOException {
		//nuc1 = InetAddress.getByName(nuc1String);
		//nuc2 = InetAddress.getByName(nuc2String);
		nuc18 = InetAddress.getByName(nuc18String);
	}

	public static void set2() throws IOException {
		//nuc7 = InetAddress.getByName(nuc7String);
		nuc14 = InetAddress.getByName(nuc14String);
	}

	/*public static void set3() throws IOException {

		nuc3 = InetAddress.getByName(nuc3String);
		getIPs = new InetAddress[]{nuc2, nuc7, nuc3};
		getIPsAsString = new String[]{nuc2String, nuc7String, nuc3String};
		//getIPs = new InetAddress[]{nuc1, nuc2};
		getIPsAsList = new CopyOnWriteArrayList<InetAddress>(getIPs);
		//getIPsAsString = new String[]{nuc1String, nuc2String};
		needToVote = new CopyOnWriteArrayList<String>(getIPsAsString);
		needToVoteStored = new CopyOnWriteArrayList<String>(needToVote);
	}*/

	public static void set3() throws IOException {
		
		nuc27 = InetAddress.getByName(nuc27String);
		getIPs = new InetAddress[]{nuc18, nuc14, nuc27};
		getIPsAsString = new String[]{nuc18String, nuc14String, nuc27String};
		getIPsAsList = new CopyOnWriteArrayList<InetAddress>(getIPs);
		needToVote = new CopyOnWriteArrayList<String>(getIPsAsString);
		needToVoteStored = new CopyOnWriteArrayList<String>(needToVote);
	}
}
