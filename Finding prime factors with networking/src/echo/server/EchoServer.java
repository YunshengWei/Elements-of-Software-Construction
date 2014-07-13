package echo.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * A simple server that will echo client inputs.
 */
public class EchoServer {

    /**
     * @param args
     *            String array containing Program arguments. It should only
     *            contain at most one String indicating the port it should
     *            connect to. The String should be parseable into an int. If no
     *            arguments, we default to port 4444.
     */
    public static void main(String[] args) throws IOException {
        //TODO complete this implementation.
        int port = 4444;
        if (args.length > 1) {
            System.err.println("There should be at most one argument.");
            System.exit(1);
        } else if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        }
        try (ServerSocket serverSocket = new ServerSocket(port);) {
            while (true) {
                try (
                    Socket clientSocket = serverSocket.accept();
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
                    ) {
                    String inputline;
                    while ((inputline = in.readLine()) != null) {
                        out.println(inputline);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }
}
