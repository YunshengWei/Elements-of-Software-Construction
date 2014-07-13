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

    public MinesweeperServerRunnable(Socket socket, Board board) {
        this.socket = socket;
        this.board = board;
    }

    @Override
    public void run() {
        

    }

    /**
     * Handle a single client connection. Returns when client disconnects.
     * 
     * @throws IOException
     *             if connection has an error or terminates unexpectedly
     */
    private void handleConnection() throws IOException {
        try (
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            ) {
            for (String line = in.readLine(); line != null; line = in.readLine()) {
               String output = handleRequest(line);
               if (output != null) {
                   out.println(output);
               }
            }
            if (socket.is)
            socket.close();
        }
//        BufferedReader in = new BufferedReader(new InputStreamReader(
//                socket.getInputStream()));
//        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//
//        try {
//            for (String line = in.readLine(); line != null; line = in
//                    .readLine()) {
//                String output = handleRequest(line);
//                if (output != null) {
//                    out.println(output);
//                }
//            }
//        } finally {
//            out.close();
//            in.close();
//        }
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
            return null;
        }
        String[] tokens = input.split(" ");
        if (tokens[0].equals("look")) {
            // 'look' request
            // TODO Question 5
        } else if (tokens[0].equals("help")) {
            // 'help' request
            // TODO Question 5
        } else if (tokens[0].equals("bye")) {
            // 'bye' request
            // TODO Question 5
        } else {
            int x = Integer.parseInt(tokens[1]);
            int y = Integer.parseInt(tokens[2]);
            if (tokens[0].equals("dig")) {
                // 'dig x y' request
                // TODO Question 5
            } else if (tokens[0].equals("flag")) {
                // 'flag x y' request
                // TODO Question 5
            } else if (tokens[0].equals("deflag")) {
                // 'deflag x y' request
                // TODO Question 5
            }
        }
        // Should never get here--make sure to return in each of the valid cases
        // above.
        throw new UnsupportedOperationException();
    }
}
