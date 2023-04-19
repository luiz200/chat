import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;


import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.ChartUtilities;

public class Cliente {
    public static void main(String[] args) {
        // Declaração de variáveis
        long startTime, endTime, tcpTime, udpTime;
        int[] numeros = {1024, 10024, 20024, 30024, 40024, 50024, 60024};
        List<Double> taxaTransferenciaTcp = new ArrayList<>();
        List<Double> taxaTransferenciaUdp = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("#.##");

        // Loop sobre o array de inteiros
        for (int dataSize : numeros) {

            System.out.println("Tamanho da mensagem: " + dataSize);

            // Envia dados via TCP
            try (Socket socket = new Socket("localhost", 1234)) {
                OutputStream out = socket.getOutputStream();
                byte[] buffer = new byte[dataSize];
                startTime = System.nanoTime(); // Obtemos o tempo de início da transmissão
                out.write(buffer); // Escrevemos os dados no output stream
                endTime = System.nanoTime(); // Obtemos o tempo de término da transmissão
                tcpTime = endTime - startTime; // Calculamos o tempo de transmissão
                System.out.println("Taxa de transferência TCP: " + (double) dataSize / tcpTime * 1000000000 + " bytes/sec");
                taxaTransferenciaTcp.add(Double.parseDouble(df.format((double) dataSize / tcpTime * 1000000000).replace(",",".")));

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
                System.out.println("Taxa de transferência UDP: " + (double) dataSize / udpTime * 1000000000 + " bytes/sec");
                // Calcula a taxa de transferência em bytes/segundo e exibe na tela
                System.out.println("Tempo de envio UDP: " + udpTime / 1000000000.0 + " segundos");
                taxaTransferenciaUdp.add(Double.parseDouble(df.format((double) dataSize / udpTime * 1000000000).replace(",",".")));
                System.out.println(taxaTransferenciaTcp.get(0));
                // Exibe o tempo de envio em segundos
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        CategoryDataset dataset = createDataset(taxaTransferenciaTcp, taxaTransferenciaUdp);
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 300));

        // Criação da janela para exibir o gráfico
        JFrame frame = new JFrame("Gráfico de Barra");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(chartPanel);
        frame.pack();
        frame.setVisible(true);
    }
    private static CategoryDataset createDataset ( List<Double> taxaTransferenciaTcp, List<Double> taxaTransferenciaUdp){

        String series1 = "TCP";
        String series2 = "UDP";

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i = 0; i < taxaTransferenciaTcp.size(); i++) {
            dataset.addValue(taxaTransferenciaTcp.get(i), series1, Integer.toString(i + 1));
            dataset.addValue(taxaTransferenciaUdp.get(i), series2, Integer.toString(i + 1));
        }

        return dataset;
    }
    private static JFreeChart createChart (CategoryDataset dataset){

        String title = "Comparação de Taxa de Transferência entre TCP e UDP";
        String categoryAxisLabel = "Tamanho da mensagem";
        String valueAxisLabel = "Taxa de Transferência (bytes/sec)";

        JFreeChart chart = ChartFactory.createBarChart(title, categoryAxisLabel, valueAxisLabel, dataset, PlotOrientation.VERTICAL, true, true, false);

        // Salva o gráfico em um arquivo
        try {
            ChartUtilities.saveChartAsJPEG(new File("grafico.jpg"), chart, 800, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, Color.BLUE);
        renderer.setSeriesPaint(1, Color.RED);
        renderer.setDrawBarOutline(false);

        CategoryAxis xAxis = plot.getDomainAxis();
        xAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);


        return chart;
    }
}
