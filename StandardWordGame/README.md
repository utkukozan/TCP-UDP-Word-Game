# Standard Word Game

This is a simple word game implemented in Java. The game generates words based on the last two letters of the previous word. The player needs to input a word that starts with the last two letters of the current word. The game continues until the player inputs "bye" or the time limit of 10 seconds is reached.

## Requirements

- Java Development Kit (JDK) installed on your system.
- Text file named `word_list.txt` containing a list of valid words. Each word should be on a separate line.

## How to Run

1. **Compile the Program:** Open your terminal or command prompt, navigate to the directory containing the Java file (`Main.java`), and compile the program using the following command:

## Gameplay

1. The game starts with an initial word ("elma" by default).
2. You need to generate a word using the last two letters of the current word.
3. The timer of 10 seconds starts once you input your word.
4. Input "bye" to exit the game at any time.
5. If the generated word is valid and starts with the last two letters of the current word, the game continues.
6. If the generated word is invalid or doesn't start with the last two letters of the current word, you will be prompted to try again.
7. After each correct input, the current word is updated, and the game continues.
8. The game ends when you input "bye" or when the time limit of 10 seconds is reached.

## File Structure

- `Main.java`: The main Java file containing the game implementation.
- `word_list.txt`: Text file containing a list of valid words.

## Notes

- Ensure that the `word_list.txt` file exists and contains valid words. You can modify this file to add or remove words from the game dictionary.
