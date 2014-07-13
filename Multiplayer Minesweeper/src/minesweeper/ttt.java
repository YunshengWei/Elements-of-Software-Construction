package minesweeper;

import java.io.IOException;
import java.net.*;

public class ttt {
    public static void main(String[] args) throws UnknownHostException, IOException {
        Socket s = new Socket("localhost", 34341);
        s.close();
        if (s.isClosed()) System.out.println(true);
        s.close();
    }
}
