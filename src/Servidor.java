import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static void main(String[] args) {
        // Inicia servidor TCP
        try (ServerSocket tcpSocket = new ServerSocket(1234)) {
            while (true) {
                try (Socket clientSocket = tcpSocket.accept()) {
                    // Recebe dados do cliente TCP
                    InputStream in = clientSocket.getInputStream();
                    byte[] buffer = new byte[1024];
                    int bytesRead = in.read(buffer);
                    while (bytesRead != -1) {
                        bytesRead = in.read(buffer);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Inicia servidor UDP
        try (DatagramSocket udpSocket = new DatagramSocket(5678)) {
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            while (true) {
                udpSocket.receive(packet);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}