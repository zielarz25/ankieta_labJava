package app.server;

import app.server.utils.DBUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main class of server. Creates new threat for each client
 */
public class Server {
    private static int port;

    public static void main(String[] args) throws IOException {

        Scanner sc = new Scanner(System.in);
        System.out.println("Podaj port na ktorym serwer ma oczekiwac na klientow: ");
        setPort(Integer.parseInt(sc.nextLine()));

        DBUtils.dbConnect();
        DBUtils.fillDatabase();

        ServerSocket serverSocket = null;
        ExecutorService executor = Executors.newCachedThreadPool();

        try {
            // create server socket
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

    public static int getPort() {
        return port;
    }

    public static void setPort(int port) {
        Server.port = port;
    }
}
