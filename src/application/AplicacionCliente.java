/*
	Hecho por:
		Eloy Guillermo Villad�niga M�rquez
		e
		Iv�n M�rquez Garc�a
		
	2� D.A.M.
	
	Pr�ctica "Chat Colectivo" - Programaci�n de Servicios y Procesos
	
	
	
	------------------------------- DESCRIPCI�N -------------------------------
	
	Este es el archivo para ejecutar la apliciaci�n gr�fica asociada a un
	cliente.
*/



package application;



import javafx.application.Application;

import java.io.IOException;

import java.net.URL;

import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;

import javafx.stage.Stage;
import javafx.stage.StageStyle;

import controller.ControladorDatosCliente;

import model.Cliente;



public class AplicacionCliente extends Application {
    
    @Override
    public void start(Stage stage) {
    	Cliente cliente = new Cliente();
    	ControladorDatosCliente cdc = new ControladorDatosCliente(cliente);
    	
    	URL rutaVistaDatosCliente = getClass().getResource("/view/VistaDatosCliente.fxml");
    	FXMLLoader vistaDatosClienteLoader = new FXMLLoader(rutaVistaDatosCliente);
    	
    	vistaDatosClienteLoader.setController(cdc);
    	
    	try {
			Scene scene = new Scene(vistaDatosClienteLoader.load(), 420, 280);
			
			stage.setTitle("Log in | eiChat");
			stage.setScene(scene);
			stage.setResizable(false);
			stage.initStyle(StageStyle.UNDECORATED);
	    	stage.show();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error al cargar la ventana");
		}
    }

    
    
    public static void main(String[] args) {
        launch(args);
    }

}
