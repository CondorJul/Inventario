package login;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import funciones.Servidor;
import funciones.Funciones;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sesion.Sesion;

public class LoginControler extends Funciones implements Initializable {

	@FXML private JFXButton jFXButtonClose;
	@FXML private Button buttonIngresar;
    @FXML private Label labelAdvertencia;
    @FXML private JFXTextField jFXTextFieldUsuario;
    @FXML private JFXPasswordField jFXPasswordFieldContrasena;
    @FXML private JFXCheckBox jFXCheckBoxRecordarContrasena;
    @FXML private JFXComboBox<Rol> jfxComboBoxRol;

    private String stringFicheroUser="login/user";
	private String stringFicheroPass="login/pass";
    
	private ObservableList<Rol> arrayComboBoxRol = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		mostrarRol();
		
		//cargamos los campos con los datos guardados
		jFXTextFieldUsuario.setText(leerFichero(stringFicheroUser));
		jFXPasswordFieldContrasena.setText(leerFichero(stringFicheroPass));
		
		jFXButtonClose.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				JFXButton jFXButton=(JFXButton)event.getSource();
				Stage stage=(Stage)jFXButton.getScene().getWindow();
				stage.close();
				
			}
		});
			
		buttonIngresar.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {

				String url = "SELECT ROL, DNI, CONTRASENNA, NOMBRE, APELLIDO, FOTO, ESTADO, CARGO FROM USUARIO WHERE ROL=? AND DNI=? AND CONTRASENNA =? AND ESTADO=1;";
				Connection conn=null;
				PreparedStatement pst=null; 
				ResultSet rs=null;

				try {
					Class.forName("com.mysql.jdbc.Driver");
					conn = DriverManager.getConnection(Servidor.SERVIDOR, Servidor.USER, Servidor.PASS);
					pst=conn.prepareStatement(url);
					pst.setInt(1, jfxComboBoxRol.getSelectionModel().getSelectedItem().getId());
					pst.setString(2, jFXTextFieldUsuario.getText().trim());
					pst.setString(3, encriptar(jFXPasswordFieldContrasena.getText()));
					rs=pst.executeQuery();
					if(rs.next()){
						//capturar datos de inicio de sesion
						Sesion.DNI=rs.getString("DNI");
						Sesion.NOMBRES=rs.getString("NOMBRE");
						Sesion.APELLIDOS=rs.getString("APELLIDO");
						Sesion.CARGO=rs.getString("CARGO");
						Sesion.ROL=rs.getInt("ROL");
						Sesion.CONTRASENA=rs.getString("CONTRASENNA");
						Blob imageBlob=rs.getBlob("FOTO");
	                	if(imageBlob!=null){
	                		ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(imageBlob.getBytes(1, (int)imageBlob.length()));
	                		Sesion.FOTO=new Image(byteArrayInputStream);
	                   	}else {
	                   		//Sesion.FOTO=new Image("/personal/verPersonal/anadirPersonal/foto0.jpg");
	                   		//Sesion.FOTO=new Image("/src/personal/verPersonal/anadirPersonal/foto0.jpg");
	                	}
						
	                	if(jFXCheckBoxRecordarContrasena.isSelected())
	                	{
	                		escribirFichero(stringFicheroUser, jFXTextFieldUsuario.getText().trim() );
		                	escribirFichero(stringFicheroPass, jFXPasswordFieldContrasena.getText().trim());
	                	}else{
	                		escribirFichero(stringFicheroUser, jFXTextFieldUsuario.getText().trim() );
	                		escribirFichero(stringFicheroPass, "");
	                	}			
						
	                	//ocultar el scene del login
	                	((Node)(arg0.getSource())).getScene().getWindow().hide();
			    		
						//cargar el interfaz principal
	                	Parent parent;
						try {
							parent = FXMLLoader.load(getClass().getResource("/principal/InterfazPrincipal.fxml"));
							Stage stage = new Stage();
							Scene scene=new Scene(parent);
							stage.setScene(scene);
							stage.setMaximized(true);
							stage.setTitle("Main From");
							
							
							stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
								
								@Override
								public void handle(WindowEvent arg0) {
									if(cerrarVentana()){
										
									}else{
										arg0.consume();
									}
								}
							});
							stage.show();
						} catch (IOException e) {
							
							e.printStackTrace();
						}
	                	
					
					}else{
						labelAdvertencia.setText("Datos incorrectos.");
						return;
					}
				} catch (Exception e) {
					labelAdvertencia.setText("Error  "+e.getMessage());
					e.printStackTrace();
				} finally{
					
					 try {
	            		 if(pst!=null){
	            			 pst.close(); 
	            		 }
						 
						 if(rs!=null){
							 rs.close();
						 }
	            		
						 if(conn!=null){
							 conn.close(); 
						 }
	            	
						
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
				}  
				 
			}
		});
			
	}
			
	public boolean cerrarVentana(){
		Alert alert = new Alert(AlertType.CONFIRMATION, "¿Está seguro que desea salir :( ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
		alert.showAndWait();

		if (alert.getResult() == ButtonType.YES) {
		    return true;
		}else {
			return false;
		}
	}

	private String leerFichero(String url){
		File archivo = null;
		FileReader fr = null;
		BufferedReader br = null;
		String texto="";
		try {
			// Apertura del fichero y creacion de BufferedReader para poder
	        // hacer una lectura comoda (disponer del metodo readLine()).
	        archivo = new File (url); //"C:\\archivo.txt"
	        fr = new FileReader (archivo);
	        br = new BufferedReader(fr);

	        // Lectura del fichero
	        //texto
	        String linea;
	        while((linea=br.readLine())!=null){
	        	 texto=texto+linea;
	         }
	         
	    }catch(Exception e){
	    	e.printStackTrace();
	    }finally{
	    	// En el finally cerramos el fichero, para asegurarnos
	        // que se cierra tanto si todo va bien como si salta 
	        // una excepcion.
	        try{
	        	if( null != fr ){   
	               fr.close();     
	            }                  
	         }catch (Exception e2){ 
	            e2.printStackTrace();
	         }
	      }
	      return texto;
	}
  
		
		
	private void escribirFichero(String url, String texto){

		FileWriter fichero = null;
        PrintWriter pw = null;
        try{
            fichero = new FileWriter(url);//url
            pw = new PrintWriter(fichero);

            //for (int i = 0; i < 10; i++)
                pw.println(texto);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           try {
           // Nuevamente aprovechamos el finally para 
           // asegurarnos que se cierra el fichero.
        	   if (null != fichero){
        		   fichero.close();
        	   }	   
           }catch (Exception e2) {
        	   e2.printStackTrace();
           }
        }

	}
		
	public void  mostrarRol(){
		arrayComboBoxRol.add(new Rol(1, "Administrador"));
		arrayComboBoxRol.add(new Rol(0, "Usuario"));
		jfxComboBoxRol.getItems().addAll(arrayComboBoxRol);
		
	}
}