package com.sg.flooring.dto;

import java.math.BigDecimal;

public class Product {
    String productType;
    BigDecimal costPerSquareFoot;
    BigDecimal laborCostPerSquareFoot;

    public Product(String ProductType, BigDecimal costPerSquareFoot, BigDecimal laborCostPerSquareFoot) {
        this.productType = ProductType;
        this.costPerSquareFoot = costPerSquareFoot;
        this.laborCostPerSquareFoot = laborCostPerSquareFoot;
    }

    public Product() {
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String ProductType) {
        this.productType = ProductType;
    }

    public BigDecimal getCostPerSquareFoot() {
        return costPerSquareFoot;
    }

    public void setCostPerSquareFoot(BigDecimal costPerSquareFoot) {
        this.costPerSquareFoot = costPerSquareFoot;
    }

    public BigDecimal getLaborCostPerSquareFoot() {
        return laborCostPerSquareFoot;
    }

    public void setLaborCostPerSquareFoot(BigDecimal laborCostPerSquareFoot) {
        this.laborCostPerSquareFoot = laborCostPerSquareFoot;
    }

    @Override
    public String toString() {
        return "Product{" + "ProductType=" + productType + ", costPerSquareFoot=" + costPerSquareFoot + ", laborCostPerSquareFoot=" + laborCostPerSquareFoot + '}';
    }
    
}
