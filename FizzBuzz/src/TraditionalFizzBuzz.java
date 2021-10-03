import java.util.Scanner;

public class TraditionalFizzBuzz {
    public static void main(String[] args) {

        int count = 0, userNum;
        Scanner sc = new Scanner(System.in);
        System.out.print("How many units of fizzing and buzzing do you need in your life?");
        userNum = Integer.parseInt(sc.nextLine());

        for (int i = 0; count <= userNum; i++) {
            if (i > 0) {
                if ((i % 3 == 0) && (i % 5 == 0)) {
                    System.out.println("Fizz Buzz");
                    count++;

                } else if (i % 3 == 0) {
                    System.out.println("Fizz");
                    count++;
                } else if (i % 5 == 0) {
                    System.out.println("Buzz");
                    count++;
                } else {
                    System.out.println(i);

                }
            } else {
                System.out.println(i);
                count++;
            }

        }
        System.out.println("TRADITION!!!!");

    }
}