import java.util.Random;
import java.util.Scanner;

public class GuessMeFinally {
    public static void main(String[] args) {
        int num, userNum, counter = 0;

        Random rand = new Random();
        Scanner sc = new Scanner(System.in);

        System.out.println("I've chosen a number bet -100 and 100. Betcha can't guess it.");
        num = rand.nextInt(201) - 100;
        System.out.println(num);
        System.out.println("Please guess the number: ");
        boolean guessed = true;

        while (guessed) {
            userNum = sc.nextInt();
            if (userNum > num) {
                System.out.println("Ha! Nice try -- too high. Try a smaller number.");
            } else if (userNum < num) {
                System.out.println("Ha! Nice try -- too low. Try a higher number");

            } else {
                if (counter == 0) {
                    System.out.println("Wow! Nice guess. That was it!");
                    break;
                } else if (counter > 0) {
                    System.out.println("Finally! It's about time you got it!");
                    break;
                }
            }
            counter++;

        }

    }
}
