package wakfuSniffer.in;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;

import wakfuSniffer.out.OutputPacket;
import wakfuSniffer.tools.Log;

public class InputPacketFromClient implements Runnable, Read{
	
	private Socket socketCommuToServer;
	private ServerSocket socketServer;
	private Socket socketCommuToClient;
	private DataInputStream inputPacketFromClient; 
	private int sizePacket;
	private int packetID;
	private byte[] packetEnd;
	private WritableByteChannel outServer;
	private byte useless;
	public int tempPacketID;
	public byte[] tempPacketEnd;
	private boolean run;
			
	public InputPacketFromClient(ServerSocket socketServer, Socket socketCommuToClient, Socket socketCommuToServer) throws IOException 
	{
		this.socketCommuToClient = socketCommuToClient;
		this.socketCommuToServer = socketCommuToServer;
		this.socketServer = socketServer;
		this.inputPacketFromClient = new DataInputStream(socketCommuToClient.getInputStream());
		this.outServer = Channels.newChannel(this.socketCommuToServer.getOutputStream());
		this.run = true;
		Log.writeLogDebugMessage("Ecoute des paquets en cours");
		
	}
	
	@Override
	public void run() {
		try {
			this.readPacket();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void readPacket() throws IOException 
	{
	
		
		while(this.run)
		{
			if(this.readHeader())
			{
				Log.writeLogCTSMessage("ID="+this.packetID);
				Thread outputPacket = new Thread(new OutputPacket(this.outServer, this.sizePacket, this.packetID, this.packetEnd, (short) 1, this.useless));
				outputPacket.start();
				
			}
		}
		
	}
	
	@Override
	public boolean readHeader() throws IOException
	{
		try {
			this.sizePacket = this.inputPacketFromClient.readShort();
			this.packetEnd = new byte[this.sizePacket-5];
			this.tempPacketEnd = new byte[this.sizePacket-5];
			this.useless = this.inputPacketFromClient.readByte();
			this.packetID = this.inputPacketFromClient.readShort();
			this.tempPacketID = this.packetID;
			for(int i = 0; i < this.sizePacket-5; i++)
			{
				this.packetEnd[i] = this.inputPacketFromClient.readByte();
			}
			if(this.tempPacketEnd.equals(this.packetEnd) && this.packetID == this.tempPacketID)
			{
				return false;
			}
			else
			{
				this.tempPacketEnd = this.packetEnd;
				this.tempPacketID = this.packetID;
				return true;
			}
		} catch (IOException e) {
			Log.writeLogDebugMessage("Impossible de lire le HEADER CLIENT");
			this.run = false;
			this.socketCommuToClient.close();
			this.socketCommuToServer.close();
			this.socketServer.close();
			
			return false;
		}
		
		
	}


	


}
