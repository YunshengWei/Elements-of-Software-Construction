package minesweeper.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import minesweeper.Board;

public class MinesweeperServerRunnable implements Runnable {

    private final Socket socket;
    private final Board board;
    private boolean readyToClose = false;
    private boolean debug;

    public MinesweeperServerRunnable(Socket socket, Board board, boolean debug) {
        this.socket = socket;
        this.board = board;
        this.debug = debug;
    }

    @Override
    public void run() {
        handleConnection();
    }

    /**
     * Handle a single client connection. Returns when client disconnects.
     * 
     */
    private void handleConnection(){
        try (
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            ) {
        	out.print(String.format(
        			"Welcome to Minesweeper. Board: %s columns by %s rows."
        		  + " Players: %s including you. Type 'help' for help.%n",
        		    board.getSizeX(), board.getSizeY(), Thread.activeCount() - 1));
            for (String line = in.readLine(); line != null && !readyToClose; line = in.readLine()) {
               String output = handleRequest(line);
               if (output != null) {
                   out.print(output);
               }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Handler for client input, performing requested operations and returning
     * an output message.
     * 
     * @param input
     *            message from client
     * @return message to client
     */
    private String handleRequest(String input) {
        String regex = "(look)|(dig -?\\d+ -?\\d+)|(flag -?\\d+ -?\\d+)|"
                + "(deflag -?\\d+ -?\\d+)|(help)|(bye)";
        if (!input.matches(regex)) {
            // invalid input
            return handleHelp();
        }
        String[] tokens = input.split(" ");
        if (tokens[0].equals("look")) {
            // 'look' request
            // TODO Question 5
            return handleLook();
        } else if (tokens[0].equals("help")) {
            // 'help' request
            // TODO Question 5
        	return handleHelp();
        } else if (tokens[0].equals("bye")) {
            // 'bye' request
            // TODO Question 5
        	return handleBye();
        } else {
            int x = Integer.parseInt(tokens[1]);
            int y = Integer.parseInt(tokens[2]);
            if (tokens[0].equals("dig")) {
                // 'dig x y' request
                // TODO Question 5
            	return handleDig(x, y);
            } else if (tokens[0].equals("flag")) {
                // 'flag x y' request
                // TODO Question 5
            	return handleFlag(x, y);
            } else if (tokens[0].equals("deflag")) {
                // 'deflag x y' request
                // TODO Question 5
            	return handleDeflag(x, y);
            }
        }
        // Should never get here--make sure to return in each of the valid cases
        // above.
        throw new UnsupportedOperationException();
    }
    
    private String handleLook() {
    	return board.toString();
    }
    
    private String handleHelp() {
    	return String.format("HELP!%n");
    }
    
    private String handleBye() {
    	readyToClose = true;
    	return null;
    }
    
    private String handleDig(int x, int y) {
    	if (x >= 0 && x < board.getSizeX() && y >= 0 && y < board.getSizeY()
    		&& board.getState(x, y) == '-') {
    		if (board.dig(x, y)) {
    			if (debug == false) {
    				readyToClose = true;
    			}
    			return String.format("BOOM!%n");
    		}
    	}
    	return board.toString();
    }
    
    private String handleFlag(int x, int y) {
    	if (x >= 0 && x < board.getSizeX() && y >= 0 && y < board.getSizeY()
        		&& board.getState(x, y) == '-') {
        		board.setFlag(x, y);
        	}
    	return board.toString();
    }
    
    private String handleDeflag(int x, int y) {
    	if (x >= 0 && x < board.getSizeX() && y >= 0 && y < board.getSizeY()
        		&& board.getState(x, y) == 'F') {
        		board.deFlag(x, y);
        	}
    	return board.toString();
    }
}
