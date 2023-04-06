package minihttpserver;

import java.io.*;
import java.net.*;

public class MiniHttpServer extends Thread {

    Socket socketCliente;

    public MiniHttpServer(Socket socketCliente) {
        this.socketCliente = socketCliente;
    }

    public void run() {

        try {
            BufferedReader leitor = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
            DataOutputStream escritor = new DataOutputStream(socketCliente.getOutputStream());

            String requisicao = leitor.readLine();
            String[] partesRequisicao = requisicao.split(" ");
            String metodo = partesRequisicao[0];
            String arquivo = partesRequisicao[1];
            System.out.println(metodo);
            if (metodo.equals("GET")) {
                if (arquivo.equals("/carr.jpeg")) {
                    File file = new File("C:\\Users\\unifjmartins\\Documents\\NetBeansProjects\\MiniHttpServer\\src\\minihttpserver\\carr.jpeg");
                    FileInputStream fileInput = new FileInputStream(file);
                    byte[] buffer = new byte[1024 * 1024];
                    int bytesRead;

                    escritor.writeBytes("HTTP/1.1 200 OK\r\n");
                    escritor.writeBytes("Content-Type: image/jpeg\r\n");
                    escritor.writeBytes("Content-Length: " + file.length() + "\r\n");
                    escritor.writeBytes("\r\n");

                    while ((bytesRead = fileInput.read(buffer)) != -1) {
                        escritor.write(buffer, 0, bytesRead);
                    }

                    fileInput.close();
                } else {
                    escritor.writeBytes("HTTP/1.1 404 Not Found\r\n");
                    escritor.writeBytes("\r\n");
                }
            } else {
                escritor.writeBytes("HTTP/1.1 405 Method Not Allowed\r\n");
                escritor.writeBytes("\r\n");
            }

            socketCliente.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

        try {
            ServerSocket servidor = new ServerSocket(8090);

            while (true) {
                Socket socketCliente = servidor.accept();
                MiniHttpServer threadServidor = new MiniHttpServer(socketCliente);
                threadServidor.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
