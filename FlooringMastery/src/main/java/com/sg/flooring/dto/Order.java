package com.sg.flooring.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Order {
    static String DELIMITER = "::";
    static int nextOrderNum;
    Integer orderNumber;
    String customerName;
    BigDecimal area;
    
    String stateAbbr;
    BigDecimal taxRate;
    String productType;
    
    BigDecimal costPerSqFoot;
    BigDecimal laborPerSqFoot;
    BigDecimal materialCost;
    BigDecimal laborCost;
    BigDecimal tax;
    BigDecimal total;
    
    LocalDate orderDate;

    public Order() {
        this.orderNumber = Order.nextOrderNum;
        Order.nextOrderNum++;
        this.setOrderDate(LocalDate.now());
    }

    public static int getNextOrderNum() {
        return nextOrderNum;
    }

    public static void setNextOrderNum(int nextOrderNum) {
        Order.nextOrderNum = nextOrderNum;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public String getStateAbbr() {
        return stateAbbr;
    }

    public void setStateAbbr(String stateAbbr) {
        this.stateAbbr = stateAbbr;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public BigDecimal getCostPerSqFoot() {
        return costPerSqFoot;
    }

    public void setCostPerSqFoot(BigDecimal costPerSqFoot) {
        this.costPerSqFoot = costPerSqFoot;
    }

    public BigDecimal getLaborPerSqFoot() {
        return laborPerSqFoot;
    }

    public void setLaborPerSqFoot(BigDecimal laborPerSqFoot) {
        this.laborPerSqFoot = laborPerSqFoot;
    }

    public BigDecimal getMaterialCost() {
        return materialCost;
    }

    public void setMaterialCost(BigDecimal materialCost) {
        this.materialCost = materialCost;
    }

    public BigDecimal getLaborCost() {
        return laborCost;
    }

    public void setLaborCost(BigDecimal laborCost) {
        this.laborCost = laborCost;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

   
    public String getFileString(){

        return orderNumber + Order.DELIMITER 
                + customerName + Order.DELIMITER 
                
                + stateAbbr + Order.DELIMITER 
                + taxRate + Order.DELIMITER 

                + productType+ Order.DELIMITER
                + area + Order.DELIMITER 
                + costPerSqFoot+ Order.DELIMITER
                + laborPerSqFoot + Order.DELIMITER

                + materialCost + Order.DELIMITER 
                + laborCost + Order.DELIMITER 
                + tax + Order.DELIMITER 
                + total;

        
    }
     public String getDatedFileString(){

        return orderNumber + Order.DELIMITER 
                + customerName + Order.DELIMITER 
                
                + stateAbbr + Order.DELIMITER 
                + taxRate + Order.DELIMITER 

                + productType+ Order.DELIMITER
                + area + Order.DELIMITER 
                + costPerSqFoot+ Order.DELIMITER
                + laborPerSqFoot + Order.DELIMITER

                + materialCost + Order.DELIMITER 
                + laborCost + Order.DELIMITER 
                + tax + Order.DELIMITER 
                + total + Order.DELIMITER
                + orderDate.format(DateTimeFormatter.BASIC_ISO_DATE);

        
    }

    @Override
    public String toString() {
        return "Order{" + "orderNumber=" + orderNumber + ", customerName=" + customerName + ", area=" + area + ", stateAbbr=" + stateAbbr + ", taxRate=" + taxRate + ", productType=" + productType + ", costPerSqFoot=" + costPerSqFoot + ", laborPerSqFoot=" + laborPerSqFoot + ", materialCost=" + materialCost + ", laborCost=" + laborCost + ", tax=" + tax + ", total=" + total + '}';
    }

    public void setOrderNum(int i) {
            this.orderNumber = i;
        }
    
}
