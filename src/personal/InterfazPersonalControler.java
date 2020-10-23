package personal;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.controlsfx.control.CheckComboBox;

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
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class InterfazPersonalControler extends Funciones implements Initializable {
	
	private ObservableList<String> observableListSistemasUso = FXCollections.observableArrayList("SIAF", "SIGA", "SISGEDO");
	
	@FXML private JFXButton jfxButtonNuevoPersonal;
    @FXML private TextField textFieldBuscarPersonal;
    @FXML private TextField textFieldDni;
    @FXML private TextField textFieldApeNom;
    @FXML private TextField textFieldUsuario;
    @FXML private TextField textFieldCargo;
    @FXML private CheckComboBox<String> checkComboBoxSistemasUso;
    @FXML private JFXButton jfxButtonEliminar;
    @FXML private JFXButton jfxButtonGuardar;
    @FXML private TableView<Personal> tableViewPersonal;
    @FXML private TableColumn<Personal, String> tableColumnDni;
    @FXML private TableColumn<Personal, String> tableColumnApeNom;
    @FXML private TableColumn<Personal, String> tableColumnUsuario;
    @FXML private TableColumn<Personal, String> tableColumnCargo;
    @FXML private TableColumn<Personal, String> tableColumnSistemasUso;
	@FXML private StackPane stakePaneTransparencia;
	@FXML private Menu menuAsignarOficina;
	@FXML private Label labelVerificacion;
	
	private int idPersonal=-1;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		inicializarTableViewPersonal();
		inicializarComboBox();
		
		Task<Void> task=new Task<Void>(){

			@Override
			protected Void call() throws Exception {
				Consulta_BaseDatosPersonal cv=new Consulta_BaseDatosPersonal();
				
				while(true){
					tableViewPersonal.setItems(cv.obtenerDatosPersonalBD());
					tableViewPersonal.refresh();
					Thread.sleep(30000);
				}
			}
			
		};
		Thread hilo=new Thread(task);
		hilo.setDaemon(true);
		hilo.start();
		
		jfxButtonNuevoPersonal.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				Node node = (Node)event.getSource();
				Stage stage = (Stage)node.getScene().getWindow();
				StackPane stackPane = (StackPane)stage.getScene().getRoot().getChildrenUnmodifiable().get(1);
				stackPane.setVisible(true);
				String url = "/personal/NuevoPersonal/NuevoPersonal.fxml";
				String css = "/estilocss/EstiloModal.css";
				
				try {
					mostrarInterfazModalShowAndWait(url, css);
				} catch (IOException e) {
					e.printStackTrace();
				}
				stackPane.setVisible(false);
			}
		});
		
		tableViewPersonal.getSelectionModel().selectedItemProperty().addListener((newSelection) -> {
		    if (newSelection != null) {
		    	ObtenerDatosPersonalFila(tableViewPersonal.getSelectionModel().getSelectedItem().getId_personal());
		    	idPersonal = tableViewPersonal.getSelectionModel().getSelectedItem().getId_personal();
		    }
		});
		
		textFieldBuscarPersonal.setOnKeyReleased(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				obtenerDatosPersonalBD_Buscar(textFieldBuscarPersonal.getText().trim());
			}
		});
		
		jfxButtonGuardar.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				ActualizarDatosPersonal();
			}
		});
		
		jfxButtonEliminar.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (EliminarDatos()) {
					EliminarDatosPersonal();
				}else{
					return;
				}
			}
		});
		
	}
	
	public void inicializarTableViewPersonal(){
		tableColumnDni.setCellValueFactory(new PropertyValueFactory<>("per_dni"));
		tableColumnApeNom.setCellValueFactory(new PropertyValueFactory<>("per_nombre_completo"));
		tableColumnUsuario.setCellValueFactory(new PropertyValueFactory<>("per_usuario"));
		tableColumnCargo.setCellValueFactory(new PropertyValueFactory<>("per_cargo"));
		tableColumnSistemasUso.setCellValueFactory(new PropertyValueFactory<>("per_sistema_usa"));
	}
		
	class Consulta_BaseDatosPersonal{
		public ObservableList<Personal> obtenerDatosPersonalBD(){
			
			ObservableList<Personal> observableList = FXCollections.observableArrayList();
			Connection conn=null;
			String url = "SELECT ID_PERSONAL, DNI, NOMBRE_COMPLETO, USUARIO, CARGO, SISTEMA_USA, ID_OFICINA FROM PERSONAL";  
			PreparedStatement pst=null;
			ResultSet rs=null;
			try{
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection(Servidor.SERVIDOR, Servidor.USER, Servidor.PASS);
		        pst = conn.prepareStatement(url); 
		        rs = pst.executeQuery();
		        while(rs.next()){
		        	observableList.add(new Personal(rs.getInt("ID_PERSONAL"), rs.getString("DNI"), rs.getString("NOMBRE_COMPLETO"), 
		        	rs.getString("USUARIO"), rs.getString("CARGO"), rs.getString("SISTEMA_USA"), rs.getString("ID_OFICINA")));
		        }
		     }catch(SQLException e){
		  	   	 System.out.println(e.getMessage());
		         e.printStackTrace();
		    
		     }catch (ClassNotFoundException e) {
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
	
	public void ObtenerDatosPersonalFila(int id_personal){
		Connection conn=null;
		String url = "SELECT DNI, NOMBRE_COMPLETO, USUARIO, CARGO, SISTEMA_USA FROM PERSONAL WHERE ID_PERSONAL=?";  
		PreparedStatement pst=null;
		ResultSet rs=null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Servidor.SERVIDOR, Servidor.USER, Servidor.PASS);
	        pst = conn.prepareStatement(url);
	        pst.setInt(1, id_personal);
	        rs = pst.executeQuery();
	        if(rs.next()){
	        	if(rs.getString("DNI")!=null){
	        		textFieldDni.setText(rs.getString("DNI"));
	        	}
	        	
	        	if(rs.getString("NOMBRE_COMPLETO")!=null){
	        		textFieldApeNom.setText(rs.getString("NOMBRE_COMPLETO"));
	        	}
	        	
	        	if(rs.getString("USUARIO")!=null){
	        		textFieldUsuario.setText(rs.getString("USUARIO"));
	        	}
	        	
	        	if(rs.getString("CARGO")!=null){
	        		textFieldCargo.setText(rs.getString("CARGO"));
	        	}
        		checkComboBoxSistemasUso.getCheckModel().clearChecks();
   	
	        	if(rs.getString("SISTEMA_USA")!=null){
	        		String[] vector = rs.getString("SISTEMA_USA").split(",");
	        		for(String string : vector){
	        			checkComboBoxSistemasUso.getCheckModel().check(string.trim());
	        		}
	        	}
	        }
	     }catch(SQLException e){
	         e.printStackTrace();
	     }catch (ClassNotFoundException e) {
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
		
	}	
	
	public void obtenerDatosPersonalBD_Buscar(String buscar){
		
		Task<Void> task=new Task<Void>(){

			@Override
			protected Void call() throws Exception {
				
				tableViewPersonal.getItems().clear();
				Connection conn=null;
				String url = "SELECT ID_PERSONAL, DNI, NOMBRE_COMPLETO, USUARIO, CARGO, SISTEMA_USA, ID_OFICINA FROM PERSONAL "
						+ "WHERE DNI LIKE ? OR NOMBRE_COMPLETO LIKE ? OR USUARIO LIKE ?;";  
				PreparedStatement pst=null;
				ResultSet rs=null;
				try{
					Class.forName("com.mysql.jdbc.Driver");
					conn = DriverManager.getConnection(Servidor.SERVIDOR, Servidor.USER, Servidor.PASS);
			        pst = conn.prepareStatement(url);
			        pst.setString(1, "%" + buscar + "%");
			        pst.setString(2, "%" + buscar + "%");
			        pst.setString(3, "%" + buscar + "%");
			        rs = pst.executeQuery();
			        
			        while(rs.next()){
			        	Personal per = new Personal(rs.getInt("ID_PERSONAL"), rs.getString("DNI"), rs.getString("NOMBRE_COMPLETO"), 
					    rs.getString("USUARIO"), rs.getString("CARGO"), rs.getString("SISTEMA_USA"), rs.getString("ID_OFICINA"));
			        
			        	if(textFieldBuscarPersonal.getText().trim().equals(buscar)){
							Platform.runLater(new Runnable() {
								
								@Override
								public void run() {
									tableViewPersonal.getItems().add(per);
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
	
	public void ActualizarDatosPersonal(){
		String mensaje =null;
		Connection conn = null;
		String url = "UPDATE PERSONAL SET DNI=?, NOMBRE_COMPLETO=?, USUARIO=?, CARGO=?, SISTEMA_USA=? "
				+ "WHERE ID_PERSONAL=?";
		PreparedStatement pst=null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Servidor.SERVIDOR, Servidor.USER, Servidor.PASS);
			pst=conn.prepareStatement(url);
			
			if(!textFieldDni.getText().trim().isEmpty()){
				pst.setString(1, textFieldDni.getText().trim());
			}else{
				pst.setNull(1, java.sql.Types.VARCHAR);
			}
			
			if(!textFieldApeNom.getText().trim().isEmpty()){
				pst.setString(2, textFieldApeNom.getText().trim());
			}else{
				pst.setNull(2, java.sql.Types.VARCHAR);
			}
			
			if(!textFieldUsuario.getText().trim().isEmpty()){
				pst.setString(3, textFieldUsuario.getText().trim());
			}else{
				pst.setNull(3, java.sql.Types.VARCHAR);
			}
			
			if(!textFieldCargo.getText().trim().isEmpty()){
				pst.setString(4, textFieldCargo.getText().trim());
			}else{
				pst.setNull(4, java.sql.Types.VARCHAR);
			}
			
			
			if(!checkComboBoxSistemasUso.getCheckModel().isEmpty()){
				String concatSistemasUso="";
				for(int i=0;i<checkComboBoxSistemasUso.getCheckModel().getItemCount();i++){
					if(checkComboBoxSistemasUso.getCheckModel().isChecked(checkComboBoxSistemasUso.getCheckModel().getItem(i))){
						if(!concatSistemasUso.equals("")){
							concatSistemasUso=concatSistemasUso+" ,";
						}
						concatSistemasUso=concatSistemasUso+checkComboBoxSistemasUso.getCheckModel().getItem(i);
					}
				}
				pst.setString(5,concatSistemasUso);

			}else{
				pst.setNull(5, java.sql.Types.VARCHAR);
			}
			
			pst.setInt(6, idPersonal);
			
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
	
	public void EliminarDatosPersonal(){
		String mensaje =null;
		Connection conn = null;
		String url = "DELETE FROM PERSONAL WHERE ID_PERSONAL=?";
		PreparedStatement pst=null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Servidor.SERVIDOR, Servidor.USER, Servidor.PASS);
			pst=conn.prepareStatement(url);
			pst.setInt(1, idPersonal);
			pst.executeUpdate();
			mensaje="Personal Eliminado";
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
	
	public void inicializarComboBox(){
		checkComboBoxSistemasUso.getItems().addAll(observableListSistemasUso);
	}
	
}
