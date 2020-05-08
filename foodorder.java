package se1;


import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class foodorder extends Frame
{
	Connection connection;
	Statement statement;
	Button givedetailsButton;
	TextArea errorText;
	TextField fidText,vegfoodsText,nonvegfoodsText,drinksText;
	ResultSet rs;
	boolean a=true;
	public foodorder() 
	{
		try 
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} 
		catch (Exception e) 
		{
			System.err.println("Unable to find and load driver");
			System.exit(1);
		}
		connectToDB();
	}
	
	
	public void connectToDB() 
    {
		try 
		{
		  connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","sravya","vasavi");
		  statement = connection.createStatement();

		} 
		catch (SQLException connectException) 
		{
		  System.out.println(connectException.getMessage());
		  System.out.println(connectException.getSQLState());
		  System.out.println(connectException.getErrorCode());
		  System.exit(1);
		}
    }
	
	public boolean buildGUI() 
	{	
		
		//Handle Insert Block Button nameText,placeText,type_of_helpText,phone_noText
		givedetailsButton = new Button("Submit");
		givedetailsButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				try 
				{
				  String query= "INSERT INTO foodorder VALUES(" +"'"+ fidText.getText() + "','" + vegfoodsText.getText() + "','"+ nonvegfoodsText.getText() +"','" +drinksText.getText()+"')";
				  int i = statement.executeUpdate(query);
				  errorText.append("\nInserted " + i + " rows successfully");
				} 
				catch (SQLException insertException) 
				{
				  displaySQLErrors(insertException);
				  a=true;
				}
				
				// DISPLAYING THE NAMES OF ORG.
				
				try 
				{
				  rs = statement.executeQuery("SELECT * FROM foodorder");
				  while (rs.next()) 
				  {
					  System.out.println(rs.getString("fid"));
				  }
				} 
				catch (SQLException en) 
				{ 
				  displaySQLErrors(en);a=false;
				}
			}
		});

	
		fidText = new TextField(15);
		vegfoodsText = new TextField(15);
		nonvegfoodsText = new TextField(15);
		drinksText= new TextField(15);
		
		errorText = new TextArea(10, 40);
		errorText.setEditable(false);

		Panel first = new Panel();
		first.setLayout(new GridLayout(4, 2));
		first.add(new Label("foodid:"));
		first.add(fidText);
		first.add(new Label("vegfoods:"));
		first.add(vegfoodsText);
		first.add(new Label("nonvegfoods:"));
		first.add(nonvegfoodsText);
		first.add(new Label("drinks:"));
		first.add(drinksText);
		
		
		first.setBounds(125,90,200,100);
		
		Panel second = new Panel(new GridLayout(4, 1));
		second.add(givedetailsButton);
        second.setBounds(125,220,150,100);         
		
		Panel third = new Panel();
		third.add(errorText);
		third.setBounds(125,320,300,200);
		
		setLayout(null);

		add(first);
		add(second);
		add(third);
	    
		setTitle("Give menu Details");
		setSize(500, 600);
		setVisible(true);
		return a;
	}

	

	private void displaySQLErrors(SQLException e) 
	{
		errorText.append("\nSQLException: " + e.getMessage() + "\n");
		errorText.append("SQLState:     " + e.getSQLState() + "\n");
		errorText.append("VendorError:  " + e.getErrorCode() + "\n");
	}
	public static void main(String[] args) 
	{
		foodorder sail = new foodorder();

		sail.addWindowListener(new WindowAdapter(){
		  public void windowClosing(WindowEvent e) 
		  {
			System.exit(0);
		  }
		});
		
		sail.buildGUI();
	}
}
