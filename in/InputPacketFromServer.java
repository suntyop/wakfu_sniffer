package wakfuSniffer.in;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;

import wakfuSniffer.out.OutputPacket;
import wakfuSniffer.tools.Log;

public class InputPacketFromServer implements Runnable,Read{
	
	private Socket socketCommuToServer;
	private ServerSocket socketServer;
	private Socket socketCommuToClient;
	private DataInputStream inputPacketFromServer; 
	private int sizePacket;
	private int packetID;
	private byte[] packetEnd;
	private WritableByteChannel outClient;
	public int tempPacketID;
	public byte[] tempPacketEnd;
	private boolean run = true;		
	
	public InputPacketFromServer(ServerSocket socketServer, Socket socketCommuToClient, Socket socketCommuToServer) throws IOException 
	{
		this.socketCommuToClient = socketCommuToClient;
		this.socketCommuToServer = socketCommuToServer;
		this.socketServer = socketServer;
		this.inputPacketFromServer = new DataInputStream(socketCommuToServer.getInputStream());
		this.outClient = Channels.newChannel(socketCommuToClient.getOutputStream());
		this.run = true;
		
	}
	
	@Override
	public void run() {
		try {
			this.readPacket();
		} catch (IOException e) {
			
		}
		
	}

	@Override
	public void readPacket() throws IOException 
	{
		while(this.run)
		{
			if(this.readHeader())
			{
				Log.writeLogSTCMessage("ID="+this.packetID);
				Thread outputPacket = new Thread(new OutputPacket(this.outClient, this.sizePacket, this.packetID, this.packetEnd, (short) 2));
				outputPacket.start();
			}
		}
		
	}
	
	@Override
	public boolean readHeader() throws IOException
	{
		try {
			
			if(inputPacketFromServer.available() >= 4)
			{
				this.sizePacket = this.inputPacketFromServer.readShort();
				this.packetEnd = new byte[this.sizePacket-4];
				this.tempPacketEnd = new byte[this.sizePacket-4];
				this.packetID = this.inputPacketFromServer.readShort();
				for(int i = 0; i < this.sizePacket-4; i++)
				{
					this.packetEnd[i] = this.inputPacketFromServer.readByte();
				}
				
				
			}
			if(this.packetEnd.equals(this.packetEnd) && this.packetID == this.tempPacketID)
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
			Log.writeLogDebugMessage("Impossible de lire le HEADER SERVER");
			this.run = false;
			this.socketCommuToClient.close();
			this.socketCommuToServer.close();
			this.socketServer.close();
			return false;
		}
		
		
	}


	


}
