#  Word Game With UDP Ack Message

This is a UDP-based version of the simple word game with ack message implemented in Java. In this version, the game is played between a client and a server over a UDP connection. The server generates words based on the last two letters of the previous word, and the client needs to input a word that starts with the last two letters of the current word. The game continues until a player fails to enter a word within 10 seconds. Additionally, all used words are written to the `past_words.txt` file.

## Requirements

- Java Development Kit (JDK) installed on your system.
- Text file named `word_list.txt` containing a list of valid words. Each word should be on a separate line.

## How to Run

1. **Run the Server:** First, you need to start the server by executing the `Server.java` file. Open your terminal or command prompt, navigate to the directory containing the Java files, and run the server using the following command:\
javac Server.java\
java Server.java

You will see the message "Server started, waiting for client connection..." indicating that the server is running and waiting for a client to connect.

2. **Run the Client:** Once the server is running and you see the "Server started, waiting for client connection..." message, you can start the client. Open another terminal or command prompt window, navigate to the same directory, and run the client using the following command:\
javac Client.java\
java Client.java

This will initiate the connection to the server and start the game. The game will also write all used words to the `past_words.txt` file.

## Gameplay

1. The client sends the 'play' message to initiate playback.
2. The server responds to the 'play' message with 'play'.
3. The game starts with an initial word written by client
4. The server needs to generate a word using the last two letters of the current word and input it.
5. The game validates the word and sends feedback to the client.
6. If the word is valid and starts with the last two letters of the current word, the game continues.
7. If the word is invalid or doesn't start with the last two letters of the current word, the server prompts the client to try again.
8. After each correct input, the current word is updated, and the game continues.
9. The game ends when either the client or the server fails to generate a word within 10 seconds.

## File Structure

- `Server.java`: The Java file containing the server-side implementation.
- `Client.java`: The Java file containing the client-side implementation.
- `Main.java`: (Optional) The main Java file containing the shared game logic.
- `word_list.txt`: Text file containing a list of valid words.
- `past_words.txt`: Text file containing all used words during the game session.

## Notes

- Ensure that the `word_list.txt` file exists and contains valid words. You can modify this file to add or remove words from the game dictionary.
- The `past_words.txt` file will be created if it does not exist and will be appended with each word used during the game session.

