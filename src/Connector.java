import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import oracle.jdbc.OracleDriver;



public class Connector {
	private Connection connection;
	private ResultSet results;
    
    public Connector() throws SQLException {
    	DriverManager.registerDriver(new OracleDriver());
    	connection = DriverManager.getConnection("jdbc:oracle:thin:@syntax.cs.uwlax.edu:1521:cs", "cs364_50", "changeme");
	}
    
    //This method should be used to add an album name
    //returns true if album added successfully 
    //returns false if album name was already taken
    public String addAlbum(String albumName) throws SQLException 
    {
    	boolean albumAlreadyExists = checkAlbumName(albumName);
    	String albumID = "";
    	
    	if (!albumAlreadyExists)
    	{
    		Statement stmt = connection.createStatement();
    		albumID = UUID.randomUUID().toString();
    		String sql = "Insert into FamilyAlbum values ('" + albumID + "' , '" + albumName + "')";
    		stmt.executeUpdate(sql);
    		stmt.close();
    	}
    	
    	return albumID;
    }
    
    //This method should be used to add a member to an album
    //returns memberID if member added successfully
    //returns empty String if member already exists
    public String addMember(String albumID, String firstName, String lastName, String birthDate, String deathDate, String description) throws Exception
    {	
    	boolean memberAlreadyExists = checkMemberName(albumID, firstName, lastName, birthDate);
    	String memberUid = "";
    	
    	if (!memberAlreadyExists) 
    	{
    		Statement stmt = connection.createStatement();
    	    memberUid = UUID.randomUUID().toString();	
    		String sql = "Insert into FamilyMember values ('" + memberUid + "' , '" + albumID + "' , '" + firstName + "' , '" + lastName + "' , '" + birthDate + "' , '" + deathDate + "' , '" + description + "')";
    		stmt.executeUpdate(sql);
    		stmt.close(); 		
    	}
    	
    	return memberUid;
    }
    
    //This method should be used to add a photo to an album
    //returns true if photo added successfully
    //returns false if photo already exists
    public boolean addPhoto(String albumID, String memberID, String filePath, String title, String date) throws SQLException
    {
    	boolean photoAlreadyExists = checkFile(albumID, memberID, filePath, "PHOTOS");
    	
    	if (!photoAlreadyExists) 
    	{
    		Statement stmt = connection.createStatement();	
    		String sql = "Insert into FamilyPhotos values ('" + albumID + "' , '" + memberID + "' , '" + filePath + "' , '" + title + "' , '" + date + "')";
    		stmt.executeUpdate(sql);
    		stmt.close();
    	}
		
    	return !photoAlreadyExists;
    }
    
    //This method should be used to add a graphic to an album
    //returns true if graphic added successfully
    //returns false if graphic already exists
    public boolean addGraphic(String albumID, String memberID, String filePath, String title, String date) throws SQLException
    {
    	boolean graphicAlreadyExists = checkFile(albumID, memberID, filePath, "GRAPHICS");
    	
    	if (!graphicAlreadyExists) 
    	{
    		Statement stmt = connection.createStatement();	
    		String sql = "Insert into FamilyGraphics values ('" + albumID + "' , '" + memberID + "' , '" + filePath + "' , '" + title + "' , '" + date + "')";
    		stmt.executeUpdate(sql);
    		stmt.close();
    	}
		
    	return !graphicAlreadyExists;
    }
    
    //This method should be used to add a document to an album
    //returns true if document added successfully
    //returns false if document already exists
    public boolean addDocument(String albumID, String memberID, String filePath, String title, String date) throws SQLException
    {
    	boolean documentAlreadyExists = checkFile(albumID, memberID, filePath, "DOCUMENTS");
    	
    	if (!documentAlreadyExists) 
    	{
    		Statement stmt = connection.createStatement();	
    		String sql = "Insert into FamilyDocuments values ('" + albumID + "' , '" + memberID + "' , '" + filePath + "' , '" + title + "' , '" + date + "')";
    		stmt.executeUpdate(sql);
    		stmt.close();
    	}
		
    	return !documentAlreadyExists;
    }
    
