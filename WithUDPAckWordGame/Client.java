import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    private static final int CLIENT_PORT = 6666;
    private static final int SERVER_PORT = 6667;
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) throws Exception {
        DatagramSocket clientSocket = new DatagramSocket(CLIENT_PORT);
        InetAddress ip = InetAddress.getLocalHost();
        byte[] buf = new byte[BUFFER_SIZE];

        System.out.println("To start game, type : 'play'");
        Scanner input = new Scanner(System.in);
        String enter = input.nextLine();

        if ("play".equals(enter)) {
            sendData(clientSocket, enter, ip, SERVER_PORT);
            System.out.println("Waiting for response from Server...");
            enter = receiveData(clientSocket, buf);

            if ("play".equals(enter)) {
                System.out.println("Game is started! Input a word: ");
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String word = reader.readLine();
                sendData(clientSocket, word, ip, SERVER_PORT);
                System.out.println("Waiting for new word from Server...");

                playGame(clientSocket, ip, buf);
            } else {
                System.out.println("Server didn't want to play.");
            }
        } else {
            sendData(clientSocket, "", ip, SERVER_PORT);
        }

        clientSocket.close();
        System.out.println("Client is closing...");
    }

    private static void playGame(DatagramSocket clientSocket, InetAddress ip, byte[] buf) throws IOException {
        boolean playing = true;
        while (playing) {
            String word = receiveData(clientSocket, buf);
            if (word.isEmpty()) {
                System.out.println("You Win!");
                break;
            } else {
                System.out.println("Server = " + word);
                String newWord = GameUtils.getInput();
                if (newWord.isEmpty()) {
                    System.out.println("You Lose!");
                    sendData(clientSocket, newWord, ip, SERVER_PORT);
                    break;
                }

                while (!GameUtils.isWordFormationValid(newWord, word)) {
                    newWord = GameUtils.getInput();
                    if (newWord.isEmpty()) {
                        System.out.println("You Lose!");
                        sendData(clientSocket, newWord, ip, SERVER_PORT);
                        playing = false;
                        break;
                    }
                }
                sendData(clientSocket, newWord, ip, SERVER_PORT);
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
        DatagramPacket ackPacket = new DatagramPacket(ack, ack.length, ip, SERVER_PORT);
        socket.send(ackPacket);
    }
}
