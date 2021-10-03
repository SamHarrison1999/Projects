package com.sg.flooring.dao;

import com.sg.flooring.dto.Order;
import com.sg.flooring.dto.Product;
import com.sg.flooring.dto.Tax;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

public class FlooringMasteryDaoFileImplTest {
    
    FlooringMasteryDao dao;
    Order testOrder;
    public FlooringMasteryDaoFileImplTest() {
    }
    
    
    @BeforeEach
    public void setUp() {
        try{
            dao = new FlooringMasteryDaoFileImpl();
            setupTestOrder();
        }
        catch(Exception e){
            System.out.println("Issue initializing tests");
        }
    }
    private void setupTestOrder(){
        testOrder = new Order();
        testOrder.setOrderNum(-1);
        testOrder.setArea(BigDecimal.ONE);
        testOrder.setCustomerName("Test Customer");
        testOrder.setOrderDate(LocalDate.now());
        testOrder.setStateAbbr("CA");
        testOrder.setTaxRate(new BigDecimal("5.0"));
        testOrder.setCostPerSqFoot(BigDecimal.ONE);
        testOrder.setLaborPerSqFoot(BigDecimal.ONE);
        testOrder.setLaborCost(BigDecimal.ZERO);
        testOrder.setMaterialCost(BigDecimal.ZERO);
        testOrder.setTax(BigDecimal.ZERO);
        testOrder.setTotal(BigDecimal.ONE);
        testOrder.setProductType("Tile");
        
        try{
            dao.addOrder(testOrder);
        }
        catch(DaoFileAccessException e){
            System.out.println("COULD NOT ADD ORDER TO DAO");
        }
    }
    @AfterEach
    public void tearDown() {
        try{
            dao.removeOrder(testOrder.getOrderNumber());
        }
        catch (Exception e){
            System.out.println("Error getting rid of test order.");
        }
    }
    
    @Test
    public void testEditProduct() {
        System.out.println("editProduct");
        String productType = "Laminate";
        System.out.println(this.testOrder);
        Order order = dao.getOrder(testOrder.getOrderNumber());
        dao.editProduct(productType, order);
        Product product = dao.getProduct(productType);
        assertEquals(order.getProductType(), product.getProductType());
        assertEquals(0, product.getCostPerSquareFoot().compareTo(order.getCostPerSqFoot()));
        
    }
    @Test
    public void testEditState() {
        
        System.out.println("editState");
        String state = "TX";
        Order order = dao.getOrder(testOrder.getOrderNumber());
        dao.editState(state, order);
        Tax targetState = dao.getState(state);
        assertEquals(order.getStateAbbr(), targetState.getStateAbbreviation());
        assertEquals(0,targetState.getTaxRate().compareTo(order.getTaxRate()));
        
    }
    /**
     * Test of doesFileExist method, of class FlooringMasteryDaoFileImpl.
     */
    @Test
    public void testDoesFileExist() {
        System.out.println("doesFileExist");
        String filePath = "invalidFilePath";
        
        assertFalse(dao.doesFileExist(filePath));
        assertTrue(dao.doesFileExist("SampleFileData\\Backup\\DataExport.txt"));
        
    }
    
    public void testGetOrder() {
        System.out.println("getOrder");
        Integer orderNum = -1;
        Order expResult = testOrder;
        Order result = dao.getOrder(orderNum);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of isValidState method, of class FlooringMasteryDaoFileImpl.
     */
    @Test
    public void testIsValidState() {
        System.out.println("isValidState");
        assertTrue(dao.isValidState("CA"));
        assertFalse(dao.isValidState("Brooklyn"));
    }
    
}