    //return value of 2 means child already has two parents
    //return value of 1 means child already has the requested parent listed as a parent
    //return value of 0 means relationship was added
    //return value of -1 means childID and parentID are identical
    public int addParentChildRelationship(String albumID, String parentID, String childID) throws SQLException
    {
    	int relationshipStatus = checkRelationship(albumID, parentID, childID);
    	
    	if (relationshipStatus == 0)
    	{
    		Statement stmt = connection.createStatement();	
    		String sql = "Insert into FamilyParentChild values ('" + albumID + "' , '" + parentID + "' , '" + childID + "')";
    		stmt.executeUpdate(sql);
    		stmt.close();
    	}
		
    	return relationshipStatus;
    }
    
    //return value of 1 means at least one of the spouses already has a spouse
    //return value of 0 means relationship was added
    //return value of -1 means spouseIDs were identical
    public int addSpouseRelationship(String albumID, String spouseOne, String spouseTwo) throws SQLException
    {
    	int relationshipStatus = checkSpouseRelationship(albumID, spouseOne, spouseTwo);
    	
    	if (relationshipStatus == 0)
    	{
    		Statement stmt = connection.createStatement();	
    		String sql = "Insert into FamilySpouse values ('" + albumID + "' , '" + spouseOne + "' , '" + spouseTwo + "')";
    		stmt.executeUpdate(sql);
    		stmt.close();
    	}
		
    	return relationshipStatus;
    }
    
    //Takes an album name and returns the album id
    //User should track this album id
    public String openAlbum(String albumName) throws SQLException
    {
    	String albumID;
    	Statement stmt = connection.createStatement();
    	String sql = "Select * from FamilyAlbum where ALBUM_NAME = '" + albumName + "'";
    	results = stmt.executeQuery(sql);
    	
    	if (results.next())
    		albumID = results.getString(1);
    	else
    		albumID = "";
    	
    	stmt.close();
    	return albumID;
    }
    
    //Passes back member information in a string array based on ID
    //null array means member id was not in database 
    //0th element is memberID
    //1st element is First Name
    //2nd element is Last Name
    //3rd element is birth date
    //4th element is death date
    //5th element is description
    public String[] getMemberInformationById(String albumID, String memberID) throws SQLException
    {
    	String[] memberInformation = new String[6];
    	Statement stmt = connection.createStatement();
    	String sql = "Select * from FamilyMember where Member_ID = '" + memberID + "' AND Member_Album_ID = '" + albumID + "'";
    	results = stmt.executeQuery(sql);
    	
    	if (results.next())
    	{
    		memberInformation[0] = results.getString(1);
    		for (int i=1; i<6;i++)
    			memberInformation[i] = results.getString(i+2);
    	}
    	else
    		memberInformation = null;
    	
    	stmt.close();
    	return memberInformation;
    }
    
    //Returns a String, the string will be the id of the passed member's spouse  
    //Returns null member has no spouse
    public String getSpouse(String albumID, String memberID) throws SQLException
    {
    	String spouse = null;;
    	Statement stmt = connection.createStatement();
    	String sql = "Select * from FamilySpouse where Spouse_One = '" + memberID + "' AND Spouse_Album_ID = '" + albumID + "'";
    	results = stmt.executeQuery(sql);
		
    	if (results.next())
    		spouse = results.getString(3);
    	else 
    	{
    		sql = "Select * from FamilySpouse where Spouse_Two = '" + memberID + "' AND Spouse_Album_ID = '" + albumID + "'";
    		results = stmt.executeQuery(sql);
    		if (results.next())
        		spouse = results.getString(2);
    	}
    	
		

    	
    	stmt.close();
    	return spouse;
    }
    
    //Returns an array of strings, each containing an ID of a parent of the passed member id  
    //Returns null if parents found
    public String[] getParents(String albumID, String memberID) throws SQLException
    {
    	String[] parents = new String[2];
    	Statement stmt = connection.createStatement();
    	String sql = "Select * from FamilyParentChild where PARENT_CHILD_CHILD_MEMBER_ID = '" + memberID + "' AND Parent_Child_Album_ID = '" + albumID + "'";
    	results = stmt.executeQuery(sql);
		
    	for (int i=0; results.next() && i<parents.length;i++)
    		parents[i] = results.getString(2); 			
		
    	if (parents[0] == null)
    		parents=null;
    	
    	stmt.close();
    	return parents;
    }
    
