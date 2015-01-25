import javax.swing.*;


import java.awt.event.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ViewMember extends JFrame{
	private JLabel name,bDate,dDate,descL;
	private JTextArea description;
	private JButton edit,delete,back,quit,vJpg,vDoc,vGif,vr;
	private Driver driver;
	
	public ViewMember(Driver d){
		//Initialize the window
		super("FamBook-View Member");
		setBounds(300,100,800,600);
		setLayout(null);
		setVisible(true);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		String[] memberInfo=null;
		driver=d;
		//System.out.println(driver.getAlbumID()+driver.getMemberID());
		try {
			memberInfo=driver.getConnector().getMemberInformationById(driver.getAlbumID(), driver.getMemberID());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//Display name
		name = new JLabel("Name:");
		name.setBounds(50, 70, 100, 25);
		add(name,0);
		
		name = new JLabel("");
		name.setBounds(125, 70, 200, 25);
		name.setText(memberInfo[1] + " " + memberInfo[2]);
		add(name,0);
		
		//Display birth date
		bDate = new JLabel("Birth Date:");
		bDate.setBounds(50, 120, 100, 25);	
		add(bDate,0);
		
		bDate = new JLabel("");
		bDate.setBounds(125, 120, 200, 25);
		bDate.setText(memberInfo[3]);
		add(bDate,0);
		
		//Display death date
		dDate = new JLabel("Death Date:");
		dDate.setBounds(50, 170, 100, 25);
		add(dDate,0);
		
		dDate = new JLabel("");
		dDate.setBounds(125, 170, 200, 25);
		dDate.setText(memberInfo[4]);
		add(dDate,0);
		
		//Display description
		descL = new JLabel("Description:");
		descL.setBounds(50, 220, 80, 25);
		add(descL,0);
		
		description = new JTextArea(16,16);
		description.setLocation(150,230);
		description.setSize(250,175);
		description.setEditable(false);
		description.setLineWrap(true);
		description.setWrapStyleWord(true);
		description.setText(memberInfo[5]);
		add(description,0);
		
		//Display the "View Relationship" button on window
		vr = new JButton("View Relationships");
		vr.setBounds(600, 75,150,25);
		vr.addActionListener(new ViewRelationshipButtonHandler());
		add(vr,0);

		//Display the "View Pictures" button on window
		vJpg = new JButton("View Pictures");
		vJpg.setBounds(600, 125,150,25);
		vJpg.addActionListener(new ViewPictureButtonHandler());
		add(vJpg,0);
		
		//Display the "View Document" button on window
		vDoc = new JButton("View Documents");
		vDoc.setBounds(600, 175,150,25);
		vDoc.addActionListener(new ViewDocumentButtonHandler());
		add(vDoc,0);
		
		//Display the "View Graphic" button on window
		vGif = new JButton("View Graphics");
		vGif.setBounds(600, 225,150,25);
		vGif.addActionListener(new ViewGraphicButtonHandler());
		add(vGif,0);
		
		//Display the "Edit" button on window
		edit = new JButton("Edit Profile");
		edit.setBounds(50, 500,125,25);
		edit.addActionListener(new EditButtonHandler());
		add(edit,0);
		
//		//Display the "Delete" button on window
//		delete = new JButton("Delete Profile");
//		delete.setBounds(200, 500,125,25);
//		delete.addActionListener(new DeleteButtonHandler());
//		add(delete,0);
		
		//Display the "Back" button on window
		back = new JButton("Back");
		back.setBounds(570, 500,75,25);
		back.addActionListener(new BackButtonHandler());
		add(back,0);
		
		//Display the "Quit" button on window
		quit = new JButton("Quit");
		quit.setBounds(670, 500,75,25);
		quit.addActionListener(new QuitButtonHandler());
		add(quit,0);
		
		repaint();
		
		
	}
	public class QuitButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	
	public class BackButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			AlbumMenu am = new AlbumMenu(driver);
			setVisible(false);
			repaint();
		}
	}
	public class EditButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
//            try {
//                //ModifyProfiles mp = new ModifyProfiles(driver);
//            }
//            //catch (SQLException ex) {
//                Logger.getLogger(ViewMember.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            setVisible(false);
//            repaint();
		}
	}
//	public class DeleteButtonHandler implements ActionListener{
//		public void actionPerformed(ActionEvent e) {
//		}
//	}
	public class ViewRelationshipButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			ViewRelationship vr = new ViewRelationship(driver);
			setVisible(false);
			repaint();
		}
	}
	public class ViewPictureButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			ViewPicture vp = new ViewPicture(driver);
			setVisible(false);
			repaint();
		}
	}
	public class ViewDocumentButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			ViewDocument vd = new ViewDocument(driver);
			setVisible(false);
			repaint();
		}
	}
	public class ViewGraphicButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			ViewGraphic vg= new ViewGraphic(driver);
			setVisible(false);
			repaint();
		}
	}

}
