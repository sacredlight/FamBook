import javax.swing.*;
import javax.swing.table.DefaultTableModel;


import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class ViewDocument extends JFrame{
	private JButton add,back,quit,delete,view;
	private JScrollPane pane;
	private Driver driver;
	private String[][] info;
	private JLabel message;
	private JTable table;
	private JFrame pop;
	
	public ViewDocument(Driver d){
		//Initialize the window
		super("FamBook-View Documents");
		setBounds(300,100,800,600);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		setVisible(true);
		
		driver =d;
		info=null;
		try {
			info = driver.getConnector().getDocuments(driver.getAlbumID(), driver.getMemberID());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//Build the scroll pane contains jtable
		updateTable(info);
		
		//Display the "add" button on window
		add = new JButton("Add New Document");
		add.setBounds(550, 150,200,25);
		add.addActionListener(new AddButtonHandler());
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

		repaint();
	}
	
	private void updateTable(String[][] info){
		if(pane!=null) remove(pane);
		if(view!=null) remove(view);
		if(delete!=null) remove(delete);
		if(info==null){
			JLabel message=new JLabel("This Member Has No Document Available.");
			message.setBounds(190,200,270,25);
			add(message,0);
		}else{
		DefaultTableModel model = new DefaultTableModel(){
			public boolean isCellEditable(int row, int col){
				return false;
			}
		};
		table = new JTable(model);
		model.addColumn("Title");
		model.addColumn("Date");
		for(int i=0;i<info.length;i++){
			String tmp[]=new String[2];
			tmp[0]=info[i][1];
			tmp[1]=info[i][2];
			model.addRow(tmp);
		}
		pane = new JScrollPane(table);
		pane.setBounds(50, 125, 450, 300);
		add(pane);
		//Display the "view" button on window
		view = new JButton("View Selected File");
		view.setBounds(550, 200,200,25);
		view.addActionListener(new ViewButtonHandler());
		add(view,0);
		
		//Display the "delete" button on window
		delete = new JButton("Delete Selected File");
		delete.setBounds(550, 250,200,25);
		delete.addActionListener(new DeleteButtonHandler());
		add(delete,0);
		}
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
	public class AddButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			AddDocument ap = new AddDocument(driver);
			setVisible(false);
			repaint();
		}
	}
	public class ViewButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			int index=table.getSelectedRow();
			if(index==-1){
				if(message!=null) remove (message);
				message=new JLabel("",JLabel.CENTER);
				message.setBounds(550,400,200,25);
				message.setText("Please Select a File");
				add(message,0);
			}else{
				String path = info[index][0];
				File file =new File(path);
				if(!file.exists()){
					if(message!=null) remove (message);
					message=new JLabel("",JLabel.CENTER);
					message.setBounds(550,400,200,25);
					message.setText("The File Does not Exist");
					add(message,0);
				}else{
				try {
					Process myProcess = new ProcessBuilder("open",path).start();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				}
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
				message.setBounds(550,400,200,25);
				message.setText("Please Select a File");
				add(message,0);
			}else{
				addPop();
			}
			repaint();
		}
	}
	private JFrame addPop(){
		pop = new JFrame();
		pop.setLayout(new BorderLayout());
		JLabel confirm = new JLabel("Delete Selected File?",JLabel.CENTER);
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
			int index=table.getSelectedRow();
			try {
				//System.out.println(info);
				int status=driver.getConnector().deleteDocument(driver.getAlbumID(), driver.getMemberID(), info[index][0]);
				//System.out.println(status);
				info = driver.getConnector().getDocuments(driver.getAlbumID(), driver.getMemberID());
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//System.out.println(info);
			updateTable(info);
			//System.out.println("table updated");
			pop.setVisible(false);
			pop.repaint();
			repaint();
		}
	}

}
