package com.sg.flooring.dao;

import com.sg.flooring.dto.Order;
import com.sg.flooring.dto.Product;
import com.sg.flooring.dto.Tax;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface FlooringMasteryDao {
    
    void importStates() throws DaoFileAccessException;
    void importOrders() throws DaoFileAccessException;
    void importProducts() throws DaoFileAccessException;
    
    void exportData() throws DaoFileAccessException;
    
    Order addOrder(Order order) throws DaoFileAccessException;
    Order editOrderCustomer(String customer, Order order);
    Order editState(String stateAbbr, Order order);
    Order editProduct(String productType, Order order);
    Order editArea(BigDecimal area, Order order);
    
    Order removeOrder(Integer orderNum) throws DaoFileAccessException;
    Order getOrder(Integer orderNum);
    
    List<Order> importDatedOrders(LocalDate date) throws DaoFileAccessException;
    List<Order> getAllOrders();
    List<Tax> getAllStates();
    List<Product> getAllProducts();
    
    boolean isValidState(String s);
    boolean isValidProduct(String s);
    boolean doesFileExist(String filePath);

    List<Order> getDatedOrders(LocalDate date);
    
    void redoDatedFile(LocalDate date) throws DaoFileAccessException;

    Tax getState(String stateAbbr);

    Product getProduct(String productType);
    
}
