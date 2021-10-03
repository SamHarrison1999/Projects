package com.sg.flooring.controller;

import com.sg.flooring.dao.AuditorFileAccessException;
import com.sg.flooring.dao.DaoFileAccessException;
import com.sg.flooring.service.NoOrdersFoundException;
import com.sg.flooring.dto.Order;
import com.sg.flooring.service.FlooringMasteryServiceLayer;
import com.sg.flooring.ui.FlooringMasteryView;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


public class FlooringMasteryController {
    
    FlooringMasteryServiceLayer flooringMasteryService;
    FlooringMasteryView flooringMasteryView;
    
    FlooringMasteryController() {
        try {
            flooringMasteryService = new FlooringMasteryServiceLayer();
            flooringMasteryView = new FlooringMasteryView();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public FlooringMasteryController(FlooringMasteryServiceLayer flooringMasteryService, FlooringMasteryView flooringMasteryView) {
        this.flooringMasteryService = flooringMasteryService;
        this.flooringMasteryView = flooringMasteryView;
    }

    public void run() {
        boolean loop = true;
        int choice;
        while (loop) {
            choice = flooringMasteryView.printMenuGetSelection();
            switch (choice) {
                case 1:
                    LocalDate date = flooringMasteryView.getDate();
                    try {
                        List<Order> toDisplay = flooringMasteryService.getDatedOrders(date);
                        flooringMasteryView.displayOrders(toDisplay);
                    } catch (NoOrdersFoundException e) {
                        flooringMasteryView.displayError(e);
                    }
                    break;
                case 2:
                    Order toAdd = new Order();

                    setValidOrderDate(toAdd);
                    setValidCustomerName(toAdd);
                    setValidTaxState(toAdd);
                    setValidProductType(toAdd);
                    setValidArea(toAdd);
                    flooringMasteryService.orderCalcs(toAdd);
                    try {
                        flooringMasteryService.fillInAndAddOrder(toAdd);
                        flooringMasteryView.displayOrder(toAdd);
                        flooringMasteryView.displaySuccess("Your order has been added!\n");

                    } catch (DaoFileAccessException | AuditorFileAccessException e) {
                        flooringMasteryView.displayError(e);
                    }
                    break;
                case 3:
                    boolean editLoop = true;
                    int editChoice;
                    int editOrderNum = flooringMasteryView.parseOrderNumber();
                    LocalDate editOrderDate = flooringMasteryView.getDate();
                    Order editedOrder = flooringMasteryService.getOrder(editOrderNum);
                    if (editedOrder == null) {
                        flooringMasteryView.parseString("No such order exists\nPress enter to continue");
                        return;
                    } else {
                        String oldCustomerName = editedOrder.getCustomerName();
                        String oldState = editedOrder.getStateAbbr();
                        String oldProduct = editedOrder.getProductType();
                        BigDecimal oldArea = editedOrder.getArea();
                        editCustomerName(editedOrder);
                        editState(editedOrder);
                        editProductType(editedOrder);
                        editArea(editedOrder);

                        if (flooringMasteryView.verifyOrderEdit(editedOrder)) {
                            try {
                                flooringMasteryService.exportData();
                                flooringMasteryService.redoDatedFile(editedOrder.getOrderDate());
                            } catch (DaoFileAccessException e) {
                                flooringMasteryView.displayError(e);
                            }
                        } else {
                            editedOrder.setCustomerName(oldCustomerName);
                            editedOrder.setStateAbbr(oldState);
                            editedOrder.setProductType(oldProduct);
                            editedOrder.setArea(oldArea);
                        }

                    }
                    break;
                case 4:
                    int orderNum = flooringMasteryView.parseOrderNumber();
                    LocalDate orderDate = flooringMasteryView.getDate();
                    Order theOrder = flooringMasteryService.getOrder(orderNum);

                    boolean toDrop = flooringMasteryView.confirmDrop(theOrder);

                    if (toDrop) {
                        try {
                            flooringMasteryService.removeOrder(orderNum);
                        } catch (DaoFileAccessException|AuditorFileAccessException e) {
                            flooringMasteryView.displayError(e);
                        }
                    } else {
                        flooringMasteryView.parseString("No changes were made.\nPress enter to continue");
                    }
                    break;
                case 5:
                    try {
                        flooringMasteryService.exportData();
                    } catch (DaoFileAccessException e) {
                        flooringMasteryView.displayError(e);
                    }
                    break;
                case 6:
                    loop = false;
                    flooringMasteryView.displayGoodBye();
                    break;
                default:
                    flooringMasteryView.displayUnkownCommandBanner();
            }
        }
        try {
            flooringMasteryService.exportData();
        } catch (DaoFileAccessException e) {
            flooringMasteryView.displayError(e);
        }
    }

    
    private void setValidArea(Order order) {
        flooringMasteryView.displayRequestAreaPrompt();
        boolean validArea = false;
        BigDecimal area;
        while (!validArea) {
            area = flooringMasteryView.parseBigDecimal();
            validArea = (area.compareTo(BigDecimal.ZERO) != -1);
            if (validArea) {
                order.setArea(area);
            }
        }
        
    }
    
    private void setValidProductType(Order toAdd) {
        boolean isValidProduct = false;
        while (!isValidProduct) {
            flooringMasteryView.displayProductSelectionBanner();
            flooringMasteryView.displayAllProducts(flooringMasteryService.getAllProducts());
            
            String productType = flooringMasteryView.parseString("Please enter the product you'd like now");
            isValidProduct = flooringMasteryService.isValidProduct(productType);
            
            if (isValidProduct) {
                toAdd.setProductType(productType);
                flooringMasteryView.displaySuccess("That is a valid product");
            } else {
                flooringMasteryView.displayBadProductBanner();
            }            
            
        }
    }
    
    private void setValidOrderDate(Order order) {
        LocalDate orderDate = flooringMasteryView.getNewOrderDate();
        while (!flooringMasteryService.isValidDate(orderDate)) {
            orderDate = flooringMasteryView.getNewOrderDate();
        }
        order.setOrderDate(orderDate);
    }

    private void setValidCustomerName(Order toAdd) {
        String customerName = "";
        while (customerName.length() == 0) {
            customerName = flooringMasteryView.parseString("Please enter the customer's name, it must not be left blank");
        }
        toAdd.setCustomerName(customerName);
        flooringMasteryView.displaySuccess("Customer name successfully chosen");
    }
    
    private void setValidTaxState(Order toAdd) {
        boolean isValidState = false;
        while (!isValidState) {
            flooringMasteryView.displayStateSelectionBanner();
            flooringMasteryView.displayStates(flooringMasteryService.getAllStates());
            
            String stateAbbr = flooringMasteryView.parseString("");
            isValidState = flooringMasteryService.isValidState(stateAbbr);
            
            if (isValidState) {
                toAdd.setStateAbbr(stateAbbr);
                flooringMasteryView.displaySuccess("That is a good state!");
            } else {
                flooringMasteryView.displayBadStateBanner();
            }            
            
        }
    }

    
    private void editCustomerName(Order theOrder) {
        String s = flooringMasteryView.getEditCustomerName(theOrder);
        if (s.length() == 0) {
            return;
        } else {
            theOrder.setCustomerName(s);
        }
    }
    
    private void editState(Order theOrder) {
        String newState;
        newState = flooringMasteryView.getEditState(theOrder, flooringMasteryService.getAllStates());
        
        if (newState.length() == 0) {
            return;
        } else {
            if (flooringMasteryService.isValidState(newState)) {
                theOrder.setStateAbbr(newState);
                flooringMasteryView.displaySuccess("Edit successful");
            } else {
                flooringMasteryView.displayInvalidInputBanner();
                editState(theOrder);
            }
        }
    }

    private void editProductType(Order theOrder) {
        String newProduct;
        newProduct = flooringMasteryView.getEditProduct(theOrder, flooringMasteryService.getAllProducts());
        
        if (newProduct.length() == 0) {
            return;
        } else {
            if (flooringMasteryService.isValidProduct(newProduct)) {
                theOrder.setProductType(newProduct);
                flooringMasteryView.displaySuccess("Edit successful");
            } else {
                flooringMasteryView.displayInvalidInputBanner();
                editProductType(theOrder);
            }
        }
    }
    
    private void editArea(Order theOrder) {
        String s;
        s = flooringMasteryView.getEditArea(theOrder);
        BigDecimal newArea;
        boolean validArea;
        if (s.length() == 0) {
            return;
        } else {
            try {
                newArea = new BigDecimal(s);
                validArea = (newArea.compareTo(BigDecimal.ZERO) != -1);
                if (validArea) {
                    theOrder.setArea(newArea);
                } else {
                    flooringMasteryView.displayInvalidInputBanner();
                    editArea(theOrder);
                }
            } catch (NumberFormatException e) {
                flooringMasteryView.displayInvalidInputBanner();
                editArea(theOrder);
            }
        }
    }
    
}
