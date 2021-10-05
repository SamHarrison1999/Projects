import java.util.Scanner;

import static java.lang.Math.abs;

public class Bank {

    public static void main(String[] args) {
        BankAccount bankAccount = new BankAccount("Test Customer", 1);
        bankAccount.showMenu();
    }

}

class BankAccount {
    int balance, previousTransaction, customerId;
    String customerName;

    BankAccount(String customerName, int customerId){
        this.customerName = customerName;
        this.customerId = customerId;
    }

    void deposit(int amount) {
        if (amount != 0) {
            balance += amount;
            previousTransaction = amount;
        }
    }

    void withdraw(int amount) {
        if (amount != 0) {
            balance -= amount;
            previousTransaction = -amount;
        }
    }

    void getPreviousTransaction(){
        if (previousTransaction > 0) {
            System.out.println("Deposited: " + previousTransaction);
        } else if (previousTransaction < 0) {
            System.out.println("Withdrawn: " + abs(previousTransaction));
        } else {
            System.out.println("No transaction occurred");
        }
    }

    void showMenu(){
        int option;
        int amount;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome " + customerName);
        System.out.println("You Customer ID is " + customerId);
        System.out.println();
        System.out.println("1. Check Balance");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Previous transaction");
        System.out.println("5. Exit ");

        do {
            System.out.println("Enter an option");
            option = scanner.nextInt();
            System.out.println();

            switch (option) {
                case 1 -> {
                    System.out.println("Balance = " + balance);
                    System.out.println();
                }
                case 2 -> {
                    System.out.println("Enter an amount to deposit");
                    System.out.println();
                    amount = scanner.nextInt();
                    deposit(amount);
                    System.out.println();
                    System.out.println("Current Balance = " + balance);
                    System.out.println();
                }
                case 3 -> {
                    System.out.println("Enter an amount to withdraw");
                    System.out.println();
                    amount = scanner.nextInt();
                    withdraw(amount);
                    System.out.println();
                    System.out.println("Current Balance = " + balance);
                    System.out.println();
                }
                case 4 -> {
                    getPreviousTransaction();
                    System.out.println();
                }
                case 5 -> System.out.println();
                default -> System.out.println("Invalid Option! Please enter again");
            }
        }while (option != 5);
        System.out.println("Thanks for using our services");
        System.exit(0);
    }

}
