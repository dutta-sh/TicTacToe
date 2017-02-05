/* Assignment1 : Tic Tac Toe
 * Course: CS 570 A
 * Group Members: Paromita Datta, Paras Garg, Pinkal Ganjawala
 */

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class TicTacToe {
    private static Scanner k = new Scanner(System.in);
    private static String gameFilePath;
    private static int playerCount;
    private static int winningCount;
    private static int rows;
    private static int cols;
    
    public static void main(String[] args) throws Exception {
        Game game;	//declared here, so it can be accessed later on and it is not declared static so constructor can be called
        getGameLoadOption();
        if(gameFilePath != null) {
            String dataDump = readFromFile(gameFilePath);
            game = new Game(dataDump);            
        } else {
            getPlayerCount();
            getBoardSize();
            getWinningCount();
            game = new Game(playerCount, rows, cols, winningCount);
        }
        
        int row;
        int col;
        String ip;
        while (true) {
            char player = game.getPlayer();
            System.out.print("Player " + player + " enter position <row col> (or Q to quit): ");
            while(true) {
                ip = k.nextLine();
                if(ip.toUpperCase().startsWith("Q")) {
                    String dataDump = game.serialize();
                    System.out.print("Enter file name to save data: ");
                    ip = k.nextLine();
                    writeToFile(ip, dataDump);
                    System.out.println("Exiting game");
                    System.exit(0); // quitting the program
                }
                try {
                    String[] ipArr = ip.split(" ");
                    row = Integer.parseInt(ipArr[0]) - 1;	//to get correct array index
                    col = Integer.parseInt(ipArr[1]) - 1;
                    if(game.isIllegal(row, col)) {
                        System.out.print("Occupied or invalid position. Enter again <row col> (or Q to quit): ");
                    } else {
                        break;
                    }
                } catch (Exception e) {
                    System.out.print("Invalid input. Enter again <row col> (or Q to quit): ");
                }
            }
            
            game.markCell(row, col);
            game.displayBoard();
            if (game.isWinner(row, col)) {
                System.out.println("Player " + player + " has won!");
                break;
            }
            if (game.isTie()) {
                System.out.println("It's a tie!");
                break;
            }
            game.setNextPlayer();
        }
        k.close();
    }
    
    //ask user whether to start a new game or load saved game
    private static void getGameLoadOption() {
        String ip;        
        System.out.print("Resume saved game (Yes/No): ");        
        while (true) {
            ip = k.nextLine();
            if(ip.toUpperCase().startsWith("Y")) {
                System.out.print("Path of saved game file: ");
                gameFilePath = k.nextLine();
                break;
            } else if(ip.toUpperCase().startsWith("N")) {
                gameFilePath = null;
                break;
            } else {
                System.out.print("Invalid input. Enter again: ");
            }                        
        }
    }
    
    //ask user how many players would play
    private static void getPlayerCount() {
        String ip;
        System.out.print("Select how many players in game (2 - 26): ");
        while (true) {
            ip = k.nextLine();
            try {
                playerCount = Integer.parseInt(ip);
                if (playerCount < 2 || playerCount > 26) {
                    System.out.print("Invalid input. Enter again: ");
                } else {
                    break;
                }
            } catch (Exception e) {
                System.out.print("Invalid input. Enter again: ");// if user gives input such as 'abcd'
            }
        }
    }
    
    //ask user the board size
    private static void getBoardSize() {
        String ip;
        System.out.print("Select board row and col size (3 - 999) <rowSize colSize>: ");
        while (true) {
            ip = k.nextLine();
            try {
                String[] ipArr = ip.split(" ");
                rows = Integer.parseInt(ipArr[0]);
                cols = Integer.parseInt(ipArr[1]);
                if (rows < 3 || rows > 999 || cols < 3 || cols > 999) {
                    System.out.print("Invalid input. Enter again <rowSize colSize>: ");
                } else if(playerCount > (rows * cols)) {
                    System.out.print("Number of players more than table size. Enter again <rowSize colSize>: ");
                } else {
                    break;
                }
            } catch (Exception e) {
                System.out.print("Invalid input. Enter again <rowSize colSize>: ");//if user gives non numeric inputs.
            }
        }
    }
    
    //ask user winning sequence count
    private static void getWinningCount() {
        String ip;
        System.out.print("Select winning sequence count (3 - boardsize): ");
        while(true) {
            ip = k.nextLine();
            try {
                winningCount = Integer.parseInt(ip);
                if (winningCount < 3 || winningCount > Math.max(rows, cols)) {
                    System.out.print("Invalid input. Enter again: ");
                } else {
                    break;
                }
            } catch (Exception e) {
                System.out.print("Invalid input. Enter again: ");// when user gives non numeric inputs
            }
        }
    }
    
    //save contents to file
    private static void writeToFile(String filePath, String contents) throws Exception {
        try {
            FileOutputStream out = new FileOutputStream(filePath);
            byte[] data= contents.getBytes();
            out.write(data);
            out.close();
            System.out.println("Game data saved to: " + filePath);
        } catch (Exception e) {
            throw new Exception("Error saving to file", e); // when unable to create the file
        }
    }
    
    //read contents from file
    private static String readFromFile(String filePath) throws Exception {
        String data;
        try {
            byte[] byteArr = Files.readAllBytes(Paths.get(filePath));
            data = new String(byteArr);
        } catch (IOException e) {
            throw new Exception("Error reading from file", e);
        }
        return data;
    }
}