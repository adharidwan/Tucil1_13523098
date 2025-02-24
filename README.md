# IQ Puzzler Pro Solver

![CLI Demo](https://tenor.com/view/chaewon-lakers-le-sserafim-kpop-lesserafim-gif-1794741366068201628.gif)

## Description
Program pembantai IQ Puzzler pro, dibuat untuk tucil1 IF2211 Strategi Algoritma

## Requirements
- Java Development Kit (JDK) version 21
- Gradle

## How to Compile for CLI
1. Open terminal/command prompt
2. Navigate to the `tucil1` directory
```bash
cd src/main/java/org/tucil1
```
3. Compile the program
```bash
javac Main.java Board.java Piece.java
```

## How to Run for CLI
0. It's come already compiled in bin/main
1. Make sure you are in the `bin/main` directory
2. Run the program using:
```bash
java org.tucil1.Main
```
## How to Run for GUI
1. go to src directory
```bash
cd src
```
2. run command
```bash
./gradlew run
```

## Program Features
- Read puzzle configuration from .txt file
- Find solution using Brute Force algorithm
- Display colored solution in console
- Save solution to .txt file & png file
- Display execution time and number of iterations
- use GUI 

## Input Format
The program accepts .txt files with the following format:
```
N M P
S
puzzle_1_shape
puzzle_2_shape
...
puzzle_P_shape
```
Where:
- N, M: Board dimensions (N x M)
- P: Number of puzzle pieces
- S: Case type (DEFAULT/CUSTOM/PYRAMID)
- puzzle_shape: Configuration of each puzzle piece using capital letters

## Author
Muhammad Adha Ridwan
13523098
K2
Informatics/Computer Science
School of Electrical Engineering and Informatics
Bandung Institute of Technology


