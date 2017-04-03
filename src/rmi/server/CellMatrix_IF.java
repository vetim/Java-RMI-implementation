package rmi.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CellMatrix_IF  extends Remote{

	public void resetMatrix() throws RemoteException;

	public int getPlayerCell (int row, int column) throws RemoteException;
	public int getPieceCell (int row, int column) throws RemoteException;
	public void setPlayerCell (int row, int column, int player) throws RemoteException;
	public void setPieceCell (int row, int column, int piece) throws RemoteException;
	public int[][] getPlayerMatrix() throws RemoteException;
	public int[][] getPieceMatrix() throws RemoteException;
	public boolean checkWinner (int currentPlayer) throws RemoteException;
	
}
