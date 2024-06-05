import java.io.*;
import java.nio.file.*;
import java.util.Scanner;
import java.util.concurrent.*;
public class GameUtils {

    public static boolean isValidWord(String newWord) throws IOException {
        if (isWordRepeated(newWord)) {
            System.out.println("This word has been used before!");
            return false;
        }
        return isWordInDatabase(newWord);
    }

    private static boolean isWordInDatabase(String word) throws IOException {
        try (Scanner scanner = new Scanner(new File("../word_list.txt"))) {
            while (scanner.hasNextLine()) {
                if (word.equals(scanner.nextLine().trim())) {
                    return true;
                }
            }
        }
        System.out.println("This word does not match our word database!");
        return false;
    }

    private static boolean isWordRepeated(String word) throws IOException {
        try (Scanner scanner = new Scanner(new File("../past_words.txt"))) {
            while (scanner.hasNextLine()) {
                if (word.equals(scanner.nextLine().trim())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void addWordToPastWords(String word) {
        try {
            Files.write(Paths.get("../past_words.txt"), (word + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Error: Word cannot be added to 'past_words.txt'");
        }
    }

    public static void clearPastWords() {
        try (PrintWriter writer = new PrintWriter("../past_words.txt")) {
            writer.print("");
        } catch (FileNotFoundException e) {
            System.out.println("Error clearing past words");
        }
    }

    public static boolean isValidFormation(String newWord, String currentWord) throws IOException {
        if (newWord.length() < 2 || currentWord.length() < 2) {
            return false;
        }
        if (newWord.substring(0, 2).equalsIgnoreCase(currentWord.substring(currentWord.length() - 2))) {
            if (isValidWord(newWord)) {
                addWordToPastWords(newWord);
                System.out.println("Your word: " + newWord + "\nWaiting for opponent...");
                return true;
            }
        } else {
            System.out.println("Unfortunately, you typed the wrong word...");
            System.out.println("Let's recall the previous word: " + currentWord);
        }
        return false;
    }

    public static String getInput() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(() -> {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Your Turn, input a string within 10 seconds: ");
            return reader.readLine().toLowerCase();
        });

        try {
            return future.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (TimeoutException e) {
            System.out.println("Time is up!");
            future.cancel(true);
        } finally {
            executor.shutdown();
        }
        return null;
    }
}
