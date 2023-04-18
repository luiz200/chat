import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class Cliente {
    public static void main(String[] args) {
        // Declaração de variáveis
        long startTime, endTime, tcpTime, udpTime;
        int[] numeros = { 1024, 10024, 20024, 30024, 40024, 50024, 60024};

        // Loop sobre o array de inteiros
        for(int dataSize : numeros){
            System.out.println("Tamanho da mensagem: " + dataSize);

            // Envia dados via TCP
            try (Socket socket = new Socket("localhost", 1234)) {
                OutputStream out = socket.getOutputStream();
                byte[] buffer = new byte[dataSize];
                startTime = System.nanoTime(); // Obtemos o tempo de início da transmissão
                out.write(buffer); // Escrevemos os dados no output stream
                endTime = System.nanoTime(); // Obtemos o tempo de término da transmissão
                tcpTime = endTime - startTime; // Calculamos o tempo de transmissão
                System.out.println("TCP transfer rate: " + (double)dataSize / tcpTime * 1000000000 + " bytes/sec");
                // Calcula a taxa de transferência em bytes/segundo e exibe na tela
                System.out.println("Tempo de envio TCP: " + tcpTime / 1000000000.0 + " segundos");
                // Exibe o tempo de envio em segundos
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Envia dados via UDP
            try (DatagramSocket socket = new DatagramSocket()) {
                byte[] buffer = new byte[dataSize];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getLocalHost(), 5678);
                startTime = System.nanoTime(); // Obtemos o tempo de início da transmissão
                socket.send(packet); // Enviamos o pacote através do socket UDP
                endTime = System.nanoTime(); // Obtemos o tempo de término da transmissão
                udpTime = endTime - startTime; // Calculamos o tempo de transmissão
                System.out.println();
                System.out.println("UDP transfer rate: " + (double)dataSize / udpTime * 1000000000 + " bytes/sec");
                // Calcula a taxa de transferência em bytes/segundo e exibe na tela
                System.out.println("Tempo de envio UDP: " + udpTime / 1000000000.0 + " segundos");
                // Exibe o tempo de envio em segundos
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}