import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    // Cria um conjunto de objetos PrintWriter, que serão responsáveis por enviar mensagens para os clientes
    private static Set<PrintWriter> clients = new HashSet<>();

    // Cria um mapa que associa o nome do cliente ao seu respectivo objeto PrintWriter
    private static Map<String, PrintWriter> nameToClient = new HashMap<>();

    public static void main(String[] args) throws IOException {
        // Cria um objeto ServerSocket que irá ouvir a porta 5000
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Servidor iniciado na porta 5000");

        // Loop infinito que aguarda a conexão de novos clientes
        while (true) {
            // Aceita a conexão do cliente e cria um novo objeto Socket para se comunicar com ele
            Socket clientSocket = serverSocket.accept();
            System.out.println("Cliente conectado");

            // Cria um objeto BufferedReader para receber mensagens do cliente
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Cria um objeto PrintWriter para enviar mensagens para o cliente
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Lê o nome do cliente enviado pelo BufferedReader e imprime uma mensagem de boas-vindas para ele
            String name = in.readLine();
            System.out.println(name + " entrou no chat");
            out.println("Bem-vindo(a) ao chat, " + name + "!");

            // Percorre todos os clientes conectados e envia uma mensagem informando que um novo cliente entrou no chat
            for (PrintWriter client : clients) {
                client.println(name + " entrou no chat");
            }

            // Adiciona o objeto PrintWriter do novo cliente ao conjunto de clientes e ao mapa nameToClient
            clients.add(out);
            nameToClient.put(name, out);

            // Cria uma nova thread para receber as mensagens do cliente
            new Thread(() -> {
                try {
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        if (inputLine.startsWith("/exit")) {
                            out.println("Você saiu do chat");
                            clientSocket.close(); // Encerra a conexão com o cliente
                        } else {
                            // Imprime a mensagem recebida juntamente com o nome do cliente que a enviou
                            System.out.println(name + ": " + inputLine);

                            // Percorre todos os clientes conectados e envia a mensagem recebida juntamente com o nome do cliente que a enviou
                            for (PrintWriter client : clients) {
                                client.println(name + ": " + inputLine);
                            }
                        }

                    }

                } catch (IOException e) {
                    // Se ocorrer uma exceção, imprime uma mensagem informando que o cliente saiu do chat
                    System.out.println(name + " saiu do chat");

                    // Remove o objeto PrintWriter do cliente do conjunto de clientes e do mapa nameToClient
                    clients.remove(out);
                    nameToClient.remove(name);

                    // Percorre todos os clientes conectados e envia uma mensagem informando que o cliente saiu do chat
                    for (PrintWriter client : clients) {
                        client.println(name + " saiu do chat");
                    }
                }
            }).start();
        }
    }
}