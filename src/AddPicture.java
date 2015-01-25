import javax.swing.*;

import java.awt.event.*;
import java.awt.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import java.io.IOException;
import java.sql.SQLException;

public class AddPicture extends JFrame implements DropTargetListener{
	private JLabel pathL,titleL,dateL,format, errorMessage;
	private JTextField path,title,bYear;
	private JTextArea pathDrop;
	private JComboBox bMonth,bDay;
	private JButton enter,back,quit;
	private Driver driver;
	private DropTarget dropTarget;
	private String filePath;
	
	
	public AddPicture(Driver d) {
		//Initialize the window
		super("FamBook-Add Picture");
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(300,100);		
		Container cont=new Container();
		cont.setPreferredSize(new Dimension(800,600));
		
		driver = d;
		
		//Display path input text field and matching label
		pathL = new JLabel("File Path");
		pathL.setBounds(250, 200, 200, 25);
		cont.add(pathL,0);
		
		
		pathDrop = new JTextArea();
		pathDrop.setBounds(350, 200, 150, 25);
		pathDrop.setText("Drag File Here");
		pathDrop.setEditable(false);
		cont.add(pathDrop);
		dropTarget = new DropTarget(pathDrop, this);
		
		
		//Display title input text field and matching label
		titleL = new JLabel("Title");
		titleL.setBounds(250, 250, 70, 25);
		cont.add(titleL,0);
		
		title = new JTextField();
		title.setBounds(350, 250,150,25);
		title.setEditable(true);
		cont.add(title,0);
		
		//Display date input field and matching label
		dateL = new JLabel("Date");
		dateL.setBounds(250, 290, 75, 25);
		cont.add(dateL,0);
		
		format = new JLabel("(MM/DD/YYYY)");
		format.setBounds(230, 310, 100, 25);
		cont.add(format,0);
		
		//Month
		String[] mData={"","01", "02","03","04","05","06","07","08","09","10","11","12"};
		bMonth = new JComboBox(mData);
		bMonth.setBounds(350, 300, 75, 25);
		bMonth.setSelectedIndex(0);
		bMonth.addActionListener(
            new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox jcmbType = (JComboBox) e.getSource();
                String cmbType = (String) jcmbType.getSelectedItem();
                jcmbType.setSelectedItem(cmbType);
            }
        });
		cont.add(bMonth,0);
		
		//Date
		String[] dData={"","01", "02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
		bDay = new JComboBox(dData);
		bDay.setBounds(440, 300, 75, 25);
		bDay.setSelectedIndex(0);
		bDay.addActionListener(
            new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox jcmbType = (JComboBox) e.getSource();
                String cmbType = (String) jcmbType.getSelectedItem();
                jcmbType.setSelectedItem(cmbType);
            }
        });
		cont.add(bDay,0);

		//Year
		bYear = new JTextField();
		bYear.setBounds(530, 300,75,25);
		bYear.setEditable(true);
		cont.add(bYear,0);
		
		errorMessage = new JLabel("");
		errorMessage.setBounds(350, 450, 350, 25);
		cont.add(errorMessage,0);

		//Display the "Enter" button on window
		enter = new JButton("Enter");
		enter.setBounds(470, 500,75,25);
		enter.addActionListener(new PictureEnterButtonHandler());
		add(enter,0);
		
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
		
		add(cont,0);
		setVisible(true);
		pack();
		repaint();
	}
	

	
	public class PicturePathHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {

		}
	}
	
	public class PictureEnterButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if (filePath == null || filePath.compareTo("")==0 || !filePath.toLowerCase().endsWith(".jpg"))
				errorMessage.setText("Supply A File");
			else if (title.getText().compareTo("")==0)
				errorMessage.setText("Supply A Title");
			else if (bMonth.getSelectedIndex()==0 || bDay.getSelectedIndex() == 0 || !containsOnlyNumbers(bYear.getText()) || bYear.getText().length() != 4)
				errorMessage.setText("Supply A Valid Date");
			else
			{
				String enteredDate = bMonth.getSelectedItem().toString() + "/" + bDay.getSelectedItem().toString() + "/" + bYear.getText();
				boolean successful=false;
				try {
					successful = driver.getConnector().addPhoto(driver.getAlbumID(), driver.getMemberID(), filePath, title.getText(), enteredDate);
				} catch (SQLException e1) {
				}
				if (successful)
				{
					errorMessage.setText("File Added");
					ViewPicture vm = new ViewPicture(driver);
					setVisible(false);
					repaint();
				}
				else
					errorMessage.setText("File Already Exists");							
			}
				
		}
	}
	
	

	public void drop(DropTargetDropEvent dtde) {
		Transferable tr = dtde.getTransferable();
	    DataFlavor[] flavors = tr.getTransferDataFlavors();
	    
	    for (int i = 0; i < flavors.length; i++) {
	    	
	    	if (flavors[i].isFlavorJavaFileListType()) {

	    		dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
	    	    java.util.List list=null;
				try {
					list = (java.util.List)tr.getTransferData(flavors[i]);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		for (int j = 0; j < list.size(); j++) {
	    			System.out.println(list.size());
	    			if (list.get(j).toString().toLowerCase().endsWith(".jpg"))
	    			{
	    				pathDrop.setText("File Accepted");
	    			    filePath = list.get(j).toString();
	    			    System.out.println(filePath);
	    			}
	    			else 
	    			{
	    				pathDrop.setText("FILE MUST BE JPG");
	    				filePath = "";
	    			}
	    		}

	    		// If we made it this far, everything worked.
	    		dtde.dropComplete(true);
	    	} 
	    }
	    
	    if (pathDrop.getText().compareTo("")==0)
	    {
	    	pathDrop.setText("FILE NOT ACCEPTED");
	    	filePath = "";	
	    }
	}
	
	
	private boolean containsOnlyNumbers(String str) {
        if (str == null || str.length() == 0)
            return false;
        
        for (int i = 0; i < str.length(); i++) {

            if (!Character.isDigit(str.charAt(i)))
                return false;
        }
        
        return true;
    }
	public class QuitButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	
	public class BackButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			ViewPicture vm = new ViewPicture(driver);
			setVisible(false);
			repaint();
		}
	}



	public void dragEnter(DropTargetDragEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	public void dragExit(DropTargetEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	public void dragOver(DropTargetDragEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	public void dropActionChanged(DropTargetDragEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}