    //Returns an array of strings, each containing an ID of a child of the passed member id
    //Returns null if parents found
    public String[] getChildren(String albumID, String memberID) throws SQLException
    {
    	String[] children=null;
    	
    	Statement countStmt = connection.createStatement();
    	String countSql = "Select count(*) from FAMILYPARENTCHILD where PARENT_CHILD_PARENT_MEMBER_ID = '" + memberID + "' AND Parent_Child_Album_ID = '" + albumID + "'";
    	results = countStmt.executeQuery(countSql);
    	
    	if(results.next())
    	{
    		int theSize = Integer.parseInt(results.getString(1));
    		if (theSize > 0)
    			children = new String[Integer.parseInt(results.getString(1))];
    		else
    			children = null;
    	}
    	
    	countStmt.close();
    	
    	if (children != null)
    	{
    		Statement stmt = connection.createStatement(); 	 	
    		String sql = "Select * from FamilyParentChild where PARENT_CHILD_PARENT_MEMBER_ID = '" + memberID + "' AND Parent_Child_Album_ID = '" + albumID + "'";
    		results = stmt.executeQuery(sql);
    	  	
    		for(int i=0;results.next() && i<children.length;i++)
    			children[i] = results.getString(3);
			
    		stmt.close();
    	}
		
    	return children;
    }
    
    //Returns a 2-D array of photos
    //[0][0] is the first photo's filepath
    //[0][1] is the first photo's title
    //[0][2] is the first photo's date
    //[1] contains info for the second photo
    //Returns null if no photos found for member
    public String[][] getPhotos(String albumID, String memberID) throws SQLException
    {
    	String[][] photoList = null;
    	
    	Statement countStmt = connection.createStatement();
    	String countSql = "Select count(*) from FAMILYPHOTOS where PHOTOS_MEMBER_ID = '" + memberID + "' AND PHOTOS_Album_ID = '" + albumID + "'";
    	results = countStmt.executeQuery(countSql);
    	
    	if(results.next())
    	{
    		int theSize = Integer.parseInt(results.getString(1));
    		if (theSize > 0)
    			photoList = new String[Integer.parseInt(results.getString(1))][3];
    		else
    			photoList = null;
    	}
    	
    	countStmt.close();
    	
    	if (photoList != null)
    	{
    		Statement stmt = connection.createStatement(); 	 	
    		String sql = "Select * from FAMILYPHOTOS where PHOTOS_MEMBER_ID = '" + memberID + "' AND PHOTOS_Album_ID = '" + albumID + "'";
    		results = stmt.executeQuery(sql);
    	  	
    		for(int i=0;results.next() && i<photoList.length;i++)
    		{
    			photoList[i][0] = results.getString(3);
    			photoList[i][1] = results.getString(4);
    			photoList[i][2] = results.getString(5);
    		}
			
    		stmt.close();
    	}
		
    	return photoList;
    }
    
    //Returns a 2-D array of graphics
    //[0][0] is the first graphic's filepath
    //[0][1] is the first graphic's title
    //[0][2] is the first graphic's date
    //[1] contains info for the second graphic
    //Returns null if no graphics found for member
    public String[][] getGraphics(String albumID, String memberID) throws SQLException
    {
    	String[][] graphicsList = null;
    	
    	Statement countStmt = connection.createStatement();
    	String countSql = "Select count(*) from FAMILYGRAPHICS where GRAPHICS_MEMBER_ID = '" + memberID + "' AND GRAPHICS_Album_ID = '" + albumID + "'";
    	results = countStmt.executeQuery(countSql);
    	
    	if(results.next())
    	{
    		int theSize = Integer.parseInt(results.getString(1));
    		if (theSize > 0)
    			graphicsList = new String[Integer.parseInt(results.getString(1))][3];
    		else
    			graphicsList = null;
    	}
    	
    	countStmt.close();
    	
    	if (graphicsList != null)
    	{
    		Statement stmt = connection.createStatement(); 	 	
    		String sql = "Select * from FAMILYGRAPHICS where GRAPHICS_MEMBER_ID = '" + memberID + "' AND GRAPHICS_Album_ID = '" + albumID + "'";
    		results = stmt.executeQuery(sql);
    	  	
    		for(int i=0;results.next() && i<graphicsList.length;i++)
    		{
    			graphicsList[i][0] = results.getString(3);
    			graphicsList[i][1] = results.getString(4);
    			graphicsList[i][2] = results.getString(5);
    		}
			
    		stmt.close();
    	}
    	return graphicsList;
    }
    
