package cs1302.game;

import java.util.*;
import java.io.*;

/**
 * This class represents a Minesweeper game.
 *
 * @author Chirag Sharma
 */

public class MinesweeperGame {

    //Instance Variables
    private int rows;
    private int cols;
    private int rounds = 0;
    private String[][] mineField;
    private Boolean[][] mineGrid;
    private double score;
    private int numMines = 0;
    private int mineX;
    private int mineY;
    private int numRevealed = 0;
    private boolean noFog = false;
    private String[][] noFogArray;

    /**
     * Deafault constructor of the MinesweeperGame class.
     *
     */
    
    //Default constructor
    public MinesweeperGame() { }
    
    /**
     * References a location that has a text fil 
     * Constructs an object instance of the {@Code MinesweepeGamer} class.  
     * References a location that has a text file and takes the text in as a string.
     *
     * @param seed the text in the file which describes how to create the mine grid. 
     */

    //Seed constructor
    public MinesweeperGame(String seed) { 
        try {
            File readFile = new File(seed);
            Scanner read = new Scanner(readFile);        
            if (read.hasNextInt()) {
                rows = read.nextInt();
                if (read.hasNextInt()) {
                    cols = read.nextInt();
                    if (read.hasNextInt()) {
                        numMines = read.nextInt();
                    }
                }
            } else {
                System.err.println("Seedfile Format Error: Cannot create game with readFile,");
                System.err.print(" because it is not formatted correctly.");
                System.out.println("");
                System.exit(1);
            }        
            if (!(rows >= 5 && cols >= 5)) {
                System.out.println("");
                System.out.println("Seedfile Value Error: Cannot create a mine field");
                System.out.print(" with that many rows and/or columns");
                System.out.println("");
                System.exit(3);
            }
            if (numMines > (rows * cols)) {
                System.err.println("Seedfile Format Error: Cannot create game with readFile,");
                System.err.print(" because it is not formatted correctly.");
                System.out.println("");
                System.exit(1);
            }

            createMineField(rows, cols);
            
            for (int i = 0; i < numMines; i++) {
                mineX = read.nextInt();
                mineY = read.nextInt();
                
                if (mineX > rows || mineX < 0 || mineY > cols || mineY < 0) {
                    System.err.println("Seedfile Format Error: Cannot create game with readFile,");
                    System.err.println(" because it is not formatted correctly.");
                    System.out.println("");
                    System.exit(1);
                }
                mineGrid[mineX][mineY] = true;
            }   
        } catch (FileNotFoundException fnfe ) {
            System.err.println("Seedfile Not Found Error: Cannot create game with FILENAME,");
            System.err.print(" because it cannot be be found\n" + 
                             "                          or cannot be read due to permission.\n" + 
                             "");
            System.exit(1);
        } catch (NumberFormatException nfe) {
            System.err.println("Seedfile Not Found Error: Cannot create game with FILENAME,");
            System.err.print(" because it cannot be be found\n" + 
                             "                          or cannot be read due to permission.\n" + 
                             "");
            System.exit(1);
        }
    }

    /**
     * prints the welcome message at the beginning of the game.
     *
     */
    
    public void printWelcome() {
        System.out.println("        _");
        System.out.println("  /\\/\\ (_)_ __   ___  _____      _____  ___ _ __   ___ _ __");
        System.out.println(" /    \\| | '_ \\ / _ \\/ __\\ \\ /\\ / / " +
                           "_ \\/ _ \\ '_ \\ / _ \\ '__|");
        System.out.println("/ /\\/\\ \\ | | | |  __/\\__ \\\\ V  V /  __/  __/ |_) |  __/ |");
        System.out.println("\\/    \\/_|_| |_|\\___||___/ \\_/\\_/ \\___|\\___| .__/ \\___|_|");
        System.out.println("                 A L P H A   E D I T I O N |_| v2020.sp");
    }
    
    /**
     * Creates the minesweeper grid for {@Code MinesweeperGame}.
     * 
     * @param numRows the number of rows the user wants the minefield to have.
     * @param numCols the number of columns the user wants the minefield to have.
     */
    
    public void createMineField(int numRows, int numCols) {
        rows = numRows;
        cols = numCols;
         
        if (!(rows >= 5 && cols >= 5)) {
            System.out.println("");
            System.out.println("Seedfile Value Error: Cannot create a mine field");
            System.out.print(" with that many rows and/or columns");
            System.out.println("");
            System.exit(3);
        }
            
        mineField = new String[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                
                mineField[i][j] = "   ";
            }
        }
        
