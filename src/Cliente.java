import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class Cliente {
    public static void main(String[] args) {
        long startTime, endTime, tcpTime, udpTime;
        int[] numeros = { 1024, 10024, 20024, 30024, 40024, 50024, 60024};

        for(int dataSize : numeros){
            System.out.println("Tamanho da mensagem: " + dataSize);
            // Envia dados via TCP
            try (Socket socket = new Socket("localhost", 1234)) {
                OutputStream out = socket.getOutputStream();
                byte[] buffer = new byte[dataSize];
                startTime = System.nanoTime();
                out.write(buffer);
                endTime = System.nanoTime();
                tcpTime = endTime - startTime;
                System.out.println("TCP transfer rate: " + (double)dataSize / tcpTime * 1000000000 + " bytes/sec");
                // converta para segundos
                System.out.println("Tempo de envio TCP: " + tcpTime / 1000000000.0 + " segundos");
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Envia dados via UDP
            try (DatagramSocket socket = new DatagramSocket()) {
                byte[] buffer = new byte[dataSize];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getLocalHost(), 5678);
                startTime = System.nanoTime();
                socket.send(packet);
                endTime = System.nanoTime();
                udpTime = endTime - startTime;
                System.out.println();
                System.out.println("UDP transfer rate: " + (double)dataSize / udpTime * 1000000000 + " bytes/sec");
                System.out.println("Tempo de envio UDP: " + udpTime / 1000000000.0 + " segundos");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}