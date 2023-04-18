import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static Set<PrintWriter> clients = new HashSet<>();
    private static Map<String, PrintWriter> nameToClient = new HashMap<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Servidor iniciado na porta 5000");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Cliente conectado");

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String name = in.readLine();
            System.out.println(name + " entrou no chat");
            out.println(name + "");

            for (PrintWriter client : clients) {
                client.println(name + " entrou no chat");
            }

            clients.add(out);
            nameToClient.put(name, out);

            new Thread(() -> {
                try {
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        System.out.println(name + ": " + inputLine);
                        for (PrintWriter client : clients) {
                            client.println(name + ": " + inputLine);
                        }
                    }
                } catch (IOException e) {
                    System.out.println(name + " saiu do chat");
                    clients.remove(out);
                    nameToClient.remove(name);
                    for (PrintWriter client : clients) {
                        client.println(name + " saiu do chat");
                    }
                }
            }).start();
        }
    }
}