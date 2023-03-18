package newbank.server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

public class Database {
    public final String filePath;
    public FileReader csv;

    // Constructor method -- assigns file path value    
    public Database(){
    filePath = "DatabaseFiles/db.csv";
    }

    /*
    NOTE: To handle the multi-threaded process, open and close the file with each action. DO NOT leave file open longer than neccesary. 
    */ 

    // Helper method to open the CSV file
    public FileReader openFile(){
        try{
            FileReader reader = new FileReader(this.filePath);
            return reader;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    // Helper method to close the CSV file
    public void closeFile(FileReader csv){
        try{
            this.csv.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return;
    }
    
    public void addCustomer(Customer c){
        try{
            FileWriter csvWriter = new FileWriter(this.filePath);
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
            csvWriter.append(',');
            csvWriter.append("/n");
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
        this.csv = openFile();
        BufferedReader csvReader = new BufferedReader(this.csv);
        Customer customer;
        try{
            String row;
            while ((row = csvReader.readLine()) != null){
                String[] data = row.split(",");
                if (data[0]==userName){
                    customer = new Customer(data[0], data[1], data[2], data[3], data[4], data[5]);
                    closeFile(csv);
                    return customer;
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        closeFile(csv);
        return null;
    }
}

    


