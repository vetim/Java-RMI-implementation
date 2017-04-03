package rmi.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Klient_IF extends Remote{
void update() throws RemoteException;
void changePlayer() throws RemoteException;
void newGame(int playerS) throws RemoteException;
void exitGame() throws RemoteException;
void setName(String namep1,String namep2) throws RemoteException;
void set_winner(int player) throws RemoteException;
void setCountedSpectator(int numberOf)throws RemoteException;

}
