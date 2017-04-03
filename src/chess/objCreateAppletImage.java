package chess;
import java.awt.*;
import java.io.*;
import javax.swing.ImageIcon;

public class objCreateAppletImage
{
	
	private String strErrorMsg = "";
	
	public Image getImage (Object parentClass, String path, int fileSize)
	{
		
		byte buff[] = createImageArray(parentClass, path, fileSize);
		return Toolkit.getDefaultToolkit().createImage(buff);
		
	}
	
	public ImageIcon getImageIcon (Object parentClass, String path, String description, int fileSize)
	{
		
		byte buff[] = createImageArray(parentClass, path, fileSize);
		return new ImageIcon(Toolkit.getDefaultToolkit().createImage(buff), description);
		
	}
	
	public void objCreateAppletImage()
	{
	}
	
	public String getErrorMsg ()
	{
		return strErrorMsg;
	}
	
	private byte[] createImageArray (Object parentClass, String path, int fileSize)
	{
		
		int count = 0;

		BufferedInputStream imgStream = new BufferedInputStream(parentClass.getClass().getResourceAsStream(path));
		
		if (imgStream != null) //If file exists
		{
			
			byte buff[] = new byte[fileSize]; //Create the array of bytes
			
			try
			{				
				count = imgStream.read(buff);
			}
			catch (IOException e)
			{
				strErrorMsg = "Error reading from file: " + path;
			}
			
			try
			{
				imgStream.close(); //Closes the stream
			}
			catch (IOException e)
			{
				strErrorMsg = "Error closing file: " + path;
			}
			
			if (count <= 0)
			{
				
				strErrorMsg = "Error, empty file: " + path;
				return null;
				
			}
			
			return buff;
			
		}
		else
		{
			
			strErrorMsg = "Could Not Find File: " + path;
			return null;
			
		}
		
	}
	
}	