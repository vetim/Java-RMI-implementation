package rmi.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.JOptionPane;

import chess.windowChessBoardClient;
import rmi.server.CellMatrix_IF;
import rmi.server.KSE_IF;

public class Client extends UnicastRemoteObject implements Klient_IF {
	private static final long serialVersionUID = 1L;
	private KSE_IF server = null;
	private CellMatrix_IF cell;
	private windowChessBoardClient wcb;

	public Client(String serverURL, String CellMatrixURL)throws RemoteException, MalformedURLException, NotBoundException{
		this.server = (KSE_IF) Naming.lookup(serverURL);
		if(this.server.regjistroKlientin(this)){
		this.cell = (CellMatrix_IF)Naming.lookup(CellMatrixURL);
		System.out.println("Client created");
		}else{
			JOptionPane.showMessageDialog(null,"Serveri i zene, mund ta ndiqni lojen si spektatore!");
			System.exit(0);
		}
	}

	public void set_wcb(windowChessBoardClient wcb) {
		this.wcb = wcb;
	}
	
	
	public CellMatrix_IF getCellMatrix() {
		return cell;
	}
	
	@Override
	public void update() throws RemoteException {
		wcb.refresh();
	}

	public KSE_IF get_KSE() {
		return server;
	}

	@Override
	public void changePlayer() throws RemoteException {
		// TODO Auto-generated method stub
		wcb.changePlayer();
	}

	@Override
	public void  newGame(int player) throws RemoteException {
		// TODO Auto-generated method stub
		wcb.newGame(player);
	}
	
	public void exitGame() throws RemoteException{
	JOptionPane.showMessageDialog(null, "Loja u nderpre nga lojtari,ju fituat");
		System.exit(1);
	}

	@Override
	public void setName(String namep1,String namep2) throws RemoteException {
		// TODO Auto-generated method stub
		wcb.setName(namep1,namep2);
	}

	@Override
	public void set_winner(int player) throws RemoteException {
		// TODO Auto-generated method stub
		if (player == 2) {
			server.set_winner(2);
		}
		if (player == 1) {
		wcb.set_winner(1);
		}
	}

	@Override
	public void setCountedSpectator(int numberOf) throws RemoteException {
		wcb.setCountedSpectators(numberOf);
		
	}

}