    //Returns a 2-D array of documents
    //[0][0] is the first document's filepath
    //[0][1] is the first document's title
    //[0][2] is the first document's date
    //[1] contains info for the second document
    //Returns null if no documents found for member
    public String[][] getDocuments(String albumID, String memberID) throws SQLException
    {
    	String[][] documentsList = null;
    	
    	Statement countStmt = connection.createStatement();
    	String countSql = "Select count(*) from FAMILYDOCUMENTS where DOCUMENTS_MEMBER_ID = '" + memberID + "' AND DOCUMENTS_Album_ID = '" + albumID + "'";
    	results = countStmt.executeQuery(countSql);
    	
    	if(results.next())
    	{
    		int theSize = Integer.parseInt(results.getString(1));
    		if (theSize > 0)
    			documentsList = new String[Integer.parseInt(results.getString(1))][3];
    		else
    			documentsList = null;
    	}
    	
    	countStmt.close();
    	
    	if (documentsList != null)
    	{
    		Statement stmt = connection.createStatement(); 	 	
    		String sql = "Select * from FAMILYDOCUMENTS where DOCUMENTS_MEMBER_ID = '" + memberID + "' AND DOCUMENTS_Album_ID = '" + albumID + "'";
    		results = stmt.executeQuery(sql);
    	  	
    		for(int i=0;results.next() && i<documentsList.length;i++)
    		{
    			documentsList[i][0] = results.getString(3);
    			documentsList[i][1] = results.getString(4);
    			documentsList[i][2] = results.getString(5);
    		}
			
    		stmt.close();
    	}
    	return documentsList;
    }
    
