package com.sg.flooring.dao;

import com.sg.flooring.dto.Order;
import com.sg.flooring.dto.Product;
import com.sg.flooring.dto.Tax;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class FlooringMasteryDaoFileImpl implements FlooringMasteryDao {
    
    private String EXPORT_DATA_PATH ="SampleFileData\\Backup\\DataExport.txt";
    private String ORDERS_FOLDER =  "SampleFileData\\Orders\\";
    private String STATES_PATH = "SampleFileData\\Data\\Taxes.txt";
    private String DATED_FILE_FIRST_LINE = "OrderNumber::CustomerName::State::TaxRate::ProductType::Area::CostPerSquareFoot::LaborCostPerSquareFoot::MaterialCost::LaborCost::Tax::Total::OrderDate";
    
    
    private final String DELIMITER = "::";
    
    private Map<Integer, Order> orders = new HashMap<>();
    private Map<String, Tax> states = new HashMap<>();
    private Map<String, Product> products = new HashMap<>();
    
    public FlooringMasteryDaoFileImpl() throws DaoFileAccessException {
        importStates();
        importOrders();
        importProducts();
    }
    
    
    @Override
    public void importStates() throws DaoFileAccessException{
        Scanner scanner;
        try{
            scanner = new Scanner(new BufferedReader(new FileReader(STATES_PATH)));
        }
        catch(FileNotFoundException e){
            //Turn the generic exception into our sapecific type so we can keep io in viewer.
            throw new DaoFileAccessException("File " + STATES_PATH + "Could not be found :(");
        }
        
        String currentLine = scanner.nextLine(); //Skip over the first line.
        Tax currentState;
        
        while(scanner.hasNextLine()){
            currentLine = scanner.nextLine();
            currentState = unmarshallState(currentLine);
            states.put(currentState.getStateAbbreviation(),currentState);
        }
        scanner.close();
    }
    
    @Override
    public void importOrders() throws DaoFileAccessException {
        Scanner scanner;
        try{
            scanner = new Scanner(new BufferedReader(new FileReader(EXPORT_DATA_PATH)));
        }
        catch(FileNotFoundException e){
            //Turn the generic exception into our sapecific type so we can keep io in viewer.
            throw new DaoFileAccessException("File " + EXPORT_DATA_PATH + "Could not be found :(");
        }
        
        String currentLine = scanner.nextLine(); //Skip over the first line.
        Order nextOrder = null;
        
        while(scanner.hasNextLine()){
            currentLine = scanner.nextLine();
            nextOrder = unmarshallOrder(currentLine);
            orders.put(nextOrder.getOrderNumber(), nextOrder);
        }
        if (nextOrder!=null){
            Order.setNextOrderNum(nextOrder.getOrderNumber()+1);
        }
        else{
            Order.setNextOrderNum(1);
        }
        scanner.close();
    }
    
    public List<Order> importDatedOrders(LocalDate date) throws DaoFileAccessException {
        Scanner scanner;
        
        String filePath = getDatedOrderFilePath(date);
        
        List<Order> ordersFromDate = new ArrayList<>();
        
        try{
            scanner = new Scanner(new BufferedReader(new FileReader(filePath)));
        }
        catch(FileNotFoundException e){
            //Turn the generic exception into our sapecific type so we can keep io in viewer.
            throw new DaoFileAccessException("No orders exist for " + date.format(DateTimeFormatter.ISO_DATE) + " :(");
        }
        
        String currentLine = scanner.nextLine(); //Skip over the first line.
        Order nextOrder = null;
        
        while(scanner.hasNextLine()){
            currentLine = scanner.nextLine();
            nextOrder = unmarshallDatedOrder(currentLine, date);
            ordersFromDate.add(nextOrder);
        }
        
        scanner.close();
        return ordersFromDate;
    }
    
    private String getDatedOrderFilePath(LocalDate date){
        return ORDERS_FOLDER+"Orders_"+date.format(DateTimeFormatter.BASIC_ISO_DATE)+".txt";
    }
    
    @Override
    public void importProducts() throws DaoFileAccessException{
        Scanner scanner;
        String PRODUCTS_PATH = "SampleFileData\\Data\\Products.txt";
        try{
            scanner = new Scanner(new BufferedReader(new FileReader(PRODUCTS_PATH)));
        }
        catch(FileNotFoundException e){
            //Turn the generic exception into our specific type so we can keep io in viewer.
            throw new DaoFileAccessException("File " + PRODUCTS_PATH + "Could not be found :(");
        }
        
        String currentLine = scanner.nextLine(); //Skip over the first line.
        Product product;
        
        while(scanner.hasNextLine()){
            currentLine = scanner.nextLine();
            product = unmarshallProduct(currentLine);
            products.put(product.getProductType(),product);
        }
        scanner.close();
    }
    
    @Override
    public void exportData() throws DaoFileAccessException{
        PrintWriter out;
        String toWrite;
        try {
            out = new PrintWriter(new FileWriter(EXPORT_DATA_PATH));
        }
        catch (IOException ex) {
            throw new DaoFileAccessException("Data could not be saved :(");
        }
        
        out.println("OrderNumber::CustomerName::State::TaxRate::ProductType::Area::CostPerSquareFoot::"
                + "LaborCostPerSquareFoot::MaterialCost::LaborCost::Tax::Total::OrderDate");
        for(Order order: this.getAllOrders()){
            toWrite = order.getDatedFileString();
            out.println(toWrite);
        }
        out.flush();
        out.close();
    }
    
    @Override
    public Order addOrder(Order order) throws DaoFileAccessException {
        Order result = orders.put(order.getOrderNumber(), order);
        this.printToDatedFile(order);
        this.exportData();      
        return result;
    }
    
    @Override
    public Order editOrderCustomer(String customer, Order order) {
        order.setCustomerName(customer);
        return order;
    }
    
    @Override
    public Order editProduct(String productType, Order order) {
        Product product = products.get(productType);
        order.setCostPerSqFoot(product.getCostPerSquareFoot());
        order.setLaborCost(product.getLaborCostPerSquareFoot());
        order.setProductType(product.getProductType());
        return order;
    }
    
    @Override
    public boolean doesFileExist(String filePath){
        File file = new File(filePath);
        return file.exists();
    }
    
    @Override
    public Order editArea(BigDecimal area, Order order) {
        order.setArea(area);
        return order;
    }
    
    @Override
    public Order removeOrder(Integer orderNum) throws DaoFileAccessException{
        Order toDrop = orders.remove(orderNum);
        this.exportData();
        this.redoDatedFile(toDrop.getOrderDate());
        return toDrop;
    }
    
    @Override
    public Order getOrder(Integer orderNum) {
        return orders.get(orderNum);
    }
    
    @Override
    public Order editState(String stateAbbr, Order order) {
        Tax state = states.get(stateAbbr);
        order.setStateAbbr(state.getStateAbbreviation());
        order.setTaxRate(state.getTaxRate());
        return order;
    }
    
    private Tax unmarshallState(String currentLine) {
        Tax newState = new Tax();
        String[] tokens = currentLine.split(DELIMITER, 3);
        newState.setStateAbbreviation(tokens[0]);
        newState.setStateName(tokens[1]);
        newState.setTaxRate(new BigDecimal(tokens[2]));
        return newState;
    }
    private Product unmarshallProduct(String currentLine) {
        Product newProduct = new Product();
        String[] tokens = currentLine.split(DELIMITER, 3);
        newProduct.setProductType(tokens[0]);
        newProduct.setCostPerSquareFoot(new BigDecimal (tokens[1]));
        newProduct.setLaborCostPerSquareFoot(new BigDecimal(tokens[2]));
        return newProduct;
    }
    
    private Order unmarshallOrder(String currentLine) {
        Order order = new Order();
        String[] tokens = currentLine.split(DELIMITER);
        order.setOrderNumber(Integer.parseInt(tokens[0]));
        order.setCustomerName(tokens[1]);
        order.setStateAbbr(tokens[2]);
        order.setTaxRate(new BigDecimal(tokens[3]));
        order.setProductType(tokens[4]);
        order.setArea(new BigDecimal(tokens[5]));
        order.setCostPerSqFoot(new BigDecimal(tokens[6]));
        order.setLaborPerSqFoot(new BigDecimal(tokens[7]));
        order.setMaterialCost(new BigDecimal(tokens[8]));
        order.setLaborCost(new BigDecimal(tokens[9]));
        order.setTax(new BigDecimal(tokens[10]));
        order.setTotal(new BigDecimal(tokens[11]));
        order.setOrderDate(LocalDate.parse(tokens[12], DateTimeFormatter.BASIC_ISO_DATE));
        return order;
    }
    
    private void printToDatedFile(Order order) throws DaoFileAccessException {
        
        String toPrint = order.getFileString();
        
        String path = ORDERS_FOLDER + "/Orders_" + order.getOrderDate().format(DateTimeFormatter.BASIC_ISO_DATE) +".txt";
        
        PrintWriter out;
        boolean firstLineNeeded = !doesFileExist(path);
        try {
            out = new PrintWriter(new FileWriter(path, true));
            
        } catch (IOException e) {
            throw new DaoFileAccessException("Could not write order to its dated file.", e);
        }
        if(firstLineNeeded){
            out.println(DATED_FILE_FIRST_LINE);
        }
        
        
        out.println(toPrint);
        out.flush();
        out.close();
    }
    
    private Order unmarshallDatedOrder(String currentLine, LocalDate date) {
        Order order = new Order();
        String[] tokens = currentLine.split(DELIMITER);
        order.setOrderNumber(Integer.parseInt(tokens[0]));
        order.setCustomerName(tokens[1]);
        order.setStateAbbr(tokens[2]);
        order.setTaxRate(new BigDecimal(tokens[3]));
        order.setProductType(tokens[4]);
        order.setArea(new BigDecimal(tokens[5]));
        order.setCostPerSqFoot(new BigDecimal(tokens[6]));
        order.setLaborPerSqFoot(new BigDecimal(tokens[7]));
        order.setMaterialCost(new BigDecimal(tokens[8]));
        order.setLaborCost(new BigDecimal(tokens[9]));
        order.setTax(new BigDecimal(tokens[10]));
        order.setTotal(new BigDecimal(tokens[11]));
        order.setOrderDate(date);
        return order;
    }
    @Override
    public List<Order> getAllOrders() {
        return new ArrayList<>(orders.values());
    }
    @Override
    public List<Tax> getAllStates() {
        return new ArrayList<>(states.values());
    }
    @Override
    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }
    @Override
    public boolean isValidState(String s) {
        for (String stateAbbr : states.keySet()){
            if (s.equals(stateAbbr)){
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean isValidProduct(String s) {
        for (String productType : products.keySet()){
            if (s.equals(productType)){
                return true;
            }
        }
        return false;
    }
    
    @Override
    public List<Order> getDatedOrders(LocalDate date) {
        List<Order> datedOrders = new ArrayList<>();
        for(Order order : this.orders.values()){
            if (order.getOrderDate().equals(date)){
                datedOrders.add(order);
            }
        }
        return datedOrders;
    }
    
    @Override
    public void redoDatedFile(LocalDate date) throws DaoFileAccessException {
        
        String path = ORDERS_FOLDER + "\\Orders_" + date.format(DateTimeFormatter.BASIC_ISO_DATE)+".txt";
        
        PrintWriter out;
        try {
            out = new PrintWriter(new FileWriter(path));
            
        } catch (IOException e) {
            throw new DaoFileAccessException("Could not write order to its dated file.", e);
        }
        out.println(DATED_FILE_FIRST_LINE);
        
        List<Order> toPrint = this.getDatedOrders(date);
        for(Order o : toPrint){
            out.println(o.getFileString());
        }
        
        out.flush();
        out.close();
    }
    
    @Override
    public Tax getState(String stateAbbr) {
        return states.get(stateAbbr);
    }
    
    @Override
    public Product getProduct(String productType) {
        return products.get(productType);
    }
    
}
