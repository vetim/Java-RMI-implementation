package rmi.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import rmi.client.Klient_IF;
import rmi.client.Spectator_IF;

public interface KSE_IF extends Remote {
	boolean regjistroKlientin(Klient_IF k) throws RemoteException;

	boolean regjistroSpektatore(Spectator_IF s) throws RemoteException;

	void broadcastUpdate() throws RemoteException;

	void changePlayer(int player) throws RemoteException;

	void game_exit(int player) throws RemoteException;

	void setName(int player, String name)throws RemoteException;

	void newGame(int i) throws RemoteException;

	void deleteSpectator(Spectator_IF s) throws RemoteException;
	
	void set_winner(int player) throws RemoteException;

	int getCurrentPlayer() throws RemoteException;
	
	int countSpectators() throws RemoteException;
	void setCountedSpectator(int numberOf)throws RemoteException; 
}

