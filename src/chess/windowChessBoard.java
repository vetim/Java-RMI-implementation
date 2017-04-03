package chess;
import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JDialog;

import rmi.server.*;
public class windowChessBoard extends objChessBoard implements MouseListener, MouseMotionListener
{
	
	private final int refreshRate = 5; //Amount of pixels moved before screen is refreshed
	
	private Image[][] imgPlayer = new Image[2][6];
	private String[] strPlayerName = {"Player1", "Player2"};
	private String strStatusMsg = "";
	private objCellMatrix cellMatrix;
	private int currentPlayer = 1;
	private int startRow = 0, startColumn = 0, pieceBeingDragged = 0;
	private int startingX = 0, startingY = 0, currentX = 0, currentY = 0, refreshCounter = 0;
	private boolean firstTime = true, hasWon = false, isDragging = false;
	
	private objPawn pawnObject = new objPawn();
	private objRock rockObject = new objRock();
	private objKnight knightObject = new objKnight();
	private objBishop bishopObject = new objBishop();
	private objQueen queenObject = new objQueen();
	private objKing kingObject = new objKing();
	private KSE kse;
	private int numOfSpectators = 0;
	
	public windowChessBoard (objCellMatrix cell,KSE kse) throws RemoteException
	{	
		this.kse=kse;
		this.cellMatrix=cell;	
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.numOfSpectators=kse.countSpectators();

		
	}
	
	private String getPlayerMsg () throws RemoteException
	{
		
		if (hasWon)
		{set_winner(1);
			return "Bravo " + strPlayerName[0] + ", ju jeni fitues!";
		}
		else if (firstTime)
		{
			return "" + strPlayerName[0] + " ju jeni i kuq, " + strPlayerName[1] + " ju jeni i kalter.";
		}
		else
		{
			return ""+ strPlayerName[0] + " zhvendosje";
		}
		
	}		 
	public void set_winner(int player) throws RemoteException {
		if (player == 1) {
			JOptionPane.showMessageDialog(null, "Urime "+ strPlayerName[0]+", ju jeni fitues!");
			kse.set_winner(1);
		}
		if (player == 2) {
			JOptionPane.showMessageDialog(null, strPlayerName[0]+" nuk fituat lojen!");
		}
	}
	
	private void resetBoard () throws RemoteException
	{
		
		hasWon = false;
		currentPlayer = 1;
		strStatusMsg = getPlayerMsg();
		cellMatrix.resetMatrix();
		repaint();
		
	}
	
	public void setupImages (Image[] imgRed, Image[] imgBlue) throws RemoteException
	{
		
		imgPlayer[0] = imgRed;
		imgPlayer[1] = imgBlue;
		resetBoard();
		
	}
	
	public int getCurrentPlayer(){
		return currentPlayer;
	}
	
	public void setName (String strPlayer1Name, String strPlayer2Name) throws RemoteException
	{
		strPlayerName[0] = strPlayer1Name;
		strPlayerName[1] = strPlayer2Name;
		strStatusMsg = getPlayerMsg();
		repaint();	
	}
	
