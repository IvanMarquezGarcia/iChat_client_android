/*
	Hecho por:
		Eloy Guillermo Villad�niga M�rquez
		e
		Iv�n M�rquez Garc�a
		
	2� D.A.M.
	
	Pr�ctica "Chat Colectivo" - Programaci�n de Servicios y Procesos
	
	
	
	------------------------------- DESCRIPCI�N ------------------------------- MEJORAR DESCRIPCI�N
	Permite al cliente recibir informaci�n del servidor de forma continua.
*/



package model;



import java.io.DataInputStream;
import java.io.IOException;

import java.net.Socket;

import javafx.application.Platform;



public class HiloLector implements Runnable {
	
    Socket socket;
    Cliente cliente;
    DataInputStream input;

    
    
    public HiloLector(Socket socket, Cliente cliente) {
        this.socket = socket;
        this.cliente = cliente;
    }

    
    
    @Override 
    public void run() {
        while (true) {
            try {
                // Iniciar flujo de entrada
                input = new DataInputStream(socket.getInputStream());

                // recibir del cliente
                String message = input.readUTF();

                // imprimirla en el �rea de texto
                Platform.runLater(() -> {
                    cliente.textArea.appendText(message + "\n");
                });
            }
            catch (IOException ex) {
            	// En caso de excepciones de e/s, imprimirlas por consola y detener la escucha
                System.out.println("Error reading from server: " + ex.getMessage());
                ex.printStackTrace();
                break;
            }
        }
    }
    
}
