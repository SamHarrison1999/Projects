import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class BirthDAYCalculator {

    public static void main(String[] args) {
        // Initialize scanner named sc
        Scanner sc = new Scanner(System.in);

        // Initialize date variable and set it to today's date
        LocalDate localDate = LocalDate.now();

        // format date to mm/dd/yyyy
        String formattedLocalDate = localDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));

        System.out.println("Welcome to the Magical BirthDAY Calculator!");
        System.out.println("What's your birthday? (yyyy-MM-dd)");

        // Stores value for users birthday
        String birthdayString = sc.nextLine();

        // Converts birthday string into a LocalDate object
        LocalDate birthday = LocalDate.parse(birthdayString);

        // Returns day user was born
        System.out.println("That means that you were born on a "+birthday.getDayOfWeek());

        LocalDate nextBirthday = birthday.withYear(localDate.getYear());

        // Changes the year in case the birthday has already passed.
        if (nextBirthday.isBefore(localDate) || nextBirthday.isEqual(localDate)) {
            nextBirthday = nextBirthday.plusYears(1);
        }

        System.out.println("This year your birthday falls on a "+ nextBirthday.getDayOfWeek()+".");
        System.out.println("And since today is "+formattedLocalDate+",");

        Period p = Period.between(localDate, nextBirthday);
        long p2 = ChronoUnit.DAYS.between(localDate, nextBirthday);
        Period diff = birthday.until(nextBirthday);
        int years=diff.getYears();
        System.out.println("there are " + p.getMonths() + " months, and " +
                p.getDays() + " days until your next birthday. (" +
                p2 + " days until you turn "+years+".)");

    }

}
