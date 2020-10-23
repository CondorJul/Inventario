package perfil;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import com.jfoenix.controls.JFXButton;

import funciones.Funciones;
import funciones.Servidor;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import sesion.Sesion;

public class InterfazPerfilControler extends Funciones implements Initializable{
	
	@FXML private TextField textFieldDni;
    @FXML private TextField textFieldNombres;
    @FXML private TextField textFieldApellidos;
    @FXML private TextField textFieldCargo;
    @FXML private ImageView imageViewFotoPerfil;
    @FXML private Hyperlink hyperlinkSubirFoto;
    @FXML private CheckBox checkBoxEstado;
    @FXML private Hyperlink hyperlinkCambiarContraseña;
    @FXML private JFXButton jfxButtonGuardar;
    @FXML private StackPane stackPaneTransparencia;
    @FXML private Label labelVerificacion;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		mostrarDatosPerfil();
		
		jfxButtonGuardar.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				ActualizarDatosPerfil();
			}
		});
		
		hyperlinkCambiarContraseña.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				Node node = (Node)event.getSource();
				Stage stage = (Stage)node.getScene().getWindow();
				StackPane stackpane = (StackPane)stage.getScene().getRoot().getChildrenUnmodifiable().get(1);
				stackpane.setVisible(true);
				
				String url = "/perfil/CambiarContrasena/CambiarContrasena.fxml";
				String css = "/estilocss/EstiloModal.css";
				
				try {
					mostrarInterfazModalShowAndWait(url, css);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				stackpane.setVisible(false);
			}
		});
		
		hyperlinkSubirFoto.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Image image=seleccionarImage();
				if(image!=null){
					imageViewFotoPerfil.setImage(image);
				}
			}
		});

	}

	public void mostrarDatosPerfil(){
		
		Connection conn=null;
		String url = "SELECT ID_USUARIO, DNI, NOMBRE, APELLIDO, CARGO, FOTO, ROL, ESTADO, CONTRASENNA FROM USUARIO WHERE DNI=?;"; 
		PreparedStatement pst=null;
		ResultSet rs=null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Servidor.SERVIDOR, Servidor.USER, Servidor.PASS);
	        pst = conn.prepareStatement(url); 
	        pst.setString(1, Sesion.DNI);
	        rs = pst.executeQuery();
	        if(rs.next()){
	        	textFieldDni.setText(rs.getString("DNI"));
	        	textFieldNombres.setText(rs.getString("NOMBRE"));
	        	textFieldApellidos.setText(rs.getString("APELLIDO"));
	        	textFieldCargo.setText(rs.getString("CARGO"));
	        	Blob imageBlob=rs.getBlob("FOTO");
            	if(imageBlob!=null){
            		ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(imageBlob.getBytes(1, (int)imageBlob.length()));
            		Sesion.FOTO=new Image(byteArrayInputStream);
               	}else {
               		Sesion.FOTO=new Image("/usuarios/NuevoUsuario/image2.jpg");
            	}
        		imageViewFotoPerfil.setImage(Sesion.FOTO);
        		if(rs.getInt("ESTADO")==1){
	        		checkBoxEstado.setSelected(true);
	        	}else{
	        		checkBoxEstado.setSelected(false);
	        	}
	        }
	     }catch(SQLException e){
	         e.printStackTrace();
	     }catch (ClassNotFoundException e) {
				e.printStackTrace();
		 } finally{
		  	 try{
		  		 if(rs!=null){
		  			 rs.close(); 
		  		 }
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
	}
	
	public void ActualizarDatosPerfil(){
		String mensaje =null;
		Connection conn = null;
		String url = "UPDATE USUARIO SET NOMBRE=?, APELLIDO=?, CARGO=?, FOTO=? "
				+ "WHERE DNI=?";
		PreparedStatement pst=null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Servidor.SERVIDOR, Servidor.USER, Servidor.PASS);
			pst=conn.prepareStatement(url);
			
			if(!textFieldNombres.getText().trim().isEmpty()){
				pst.setString(1,textFieldNombres.getText().trim());
			}else{
				pst.setNull(1, java.sql.Types.VARCHAR);
			}
			
			if(!textFieldApellidos.getText().trim().isEmpty()){
				pst.setString(2,textFieldApellidos.getText().trim());
			}else{
				pst.setNull(2, java.sql.Types.VARCHAR);
			}
			
			if(!textFieldCargo.getText().trim().isEmpty()){
				pst.setString(3,textFieldCargo.getText().trim());
			}else{
				pst.setNull(3, java.sql.Types.VARCHAR);
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
            	pst.setBlob(4, bais);
			}else{
				pst.setBlob(4, (Blob) null);
			}
			
			pst.setString(5, Sesion.DNI);
			
			pst.executeUpdate();
			mensaje="Datos Actualizados Corectamente";
			mostrarInformacion(labelVerificacion, mensaje, Funciones.CORRECTO);
		}catch (Exception e) {
			mensaje=("Error  "+e.getMessage());
			mostrarInformacion(labelVerificacion, mensaje, Funciones.INCORRECTO);
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
	}
	
}
