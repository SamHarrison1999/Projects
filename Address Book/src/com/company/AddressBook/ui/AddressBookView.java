package com.company.AddressBook.ui;

import com.company.AddressBook.dto.Address;

import java.util.List;

public class AddressBookView {

    private UserIO io;

    public AddressBookView(UserIO io) {
        this.io = io;
    }

    public int printMenuAndGetSelection() {
        io.print("Main Menu");
        io.print("1. List Addresses");
        io.print("2. Create New Address");
        io.print("3. View a Address");
        io.print("4. Remove a Address");
        io.print("5. Exit");

        return io.readInt("Please select from the above choices.", 1, 5);
    }

    public Address getNewAddressInfo() {
        String addressId = io.readString("Please enter Address ID");
        String firstName = io.readString("Please enter First Name");
        String lastName = io.readString("Please enter Last Name");
        String address = io.readString("Please enter Address");
        Address currentAddress = new Address(addressId);
        currentAddress.setFirstName(firstName);
        currentAddress.setLastName(lastName);
        currentAddress.setAddress(address);
        return currentAddress;
    }

    public void displayCreateAddressBanner() {
        io.print("=== Create Address ===");
    }

    public void displayCreateSuccessBanner() {
        io.readString(
                "Address successfully created.  Please hit enter to continue");
    }

    public void displayAddressList(List<Address> addressList) {
        for (Address currentAddress : addressList) {
            String addressInfo = String.format("#%s : %s %s",
                    currentAddress.getAddressId(),
                    currentAddress.getFirstName(),
                    currentAddress.getLastName());
            io.print(addressInfo);
        }
        io.readString("Please hit enter to continue.");
    }

    public void displayDisplayAllBanner() {
        io.print("=== Display All Addresses ===");
    }

    public void displayDisplayAddressBanner () {
        io.print("=== Display Address ===");
    }

    public String getAddressIdChoice() {
        return io.readString("Please enter the Address ID.");
    }

    public void displayAddress(Address address) {
        if (address != null) {
            io.print(address.getAddressId());
            io.print(address.getFirstName() + " " + address.getLastName());
            io.print(address.getAddress());
            io.print("");
        } else {
            io.print("No such address.");
        }
        io.readString("Please hit enter to continue.");
    }

    public void displayRemoveAddressBanner () {
        io.print("=== Remove Address ===");
    }

    public void displayRemoveResult(Address addressRecord) {
        if(addressRecord != null){
            io.print("Address successfully removed.");
        }else{
            io.print("No such address.");
        }
        io.readString("Please hit enter to continue.");
    }

    public void displayExitBanner() {
        io.print("Good Bye!!!");
    }

    public void displayUnknownCommandBanner() {
        io.print("Unknown Command!!!");
    }

    public void displayErrorMessage(String errorMsg) {
        io.print("=== ERROR ===");
        io.print(errorMsg);
    }
}