    //Returns a 2-D array of members
    //[0][0] is the first member's first name
    //[0][1] is the first member's last name
    //[0][2] is the first member's birth date
    //[0][3] is the first member's death date
    //[0][4] is the first member's description
    //[0][5] is the first member's id
    //[1] contains info for the second member
    //Returns null if no members match criteria
    //If firstName, lastName, and birthDate are all empty, then function will return all members
    public String[][] searchMembers(String albumID, String firstName, String lastName, String birthDate) throws SQLException
    {
    	String[][]memberList = null;
    	boolean firstNameEmpty,lastNameEmpty,birthDateEmpty;
    	int theSize = 0;
    	
    	firstNameEmpty = firstName.compareTo("")==0;
    	lastNameEmpty = lastName.compareTo("")==0;
    	birthDateEmpty = birthDate.compareTo("")==0;
    	
    	Statement countStmt = connection.createStatement();
    	String countSql;
    	
    	if (firstNameEmpty && lastNameEmpty && birthDateEmpty)
    		countSql =  "Select count(*) from FAMILYMEMBER where MEMBER_ALBUM_ID = '" + albumID +"'";
    	else if (firstNameEmpty && lastNameEmpty && !birthDateEmpty)
    		countSql = "Select count(*) from FAMILYMEMBER where MEMBER_ALBUM_ID = '" + albumID +"' AND MEMBER_BIRTHDATE = '" + birthDate + "'";
    	else if (firstNameEmpty && !lastNameEmpty && birthDateEmpty)
    		countSql = "Select count(*) from FAMILYMEMBER where MEMBER_ALBUM_ID = '" + albumID +"' AND MEMBER_LASTNAME = '" + lastName + "'";
    	else if (!firstNameEmpty && lastNameEmpty && birthDateEmpty)
    		countSql = "Select count(*) from FAMILYMEMBER where MEMBER_ALBUM_ID = '" + albumID +"' AND MEMBER_FIRSTNAME = '" + firstName + "'";
    	else if (!firstNameEmpty && !lastNameEmpty && birthDateEmpty)
    		countSql = "Select count(*) from FAMILYMEMBER where MEMBER_ALBUM_ID = '" + albumID +"' AND MEMBER_FIRSTNAME = '" + firstName + "' AND MEMBER_LASTNAME = '" + lastName + "'";
    	else if (!firstNameEmpty && lastNameEmpty && !birthDateEmpty)
    		countSql = "Select count(*) from FAMILYMEMBER where MEMBER_ALBUM_ID = '" + albumID +"' AND MEMBER_FIRSTNAME = '" + firstName + "' AND MEMBER_BIRTHDATE = '" + birthDate + "'";
    	else if (firstNameEmpty && !lastNameEmpty && !birthDateEmpty)
    		countSql = "Select count(*) from FAMILYMEMBER where MEMBER_ALBUM_ID = '" + albumID +"' AND MEMBER_LASTNAME = '" + lastName + "' AND MEMBER_BIRTHDATE = '" + birthDate + "'";
    	else 
    		countSql = "Select count(*) from FAMILYMEMBER where MEMBER_ALBUM_ID = '" + albumID +"' AND MEMBER_FIRSTNAME = '" + firstName + "' AND MEMBER_LASTNAME = '" + lastName + "' AND MEMBER_BIRTHDATE = '" + birthDate + "'";
    	
    	results = countStmt.executeQuery(countSql);
    	
    	if(results.next())
    	{
    		theSize = Integer.parseInt(results.getString(1));
    		if (theSize > 0)
    			memberList = new String[Integer.parseInt(results.getString(1))][6];
    		else
    			memberList = null;
    	}
    	
    	countStmt.close();
    	
    	if (memberList != null)
    	{
    		Statement stmt = connection.createStatement(); 	 	
    		String sql;
    		
    		if (firstNameEmpty && lastNameEmpty && birthDateEmpty)
        		sql =  "Select * from FAMILYMEMBER where MEMBER_ALBUM_ID = '" + albumID +"'";
        	else if (firstNameEmpty && lastNameEmpty && !birthDateEmpty)
        		sql = "Select * from FAMILYMEMBER where MEMBER_ALBUM_ID = '" + albumID +"' AND MEMBER_BIRTHDATE = '" + birthDate + "'";
        	else if (firstNameEmpty && !lastNameEmpty && birthDateEmpty)
        		sql = "Select * from FAMILYMEMBER where MEMBER_ALBUM_ID = '" + albumID +"' AND MEMBER_LASTNAME = '" + lastName + "'";
        	else if (!firstNameEmpty && lastNameEmpty && birthDateEmpty)
        		sql = "Select * from FAMILYMEMBER where MEMBER_ALBUM_ID = '" + albumID +"' AND MEMBER_FIRSTNAME = '" + firstName + "'";
        	else if (!firstNameEmpty && !lastNameEmpty && birthDateEmpty)
        		sql = "Select * from FAMILYMEMBER where MEMBER_ALBUM_ID = '" + albumID +"' AND MEMBER_FIRSTNAME = '" + firstName + "' AND MEMBER_LASTNAME = '" + lastName + "'";
        	else if (!firstNameEmpty && lastNameEmpty && !birthDateEmpty)
        		sql = "Select * from FAMILYMEMBER where MEMBER_ALBUM_ID = '" + albumID +"' AND MEMBER_FIRSTNAME = '" + firstName + "' AND MEMBER_BIRTHDATE = '" + birthDate + "'";
        	else if (firstNameEmpty && !lastNameEmpty && !birthDateEmpty)
        		sql = "Select * from FAMILYMEMBER where MEMBER_ALBUM_ID = '" + albumID +"' AND MEMBER_LASTNAME = '" + lastName + "' AND MEMBER_BIRTHDATE = '" + birthDate + "'";
        	else 
        		sql = "Select * from FAMILYMEMBER where MEMBER_ALBUM_ID = '" + albumID +"' AND MEMBER_FIRSTNAME = '" + firstName + "' AND MEMBER_LASTNAME = '" + lastName + "' AND MEMBER_BIRTHDATE = '" + birthDate + "'";
    		
    		results = stmt.executeQuery(sql);
    		for (int i=0;results.next();i++)
        	{
    			memberList[i][5] = results.getString(1);
        		for (int j=0; j<5;j++)
        			memberList[i][j] = results.getString(j+3);
        	}
    		stmt.close();
    	}
		
    	return memberList;
    }
    
