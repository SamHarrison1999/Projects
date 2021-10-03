
package com.sg.flooring.dao;

import com.sg.flooring.dto.Order;

public interface FlooringMasteryAuditorDao {
    
    void writeEntry(String s)throws AuditorFileAccessException;

    void writeAddEntry(Order order) throws AuditorFileAccessException;

    void writeRemoveEntry(Order order)throws AuditorFileAccessException;

    void writeEditEntry(Order order)throws AuditorFileAccessException;

}
