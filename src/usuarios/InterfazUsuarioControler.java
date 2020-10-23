package usuarios;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import funciones.Funciones;
import funciones.Servidor;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class InterfazUsuarioControler extends Funciones implements Initializable  {

    @FXML private TextField textFieldBuscarUsuario;
    @FXML private TextField textFieldDni;
    @FXML private TextField textFieldNombre;
    @FXML private TextField textFieldApellidos;
    @FXML private TextField textFieldCargo;
    @FXML private JFXButton jfxButtonEliminar;
    @FXML private JFXButton jfxButtonGuardar;
    @FXML private ImageView ImageViewFotoPerfil;
    @FXML private Hyperlink hyperlinkSubirFoto;
    @FXML private CheckBox checkBoxEstado;
    @FXML private Hyperlink hyperlinkRestablecerContrasena;
	@FXML private JFXButton jfxButtonNuevoUsuario;
    @FXML private TableView<Usuarios> tableViewUsuario;
    @FXML private TableColumn<Usuarios, String> tableColumnDni;
    @FXML private TableColumn<Usuarios, String> tableColumnApeNom;
    @FXML private TableColumn<Usuarios, String> tableColumnCargo;
    @FXML private TableColumn<Usuarios, String> tableColumnRol;
	@FXML private StackPane stakePaneTransparencia;
	@FXML private Label labelVerificacion;
	
	private int idUsuario=-1;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		inicializarTableViewUsuarios();
		
		Task<Void> task=new Task<Void>(){

			@Override
			protected Void call() throws Exception {
				Consulta_BaseDatosUsuarios cv=new Consulta_BaseDatosUsuarios();
				
				while(true){
					tableViewUsuario.setItems(cv.obtenerDatosUsuariosBD());
					Thread.sleep(30000);
				}
			}
			
		};
		Thread hilo=new Thread(task);
		hilo.setDaemon(true);
		hilo.start();
		
		jfxButtonNuevoUsuario.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				Node node = (Node)arg0.getSource();
				Stage stage = (Stage)node.getScene().getWindow();
				StackPane stackPane = (StackPane)stage.getScene().getRoot().getChildrenUnmodifiable().get(1);
				stackPane.setVisible(true);
				
				String url = "/usuarios/NuevoUsuario/NuevoUsuario.fxml";
				String css = "/estilocss/EstiloModal.css";
				
				try {
					mostrarInterfazModalShowAndWait(url, css);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				stackPane.setVisible(false);
			}
		});
		
		tableViewUsuario.getSelectionModel().selectedItemProperty().addListener((newSelection) -> {
		    if (newSelection != null) {
		    	ObtenerDatosUsuarioFila(tableViewUsuario.getSelectionModel().getSelectedItem().getUser_id_usuario());
		    	idUsuario = tableViewUsuario.getSelectionModel().getSelectedItem().getUser_id_usuario();
		    }
		});
		
		textFieldBuscarUsuario.setOnKeyReleased(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				obtenerDatosUsuariosBD_Buscar(textFieldBuscarUsuario.getText().trim());
			}
		});
		
		jfxButtonGuardar.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				ActualizarDatosUsuario();
			}
		});
		
		jfxButtonEliminar.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (EliminarDatos()) {
					EliminarDatosUsuario();
				}else{
					return;
				}
			}
		});
		
		hyperlinkRestablecerContrasena.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				Node node = (Node)event.getSource();
				Stage stage = (Stage)node.getScene().getWindow();
				StackPane stackpane = (StackPane)stage.getScene().getRoot().getChildrenUnmodifiable().get(1);
				stackpane.setVisible(true);
				
				String url = "/usuarios/RestablecerContrasena/RestablecerContrasena.fxml";
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
		
	}
	
	public void inicializarTableViewUsuarios(){
		
		tableColumnDni.setCellValueFactory(new PropertyValueFactory<>("user_dni"));
		tableColumnApeNom.setCellValueFactory(new PropertyValueFactory<>("user_nom_apell"));
		tableColumnCargo.setCellValueFactory(new PropertyValueFactory<>("user_cargo"));
		tableColumnRol.setCellValueFactory(new PropertyValueFactory<>("user_rol_string"));
	
	}
	
	class Consulta_BaseDatosUsuarios{
		public ObservableList<Usuarios> obtenerDatosUsuariosBD(){
			
			ObservableList<Usuarios> observableList = FXCollections.observableArrayList();
			Connection conn=null;
			String url = "SELECT ID_USUARIO, DNI, NOMBRE, APELLIDO, CARGO, FOTO, ROL, ESTADO, CONTRASENNA FROM USUARIO";  
			PreparedStatement pst=null;
			ResultSet rs=null;
			try{
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection(Servidor.SERVIDOR, Servidor.USER, Servidor.PASS);
		        pst = conn.prepareStatement(url); 
		        rs = pst.executeQuery();
		        while(rs.next()){
		        	
		        	observableList.add(new Usuarios(rs.getInt("ID_USUARIO"), rs.getString("DNI"), rs.getString("NOMBRE"), rs.getString("APELLIDO"), rs.getString("CARGO"), null, rs.getInt("ROL"), rs.getInt("ESTADO")));
		        	
		        }
		     }catch(SQLException e){
		  	   	 System.out.println(e.getMessage());
		         e.printStackTrace();
		    
		     }catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			 }finally{
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
			return observableList;
			
		}
	}
	
	public void ObtenerDatosUsuarioFila(int id_usuario){
		Connection conn=null;
		String url = "SELECT ID_USUARIO, DNI, NOMBRE, APELLIDO, CARGO, FOTO, ROL, ESTADO, CONTRASENNA FROM USUARIO WHERE ID_USUARIO=?";  
		PreparedStatement pst=null;
		ResultSet rs=null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Servidor.SERVIDOR, Servidor.USER, Servidor.PASS);
	        pst = conn.prepareStatement(url);
	        pst.setInt(1, id_usuario);
	        rs = pst.executeQuery();
	        if(rs.next()){
	        	textFieldDni.setText(rs.getString("DNI"));
	        	textFieldNombre.setText(rs.getString("NOMBRE"));
	        	textFieldApellidos.setText(rs.getString("APELLIDO"));
	        	textFieldCargo.setText(rs.getString("CARGO"));
	        	Blob imageBlob=rs.getBlob("FOTO");
            	if(imageBlob!=null){
            		ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(imageBlob.getBytes(1, (int)imageBlob.length()));
            		ImageViewFotoPerfil.setImage(new Image(byteArrayInputStream));
               	}else {
            		ImageViewFotoPerfil.setImage(null);

            	}
        		if(rs.getInt("ESTADO")==1){
	        		checkBoxEstado.setSelected(true);
	        	}else{
	        		checkBoxEstado.setSelected(false);
	        	}
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs!=null){
		  			 rs.close(); 
				}
		  		if(pst!=null){
		  			 pst.close();
				}
		  		if(conn!=null){
		  			 conn.close();
		  	   	}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void obtenerDatosUsuariosBD_Buscar(String buscar){
		
		Task<Void> task=new Task<Void>(){

			@Override
			protected Void call() throws Exception {
				
				tableViewUsuario.getItems().clear();
				Connection conn=null;
				String url = "SELECT ID_USUARIO, DNI, NOMBRE, APELLIDO, CARGO, FOTO, ROL, ESTADO, CONTRASENNA FROM USUARIO "
						+ "WHERE DNI LIKE ? OR NOMBRE LIKE ? OR APELLIDO LIKE ? OR CARGO LIKE ?;";  
				PreparedStatement pst=null;
				ResultSet rs=null;
				try{
					Class.forName("com.mysql.jdbc.Driver");
					conn = DriverManager.getConnection(Servidor.SERVIDOR, Servidor.USER, Servidor.PASS);
			        pst = conn.prepareStatement(url);
			        pst.setString(1, "%" + buscar + "%");
			        pst.setString(2, "%" + buscar + "%");
			        pst.setString(3, "%" + buscar + "%");
			        pst.setString(4, "%" + buscar + "%");
			        rs = pst.executeQuery();
			        
			        while(rs.next()){
			        	Usuarios user = new Usuarios(rs.getInt("ID_USUARIO"), rs.getString("DNI"), rs.getString("NOMBRE"), rs.getString("APELLIDO"), 
			        	rs.getString("CARGO"), null, rs.getInt("ROL"), rs.getInt("ESTADO"));
			        
			        	if(textFieldBuscarUsuario.getText().trim().equals(buscar)){
							Platform.runLater(new Runnable() {
								
								@Override
								public void run() {
									tableViewUsuario.getItems().add(user);
								}
							});
						}else{
							
							break;
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
				 return null;	
			}
		};
		
		
		Thread hilo=new Thread(task);
		hilo.setDaemon(true);
		hilo.start();
	}
	
	public void ActualizarDatosUsuario(){
		String mensaje =null;
		Connection conn = null;
		String url = "UPDATE USUARIO SET DNI=?, NOMBRE=?, APELLIDO=?, CARGO=?, ESTADO=? "
				+ "WHERE ID_USUARIO=?";
		PreparedStatement pst=null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Servidor.SERVIDOR, Servidor.USER, Servidor.PASS);
			pst=conn.prepareStatement(url);
			
			if(!textFieldDni.getText().trim().isEmpty()){
				pst.setString(1,textFieldDni.getText().trim());
			}else{
				pst.setNull(1, java.sql.Types.VARCHAR);
			}
			
			if(!textFieldNombre.getText().trim().isEmpty()){
				pst.setString(2,textFieldNombre.getText().trim());
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
			
			if (checkBoxEstado.isSelected()) {
				pst.setInt(5, 1);
			}else{
				pst.setInt(5, 0);
			}
			
			pst.setInt(6, idUsuario);
			
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
	
	public void EliminarDatosUsuario(){
		String mensaje =null;
		Connection conn = null;
		String url = "DELETE FROM USUARIO WHERE ID_USUARIO=?";
		PreparedStatement pst=null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Servidor.SERVIDOR, Servidor.USER, Servidor.PASS);
			pst=conn.prepareStatement(url);
			pst.setInt(1, idUsuario);
			pst.executeUpdate();
			mensaje="Usuario Eliminado";
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