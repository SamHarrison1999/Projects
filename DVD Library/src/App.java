import Controller.DVDLibraryController;
import Dao.DVDLibraryDao;
import Dao.DVDLibraryDaoException;
import Dao.DVDLibraryDaoFileImpl;
import Ui.DVDLibraryView;
import Ui.UserIO;
import Ui.UserIOConsoleImpl;

public class App {
    public static void main(String[] args) throws DVDLibraryDaoException {
        UserIO myIO = new UserIOConsoleImpl();
        DVDLibraryView myView = new DVDLibraryView(myIO);
        DVDLibraryDao myDao = new DVDLibraryDaoFileImpl();
        DVDLibraryController controller = new DVDLibraryController(myDao, myView);
        controller.run();
    }
}
