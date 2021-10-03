package Dao;

import Dto.DVD;
import java.util.List;

public interface DVDLibraryDao {
    /*
    Adds the given DVD to the library and associates it with the given
    DVD name. If there is already a DVD associated with the given
    DVD name it will return that DVD object, otherwise it will
    return null.
    @param dvdName name with which the DVD is to be associated
    @param DVD dvd to be added to the library
    @return the DVD object previously associated with the given
    DVD if it exists, null otherwise

    @throws DVDLibraryrDaoException
    */

    DVD addDVD(String dvdName, DVD dvd) throws DVDLibraryDaoException;

    /*
    Removes the given DVD from the library associated with the given DVD name.
    Return the DVD object that is being removed or null if there is not DVD associated
    with the given name.

    @param dvdName name of DVD to be removed
    @return DVD object that was removed or null if no DVD was associated with the
    given DVD name.

    @throws DVDLibraryrDaoException
    */

    DVD removeDVD(String dvdName) throws DVDLibraryDaoException;

    /*
    Returns a List of all DVDs in the library.
    @return DVD List containing all DVDs in the library.

    @throws DVDLibraryrDaoException
    */

    List<DVD> getAllDVDs() throws DVDLibraryDaoException;

    /*
    @param dvdName name of the DVD to retrieve
    @return the DVD object associated with the given dvdName,
    null if no such DVD exists

    @throws DVDLibraryrDaoException
    */

    DVD getDVD(String dvdName) throws DVDLibraryDaoException;

    /*
    @param dvd the DVD we want to change
    @param newReleaseDate the new title

    @throws DVDLibraryrDaoException
    */

    void editTitle(DVD dvd, String newTitle) throws DVDLibraryDaoException;

    /*
    @param dvd the DVD we want to change
    @param newReleaseDate the new release date

    @throws DVDLibraryrDaoException
    */

    void editReleaseDate(DVD dvd, String newReleaseDate) throws DVDLibraryDaoException;

    /*
    @param dvd the DVD we want to change
    @param newMPAARating the new MPAA rating.

    @throws DVDLibraryrDaoException
    */

    void editMPAARating(DVD dvd, String newMPAARating) throws DVDLibraryDaoException;

    /*
    @param dvd the DVD we want to change
    @param newDirector the new director

    @throws DVDLibraryrDaoException
    */

    void editDirector(DVD dvd, String newDirector) throws DVDLibraryDaoException;

    /*
    @param dvd the DVD we want to change
    @param newStudio the new studio

    @throws DVDLibraryrDaoException
    */

    void editStudio(DVD dvd, String newStudio) throws DVDLibraryDaoException;

    /*
    @param dvd the DVD we want to change
    @param newUserRating the new user rating

    @throws DVDLibraryrDaoException
    */

    void editUserRating(DVD dvd, String newUserRating) throws DVDLibraryDaoException;

}

