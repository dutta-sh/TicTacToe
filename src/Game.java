/* Assignment1 : Tic Tac Toe
 * Course: CS 570 A
 * Group Members: Paromita Datta, Paras Garg, Pinkal Ganjawala
 */

public class Game {
    private char[][] table;
    private int rowCount;
    private int colCount;
    private int winningCount;
    private char[] players;
    private int currentPlayer;
    
    //initialize a new game
    public Game(int playerCount, int rowCount, int colCount, int winningCount) {
        this.rowCount = rowCount;
        this.colCount = colCount;
        this.winningCount = winningCount;
        this.players = "XOABCDEFGHIJKLMNPQRSTUVWYZ".substring(0, playerCount).toCharArray();
        this.currentPlayer = 0; // At any point in time it shows the position of the current player in the players[]       
        table = new char[rowCount][colCount];
        for (int i = 0; i < rowCount; i++)
            for (int p = 0; p < colCount; p++)
                table[i][p] = ' ';
        
        System.out.println("New Game is ready!");
        displayBoard();
    }
    
    //initialize a game from data dump
    public Game(String gameData) throws Exception {
        try {
            String[] dataArr = gameData.split(";");
            this.players = dataArr[0].toCharArray();
            this.currentPlayer = Integer.parseInt(dataArr[1]);
            this.winningCount = Integer.parseInt(dataArr[2]);
    
            String[] rowData = dataArr[3].split("\\|");
            this.rowCount = rowData.length;
            this.colCount = rowData[0].length();
            this.table = new char[this.rowCount][this.colCount];
    
            for (int i = 0; i < this.rowCount; i++) {		//fetching data elements from array
                char[] cells = rowData[i].toCharArray();
                for (int p = 0; p < this.colCount; p++) {
                    this.table[i][p] = cells[p];
                }
            }
            System.out.println("Game is ready from where it was left off!");
            displayBoard();
        } catch (Exception e) {
            throw new Exception("Unable to parse game file", e);
        }
    }
    
    //check if a user can take this cell
    public boolean isIllegal(int row, int col) {
        if (row >= rowCount || row < 0 || col >= colCount || col < 0)
            return true;
        if (table[row][col] != ' ')
            return true;
        return false;
    }
    
    //display the board with proper indentation
    public void displayBoard() {
        System.out.print("   ");
        for (int p = 0; p < colCount; p++) {
            System.out.print(String.format("%3s", p+1));
            if(p < colCount - 1)
                System.out.print(" ");
            else
                System.out.println();
        }
        
        for (int i = 0; i < rowCount; i++) {
            System.out.print(String.format("%3s", i+1));
            for (int p = 0; p < colCount; p++) {
                System.out.print(" " + table[i][p]);// prints space + character
                if(p < colCount - 1)
                    System.out.print(" |");	// prints space + |
                else
                    System.out.println();
            }
            if(i < rowCount - 1) {
                System.out.print("   ");
                for (int p = 0; p < colCount; p++) {
                    System.out.print("---");
                    if(p < colCount - 1)
                        System.out.print("+");
                    else
                        System.out.println();
                }
            } else
                System.out.println();
        }
    }
    
    //return the current player
    public char getPlayer() {
        return players[currentPlayer];
    }
    
    //set a cell for the current player
    public void markCell(int row, int column) {
        table[row][column] = players[currentPlayer];;
    }
    
    //change player to the next one
    public void setNextPlayer() {
        currentPlayer = (currentPlayer + 1)%(players.length);
     /*   currentPlayer = (currentPlayer + 1);
        if(currentPlayer == players.length)
        	currentPlayer = 0; */
    }
    
    /* decide if current cell results in a win
    from current position traverse in all 8 directions, until sequence is broken
    then add up counts from opposite directions (east + west, north + south etc)
     if this count >= winning count, then a win has occurred */
    public boolean isWinner(int row, int col) {
        char player = table[row][col]; //position of the current player
        int southEnd = Math.min(row + winningCount, rowCount); //checks current position + winning sequence, with board rowsize
        int northEnd = Math.max(row - winningCount, -1);	//for loop goes to > -1 which is 0
        int eastEnd = Math.min(col + winningCount, colCount);
        int westEnd = Math.max(col - winningCount, -1);
        
        int south = 1;
        for(int i = row + 1; i < southEnd; i++) { // row+1 is used to count from the next position since value is already present in the current position
            if(table[i][col] == player)
                south++;
            else
                break;
            if(south >= winningCount)
                return true;
        }
        
        int north = 1;
        for(int i = row - 1; i > northEnd; i--) {
            if(table[i][col] == player)
                north++;
            else
                break;
            if(north >= winningCount)
                return true;
        }
        if(south + north - 1 >= winningCount)// -1 to avoid repetition of starting element
            return true;
        
        int east = 1;
        for(int p = col + 1; p < eastEnd; p++) {
            if(table[row][p] == player)
                east++;
            else
                break;
            if(east >= winningCount)
                return true;
        }
        
        int west = 1;
        for(int p = col - 1; p > westEnd; p--) {
            if(table[row][p] == player)
                west++;
            else
                break;
            if(west >= winningCount)
                return true;
        }
        if(east + west - 1 >= winningCount)
            return true;
        
        ///////////////////////////////////////////////////////////////////////////
        
        int southEast = 1;
        int i = row + 1;
        int p = col + 1;
        while(i < southEnd && p < eastEnd) {
            if(table[i][p] == player)
                southEast++;
            else
                break;
            if(southEast >= winningCount)
                return true;
            i++;
            p++;
        }
        
        int northWest = 1;
        i = row - 1;
        p = col - 1;
        while(i > northEnd && p > westEnd) {
            if(table[i][p] == player)
                northWest++;
            else
                break;
            if(northWest >= winningCount)
                return true;
            i--;
            p--;
        }
        if(southEast + northWest - 1 >= winningCount)
            return true;
        
        int southWest = 1;
        i = row + 1;
        p = col - 1;
        while(i < southEnd && p > westEnd) {
            if(table[i][p] == player)
                southWest++;
            else
                break;
            if(southWest >= winningCount)
                return true;
            i++;
            p--;
        }
        
        int northEast = 1;
        i = row - 1;
        p = col + 1;
        while(i > northEnd && p < eastEnd) {
            if(table[i][p] == player)
                northEast++;
            else
                break;
            if(northEast >= winningCount)
                return true;
            i--;
            p++;
        }
        if(northEast + southWest - 1 >= winningCount)
            return true;
        
        return false;
    }
    
    //decide if its a tie
    public boolean isTie() {
        for (int i = 0; i < rowCount; i++)
            for (int p = 0; p < colCount; p++)
                if (table[i][p] == ' ')
                    return false;
        return true;
    }
    
    //create a data dump of current game
    public String serialize() {
        StringBuilder strBld = new StringBuilder();
        strBld.append(players).append(";");
        strBld.append(currentPlayer).append(";");
        strBld.append(winningCount).append(";");
        for (int i = 0; i < rowCount; i++) {
            for (int p = 0; p < colCount; p++) {
                strBld.append(table[i][p]);
            }
            strBld.append("|");
        }
        return strBld.toString();
    }
}