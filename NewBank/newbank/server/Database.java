package newbank.server;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;

// Serialised libraries
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class Database {
    public File csv;
    public FileReader csvReader;

    private static final String projectName = "NewBank";
    private static final String projectDirectory = "newbank/server/DatabaseFiles/";
    private String dbLocation;

    // Constructor method     
    public Database(){
        File currentDirFile = new File(".");
        
        // Code to reset the project root directory
        try{
            String userDirectory = currentDirFile.getCanonicalPath();
            if (userDirectory.contains(projectName)){
                dbLocation = userDirectory + "/" + projectDirectory;
            } else {
                dbLocation = userDirectory +"/" + projectName + "/" + projectDirectory;
            }
            
        } catch (IOException ioe){
            System.out.println("Database error setting default location: newbank/server/DatabaseFiles/");
            dbLocation = projectDirectory;
        }
        
    }
    
    public Customer getCustomer(String userName, boolean isSerialized) {
        File userNameFile = new File(dbLocation + userName.toLowerCase() + ".obj");

        // Try to find the name of existing customer
        try{
            FileInputStream fis = new FileInputStream(userNameFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Customer customer = (Customer) ois.readObject();
            ois.close(); 
            return customer;
        } catch (IOException ioe) {
            System.out.println(ioe);
            return null;
        } catch (ClassNotFoundException cnf){
            System.out.println(cnf);
            return null;
        }

    }

    public void addCustomer(Customer customer, boolean isSerialized){
        // add the customer into the database
        // Try to serialise the data
		try {
            File newUser = new File (dbLocation + customer.getUsername().toLowerCase() + ".obj");
			FileOutputStream fos = new FileOutputStream(newUser);
			ObjectOutputStream oos  = new ObjectOutputStream(fos);
    		oos.writeObject(customer);
    		oos.flush();
    		oos.close();
		} catch(IOException ioe) {
			System.out.println("Error addCustomer: " + ioe.toString());
		}
    }

    public boolean updateCustomer(Customer customer){
        // This is for updating information on a customer and write back
        try {
            File updatedUser = new File (dbLocation + customer.getUsername().toLowerCase() + ".obj");
			FileOutputStream fos = new FileOutputStream(updatedUser);
			ObjectOutputStream oos  = new ObjectOutputStream(fos);
    		oos.writeObject(customer);
    		oos.flush();
    		oos.close();
            return true;
		} catch(IOException ioe) {
			System.out.println("Error updateCustomer: " + ioe.toString());
            return false;
		}
    }

}

    


