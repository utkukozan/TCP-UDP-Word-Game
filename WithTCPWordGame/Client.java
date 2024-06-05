import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 6666);
             BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
             DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
             DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream())) {

            // Picking first word
            System.out.println("Please input a word: ");
            dataOutputStream.writeUTF(input.readLine().toLowerCase());
            dataOutputStream.flush();
            System.out.println("Game started! Waiting for new word from server...");

            int power = 1;

            while (power == 1) {
                try {
                    String word = dataInputStream.readUTF();
                    System.out.println("Server: " + word);

                    String newWord = GameUtils.getInput();
                    assert newWord != null;
                    if (newWord.isEmpty()) {
                        System.out.println("You Lose!");
                        break;
                    }

                    boolean validWord = false;
                    while (!validWord) {
                        if (GameUtils.isValidFormation(newWord, word)) {
                            dataOutputStream.writeUTF(newWord);
                            dataOutputStream.flush();
                            validWord = true;
                        } else {
                            newWord = GameUtils.getInput();
                            if (newWord.isEmpty()) {
                                System.out.println("You Lose!");
                                power = 0;
                                break;
                            }
                        }
                    }
                } catch (EOFException e) {
                    System.out.println("You win!");
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
        } finally {
            GameUtils.clearPastWords(); // Clear content of "past_words.txt"
        }
    }
}
