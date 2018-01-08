package app.server;

import app.server.utils.DBUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
    private static final int port = 45000;


    public static void main(String[] args) throws IOException {

        DBUtils.dbConnect();
        DBUtils.fillDatabase();

        ServerSocket serverSocket = null;
        ExecutorService executor = Executors.newCachedThreadPool();

        try {
            // create app.server socket
            serverSocket = new ServerSocket(port);

            while (true) {
            // wait for clients
            Socket socket = serverSocket.accept();
            executor.execute(new ServerThread(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            serverSocket.close();
            System.out.println("Server socket closed!");
            DBUtils.dbDisconnect();



        }

    }
}
