import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        String word, newWord;
        int power;

        try (ServerSocket serverSocket = new ServerSocket(6666)) {
            System.out.println("Server started, waiting for client connection...");
            try (Socket clientSocket = serverSocket.accept();
                 DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
                 DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream())) {

                System.out.println("Client connected successfully");
                power = 1;

                System.out.println("Waiting for input from client...");

                while (power == 1) {
                    try {
                        word = dataInputStream.readUTF();
                        System.out.println("Client: " + word);
                        newWord = GameUtils.getInput();

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
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        } finally {
            GameUtils.clearPastWords(); // Clear content of "past_words.txt"
        }
    }
}
