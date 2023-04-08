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
        
        try{
            String userDirectory = currentDirFile.getCanonicalPath();
            if (userDirectory.contains(projectName)){
                dbLocation = userDirectory + "/" + projectDirectory;
            } else {
                dbLocation = userDirectory +"/" + projectName + "/" + projectDirectory;
            }
            //System.out.println(helper);
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

    /*
    NOTE: To handle the multi-threaded process, open and close the file with each action. DO NOT leave file open longer than neccesary. 
     

    // Helper method to open the CSV file
    public FileReader openFile(File csv){
        try{
            FileReader csvReader = new FileReader(csv);
            return csvReader;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    // Helper method to close the CSV file
    public void closeFile(FileReader csvReader){
        try{
            this.csvReader.close();;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return;
    }
    
    public void addCustomer(Customer c){
        try{
            FileWriter csvWriter = new FileWriter(csv, true);
            csvWriter.append(c.getUsername());
            csvWriter.append(',');
            csvWriter.append(c.getPassword());
            csvWriter.append(',');
            csvWriter.append(c.getFirstName());
            csvWriter.append(',');
            csvWriter.append(c.getLastName());
            csvWriter.append(',');
            csvWriter.append(c.getAddress());
            csvWriter.append(',');
            csvWriter.append(c.getEmail());
            csvWriter.append('\n');
            csvWriter.flush();
            csvWriter.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return;
        

    }

    public Customer getCustomer(String userName){
        // Search the username value in the CSV
        this.csvReader = openFile(csv);
        BufferedReader csvBuffReader = new BufferedReader(this.csvReader);
        Customer customer;
        try{
            String row;
            while ((row = csvBuffReader.readLine()) != null){
                String[] data = row.split(",");
                if (data[0].contentEquals(userName)){
                    customer = new Customer(data[0], data[1], data[2], data[3], data[4], data[5]);
                    closeFile(this.csvReader);
                    return customer;
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        closeFile(this.csvReader);
        return null;
    }
    */

}

    


