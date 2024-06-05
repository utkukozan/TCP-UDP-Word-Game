import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Server {
    private static final int SERVER_PORT = 6667;
    private static final int CLIENT_PORT = 6666;
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) {
        try (DatagramSocket serverSocket = new DatagramSocket(SERVER_PORT)) {
            System.out.println("Server started, waiting for client connection...");
            byte[] buf = new byte[BUFFER_SIZE];

            String enter = receiveData(serverSocket, buf);
            if ("play".equals(enter)) {
                System.out.println("Client connected successfully");
                System.out.println("Client wants to play with you. Type 'play' to play with client.");
                Scanner input = new Scanner(System.in);
                enter = input.nextLine();

                if ("play".equals(enter)) {
                    sendData(serverSocket, enter, InetAddress.getLocalHost(), CLIENT_PORT);
                    System.out.println("Game is started, waiting for a word from Client...");
                    playGame(serverSocket, buf);
                } else {
                    sendData(serverSocket, "", InetAddress.getLocalHost(), CLIENT_PORT);
                }
            } else {
                System.out.println("Client didn't want to play.");
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
        System.out.println("Server is closing...");
    }

    private static void playGame(DatagramSocket serverSocket, byte[] buf) throws IOException {
        boolean playing = true;
        while (playing) {
            String word = receiveData(serverSocket, buf);
            if (word.isEmpty()) {
                System.out.println("You Win!");
                break;
            } else {
                System.out.println("Client = " + word);
                String newWord = GameUtils.getInput();
                if (newWord.isEmpty()) {
                    System.out.println("You Lose!");
                    sendData(serverSocket, newWord, InetAddress.getLocalHost(), CLIENT_PORT);
                    break;
                }

                while (!GameUtils.isWordFormationValid(newWord, word)) {
                    newWord = GameUtils.getInput();
                    if (newWord.isEmpty()) {
                        System.out.println("You Lose!");
                        sendData(serverSocket, newWord, InetAddress.getLocalHost(), CLIENT_PORT);
                        playing = false;
                        break;
                    }
                }
                sendData(serverSocket, newWord, InetAddress.getLocalHost(), CLIENT_PORT);
            }
        }
        GameUtils.deleteContent(); // Delete content of "past_words.txt"
    }

    private static void sendData(DatagramSocket socket, String data, InetAddress ip, int port) throws IOException {
        byte[] buf = data.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, ip, port);
        socket.send(packet);
        receiveAck(socket, buf);
    }

    private static String receiveData(DatagramSocket socket, byte[] buf) throws IOException {
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        sendAck(socket, packet.getAddress());
        return new String(packet.getData(), 0, packet.getLength());
    }

    private static void receiveAck(DatagramSocket socket, byte[] buf) throws IOException {
        DatagramPacket ackPacket = new DatagramPacket(buf, buf.length);
        socket.receive(ackPacket);
        System.out.println("Packet was successfully sent!");
    }

    private static void sendAck(DatagramSocket socket, InetAddress ip) throws IOException {
        byte[] ack = "ACK".getBytes();
        DatagramPacket ackPacket = new DatagramPacket(ack, ack.length, ip, CLIENT_PORT);
        socket.send(ackPacket);
    }
}
