package wakfuSniffer.in;

import java.io.IOException;

public interface Read {
	public void readPacket() throws IOException;
	public boolean readHeader() throws IOException;
}
