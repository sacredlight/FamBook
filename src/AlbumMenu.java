import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class AlbumMenu extends JFrame{
	private JLabel welcome;
	private JButton add,view,back,quit;
	private Driver driver;
	public AlbumMenu(Driver d){
		//Initialize the window
		super("FamBook-Album Menu");
		setBounds(300,100,800,600);
		setLayout(null);
		setVisible(true);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Display welcome on window
		welcome = new JLabel("Welcome to "+"Family Album "+d.getAlbumName(),JLabel.CENTER);
		welcome.setFont(new Font("Arial", Font.PLAIN, 28));
		welcome.setBounds(100, 100, 600, 100);
		add(welcome,0);
		
		//Add "Add Member" button
		add = new JButton("Add New Member");
		add.setBounds(200, 225,400,100);
		add.addActionListener(new AddButtonHandler());
		add(add,0);
		
		//Add "View Member" button
		view = new JButton("View/Delete Family Members");
		view.setBounds(200, 350,400,100);
		view.addActionListener(new ViewButtonHandler());
		add(view,0);
		
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
		driver = d;
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
	public class AddButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			AddMember am = new AddMember(driver);
			setVisible(false);
			repaint();
		}
	}
	public class ViewButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			SearchPage sp= new SearchPage(driver);
			setVisible(false);
			repaint();
		}
	}
}