	protected void drawExtra (Graphics g) throws RemoteException
	{
		
		for (int i = 0; i < vecPaintInstructions.size(); i++)
		{
			
			currentInstruction = (objPaintInstruction)vecPaintInstructions.elementAt(i);
			int paintStartRow = currentInstruction.getStartRow();
			int paintStartColumn = currentInstruction.getStartColumn();
			int rowCells = currentInstruction.getRowCells();
			int columnCells = currentInstruction.getColumnCells();
			
			for (int row = paintStartRow; row < (paintStartRow + rowCells); row++)
			{
				
				for (int column = paintStartColumn; column < (paintStartColumn + columnCells); column++)
				{
					int playerCell = cellMatrix.getPlayerCell(row, column);
					int pieceCell = cellMatrix.getPieceCell(row, column);
					if (playerCell != 0)
					{
						try
						{
							g.drawImage(imgPlayer[playerCell - 1][pieceCell], ((column + 1) * 50)-50, ((row + 1) * 50)-50, this);
						}
						catch (ArrayIndexOutOfBoundsException e)
						{
						}
					}	
				}
			}
		}
		
		if (isDragging)
		{
			g.drawImage(imgPlayer[0][pieceBeingDragged], (currentX - 25), (currentY - 25), this);
		}		

		g.setColor(new Color(0,0,0));
		g.drawString(strStatusMsg, 15, 410);
		if (this.currentPlayer == 1) {
			g.setColor(new Color(255, 0, 0));
		} else {
			g.setColor(new Color(0, 255, 0));
		}
		
		g.drawString(strPlayerName[1], 15,430);
		if (this.currentPlayer == 1) {
			g.setColor(new Color(0, 255, 0));
		} else {
			g.setColor(new Color(255, 0, 0));
		}
		g.drawString(strPlayerName[0], 15,450);
		
		
		g.setColor(new Color(0, 0, 0));
		g.drawString("Numri i spektatoreve: "+numOfSpectators,280,450);
	
			
		vecPaintInstructions.clear(); //clear all paint instructions
	}
	
	public void newGame(int player) throws RemoteException {
		if (player == 2) {
			if (firstTime == true) {
				JOptionPane.showMessageDialog(this, "filloj loja e re");
				firstTime = false;
			} else {
				JOptionPane.showMessageDialog(this,	"filloj loja e re,ju jeni fitues");
			}
			repaint();
		} else {
			if (firstTime == true) {
				firstTime = false;
				
			}
			resetBoard();
		}

	}
	
	private void checkMove (int desRow, int desColumn) throws RemoteException
	{
		
		boolean legalMove = false;
		
		if (cellMatrix.getPlayerCell(desRow,desColumn) == currentPlayer)
		{
			strStatusMsg = "Can not move onto a piece that is yours";
		}
		else
		{
			
			switch (pieceBeingDragged)
			{
				
				case 0: legalMove = pawnObject.legalMove(startRow, startColumn, desRow, desColumn, cellMatrix.getPlayerMatrix(), currentPlayer);
						break;
				case 1: legalMove = rockObject.legalMove(startRow, startColumn, desRow, desColumn, cellMatrix.getPlayerMatrix());
						break;
				case 2: legalMove = knightObject.legalMove(startRow, startColumn, desRow, desColumn, cellMatrix.getPlayerMatrix());
						break;
				case 3: legalMove = bishopObject.legalMove(startRow, startColumn, desRow, desColumn, cellMatrix.getPlayerMatrix());
						break;
				case 4: legalMove = queenObject.legalMove(startRow, startColumn, desRow, desColumn, cellMatrix.getPlayerMatrix());
						break;
				case 5: legalMove = kingObject.legalMove(startRow, startColumn, desRow, desColumn, cellMatrix.getPlayerMatrix());
						break;
						
			}
			
		}				
		
		if (legalMove)
		{	
			cellMatrix.setPlayerCell(desRow, desColumn, currentPlayer);
			
			if (pieceBeingDragged == 0 && (desRow == 0 || desRow == 7)) //If pawn has got to the end row
			{
				
				boolean canPass = false;
				int newPiece = 2;
				String strNewPiece = "Rock";
				String[] strPieces = {"Rock","Knight","Bishop","Queen"};
				JOptionPane digBox = new JOptionPane("Choose the piece to change your pawn into", 
				JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, strPieces, "Rock");
				JDialog dig = digBox.createDialog(null, "pawn at end of board");
				
				do
				{					
					
					dig.setVisible(true);					
					
					try
					{
						
						strNewPiece = digBox.getValue().toString();
						
						for (int i = 0; i < strPieces.length; i++)
						{
						
							if (strNewPiece.equalsIgnoreCase(strPieces[i]))
							{

								canPass = true;
								newPiece = i+1;
								
							}
							
						}
						
					}
					catch (NullPointerException e)
					{
						canPass = false;							
					}												

				}
				while (canPass == false);

				cellMatrix.setPieceCell(desRow, desColumn, newPiece);
				
			}
			else
			{
				cellMatrix.setPieceCell(desRow, desColumn, pieceBeingDragged);
			}							
			if (cellMatrix.checkWinner(currentPlayer))
			{
				hasWon = true;
				strStatusMsg = getPlayerMsg();					
			}
			else
			{					
				changePlayer();
				strStatusMsg = getPlayerMsg();
			}			
		}
		else
		{
			
			switch (pieceBeingDragged)
			{
				
				case 0: strStatusMsg = pawnObject.getErrorMsg();
						break;
				case 1: strStatusMsg = rockObject.getErrorMsg();
						break;
				case 2: strStatusMsg = knightObject.getErrorMsg();
						break;
				case 3: strStatusMsg = bishopObject.getErrorMsg();
						break;
				case 4: strStatusMsg = queenObject.getErrorMsg();
						break;
				case 5: strStatusMsg = kingObject.getErrorMsg();
						break;
						
			}
				
			unsucessfullDrag();
			
		}
			
	}
	
