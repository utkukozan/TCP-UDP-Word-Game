import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Server {
    private static final int SERVER_PORT = 6667;
    private static final int CLIENT_PORT = 6666;
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) {
        try (DatagramSocket serverSocket = new DatagramSocket(SERVER_PORT)) {
            System.out.println("Server started, waiting for client connection...");
            InetAddress ip = InetAddress.getLocalHost();
            byte[] buffer = new byte[BUFFER_SIZE];

            String enter = receiveData(serverSocket, buffer);
            if ("play".equals(enter)) {
                System.out.println("Client connected successfully");
                System.out.println("Client wants to play with you. Type 'play' to play with client.");
                Scanner input = new Scanner(System.in);
                enter = input.nextLine();
                if ("play".equals(enter)) {
                    sendData(serverSocket, "play", ip, CLIENT_PORT);
                    System.out.println("Game is started, waiting for a word from Client to input...");
                    playGame(serverSocket, ip, buffer);
                } else {
                    sendData(serverSocket, "", ip, CLIENT_PORT);
                }
            } else {
                System.out.println("Client didn't want to play.");
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
        System.out.println("Server is closing...");
    }

    private static void playGame(DatagramSocket socket, InetAddress ip, byte[] buffer) throws IOException {
        boolean playing = true;
        while (playing) {
            String word = receiveData(socket, buffer);
            if (word.isEmpty()) {
                System.out.println("You Win!");
                break;
            } else {
                System.out.println("Client = " + word);
                String newWord = GameUtils.getInput();
                if (newWord.isEmpty()) {
                    System.out.println("You Lose!");
                    sendData(socket, newWord, ip, CLIENT_PORT);
                    break;
                }

                while (!GameUtils.isWordFormationValid(newWord, word)) {
                    newWord = GameUtils.getInput();
                    if (newWord.isEmpty()) {
                        System.out.println("You Lose!");
                        sendData(socket, newWord, ip, CLIENT_PORT);
                        playing = false;
                        break;
                    }
                }
                sendData(socket, newWord, ip, CLIENT_PORT);
            }
        }
        GameUtils.deleteContent(); // Delete content of "past_words.txt"
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
