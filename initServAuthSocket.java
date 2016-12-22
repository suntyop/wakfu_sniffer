package wakfuSniffer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;

import wakfuSniffer.in.InputPacketFromClient;
import wakfuSniffer.in.InputPacketFromServer;
import wakfuSniffer.tools.Log;
import wakfuSniffer.tools.PropertyLoader;

public class InitServAuthSocket {
	
	private int portServAuthSocketProxy;
	private int portServAuthSocket;
	private String ipServAuthSocket;
	private Socket socketCommuToServer;
	private ServerSocket socketServer;
	private Socket socketCommuToClient;
	
	public InitServAuthSocket()
	{
	
			Properties prop;
			try {
				prop = PropertyLoader.load("config/init.properties");
				this.portServAuthSocketProxy = Integer.parseInt(prop.getProperty("serv.proxy.port", "50224"));
				this.portServAuthSocket = Integer.parseInt(prop.getProperty("serv.auth.port", "5558"));
				this.ipServAuthSocket = prop.getProperty("serv.auth.ip", "52.16.189.225");
				this.initServAuthSocket();
			} catch (IOException e) {
				Log.writeLogDebugMessage("Impossible de lire le fichier init.properties...");
			}

			
	}
	private void initServAuthSocket()
	{
		try {
			this.socketServer = new ServerSocket(this.portServAuthSocketProxy);
			
			if(this.connectToServerAuth())
			{
				
					Log.writeLogDebugMessage("Le sniffer écoute le port "+this.portServAuthSocketProxy);
					Log.writeLogDebugMessage("En attente du client.");
					Log.writeLogDebugMessage("Vous pouvez lancer le jeu ! Connectez vous à votre compte Wakfu...");
					this.socketCommuToClient = this.socketServer.accept(); // Attente du client
					Log.writeLogDebugMessage("Client détecté sous l'ip "+ this.socketServer.getLocalSocketAddress());
					Thread inputPacketFromClient = new Thread(new InputPacketFromClient(this.socketServer, this.socketCommuToClient, this.socketCommuToServer)); // On écoute les paquets
					Thread inputPacketFromServer = new Thread(new InputPacketFromServer(this.socketServer, this.socketCommuToClient, this.socketCommuToServer));
					inputPacketFromClient.start();
					inputPacketFromServer.start();
				
			}
				
		} catch (IOException e) {
			Log.writeLogDebugMessage("Impossible d'écouter le port "+this.portServAuthSocket+". Entrez un port ouvert et non occupé dans les fichiers de config");
		}
	}
	private boolean connectToServerAuth()
	{
		try {
			Log.writeLogDebugMessage("Tentative de connexion au serveur d'authentification de Wakfu");
			this.socketCommuToServer = new Socket(this.ipServAuthSocket,this.portServAuthSocket);
			Log.writeLogDebugMessage("Connexion au serveur d'authentification de Wakfu réussie. ");
			return true;
		} catch (UnknownHostException e) {
			Log.writeLogDebugMessage("Impossible de se connecter au serveur de Wakfu.. Vérifiez l'IP dans les fichiers de config");
			return false;
		} catch (IOException e) {
			Log.writeLogDebugMessage("Impossible de se connecter au port "+this.portServAuthSocket+" du serveur Wakfu. Vérifiez le port dans les fichiers de config");
			return false;
		}
	}
}
