package com.sg.flooring.service;

import com.sg.flooring.dao.AuditorFileAccessException;
import com.sg.flooring.dao.DaoFileAccessException;
import com.sg.flooring.dto.Order;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FlooringMasteryServiceLayerTest {
    FlooringMasteryServiceLayer service;
    Order testOrder;
    public FlooringMasteryServiceLayerTest() {
    }
    
    @BeforeEach
    public void setUp() {
        try {
            service = new FlooringMasteryServiceLayer();
            setupTestOrder();
        } catch (DaoFileAccessException ex) {
            System.out.println("Service init error");
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
            service.fillInAndAddOrder(testOrder);
        }
        catch(DaoFileAccessException|AuditorFileAccessException e){
            System.out.println("COULD NOT ADD ORDER TO DAO");
        }
    }
    @AfterEach
    public void tearDown() {
        try {
            service.removeOrder(-1);
        } catch (DaoFileAccessException | AuditorFileAccessException ex) {
            Logger.getLogger(FlooringMasteryServiceLayerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

  
    /**
     * Test of removeOrder method, of class FlooringMasteryServiceLayer.
     */
    @Test
    public void testRemoveOrder() {
        try {
            service.removeOrder(-1);
            assertFalse(service.getAllOrders().contains(service.getOrder(-1)));
            service.fillInAndAddOrder(testOrder);
        } catch (DaoFileAccessException | AuditorFileAccessException ex) {
            Logger.getLogger(FlooringMasteryServiceLayerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of editOrderState method, of class FlooringMasteryServiceLayer.
     */
    @Test
    public void testEditOrderState() {
        try {
            BigDecimal oldTotal = testOrder.getTotal();
            service.editOrderState("TX", testOrder.getOrderNumber());
            assertTrue(0 != oldTotal.compareTo(testOrder.getTotal()));
        } catch (AuditorFileAccessException ex) {
            Logger.getLogger(FlooringMasteryServiceLayerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of orderCalcs method, of class FlooringMasteryServiceLayer.
     */
    @Test
    public void testOrderCalcs() {
        Order knownOrder = service.getOrder(1);
        Order order = new Order();
        order.setOrderNum(-1);
        order.setArea(knownOrder.getArea());
        order.setCustomerName("Test Customer");
        order.setOrderDate(LocalDate.now());
        order.setStateAbbr(knownOrder.getStateAbbr());
        order.setProductType(knownOrder.getProductType());
        service.orderCalcs(order);
        
        assertEquals(0, order.getTotal().compareTo(knownOrder.getTotal()));
    }

    /**
     * Test of isValidState method, of class FlooringMasteryServiceLayer.
     */
    @Test
    public void testIsValidState() {
        assertTrue(service.isValidState("TX"));
        assertFalse(service.isValidState("Tile"));
    }

    /**
     * Test of isValidProduct method, of class FlooringMasteryServiceLayer.
     */
    @Test
    public void testIsValidProduct() {
        assertTrue(service.isValidProduct("Tile"));
        assertFalse(service.isValidProduct("TX"));
    }
    
}
