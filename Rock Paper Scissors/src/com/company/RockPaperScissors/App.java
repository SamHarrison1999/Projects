package com.company.RockPaperScissors;

public class App {
    public boolean game(String user, int computer) {
        int userPick = 0;
        boolean winner = false;
        if (user.equalsIgnoreCase("Rock")) {
            if (computer == 2) {          //Rock beats scissors return true
                winner = true;
            }
        }
        else if (user.equalsIgnoreCase("Paper")) {
            if (computer == 0) {         //Paper beats rock return true
                winner = true;
            }
        }
        else if (user.equalsIgnoreCase("Scissors")) {
            if (computer == 1) {
                winner = true;           //Scissors beats paper return true
            }
        }                                //Otherwise winner returns false
        return winner;
    }
}
