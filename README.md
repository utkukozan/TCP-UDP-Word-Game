# Word Game

This repository contains a word game implemented in Java. The game has multiple versions, each showcasing different network communication protocols (TCP, UDP, UDP with ACK). Below you'll find an overview of each version and links to their respective detailed README.md files.

## Table of Contents

- [Introduction](#introduction)
- [Game Versions](#game-versions)
  - [Standard Version](#standard-version)
  - [TCP Version](#tcp-version)
  - [UDP Version](#udp-version)
  - [UDP with ACK Version](#udp-with-ack-version)
- [Setup and Running the Game](#setup-and-running-the-game)
- [Contributing](#contributing)
- [License](#license)

## Introduction

This word game challenges players to come up with words based on the last two letters of the previous word. The game has several implementations:
- **Standard Version**: Single-player mode with no network communication.
- **TCP Version**: Multiplayer mode using TCP for communication between server and client.
- **UDP Version**: Multiplayer mode using UDP for communication between server and client.
- **UDP with ACK Version**: Multiplayer mode using UDP with acknowledgment to ensure packet delivery between server and client.

## Game Versions

### [Standard Version](standard/README.md)

In the standard version, the player must enter a word within a certain time limit. The word must be formed from the last two letters of the previous word. This mode is single-player and does not involve any network communication.

### [TCP Version](tcp/README.md)

In the TCP version, the game is played between a server and a client. The server and client establish a connection using the TCP protocol. Players take turns to enter words within a certain time limit. The word must be formed from the last two letters of the previous word. This version ensures reliable communication due to the inherent properties of TCP (e.g., connection-oriented, error-checking, and flow control).

### [UDP Version](udp/README.md)

In the UDP version, the game is also played between a server and a client. However, the communication uses the UDP protocol. Players send and receive words in packets, and the game requires each player to send a "play" request to initiate a turn. The word must be formed from the last two letters of the previous word. Unlike TCP, UDP is connectionless and does not guarantee packet delivery, order, or error-checking, making it a faster but less reliable option.

### [UDP with ACK Version](udp_ack/README.md)

The UDP with ACK version builds upon the UDP version by adding an acknowledgment mechanism. In this version, after a player sends a word, they wait for an acknowledgment from the other player indicating that the packet was received successfully. This addition ensures more reliable communication compared to standard UDP, as it allows for retransmission of lost packets.

## Setup and Running the Game

To run any version of the game, follow the instructions in the specific README.md file** for that version to compile and run the game.

## Contributing

Contributions are welcome! Please fork this repository and submit pull requests for any enhancements, bug fixes, or new features. Ensure your code follows the existing style and includes appropriate tests.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more details.

---

**Note:** This repository is intended for educational purposes to help learn and understand Java TCP-UDP.

**Remember to star the repository if you find it useful!**