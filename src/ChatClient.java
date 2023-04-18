import java.io.*;
import java.net.*;

public class ChatClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 5000);
        System.out.println("Conectado ao servidor");

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));

        // Solicita o nome do cliente
        System.out.print("Digite seu nome: ");
        String name = consoleIn.readLine();
        out.println(name); // Envia o nome do cliente para o servidor

        new Thread(() -> {
            try {
                String serverInput;
                while ((serverInput = in.readLine()) != null) {
                    System.out.println(serverInput);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        String userInput;
        while ((userInput = consoleIn.readLine()) != null) {
            out.println(userInput); // Adiciona o nome do cliente à mensagem
        }

        socket.close();
    }
}
