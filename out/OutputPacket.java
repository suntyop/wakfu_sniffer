package wakfuSniffer.out;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

import wakfuSniffer.tools.Log;

public class OutputPacket implements Runnable {
	
	public WritableByteChannel out;
	
	private int packetSize;
	private short type;
	private int packetID;
	private byte[] packetEnd;
	private byte useless;
	
	public OutputPacket(WritableByteChannel out, int packetSize, int packetID, byte[] packetEnd,  short type, byte useless)
	{
		this.out = out;
		this.packetSize = packetSize;
		this.packetID = packetID;
		this.packetEnd = packetEnd;
		this.type = type;
		this.useless = useless;
	}
	public OutputPacket(WritableByteChannel out, int packetSize, int packetID, byte[] packetEnd,  short type)
	{
		this.out = out;
		this.packetSize = packetSize;
		this.packetID = packetID;
		this.packetEnd = packetEnd;
		this.type = type;
	}

	@Override
	public synchronized  void run() {
		
		ByteBuffer tempBuffer = ByteBuffer.allocate(this.packetSize);
		
		if(this.type == 1)
		{
			tempBuffer.put(this.useless);
			tempBuffer.putShort((short) this.packetID);
			tempBuffer.put(this.packetEnd);
			tempBuffer.flip();
			try {
				this.out.write(tempBuffer);
			} catch (IOException e) {
				
			}
			tempBuffer.clear();

		}
		else if(this.type ==2)
		{
			if(this.packetID == 1036)
			{
				tempBuffer.put(hexStringToByteArray("03a6040c000000080000000800000007506861657269730000000d0000000d35322e37362e3133392e32343200000002000015b4000001bb060000000700000005456672696d000000060000000e38302e3233392e3137332e31353800000002000015b4000001bb0500000006000000074461746875726100000000000000093132372e302e302e3100000002000015b4000001bb000000000500000005416d617261000000030000000e38302e3233392e3137332e31343900000002000015b4000001bb040000000400000005456c626f72000000040000000e38302e3233392e3137332e31353000000002000015b4000001bb0200000003000000034e6f780000000b0000000e38302e3233392e3137332e31353700000002000015b4000001bb03000000020000000952656d696e67746f6e000000010000000e38302e3233392e3137332e31353100000002000015b4000001bb0700000001000000074165726166616c000000000000001b6165726166616c2e706c6174666f726d732e77616b66752e636f6d00000002000015b4000001bb0100000007000000080000000701002e01022d31000000380000000500d0000000047472756500d10000000431333b3200d20000000000dd0000000d64656661756c743b737465616d01a4000000013800000000070000000701002e01022d31000000350000000500d0000000047472756500d1000000013600d20000000000dd0000000d64656661756c743b737465616d01a4000000013700000000060000000701002e01022d31000000350000000500d0000000047472756500d1000000013000d20000000000dd0000000d64656661756c743b737465616d01a4000000013600000000040000000701002e01022d31000000350000000500d0000000047472756500d1000000013400d20000000000dd0000000d64656661756c743b737465616d01a4000000013400000000030000000701002e01022d310000003a0000000500d0000000047472756500d10000000631313b313b3200d20000000000dd0000000d64656661756c743b737465616d01a4000000013300000000020000000701002e01022d31000000390000000500d0000000047472756500d10000000000d20000000531323b313400dd0000000d64656661756c743b737465616d01a4000000013200000000010000000701002e01022d31000000350000000500d0000000047472756500d1000000013000d20000000000dd0000000d64656661756c743b737465616d01a4000000013100"));
			}
			else
			{
				tempBuffer.putShort((short) this.packetSize);
				tempBuffer.putShort((short) this.packetID);
				tempBuffer.put(this.packetEnd);
			}
			tempBuffer.flip();
			try {
				this.out.write(tempBuffer);
			} catch (IOException e) {
				
			}
			tempBuffer.clear();
		}
		else
			Log.writeLogDebugMessage("Impossible d'envoyer le paquet "+this.packetID);
		
		
	}
	
	public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}

}
