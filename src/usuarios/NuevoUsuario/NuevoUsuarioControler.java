package usuarios.NuevoUsuario;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import com.jfoenix.controls.JFXButton;

import funciones.Conexion;
import funciones.Funciones;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import login.Rol;

public class NuevoUsuarioControler extends Funciones implements Initializable{
	
	@FXML private TextField textFieldDni;
    @FXML private TextField textFieldNombres;
    @FXML private TextField textFieldApellidos;
    @FXML private TextField textFieldCargo;
    @FXML private ComboBox<Rol> comboBoxSeleccioneRol;
    @FXML private TextField textFieldContrasena;
    @FXML private ImageView imageViewFotoPerfil;
    @FXML private Hyperlink hyperlinkSeleccionarFoto;
    @FXML private JFXButton jfxButtonCerrar;
    @FXML private JFXButton jfxButtonValidar;
    @FXML private Label labelVerificacion;
    
    private final String LINK_CARPETA_FOTO="/usuarios/NuevoUsuario/";

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		comboBoxSeleccioneRol.getItems().addAll(new Rol(1, "Administrador"),new Rol(0, "Usuario"));
		imageViewFotoPerfil.setImage(new Image(LINK_CARPETA_FOTO+"image"+((int) (Math.random() * 7) )+".jpg"));

		jfxButtonCerrar.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				JFXButton salir = (JFXButton)event.getSource();
				Stage cerrar = (Stage)salir.getScene().getWindow();
				cerrar.close();
			}
		});
		
		jfxButtonValidar.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				InsertarDatosUsuario();
			}
		});
		
		hyperlinkSeleccionarFoto.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				Image image=seleccionarImage();
				if(image!=null){
					imageViewFotoPerfil.setImage(image);
				}
			}
		});
		
	}
	
	public void InsertarDatosUsuario(){
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				ObservableList<String> styleClass = labelVerificacion.getStyleClass();
		        styleClass.removeAll(Collections.singleton("labelInformacionincorrecto"));  
		        styleClass.removeAll(Collections.singleton("labelInformacioncorrecto"));
		        
		        String mensaje=null;
		        Connection conn=null;
		        PreparedStatement pst=null;
		        String url = "INSERT INTO USUARIO(DNI, NOMBRE, APELLIDO, CARGO, FOTO, ROL, ESTADO, CONTRASENNA)"
		        		+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?);";
		        
		        try {
		        	Conexion.conexionDB();
					conn=Conexion.getConexion();
					pst=conn.prepareStatement(url);
					if(!textFieldDni.getText().trim().isEmpty()){
						pst.setString(1,textFieldDni.getText().trim());
					}else{
						pst.setNull(1, java.sql.Types.VARCHAR);
					}
					
					if(!textFieldNombres.getText().trim().isEmpty()){
						pst.setString(2,textFieldNombres.getText().trim());
					}else{
						pst.setNull(2, java.sql.Types.VARCHAR);
					}
					
					if(!textFieldApellidos.getText().trim().isEmpty()){
						pst.setString(3,textFieldApellidos.getText().trim());
					}else{
						pst.setNull(3, java.sql.Types.VARCHAR);
					}
					
					if(!textFieldCargo.getText().trim().isEmpty()){
						pst.setString(4,textFieldCargo.getText().trim());
					}else{
						pst.setNull(4, java.sql.Types.VARCHAR);
					}
					
					if(imageViewFotoPerfil.getImage()!=null){
						BufferedImage image = SwingFXUtils.fromFXImage(imageViewFotoPerfil.getImage(), null);
		            	ByteArrayOutputStream baos = null;
		            	try{
	            	       baos = new ByteArrayOutputStream();
	            	       ImageIO.write(image, "jpg", baos);
		            	}finally{
		            		try{
		            			baos.close();
		            		}catch (Exception e){
		            			
		            		}
		            	}
		            	   
		            	ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		            	pst.setBlob(5, bais);
					}else{
						pst.setBlob(5, (Blob) null);
					}
					
					if(comboBoxSeleccioneRol.getSelectionModel().getSelectedItem()!=null){
						pst.setInt(6,comboBoxSeleccioneRol.getSelectionModel().getSelectedItem().getId());
					}else{
						pst.setNull(6, java.sql.Types.VARCHAR);
					}				
					
					pst.setInt(7, 1);
					
					pst.setString(8, encriptar(textFieldContrasena.getText()));
					
					int rs=pst.executeUpdate();
					
					if(rs==1){
						mensaje="Insercion Correcta";
					}else{
						mensaje="Falló....";
					}
					if(!styleClass.contains("labelInformacioncorrecto")){
			                styleClass.add("labelInformacioncorrecto");
			        }
			        labelVerificacion.setText(mensaje);
				} catch (Exception e) {
					if (!styleClass.contains("labelInformacionincorrecto")) {
		                styleClass.add("labelInformacionincorrecto");
		              
		            }
					mensaje=("Error  "+e.getMessage());
					labelVerificacion.setText(mensaje);
					e.printStackTrace();
				}finally{
					try{
						if(pst!=null){
							pst.close();
						}
		       			if(conn!=null){
		       				conn.close(); 
		       			}  
		   			}catch (SQLException e) {
						e.printStackTrace();
					}
				}
				return null;
			}
		};
		Thread hilo = new Thread(task);
		hilo.setDaemon(true);
		hilo.start();
	}
}
