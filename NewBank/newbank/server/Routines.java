package newbank.server;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.TimerTask;

public class Routines extends TimerTask{
    final private static String dir = "newbank/server/DatabaseFiles/";
    
    // This method returns all files in a given directory
    public Set<String> getFiles(String dir){
        Set<String> results = new HashSet<String>();
        File[] files = new File(dir).listFiles();
        for (File file : files) {
            // Select only .obj files
            if (file.getName().contains(".obj")) {
                results.add(file.getName());
            }
        }
        return results;
    }

    // Run through all savings accounts and add interest calculations
    public void updateSavingsInterest(){
        Set<String> files = getFiles(null)

    }

    // Create a similar method for calculating all loan balances

    // Create a monthly routine to debit accounts

    // Create a regular routine for direct debits

    public void run(){
        // Update savings interest
        updateSavingsInterest();
    }
}
