import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;



public class AddSpouse extends JFrame{
	private JLabel instr,first,last,bDate, member, memberInformation, message;
	private JTextField fName,lName,year;
	private JComboBox month,day;
	private JButton enter,back,quit,add;
	private JScrollPane pane;
	private String[][] info;
	private Driver driver;
	private JTable table;
	private String[] spouseMemberInfo;
	
	public AddSpouse(Driver theDriver){
		//Initialize the window
		super("FamBook-Add Spouse");
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setLocation(300,100);		
		Container cont=new Container();
		cont.setPreferredSize(new Dimension(800,600));
		
		driver = theDriver;
		try {
			spouseMemberInfo = driver.getConnector().getMemberInformationById(driver.getAlbumID(), driver.getMemberID());
		} catch (SQLException e1) {
		}
		
		member = new JLabel("Spouse Member Info:");
		member.setBounds(60, 10, 150, 50);
		cont.add(member);
		
		memberInformation = new JLabel(spouseMemberInfo[1] + " " + spouseMemberInfo[2] + ", " + spouseMemberInfo[3]);
		memberInformation.setBounds(220, 10, 300, 50);
		cont.add(memberInformation,0);
		
		//Display instruction on search page
		instr = new JLabel("Please Type in Following Information:");
		instr.setBounds(60, 50, 300, 50);
		cont.add(instr,0);
		
		//Display first name input text field and matching label
		first = new JLabel("First Name");
		first.setBounds(90, 110, 100, 25);
		cont.add(first,0);
		
		fName = new JTextField();
		fName.setBounds(60, 145,150,25);
		fName.setEditable(true);
		cont.add(fName,0);
		
		//Display last name input text field and matching label
		last = new JLabel("Last Name");
		last.setBounds(265, 110, 100, 25);
		cont.add(last,0);
		
		lName = new JTextField();
		lName.setBounds(235, 145,150,25);
		lName.setEditable(true);
		cont.add(lName,0);
		
		//Display birth date input field and matching label
		bDate = new JLabel("Birth Date (MM/DD/YYYY)");
		bDate.setBounds(450, 110, 200, 25);
		cont.add(bDate,0);
		
		//Month
		String[] mData={"","01", "02","03","04","05","06","07","08","09","10","11","12"};
		month = new JComboBox(mData);
		month.setBounds(420, 145, 75, 25);
		month.setSelectedIndex(0);
        month.addActionListener(
								new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox jcmbType = (JComboBox) e.getSource();
                String cmbType = (String) jcmbType.getSelectedItem();
                jcmbType.setSelectedItem(cmbType);
            }
        });
		cont.add(month,0);
		
		//Date
		String[] dData={"","01", "02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
		day = new JComboBox(dData);
		day.setBounds(500, 145, 75, 25);
		day.setSelectedIndex(0);
        day.addActionListener(
							  new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox jcmbType = (JComboBox) e.getSource();
                String cmbType = (String) jcmbType.getSelectedItem();
                jcmbType.setSelectedItem(cmbType);
            }
        });
		cont.add(day,0);
		
		//Year
		year = new JTextField();
		year.setBounds(580, 145,75,25);
		year.setEditable(true);
		cont.add(year,0);
		
		//Display the "Enter" button on window
		enter = new JButton("Enter");
		enter.setBounds(700, 145,75,25);
		enter.addActionListener(new SpouseEnterButtonHandler());
		add(enter,0);
		
		//Display the "add" button on window
		add = new JButton("Add Selected as Spouse");
		add.setBounds(320, 460,175,25);
		add.addActionListener(new SpouseAddButtonHandler());
		add(add,0);
		
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
	private void updateTable(String[][] info){
		DefaultTableModel model = new DefaultTableModel(){
			public boolean isCellEditable(int row, int col){
				return false;
			}
		};
		table = new JTable(model);
		if(pane!=null) remove(pane);
		model.addColumn("First Name");
		model.addColumn("Last Name Name");
		model.addColumn("Birth Date");
		model.addColumn("Death Date");
		model.addColumn("Description");
		for(int i=0;i<info.length;i++){
			model.addRow(info[i]);
		}
		pane = new JScrollPane(table);
		pane.setBounds(50, 230, 700, 200);
		add(pane);
	}
	
	public class SpouseEnterButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(message!=null) remove (message);
			String birthday = "";
			info = null;
			boolean birthDateValid=true;
			
			
			if (month.getSelectedIndex()!=0 || day.getSelectedIndex() != 0 || year.getText().length() > 0)
			{
				if (month.getSelectedIndex()==0 || day.getSelectedIndex() == 0 || year.getText().length() != 4 || !containsOnlyNumbers(year.getText()))
				{
					message=new JLabel("",JLabel.CENTER);
					message.setBounds(280,520,250,25);
					message.setText("Enter Valid BirthDate");
					add(message,0);
					birthDateValid = false;
				}
				else
				{
					birthday=month.getSelectedItem()+"/"+day.getSelectedItem()+"/"+year.getText();
					birthDateValid = true;
				}
			}
			
			
			try {
				info= driver.getConnector().searchMembers(driver.getAlbumID(), fName.getText(), lName.getText(), birthday);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			if(info==null){
				message=new JLabel("",JLabel.CENTER);
				message.setBounds(280,520,250,25);
				message.setText("No Result Matching Input Information");
				System.out.println("HERE");
				add(message,0);
				
			}else{
				if (birthDateValid)
					updateTable(info);				
			}
			repaint();
		}
	}
	
	public class SpouseAddButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			
			if(message!=null) remove (message);
			message=new JLabel("",JLabel.CENTER);
			message.setBounds(280,520,250,25);
			
			if (table == null)
				message.setText("Please Search First");
		    else
			{
				int index=table.getSelectedRow();
				
				if(index==-1){
					message.setText("Please Select a Member");
				}else
				{
					String spouseID = info[index][5];
					int errorCode=-1;
					try {
						errorCode = driver.getConnector().addSpouseRelationship(driver.getAlbumID(), spouseMemberInfo[0], spouseID);
					} catch (SQLException e1) {
					}	
					
					if (errorCode==-1)
						message.setText("Select Distinct Spouse");
					else if (errorCode==1)
						message.setText("Each Member Can Only Have 1 Spouse");
					else						
						message.setText("Relationship Added");
				}
			}
			
			add(message,0);
			repaint();
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
			ViewRelationship vr = new ViewRelationship(driver);
			setVisible(false);
			repaint();
		}
	}
	
}