import java.io.*;
import java.net.*;

public class ChatClient {
    public static void main(String[] args) throws IOException {
        // Cria um socket que se conecta ao servidor na porta 5000
        Socket socket = new Socket("localhost", 5000);
        System.out.println("Conectado ao servidor");

        // Cria um BufferedReader para receber mensagens do servidor
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        // Cria um PrintWriter para enviar mensagens ao servidor
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        // Cria um BufferedReader para ler entradas do usuário pelo console
        BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));

        // Solicita o nome do cliente ao usuário pelo console
        System.out.print("Digite seu nome: ");
        String name = consoleIn.readLine();
        out.println(name); // Envia o nome do cliente para o servidor

        // Cria uma nova thread para receber mensagens do servidor em tempo real
        new Thread(() -> {
            try {
                String serverInput;
                while ((serverInput = in.readLine()) != null) {
                    System.out.println(serverInput); // Imprime a mensagem recebida do servidor
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        // Aguarda por entradas do usuário pelo console e envia-as ao servidor
        String userInput;
        while ((userInput = consoleIn.readLine()) != null) {
            out.println(userInput); // Adiciona o nome do cliente à mensagem e envia ao servidor
        }

        // Fecha o socket ao finalizar o programa
        socket.close();
    }
}
