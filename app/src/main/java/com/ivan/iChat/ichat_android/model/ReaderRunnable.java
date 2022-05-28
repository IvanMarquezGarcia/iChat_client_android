/*
	Hecho por: Iván Márquez García
		
	2° D.A.M.
	
	
	
	------------------------------- DESCRIPCIÓN -------------------------------
	
	Permite al cliente recibir información del servidor de forma continua sin
	bloquear su capacidad de enviar información.
*/



package com.ivan.iChat.ichat_android.model;


import android.view.View;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.IOException;

import java.net.SocketException;

import com.ivan.iChat.ichat_android.ChatActivity;
import com.ivan.iChat.ichat_android.R;

import static com.ivan.iChat.ichat_android.utils.Constants.*;


public class ReaderRunnable implements Runnable {
	
	private ChatActivity chatActivity;
	private DataInputStream input;
    
    
    public ReaderRunnable(ChatActivity ca) {
        this.chatActivity = ca;
        try {
			input = new DataInputStream(chatActivity.getSocket().getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    
    /*
		ESTADO: FUNCIONAL 
		
		DESCRIPCIÓN:
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
	    	input = new DataInputStream(chatActivity.getSocket().getInputStream());
    	}
    	catch (IOException ex) {
    		correcto = false;
    		String errorMsg = "Error al crear el socket del cliente";
            
    		if (chatActivity.getSocket() == null)
        		errorMsg = "Flujo de entrada cerrado";
        		
        		System.out.println("-----------------------------------------------------------");
        		//ex.printStackTrace();
        		System.out.println(errorMsg);
        		System.out.println("-----------------------------------------------------------");
        		
        		
        		chatActivity.getErrorText().setText(errorMsg);
        		chatActivity.getErrorText().setVisibility(View.VISIBLE);
        }
    	
        while (chatActivity.getConnectionStatus() == STATUS_CONNECTED && correcto == true) {
        	// Setear mensaje por defecto y ocultar errorText
			chatActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					chatActivity.getErrorText().setVisibility(View.INVISIBLE);
					chatActivity.getErrorText().setText("Error de conexión");
				}
			});

            // recibir del servidor
            String message = null;
            try {
            	message = input.readUTF();
            	
            	// Si el servidor est� lleno
            	if (message.equals("S_lleno_#no#mas#peticiones#_")) {
					chatActivity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							chatActivity.getErrorText().setText("Servidor lleno");
							chatActivity.getErrorText().setVisibility(View.VISIBLE);
						}
					});
            		
            		message = null;
            		
            		desconectar();
            	}
            	
            	// Si el servidor se ha desconectado
            	if (message.equals("------//_/_#s#b#y#e#s#_!/_")) {
					chatActivity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							chatActivity.getErrorText().setText("Servidor desconectado");
							chatActivity.getErrorText().setVisibility(View.VISIBLE);
						}
					});
            		
					message = null;
					
					desconectar();
            	}
            }
            catch(SocketException se) {
            	correcto = false;
            	
            	System.out.println("-----------------------------------------------------------");
            	//se.printStackTrace();
            	if (chatActivity.getSocket() == null || chatActivity.getSocket().isClosed() == true || chatActivity.getSocket().isInputShutdown() == true)
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
            if (message != null) {
            	System.out.println(message);
                chatActivity.getMessages().add(new Message(message, false));
				chatActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						chatActivity.getMssgsAdapter().notifyDataSetChanged();
						chatActivity.getMssgsList().scrollToPosition(chatActivity.getMssgsList().getAdapter().getItemCount() - 1);
					}
				});
            }
        }
    }
    
    
    /*
	ESTADO: FUNCIONAL 

	DESCRIPCI�N:
		Indica al servidor que el cliente se va a
		desconectar y cierra los flujos del socket
		del cliente y el propio socket 
	 */
	public int desconectar() {
		int status = STATUS_CONNECTED;
		try {
			// Indicar desconexi�n al servidor
			chatActivity.getOutput().writeUTF("|/\\\\/\\//\\|");

			// Desconectar
			if (chatActivity.getSocket().isInputShutdown() == false)
				chatActivity.getSocket().shutdownInput();

			if (chatActivity.getSocket().isOutputShutdown() == false)
				chatActivity.getSocket().shutdownOutput();

			if (chatActivity.getSocket().isClosed() == false) {
				chatActivity.getSocket().close();
			}

			status = STATUS_DISCONNECTED;
		}
		catch(SocketException se) {
			System.out.println("-----------------------------------------------------------");
			//se.printStackTrace();
			System.out.println("La comunicaci�n con el servidor se ha interrumpido.");
			System.out.println("-----------------------------------------------------------");

			status = STATUS_DISCONNECTED;
		}
		catch(IOException ioe) {
			status = STATUS_ERROR;

			System.out.println("-----------------------------------------------------------");
			//ioe.printStackTrace();
			System.out.println("Error al desconectar el cliente");
			System.out.println("-----------------------------------------------------------");
		}
		return status;
	}
    
}
