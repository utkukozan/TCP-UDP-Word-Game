import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final int CLIENT_PORT = 6666;
    private static final int SERVER_PORT = 6667;
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) {
        try (DatagramSocket clientSocket = new DatagramSocket(CLIENT_PORT)) {
            InetAddress ip = InetAddress.getLocalHost();
            byte[] buf = new byte[BUFFER_SIZE];

            Scanner input = new Scanner(System.in);
            System.out.println("To start the game, type: 'play'");
            String enter = input.nextLine();

            if ("play".equals(enter)) {
                sendData(clientSocket, enter, ip, SERVER_PORT);
                System.out.println("Waiting for response from the server...");
                String response = receiveData(clientSocket, buf);

                if ("play".equals(response)) {
                    System.out.println("The game has started! Enter a word: ");
                    playGame(clientSocket, ip, buf);
                } else {
                    System.out.println("The server doesn't want to play.");
                }
            } else {
                sendData(clientSocket, "", ip, SERVER_PORT);
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
        System.out.println("Client is closing...");
    }

    private static void playGame(DatagramSocket socket, InetAddress ip, byte[] buf) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String word = reader.readLine();
            sendData(socket, word, ip, SERVER_PORT);
            System.out.println("The word has been sent! Waiting for a new word from the server...");

            boolean playing = true;
            while (playing) {
                word = receiveData(socket, buf);
                if (word.isEmpty()) {
                    System.out.println("You Win!");
                    break;
                } else {
                    System.out.println("Server: " + word);
                    String newWord = GameUtils.getInput();
                    if (newWord.isEmpty()) {
                        System.out.println("You Lose!");
                        sendData(socket, newWord, ip, SERVER_PORT);
                        break;
                    }

                    while (!GameUtils.isWordFormationValid(newWord, word)) {
                        newWord = GameUtils.getInput();
                        if (newWord.isEmpty()) {
                            System.out.println("You Lose!");
                            sendData(socket, newWord, ip, SERVER_PORT);
                            playing = false;
                            break;
                        }
                    }
                    sendData(socket, newWord, ip, SERVER_PORT);
                }
            }
            GameUtils.deleteContent(); // Delete content of "past_words.txt"
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void sendData(DatagramSocket socket, String data, InetAddress ip, int port) throws IOException {
        byte[] buf = data.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, ip, port);
        socket.send(packet);
    }

    private static String receiveData(DatagramSocket socket, byte[] buf) throws IOException {
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        return new String(packet.getData(), 0, packet.getLength());
    }
}
