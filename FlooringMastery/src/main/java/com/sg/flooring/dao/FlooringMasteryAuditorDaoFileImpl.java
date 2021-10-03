package com.sg.flooring.dao;

import com.sg.flooring.dto.Order;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class FlooringMasteryAuditorDaoFileImpl implements FlooringMasteryAuditorDao {

    
    public static String AUDIT_FILE = "audit.txt";
   
    public FlooringMasteryAuditorDaoFileImpl(){
        
    }
    public FlooringMasteryAuditorDaoFileImpl(String s){
        AUDIT_FILE = s;
    }
    public void writeEntry(String entry) throws AuditorFileAccessException {
        PrintWriter out;
       
        try {
            out = new PrintWriter(new FileWriter(AUDIT_FILE, true));
        } catch (IOException e) {
            throw new AuditorFileAccessException("Could not persist audit information.", e);
        }
 
        LocalDateTime timestamp = LocalDateTime.now();
        out.println(timestamp + " : " + entry);
        out.flush();
        out.close();
    }

    @Override
    public void writeAddEntry(Order order) throws AuditorFileAccessException{
        String s = LocalDateTime.now() + ": the following Order has been added " + order.toString();
        writeEntry(s);
    }

    @Override
    public void writeRemoveEntry(Order order) throws AuditorFileAccessException {
        String s = LocalDateTime.now() + ": the following Order has been removed" + order.toString();
        writeEntry(s);
    }

    @Override
    public void writeEditEntry(Order order) throws AuditorFileAccessException {
        String s = LocalDateTime.now() + ": the following Order has been edited" + order.toString();
        writeEntry(s);
    }
    
    
}
