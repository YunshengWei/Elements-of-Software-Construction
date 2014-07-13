package echo.client;

import java.io.*;
import java.net.*;

/**
 * A simple client that will interact with an EchoServer.
 */
public class EchoClient {

    /**
     * @param args
     *            String array containing Program arguments. It should only
     *            contain exactly one String indicating which server to connect
     *            to. We require that this string be in the form
     *            hostname:portnumber.
     */
    public static void main(String[] args) throws IOException {
        // TODO Complete this implementation.
        if (args.length != 1) {
            System.err.println(
                "Usage: java EchoClient <hostname>:<portnumber>");
            System.exit(1);
        }
        
        String[] tmp = args[0].split(":");
        String hostName = tmp[0];
        int portNumber = Integer.valueOf(tmp[1]);
        
        try (
            Socket clientSocket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(
                clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    clientSocket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(
                new InputStreamReader(System.in));
            ) {
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
                System.out.println(">>> " + in.readLine());
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
