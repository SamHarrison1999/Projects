package Dao;

import java.util.List;
import Dto.DVD;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DVDLibraryDaoFileImpl implements DVDLibraryDao{
    private Map<String, DVD> dvds = new HashMap<>();
    public static final String LIBRARY_FILE = "library.txt";
    public static final String DELIMITER = "::";

    @Override
    public DVD addDVD(String dvdName, DVD dvd) throws DVDLibraryDaoException {
        loadLibrary();
        DVD newDVD = dvds.put(dvdName, dvd);
        writeLibrary();
        return newDVD;
    }

    @Override
    public List<DVD> getAllDVDs() throws DVDLibraryDaoException {
        loadLibrary();
        return new ArrayList<DVD>(dvds.values());
    }

    @Override
    public DVD getDVD(String dvdName) throws DVDLibraryDaoException {
        loadLibrary();
        return dvds.get(dvdName);
    }

    @Override
    public DVD removeDVD(String dvdName) throws DVDLibraryDaoException {
        loadLibrary();
        DVD removedDVD = dvds.remove(dvdName);
        writeLibrary();
        return removedDVD;
    }

    @Override
    public void editTitle(DVD dvd, String newTitle) throws DVDLibraryDaoException{
        loadLibrary();
        dvds.remove(dvd.getTitle());
        dvd.setTitle(newTitle);
        dvds.put(newTitle, dvd);
        writeLibrary();
    }

    @Override
    public void editReleaseDate(DVD dvd, String newReleaseDate) throws DVDLibraryDaoException {
        dvd.setReleaseDate(newReleaseDate);
        writeLibrary();
    }

    @Override
    public void editMPAARating(DVD dvd, String newMPAARating) throws DVDLibraryDaoException {
        dvd.setMPAARating(newMPAARating);
        writeLibrary();
    }

    @Override
    public void editDirector(DVD dvd, String newDirector) throws DVDLibraryDaoException {
        dvd.setDirector(newDirector);
        writeLibrary();
    }

    @Override
    public void editStudio(DVD dvd, String newStudio) throws DVDLibraryDaoException {
        dvd.setStudio(newStudio);
        writeLibrary();
    }

    @Override
    public void editUserRating(DVD dvd, String newUserRating) throws DVDLibraryDaoException {
        dvd.setUserRating(newUserRating);
        writeLibrary();
    }

    private DVD unmarshallDVD(String dvdAsText) {
        // splits string into an arrary of strings split at delimiter
        String[] dvdTokens = dvdAsText.split(DELIMITER);
        // first it the ID, next the first name, then last name and finally the cohort
        String dvdTitle = dvdTokens[0];
        DVD dvdFromFile = new DVD(dvdTitle);
        dvdFromFile.setReleaseDate(dvdTokens[1]);
        dvdFromFile.setMPAARating(dvdTokens[2]);
        dvdFromFile.setDirector(dvdTokens[3]);
        dvdFromFile.setStudio(dvdTokens[4]);
        dvdFromFile.setUserRating(dvdTokens[5]);

        return dvdFromFile;
    }

    private void loadLibrary() throws DVDLibraryDaoException {
        Scanner scanner;

        try {
            scanner = new Scanner(new BufferedReader(new FileReader(LIBRARY_FILE)));
        } catch(FileNotFoundException e) {
            throw new DVDLibraryDaoException("-_- Could not load library data into memory.", e);
        }

        String currentLine;
        DVD currentDVD;

        while(scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentDVD = unmarshallDVD(currentLine);
            dvds.put(currentDVD.getTitle(), currentDVD);
        }
        // close scanner
        scanner.close();
    }

    private String marshallDVD(DVD aDVD) {
        String dvdAsText = aDVD.getTitle() + DELIMITER;
        dvdAsText += aDVD.getReleaseDate() + DELIMITER;
        dvdAsText += aDVD.getMPAARating() + DELIMITER;
        dvdAsText += aDVD.getDirector() + DELIMITER;
        dvdAsText += aDVD.getStudio() + DELIMITER;
        dvdAsText += aDVD.getUserRating();

        return dvdAsText;
    }

    /*
    Write all dvds in the library out to a LIBRARY_FILE.

    @throws DVDLibraryDaoException if an error occurs writing to the file
    */
    private void writeLibrary() throws DVDLibraryDaoException {
        PrintWriter out;

        try {
            out = new PrintWriter(new FileWriter(LIBRARY_FILE));
        } catch (IOException e) {
            throw new DVDLibraryDaoException("Could not save student data.", e);
        }
        String dvdAsText;
        List<DVD> dvdList = new ArrayList(dvds.values());

        for(DVD currentDVD : dvdList) {
            dvdAsText = marshallDVD(currentDVD);
            out.println(dvdAsText);
            out.flush();
        }

        out.close();
    }
}
