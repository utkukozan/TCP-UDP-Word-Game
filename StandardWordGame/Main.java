import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class Main {
    private static final int ANSWER_LIMIT = 10; // Answer time limit in seconds
    private static Scanner input;

    public static void main(String[] args) throws FileNotFoundException {
        String word = "elma", newWord = "";
        boolean isGameOver = false;

        System.out.println("Initial word: " + word);
        System.out.println("Generate a word using the last 2 letters of this word.");
        System.out.println("You have 10 seconds to enter each word!");

        input = new Scanner(System.in);

        while (!isGameOver) {
            newWord = generateWordWithTimeout();
            if (newWord.isEmpty() || newWord.equals("bye")) {
                isGameOver = true;
                if (newWord.isEmpty()) {
                    System.out.println("You failed to answer in time. Game Over!");
                }
            } else {
                if (newWord.startsWith(word.substring(word.length() - 2))) {
                    if (isWordValid(newWord)) {
                        System.out.println("Correct!");
                        word = newWord;
                        System.out.println("Current word: " + word);
                        System.out.println("Generate a word using the last 2 letters of this word.");
                    } else {
                        System.out.println("Try again: ");
                    }
                } else {
                    System.out.println("Incorrect word generated. Try again.");
                    System.out.println("Reminder of the previous word: " + word);
                }
            }
        }

        System.out.println("Exiting...");
        input.close();
    }

    private static String generateWordWithTimeout() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(() -> input.nextLine());

        try {
            return future.get(ANSWER_LIMIT, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            System.out.println("Time's up for this word!");
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            executor.shutdownNow();
        }
    }

    private static boolean isWordValid(String newWord) throws FileNotFoundException {
        File file = new File("../word_list.txt");
        Scanner fileScanner = new Scanner(file);
        boolean isValid = false;

        while (fileScanner.hasNext()) {
            if (newWord.equals(fileScanner.nextLine())) {
                isValid = true;
                break;
            }
        }

        fileScanner.close();

        if (isValid) {
            System.out.println("The word matches our system!");
        } else {
            System.out.println("The word does NOT match our system!");
        }

        return isValid;
    }
}
