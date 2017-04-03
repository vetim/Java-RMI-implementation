package rmi.client;

import java.rmi.*;

import rmi.server.KSE_IF;

public interface Spectator_IF extends Remote{
void update()throws RemoteException;
public KSE_IF get_KSE() throws RemoteException;
void exitGame(int player) throws RemoteException; 
void newGame() throws RemoteException;
void setName(String namep1,String namep2)throws RemoteException;
void set_winner(int player) throws RemoteException;
void changePlayer() throws RemoteException;
void setCountedSpectator(int numberOf)throws RemoteException;

}
