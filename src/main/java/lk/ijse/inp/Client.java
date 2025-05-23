package lk.ijse.inp;

import javafx.fxml.Initializable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Client implements Initializable {

    private void connectToServer(String host, int port) throws IOException {
        Socket socket = new Socket("localhost", 3000);
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        Scanner scanner = new Scanner(System.in);
        System.out.println("Connected to " + host + ":" + port);
        System.out.print("Enter your name:");
        String name = scanner.nextLine();
        dos.writeUTF(name);
        dos.flush();
        String response = dis.readUTF();
        System.out.println(response);
        dos.close();
        dis.close();
        socket.close();
    }

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 3000);
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

            System.out.println("Connected to server!");

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your name: ");
            String clientName = scanner.nextLine();

            System.out.println(clientName + " is joined to chat!");

            Scanner input = new Scanner(System.in);

            while (true) {
                System.out.print("Enter message for server (type 'exit' to quit): ");
                String clientMessage = input.nextLine();
                dataOutputStream.writeUTF(clientMessage);
                dataOutputStream.flush();

                if (clientMessage.equalsIgnoreCase("exit")) {
                    break;
                }

                String serverMessage = dataInputStream.readUTF();
                System.out.println("Server says: " + serverMessage);

                if (serverMessage.equalsIgnoreCase("exit")) {
                    break;
                }
            }

            socket.close();
            System.out.println("Stopped from Server. Bye!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            new Client();
            connectToServer("localhost", 3000);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}