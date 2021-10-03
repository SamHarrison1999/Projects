package com.company.LuckySevens;

import java.util.Random;

public class App {
    public void roll(int bet) {
        Random nr = new Random();
        int rolls = 0;
        int max = 0;
        int rollsAtMax = 0;
        do {
            int die1 = nr.nextInt (6) + 1;
            int die2 = nr.nextInt(6) + 1;
            rolls++;

            if ((die1 + die2) == 7) {
                bet += 4;
            }

            else {
                bet--;
            }

            if (bet >= max) {
                max = bet;
                rollsAtMax = rolls;
            }

        } while (bet > 0);

        System.out.println("You are broke after " + rolls + " rolls.");
        System.out.println("You should have quit after " + rollsAtMax + " when you had $" + max);
    }
}
