/*
	Hecho por:
		Eloy Guillermo Villad�niga M�rquez
		e
		Iv�n M�rquez Garc�a

	2� D.A.M.

	Pr�ctica "Chat Colectivo" - Programaci�n de Servicios y Procesos



	------------------------------- DESCRIPCI�N -------------------------------
	Representa un hilo que se encarga de las tareas de una conexi�n (leer
	desde el cliente y enviar al resto).
 */



package model;



import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.net.Socket;
import java.net.SocketException;
import java.util.Date;

import javafx.application.Platform;



public class HiloServidor implements Runnable {

	Socket socketCliente;
	Servidor servidor;
	DataInputStream input;
	DataOutputStream output;

	public HiloServidor(Socket socket, Servidor server) {
		this.socketCliente = socket;
		this.servidor = server;
	}

	@Override
	public void run() {
		try {
			// Iniciar flujos de entrada y salida
			input = new DataInputStream(socketCliente.getInputStream());
			output = new DataOutputStream(socketCliente.getOutputStream());
			
			while (servidor.isConectado() == true) {
				// Reciber mensaje de cliente
				String mensaje = input.readUTF();

				// String para indicar la desconexi�n: |/\\\\/\\//\\|
				if (mensaje.equals("|/\\\\/\\//\\|")) {
					String host = socketCliente.getInetAddress().getHostName();
					int port = socketCliente.getPort();
					
					socketCliente.shutdownInput();
					socketCliente.shutdownOutput();
					socketCliente.close();
					socketCliente = null;
					
					input.close();
					input = null;
					
					output.close();
					output = null;
					
					servidor.textArea.appendText("[" + new Date() + "] | [HOST: " + host + " PORT: " + port + "] - " + "cliente se ha desconectado\n");
					
					clienteDesconectado();
					
					break;
				}
				else {
					// Enviar mensaje a todos
					servidor.mensajeParaTodos(mensaje);
	
					// Mostrar mensaje en el �rea de texto
					Platform.runLater(() -> {                    
						servidor.textArea.appendText(mensaje + "\n");
					});
				}
			}
		}
		catch(SocketException se) {
    		System.out.println("-----------------------------------------------------------");
    		se.printStackTrace();
    		System.out.println("La escucha del servidor se ha interrumpido.");
    		System.out.println("-----------------------------------------------------------");
    	}
		catch (IOException ioe) {
			System.out.println("-----------------------------------------------------------");
			ioe.printStackTrace();
			System.out.println("Error de e/s");
			System.out.println("-----------------------------------------------------------");
		}
		finally {
			try {
				if (socketCliente != null && socketCliente.isClosed() == false)
					socketCliente.close();
			}
			catch (IOException ex) {
				System.out.println("-----------------------------------------------------------");
				ex.printStackTrace();
				System.out.println("Error al cerrar el socket del cliente");
				System.out.println("-----------------------------------------------------------");
			}
		}
	}

	
	// Enviar mensaje de vuelta a cliente - sustituir en el cliente para
	// optimizar servidor
	public void sendMessage(String message) {
		try {
			output.writeUTF(message);
			output.flush();

		} catch (IOException ex) {
			System.out.println("-----------------------------------------------------------");
			ex.printStackTrace();
			System.out.println("Error al enviar el mensaje al servidor");
			System.out.println("-----------------------------------------------------------");
		} 
	}
	
	
	// Indicar que el cliente se ha desconectado
	public void clienteDesconectado() {
		servidor.limpiarConexiones();
	}

}
