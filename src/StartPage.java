import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.sql.SQLException;

public class StartPage extends JFrame{
	private JButton go,quit,add;
	private JLabel welcome,instr,message;
	private JTextField name;
	private Driver driver;
	
	public StartPage(Driver d){		
		//Initialize the window
		super("FamBook");
		setBounds(300,100,800,600);
		//setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		makeWindow();
		setVisible(true);
		repaint();
		driver=d;
	}
	private void makeWindow(){
		//Display welcome on window
		welcome = new JLabel("Welcome to FamBook",JLabel.CENTER);
		welcome.setFont(new Font("Marker Felt", Font.PLAIN, 48));
		welcome.setBounds(100, 100, 600, 100);
		welcome.setVerticalTextPosition(JLabel.CENTER);
		welcome.setHorizontalTextPosition(JLabel.CENTER);
		add(welcome,0);
		
		//Display instruction on window
		instr = new JLabel("Please Type in Your Album Name:",JLabel.CENTER);
		instr.setBounds(100, 250, 600, 50);
		instr.setVerticalTextPosition(JLabel.CENTER);
		instr.setHorizontalTextPosition(JLabel.CENTER);	
		add(instr,0);
		
		//Display type-in text field on window
		name = new JTextField();
		name.setBounds(250, 300,300,50);
		name.setEditable(true);
		add(name,0);

		//Display the "add" button on window
		add = new JButton("New Album");
		add.addActionListener(new AddButtonHandler());
		add.setBounds(400, 380,125,25);
		add(add,0);
		
		//Display the "go" button on window
		go = new JButton("Go");
		go.addActionListener(new GoButtonHandler());
		go.setBounds(280, 380,75,25);
		add(go,0);
		
		//Display the "Quit" button on window
		quit = new JButton("Quit");
		quit.addActionListener(new QuitButtonHandler());
		quit.setBounds(700, 500,75,25);
		add(quit,0);
	}
	
	public class AddButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			CreateAlbum ca = new CreateAlbum(driver);
			setVisible(false);
			repaint();
		}
	}
	
	public class QuitButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	
	public class GoButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e){		
			try { 
				if (name.getText().compareTo("")==0)
				{
					if(message!=null) remove(message);
					message=new JLabel("Album Name Cannot Be Empty.");
					message.setBounds(300,420,200,25);
					add(message,0);
					repaint();
				}
				else 
				{
					String albumNumber = "";
					driver.setAlbumName(name.getText());
					albumNumber = driver.getConnector().openAlbum(name.getText());
					if(albumNumber.equals("")){
						if(message!=null) remove(message);
						message=new JLabel("Album Name Does Not Exist.");
						message.setBounds(300,420,200,25);
						add(message,0);
						repaint();
					}else{
						driver.setAlbumID(albumNumber);
						AlbumMenu am = new AlbumMenu(driver);
						setVisible(false);
						repaint();
					}
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}	
		}
	}
}