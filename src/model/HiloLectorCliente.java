/*
	Hecho por:
		Eloy Guillermo Villad�niga M�rquez
		e
		Iv�n M�rquez Garc�a
		
	2� D.A.M.
	
	Pr�ctica "Chat Colectivo" - Programaci�n de Servicios y Procesos
	
	
	
	------------------------------- DESCRIPCI�N -------------------------------
	
	Permite al cliente recibir informaci�n del servidor de forma continua sin
	limitar su capacidad de enviar informaci�n.
*/



package model;



import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.net.Socket;
import java.net.SocketException;

import javafx.application.Platform;



public class HiloLectorCliente implements Runnable {
	
    Socket socketCliente;
    Cliente cliente;
    DataInputStream input;

    
    
    public HiloLectorCliente(Cliente cliente) {
        this.cliente = cliente;
        this.socketCliente = cliente.getSocket();
    }

    
    
    @Override 
    public void run() {
    	boolean correcto = true;
    	
    	try {
	        // Iniciar flujo de entrada
	    	input = new DataInputStream(socketCliente.getInputStream());
    	}
    	catch (IOException ex) {
    		correcto = false;
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
    	
        while (cliente.getEstado() == 1 && correcto == true) {
        	// Setear mensaje por defecto y ocultar errorText
        	cliente.errorText.setVisible(false);
        	cliente.errorText.setText("Error de conexi�n");

            // recibir del servidor
            String mensaje = null;
            try {
            	mensaje = input.readUTF();
            	
            	if (mensaje.equals("|/\\\\/\\//\\|")) {
            		cliente.errorText.setText("Servidor desconectado");
            		cliente.errorText.setVisible(true);
            		
					cliente.desconectar();
					
					mensaje = null;
            	}
            }
            catch(SocketException se) {
            	correcto = false;
            	
            	System.out.println("-----------------------------------------------------------");
            	//se.printStackTrace();
            	if (socketCliente.isClosed() == true || socketCliente.isInputShutdown() == true)
            		System.out.println("Flujo de entrada cerrado.");
            	System.out.println("-----------------------------------------------------------");
            }
            catch(IOException ioe) {
            	correcto = false;
            	
            	System.out.println("-----------------------------------------------------------");
            	ioe.printStackTrace();
            	System.out.println("Error de e/s.");
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
