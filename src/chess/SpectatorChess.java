package chess;
import java.awt.*;
import java.awt.event.*;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.*;

import rmi.client.Client;
import rmi.client.Spectator;
import rmi.server.KSE_IF;
import rmi.server.ServerDriver;

public class SpectatorChess implements  WindowFocusListener
{
	
	private windowChessBoardSpectator mainChessBoard;
	private objCreateAppletImage createImage;
	private JLabel lblPlayerOne, lblPlayerTwo;
	private String[] strRedPieces = {"redPawn.gif","redRock.gif","redKnight.gif","redBishop.gif","redQueen.gif","redKing.gif"};
	private String[] strBluePieces = {"bluePawn.gif","blueRock.gif","blueKnight.gif","blueBishop.gif","blueQueen.gif","blueKing.gif"};
	private Color clrBlue = new Color(200,200,200,0);
	private MediaTracker mt;
	private Spectator sp;
	public SpectatorChess(String server,String ClientPlayerName) throws RemoteException, MalformedURLException //With applications, you have to specify a main method (not with applets)
	, NotBoundException
		{

	      JFrame frame = new JFrame("Chess Game"); //Title
	      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	 
	      
	      frame.setContentPane(createGUI(frame,server,ClientPlayerName));
	      
	      
	      frame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent windowevent) {
					new Thread() {
						public void run() {
							try {
								
								get_kse().deleteSpectator(sp);
								
							} catch (RemoteException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}}}.start();
								System.exit(1);
							
				}
			});
	      
	      
	      get_kse().setName(3, ClientPlayerName);
	      
	      frame.addWindowFocusListener(this);
	      frame.setTitle("Spectator frame");
	      frame.pack();       
	      frame.setResizable(false);
	      frame.setLocationRelativeTo(null);
	      frame.setVisible(true);  
	      
	  }
	
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public Container createGUI (JFrame frame,String serverURL,String clientPlayerName) throws RemoteException, MalformedURLException, NotBoundException
 {
		String server = "rmi://"+serverURL+"/RMI_KSE";
		String CellMatrix = "rmi://"+serverURL+"/RMICellMatrix";
		sp = new Spectator(server, CellMatrix);	
		
		
		
		/////////////////////////////////		
		JPanel panRoot = new JPanel(){
			Image back = Toolkit.getDefaultToolkit().createImage("src/images/1.jpg");		
			public void paintComponent(Graphics g){
				super.paintComponent(g);
				
				g.drawImage(back, 0, 0,550,650, this);	
			}
		};

		
		panRoot.setOpaque(true);
	    panRoot.setPreferredSize(new Dimension(500, 586));
		mainChessBoard = new windowChessBoardSpectator(sp.getCellMatrix(),sp.get_KSE());
	    mainChessBoard.setLocation(45, 35);
		sp.set_wcb(mainChessBoard);
		
		createImage = new objCreateAppletImage();
		
		mainChessBoard.setSize(new Dimension(399, 455));
		
		try
		{
			
			Image[] imgRed = new Image[6];
			Image[] imgBlue = new Image[6];
			mt = new MediaTracker(frame);
			
			for (int i = 0; i < 6; i++)
			{				
			
				imgRed[i] = createImage.getImage(this, "/images/" + strRedPieces[i], 5000);
				imgBlue[i] = createImage.getImage(this, "/images/" + strBluePieces[i], 5000);
				mt.addImage(imgRed[i], 0);
				mt.addImage(imgBlue[i], 0);
				
			}
			
			try
			{
				mt.waitForID(0);
			}
			catch (InterruptedException e)
			{
			}
			
			mainChessBoard.setupImages(imgRed, imgBlue);
			
		}
		catch (NullPointerException e)
		{
			
			JOptionPane.showMessageDialog(null, "Unable to load images. There should be a folder called images with all the chess pieces in it. Try downloading this programme again", "Unable to load images", JOptionPane.WARNING_MESSAGE);

		}
		
		JPanel panBottomHalf = new JPanel(new BorderLayout());
		panBottomHalf.setBounds(239, 510, 71, 35);
		JPanel panNewGame = new JPanel();
		panNewGame.setOpaque(false);
		panRoot.setLayout(null);
		panRoot.add(mainChessBoard);
		panRoot.add(panBottomHalf);
		panBottomHalf.add(panNewGame, BorderLayout.SOUTH);
		
		//panBottomHalf.add(mainChessBoard, BorderLayout.NORTH);
		
		panRoot.setBackground(clrBlue);
		panBottomHalf.setBackground(clrBlue);
		panNewGame.setBackground(clrBlue);
		
		
		
		return panRoot;
		
		
		
	}
	
	
	
	public void windowGainedFocus (WindowEvent e)
	{
		//mainChessBoard.gotFocus();
	}
	
	public void windowLostFocus (WindowEvent e)
	{
	}
	public KSE_IF get_kse() throws RemoteException{
		
		return sp.get_KSE();
	}
	
}