        mineGrid = new Boolean[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                mineGrid[i][j] = false;
            }
        }

        noFogArray = new String[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                noFogArray[i][j] = "   ";
            }
        }
        
    }

    /**
     * Prints the minefield game grid in the form of an array
     * that the user sees.
     */
    
    public void printMineField() {
        System.out.println(" ");
        System.out.println(" Rounds Completed: " + rounds);
        System.out.println(" ");
        for (int i = 0; i < rows; i++) {
            //Accounts for double digit row inputs
            if (i <= 9) {
                System.out.print(" ");
            }
            System.out.print(" " + i + " |");
            for (int j = 0; j < cols; j++) {
                System.out.print(mineField[i][j]);
                if (j < cols - 1) {
                    System.out.print("|");
                }
            }
            System.out.println("|");
        }
        
        System.out.print("      ");
        for (int b = 0; b < cols; b++) {
            if (b > 9) {
                System.out.print(b + "  ");
            } else {
                System.out.print(b + "   ");
            }
        }
        System.out.println();
    }

    /**
     * Asks the user for the command they want to run and
     * calls the method of that respective command.
     */

    public void promptUser() {
        Scanner prompt = new Scanner(System.in);
        String input = prompt.next().trim();
                        
        if ((input.equals("h")) || (input.equals("help"))) {
            rounds++;
            System.out.println();
            System.out.println("Commands Available...");
            System.out.println(" - Reveal: r/reveal row col");
            System.out.println(" -   Mark: m/mark   row col");
            System.out.println(" -  Guess: g/guess  row col");
            System.out.println(" -   Help: h/help");
            System.out.println(" -   Quit: q/quit");
            System.out.println();
        } else if ((input.equals("q")) || (input.equals("quit"))) {
            System.out.println("");
            System.out.println("Quitting the game...");
            System.out.println("Bye!");
            System.out.println("");
            System.exit(0);
        } else if ((input.equals("m")) || (input.equals("mark"))) {
            mark(prompt);

        } else if ((input.equals("r")) || (input.equals("reveal"))) {
            reveal(prompt);

        } else if ((input.equals("g")) || (input.equals("guess"))) {
            guess(prompt);

        } else if (input.equals("nofog")) {
            rounds++;
            noFog = true;
            System.out.println();

        } else {
            System.out.println();
            System.out.println("Input Error: Command not recognozed!");
        }
                
    }
    
    /**
     * Marks the square selected by the user with the letter "F".
     * Indicates a definitive mine location. 
     *
     * @param prompt represents the square the user wants to mark 
     * via command line row and columns inputs from the user.
     */
    
    public void mark(Scanner prompt) {
        int mRow = 0;
        int mCol = 0;
        if (prompt.hasNextInt()) {         
            mRow = prompt.nextInt();
            if (prompt.hasNextInt()) {
                mCol = prompt.nextInt();
                if (mRow >= 0 && mRow < mineGrid.length && mCol >= 0 && mCol < mineGrid[0].length) {
                    mineField[mRow][mCol] = " F ";
                    rounds++;
                    System.out.println();
                } else {
                    System.out.println("Input Error: Command not recognized!");
                }
            }
        }
        
    }
    
    /**
     * Reveals the square selected by the user.
     * If the selected square is not a mine, it displays the number of adjacent mines. 
     * if the selected square is a mine, the game ends and the player loses.
     *
     * @param prompt represents the square the user wants to reveal
     * via command line row and columns inputs from the user.
     */
    
    public void reveal(Scanner prompt) {
        int rRow = 0;
        int rCol = 0;
        if (prompt.hasNextInt()) {
            rRow = prompt.nextInt();
            if (prompt.hasNextInt()) {
                rCol = prompt.nextInt();
                if (rRow >= 0 && rRow < mineGrid.length && rCol >= 0 && rCol < mineGrid[0].length) {
                    rounds++;
                    System.out.println();
                    if (mineGrid[rRow][rCol] == false) {
                        mineField[rRow][rCol] = " " + getNumAdjMines(rRow, rCol) + " ";
                        numRevealed++;
                    } else {
                        printLoss();
                    }
                } else {
                    System.out.println("Input Error: Command not recognized!");
                }
            }
        }
    }
    
    /**
     * Marks the square selected by the user with a question mark("?").
     * Indicates a possible mine location the user is not a 100 percent sure about.
     *
     * @param prompt represents the square the user wants to guess 
     * via command line row and columns inputs from the user.
     */
    
    public void guess(Scanner prompt) {
        int gRow = 0;
        int gCol = 0;
        if (prompt.hasNextInt()) {
            gRow = prompt.nextInt();
            if (prompt.hasNextInt()) {
                gCol = prompt.nextInt();
                if (gRow >= 0 && gRow < mineGrid.length && gCol >= 0 && gCol < mineGrid[0].length) {
                    mineField[gRow][gCol] = " ? ";
                    rounds++;
                    System.out.println();
                } else {
                    System.out.println("Input Error: Command not recognized!");
                }
            }
        }
    }
    
    /**
     * Prints the minesweeper game but shows all the positions of the mines, indicated by a "< >".
     *
     */
    
    public void printNoFog() {
        System.out.println(" ");
        System.out.println(" Rounds Completed: " + rounds);
        System.out.println(" ");
        for (int i = 0; i < rows; i++) {
            if (i <= 9) {
                System.out.print(" ");
            }
            System.out.print(" " + i + " |");
            for (int j = 0; j < cols; j++) {
                if (mineGrid[i][j]) {
                    System.out.print("<" + " " + ">");
                } else {
                    System.out.print(mineField[i][j]);
                }
                if (j < cols - 1) {
                    System.out.print("|");
                } 
            }
            System.out.println("|");
        } 
        System.out.print("      ");
        for (int c = 0; c < cols; c++) {
            if (c > 9) {
                System.out.print(c + "  ");
            } else {
                System.out.print(c + "   ");
            }
        }
        System.out.println();
    }

    /**
     * Returns the number of adjacent mines to the square selected by the user.
     *
     * @param row the row index of the square.
     * @param col the column index of the square.
     * @return an integer representing the number of adjacent mines.
     */
    
    public int getNumAdjMines(int row, int col) {
        int adjMines = 0;    
        //Checking top left
        if (row - 1 >= 0 && col - 1 >= 0) {
            if (mineGrid[row - 1][col - 1] == true) {
                adjMines++;
            }
        }
        //Checking top
        if (row - 1 >= 0) {
            if (mineGrid[row - 1][col] == true) {
                adjMines++;
            }
        }
        //Checking top right
        if (row - 1 >= 0 && col + 1 < cols) {
            if (mineGrid[row - 1][col + 1] == true) {
                adjMines++;
            }
        }
        //Checking left
        if (col - 1 >= 0) {
            if (mineGrid[row][col - 1] == true) {
                adjMines++;
            }
        }
        //Checking right
        if (col + 1 < cols) {
            if (mineGrid[row][col + 1] == true) {
                adjMines++;
            }
        }
        //Checking bottom left
        if (row + 1 < rows && col - 1 >= 0) {
            if (mineGrid[row + 1][col - 1] == true) {
                adjMines++;
            }
        }
        //Checking bottom
        if (row + 1 < rows) {
            if (mineGrid[row + 1][col] == true) {
                adjMines++;
            }
        }
        //Checking bottom right
        if (row + 1 < rows && col + 1 < cols) {
            if (mineGrid[row + 1][col + 1] == true) {
                adjMines++;
            }
        }
        return adjMines;                                               
    }
    
    /**
     * Checks if the player has met all the conditions for winning the game.
     * 
     * @return true if thats the case. 
     */
    
    public boolean isWon() {
        boolean minesFound = true;
        boolean sqRevealed = true;
        
        for (int i = 0; i < mineField.length; i++) {
            for (int j = 0; j < mineField[0].length; j++) {
                if ((mineGrid[i][j])) {
                    if (!(mineField[i][j].equals(" F "))) {
                        minesFound = false;
                    }
                } else if (mineField[i][j].contains("F") || mineField[i][j].contains("?") ||
                           mineField[i][j].contains("   ")) {
                    sqRevealed = false;                      
                }
            }
        }
        return sqRevealed && minesFound;
    } 

    /**
     * Checks if the player revealed a mine which would
     * cause them to lose the game.
     *
     * @return true if player reveals a mine.
     */
    
    public boolean isLost() {
        boolean isLost = false;

        for (int i = 0; i < mineField.length; i++) {
            for (int j = 0; j < mineField.length; j++) {
                if (mineGrid[i][j]) {
                    if (mineField[i][j].equals(" " + getNumAdjMines(i, j) + " ")) {
                        isLost = true;
                    } else {
                        isLost = false;
                        printLoss();
                    }
                }
            }
        }
        return isLost;   
    }

    /**
     * Executes the game loop which starts the game.
     *
     */
    
    public void play() {
        printWelcome();
        while (true) {
            if (noFog) {
                printNoFog();
                System.out.println("");
                System.out.print("minesweeper-alpha: ");
                noFog = false;
            } else {
                printMineField();
                System.out.println("");
                System.out.print("minesweeper-alpha: ");
            }
            promptUser();    
            if (isWon()) {
                printWin();
                break;
            }
        }    
    }

    /**
     * Calculates and returns the players score.
     * 
     * @return the score.
     */

    public double getScore() {
        score = 100.0 * rows * cols / rounds;
        return score;
    }

    /**
     * Prints the win message and exits the game.
     *
     */
    
    public void printWin() {
        System.out.println("");
        System.out.println(" ░░░░░░░░░▄░░░░░░░░░░░░░░▄░░░░ \"So Doge\"");
        System.out.println(" ░░░░░░░░▌▒█░░░░░░░░░░░▄▀▒▌░░░");
        System.out.println(" ░░░░░░░░▌▒▒█░░░░░░░░▄▀▒▒▒▐░░░ \"Such Score\"");
        System.out.println(" ░░░░░░░▐▄▀▒▒▀▀▀▀▄▄▄▀▒▒▒▒▒▐░░░");
        System.out.println(" ░░░░░▄▄▀▒░▒▒▒▒▒▒▒▒▒█▒▒▄█▒▐░░░ \"Much Minesweeping\"");
        System.out.println(" ░░░▄▀▒▒▒░░░▒▒▒░░░▒▒▒▀██▀▒▌░░░");
        System.out.println(" ░░▐▒▒▒▄▄▒▒▒▒░░░▒▒▒▒▒▒▒▀▄▒▒▌░░ \"Wow\"");
        System.out.println(" ░░▌░░▌█▀▒▒▒▒▒▄▀█▄▒▒▒▒▒▒▒█▒▐░░");
        System.out.println(" ░▐░░░▒▒▒▒▒▒▒▒▌██▀▒▒░░░▒▒▒▀▄▌░");
        System.out.println(" ░▌░▒▄██▄▒▒▒▒▒▒▒▒▒░░░░░░▒▒▒▒▌░");
        System.out.println(" ▀▒▀▐▄█▄█▌▄░▀▒▒░░░░░░░░░░▒▒▒▐░");
        System.out.println(" ▐▒▒▐▀▐▀▒░▄▄▒▄▒▒▒▒▒▒░▒░▒░▒▒▒▒");
        System.out.println(" ▐▒▒▒▀▀▄▄▒▒▒▄▒▒▒▒▒▒▒▒░▒░▒░▒▒▐░");
        System.out.println(" ░▌▒▒▒▒▒▒▀▀▀▒▒▒▒▒▒░▒░▒░▒░▒▒▒▌░");
        System.out.println(" ░▐▒▒▒▒▒▒▒▒▒▒▒▒▒▒░▒░▒░▒▒▄▒▒▐░░");
        System.out.println(" ░░▀▄▒▒▒▒▒▒▒▒▒▒▒░▒░▒░▒▄▒▒▒▒▌░░");
        System.out.println(" ░░░░▀▄▒▒▒▒▒▒▒▒▒▒▄▄▄▀▒▒▒▒▄▀░░░ CONGRATULATIONS!");
        System.out.println(" ░░░░░░▀▄▄▄▄▄▄▀▀▀▒▒▒▒▒▄▄▀░░░░░ YOU HAVE WON!");
        System.out.println(" ░░░░░░░░░▒▒▒▒▒▒▒▒▒▒▀▀░░░░░░░░ SCORE: " + getScore());
        System.out.println("");
        System.exit(0);
    }

    /**
     * Prints the lose message and exists the game.
     *
     */

    public void printLoss() {
        System.out.println("");
        System.out.println(" Oh no... You revealed a mine!");
        System.out.println("  __ _  __ _ _ __ ___   ___    _____   _____ _ __");
        System.out.println(" / _` |/ _` | '_ ` _ \\ / _ \\  / _ \\ \\ / / _ \\ '__|");
        System.out.println("| (_| | (_| | | | | | |  __/ | (_) \\ V /  __/ |");
        System.out.println(" \\__, |\\__,_|_| |_| |_|\\___|  \\___/ \\_/ \\___|_|");
        System.out.println(" |___/");
        System.out.println("");
        System.exit(0);
    }
    
}

        
