package rmi.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import chess.windowChessBoard;
import rmi.client.Klient_IF;
import rmi.client.Spectator_IF;

public class KSE extends UnicastRemoteObject implements KSE_IF {

	private static final long serialVersionUID = 1L;
	private ArrayList<Spectator_IF> spectator;
	private volatile Klient_IF klienti=null;
	private windowChessBoard wcb=null;
	private String player1_name="player1";
	private String player2_name="player2";
	
	public KSE() throws RemoteException {
		
		spectator = new ArrayList<Spectator_IF>();
	
	}

	public void set_wcb(windowChessBoard wcb) throws RemoteException{
		this.wcb = wcb;	
		wcb.setName(player1_name, player2_name);
	}

	@Override
	public synchronized boolean regjistroKlientin(Klient_IF k) throws RemoteException {
		if (klienti == null) {
			klienti = k;
			return true;
		} else {
		return false;
		}
	}

	@Override
	public boolean regjistroSpektatore(Spectator_IF s) throws RemoteException {
		spectator.add(s);
		setCountedSpectator(spectator.size());

		return true;
	}

	@Override
	public synchronized void broadcastUpdate() throws RemoteException {
		if (klienti != null) {
			klienti.update();
		}
			wcb.refresh();
		int i = 0;
		while (i < spectator.size()) {	
			spectator.get(i++).update();
		}

	}

	public synchronized void changePlayer(int player) throws RemoteException {
		if (player == 1) {
			if (klienti != null) {
				klienti.changePlayer();
			}
		}

		if (player == 2) {
			wcb.changePlayer();
		}
		int i = 0;
		while (i < spectator.size()) {	
			spectator.get(i++).changePlayer();
		}
		
	}
	
	public void set_winner(final int player) throws RemoteException {
		if (player == 1) {
			new Thread() {
				public void run() {
					try {
						klienti.set_winner(1);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}.start();
			
		}
		if (player == 2) {new Thread() {
			public void run() {
				try {
					wcb.set_winner(2);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}.start();
			
		}
		 int i = 0;
			while (i < spectator.size()) {
				final int k = i;
				new Thread() {
					public void run() {
						try {
							spectator.get(k).set_winner(player);;
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}.start();
				i++;
			}
	}
	
	public synchronized void newGame(int player) throws RemoteException{
		if (klienti != null) {
			klienti.newGame(player);
			klienti.setName(player1_name, player2_name);
		}
		wcb.newGame(player);
		wcb.setName(player1_name, player2_name);
		int i = 0;
		while (i < spectator.size()) {
			final int f = i;
			new Thread() {

				public void run() {

					try {
						spectator.get(f).newGame();
						spectator.get(f).setName(player1_name, player2_name);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}.start();
			i++;
		}
	}
	public void deleteSpectator(final Spectator_IF s) throws RemoteException{
		new Thread() {
			public void run() {
				spectator.remove(s);
				try {
					setCountedSpectator(spectator.size());
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	public void game_exit(final int player) throws RemoteException{
		 int i = 0;
		while (i < spectator.size()) {
			final int k = i;
			new Thread() {
				public void run() {
					try {
						spectator.get(k).exitGame(player);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			}.start();
			i++;
		}
		if(player==1){
			if (klienti != null)
				new Thread() {
					public void run() {
						try {
							klienti.exitGame();
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}.start();
		}
		if(player==2){
			JOptionPane.showMessageDialog(null, "Loja u nderpre nga klienti, ju jeni fituesi");
			
			new Thread() {
				public void run() {
					try {
						wcb.newGame(1);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					klienti = null;
				}
			}.start();
		}
	}
	
	public void setName(int player,String name) throws RemoteException{
		if(player==1){player1_name=name;}
		if(player==2){player2_name=name;}
	}

	@Override
	public int getCurrentPlayer()  throws RemoteException{
		return wcb.getCurrentPlayer();
	}

	@Override
	public int countSpectators() throws RemoteException {
		return spectator.size();
	}	
	public void setCountedSpectator(int numberOf)throws RemoteException {
		wcb.setCountedSpectators(numberOf);
		if(klienti!=null){klienti.setCountedSpectator(numberOf);}
		int i = 0;
		while (i<spectator.size()){
			spectator.get(i++).setCountedSpectator(numberOf);
		}
	}
}
