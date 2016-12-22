package wakfuSniffer.in;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class InputPacket {
	
	protected Socket socketCommuToServer;
	protected ServerSocket socketServer;
	protected Socket socketCommuToClient;
	protected DataInputStream inputPacketFromServer; 
	protected DataInputStream inputPacketFromClient;
	protected int sizePacket;
	protected int packetID;
	protected byte[] packetEnd;
	protected int tempPacketID;
	protected byte[] tempPacketEnd;	
	
	public InputPacket(ServerSocket socketServer, Socket socketCommuToClient, Socket socketCommuToServer) throws IOException 
	{
		this.socketCommuToClient = socketCommuToClient;
		this.socketCommuToServer = socketCommuToServer;
		this.socketServer = socketServer;
		this.inputPacketFromServer = new DataInputStream(socketCommuToServer.getInputStream());
		this.inputPacketFromClient = new DataInputStream(socketCommuToServer.getInputStream());
	}

}
