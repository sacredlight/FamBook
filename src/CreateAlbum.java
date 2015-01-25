import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class CreateAlbum extends JFrame{
	private JButton enter,back,quit;
	private JTextField name;
	private JLabel nameL,message;
	private Driver driver;
	
	public CreateAlbum(Driver d){
		//Initialize the window
		super("FamBook-Create Album");
		setBounds(300,100,800,600);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		setVisible(true);
		
		//Display name input field
		nameL = new JLabel("Please Type in Preferred Album Name:");
		nameL.setBounds(260, 160, 300, 25);
		add(nameL,0);
		
		name = new JTextField();
		name.setBounds(250, 200,300,50);
		name.setEditable(true);
		add(name,0);
		
		//Display the "enter" button on window
		enter = new JButton("Enter");
		enter.addActionListener(new EnterButtonHandler());
		enter.setBounds(350, 280,100,25);
		add(enter,0);
		
		//Display the "Back" button on window
		back = new JButton("Back");
		back.addActionListener(new BackButtonHandler());
		back.setBounds(600, 530,75,25);
		add(back,0);
		
		//Display the "Quit" button on window
		quit = new JButton("Quit");
		quit.addActionListener(new QuitButtonHandler());
		quit.setBounds(700, 530,75,25);
		add(quit,0);
		
		repaint();
		driver=d;
	}
	
	public class QuitButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	
	public class BackButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			StartPage sp = new StartPage(driver);
			setVisible(false);
			repaint();
		}
	}
	
	public class EnterButtonHandler implements ActionListener {
	   	 public void actionPerformed(ActionEvent e){   		 					
	   		 	try {
					if (name.getText().compareTo("")==0)
					{
						if(message!=null) remove(message);
						message=new JLabel("Album name cannot be empty.",JLabel.CENTER);
						message.setBounds(280,320,250,25);
						add(message,0);
						repaint();
					}
					else
					{	
						String albumName = name.getText();
						driver.setAlbumName(albumName);
						String id=driver.getConnector().addAlbum(albumName);
						if(id.compareTo("")!=0){
							driver.setAlbumID(id);
							AlbumMenu am = new AlbumMenu(driver);
							setVisible(false);
							repaint();
						}else{
							if(message!=null) remove(message);
							message=new JLabel("Album Name Already Exsits",JLabel.CENTER);
							message.setBounds(280,320,250,25);
							add(message,0);
							repaint();
						}
						
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
	   	 }
	}
}
