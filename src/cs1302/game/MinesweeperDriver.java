package cs1302.game;

import java.util.*;

/**
 * This class contains the main method
 * and is the runner class for {@Code MinesweeperGame}.
 */

public class MinesweeperDriver {
    public static void main(String [] args) {

        String inputSeedFile = "";
        
        if (args[0].equals("--seed")) {
            inputSeedFile = args[1];
        } else {
            System.err.println("Unable to interpret supplied command-line arguments.");
            System.exit(1);
        }

        if (inputSeedFile.length() > 0) {
            MinesweeperGame g = new MinesweeperGame(inputSeedFile);
            g.play();
        }
            
    }
}

