package newbank.server;

import java.io.File;
import java.util.Set;
import java.util.TimerTask;

public class Routines extends TimerTask{

    // This method returns all files in a given directory
    public Set<String> getFiles(String dir){
        Set<String> results = new Set<String>();
        File[] files = new File(dir).listFiles();
        for (File file : files) {
            // Select only .obj files
            if (file.getName().contains(".obj")) {
                results.add(file.getName());
            }
        }
        return results;
    }

    public void updateSavingsInterest(){
        // Run through all savings accounts and add interest calculations

    }

    // Create a similar method for calculating all loan balances

    // Create a monthly routine to debit accounts

    // Create a regular routine for direct debits

    public void run(){
        // Update savings interest
        updateSavingsInterest();
    }
}
