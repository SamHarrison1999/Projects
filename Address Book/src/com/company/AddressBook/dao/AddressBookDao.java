package com.company.AddressBook.dao;

import com.company.AddressBook.dto.Address;

import java.util.List;

public interface AddressBookDao {
    /**
     * Adds the given Address to the roster and associates it with the given
     * address id. If there is already a address associated with the given
     * address id it will return that address object, otherwise it will
     * return null.
     *
     * @param addressId id with which address is to be associated
     * @param address address to be added to the address book
     * @return the Address object previously associated with the given
     * address id if it exists, null otherwise
     * @throws AddressBookDaoException
     */
    Address addAddress(String addressId, Address address)
            throws AddressBookDaoException;

    /**
     * Returns a List of all Addresses on the address book.
     *
     * @return Address List containing all addresses on the address book.
     * @throws AddressBookDaoException
     */
    List<Address> getAllAddresses()
            throws AddressBookDaoException;

    /**
     * Returns the address object associated with the given address id.
     * Returns null if no such address exists
     *
     * @param addressId ID of the address to retrieve
     * @return the Address object associated with the given address id,
     * null if no such address exists
     * @throws AddressBookDaoException
     */
    Address getAddress(String addressId)
            throws AddressBookDaoException;

    /**
     * Removes from the address the person associated with the given id.
     * Returns the address object that is being removed or null if
     * there is no address associated with the given id
     *
     * @param addressId id of address to be removed
     * @return Address object that was removed or null if no address
     * was associated with the given address id
     * @throws AddressBookDaoException
     */
    Address removeAddress(String addressId)
            throws AddressBookDaoException;
}