package com.groupe.roomgame.networking;

/*
* @author Dominic Dewhurst
*/

import java.net.InetAddress;
import java.io.IOException;

public class IPs {

	public static InetAddress nuc1; 
	public static InetAddress nuc2;
	public static InetAddress nuc3;

	public static InetAddress[] getIPs;

	public static void set1() throws IOException {
		nuc1 = InetAddress.getByName("129.3.20.61");
	}

	public static void set2() throws IOException {
		nuc2 = InetAddress.getByName("129.3.20.62");
	}

	public static void set3() throws IOException {
		nuc3 = InetAddress.getByName("129.3.20.63");
		getIPs = new InetAddress[]{nuc1, nuc2, nuc3};
	}
}
