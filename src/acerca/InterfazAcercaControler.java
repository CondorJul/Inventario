package acerca;

import java.net.URL;
import java.util.ResourceBundle;

import funciones.Funciones;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class InterfazAcercaControler extends Funciones implements Initializable{
	
	@FXML private Label labelCerrar;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		labelCerrar.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				Node node = (Node)event.getSource();
				Stage cerrar = (Stage)node.getScene().getWindow();
				cerrar.close();
			}
		});
	}

}
