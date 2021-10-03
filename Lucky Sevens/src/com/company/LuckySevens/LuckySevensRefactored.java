package com.company.LuckySevens;

import java.util.Scanner;

public class LuckySevensRefactored {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("How many dollars do you have to bet? ");
        int bet = sc.nextInt();

        App app = new App();

        app.roll(bet);
    }
}
