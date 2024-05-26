import java.util.Random;
import java.util.Scanner;

public class NumberGuessingGame {

    private static final int MAX_ATTEMPTS = 10;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean playAgain = true;

        System.out.println("Welcome to the Number Guessing Game!");

        while (playAgain) {
            playGame(scanner);
            System.out.print("Do you want to play again? (yes/no): ");
            String response = scanner.next();
            playAgain = response.equalsIgnoreCase("yes");
        }

        System.out.println("Thanks for playing!");
        scanner.close();
    }

    private static void playGame(Scanner scanner) {
        Random random = new Random();
        int numberToGuess = random.nextInt(100) + 1;
        int attempts = 0;
        boolean hasWon = false;

        System.out.println("I have generated a random number between 1 and 100. Try to guess it!");

        while (attempts < MAX_ATTEMPTS && !hasWon) {
            System.out.print("Enter your guess: ");
            int userGuess = scanner.nextInt();
            attempts++;

            if (userGuess == numberToGuess) {
                hasWon = true;
            } else if (userGuess > numberToGuess) {
                System.out.println("Too high! Try again.");
            } else {
                System.out.println("Too low! Try again.");
            }
        }

        if (hasWon) {
            System.out.println("Congratulations! You guessed the number in " + attempts + " attempts.");
        } else {
            System.out.println("Sorry, you've used all " + MAX_ATTEMPTS + " attempts. The number was " + numberToGuess + ".");
        }
    }
}
