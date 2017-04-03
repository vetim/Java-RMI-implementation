package rmi.server;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import chess.windowChessBoard;

public class ServerDriver {

	KSE kse;
	objCellMatrix cell;

	public ServerDriver() throws RemoteException {
		
		cell = new objCellMatrix();
		kse = new KSE();
	}

	public void startServer(String port) throws RemoteException, MalformedURLException {

		Registry registry = LocateRegistry.createRegistry(Integer.parseInt(port));
		registry.rebind("RMI_KSE", kse);
		registry.rebind("RMICellMatrix", cell);
		

	}

	public KSE getKse() {
		return kse;
	}

	public objCellMatrix getCell() {
		return cell;
	}
}