    //returns 1 or greater if delete was successful -- sends back number of rows deleted
    //returns 0 if nothing was deleted
    public int deleteMember(String albumID, String memberID) throws SQLException
    {
    	int rowsDeleted = 0;
    	Statement stmt = connection.createStatement(); 	 	
    	String sql = "Delete from FamilyMember where MEMBER_ALBUM_ID = '" + albumID +"' And Member_ID = '" + memberID + "'";
    	rowsDeleted += stmt.executeUpdate(sql);
    	sql = "Delete from FamilyDocuments where Documents_ALBUM_ID = '" + albumID +"' And Documents_Member_ID = '" + memberID + "'";
    	rowsDeleted += stmt.executeUpdate(sql);
    	sql = "Delete from FamilyGraphics where Graphics_ALBUM_ID = '" + albumID +"' And Graphics_Member_ID = '" + memberID + "'";
    	rowsDeleted += stmt.executeUpdate(sql);
    	sql = "Delete from FamilyPhotos where Photos_ALBUM_ID = '" + albumID +"' And Photos_Member_ID = '" + memberID + "'";
    	rowsDeleted += stmt.executeUpdate(sql);
    	sql = "Delete from FamilyParentChild where Parent_Child_ALBUM_ID = '" + albumID +"' And Parent_Child_Parent_Member_ID = '" + memberID + "' Or Parent_Child_Child_Member_ID = '" + memberID + "'";
    	rowsDeleted += stmt.executeUpdate(sql);
    	sql = "Delete from FamilySpouse where Spouse_ALBUM_ID = '" + albumID +"' And Spouse_One = '" + memberID + "' Or Spouse_Two = '" + memberID + "'";
    	rowsDeleted += stmt.executeUpdate(sql);
    	
    	return rowsDeleted;
    }
    
    //returns 1 or greater if delete was successful -- sends back number of rows deleted
    //returns 0 if nothing was deleted
    public int deletePhoto(String albumID, String memberID, String filePath) throws SQLException
    {
    	int rowsDeleted = 0;
    	Statement stmt = connection.createStatement(); 	 	
    	String sql = "Delete from FamilyPhotos where Photos_ALBUM_ID = '" + albumID +"' And Photos_Member_ID = '" + memberID + "' And Photos_Filepath = '" + filePath + "'";
    	rowsDeleted = stmt.executeUpdate(sql);
    	
    	return rowsDeleted;
    }
    
    //returns 1 or greater if delete was successful -- sends back number of rows deleted
    //returns 0 if nothing was deleted
    public int deleteDocument(String albumID, String memberID, String filePath) throws SQLException
    {
    	int rowsDeleted = 0;
    	Statement stmt = connection.createStatement(); 	 	
    	String sql = "Delete from FamilyDocuments where Documents_ALBUM_ID = '" + albumID +"' And Documents_Member_ID = '" + memberID + "' And Documents_Filepath = '" + filePath + "'";
    	rowsDeleted = stmt.executeUpdate(sql);
    	
    	return rowsDeleted;
    }
    
    //returns 1 or greater if delete was successful -- sends back number of rows deleted
    //returns 0 if nothing was deleted
    public int deleteGraphic(String albumID, String memberID, String filePath) throws SQLException
    {
    	int rowsDeleted = 0;
    	Statement stmt = connection.createStatement(); 	 	
    	String sql = "Delete from FamilyGraphics where Graphics_ALBUM_ID = '" + albumID +"' And Graphics_Member_ID = '" + memberID + "' And Graphics_Filepath = '" + filePath + "'";
    	rowsDeleted = stmt.executeUpdate(sql);
    	
    	return rowsDeleted;
    }
    
    //returns 1 or greater if delete was successful -- sends back number of rows deleted
    //returns 0 if nothing was deleted
    public int deleteParentChildRelationship(String albumID, String parentID, String childID) throws SQLException
    {
    	int rowsDeleted = 0;
    	Statement stmt = connection.createStatement(); 	 	
    	String sql = "Delete from FamilyParentChild where Parent_Child_ALBUM_ID = '" + albumID +"' And Parent_Child_Parent_Member_ID = '" + parentID + "' And Parent_Child_Child_Member_ID = '" + childID + "'";
    	rowsDeleted = stmt.executeUpdate(sql);
    	
    	return rowsDeleted;
    }
    
