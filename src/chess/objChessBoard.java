package chess;
//Standard chess board which can be used for chess, draughts etc
import java.awt.*;
import java.rmi.RemoteException;
import java.util.Vector;

import javax.swing.JPanel;

public class objChessBoard extends Canvas
{
	
	protected objPaintInstruction currentInstruction = null;
	protected Vector vecPaintInstructions = new Vector();
	
	public void chessBoard ()
	{
	}
	
	public void update (Graphics g)
	{
		paint(g);
	
	}
	
	public void paint(Graphics g)
	{
		
		if (vecPaintInstructions.size() == 0)
		{
			currentInstruction = new objPaintInstruction(0,0,8);
			vecPaintInstructions.addElement(currentInstruction);
			
		}
		
		g.setColor(new Color(80,80,80));
		g.fillRect(0,400,450,450); //Paint over the current status text		
		
		for (int i = 0; i < vecPaintInstructions.size(); i++)
		{
			
			currentInstruction = (objPaintInstruction)vecPaintInstructions.elementAt(i);
			int startRow = currentInstruction.getStartRow();
			int startColumn = currentInstruction.getStartColumn();
			int rowCells = currentInstruction.getRowCells();
			int columnCells = currentInstruction.getColumnCells();
			
			for (int row = startRow; row < (startRow + rowCells); row++)
			{
				
				for (int column = startColumn; column < (startColumn + columnCells); column++)
				{
					drawTile(row, column, g);
				}	
			}	
		}
		try {
			drawExtra(g);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void drawTile (int row, int column, Graphics g)
	{
		
		if ((row + 1) % 2 == 0)
		{
			
			if ((column + 1) % 2 == 0)
			{
				g.setColor(new Color(255,255,255)); //white
			}
			else
			{
				g.setColor(new Color(0,0,0)); //black
			}
			
		}
		else
		{
			
			if ((column + 1) % 2 == 0)
			{
				g.setColor(new Color(0,0,0));
			}
			else
			{
				g.setColor(new Color(255,255,255));
			}
			
		}
		
		g.fillRect(column * 50, row * 50, 50, 50);
		
	}	
	//Protected means it can only be used by this class, and classes extending it
	protected void drawExtra(Graphics g) throws RemoteException //Any class extending the chess board can use this method to add extra things like player pieces
	{
		
	}
	
	protected int findWhichTileSelected (int coor) //Finds which tile the mouse is over
	{
		
		for (int i = 0; i < 8; i++)
		{
			
			if ((coor >= ((i * 50))) && (coor <= (50+(i * 50))))
			{
				return i;
			}
			
		}
		
		return -1;
		
	}
	
}