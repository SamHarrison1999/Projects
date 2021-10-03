package com.company.RockPaperScissors;

import java.util.Random;
import java.util.Scanner;

public class RockPaperScissorsRefactored {
    public static void main(String[] args) {
        final int MAX_ROUNDS = 10;
        final int MIN_ROUNDS = 1;
        Scanner sc = new Scanner(System.in);
        boolean playAgain = false;
        App app = new App();

        do {
            System.out.println("How many rounds would you like to play? (1-10) ");
            String sRoundCount = sc.nextLine();
            Random newR = new Random();
            int roundCount = Integer.parseInt(sRoundCount);
            int userPick = 0;
            int ties = 0;
            int userWins = 0;
            int computerWins = 0;


            if (roundCount <= MAX_ROUNDS && roundCount >= MIN_ROUNDS) {                  //Satisfies requirement that rounds is 1-10 only
                for(int currentRound = 0; currentRound < roundCount; currentRound++){    //Loops through rounds based on user input
                    System.out.println("Rock, Paper, or Scissors?");
                    String sUserPick = sc.nextLine();
                    int computerPick = newR.nextInt(3);   //computer random 0, 1, or 2   0=rock/1=paper/2=scissors

                    //Convert user input to int value for easier handling
                    if (sUserPick.equalsIgnoreCase("Rock")){
                        userPick  = 0;
                    }
                    else if (sUserPick.equalsIgnoreCase("Paper")) {
                        userPick = 1;
                    }
                    else {
                        userPick = 2;
                    }

                    if (userPick == computerPick) {                      //determines tie plus incrememnts int tie
                        System.out.println("The round was a tie!");
                        ties++;
                    }
                    else {
                        if (app.game(sUserPick, computerPick)) {                //If not a tie then string value of user input and computer random int goes into winner method
                            System.out.println("Congratulations, you won!");  //A true return from winner() will print and increment userWins
                            userWins++;
                        }
                        else {
                            System.out.println("The computer won!");          //False return from winner() prints and increments computerWins
                            computerWins++;
                        }
                    }
                }
            }

            //if user enters an invalid round count - print and end program
            else {
                System.out.println("Sorry, the game only allows between 1-10 rounds.  PLease restart to try again.");
                break;
            }

            //print totals for userWins/computerWins/ties
            System.out.println("\n\nYou won " + userWins + " rounds!");
            System.out.println("The computer won " + computerWins + " rounds.");
            System.out.println("There were " + ties + " ties!");

            //Determine who was the overall winner - tied otherwise
            if (userWins > computerWins) {
                System.out.println("Congratulations!  You are the overall winner!");
            }
            else if (computerWins > userWins) {
                System.out.println("The computer is the overall winner!");
            }
            else {
                System.out.println("You've tied with the computer overall!");
            }

            //Prompt user to play again - a "y" returns true and the do-while loop reruns
            System.out.println("Would you like to play again? (y/n) ");
            String answer = sc.nextLine();
            if (answer.equalsIgnoreCase("y")) {
                playAgain = true;
            }
            else {
                System.out.println("Thanks for playing!");
                playAgain = false;
            }
        } while (playAgain);
    }
}
