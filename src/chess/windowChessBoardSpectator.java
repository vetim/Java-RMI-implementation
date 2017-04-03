package chess;

import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;

import javax.swing.JOptionPane;
import javax.swing.JDialog;

import rmi.server.CellMatrix_IF;
import rmi.server.KSE_IF;

public class windowChessBoardSpectator extends objChessBoard {

	private final int refreshRate = 5; // Amount of pixels moved before screen
										// is refreshed

	private Image[][] imgPlayer = new Image[2][6];
	private String[] strPlayerName = { "Player1", "Player2" };
	private String strStatusMsg = "";
	private CellMatrix_IF cellMatrix;
	private int currentPlayer = 1, startRow = 0, startColumn = 0,
			pieceBeingDragged = 0;
	private int startingX = 0, startingY = 0, currentX = 0, currentY = 0,
			refreshCounter = 0;
	private boolean firstTime = true, hasWon = false, isDragging = false;
	private KSE_IF kse;

	private objPawn pawnObject = new objPawn();
	private objRock rockObject = new objRock();
	private objKnight knightObject = new objKnight();
	private objBishop bishopObject = new objBishop();
	private objQueen queenObject = new objQueen();
	private objKing kingObject = new objKing();

	private int numOfSpectators = 0;

	public windowChessBoardSpectator(CellMatrix_IF cell, KSE_IF kse)
			throws RemoteException {
		this.cellMatrix = cell;
		this.kse = kse;

		int currentPlayer = kse.getCurrentPlayer();
		if (currentPlayer == 1) {
			this.currentPlayer = currentPlayer;
		} else {
			this.currentPlayer = currentPlayer;
		}
		this.numOfSpectators = kse.countSpectators();
	}

	public void set_winer(int player) {
		if (player == 1) {
			JOptionPane.showMessageDialog(null, strPlayerName[0]
					+ " eshte fitues!");
		}
		if (player == 2) {
			JOptionPane.showMessageDialog(null, strPlayerName[1]
					+ " eshte fitues!");

		}
	}

	private String getPlayerMsg() {

		if (hasWon) {
			return "Congrats " + strPlayerName[currentPlayer - 1]
					+ ", you are the winner!";
		} else if (firstTime) {
			return "" + strPlayerName[currentPlayer-1] + " you are red, "
					+ strPlayerName[1]
					+ " you are blue. Press new game to start";
		} else {
			return "" + strPlayerName[currentPlayer - 1] + " move";
		}

	}

	public void changePlayer() {
		if (currentPlayer == 1) {
			currentPlayer = 2;
		} else {
			currentPlayer = 1;
		}
	}

	public void setupImages(Image[] imgRed, Image[] imgBlue)
			throws RemoteException {

		imgPlayer[0] = imgRed;
		imgPlayer[1] = imgBlue;
		repaint();

	}

	public void setName(String strPlayer1Name, String strPlayer2Name) {

		strPlayerName[0] = strPlayer1Name;
		strPlayerName[1] = strPlayer2Name;
		strStatusMsg = getPlayerMsg();
		repaint();

	}

	protected void drawExtra(Graphics g) throws RemoteException {

		for (int i = 0; i < vecPaintInstructions.size(); i++) {

			currentInstruction = (objPaintInstruction) vecPaintInstructions
					.elementAt(i);
			int paintStartRow = currentInstruction.getStartRow();
			int paintStartColumn = currentInstruction.getStartColumn();
			int rowCells = currentInstruction.getRowCells();
			int columnCells = currentInstruction.getColumnCells();

			for (int row = paintStartRow; row < (paintStartRow + rowCells); row++) {

				for (int column = paintStartColumn; column < (paintStartColumn + columnCells); column++) {
					int playerCell = cellMatrix.getPlayerCell(row, column);
					int pieceCell = cellMatrix.getPieceCell(row, column);
					if (playerCell != 0) {
						try {
							g.drawImage(imgPlayer[playerCell - 1][pieceCell],
									((column + 1) * 50) - 50,
									((row + 1) * 50) - 50, this);
						} catch (ArrayIndexOutOfBoundsException e) {
						}
					}

				}

			}

		}

		g.setColor(new Color(0,0,0));
		g.drawString(strStatusMsg, 15, 410);
		if (this.currentPlayer == 2) {
			g.setColor(new Color(0, 255, 0));
		} else {
			g.setColor(new Color(255, 0, 0));
		}
		
		g.drawString(strPlayerName[1], 15,430);//klienti
		if (this.currentPlayer == 2) {
			g.setColor(new Color(255, 0, 0));
		} else {
			g.setColor(new Color(0, 255, 0));
		}
		g.drawString(strPlayerName[0], 15,450);
		g.setColor(new Color(0, 0, 0));
		
		g.drawString("Numri i spektatoreve: "+numOfSpectators,280,450);

		vecPaintInstructions.clear(); // clear all paint instructions
	}

	private void updatePaintInstructions(int desRow, int desColumn) {

		currentInstruction = new objPaintInstruction(startRow, startColumn, 1);
		vecPaintInstructions.addElement(currentInstruction);

		currentInstruction = new objPaintInstruction(desRow, desColumn);
		vecPaintInstructions.addElement(currentInstruction);

	}

	public void refresh() {
		repaint();
	}

	public void newGame() {
		JOptionPane.showMessageDialog(null, "Filloj loja e re");
		repaint();
	}

	public void setCountedSpectators(int numer) {
		numOfSpectators = numer;
	}
}