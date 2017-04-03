package window;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.JRadioButton;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.border.BevelBorder;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import chess.*;
import java.awt.Font;
import javax.swing.UIManager;
public class startGame extends JPanel{
	//Game Start here!
	private JTextField txt_ip;
	private JTextField txt_port;
	private JButton OK;
	private JButton cancel;
	private JTextField txtPlayer;
	
	public startGame(final JFrame f) {
		setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		setLayout(null);
		ButtonGroup bg=new ButtonGroup();
		
		
		final JRadioButton serverRadioButton = new JRadioButton("Server");
		serverRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txt_ip.setEditable(false);
				txtPlayer.setEnabled(true);
			}
		});
		serverRadioButton.setSelected(true);
		serverRadioButton.setBounds(29, 60, 109, 23);
		bg.add(serverRadioButton);
		add(serverRadioButton);
		
		final JRadioButton clientRadioButton = new JRadioButton("Klient");
		clientRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txt_ip.setEditable(true);
				txtPlayer.setEnabled(true);
			}
		});
		clientRadioButton.setBounds(140, 60, 109, 23);
		bg.add(clientRadioButton);
		add(clientRadioButton);
		
		final JRadioButton spectatorRadioButton = new JRadioButton("Spektatore");
		spectatorRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txt_ip.setEditable(true);
				txtPlayer.setEnabled(false);
			}
		});
		spectatorRadioButton.setBounds(253, 60, 109, 23);
		bg.add(spectatorRadioButton);
		add(spectatorRadioButton);
	
		txt_ip = new JTextField();
		txt_ip.setEditable(false);
		txt_ip.setText("127.0.0.1");
		txt_ip.setToolTipText("ip");
		txt_ip.setName("");
		txt_ip.setBounds(140, 120, 119, 20);
		add(txt_ip);
		txt_ip.setColumns(10);
		
		txt_port = new JTextField();
		txt_port.setBounds(269, 120, 74, 20);
		txt_port.setText("1099");
		txt_port.setToolTipText("port");
		add(txt_port);
		txt_port.setColumns(10);
		
		txtPlayer = new JTextField();
		txtPlayer.setBounds(140, 89, 202, 20);
		add(txtPlayer);
		txtPlayer.setColumns(10);
		
		JLabel lblServerAddress = new JLabel("Server address:");
		lblServerAddress.setBounds(29, 119, 103, 23);
		add(lblServerAddress);
		
		OK = new JButton("OK");
		bg.add(OK);
		OK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if((txt_ip.getText().equals("")||txt_ip==null)&&!serverRadioButton.isSelected()){
					JOptionPane.showMessageDialog(f, "Shtyp adresen");
					
				}
				if(serverRadioButton.isSelected()&&!txt_port.getText().equals("")){
					if(!txtPlayer.getText().equals("")){
					try {
					new chessGUI(txt_port.getText(),txtPlayer.getText());
					f.setVisible(false);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(f, "Serveri nuk u startua!");
					e.printStackTrace();
				}
				 catch (MalformedURLException e){JOptionPane.showMessageDialog(f, "Adresa e serverit gabim!");}
					}else{JOptionPane.showMessageDialog(f, "Shtype emrin e lojtarit");}
				}
				if(clientRadioButton.isSelected()&&!txt_port.getText().equals("")){
					if(!txtPlayer.getText().equals("")){
					try {
						new chessGUI_client(txt_ip.getText()+":"+txt_port.getText(),txtPlayer.getText());
						f.setVisible(false);
					} catch (RemoteException | NotBoundException e) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(f, "Probleme me koneksionin!");
					}catch (MalformedURLException e){
						JOptionPane.showMessageDialog(f, "Adresa e serverit gabim!");
						
					}}else{JOptionPane.showMessageDialog(f, "Shtype emrin e lojtarit");}
				}
				if(spectatorRadioButton.isSelected()&&!txt_port.getText().equals("")){
					try {
						new SpectatorChess(txt_ip.getText()+":"+txt_port.getText(),txtPlayer.getText());
						f.setVisible(false);
					} catch (RemoteException | MalformedURLException
							| NotBoundException e) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(f, "Probleme me koneksionin!");
					}
				}
			
			}
			});
		OK.setBounds(140, 168, 89, 23);
		add(OK);
		
		cancel = new JButton("Cancel");
		bg.add(cancel);
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(1);
				
			}
		});
		cancel.setBounds(253, 168, 89, 23);
		add(cancel);
		
		JLabel lblZgjidheLlojinE = new JLabel("Zgjidhe llojin e koneksionit:");
		lblZgjidheLlojinE.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblZgjidheLlojinE.setBounds(29, 30, 292, 23);
		add(lblZgjidheLlojinE);
		
		
		
		JLabel lblEmri = new JLabel("Emri i Lojtarit: ");
		lblEmri.setBounds(29, 90, 82, 18);
		add(lblEmri);
		
		
	}
public static void main(String[] asf){
	try {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	} catch (Throwable e) {
		e.printStackTrace();
	}
	JFrame f =  new JFrame();
	f.getContentPane().add(new startGame(f));
	f.setSize(380,245);
	f.setLocationRelativeTo(null);
    f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
	f.setTitle("Krijimi i koneksionit");
    f.setVisible(true);


	
}	
}
