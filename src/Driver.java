import java.sql.SQLException;

public class Driver {

	private String currentAlbumID,currentAlbumName;
	private String currentMemberID;
	private Connector connection;
	
	public Driver()
	{
		try {
			connection = new Connector();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Connector getConnector()
	{
		return connection;
	}
	
	public void setAlbumID(String albumID)
	{
		currentAlbumID=albumID;
	}
	
	public String getAlbumID()
	{
		return currentAlbumID;
	}
	public void setAlbumName(String albumName)
	{
		currentAlbumName=albumName;
	}
	
	public String getAlbumName()
	{
		return currentAlbumName;
	}
	public void setMemberID(String memberID)
	{
		currentMemberID=memberID;
	}
	
	public String getMemberID()
	{
		return currentMemberID;
	}
	
	
	
	public static void main(String[] args) {
		String[][] str = new String[2][3];
		str[0][0]="fds";
		str[0][1]="fds";
		str[0][2]="fds";
		str[1][0]="fds";
		str[1][1]="fds";
		str[1][2]="fds";
		Driver mainDriver = new Driver();
		StartPage start = new StartPage (mainDriver);
		//SearchPage search = new SearchPage(mainDriver, str);
		//AlbumMenu menu = new AlbumMenu();
		//AddMember addMem=new AddMember();
		//ViewMember view = new ViewMember();
		//CreateAlbum creAlb=new CreateAlbum();
		//ViewPicture vp=new ViewPicture(null);
		//AddPicture ap=new AddPicture();
		//ViewRelationship vr=new ViewRelationship(mainDriver,null,null);
		//ViewGraphic vg = new ViewGraphic(null);
		//ViewDocument vd=new ViewDocument(str);
		//AddParent ap=new AddParent(str);
		//AddChildren ac = new AddChildren(str);
		//AddGraphic ag = new AddGraphic();
		//AddDocument ad = new AddDocument();
	}

}