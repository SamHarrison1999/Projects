package Controller;

import Dao.*;
import Dto.*;
import Ui.*;
import java.time.Year;
import java.util.*;
import java.util.stream.*;
import static java.lang.Integer.parseInt;

public class DVDLibraryController {
    private UserIO io = new UserIOConsoleImpl();
    private DVDLibraryView view;
    private DVDLibraryDao dao;

    public DVDLibraryController(DVDLibraryDao dao, DVDLibraryView view) {
        this.dao = dao;
        this.view = view;
    }

    public void run() throws DVDLibraryDaoException {
        boolean keepGoing = true;
        int menuSelection = 0;

        try {
            while (keepGoing) {

                menuSelection = getMenuSelection();

                switch (menuSelection) {
                    case 1: listDVDs();
                        break;
                    case 2: createDVD();
                        break;
                    case 3: viewDVD();
                        break;
                    case 4: removeDVD();
                        break;
                    case 5: editDVD();
                        break;
                    case 6: averageDVDAge();
                        break;
                    case 7: newestDVD();
                        break;
                    case 8: oldestDVD();
                        break;
                    case 9: keepGoing = false;
                        break;
                    default: unknownCommand();
                }
            }
            exitMessage();
        } catch (DVDLibraryDaoException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }

    private int getMenuSelection() {
        return view.printMenuAndGetSelection();
    }

    private void createDVD() throws DVDLibraryDaoException {
        view.displayCreateDVDBanner();
        DVD newDVD = view.getNewDVDInfo();
        dao.addDVD(newDVD.getTitle(), newDVD);
        view.displayCreateSuccessBanner();
    }

    private void listDVDs() throws DVDLibraryDaoException {
        int menuSelection = view.printListMenuGetSelection();
        List<DVD> dvdLibrary = dao.getAllDVDs();
        Map<Rating, List<DVD>> library = null;
        boolean arrayArrays = false;

        switch (menuSelection) {
            case 1: // all DVD's
                break;
            case 2: dvdLibrary = dvdsLastNYears(dvdLibrary);
                break;
            case 3: dvdLibrary = dvdsWithRating(dvdLibrary);
                break;
            case 4: library = dvdsWithDirector(dvdLibrary);
                arrayArrays = true;
                break;
            case 5: dvdLibrary = dvdFromStudio(dvdLibrary);
                break;
        }

        if (arrayArrays) {
            view.displayDVDWithDirector(library);
        } else {
            view.displayDVDLibrary(dvdLibrary);
        }
    }

    private void viewDVD() throws DVDLibraryDaoException {
        String dvdChoice = view.getDVDTitleChoice();
        DVD chosenDVD = dao.getDVD(dvdChoice);
        view.displayDVD(chosenDVD, true);
    }

    private void removeDVD() throws DVDLibraryDaoException {
        view.displayRemoveDVDBanner();
        String dvdTitle = view.getDVDTitleChoice();
        DVD removedDVD = dao.removeDVD(dvdTitle);
        view.displayRemoveResult(removedDVD);
    }

    private void editDVD() throws DVDLibraryDaoException {
        view.displayEditDVDBanner();
        String dvdTitleToEdit = view.getDVDTitleChoice();
        DVD dvdToEdit = dao.getDVD(dvdTitleToEdit);
        view.displayDVD(dvdToEdit, false);

        if (dvdToEdit != null) {
            boolean keepGoing = true;
            String edit;

            try {
                while(keepGoing) {
                    int menuSelection = view.printEditMenuAndGetSelection();
                    switch(menuSelection) {
                        case 1:
                            // Special case. The title is the key
                            // we remove the key from the Map and create a new key and value
                            // v0 has it here, v1 has it in the DAO where it should be.
                            edit = view.getDVDEdit("title");
                            dao.editTitle(dvdToEdit, edit);
                            break;
                        case 2:
                            edit = view.getDVDEdit("release date");
                            dao.editReleaseDate(dvdToEdit, edit);
                            break;
                        case 3:
                            edit = view.getDVDEdit("MPAA Rating");
                            dao.editMPAARating(dvdToEdit, edit);
                            break;
                        case 4:
                            edit = view.getDVDEdit("studio");
                            dao.editStudio(dvdToEdit, edit);
                            break;
                        case 5:
                            edit = view.getDVDEdit("user rating");
                            dao.editUserRating(dvdToEdit, edit);
                            break;
                        case 6:
                            keepGoing = false;
                            break;
                        default: unknownCommand();
                    }
                    view.displayDVD(dvdToEdit, false);
                }
            } catch (DVDLibraryDaoException e) {
                throw new DVDLibraryDaoException("Could not make edit.", e);
            }
        } else {
            view.createPause();
        }
        view.displayEditSuccessBanner();
    }

    private void unknownCommand() {
        view.displayUnknownCommandBanner();
    }

    private void exitMessage() {
        view.displayExitBanner();
    }

    private List dvdsLastNYears(List<DVD> dvdLibrary) {
        int years = view.getNYears();
        Year currentYear = Year.now();
        int today = currentYear.getValue();
        int yearOfInterest = today - years;

        dvdLibrary = dvdLibrary.stream()
                .filter((d) -> parseInt(d.getReleaseDate()) >= yearOfInterest)
                .collect(Collectors.toList());
        return dvdLibrary;
    }

    private List<DVD> dvdsWithRating(List<DVD> dvdLibrary) {
        String rating = view.getRating();

        dvdLibrary = dvdLibrary.stream()
                .filter((d) -> d.getReleaseDate().equals(rating))
                .collect(Collectors.toList());
        return dvdLibrary;
    }

    private List<DVD> dvdFromStudio(List<DVD> dvdLibrary) {
        String studio = view.getStudio();

        dvdLibrary = dvdLibrary.stream()
                .filter((d) -> d.getDirector().contains(studio))
                .collect(Collectors.toList());
        return dvdLibrary;
    }

    private Map<Rating, List<DVD>> dvdsWithDirector(List<DVD> dvdLibrary) {
        String director = view.getDirector();

        Map<Rating, List<DVD>> library= dvdLibrary.stream()
                .filter((d) -> d.getDirector().contains(director))
                .collect(Collectors.groupingBy(DVD::getMPAARating));
        return library;
    }

    private void averageDVDAge() throws DVDLibraryDaoException {
        List<DVD> dvds = dao.getAllDVDs();

        OptionalDouble averageDVDAge = dvds.stream()
                .mapToInt((d) -> parseInt(d.getReleaseDate()))
                .average();

        view.displayAverageAge(averageDVDAge);
    }

    private void newestDVD() throws DVDLibraryDaoException {
        List<DVD> dvds = dao.getAllDVDs();
        int newestReleaseDate = 0;
        DVD newestDVD = null;

        for(DVD d: dvds){
            if(parseInt(d.getReleaseDate()) > newestReleaseDate){
                newestDVD = d;
            }
        }

        view.displayNewestAge(newestDVD);
    }

    private void oldestDVD() throws DVDLibraryDaoException {
        List<DVD> dvds = dao.getAllDVDs();
        DVD oldestDVD = null;
        Year today = Year.now();
        int oldestReleaseDate = today.getValue();

        for(DVD d: dvds){
            if(parseInt(d.getReleaseDate()) < oldestReleaseDate){
                oldestDVD = d;
            }
        }

        view.displayAverageAge(oldestDVD);
    }

}
