package wakfuSniffer;

import wakfuSniffer.tools.Log;

public class Main {

	public static void main(String[] args) {
		
		Log.writeLogDebugMessage("Sniffer développé par Suntyop et destiné au jeu WAKFU");
		Log.writeLogDebugMessage("Vous devez être connecté à Internet pour utiliser ce programme");
		Log.writeLogDebugMessage("Initialisation de la liste des paquets");
		
		Log.writeLogDebugMessage("Initialisation du proxy d'identification");
		new InitServAuthSocket();
		
	
	}

}
