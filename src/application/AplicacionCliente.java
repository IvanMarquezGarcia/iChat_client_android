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

import controller.ControladorCliente;

import model.Cliente;



public class AplicacionCliente extends Application {
    
    @Override
    public void start(Stage stage) {
    	Cliente c = new Cliente("Iv�n", "localhost", 1234);
    	ControladorCliente ccli = new ControladorCliente(c);
    	
    	URL rutaRecurso = getClass().getResource("/view/VistaCliente.fxml");
    	FXMLLoader fxmlLoader = new FXMLLoader(rutaRecurso);
    	
    	fxmlLoader.setController(ccli);
    	
		try {
			Scene scene = new Scene(fxmlLoader.load(), 530, 600);
			
			stage.setTitle("Cliente de eiChat - " + c.getNombre());
			stage.setScene(scene);
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
