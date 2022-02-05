/*
	Hecho por:
		Eloy Guillermo Villad�niga M�rquez
		e
		Iv�n M�rquez Garc�a
		
	2� D.A.M.
	
	Pr�ctica "Chat Colectivo" - Programaci�n de Servicios y Procesos
	
	
	
	------------------------------- DESCRIPCI�N ------------------------------- MEJORAR DESCRIPCI�N
	Representa cada nueva conexi�n.
*/



package model;



import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.net.Socket;

import javafx.application.Platform;



public class TaskClientConnection implements Runnable {

    Socket socket;
    Servidor server;
    DataInputStream input;
    DataOutputStream output;

    public TaskClientConnection(Socket socket, Servidor server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            // Iniciar flujos de entrada y salida
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());

            while (true) {
                // Reciber mensaje de cliente
                String message = input.readUTF();

                // Enviar mensaje a todos
                server.mensajeParaTodos(message);
                
                // Mostrar mensaje en el �rea de texto
                Platform.runLater(() -> {                    
                    server.textArea.appendText(message + "\n");
                });
            }
        }
        catch (IOException ex) { ex.printStackTrace(); }
        finally {
            try { socket.close(); }
            catch (IOException ex) { ex.printStackTrace(); }
        }
    }

    //send message back to client - NI IDEA DE QU� HACE ESTO
    public void sendMessage(String message) {
          try {
            output.writeUTF(message);
            output.flush();

        } catch (IOException ex) {
            ex.printStackTrace();
        } 
       
    }

}
