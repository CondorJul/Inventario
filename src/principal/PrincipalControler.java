package principal;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import funciones.Funciones;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import sesion.Sesion;

public class PrincipalControler extends Funciones implements Initializable{
	
    @FXML private Circle circleFotoPerfil2;
    @FXML private Button buttonSalir;
    @FXML private Button buttonCambiarContrasena;
    @FXML private Circle circleFotoPerfil;
    @FXML private Label labelNombreYApellidos;
    @FXML private Label LabelCargo;
    @FXML private FontAwesomeIconView fontAwesomeMenu;
    @FXML private BorderPane borderPaneContainer;
    @FXML private ToggleButton toggleButtonInicio;
    @FXML private StackPane stackPaneMenu;
    @FXML private ToggleGroup toggleGroupOpciones;
    @FXML private ToggleButton toggleButtonEquipos;
    @FXML private ToggleButton toggleButtonOficinas;
    @FXML private ToggleButton toggleButtonPersonal;
    @FXML private ToggleButton toggleButtonUsuarios;
    @FXML private ToggleButton toggleButtonPerfil;
    @FXML private ToggleButton toggleButtonAcerca;
    @FXML private StackPane stackPaneTransparencia;
    
    private BorderPane borderPaneEquipos; 
    private BorderPane borderPanePersonal;
    private BorderPane borderPaneOficinas;
    
    
    
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		labelNombreYApellidos.setText(Sesion.NOMBRES+" "+Sesion.APELLIDOS);
		LabelCargo.setText(Sesion.CARGO);
		circleFotoPerfil.setFill(new ImagePattern(Sesion.FOTO));
		circleFotoPerfil2.setFill(new ImagePattern(Sesion.FOTO));
		
		cargarInterfazInterna();
		toggleButtonEquipos.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				ocultarMenu();
				/*
				String url = "/equipos/InterfazEquipos.fxml";
				String css = "";
				
				try {
					setInterfazInterna(borderPaneContainer, url, css);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				
				
				setInterfazInterna(borderPaneContainer, borderPaneEquipos);
			}
		});

		toggleButtonOficinas.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				ocultarMenu();
				
				/*
				String url = "/oficinas/interfazOficinas.fxml";
				String css = "";
				
				try {
					setInterfazInterna(borderPaneContainer, url, css);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				setInterfazInterna(borderPaneContainer, borderPaneOficinas);
			}
		});
		
		toggleButtonPersonal.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				ocultarMenu();
				/*
				String url = "/personal/InterfazPersonal.fxml";
				String css = "";
				
				try {
					setInterfazInterna(borderPaneContainer, url, css);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				
				
				setInterfazInterna(borderPaneContainer, borderPanePersonal);
			}
		});
		
		toggleButtonUsuarios.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				ocultarMenu();
				String url = "/usuarios/InterfazUsuario.fxml";
				String css = "";
				
				try {
					setInterfazInterna(borderPaneContainer, url, css);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		
		toggleButtonPerfil.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				
				ocultarMenu();
				String url = "/perfil/InterfazPerfil.fxml";
				String css = "";
				
				try {
					setInterfazInterna(borderPaneContainer, url, css);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		
		toggleButtonAcerca.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				ocultarMenu();
				Node node =  (Node)arg0.getSource();
				Stage stage = (Stage)node.getScene().getWindow();
				StackPane stackpane = (StackPane)stage.getScene().getRoot().getChildrenUnmodifiable().get(1);
				stackpane.setVisible(true);
							
				String url = "/acerca/InterfazAcerca.fxml";
				String css = "/estilocss/EstiloModal.css";
				
				try {
					mostrarInterfazModalShowAndWait(url, css);;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				stackpane.setVisible(false);
			}
			
		});
		
		stackPaneTransparencia.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				ocultarMenu();	
			}
		});
		
		fontAwesomeMenu.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				mostrarMenu();
			}
		});
		
		buttonCambiarContrasena.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent cambiar) {
				
				stackPaneTransparencia.setVisible(true);
				
				String url = "/perfil/CambiarContrasena/CambiarContrasena.fxml";
				String css = "/estilocss/EstiloModal.css";
				
				try {
					mostrarInterfazModalShowAndWait(url, css);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				stackPaneTransparencia.setVisible(false);
			}
		});
		
		buttonSalir.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.exit(0);
			}
		});
		
	}
	
	public void mostrarMenu(){

		TranslateTransition menuLateral=new TranslateTransition(Duration.millis(200.0), stackPaneMenu);
		menuLateral.setToX(0);
		stackPaneTransparencia.setVisible(true);
	
		menuLateral.play();
	}
	public void ocultarMenu(){
		TranslateTransition menuLateral=new TranslateTransition(Duration.millis(200.0), stackPaneMenu);
		menuLateral.setToX(-203);
		stackPaneTransparencia.setVisible(false);
		menuLateral.play();
		
	}
	
	
	public void cargarInterfazInterna(){
	
		
		try {
			borderPaneEquipos=obtenerBorderPaneInterna("/equipos/InterfazEquipos.fxml", "");
			borderPanePersonal=obtenerBorderPaneInterna("/personal/InterfazPersonal.fxml", "");
			borderPaneOficinas=obtenerBorderPaneInterna("/oficinas/interfazOficinas.fxml", "");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

}