    //returns 1 or greater if delete was successful -- sends back number of rows deleted
    //returns 0 if nothing was deleted
    public int deleteSpouseRelationship(String albumID, String spouseOne, String spouseTwo) throws SQLException
    {
    	int rowsDeleted = 0;
    	Statement stmt = connection.createStatement(); 	 	
    	String sql = "Delete from FamilySpouse where Spouse_ALBUM_ID = '" + albumID +"' And Spouse_One = '" + spouseOne + "' And Spouse_Two = '" + spouseTwo + "'";
    	rowsDeleted += stmt.executeUpdate(sql);
    	sql = "Delete from FamilySpouse where Spouse_ALBUM_ID = '" + albumID +"' And Spouse_One = '" + spouseTwo + "' And Spouse_Two = '" + spouseOne + "'";
    	rowsDeleted += stmt.executeUpdate(sql);
    	
    	return rowsDeleted;
    }
    
    //Returns 0 if member already exists
    //Returns 1 if member edited successfully
    public int editMember(String albumID, String memberID, String firstName, String lastName, String birthDate, String deathDate, String description) throws SQLException
    {
    	boolean memberAlreadyExists = checkMemberName(albumID, firstName, lastName, birthDate);
    	
    	if (!memberAlreadyExists)
    	{
    		Statement stmt = connection.createStatement();
    		String sql = "Update FamilyMember set member_firstname = '" + firstName + "', member_lastname = '" + lastName + "', member_birthdate = '" + birthDate + "', member_deathdate = '" + deathDate + "', member_description = '" + description + "' where member_id = '" + memberID + "' and member_album_id = '" + albumID + "'"; 
    		stmt.executeUpdate(sql);
    		stmt.close();
    		return 1;
    	}
    	
    	return 0;
    }
    
    
    
    //This method is used to check if requested album name already exists
    private boolean checkAlbumName(String albumName) throws SQLException
    {
    	boolean returnValue;
    	Statement stmt = connection.createStatement();
    	String sql = "Select * from FamilyAlbum where ALBUM_NAME = '" + albumName + "'";
    	results = stmt.executeQuery(sql);
    	
    	if (results.next())//means Album Name already in DB
    		returnValue = true;
    	else
    		returnValue = false;
    	
    	stmt.close();
    	return returnValue;
    }
    
    private boolean checkMemberName(String albumID, String firstName, String lastName, String birthDate) throws SQLException
    {
    	boolean returnValue;
    	Statement stmt = connection.createStatement();
    	String sql = "Select * from FamilyMember where MEMBER_ALBUM_ID = '" + albumID + "' AND MEMBER_FIRSTNAME = '" + firstName + "' AND MEMBER_LASTNAME = '" + lastName + "' AND MEMBER_BIRTHDATE = '" + birthDate + "'";
    	results = stmt.executeQuery(sql);
    	
    	if (results.next())//means Member already in DB
    		returnValue = true;
    	else
    		returnValue = false;
    	
    	stmt.close();
    	return returnValue; 	
    }
    
    private boolean checkFile(String albumID, String memberID, String filePath, String table) throws SQLException
    {
    	boolean returnValue;
    	Statement stmt = connection.createStatement();
    	String sql = "Select * from FAMILY" + table + " where " + table + "_ALBUM_ID = '" + albumID + "' AND " + table + "_MEMBER_ID = '" + memberID + "' AND " + table + "_FILEPATH = '" + filePath + "'";
    	results = stmt.executeQuery(sql);
    	
    	if (results.next())//means file already in DB
    		returnValue = true;
    	else
    		returnValue = false;
    	
    	stmt.close();
    	return returnValue;  	
    }
    
