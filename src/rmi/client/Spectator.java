package rmi.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException ;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.JOptionPane;

import rmi.server.CellMatrix_IF;
import rmi.server.KSE_IF;
import chess.windowChessBoardClient;
import chess.windowChessBoardSpectator;

public class Spectator extends UnicastRemoteObject implements Spectator_IF{
	private windowChessBoardSpectator wcb;
	private CellMatrix_IF cell ;
	private KSE_IF server;

	public Spectator(String serverURL, String CellMatrixURL) throws RemoteException, MalformedURLException, NotBoundException {
		this.server = ((KSE_IF) Naming.lookup(serverURL));
		this.cell = ((CellMatrix_IF) Naming.lookup(CellMatrixURL));
		this.server.regjistroSpektatore(this);
		System.out.println("Spectator created");

	}
	public void set_wcb(windowChessBoardSpectator wcb){
		this.wcb=wcb;
	}
	private static final long serialVersionUID = 1L;

	@Override
	public void update() throws RemoteException {
	
		wcb.refresh();
		
	}
	
	public CellMatrix_IF getCellMatrix(){
		return cell;
	}
	@Override
	public void exitGame(int player) throws RemoteException {
		// TODO Auto-generated method stub
		if(player==1){JOptionPane.showMessageDialog(null,"Loja u nderpre nga Serveri");
		System.exit(1);
		}
		if(player==2){JOptionPane.showMessageDialog(null,"Loja u nderpre nga klienti");}

	}
	
	public KSE_IF get_KSE() throws RemoteException {
		return server;
	}
	public void newGame() throws RemoteException {
		wcb.newGame();
	}
	@Override
	public void setName(String namep1,String namep2) throws RemoteException {
		// TODO Auto-generated method stub
		wcb.setName(namep1,namep2);
	}
	@Override
	public void set_winner(int player) throws RemoteException {
		// TODO Auto-generated method stub
		wcb.set_winer(player);
	}
	@Override
	public void changePlayer() throws RemoteException {
		// TODO Auto-generated method stub
		wcb.changePlayer();
	}
	@Override
	public void setCountedSpectator(int numberOf) throws RemoteException {
		// TODO Auto-generated method stub
		if(wcb!=null){wcb.setCountedSpectators(numberOf);}
	}
	
}
