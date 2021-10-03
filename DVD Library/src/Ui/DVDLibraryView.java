package Ui;

import Dto.DVD;
import Dto.Rating;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.Set;


public class DVDLibraryView {
    private UserIO io;

    public DVDLibraryView(UserIO io) {
        this.io = io;
    }

    public int printMenuAndGetSelection() {
        io.print("Main Menu");
        io.print("1. List DVD's");
        io.print("2. Create New DVD");
        io.print("3. View a DVD");
        io.print("4. Remove a DVD");
        io.print("5. Edit a DVD");
        io.print("6. Get Average DVD Age");
        io.print("7. Get Newest DVD");
        io.print("8. Get Oldest DVD");
        io.print("9. Exit");

        return io.readInt("Please select from the above choices.", 1, 9);
    }

    public DVD getNewDVDInfo() {
        String title = io.readString("Please enter DVD name");
        String releaseDate = io.readString("Please enter release date");
        String MPAARating = io.readString("Please enter MPAA rating");
        String director = io.readString("Please enter director name");
        String studio = io.readString("Please enter studio");
        String userRating = io.readString("Please enter personal rating and comments");

        DVD currentDVD = new DVD(title);

        currentDVD.setReleaseDate(releaseDate);
        currentDVD.setMPAARating(MPAARating);
        currentDVD.setDirector(director);
        currentDVD.setStudio(studio);
        currentDVD.setUserRating(userRating);

        return currentDVD;
    }

    public void displayCreateDVDBanner() {
        io.print("~~~~ Create DVD ~~~~");
    }

    public void displayCreateSuccessBanner(){
        io.readString("DVD successfully created. Please hit enter to continue.");
    }

    public void displayDVDLibrary(List<DVD> dvdList){
        for(DVD currentDVD : dvdList) {
            String dvdInfo = String.format("%s (%s)",
                    currentDVD.getTitle(),
                    currentDVD.getReleaseDate());
            io.print(dvdInfo);
        }
        io.readString("Please hit enter to continue.");
    }

    public void displayAllBanner(){
        io.print("~~~~ Display All DVDs ~~~~");
    }

    public String getDVDTitleChoice() {
        return io.readString("Please enter the DVD title.");
    }

    public void displayDVD(DVD dvd, boolean prompt) {
        if(dvd != null) {
            io.print(dvd.getTitle() + " " + dvd.getReleaseDate());
            io.print("Directed by " + dvd.getDirector());
            io.print("Rated " + dvd.getMPAARating());
            io.print(dvd.getStudio());
            io.print("Notes: " + dvd.getUserRating());
            io.print("");
        } else {
            io.print("No such DVD. ");
        }

        if (prompt){
            io.readString("Please hit enter to continue.");
        }
    }

    public void displayRemoveDVDBanner () {
        io.print("~~~~ Remove DVD ~~~~");
    }

    public void displayRemoveResult(DVD selectedDVD) {
        if(selectedDVD != null) {
            io.print("DVD successfully removed.");
        } else {
            io.print("No such DVD,");
        }
        io.readString("Please hit enter to continue.");
    }

    public void displayEditDVDBanner() {
        io.print("~~~~ Edit DVD ~~~~");
    }

    public int printEditMenuAndGetSelection() {
        io.print("Edit Menu");
        io.print("1. Edit Title");
        io.print("2. Edit Release Date");
        io.print("3. Edit MPAA Rating");
        io.print("4. Edit Studio");
        io.print("5. Edit User Rating");
        io.print("6. Exit editing");

        return io.readInt("Please select from the above choices.", 1, 6);
    }

    public String getDVDEdit(String toEdit) {
        return io.readString("What would you like to change the " + toEdit + " to? ");
    }

    public String displayEditSuccessBanner() {
        return io.readString("DVD successfully edited. Please hit enter to continue.");
    }

    public void displayExitBanner() {
        io.print("GoodBye");
    }

    public void displayUnknownCommandBanner() {
        io.print("~~~~ Unknown Command ~~~~");
    }

    public void displayErrorMessage(String errorMsg) {
        io.print("~~~~ ERROR ~~~~");
        io.print(errorMsg);
    }

    public void createPause() {
        io.readString("Please hit enter to continue");
    }

    public int printListMenuGetSelection() {
        io.print("List DVD Menu");
        io.print("1. All DVD's");
        io.print("2. DVD's in last given years");
        io.print("3. DVD's with given MPAA Rating");
        io.print("4. DVD's with given the director");
        io.print("5. DVD's from given studio");

        return io.readInt("Please select from the above choices.", 1, 5);
    }

    public int getNYears() {
        return io.readInt("How many years back do you want DVD's from?");
    }

    public String getRating() {
        return io.readString("Which rating would you like to get DVD's by?");
    }

    public String getDirector() {
        return io.readString("Which director would you like to get DVD's by?");
    }

    public String getStudio() {
        return io.readString("Which studio would you like to get DVD's from?");
    }

    public void displayDVDWithDirector(Map<Rating, List<DVD>> library) {
        Set<Rating> rating = library.keySet();

        rating.forEach((s) -> {
            List<DVD> dvdList = library.get(s);
            io.print("DVD's with " + s + " MPAA Rating:");

            dvdList.stream()
                    .map((currentDVD) -> String.format("%s (%s)",
                            currentDVD.getTitle(),
                            currentDVD.getReleaseDate())).
                    forEachOrdered((dvdInfo) -> io.print(dvdInfo));
        });

        io.readString("Please hit enter to continue.");
    }

    public void displayAverageAge(OptionalDouble averageDVDAge) {
        io.print("Getting average age of DVD's in library...");
        io.print("Calculating...");
        io.print("The average age of the DVD's in the library is " + averageDVDAge);
    }

    public void displayNewestAge(DVD newestDVD) {
        io.print("~~~~ Get Newest DVD ~~~~");
        io.print("The newest DVD in the library is " + newestDVD + " released in " + newestDVD.getReleaseDate() + ".");
    }

    public void displayAverageAge(DVD oldestDVD) {
        io.print("~~~~ Get Oldest DVD ~~~~");
        io.print("The oldest DVD in the library is " + oldestDVD + " released in " + oldestDVD.getReleaseDate() + ".");
    }

}