    //Checks the current relationship status
    //return value of 2 means child already has two parents
    //return value of 1 means child already has the requested parent listed as a parent
    //return value of 0 means relationship is ok to add
    //return value of -1 means childID and parentID are identical
    private int checkRelationship(String albumID, String parentID, String childID) throws SQLException
    {
    	if (childID.compareTo(parentID)==0)
    		return -1;
    	
    	Statement stmt = connection.createStatement();
    	String sql = "Select * from FAMILYPARENTCHILD where PARENT_CHILD_ALBUM_ID = '" + albumID + "' AND PARENT_CHILD_CHILD_MEMBER_ID = '" + childID + "'";
    	results = stmt.executeQuery(sql);
    	
    	//maybe should check to make sure both ids are valid?
    	
    	for (int i=0;results.next();i++)
    	{
    		if(i == 1) 
    		{
    			stmt.close();
    			return 2;
    		}
    		else if (results.getString(2).compareTo(parentID)==0 && results.getString(3).compareTo(childID)==0)
    		{
    			stmt.close();
    			return 1;
    		}
    	}
    	
    	stmt.close();
    	return 0;
    }
    
    //Checks the current spouse relationship status
    //return value of 1 means one of the spouses already has a spouse
    //return value of 0 means relationship is ok to add
    //return value of -1 means childID and parentID are identical
    private int checkSpouseRelationship(String albumID, String spouseOne, String spouseTwo) throws SQLException
    {
    	if (spouseOne.compareTo(spouseTwo)==0)
    		return -1;
    	
    	Statement stmt = connection.createStatement();
    	String sql = "Select * from FAMILYSPOUSE where SPOUSE_ALBUM_ID = '" + albumID + "' AND ( SPOUSE_ONE = '" + spouseOne + "' OR SPOUSE_ONE = '" + spouseTwo + "' OR SPOUSE_TWO = '" + spouseOne + "' OR SPOUSE_TWO = '" + spouseTwo + "' )";
    	results = stmt.executeQuery(sql);
    	
    	if (results.next())
    	{
    		stmt.close();
    		return 1;
    	}
    	
    	stmt.close();
    	return 0;
    }
	
	
//		public static void main(String[] args) throws Exception {
//			Connector conn = new Connector();
//			conn.addMember("1", "a", "b", "10/14/1983", "", "");
//			conn.addDocument("1", "78358e64-f773-406d-a1c4-4262e3906809", "testpath", "drghetrh", "10/14/1983");
//			conn.addDocument("1", "2", "drthdrthj", "drghetrh", "10/14/1983");
//			conn.addGraphic("1", "78358e64-f773-406d-a1c4-4262e3906809", "drthdrthj", "drghetrh", "10/14/1983");
//			conn.addGraphic("1", "2", "drthdrthj", "drghetrh", "10/14/1983");
//			conn.addPhoto("1", "78358e64-f773-406d-a1c4-4262e3906809", "drthdrthj", "drghetrh", "10/14/1983");
//			conn.addPhoto("1", "2", "drthdrthj", "drghetrh", "10/14/1983");
//			conn.addParentChildRelationship("1", "78358e64-f773-406d-a1c4-4262e3906809", "2");
//			conn.addSpouseRelationship("1", "78358e64-f773-406d-a1c4-4262e3906809", "2");
//			System.out.println(conn.deleteMember("1", "78358e64-f773-406d-a1c4-4262e3906809"));
//			System.out.println(conn.deleteDocument("1", "78358e64-f773-406d-a1c4-4262e3906809", "testpath"));
//			System.out.println(conn.deletePhoto("1", "78358e64-f773-406d-a1c4-4262e3906809", "drthdrthj"));
//			System.out.println(conn.deleteGraphic("1", "78358e64-f773-406d-a1c4-4262e3906809", "drthdrthj"));
//			System.out.println(conn.deleteParentChildRelationship("1", "78358e64-f773-406d-a1c4-4262e3906809", "2"));
//			System.out.println(conn.deleteSpouseRelationship("1", "78358e64-f773-406d-a1c4-4262e3906809", "2"));
//			System.out.println(conn.editMember("6fc53ccb-aae4-4504-b650-4a63089b1124", "c93bccda-2b48-40bf-9e4c-50d963f50bc6", "Diane", "Smith", "01/03/1974", "", "Test Edit"));
//			System.out.println(conn.getSpouse("1", "1"));
//			System.out.println(conn.addSpouseRelationship("1", "2", "1"));
			
//		}
}
