package chess;
import java.awt.*;
import java.awt.event.*;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.*;

import rmi.client.Client;
import rmi.server.KSE_IF;
import rmi.server.ServerDriver;

public class chessGUI_client implements ActionListener, WindowFocusListener
{
	
	private windowChessBoardClient mainChessBoard;
	private objCreateAppletImage createImage;
	private JButton cmdNewGame;
	private String[] strRedPieces = {"redPawn.gif","redRock.gif","redKnight.gif","redBishop.gif","redQueen.gif","redKing.gif"};
	private String[] strBluePieces = {"bluePawn.gif","blueRock.gif","blueKnight.gif","blueBishop.gif","blueQueen.gif","blueKing.gif"};
	private Color clrBlue = new Color(200,200,200,0);
	private MediaTracker mt;
	private Client c;
	
	public chessGUI_client(String server,String ClientPlayerName) throws RemoteException, MalformedURLException //With applications, you have to specify a main method (not with applets)
	, NotBoundException
		{
			

	      JFrame frame = new JFrame("Chess Game"); //Title
	      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	     
	    //  final chessGUI_client chessWindow = new chessGUI_client();
	      
			frame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent windowevent) {
					new Thread() {
						public void run() {
							try {
								
								get_kse().game_exit(2);
								
							} catch (RemoteException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}}}.start();						
							System.exit(1);
				}
			});
	      
	      frame.setContentPane(createGUI(frame,server,ClientPlayerName));
	      
	      get_kse().setName(2, ClientPlayerName);
	      
	      frame.addWindowFocusListener(this);
	      frame.setTitle("Client frame");
	      frame.setResizable(false);
	      frame.pack();
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
		c = new Client(server, CellMatrix);	
		
		
		
		/////////////////////////////////
		JPanel panRoot = new JPanel(){
			Image back = Toolkit.getDefaultToolkit().createImage("src/images/1.jpg");		
			public void paintComponent(Graphics g){
				super.paintComponent(g);
				
				g.drawImage(back, 0, 0,550,650, this);	
			}
		};
		panRoot.setOpaque(true);
	    panRoot.setPreferredSize(new Dimension(500,586));
		
		mainChessBoard = new windowChessBoardClient(c.getCellMatrix(),c.get_KSE());
		mainChessBoard.setLocation(45, 35);
		c.set_wcb(mainChessBoard);
		
		
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
			cmdNewGame.setEnabled(false);
			
			
		}
		
		JPanel panBottomHalf = new JPanel(new BorderLayout());
		panBottomHalf.setBounds(45, 498, 399, 77);
		JPanel panNewGame = new JPanel();
		panNewGame.setOpaque(false);
		panRoot.setLayout(null);
		panRoot.add(mainChessBoard);
		panRoot.add(panBottomHalf);
		panBottomHalf.add(panNewGame, BorderLayout.CENTER);
		
		//panBottomHalf.add(mainChessBoard, BorderLayout.NORTH);
		
		panRoot.setBackground(clrBlue);
		panBottomHalf.setBackground(clrBlue);
		panNewGame.setBackground(clrBlue);
		panNewGame.setLayout(null);
		
		cmdNewGame = new JButton("Loje e re");
		cmdNewGame.setBounds(318, 0, 81, 37);
		cmdNewGame.setFont(new Font("MV Boli", Font.BOLD | Font.ITALIC, 15));
		panNewGame.add(cmdNewGame);
		
		cmdNewGame.addActionListener(this);
		cmdNewGame.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		return panRoot;
		
		
	}
	
	public void actionPerformed(ActionEvent e)
	{
	///////	mainChessBoard.setNames(txtPlayerOne.getText(), txtPlayerTwo.getText());
		
		if (e.getSource() == cmdNewGame)
		{
			try {
				mainChessBoard.newGame(2);
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				c.get_KSE().newGame(2);
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
	
	public void windowGainedFocus (WindowEvent e)
	{
		//mainChessBoard.gotFocus();
	}
	
	public void windowLostFocus (WindowEvent e)
	{
	}
	public KSE_IF get_kse(){
		
		return c.get_KSE();
	}
	
}