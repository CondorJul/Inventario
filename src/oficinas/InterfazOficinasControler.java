package oficinas;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class InterfazOficinasControler extends Funciones implements Initializable {

	private int idOficina=-1;
	private ObservableList<String> observableListSistemasUso=FXCollections.observableArrayList("SIAF", "SIGA", "SISGEDO");
	
	@FXML private JFXButton jfxButtonNuevaOficina;
	@FXML private StackPane stakePaneTransparencia;
    @FXML private TextField textFieldBuscarOficinas;
    @FXML private TextField textFieldNombre;
    @FXML private TextField textFieldSiglas;
    @FXML private TextField textFieldAnexo;
    @FXML private TextField textFieldIPInferior;
    @FXML private TextField textFieldIPSuperior;
    @FXML private CheckComboBox<String> checkComboBoxSistemasUso;
    @FXML private JFXButton jfxButtonEliminar;
    @FXML private JFXButton jfxButtonGuardar;
    @FXML private TableView<Oficinas> tableViewOficinas;
    @FXML private TableColumn<Oficinas, String> tableColumnNombreOficina;
    @FXML private TableColumn<Oficinas, String> tableColumnSiglas;
    @FXML private TableColumn<Oficinas, String> tableColumnAnexo;
    @FXML private TableColumn<Oficinas, String> tableColumnIPInferior;
    @FXML private TableColumn<Oficinas, String> tableColumnIPSuperior;
    @FXML private TableColumn<Oficinas, String> tableColumnSistemasUso;
    @FXML private Label labelVerificacion;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		inicializarTableViewOficinas();
		inicializarComboBox();
		
		Task<Void> task=new Task<Void>(){

			@Override
			protected Void call() throws Exception {
				Consulta_BaseDatosOficinas cv=new Consulta_BaseDatosOficinas();
				
				while(true){
					tableViewOficinas.setItems(cv.obtenerDatosOficinasBD());
					Thread.sleep(30000);
					//ocultarVerificacion();
				}
			}
			
		};
		Thread hilo=new Thread(task);
		hilo.setDaemon(true);
		hilo.start();
		
		jfxButtonNuevaOficina.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				Node node = (Node)event.getSource();
				Stage stage = (Stage)node.getScene().getWindow();
				StackPane stackPane = (StackPane)stage.getScene().getRoot().getChildrenUnmodifiable().get(1);
				stackPane.setVisible(true);
				
				String url = "/oficinas/NuevaOficina/NuevaOficina.fxml";
				String css = "/estilocss/EstiloModal.css";
				
				try {
					mostrarInterfazModalShowAndWait(url, css);
				} catch (IOException e) {
					e.printStackTrace();
				}
				stackPane.setVisible(false);
			}
		});
		
		tableViewOficinas.getSelectionModel().selectedItemProperty().addListener((newSelection) -> {
			if (newSelection != null) {
				ObtenerDatosOficinasFila(tableViewOficinas.getSelectionModel().getSelectedItem().getId_oficina());
				idOficina = tableViewOficinas.getSelectionModel().getSelectedItem().getId_oficina();
			}
		});
		
		textFieldBuscarOficinas.setOnKeyReleased(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				obtenerDatosOficinasBD_Buscar(textFieldBuscarOficinas.getText().trim());
			}
		});
		
		jfxButtonGuardar.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				ActualizarDatosOficina();
			}
		});
		
		jfxButtonEliminar.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if (EliminarDatos()) {
					EliminarDatosOficina();
					//
				}else{
					return;
				}
			}
		});
	
	}

	public void inicializarTableViewOficinas(){
		tableColumnNombreOficina.setCellValueFactory(new PropertyValueFactory<>("of_nombre"));
		tableColumnSiglas.setCellValueFactory(new PropertyValueFactory<>("of_siglas"));
		tableColumnAnexo.setCellValueFactory(new PropertyValueFactory<>("of_anexo"));
		tableColumnIPInferior.setCellValueFactory(new PropertyValueFactory<>("of_rango_ip_inf"));
		tableColumnIPSuperior.setCellValueFactory(new PropertyValueFactory<>("of_rango_ip_sup"));
		tableColumnSistemasUso.setCellValueFactory(new PropertyValueFactory<>("of_sistema_usa"));
	}
	
	class Consulta_BaseDatosOficinas{
		public ObservableList<Oficinas> obtenerDatosOficinasBD(){
			ObservableList<Oficinas> observableList = FXCollections.observableArrayList();
			Connection conn=null;
			String url = "SELECT ID_OFICINA, NOMBRE_OFICINA, SIGLAS, ANEXO, RANGO_IP_INF, RANGO_IP_SUP, SISTEMA_USA FROM OFICINA";  
			PreparedStatement pst=null;
			ResultSet rs=null;
			try{
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection(Servidor.SERVIDOR, Servidor.USER, Servidor.PASS);
		        pst = conn.prepareStatement(url); 
		        rs = pst.executeQuery();
		        while(rs.next()){
		        	observableList.add(new Oficinas(rs.getInt("ID_OFICINA"), rs.getString("NOMBRE_OFICINA"), rs.getString("SIGLAS"), 
		        	rs.getString("ANEXO"), rs.getString("RANGO_IP_INF"), rs.getString("RANGO_IP_SUP"), rs.getString("SISTEMA_USA")));
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
			return observableList;
		}
	}
	
	public void ObtenerDatosOficinasFila(int id_oficina){
		Connection conn=null;
		String url = "SELECT ID_OFICINA, NOMBRE_OFICINA, SIGLAS, ANEXO, RANGO_IP_INF, RANGO_IP_SUP, SISTEMA_USA FROM OFICINA WHERE ID_OFICINA=?";  
		PreparedStatement pst=null;
		ResultSet rs=null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Servidor.SERVIDOR, Servidor.USER, Servidor.PASS);
	        pst = conn.prepareStatement(url);
	        pst.setInt(1, id_oficina);
	        rs = pst.executeQuery();
	        if(rs.next()){
	        	if(rs.getString("NOMBRE_OFICINA")!=null){
	        		textFieldNombre.setText(rs.getString("NOMBRE_OFICINA"));
	        	}
	        	
	        	if(rs.getString("SIGLAS")!=null){
	        		textFieldSiglas.setText(rs.getString("SIGLAS"));
	        	}
	        	
	        	if(rs.getString("ANEXO")!=null){
	        		textFieldAnexo.setText(rs.getString("ANEXO"));
	        	}
	        	
	        	if(rs.getString("RANGO_IP_INF")!=null){
	        		textFieldIPInferior.setText(rs.getString("RANGO_IP_INF"));
	        	}
	        	
	        	if(rs.getString("RANGO_IP_SUP")!=null){
	        		textFieldIPSuperior.setText(rs.getString("RANGO_IP_SUP"));
	        	}
	        	
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
	
	public void obtenerDatosOficinasBD_Buscar(String buscar){
		
		Task<Void> task=new Task<Void>(){

			@Override
			protected Void call() throws Exception {
				
				tableViewOficinas.getItems().clear();
				Connection conn=null;
				String url = "SELECT ID_OFICINA, NOMBRE_OFICINA, SIGLAS, ANEXO, RANGO_IP_INF, RANGO_IP_SUP, SISTEMA_USA FROM OFICINA "
						+ "WHERE NOMBRE_OFICINA LIKE ? OR SIGLAS LIKE ? OR ANEXO LIKE ? OR RANGO_IP_INF LIKE ? OR RANGO_IP_SUP LIKE ?;";  
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
			        pst.setString(5, "%" + buscar + "%");
			        rs = pst.executeQuery();
			        
			        while(rs.next()){
			        	Oficinas of = new Oficinas(rs.getInt("ID_OFICINA"), rs.getString("NOMBRE_OFICINA"), rs.getString("SIGLAS"), 
					    rs.getString("ANEXO"), rs.getString("RANGO_IP_INF"), rs.getString("RANGO_IP_SUP"), rs.getString("SISTEMA_USA"));
			        
			        	if(textFieldBuscarOficinas.getText().trim().equals(buscar)){
							Platform.runLater(new Runnable() {
								
								@Override
								public void run() {
									tableViewOficinas.getItems().add(of);
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
	
	public void ActualizarDatosOficina(){
		String mensaje =null;
		Connection conn = null;
		String url = "UPDATE OFICINA SET NOMBRE_OFICINA=?, SIGLAS=?, ANEXO=?, RANGO_IP_INF=?, RANGO_IP_SUP=?, SISTEMA_USA=? "
				+ "WHERE ID_OFICINA=?";
		PreparedStatement pst=null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Servidor.SERVIDOR, Servidor.USER, Servidor.PASS);
			pst=conn.prepareStatement(url);
			if(!textFieldNombre.getText().trim().isEmpty()){
				pst.setString(1,textFieldNombre.getText().trim());
			}else{
				pst.setNull(1, java.sql.Types.VARCHAR);
			}
			
			if(!textFieldSiglas.getText().trim().isEmpty()){
				pst.setString(2,textFieldSiglas.getText().trim());
			}else{
				pst.setNull(2, java.sql.Types.VARCHAR);
			}
			
			if(!textFieldAnexo.getText().trim().isEmpty()){
				pst.setString(3,textFieldAnexo.getText().trim());
			}else{
				pst.setNull(3, java.sql.Types.VARCHAR);
			}
			
			if(!textFieldIPInferior.getText().trim().isEmpty()){
				pst.setString(4,textFieldIPInferior.getText().trim());
			}else{
				pst.setNull(4, java.sql.Types.VARCHAR);
			}
			
			if(!textFieldIPSuperior.getText().trim().isEmpty()){
				pst.setString(5,textFieldIPSuperior.getText().trim());
			}else{
				pst.setNull(5, java.sql.Types.VARCHAR);
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
				pst.setString(6,concatSistemasUso);

			}else{
				pst.setNull(6, java.sql.Types.VARCHAR);
			}
			pst.setInt(7, idOficina);
			
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
	
	public void EliminarDatosOficina(){
		String mensaje =null;
		Connection conn = null;
		String url = "DELETE FROM OFICINA WHERE ID_OFICINA=?";
		PreparedStatement pst=null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Servidor.SERVIDOR, Servidor.USER, Servidor.PASS);
			pst=conn.prepareStatement(url);
			pst.setInt(1, idOficina);
			pst.executeUpdate();
			mensaje="Oficina Eliminado";
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
