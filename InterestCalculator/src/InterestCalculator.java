import static java.lang.Integer.parseInt;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Scanner;

public class InterestCalculator {
    static MathContext mc = new MathContext(2);

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        BigDecimal initialPrinciple;
        BigDecimal annualInterestRate;
        int years;
        String freq;

        // collects user input
        System.out.print("How much do you want to invest? ");
        initialPrinciple = new BigDecimal(input.nextLine());

        System.out.print("How many years are you investing? ");
        years = parseInt(input.nextLine());

        System.out.print("What is the annual interest rate % growth? ");
        annualInterestRate = new BigDecimal(input.nextLine());

        System.out.println("How often is this investment compounded?"
                + "\nA. Yearly"
                + "\nB. Quarterly"
                + "\nC. Monthly"
                + "\nD. Daily");
        freq = input.nextLine();

        System.out.println();
        System.out.println("\nCalculating...");

        printer(years, initialPrinciple, annualInterestRate, freq);
    }

    public static void printer(int year, BigDecimal balance, BigDecimal interestRate, String freq) {
        BigDecimal interest;
        int frequency = freqOfCompounder(freq);

        // prints the yearly breakdown
        for (int i = 1; i <= year; i++) {
            BigDecimal end;
            interest = compounder(interestRate, balance, frequency);
            end = balance.add(interest);

            System.out.println("Year " + i + ":");
            System.out.println("Began with $" + balance);
            System.out.println("Earned $" + interest);
            System.out.println("Ended with $" + end);
            System.out.println();
            balance = end;
        }
    }

    // does the compounding returns only the interest earned per year
    public static BigDecimal compounder(BigDecimal rate, BigDecimal balance, int frequency){
        BigDecimal interestTotal;
        BigDecimal initialBalance = balance;
        BigDecimal hundred = new BigDecimal("100");
        BigDecimal freq = new BigDecimal(frequency);

        for (int j = 0; j < frequency; j++) {
            BigDecimal interest = rate.divide(freq, mc);
            interest = interest.divide(hundred, mc);
            interest = interest.multiply(balance, mc);

            balance = balance.add(interest);
        }

        interestTotal = balance.subtract(initialBalance);

        return interestTotal;
    }

    // "translates" the user input into an interger
    public static int freqOfCompounder(String freq){
        int frequency = 0;
        freq = freq.toUpperCase();

        switch(freq){
            case "A" : frequency = 1;
                break;
            case "B" : frequency = 4;
                break;
            case "C" : frequency = 12;
                break;
            case "D" : frequency = 365;
                break;
            default:
                System.out.println("Please try again.");
                System.exit(0);
        }

        return frequency;
    }

}

