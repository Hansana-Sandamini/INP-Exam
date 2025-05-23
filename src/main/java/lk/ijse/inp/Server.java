package lk.ijse.inp;

import javafx.fxml.Initializable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Server extends Thread implements Initializable {

    boolean isRunning = true;

    @Override
    public void run() {
    }

    private List<ClientHandler> clients = new ArrayList<>();
    private String clientName = Client.class.getSimpleName();
    private String clientName2 = Client2.class.getSimpleName();

    private void setClientName(String clientName, String clientName2) {
        this.clientName = clientName;
        this.clientName2 = clientName2;
    }

    private void connectToClient() {
        new Thread(() -> {
            try {
                setClientName(clientName, clientName2);
                Socket socket1 = new Socket("localhost", 3000);
                System.out.println("Connected to " + socket1.getRemoteSocketAddress());
                System.out.println("Client" + clientName + "joined to chat!");
                System.out.println("Client" + clientName2 + "left to chat!");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(3000);
            System.out.println("Server started. Waiting for client connection...");
            System.out.println("Start Date & Time: " + LocalDate.now() + "  " + LocalTime.now());

            Socket socket = serverSocket.accept();
            System.out.println("Client connected!");

            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            Scanner input = new Scanner(System.in);

            while (true) {
                String clientMessage = dataInputStream.readUTF();
                System.out.println("Client says: " + clientMessage);

                if (clientMessage.equalsIgnoreCase("exit")) {
                    break;
                }

                System.out.print("Enter message for client (type 'exit' to quit): ");
                String serverMessage = input.nextLine();
                dataOutputStream.writeUTF(serverMessage);
                dataOutputStream.flush();

                if (serverMessage.equalsIgnoreCase("exit")) {
                    System.out.println("Client disconnected!");
                    break;
                }
            }

            socket.close();
            serverSocket.close();
            System.out.println("Server stopped!");
            System.out.println("End Date & Time: " + LocalDate.now() + "  " + LocalTime.now());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
            connectToClient();
        new Thread(() -> {
            ServerSocket serverSocket;

            try {
                serverSocket = new ServerSocket(3001);
                System.out.println("Server started. Waiting for clients...");

                while (isRunning) {
                    Socket socket = serverSocket.accept();
                    ClientHandler clientHandler = new ClientHandler();
                    clients.add(clientHandler);
                    clientHandler.start();
                }
            } catch (IOException e) {
                if (isRunning) {
                    System.out.println("Server error: " + e.getMessage() + "\n");
                }
            }
        }).start();
    }

    private class ClientHandler {
        DataInputStream dataInputStream;
        DataOutputStream dataOutputStream;

        public void start() throws IOException {
            while (isRunning) {
                dataInputStream.readUTF();
            }
        }
    }
}