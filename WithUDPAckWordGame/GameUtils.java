import java.io.*;
import java.nio.file.*;
import java.util.Scanner;
import java.util.concurrent.*;

public class GameUtils {
    private static final String WORD_LIST_FILE = "../word_list.txt";
    private static final String PAST_WORDS_FILE = "../past_words.txt";
    private static String newWord = "";

    public static boolean isWordValid(String newWord) throws IOException {
        return isWordInList(newWord) && isWordNotRepeating(newWord);
    }

    private static boolean isWordInList(String newWord) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(WORD_LIST_FILE))) {
            while (scanner.hasNextLine()) {
                if (newWord.equals(scanner.nextLine())) {
                    return true;
                }
            }
        }
        System.out.println("This word does not match our word database!");
        return false;
    }

    public static boolean isWordFormationValid(String newWord, String word) throws IOException {
        if (newWord.substring(0, 2).equals(word.substring(word.length() - 2))) {
            if (isWordValid(newWord)) {
                addWordToTxt(newWord);
                System.out.println("Your word = " + newWord + "\nWaiting for opponent...");
                return true;
            } else {
                return false;
            }
        } else {
            System.out.print("Unfortunately, you typed the wrong word...");
            System.out.println(" Let's recall the previous word: " + word);
            return false;
        }
    }

    private static boolean isWordNotRepeating(String newWord) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(PAST_WORDS_FILE))) {
            while (scanner.hasNextLine()) {
                if (newWord.equals(scanner.nextLine())) {
                    System.out.println("This word was used before!");
                    return false;
                }
            }
        }
        return true;
    }

    private static void addWordToTxt(String newWord) {
        try {
            Files.write(Paths.get(PAST_WORDS_FILE), (newWord + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Error: word cannot be added to 'past_words.txt'");
        }
    }

    public static void deleteContent() {
        try (PrintWriter writer = new PrintWriter(PAST_WORDS_FILE)) {
            writer.print("");
        } catch (FileNotFoundException e) {
            System.out.println("Error: 'past_words.txt' not found.");
        }
    }

    public static String getInput() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(() -> {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            try {
                return in.readLine().toLowerCase();
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        });

        System.out.println("Your turn, input a string within 10 seconds: ");
        try {
            newWord = future.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.out.println("Job was interrupted");
        } catch (ExecutionException e) {
            System.out.println("Caught exception: " + e.getCause());
        } catch (TimeoutException e) {
            future.cancel(true);
            System.out.println("Time is up!");
            newWord = "";
        } finally {
            executor.shutdown();
        }
        return newWord;
    }
}
