/*
	Hecho por:
		Eloy Guillermo Villad�niga M�rquez
		e
		Iv�n M�rquez Garc�a
		
	2� D.A.M.
	
	Pr�ctica "Chat Colectivo" - Programaci�n de Servicios y Procesos
	
	
	
	------------------------------- DESCRIPCI�N -------------------------------
	
	Permite al cliente recibir informaci�n del servidor de forma continua sin
	bloquear su capacidad de enviar informaci�n.
*/



package model;



import java.io.DataInputStream;
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

    
    /*
		ESTADO: FUNCIONAL 
		
		DESCRIPCI�N:
			Ejecuta la tarea de lectura de datos
			cliente <- servidor de manera continua.
	*/
    @Override 
    public void run() {
    	// controla que se pare la escucha
    	// si algo no va bien
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
        		//ex.printStackTrace();
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
            	
            	// Si el servidor est� lleno
            	if (mensaje.equals("S_lleno_#no#mas#peticiones#_")) {
            		cliente.errorText.setText("Servidor lleno");
            		cliente.errorText.setVisible(true);
            		
            		mensaje = null;
            		
            		cliente.desconectar();
            	}
            	
            	// Si el servidor se ha desconectado
            	if (mensaje.equals("|/\\\\/\\//\\|")) {
            		cliente.errorText.setText("Servidor desconectado");
            		cliente.errorText.setVisible(true);
            		
					mensaje = null;
					
					cliente.desconectar();
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
            	//ioe.printStackTrace();
            	System.out.println("Error de e/s.");
            	System.out.println("-----------------------------------------------------------");
            }

            // Si el mensaje no es nulo, mostrarlo
            if (mensaje != null) {
            	String msg = mensaje;
                Platform.runLater(() -> {
                	cliente.listView.getItems().add(new Mensaje(msg, false));
                	cliente.listView.scrollTo(cliente.listView.getItems().size() - 1);
                });
            }
        }
    }
    
}
