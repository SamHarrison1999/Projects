package com.company.AddressBook;

import com.company.AddressBook.controller.AddressBookController;
import com.company.AddressBook.dao.AddressBookDao;
import com.company.AddressBook.dao.AddressBookDaoFileImpl;
import com.company.AddressBook.ui.AddressBookView;
import com.company.AddressBook.ui.UserIO;
import com.company.AddressBook.ui.UserIOConsoleImpl;

public class App {

    public static void main(String[] args) {
        UserIO myIo = new UserIOConsoleImpl();
        AddressBookView myView = new AddressBookView(myIo);
        AddressBookDao myDao = new AddressBookDaoFileImpl();
        AddressBookController controller = new AddressBookController(myDao, myView);
        controller.run();
    }
}