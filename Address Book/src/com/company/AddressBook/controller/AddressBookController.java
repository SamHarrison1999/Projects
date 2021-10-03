package com.company.AddressBook.controller;

import com.company.AddressBook.dao.AddressBookDao;
import com.company.AddressBook.dao.AddressBookDaoException;
import com.company.AddressBook.dto.Address;
import com.company.AddressBook.ui.AddressBookView;
import com.company.AddressBook.ui.UserIO;
import com.company.AddressBook.ui.UserIOConsoleImpl;

import java.util.List;

public class AddressBookController {

    private AddressBookView view;
    private AddressBookDao dao;
    private UserIO io = new UserIOConsoleImpl();

    public AddressBookController(AddressBookDao dao, AddressBookView view) {
        this.dao = dao;
        this.view = view;
    }

    public void run() {
        boolean keepGoing = true;
        int menuSelection = 0;
        try {
            while (keepGoing) {

                menuSelection = getMenuSelection();

                switch (menuSelection) {
                    case 1:
                        listAddresses();
                        break;
                    case 2:
                        createAddress();
                        break;
                    case 3:
                        viewAddress();
                        break;
                    case 4:
                        removeAddress();
                        break;
                    case 5:
                        keepGoing = false;
                        break;
                    default:
                        unknownCommand();
                }

            }
            exitMessage();
        } catch (AddressBookDaoException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }

    private int getMenuSelection() {
        return view.printMenuAndGetSelection();
    }

    private void createAddress() throws AddressBookDaoException {
        view.displayCreateAddressBanner();
        Address newAddress = view.getNewAddressInfo();
        dao.addAddress(newAddress.getAddressId(), newAddress);
        view.displayCreateSuccessBanner();
    }

    private void listAddresses() throws AddressBookDaoException {
        view.displayDisplayAllBanner();
        List<Address> addressList = dao.getAllAddresses();
        view.displayAddressList(addressList);
    }

    private void viewAddress() throws AddressBookDaoException {
        view.displayDisplayAddressBanner();
        String addressId = view.getAddressIdChoice();
        Address address = dao.getAddress(addressId);
        view.displayAddress(address);
    }

    private void removeAddress() throws AddressBookDaoException {
        view.displayRemoveAddressBanner();
        String addressId = view.getAddressIdChoice();
        Address removedAddress = dao.removeAddress(addressId);
        view.displayRemoveResult(removedAddress);
    }

    private void unknownCommand() {
        view.displayUnknownCommandBanner();
    }

    private void exitMessage() {
        view.displayExitBanner();
    }


}