	public void changePlayer() {
		if (currentPlayer == 1) {
			currentPlayer = 0;
		} else {
			currentPlayer = 1;
		}

	}
	private void unsucessfullDrag () throws RemoteException
	{
		
		cellMatrix.setPieceCell(startRow, startColumn, pieceBeingDragged);
		cellMatrix.setPlayerCell(startRow, startColumn, currentPlayer);
		
	}
	
	private void updatePaintInstructions (int desRow, int desColumn)
	{
		
		currentInstruction = new objPaintInstruction(startRow, startColumn, 1);
		vecPaintInstructions.addElement(currentInstruction);
			
		currentInstruction = new objPaintInstruction(desRow, desColumn);
		vecPaintInstructions.addElement(currentInstruction);
		
	}
	
	public void mouseClicked (MouseEvent e)
	{
	}
	
	public void mouseEntered (MouseEvent e)
	{
	}
	
	public void mouseExited (MouseEvent e)
	{
		mouseReleased(e);
	}
	
	public void mousePressed (MouseEvent e)
	{
		
		if (!hasWon && !firstTime)
		{			
		
			int x = e.getX();
			int y = e.getY();
			
			if ((x > 10 && x < 380) && (y > 10 && y < 380)) //in the correct bounds
			{
			
				startRow = findWhichTileSelected(y);
				startColumn = findWhichTileSelected(x);
						
				try {
					if (cellMatrix.getPlayerCell(startRow, startColumn) == currentPlayer)
					{
						
						pieceBeingDragged = cellMatrix.getPieceCell(startRow, startColumn);
						cellMatrix.setPieceCell(startRow, startColumn, 6);
						cellMatrix.setPlayerCell(startRow, startColumn, 0);
						isDragging = true;
						
					}
					else
					{
						isDragging = false;
					}
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
			}
			
		}
		
	}
	
	public void mouseReleased (MouseEvent e)
	{
		if (isDragging)
		{	
			isDragging = false;
			
			int desRow = findWhichTileSelected(currentY);
			int desColumn = findWhichTileSelected(currentX);
			try {
				checkMove(desRow, desColumn);
				kse.broadcastUpdate();
				if(currentPlayer!=1){
				kse.changePlayer(1);
				}
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}			
			repaint();
		}
	}
	
	public void mouseDragged(MouseEvent e)
	{
		
		if (isDragging)
		{
			
			int x = e.getX();
			int y = e.getY();
			
			if ((x > 10 && x < 380) && (y > 10 && y < 380)) //If in the correct bounds
			{
			
				if (refreshCounter >= refreshRate)
				{
								
					currentX = x;
					currentY = y;
					refreshCounter = 0;
					int desRow = findWhichTileSelected(currentY);
					int desColumn = findWhichTileSelected(currentX);
					
					updatePaintInstructions(desRow, desColumn);
					repaint();
					
				}
				else
				{
					refreshCounter++;
				}
			
			}
			
		}
		
	}
	
	public void mouseMoved (MouseEvent e)
	{
	}

	public void refresh()
	{
		repaint();		
	}

	public void gotFocus() {
		repaint();
	}
	public void setCountedSpectators(int numer){
		numOfSpectators=numer;
	}
				
}