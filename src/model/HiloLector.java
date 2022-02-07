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
	
    Socket socketCliente;
    Cliente cliente;
    DataInputStream input;

    
    
    public HiloLector(Cliente cliente) {
        this.cliente = cliente;
        this.socketCliente = cliente.getSocket();
    }

    
    
    @Override 
    public void run() {
    	try {
	        // Iniciar flujo de entrada
	    	input = new DataInputStream(socketCliente.getInputStream());
    	}
    	catch (IOException ex) {
    		String errorMsg = "Error al crear el socket del cliente"; 
            
    		if (socketCliente == null)
        		errorMsg = "Flujo de entrada cerrado";
        		
        		System.out.println("-----------------------------------------------------------");
        		ex.printStackTrace();
        		System.out.println(errorMsg);
        		System.out.println("-----------------------------------------------------------");
        		
        		
        		cliente.errorText.setText(errorMsg);
        		cliente.errorText.setVisible(true);
        }
    	
        while (cliente.getEstado() == 1) {
        	// Setear mensaje por defecto y ocultar errorText
        	cliente.errorText.setVisible(false);
        	cliente.errorText.setText("Error de conexi�n");

            // recibir del servidor
            String mensaje = null;
            try {
            	mensaje = input.readUTF();
            } catch(IOException ioe) {
            	System.out.println("-----------------------------------------------------------");
            	ioe.printStackTrace();
            	if (socketCliente.isClosed() == true || socketCliente.isInputShutdown() == true)
            		System.out.println("Flujo de entrada cerrado.");
            	System.out.println("-----------------------------------------------------------");
            }

            if (mensaje != null) {
                // imprimirla en el �rea de texto
            	String msg = mensaje;
                Platform.runLater(() -> {
                	cliente.textArea.appendText(msg + "\n");
                });
            }
        }
        }
    
}
