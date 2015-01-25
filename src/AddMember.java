import javax.swing.*;


import java.awt.event.*;
import java.awt.*;

public class AddMember extends JFrame{
	private JLabel format,first,last,bDate,dDate,desc, desc2,errorMessage;
	private JTextField fName,lName,bYear,dYear;
	private JTextArea description;
	private JComboBox bMonth,bDay,dMonth,dDay;
	private JButton enter,back,quit;
	private Driver driver;
	
	public AddMember(Driver d){
		//Initialize the window
		super("FamBook-Add Member");
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(300,100);		
		Container cont=new Container();
		cont.setPreferredSize(new Dimension(800,600));
		
		//Display first name input text field and matching label
		first = new JLabel("First Name");
		first.setBounds(250, 50, 70, 25);
		cont.add(first,0);
		
		fName = new JTextField();
		fName.setBounds(350, 50,150,25);
		fName.setEditable(true);
		cont.add(fName,0);
		
		//Display last name input text field and matching label
		last = new JLabel("Last Name");
		last.setBounds(250, 100, 70, 25);
		cont.add(last,0);
		
		lName = new JTextField();
		lName.setBounds(350, 100,150,25);
		lName.setEditable(true);
		cont.add(lName,0);
		
		//Display birth date and death date input field and matching label
		bDate = new JLabel("Birth Date");
		bDate.setBounds(250, 140, 75, 25);
		cont.add(bDate,0);
		
		format = new JLabel("(MM/DD/YYYY)");
		format.setBounds(230, 160, 100, 25);
		cont.add(format,0);
		
		dDate = new JLabel("Death Date (Optional)");
		dDate.setBounds(195, 190, 150, 25);
		cont.add(dDate,0);
		
		format = new JLabel("(MM/DD/YYYY)");
		format.setBounds(230, 210, 100, 25);
		cont.add(format,0);
				
		//Month
		String[] mData={"","01", "02","03","04","05","06","07","08","09","10","11","12"};
		bMonth = new JComboBox(mData);
		bMonth.setBounds(350, 150, 75, 25);
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
		
		dMonth = new JComboBox(mData);
		dMonth.setBounds(350, 200, 75, 25);
		dMonth.setSelectedIndex(0);
		dMonth.addActionListener(
            new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox jcmbType = (JComboBox) e.getSource();
                String cmbType = (String) jcmbType.getSelectedItem();
                jcmbType.setSelectedItem(cmbType);
            }
        });
		cont.add(dMonth,0);
		
		//Date
		String[] dData={"","01", "02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
		bDay = new JComboBox(dData);
		bDay.setBounds(440, 150, 75, 25);
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
		
		dDay = new JComboBox(dData);
		dDay.setBounds(440, 200, 75, 25);
		dDay.setSelectedIndex(0);
		dDay.addActionListener(
            new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox jcmbType = (JComboBox) e.getSource();
                String cmbType = (String) jcmbType.getSelectedItem();
                jcmbType.setSelectedItem(cmbType);
            }
        });
		cont.add(dDay,0);
		
		//Year
		bYear = new JTextField();
		bYear.setBounds(530, 150,75,25);
		bYear.setEditable(true);
		cont.add(bYear,0);
		
		dYear = new JTextField();
		dYear.setBounds(530, 200,75,25);
		dYear.setEditable(true);
		cont.add(dYear,0);
		
		//Display description field and matching label
		desc = new JLabel("Description");
		desc.setBounds(250, 250, 75, 25);
		desc2 = new JLabel("(Max of 256 Characters)");
		desc2.setBounds(190, 275, 150, 25);
		cont.add(desc,0);
		cont.add(desc2,0);
		
		description = new JTextArea(16,16);
		description.setLocation(350,250);
		description.setSize(16*16,16*10);
		description.setEditable(true);
		description.setLineWrap(true);
		description.setWrapStyleWord(true);
		cont.add(description,0);
		
		errorMessage = new JLabel("");
		errorMessage.setBounds(350, 450, 350, 25);
		cont.add(errorMessage,0);
		
		//Display the "Enter" button on window
		enter = new JButton("Enter");
		enter.setBounds(470, 500,75,25);
		enter.addActionListener(new EnterButtonHandler());
		add(enter,0);
		
		//Display the "Back" button on window
		back = new JButton("Back");
		back.setBounds(570, 500,75,25);
		back.addActionListener(new BackButtonHandler());
		add(back,0);
		
		//Display the "Quit" button on window
		quit = new JButton("Quit");
		quit.addActionListener(new QuitButtonHandler());
		quit.setBounds(670, 500,75,25);
		add(quit,0);
		
		add(cont,0);
		setVisible(true);
		pack();
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
			AlbumMenu am = new AlbumMenu(driver);
			setVisible(false);
			repaint();
		}
	}
	
	public class EnterButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			
			
			if (fName.getText().compareTo("")==0)
				errorMessage.setText("Enter a First Name");
			else if (lName.getText().compareTo("")==0)
				errorMessage.setText("Enter a Last Name");
			else if (bMonth.getSelectedIndex()==0)
				errorMessage.setText("Select Birth Month");
			else if (bDay.getSelectedIndex() == 0)
				errorMessage.setText("Select Birth Day");
			else if (bYear.getText().length() != 4 || !containsOnlyNumbers(bYear.getText()))
				errorMessage.setText("Enter Valid Birth Year");
			else if (description.getText().length() > 256)
				errorMessage.setText("Description Too Many Characters");
			else if (dMonth.getSelectedIndex()!=0 || dDay.getSelectedIndex() != 0 || dYear.getText().length() > 0)
			{
				if (dMonth.getSelectedIndex()==0 || dDay.getSelectedIndex() == 0 || dYear.getText().length() != 4 || !containsOnlyNumbers(dYear.getText()))
					errorMessage.setText("Enter Valid Death Date");
				else
					enterMember();
			}
			else {
				enterMember();
			}
			repaint();
		}
	}
	
	
	//Actually enters the member into the Database, after some final error checking
	private void enterMember()
	{
		String enteredFirstName, enteredLastName, enteredBirthDate, enteredDeathDate, enteredDescription;		
		
		enteredFirstName = fName.getText();
		enteredLastName = lName.getText();
		enteredBirthDate = bMonth.getSelectedItem().toString() + "/" + bDay.getSelectedItem().toString() + "/" + bYear.getText();
		if (dMonth.getSelectedIndex()!=0)//means death date is not empty
			enteredDeathDate =  dMonth.getSelectedItem().toString() + "/" + dDay.getSelectedItem().toString() + "/" + dYear.getText();
		else
			enteredDeathDate = "";
		enteredDescription = description.getText();

		try {
			String memberID = driver.getConnector().addMember(driver.getAlbumID(), enteredFirstName, enteredLastName, enteredBirthDate, enteredDeathDate, enteredDescription);
			//System.out.println(driver.getAlbumID());
			if(memberID.compareTo("") != 0)
			{
				errorMessage.setText("");//No errors to report, member added successfully
				driver.setMemberID(memberID);
				ViewMember vm=new ViewMember(driver);
				setVisible(false);
				repaint();
			}
			else
				errorMessage.setText("Member Already Exists");
		} catch (Exception e) {
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

}
