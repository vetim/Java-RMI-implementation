package chess;
public class objKnight extends objChessPieces
{
	
	public void objKnight()
	{
	}
	
	public boolean legalMove (int startRow, int startColumn, int desRow, int desColumn, int[][] playerMatrix)
	{
		
		finalDesRow = desRow;
		finalDesColumn = desColumn;
		strErrorMsg = "Horse can only move in a L shape";
		
		if (2 == Math.abs(startRow - desRow) && 1 == Math.abs(startColumn - desColumn)) //2N, 1E
		{
			return true;
		}
		else if (1 == Math.abs(startRow - desRow) && 2 == Math.abs(startColumn - desColumn)) //2N, 1W
		{
			return true;
		}
		return false;
		
	}
	
}