import javax.swing.*;

import java.awt.event.*;
import java.awt.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;


import java.awt.event.*;
import java.awt.*;
import java.sql.SQLException;

//implement delete button listener
//waiting for testing
public class SearchPage extends JFrame {
	private JLabel instr,first,last,bDate,message;
	private JTextField fName,lName,year;
	private JComboBox month,day;
	private JButton enter,back,quit,view,delete;
	private JScrollPane pane;
	private JTable table;
	private Driver driver;
	private String[][] info;
	private JFrame pop;
	private String birthday;
	
	//change parameters, not pass info here.
	public SearchPage(Driver d){
		//Initialize the window
		super("FamBook-Search Member");
		setLocation(300,100);		
		Container cont=new Container();
		cont.setPreferredSize(new Dimension(800,600));
		
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
		enter.addActionListener(new EnterButtonHandler());
		add(enter,0);
		
		//Display the "back" button on window
		back = new JButton("Back");
		back.setBounds(620, 530,75,25);
		back.addActionListener(new BackButtonHandler());
		add(back,0);
		
		//Display the "Quit" button on window
		quit = new JButton("Quit");
		quit.setBounds(700, 530,75,25);
		quit.addActionListener(new QuitButtonHandler());
		add(quit,0);
		
		
		add(cont,0);
		setVisible(true);
		pack();
		repaint();
		driver=d;
	}
	private void updateTable(String[][] info){
		DefaultTableModel model = new DefaultTableModel()
		{
			public boolean isCellEditable(int row, int col){
				return false;
			}
		};
		table = new JTable(model);
		//if(pane!=null) remove(pane);
		model.addColumn("First Name");
		model.addColumn("Last Name");
		model.addColumn("Birth Date");
		model.addColumn("Death Date");
		model.addColumn("Description");
		for(int i=0;i<info.length;i++){
			model.addRow(info[i]);
		}
		pane = new JScrollPane(table);
		pane.setBounds(50, 230, 500, 200);
		add(pane);
		makeButtons();
	}
	private void makeButtons(){
		//Display the "view" button on window
		//if(view !=null)	remove(view);
			view = new JButton("View Selected Member");
			view.setBounds(600, 270,175,25);
			view.addActionListener(new ViewButtonHandler());
			add(view,0);
		
		//Display the "delete" button on window
		//if(delete==null) remove(delete);
			delete = new JButton("Delete Selected Member");
			delete.setBounds(600, 350,175,25);
			delete.addActionListener(new DeleteButtonHandler());
			add(delete,0);
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
			if(pane!=null) remove(pane);
			if(view!=null) remove(view);
			if(delete!=null) remove(delete);
			if(message!=null) remove (message);
			message=new JLabel("",JLabel.CENTER);
			message.setBounds(280,520,250,25);
			
			birthday = "";
			info = null;
			boolean birthDateValid=true;
			
			
			if (month.getSelectedIndex()!=0 || day.getSelectedIndex() != 0 || year.getText().length() > 0)
			{
				if (month.getSelectedIndex()==0 || day.getSelectedIndex() == 0 || year.getText().length() != 4 || !containsOnlyNumbers(year.getText()))
				{
					message.setText("Enter Valid BirthDate");
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
				message.setText("No Result Matching Input Information");		
			}else{
				if (birthDateValid)
					updateTable(info);				
			}
			
			add(message,0);
			repaint();
		}
	}
	public class ViewButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			int index=table.getSelectedRow();
			if(index==-1){
				if(message!=null) remove (message);
				message=new JLabel("",JLabel.CENTER);
				message.setBounds(580,400,200,25);
				message.setText("Please Select a Member");
				add(message,0);
			}else{
				String id = info[index][5];
				driver.setMemberID(id);
				ViewMember vm = new ViewMember(driver);
				setVisible(false);
			}
			repaint();
		}
	}
	public class DeleteButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			int index=table.getSelectedRow();
			if(index==-1){
				if(message!=null) remove (message);
				message=new JLabel("",JLabel.CENTER);
				message.setBounds(580,400,200,25);
				message.setText("Please Select a Member");
				add(message,0);
			}else{
				addPop();
			}
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
	
	private JFrame addPop(){
		pop = new JFrame();
		pop.setLayout(new BorderLayout());
		JLabel confirm = new JLabel("Delete Selected Member?",JLabel.CENTER);
		confirm.setPreferredSize(new Dimension(200,50));
		pop.add(confirm,BorderLayout.NORTH);
		JButton ok = new JButton("Ok");
		ok.addActionListener(new OkButtonHandler());
		ok.setPreferredSize(new Dimension(75,25));
		pop.add(ok,BorderLayout.WEST);
		JButton no = new JButton("Cancel");
		no.addActionListener(new CancelButtonHandler());
		no.setPreferredSize(new Dimension(75,25));
		pop.add(no,BorderLayout.EAST);
		pop.pack();
		pop.setLocation(400, 300);
		pop.setVisible(true);
		pop.repaint();
		return pop;
	}
	public class CancelButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			pop.setVisible(false);
			pop.repaint();
		}
	}
	public class OkButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(pane!=null) remove(pane);
			if(view!=null) remove(view);
			if(delete!=null) remove(delete);
			if(message!=null) remove (message);
			message=new JLabel("",JLabel.CENTER);
			message.setBounds(280,520,250,25);
			int index=table.getSelectedRow();
			try {
				//System.out.println(info);
				int status=driver.getConnector().deleteMember(driver.getAlbumID(), info[index][5]);
				//System.out.println(status);
				info = driver.getConnector().searchMembers(driver.getAlbumID(), fName.getText(), lName.getText(), birthday);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(info==null){
				message.setText("No Result Matching Input Information");
				add(message,0);
			}else{
				updateTable(info);
			}
			//System.out.println("table updated");
			pop.setVisible(false);
			pop.repaint();
			repaint();
		}
	}

	
//	public static void main(String[] args) throws Exception {
//		Driver myDriver = new Driver();
//		myDriver.setAlbumID("1");
//		myDriver.setAlbumName("thisName");
//		new SearchPage(myDriver);
//	}
}