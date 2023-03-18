package newbank.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class Database {
    public File csv;
    public FileReader csvReader;

    // Constructor method -- assigns file path value    
    public Database(){
    csv = new File("NewBank/newbank/server/DatabaseFiles/db.csv");
    }
    /*
    NOTE: To handle the multi-threaded process, open and close the file with each action. DO NOT leave file open longer than neccesary. 
    */ 

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
}

    


