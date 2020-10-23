package equipos;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import equipos.NuevoEquipo.NuevoEquipoControler;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class InterfazEquiposControler extends Funciones implements Initializable{
		
	@FXML private StackPane stakePaneTransparencia;
	@FXML private JFXButton jfxButtonNuevoEquipo;
    @FXML private TextField textFieldBuscarEquipos;
    @FXML private TableView<Equipos> tableViewEquipos;
    @FXML private TableColumn<Equipos,String > tableColumnApeNom;
    @FXML private TableColumn<Equipos, String> tableColumnUsuario;
    @FXML private TableColumn<Equipos, String> tableColumnCargo;
    @FXML private TableColumn<Equipos, String> tableColumnNombreOficina;
    @FXML private TableColumn<Equipos, String> tableColumnSiglas;
    @FXML private TableColumn<Equipos, String> tableColumnAnexo;
    @FXML private TableColumn<Equipos, String> tableColumntipoEquipo;
    @FXML private TableColumn<Equipos, String> tableColumnNombreEquipo;
    @FXML private TableColumn<Equipos, String> tableColumnMarca;
    @FXML private TableColumn<Equipos, String> tableColumnModelo;
    @FXML private TableColumn<Equipos, String> tableColumnSerie;
    @FXML private TableColumn<Equipos, String> tableColumnColor;
    @FXML private TableColumn<Equipos, String> tableColumnSO;
    @FXML private TableColumn<Equipos, String> tableColumnProcesador;
    @FXML private TableColumn<Equipos, String> tableColumnMemoria;
    @FXML private TableColumn<Equipos, String> tableColumnDisco;
    @FXML private TableColumn<Equipos, String> tableColumnMAC;
    @FXML private TableColumn<Equipos, String> tableColumnIP;
    @FXML private TableColumn<Equipos, String> tableColumnTipoCase;
    @FXML private TableColumn<Equipos, String> tableColumnArquitectura;
    @FXML private TableColumn<Equipos, String> tableColumnPertenece;
    @FXML private TableColumn<Equipos, String> tableColumnTipoConexion;
    @FXML private TableColumn<Equipos, String> tableColumnEstado;
    @FXML private TableColumn<Equipos, String> tableColumnCondicion;
    @FXML private Menu menuAsignarOficina;
    @FXML private Menu menuAsignarPersonal;
    @FXML private MenuItem menuItemModificarEquipo; 
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		inicializarTableViewEquipos();
		//Agregar datos de Oficina
		menuAsignarOficina.getItems().clear();
		menuAsignarOficina.getItems().addAll(new MenuItemOficina(-1,"","Ninguno"), new SeparatorMenuItem());
		menuAsignarOficina.getItems().addAll(obtenerDatosMenuItemOficina());
		
		//Agregar datos de Personal
		menuAsignarPersonal.getItems().clear();
		menuAsignarPersonal.getItems().addAll(new MenuItemPersonal(-1,"","Ninguno"), new SeparatorMenuItem());
		menuAsignarPersonal.getItems().addAll(obtenerDatosMenuItemPersonal());
		
		Task<Void> task=new Task<Void>(){

			@Override
			protected Void call() throws Exception {
				
				while(true){
					tableViewEquipos.setItems(obtenerDatosEquipoBD());
					tableViewEquipos.refresh();
					Thread.sleep(30000);
				}
			}
			
		};
		
		
		Thread hilo=new Thread(task);
		hilo.setDaemon(true);
		hilo.start();
		
		jfxButtonNuevoEquipo.setOnAction(new EventHandler<ActionEvent>() {
					
			@Override
			public void handle(ActionEvent event) {
				Node node = (Node)event.getSource();
				Stage stage=(Stage)node.getScene().getWindow();
				StackPane stackPane = (StackPane)stage.getScene().getRoot().getChildrenUnmodifiable().get(1);
				stackPane.setVisible(true);
				String url = "/equipos/NuevoEquipo/nuevoEquipo.fxml";
				String css = "/estilocss/EstiloModal.css";
				
				try {
					//mostrarInterfazModalShowAndWait(url, css);
					FXMLLoader fXMLLoader=new FXMLLoader();
					fXMLLoader.setLocation(getClass().getResource(url));
					fXMLLoader.load();
					Parent parent= fXMLLoader.getRoot();
					NuevoEquipoControler nuevoEquipoControler=fXMLLoader.getController();
					
					nuevoEquipoControler.especificarTipoModal(NuevoEquipoControler.NUEVO_EQUIPO);;
					Scene scene=new Scene(parent);
					scene.setFill(new Color(0,0,0,0));
					scene.getStylesheets().add(getClass().getResource(css).toExternalForm());
					Stage stageModal=new Stage();
					stageModal.setScene(scene);
					stageModal.initModality(Modality.APPLICATION_MODAL);
					stageModal.initStyle(StageStyle.TRANSPARENT);
					stageModal.showAndWait();
					tableViewEquipos.getItems().add(obtenerDatosEquipoBD(nuevoEquipoControler.getIdEquipo()));
				
					//tableViewEquipos.getSelectionModel().
					//tableViewEquipos.refresh();  
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				stackPane.setVisible(false);
			}
		});
		
		textFieldBuscarEquipos.setOnKeyReleased(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				obtenerDatosEquipoBD_Buscar(textFieldBuscarEquipos.getText().trim());
			}
		});
		
		
		menuItemModificarEquipo.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				//Node node = (Node)event.getSource();
				Stage stage=(Stage)tableViewEquipos.getScene().getWindow();
				StackPane stackPane = (StackPane)stage.getScene().getRoot().getChildrenUnmodifiable().get(1);
				stackPane.setVisible(true);
				String url = "/equipos/NuevoEquipo/nuevoEquipo.fxml";
				String css = "/estilocss/EstiloModal.css";
				
				try {
					//mostrarInterfazModalShowAndWait(url, css);
					FXMLLoader fXMLLoader=new FXMLLoader();
					fXMLLoader.setLocation(getClass().getResource(url));
					fXMLLoader.load();
					Parent parent= fXMLLoader.getRoot();
					NuevoEquipoControler nuevoEquipoControler=fXMLLoader.getController();
					nuevoEquipoControler.especificarTipoModal(NuevoEquipoControler.MODIFICAR_EQUIPO);
					nuevoEquipoControler.setIdEquipo(tableViewEquipos.getSelectionModel().getSelectedItem().getId_equipo());
					Scene scene=new Scene(parent);
					scene.setFill(new Color(0,0,0,0));
					scene.getStylesheets().add(getClass().getResource(css).toExternalForm());
					Stage stageModal=new Stage();
					stageModal.setScene(scene);
					stageModal.initModality(Modality.APPLICATION_MODAL);
					stageModal.initStyle(StageStyle.TRANSPARENT);
					stageModal.showAndWait();
					tableViewEquipos.getItems().add(obtenerDatosEquipoBD(nuevoEquipoControler.getIdEquipo()));
					tableViewEquipos.refresh();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				stackPane.setVisible(false);
			}
		});
	}
	
	public void inicializarTableViewEquipos(){
		//tableViewPerDiscapacidad.setItems(arrayTableViewPerDiscapacidad);
		tableColumnApeNom.setCellValueFactory(new PropertyValueFactory<>("per_apellYNombres"));
		tableColumnUsuario.setCellValueFactory(new PropertyValueFactory<>("per_usuario"));
		tableColumnCargo.setCellValueFactory(new PropertyValueFactory<>("per_cargo"));
		tableColumnNombreOficina.setCellValueFactory(new PropertyValueFactory<>("of_nombre"));
		tableColumnSiglas.setCellValueFactory(new PropertyValueFactory<>("of_siglas"));
		tableColumnAnexo.setCellValueFactory(new PropertyValueFactory<>("of_anexo"));
		tableColumntipoEquipo.setCellValueFactory(new PropertyValueFactory<>("eq_tipoEquipo"));
		tableColumnNombreEquipo.setCellValueFactory(new PropertyValueFactory<>("eq_nombre"));
		tableColumnMarca.setCellValueFactory(new PropertyValueFactory<>("eq_marca"));
		tableColumnModelo.setCellValueFactory(new PropertyValueFactory<>("eq_modelo"));
		tableColumnSerie.setCellValueFactory(new PropertyValueFactory<>("eq_serie"));
		tableColumnColor.setCellValueFactory(new PropertyValueFactory<>("eq_color"));
		tableColumnSO.setCellValueFactory(new PropertyValueFactory<>("eq_sistemaOperativo"));
		tableColumnProcesador.setCellValueFactory(new PropertyValueFactory<>("eq_procesador"));
		tableColumnMemoria.setCellValueFactory(new PropertyValueFactory<>("eq_memoria"));
		tableColumnDisco.setCellValueFactory(new PropertyValueFactory<>("eq_disco"));
		tableColumnMAC.setCellValueFactory(new PropertyValueFactory<>("eq_direccinMAC"));
		tableColumnIP.setCellValueFactory(new PropertyValueFactory<>("eq_direccionIP"));
		tableColumnTipoCase.setCellValueFactory(new PropertyValueFactory<>("eq_tipoCase"));
		tableColumnArquitectura.setCellValueFactory(new PropertyValueFactory<>("eq_arquitectura"));
		tableColumnPertenece.setCellValueFactory(new PropertyValueFactory<>("eq_pertenece"));
		tableColumnTipoConexion.setCellValueFactory(new PropertyValueFactory<>("eq_tipoConexion"));
		tableColumnEstado.setCellValueFactory(new PropertyValueFactory<>("eq_estado"));
		tableColumnCondicion.setCellValueFactory(new PropertyValueFactory<>("eq_condicion"));
	}


	//metodos

	
	public ObservableList<Equipos> obtenerDatosEquipoBD(){
		ObservableList<Equipos> observableList = FXCollections.observableArrayList();
		Connection conn=null;
		String url = "SELECT ID_EQUIPO, MARCA, MODELO, SERIE, FABRICANTE, COLOR, NOMBRE_EQUIPO, SO, PROCESADOR, MEMORIA, "
				+ "DISCO, DIRECION_IP, DIRECCION_MAC, ARQUITECTURA, (SELECT NOMBRE_TIPO FROM TIPO_EQUIPO WHERE ID_TIPO_EQUIPO=EQUIPO.ID_TIPO_EQUIPO) AS TIPO_EQUIPO,"
				+ " TIPO_CASE, PERTENECE, TIPO_CONEXION, ESTADO, "
				+ "CONDICION, PUERTO, EQUIPO.ID_OFICINA, EQUIPO.ID_PERSONAL, DNI, NOMBRE_COMPLETO, USUARIO, CARGO, "
				+ "NOMBRE_OFICINA, SIGLAS, ANEXO "
				+ "FROM EQUIPO LEFT JOIN  PERSONAL  ON EQUIPO.ID_PERSONAL=PERSONAL.ID_PERSONAL "
				+ "LEFT JOIN OFICINA ON EQUIPO.ID_OFICINA=OFICINA.ID_OFICINA;";  
		PreparedStatement pst=null;
		ResultSet rs=null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Servidor.SERVIDOR, Servidor.USER, Servidor.PASS);
	        pst = conn.prepareStatement(url); 
	        rs = pst.executeQuery();
	        while(rs.next()){
	        	observableList.add(new Equipos(rs.getInt("ID_EQUIPO"), rs.getInt("ID_OFICINA"),rs.getInt("ID_PERSONAL") , rs.getString("NOMBRE_COMPLETO"), rs.getString("USUARIO"), 
	        	rs.getString("CARGO"), rs.getString("NOMBRE_OFICINA"), rs.getString("SIGLAS"), rs.getString("ANEXO"),
	        	rs.getString("TIPO_EQUIPO"), rs.getString("NOMBRE_EQUIPO"), rs.getString("MARCA"), rs.getString("MODELO"),
	        	rs.getString("SERIE") , rs.getString("COLOR"), rs.getString("SO"), rs.getString("PROCESADOR"), rs.getString("MEMORIA"),
	        	rs.getString("DISCO"), rs.getString("DIRECCION_MAC"), rs.getString("DIRECION_IP"), rs.getString("TIPO_CASE"), rs.getString("ARQUITECTURA"), rs.getString("PERTENECE"), 
	        	rs.getString("TIPO_CONEXION"), rs.getString("ESTADO"), rs.getString("CONDICION")));
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
		
		return observableList;
	}
	
	public Equipos obtenerDatosEquipoBD(int  id_equipo){
		//ObservableList<Equipos> observableList = FXCollections.observableArrayList();
		Equipos equipos=null;
		Connection conn=null;
		String url = "SELECT ID_EQUIPO, MARCA, MODELO, SERIE, FABRICANTE, COLOR, NOMBRE_EQUIPO, SO, PROCESADOR, MEMORIA, "
				+ "DISCO, DIRECION_IP, DIRECCION_MAC, ARQUITECTURA, (SELECT NOMBRE_TIPO FROM TIPO_EQUIPO WHERE ID_TIPO_EQUIPO=EQUIPO.ID_TIPO_EQUIPO) AS TIPO_EQUIPO,"
				+ " TIPO_CASE, PERTENECE, TIPO_CONEXION, ESTADO, "
				+ "CONDICION, PUERTO, EQUIPO.ID_OFICINA, EQUIPO.ID_PERSONAL, DNI, NOMBRE_COMPLETO, USUARIO, CARGO, "
				+ "NOMBRE_OFICINA, SIGLAS, ANEXO "
				+ "FROM EQUIPO LEFT JOIN  PERSONAL  ON EQUIPO.ID_PERSONAL=PERSONAL.ID_PERSONAL "
				+ "LEFT JOIN OFICINA ON EQUIPO.ID_OFICINA=OFICINA.ID_OFICINA WHERE id_equipo =?;";  
		PreparedStatement pst=null;
		ResultSet rs=null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Servidor.SERVIDOR, Servidor.USER, Servidor.PASS);
	        pst = conn.prepareStatement(url); 
	        pst.setInt(1, id_equipo);
	        rs = pst.executeQuery();
	        while(rs.next()){
	        	equipos=new Equipos(rs.getInt("ID_EQUIPO"), rs.getInt("ID_OFICINA"),rs.getInt("ID_PERSONAL") , rs.getString("NOMBRE_COMPLETO"), rs.getString("USUARIO"), 
	    	        	rs.getString("CARGO"), rs.getString("NOMBRE_OFICINA"), rs.getString("SIGLAS"), rs.getString("ANEXO"),
	    	        	rs.getString("TIPO_EQUIPO"), rs.getString("NOMBRE_EQUIPO"), rs.getString("MARCA"), rs.getString("MODELO"),
	    	        	rs.getString("SERIE") , rs.getString("COLOR"), rs.getString("SO"), rs.getString("PROCESADOR"), rs.getString("MEMORIA"),
	    	        	rs.getString("DISCO"), rs.getString("DIRECCION_MAC"), rs.getString("DIRECION_IP"), rs.getString("TIPO_CASE"), rs.getString("ARQUITECTURA"), rs.getString("PERTENECE"), 
	    	        	rs.getString("TIPO_CONEXION"), rs.getString("ESTADO"), rs.getString("CONDICION"));
	        	//observableList.add();
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
		
		return equipos;
	}
	
	public void obtenerDatosEquipoBD_Buscar(String buscar){
		
		Task<Void> task=new Task<Void>(){

			@Override
			protected Void call() throws Exception {
				
				tableViewEquipos.getItems().clear();
				Connection conn=null;
				String url = "SELECT ID_EQUIPO, MARCA, MODELO, SERIE, FABRICANTE, COLOR, NOMBRE_EQUIPO, SO, PROCESADOR, MEMORIA, "
						+ "DISCO, DIRECION_IP, DIRECCION_MAC, ARQUITECTURA, (SELECT NOMBRE_TIPO FROM TIPO_EQUIPO WHERE ID_TIPO_EQUIPO=EQUIPO.ID_TIPO_EQUIPO) AS TIPO_EQUIPO,"
						+ " TIPO_CASE, PERTENECE, TIPO_CONEXION, ESTADO, "
						+ "CONDICION, PUERTO, EQUIPO.ID_OFICINA, EQUIPO.ID_PERSONAL, DNI, NOMBRE_COMPLETO, USUARIO, CARGO, "
						+ "NOMBRE_OFICINA, SIGLAS, ANEXO "
						+ "FROM EQUIPO LEFT JOIN  PERSONAL  ON EQUIPO.ID_PERSONAL=PERSONAL.ID_PERSONAL "
						+ "LEFT JOIN OFICINA ON EQUIPO.ID_OFICINA=OFICINA.ID_OFICINA WHERE NOMBRE_OFICINA LIKE ? OR NOMBRE_COMPLETO LIKE ? OR SIGLAS LIKE ? OR NOMBRE_EQUIPO LIKE ? OR DIRECION_IP LIKE ?;";  
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
			        	Equipos eq = new Equipos(rs.getInt("ID_EQUIPO"), rs.getInt("ID_OFICINA"),rs.getInt("ID_PERSONAL") , rs.getString("NOMBRE_COMPLETO"), rs.getString("USUARIO"), 
			        	rs.getString("CARGO"), rs.getString("NOMBRE_OFICINA"), rs.getString("SIGLAS"), rs.getString("ANEXO"),
			        	rs.getString("TIPO_EQUIPO"), rs.getString("NOMBRE_EQUIPO"), rs.getString("MARCA"), rs.getString("MODELO"),
			        	rs.getString("SERIE") , rs.getString("COLOR"), rs.getString("SO"), rs.getString("PROCESADOR"), rs.getString("MEMORIA"),
			        	rs.getString("DISCO"), rs.getString("DIRECCION_MAC"), rs.getString("DIRECION_IP"), rs.getString("TIPO_CASE"), rs.getString("ARQUITECTURA"), rs.getString("PERTENECE"), 
			        	rs.getString("TIPO_CONEXION"), rs.getString("ESTADO"), rs.getString("CONDICION"));
			        
			        	if(textFieldBuscarEquipos.getText().trim().equals(buscar)){
							Platform.runLater(new Runnable() {
								
								@Override
								public void run() {
									tableViewEquipos.getItems().add(eq);
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
	
	public ObservableList<MenuItemOficina> obtenerDatosMenuItemOficina(){
		ObservableList<MenuItemOficina> observableList = FXCollections.observableArrayList();
		Connection conn=null;
		String url = "SELECT ID_OFICINA, NOMBRE_OFICINA, SIGLAS FROM OFICINA ORDER BY NOMBRE_OFICINA";  
		PreparedStatement pst=null;
		ResultSet rs=null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Servidor.SERVIDOR, Servidor.USER, Servidor.PASS);
	        pst = conn.prepareStatement(url); 
	        rs = pst.executeQuery();
	        while(rs.next()){
	        	MenuItemOficina mioAux = new MenuItemOficina(rs.getInt("ID_OFICINA"), rs.getString("NOMBRE_OFICINA"), rs.getString("SIGLAS"));
	        	mioAux.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						MenuItemOficina act = (MenuItemOficina)event.getSource();
						ActualizarDatosEquipoOficina(act.getId_oficina(), tableViewEquipos.getSelectionModel().getSelectedItem().getId_equipo());
					}
				});
	        	observableList.add(mioAux);
	        
	        }
	     }catch(SQLException e){
	         e.printStackTrace();
	    
	     }catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
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
		
		return observableList;
	}
	
	public void ActualizarDatosEquipoOficina(int id_Oficina, int id_equipo){
		Connection conn = null;
		String url = "UPDATE EQUIPO SET ID_OFICINA=? WHERE ID_EQUIPO=?";
		PreparedStatement pst = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = (Connection) DriverManager.getConnection(Servidor.SERVIDOR, Servidor.USER, Servidor.PASS);
	        pst = conn.prepareStatement(url);
	        pst.setInt(1, id_Oficina);
	        pst.setInt(2, id_equipo);
	        pst.executeUpdate();
	        System.out.println(id_Oficina+" "+id_equipo);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				if (pst!=null) {
					pst.close();
				}
				if (conn!=null) {
					conn.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	public ObservableList<MenuItemPersonal> obtenerDatosMenuItemPersonal(){
		ObservableList<MenuItemPersonal> observableList = FXCollections.observableArrayList();
		Connection conn=null;
		String url = "SELECT ID_PERSONAL, NOMBRE_COMPLETO, USUARIO FROM PERSONAL ORDER BY NOMBRE_COMPLETO;";  
		PreparedStatement pst=null;
		ResultSet rs=null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Servidor.SERVIDOR, Servidor.USER, Servidor.PASS);
	        pst = conn.prepareStatement(url); 
	        rs = pst.executeQuery();
	        while(rs.next()){
	        	MenuItemPersonal miPAux = new MenuItemPersonal(rs.getInt("ID_PERSONAL"), rs.getString("NOMBRE_COMPLETO"), rs.getString("USUARIO"));
	        	miPAux.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						MenuItemPersonal act = (MenuItemPersonal)event.getSource();
						ActualizarDatosEquipoPersonal(act.getId_personal(), tableViewEquipos.getSelectionModel().getSelectedItem().getId_equipo());
					}
				});
	        	observableList.add(miPAux);
	        
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
		
		return observableList;
	}
	
	public void ActualizarDatosEquipoPersonal(int id_Personal, int id_equipo){
		Connection conn = null;
		String url = "UPDATE EQUIPO SET ID_PERSONAL=? WHERE ID_EQUIPO=?";
		PreparedStatement pst = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = (Connection) DriverManager.getConnection(Servidor.SERVIDOR, Servidor.USER, Servidor.PASS);
	        pst = conn.prepareStatement(url);
	        pst.setInt(1, id_Personal);
	        pst.setInt(2, id_equipo);
	        pst.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				if (pst!=null) {
					pst.close();
				}
				if (conn!=null) {
					conn.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
}
