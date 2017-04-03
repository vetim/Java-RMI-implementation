package chess;
public class objBishop extends objChessPieces
{
	
	public void objBishop ()
	{
	}
	
	public boolean legalMove (int startRow, int startColumn, int desRow, int desColumn, int[][] playerMatrix)
	{
		
		if (startRow == desRow || startColumn == desColumn) //If moved straight
		{
			
			strErrorMsg = "Bishop can only move along diagonal lines";
			return false;
			
		}
		
		return axisMove(startRow, startColumn, desRow, desColumn, playerMatrix, false);
		
	}
	
}