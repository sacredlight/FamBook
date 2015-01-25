import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class ViewRelationship extends JFrame{
	private JLabel parents,children,spouse,message;
	private JButton quit,back,addp,addc,adds,viewp,deletep,viewc,deletec,views,deletes;
	private Driver driver;
	private JTable parentTable,childTable,spouseTable;
	private String[] p,c;
	private String s;
	private JFrame pop;
	private JScrollPane ppane,cpane,spane;
	
	public ViewRelationship(Driver d){
		//Initialize the window
		super("FamBook-View Relationship");
		setBounds(300,100,800,600);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		setVisible(true);
		
		driver=d;
		p=null;
		c=null;
		s=null;
		try {
			p=driver.getConnector().getParents(driver.getAlbumID(), driver.getMemberID());
			c=driver.getConnector().getChildren(driver.getAlbumID(), driver.getMemberID());
			s=driver.getConnector().getSpouse(driver.getAlbumID(), driver.getMemberID());
			//System.out.println(c[0]+",");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//Display parents
		parents = new JLabel("Parents");
		parents.setBounds(50, 50, 75, 25);
		add(parents,0);
		
		if(p==null){
			JLabel message=new JLabel("This Member Has No Parent Available.",JLabel.CENTER);
			message.setBounds(175,100,500,25);
			add(message,0);
		}else{
			updateParentsTable(p);
		}
		
		//Display children
		children = new JLabel("Children");
		children.setBounds(50, 200, 75, 25);
		add(children,0);
		
		if(c==null){
			JLabel message=new JLabel("This Member Has No Child Available.",JLabel.CENTER);
			message.setBounds(175,250,500,25);
			add(message,0);
		}else{
			updateChildrenTable(c);
		}
		
		spouse = new JLabel("Spouse");
		spouse.setBounds(50, 350, 75, 25);
		add(spouse,0);
		
		if(s==null){
			JLabel message=new JLabel("This Member Has No Spouse Available.",JLabel.CENTER);
			message.setBounds(175,400,500,25);
			add(message,0);
		}else{
			updateSpouseTable(s);
		}
		
		//Display the "add parent" button on window
		addp = new JButton("Add");
		addp.setBounds(50, 85,50,25);
		addp.addActionListener(new AddParentsButtonHandler());
		add(addp,0);
		
		//Display the "add child button on window
		addc = new JButton("Add");
		addc.setBounds(50, 235,50,25);
		addc.addActionListener(new AddChildrenButtonHandler());
		add(addc,0);
		
		//Display the "add sponsor button on window
		adds = new JButton("Add");
		adds.setBounds(50, 385,50,25);
		adds.addActionListener(new AddSpouseButtonHandler());
		add(adds,0);
		
		//Display the "Back" button on window
		back = new JButton("Back");
		back.setBounds(600, 530,75,25);
		back.addActionListener(new BackButtonHandler());
		add(back,0);
		
		//Display the "Quit" button on window
		quit = new JButton("Quit");
		quit.setBounds(700, 530,75,25);
		quit.addActionListener(new QuitButtonHandler());
		add(quit,0);
		
		repaint();		
	}
	private void updateParentsTable(String[] info){
		if(ppane!=null) remove(ppane);
		if(viewp!=null) remove(viewp);
		if(deletep!=null) remove(deletep);
		DefaultTableModel model = new DefaultTableModel()
		{
			public boolean isCellEditable(int row, int col){
				return false;
			}
		};
		parentTable = new JTable(model);
		model.addColumn("First Name");
		model.addColumn("Last Name");
		model.addColumn("Birth Date");
		model.addColumn("Death Date");
		model.addColumn("Description");
		for(int i=0;i<info.length;i++){
			try {
				//System.out.println(info[i] +",");
				if(info[i]!=null){
				String[] tmp=driver.getConnector().getMemberInformationById(driver.getAlbumID(), info[i]);
				String[] swap = new String[tmp.length-1];
				for(int j=0;j<swap.length;j++){
					swap[j] = tmp[j+1];
				}
				model.addRow(swap);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		ppane = new JScrollPane(parentTable);
		ppane.setBounds(175, 55, 500, 60);
		add(ppane);
		makeParentsButtons();
		repaint();
	}
	private void updateChildrenTable(String[] info){
		if(cpane!=null) remove(cpane);
		if(viewc!=null) remove(viewc);
		if(deletec!=null) remove(deletec);
		DefaultTableModel model = new DefaultTableModel()
		{
			public boolean isCellEditable(int row, int col){
				return false;
			}
		};
		childTable = new JTable(model);
		model.addColumn("First Name");
		model.addColumn("Last Name");
		model.addColumn("Birth Date");
		model.addColumn("Death Date");
		model.addColumn("Description");
		for(int i=0;i<info.length;i++){
			try {
				if(info[i]!=null){
				String[] tmp=driver.getConnector().getMemberInformationById(driver.getAlbumID(), info[i]);
					String[] swap = new String[tmp.length-1];
					for(int j=0;j<swap.length;j++){
						swap[j] = tmp[j+1];
					}
					model.addRow(swap);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		cpane = new JScrollPane(childTable);
		cpane.setBounds(175, 205, 500, 60);
		add(cpane);
		makeChildrenButtons();
		repaint();
	}
	private void updateSpouseTable(String info){
		if(spane!=null) remove(spane);
		if(views!=null) remove(views);
		if(deletes!=null) remove(deletes);
		DefaultTableModel model = new DefaultTableModel()
		{
			public boolean isCellEditable(int row, int col){
				return false;
			}
		};
		spouseTable = new JTable(model);
		model.addColumn("First Name");
		model.addColumn("Last Name");
		model.addColumn("Birth Date");
		model.addColumn("Death Date");
		model.addColumn("Description");
			try {
				if(info!=null){
				String[] tmp=driver.getConnector().getMemberInformationById(driver.getAlbumID(), info);
					String[] swap = new String[tmp.length-1];
					for(int j=0;j<swap.length;j++){
						swap[j] = tmp[j+1];
					}
					model.addRow(swap);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		spane = new JScrollPane(spouseTable);
		spane.setBounds(175, 355, 500, 60);
		add(spane);
		makeSpouseButtons();
		repaint();
	}
	private void makeParentsButtons(){
		//Display the "view" button on window
			viewp = new JButton("View");
			viewp.setBounds(300, 130,75,25);
			viewp.addActionListener(new ViewParentsButtonHandler());
			add(viewp,0);
		
		//Display the "delete" button on window
		//if(delete==null) remove(delete);
			deletep = new JButton("Delete");
			deletep.setBounds(450, 130,75,25);
			deletep.addActionListener(new DeleteParentsButtonHandler());
			add(deletep,0);
	}

	private void makeChildrenButtons(){
		//Display the "view" button on window
		//if(view !=null)	remove(view);
			viewc = new JButton("View");
			viewc.setBounds(300, 280,75,25);
			viewc.addActionListener(new ViewChildrenButtonHandler());
			add(viewc,0);
		
		//Display the "delete" button on window
		//if(delete==null) remove(delete);
			deletec = new JButton("Delete");
			deletec.setBounds(450, 280,75,25);
			deletec.addActionListener(new DeleteChildrenButtonHandler());
			add(deletec,0);
	}
	
	private void makeSpouseButtons(){
		//Display the "view" button on window
		//if(view !=null)	remove(view);
			views = new JButton("View");
			views.setBounds(300, 450,75,25);
			views.addActionListener(new ViewSpouseButtonHandler());
			add(views,0);
		
		//Display the "delete" button on window
		//if(delete==null) remove(delete);
			deletes = new JButton("Delete");
			deletes.setBounds(450, 450,75,25);
			deletes.addActionListener(new DeleteSpouseButtonHandler());
			add(deletes,0);
	}
	
	public class QuitButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	
	public class BackButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			ViewMember vm = new ViewMember(driver);
			setVisible(false);
			repaint();
		}
	}
	
	public class AddParentsButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			AddParent ap = new AddParent(driver);
			setVisible(false);
			repaint();
		}
	}
	
	public class AddChildrenButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			AddChildren ac = new AddChildren(driver);
			setVisible(false);
			repaint();
		}
	}
	
	public class AddSpouseButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			AddSpouse ac = new AddSpouse(driver);
			setVisible(false);
			repaint();
		}
	}
	
	public class ViewParentsButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			int index=parentTable.getSelectedRow();
			if(index==-1){
				if(message!=null) remove (message);
				message=new JLabel("",JLabel.CENTER);
				message.setBounds(175,10,500,25);
				message.setForeground(Color.red);
				message.setText("Please Select a Member");
				add(message,0);
			}else{
				String id = p[index];
				driver.setMemberID(id);
				ViewMember vm = new ViewMember(driver);
				setVisible(false);
			}
			repaint();
		}
	}
	public class ViewChildrenButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			int index=childTable.getSelectedRow();
			if(index==-1){
				if(message!=null) remove (message);
				message=new JLabel("",JLabel.CENTER);
				message.setBounds(175,10,500,25);
				message.setForeground(Color.red);
				message.setText("Please Select a Member");
				add(message,0);
			}else{
				String id = c[index];
				driver.setMemberID(id);
				ViewMember vm = new ViewMember(driver);
				setVisible(false);
			}
			repaint();
		}
	}
	
	public class ViewSpouseButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			int index=spouseTable.getSelectedRow();
			if(index==-1){
				if(message!=null) remove (message);
				message=new JLabel("",JLabel.CENTER);
				message.setBounds(175,10,500,25);
				message.setForeground(Color.red);
				message.setText("Please Select a Member");
				add(message,0);
			}else{
				String id = s;
				driver.setMemberID(id);
				ViewMember vm = new ViewMember(driver);
				setVisible(false);
			}
			repaint();
		}
	}
	
	public class DeleteParentsButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			int index=parentTable.getSelectedRow();
			if(index==-1){
				if(message!=null) remove (message);
				message=new JLabel("",JLabel.CENTER);
				message.setBounds(175,10,200,25);
				message.setText("Please Select a Member");
				add(message,0);
			}else{
				addPop(1);
			}
			repaint();
		}
	}
	public class DeleteChildrenButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			int index=childTable.getSelectedRow();
			if(index==-1){
				if(message!=null) remove (message);
				message=new JLabel("",JLabel.CENTER);
				message.setBounds(175,10,200,25);
				message.setText("Please Select a Member");
				add(message,0);
			}else{
				addPop(2);
			}
			repaint();
		}
	}
	public class DeleteSpouseButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			int index=spouseTable.getSelectedRow();
			if(index==-1){
				if(message!=null) remove (message);
				message=new JLabel("",JLabel.CENTER);
				message.setBounds(175,10,200,25);
				message.setText("Please Select a Member");
				add(message,0);
			}else{
				addPop(3);
			}
			repaint();
		}
	}
	private JFrame addPop(int i){
		pop = new JFrame();
		pop.setLayout(new BorderLayout());
		JLabel confirm = new JLabel("Delete Selected Member?",JLabel.CENTER);
		confirm.setPreferredSize(new Dimension(200,50));
		pop.add(confirm,BorderLayout.NORTH);
		JButton ok = new JButton("Ok");
		if(i==1)
			ok.addActionListener(new pOkButtonHandler());
		else if(i==2)
			ok.addActionListener(new cOkButtonHandler());
		else if(i==3)
			ok.addActionListener(new sOkButtonHandler());
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
	public class pOkButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			int index=parentTable.getSelectedRow();
			try {
				int status=driver.getConnector().deleteParentChildRelationship(driver.getAlbumID(), p[index],driver.getMemberID());
				System.out.println(status);
				System.out.println(p[index]);
				p=driver.getConnector().getParents(driver.getAlbumID(), driver.getMemberID());
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(p==null){
				if(ppane!=null) remove(ppane);
				if(viewp!=null) remove(viewp);
				if(deletep!=null) remove(deletep);
				JLabel message=new JLabel("This Member Has No Parent Available.",JLabel.CENTER);
				message.setBounds(175,100,500,25);
				add(message,0);
			}else{
				updateParentsTable(p);
			}
			pop.setVisible(false);
			pop.repaint();
			repaint();
		}
	}
	public class cOkButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			int index=childTable.getSelectedRow();
			try {
				int status=driver.getConnector().deleteParentChildRelationship(driver.getAlbumID(),driver.getMemberID(), c[index]);
				System.out.println(status);
				System.out.println(c[index]);
				c=driver.getConnector().getChildren(driver.getAlbumID(), driver.getMemberID());
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(c==null){
				if(cpane!=null) remove(cpane);
				if(viewc!=null) remove(viewc);
				if(deletec!=null) remove(deletec);
				JLabel message=new JLabel("This Member Has No Child Available.",JLabel.CENTER);
				message.setBounds(175,250,500,25);
				add(message,0);
			}else{
				updateChildrenTable(c);
			}
			pop.setVisible(false);
			pop.repaint();
			repaint();
		}
	}
	public class sOkButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			try {
				int status=driver.getConnector().deleteSpouseRelationship(driver.getAlbumID(),s,driver.getMemberID());
				System.out.println(status);
				System.out.println(driver.getAlbumID());
				System.out.println(driver.getMemberID());
				System.out.println(s);
				s=driver.getConnector().getSpouse(driver.getAlbumID(), driver.getMemberID());
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(s==null){
				if(spane!=null) remove(spane);
				if(views!=null) remove(views);
				if(deletes!=null) remove(deletes);
				JLabel message=new JLabel("This Member Has No Spouse Available.",JLabel.CENTER);
				message.setBounds(175,400,500,25);
				add(message,0);
			}else{
				updateSpouseTable(s);
			}
			pop.setVisible(false);
			pop.repaint();
			repaint();
		}
	}
}

