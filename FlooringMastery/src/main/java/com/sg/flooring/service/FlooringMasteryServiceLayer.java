
package com.sg.flooring.service;

import com.sg.flooring.dao.*;
import com.sg.flooring.dao.FlooringMasteryDaoFileImpl;
import com.sg.flooring.dao.FlooringMasteryDao;
import com.sg.flooring.dao.AuditorFileAccessException;
import com.sg.flooring.dao.DaoFileAccessException;
import com.sg.flooring.dto.Order;
import com.sg.flooring.dto.Product;
import com.sg.flooring.dto.Tax;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

public class FlooringMasteryServiceLayer {

    FlooringMasteryDao dao;
    FlooringMasteryAuditorDao auditor;

    public FlooringMasteryServiceLayer() throws DaoFileAccessException {
        dao = new FlooringMasteryDaoFileImpl();
        auditor = new FlooringMasteryAuditorDaoFileImpl();
    }

    public FlooringMasteryServiceLayer(FlooringMasteryDao dao, FlooringMasteryAuditorDao auditor) {
        this.dao = dao;
        this.auditor = auditor;
    }

    public void exportData() throws DaoFileAccessException {
        dao.exportData();
    }

    public List<Order> getAllOrders() {
        return dao.getAllOrders();
    }

    public List<Order> getDatedOrders(LocalDate date) throws NoOrdersFoundException {
        List<Order> datedOrders = dao.getDatedOrders(date);
        if (datedOrders.size() == 0) {
            throw new NoOrdersFoundException("No orders for this date were found");
        } else {
            return datedOrders;
        }
    }

    public Order getOrder(Integer orderNumber) {
        return dao.getOrder(orderNumber);
    }

    public Order fillInAndAddOrder(Order order) throws DaoFileAccessException, AuditorFileAccessException {
        orderCalcs(order);
        Order added = dao.addOrder(order);
        auditor.writeAddEntry(order);
        return added;
    }

    public Order removeOrder(Integer orderNumber) throws DaoFileAccessException, AuditorFileAccessException {
        auditor.writeRemoveEntry(dao.getOrder(orderNumber));
        return dao.removeOrder(orderNumber);
    }

    public Order editOrderState(String s, Integer orderNum) throws AuditorFileAccessException {
        auditor.writeEditEntry(dao.getOrder(orderNum));
        Order order = dao.editState(s, dao.getOrder(orderNum));
        orderCalcs(order);
        return order;
    }

    public Order editOrderCustomer(String s, Integer orderNum) throws AuditorFileAccessException {
        auditor.writeEditEntry(dao.getOrder(orderNum));
        return dao.editOrderCustomer(s, dao.getOrder(orderNum));
    }

    public Order editOrderProduct(String s, Integer orderNum) throws AuditorFileAccessException {
        auditor.writeEditEntry(dao.getOrder(orderNum));
        Order order = dao.editProduct(s, dao.getOrder(orderNum));
        orderCalcs(order);
        return order;
    }

    public Order editOrderArea(BigDecimal area, Integer orderNum) throws AuditorFileAccessException {
        auditor.writeEditEntry(dao.getOrder(orderNum));
        Order order = dao.editArea(area, dao.getOrder(orderNum));
        orderCalcs(order);
        return order;
    }

    public void orderCalcs(Order order) {
        Tax state = dao.getState(order.getStateAbbr());
        Product product = dao.getProduct(order.getProductType());

        order.setTaxRate(state.getTaxRate());

        order.setCostPerSqFoot(product.getCostPerSquareFoot());
        order.setLaborPerSqFoot(product.getLaborCostPerSquareFoot());

        BigDecimal laborCost = order.getArea().multiply(order.getLaborPerSqFoot()).setScale(2, RoundingMode.HALF_UP);
        order.setLaborCost(laborCost);

        BigDecimal materialCost = order.getArea().multiply(order.getCostPerSqFoot()).setScale(2, RoundingMode.HALF_UP);
        order.setMaterialCost(materialCost);

        BigDecimal taxRate = order.getTaxRate().divide(new BigDecimal("100"), RoundingMode.HALF_UP);
        BigDecimal taxes = laborCost.add(materialCost).multiply(taxRate).setScale(2, RoundingMode.HALF_UP);
        order.setTax(taxes);

        order.setTotal(taxes.add(materialCost).add(laborCost));
    }

    public boolean isValidState(String s) {
        return dao.isValidState(s);
    }

    public boolean isValidProduct(String s) {
        return dao.isValidProduct(s);
    }

    public void redoDatedFile(LocalDate date) throws DaoFileAccessException {
        dao.redoDatedFile(date);
    }

    public List<Tax> getAllStates() {
        return dao.getAllStates();
    }

    public List<Product> getAllProducts() {
        return dao.getAllProducts();
    }

    public boolean isValidDate(LocalDate date) {
        LocalDate now = LocalDate.now();
        return date.isAfter(now);
    }
}
