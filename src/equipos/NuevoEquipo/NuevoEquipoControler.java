package equipos.NuevoEquipo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.OperatingSystem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import com.jfoenix.controls.JFXButton;

import equipos.TipoEquipo.TipoEquipo;
import equipos.TipoEquipo.TipoEquipoControler;
import funciones.Conexion;
import funciones.Funciones;
import funciones.Servidor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NuevoEquipoControler extends Funciones implements Initializable{
	private int idEquipo=-1;
	///NUEVAS MODIFICACIONES PARA INSERTAR Y ACTUALIZAR
	private int tipoModal=-1;
	public static final int NUEVO_EQUIPO=1;
	public static final int MODIFICAR_EQUIPO=2;
	public void especificarTipoModal(int tipoModal){
		this.tipoModal=tipoModal;
		if(tipoModal==NUEVO_EQUIPO){

		}else if(tipoModal==MODIFICAR_EQUIPO){
			
		}
		
	}
	
	public int getIdEquipo(){
		return idEquipo;
	}
	public void setIdEquipo(int idEquipo ){
		this.idEquipo=idEquipo;
		if(tipoModal==MODIFICAR_EQUIPO){
			mostrarDatosSQL(idEquipo);
		}
	}
		
	//Fxcollecction 
	private ObservableList<String> observableListSistemaOperativo=FXCollections.observableArrayList("Windows 7","Windows 8","Windows 8.1", "Windows 10");
	private ObservableList<String> observableListArquitectura=FXCollections.observableArrayList("x86", "x64");
	private ObservableList<String> observableListTipoCase=FXCollections.observableArrayList("Barebone","Minitorre","Sobremesa","Mediatorre","Torre","Servidor","Rack","Portatil","Integrado a la pantalla");
	private ObservableList<String> observableListPertenece=FXCollections.observableArrayList("Insitucional","Propio");
	private ObservableList<String> observableListTipoConexion=FXCollections.observableArrayList("Red Ethernet","Red Wi-Fi");
	private ObservableList<String> observableListEstado=FXCollections.observableArrayList("Inhabilitado","Habilitado");
	private ObservableList<String> observableListCondicion=FXCollections.observableArrayList("Bueno","Regular","Malo");
	
	@FXML private ComboBox<TipoEquipo> comboBoxTipoEquipo;

	@FXML private Button buttonAgregarTipoEquipo;
    @FXML private TextField textFieldMarca;
    @FXML private TextField textFieldModelo;
    @FXML private TextField textFieldSerie;
    @FXML private TextField textFieldFabricante;
    @FXML private TextField textFieldColor;
    @FXML private TextField textFieldNombreEquipo;
    @FXML private ComboBox<String> comboBoxSistemaOperativo;
    @FXML private TextField textFieldProcesador;
    @FXML private TextField textFieldMemoria;
    @FXML private TextField textFieldDisco;
    @FXML private TextField textFieldDireccionIPV4;
    @FXML private TextField textFieldDirecionFisicaMac;
    @FXML private ComboBox<String> comboBoxArquitectura;
    @FXML private ComboBox<String> comboBoxTipoCase;
    @FXML private ComboBox<String> comboBoxPertenece;
    @FXML private ComboBox<String> comboBoxTipoConexion;
    @FXML private ComboBox<String> comboBoxEstado;
    @FXML private ComboBox<String> comboBoxCondicion;
    @FXML private TextField textFieldNroPuertos;
    @FXML private JFXButton jfxButtonCerrar;
    @FXML private JFXButton jfxButtonValidar;
    @FXML private ProgressIndicator ProgressIndicatorCargando;
    @FXML private Label labelVerificacion;
    @FXML private Button buttonObtenerDatos;
    @FXML private Label labelObtenerDatos;
    
    private Sigar sigar;
   
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	sigar = new Sigar();
		inicializarComboboxs();
		ListarTipoEquipos();
		
		buttonObtenerDatos.setOnMouseEntered(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				labelObtenerDatos.setVisible(true);
			}
		});
		
		buttonObtenerDatos.setOnMouseExited(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				labelObtenerDatos.setVisible(false);
			}
		});
		
		buttonObtenerDatos.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				obtenerDatosPC();
			}
		});
		
		buttonAgregarTipoEquipo.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				String url = "/equipos/TipoEquipo/TipoEquipo.fxml";
				String css =  "/estilocss/EstiloModal.css";
							
				try {
										
					mostrarInterfazModalShowAndWait(url, css);
					TipoEquipo tipoEquipoAuxiliar=comboBoxTipoEquipo.getSelectionModel().getSelectedItem();
					ListarTipoEquipos();
					if(TipoEquipoControler.TIPO_EQUIPO_ULTIMA_INSERCION!=null){
						comboBoxTipoEquipo.getSelectionModel().select(TipoEquipoControler.TIPO_EQUIPO_ULTIMA_INSERCION);
						TipoEquipoControler.TIPO_EQUIPO_ULTIMA_INSERCION=null;
					}else{
						comboBoxTipoEquipo.getSelectionModel().select(tipoEquipoAuxiliar);
						TipoEquipoControler.TIPO_EQUIPO_ULTIMA_INSERCION=null;
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			
			}
		});
		  
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
				//labelVerificacion.setText(InsertarDatosEquipo());
				if(tipoModal==NUEVO_EQUIPO){
					InsertarDatosEquipo();
				}else if(tipoModal==MODIFICAR_EQUIPO){
					actualizarDatosEquipo();
				}
			
			}
		});
					
	}
	//Metodos
    public void InsertarDatosEquipo(){
    	
    	ProgressIndicatorCargando.setVisible(true);
 
    	Task<Void> task=new Task<Void>(){

			@Override
			protected Void call() throws Exception {
					        
				String mensaje =null;
				Connection conn = null;
				String url = "INSERT INTO EQUIPO (MARCA, MODELO, SERIE, FABRICANTE,"
						+ " COLOR, NOMBRE_EQUIPO, SO, PROCESADOR, MEMORIA, DISCO, DIRECION_IP, "
						+ "DIRECCION_MAC, ARQUITECTURA, TIPO_CASE, PERTENECE, TIPO_CONEXION, ESTADO, "
						+ "CONDICION, PUERTO, ID_OFICINA, ID_PERSONAL, ID_TIPO_EQUIPO)"
						+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
				PreparedStatement pst=null;
				ResultSet rs=null;
				try {
					Conexion.conexionDB();
					conn=Conexion.getConexion();
					pst=conn.prepareStatement(url,PreparedStatement.RETURN_GENERATED_KEYS);
					if(!textFieldMarca.getText().trim().isEmpty()){
						pst.setString(1,textFieldMarca.getText().trim());
					}else{
						pst.setNull(1, java.sql.Types.VARCHAR);
					}
					
					if(!textFieldModelo.getText().trim().isEmpty()){
						pst.setString(2,textFieldModelo.getText().trim());
					}else{
						pst.setNull(2, java.sql.Types.VARCHAR);
					}
					
					if(!textFieldSerie.getText().trim().isEmpty()){
						pst.setString(3,textFieldSerie.getText().trim());
					}else{
						pst.setNull(3, java.sql.Types.VARCHAR);
					}
					
					if(!textFieldFabricante.getText().trim().isEmpty()){
						pst.setString(4,textFieldFabricante.getText().trim());
					}else{
						pst.setNull(4, java.sql.Types.VARCHAR);
					}
					
					
					if(!textFieldColor.getText().trim().isEmpty()){
						pst.setString(5,textFieldColor.getText().trim());
					}else{
						pst.setNull(5, java.sql.Types.VARCHAR);
					}
					
					if(!textFieldNombreEquipo.getText().trim().isEmpty()){
						pst.setString(6,textFieldNombreEquipo.getText().trim());
					}else{
						pst.setNull(6, java.sql.Types.VARCHAR);
					}
					
					
					if(comboBoxSistemaOperativo.getSelectionModel().getSelectedItem()!=null){
						pst.setString(7,comboBoxSistemaOperativo.getSelectionModel().getSelectedItem());
					}else{
						pst.setNull(7, java.sql.Types.VARCHAR);
					}
					
					if(!textFieldProcesador.getText().trim().isEmpty()){
						pst.setString(8,textFieldProcesador.getText().trim());
					}else{
						pst.setNull(8, java.sql.Types.VARCHAR);
					}
					
					if(!textFieldMemoria.getText().trim().isEmpty()){
						pst.setString(9,textFieldMemoria.getText().trim());
					}else{
						pst.setNull(9, java.sql.Types.VARCHAR);
					}
					
					if(!textFieldDisco.getText().trim().isEmpty()){
						pst.setString(10,textFieldDisco.getText().trim());
					}else{
						pst.setNull(10, java.sql.Types.VARCHAR);
					}
					
					
					if(!textFieldDireccionIPV4.getText().trim().isEmpty()){
						pst.setString(11,textFieldDireccionIPV4.getText().trim());
					}else{
						pst.setNull(11, java.sql.Types.VARCHAR);
					}
					
					if(!textFieldDirecionFisicaMac.getText().trim().isEmpty()){
						pst.setString(12,textFieldDirecionFisicaMac.getText().trim());
					}else{
						pst.setNull(12, java.sql.Types.VARCHAR);
					}
					
					if(comboBoxArquitectura.getSelectionModel().getSelectedItem()!=null){
						pst.setString(13,comboBoxArquitectura.getSelectionModel().getSelectedItem());
					}else{
						pst.setNull(13, java.sql.Types.VARCHAR);
					}
					
					if(comboBoxTipoCase.getSelectionModel().getSelectedItem()!=null){
						pst.setString(14,comboBoxTipoCase.getSelectionModel().getSelectedItem());
					}else{
						pst.setNull(14, java.sql.Types.VARCHAR);
					}
					
					if(comboBoxPertenece.getSelectionModel().getSelectedItem()!=null){
						pst.setString(15,comboBoxPertenece.getSelectionModel().getSelectedItem());
					}else{
						pst.setNull(15, java.sql.Types.VARCHAR);
					}
					
					if(comboBoxTipoConexion.getSelectionModel().getSelectedItem()!=null){
						pst.setString(16,comboBoxTipoConexion.getSelectionModel().getSelectedItem());
					}else{
						pst.setNull(16, java.sql.Types.VARCHAR);
					}
					
					if(comboBoxEstado.getSelectionModel().getSelectedItem()!=null){
						pst.setString(17,comboBoxEstado.getSelectionModel().getSelectedItem());
					}else{
						pst.setNull(17, java.sql.Types.VARCHAR);
					}
					
					if(comboBoxCondicion.getSelectionModel().getSelectedItem()!=null){
						pst.setString(18,comboBoxCondicion.getSelectionModel().getSelectedItem());
					}else{
						pst.setNull(18, java.sql.Types.VARCHAR);
					}
					
					if(!textFieldNroPuertos.getText().trim().isEmpty()){
						pst.setInt(19,Integer.parseInt(textFieldNroPuertos.getText().trim()));
					}else{
						pst.setNull(19, java.sql.Types.INTEGER);
					}

					pst.setNull(20, java.sql.Types.INTEGER);
					pst.setNull(21, java.sql.Types.INTEGER);
					
					if(comboBoxTipoEquipo.getSelectionModel().getSelectedItem()!=null){
						pst.setInt(22,comboBoxTipoEquipo.getSelectionModel().getSelectedItem().getIdTipoEquipo());
					}else{
						pst.setNull(22, java.sql.Types.INTEGER);
					}


					pst.executeUpdate();
					rs=pst.getGeneratedKeys();

					while(rs.next()){
						idEquipo=rs.getInt(1);
					}
					
					mensaje="Equipo Insertado Exitosamente.";
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
				
				ProgressIndicatorCargando.setVisible(false);
				return null;
			}  		
    	};
    	
    	Thread hilo=new Thread(task);
    	hilo.setDaemon(true);
    	hilo.start();
   	
	}

    public void actualizarDatosEquipo(){
    	
    	ProgressIndicatorCargando.setVisible(true);
 
    	Task<Void> task=new Task<Void>(){

			@Override
			protected Void call() throws Exception {
					        
				String mensaje =null;
				Connection conn = null;
				String url = "UPDATE  EQUIPO SET MARCA=?, MODELO=?, SERIE=?, FABRICANTE=?,"
						+ " COLOR=?, NOMBRE_EQUIPO=?, SO=?, PROCESADOR=?, MEMORIA=?, DISCO=?, DIRECION_IP=?, "
						+ "DIRECCION_MAC=?, ARQUITECTURA=?, TIPO_CASE=?, PERTENECE=?, TIPO_CONEXION=?, ESTADO=?, "
						+ "CONDICION=?, PUERTO=?, ID_TIPO_EQUIPO=? "
						+ " WHERE ID_EQUIPO =?";
				PreparedStatement pst=null;
				
				
				
				
				try {
					Conexion.conexionDB();
					conn=Conexion.getConexion();
					pst=conn.prepareStatement(url);
					if(!textFieldMarca.getText().trim().isEmpty()){
						pst.setString(1,textFieldMarca.getText().trim());
					}else{
						pst.setNull(1, java.sql.Types.VARCHAR);
					}
					
					if(!textFieldModelo.getText().trim().isEmpty()){
						pst.setString(2,textFieldModelo.getText().trim());
					}else{
						pst.setNull(2, java.sql.Types.VARCHAR);
					}
					
					if(!textFieldSerie.getText().trim().isEmpty()){
						pst.setString(3,textFieldSerie.getText().trim());
					}else{
						pst.setNull(3, java.sql.Types.VARCHAR);
					}
					
					if(!textFieldFabricante.getText().trim().isEmpty()){
						pst.setString(4,textFieldFabricante.getText().trim());
					}else{
						pst.setNull(4, java.sql.Types.VARCHAR);
					}
					
					
					if(!textFieldColor.getText().trim().isEmpty()){
						pst.setString(5,textFieldColor.getText().trim());
					}else{
						pst.setNull(5, java.sql.Types.VARCHAR);
					}
					
					if(!textFieldNombreEquipo.getText().trim().isEmpty()){
						pst.setString(6,textFieldNombreEquipo.getText().trim());
					}else{
						pst.setNull(6, java.sql.Types.VARCHAR);
					}
					
					
					if(comboBoxSistemaOperativo.getSelectionModel().getSelectedItem()!=null){
						pst.setString(7,comboBoxSistemaOperativo.getSelectionModel().getSelectedItem());
					}else{
						pst.setNull(7, java.sql.Types.VARCHAR);
					}
					
					if(!textFieldProcesador.getText().trim().isEmpty()){
						pst.setString(8,textFieldProcesador.getText().trim());
					}else{
						pst.setNull(8, java.sql.Types.VARCHAR);
					}
					
					if(!textFieldMemoria.getText().trim().isEmpty()){
						pst.setString(9,textFieldMemoria.getText().trim());
					}else{
						pst.setNull(9, java.sql.Types.VARCHAR);
					}
					
					if(!textFieldDisco.getText().trim().isEmpty()){
						pst.setString(10,textFieldDisco.getText().trim());
					}else{
						pst.setNull(10, java.sql.Types.VARCHAR);
					}
					
					
					if(!textFieldDireccionIPV4.getText().trim().isEmpty()){
						pst.setString(11,textFieldDireccionIPV4.getText().trim());
					}else{
						pst.setNull(11, java.sql.Types.VARCHAR);
					}
					
					if(!textFieldDirecionFisicaMac.getText().trim().isEmpty()){
						pst.setString(12,textFieldDirecionFisicaMac.getText().trim());
					}else{
						pst.setNull(12, java.sql.Types.VARCHAR);
					}
					
					if(comboBoxArquitectura.getSelectionModel().getSelectedItem()!=null){
						pst.setString(13,comboBoxArquitectura.getSelectionModel().getSelectedItem());
					}else{
						pst.setNull(13, java.sql.Types.VARCHAR);
					}
					
					if(comboBoxTipoCase.getSelectionModel().getSelectedItem()!=null){
						pst.setString(14,comboBoxTipoCase.getSelectionModel().getSelectedItem());
					}else{
						pst.setNull(14, java.sql.Types.VARCHAR);
					}
					
					if(comboBoxPertenece.getSelectionModel().getSelectedItem()!=null){
						pst.setString(15,comboBoxPertenece.getSelectionModel().getSelectedItem());
					}else{
						pst.setNull(15, java.sql.Types.VARCHAR);
					}
					
					if(comboBoxTipoConexion.getSelectionModel().getSelectedItem()!=null){
						pst.setString(16,comboBoxTipoConexion.getSelectionModel().getSelectedItem());
					}else{
						pst.setNull(16, java.sql.Types.VARCHAR);
					}
					
					if(comboBoxEstado.getSelectionModel().getSelectedItem()!=null){
						pst.setString(17,comboBoxEstado.getSelectionModel().getSelectedItem());
					}else{
						pst.setNull(17, java.sql.Types.VARCHAR);
					}
					
					if(comboBoxCondicion.getSelectionModel().getSelectedItem()!=null){
						pst.setString(18,comboBoxCondicion.getSelectionModel().getSelectedItem());
					}else{
						pst.setNull(18, java.sql.Types.VARCHAR);
					}
					
					if(!textFieldNroPuertos.getText().trim().isEmpty()){
						pst.setInt(19,Integer.parseInt(textFieldNroPuertos.getText().trim()));
					}else{
						pst.setNull(19, java.sql.Types.INTEGER);
					}

					
					if(comboBoxTipoEquipo.getSelectionModel().getSelectedItem()!=null){
						pst.setInt(20,comboBoxTipoEquipo.getSelectionModel().getSelectedItem().getIdTipoEquipo());
					}else{
						pst.setNull(20, java.sql.Types.INTEGER);
					}

					pst.setInt(21, idEquipo);
					pst.executeUpdate();
		         	mensaje="Equipo Modificado Exitosamente.";
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
				
				ProgressIndicatorCargando.setVisible(false);
				return null;
			}  		
    	};
    	
    	Thread hilo=new Thread(task);
    	hilo.setDaemon(true);
    	hilo.start();
   	
	}
    
    public void ListarTipoEquipos(){
    	    	
     	comboBoxTipoEquipo.getItems().clear();
    	Connection conn = null;
		String url = "SELECT ID_TIPO_EQUIPO, NOMBRE_TIPO FROM TIPO_EQUIPO;";
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try {
			Conexion.conexionDB();
			conn=Conexion.getConexion();
			pst=conn.prepareStatement(url);
			rs=pst.executeQuery();
			while(rs.next()){
				
				comboBoxTipoEquipo.getItems().add(new TipoEquipo(rs.getInt("ID_TIPO_EQUIPO"), rs.getString("NOMBRE_TIPO")));
			}
            
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				if(pst!=null){
					pst.close();
				}
       			if(conn!=null){
       				conn.close();
       			}
       			if(rs!=null){
       				rs.close();
       			}
       			
   			}catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
	}
 
	public void inicializarComboboxs(){
		comboBoxSistemaOperativo.getItems().addAll(observableListSistemaOperativo);
		comboBoxArquitectura.getItems().addAll(observableListArquitectura);
		comboBoxTipoCase.getItems().addAll(observableListTipoCase);
		comboBoxPertenece.getItems().addAll(observableListPertenece);
		comboBoxTipoConexion.getItems().addAll(observableListTipoConexion);
		comboBoxEstado.getItems().addAll(observableListEstado);
		comboBoxCondicion.getItems().addAll(observableListCondicion);
	}
	
	public void obtenerDatosPC(){
		try {
			InetAddress pcAddress = InetAddress.getLocalHost();
			textFieldNombreEquipo.setText(pcAddress.getHostName());
			textFieldDireccionIPV4.setText(pcAddress.getHostAddress());
			textFieldDirecionFisicaMac.setText(conseguirMAC());
			textFieldProcesador.setText(conseguirProcesador());
			
			double memoria = Double.parseDouble(conseguirMemoria());
			double ram = Math.round(memoria/1024);
			textFieldMemoria.setText(ram+" GB");
			
			textFieldDisco.setText(disco());
			
			String sSistemaOperativo = System.getProperty("os.name");
			comboBoxSistemaOperativo.getSelectionModel().select(sSistemaOperativo);
			comboBoxArquitectura.getSelectionModel().select(conseguirArquitectura());
			//System.out.println(sSistemaOperativo);
			
			//String sArquitectura = System.getProperty("os.arch");
			//System.out.println(sArquitectura);
			
			//String sVersion = System.getProperty("os.version");
			//System.out.println(sVersion);
			
			//String sUserMaquina = System.getProperty("user.name");
			//System.out.println(sUserMaquina);
			
			//tex"os.arch"
			//"os.arch"
			//textFieldDireccionFisicaMac.setText(pcAddress.get);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	//obtener direccion mac
	public String conseguirMAC(){
		  StringBuilder sb = new StringBuilder();
		  NetworkInterface a;
		  try {
			  a = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
			  byte[] mac = a.getHardwareAddress();
			  for (int i = 0; i < mac.length; i++) {
				  sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
			  }
		  } catch (Exception e) {
			  e.printStackTrace(); 
		  }
		  return sb.toString();
	}
	
	//Obtener arquitectura
	public String conseguirArquitectura() {
		OperatingSystem sys = OperatingSystem.getInstance();
        return sys.getArch();
	}
	
	//obtener procesador
	public String conseguirProcesador(){
		CpuInfo[] infos = null;
		try {
			infos = sigar.getCpuInfoList();
		} catch (SigarException e) {
			e.printStackTrace();
		}
		
		CpuInfo info = infos[0];
        return info.getVendor()+" "+info.getModel();
	}
	
	//Obtener memoria
	public String conseguirMemoria(){
		
        Mem memoria=null;
		try {
			memoria = sigar.getMem();
		} catch (SigarException e) {
			e.printStackTrace();
		}       
        return memoria.getRam()+"";
    }
	
	//obtener disco
	public String disco(){
	    String dp="";
	    dp = System.getProperty("user.home");
	    dp=dp.substring(0, 1);
	    dp = dp+":"+"/";
	               
	    return dp;
	}
	
	public void mostrarDatosSQL(int id_equipo){
		//ObservableList<Equipos> observableList = FXCollections.observableArrayList();
		
		Connection conn=null;
		String url = "SELECT  MARCA, MODELO, SERIE, FABRICANTE, COLOR, NOMBRE_EQUIPO,SO, "
				+ "PROCESADOR, MEMORIA, DISCO, DIRECION_IP, DIRECCION_MAC, ARQUITECTURA,"
				+ " TIPO_CASE, PERTENECE, TIPO_CONEXION, ESTADO, CONDICION, PUERTO, ID_OFICINA,"
				+ " ID_PERSONAL, ID_TIPO_EQUIPO FROM equipo WHERE ID_EQUIPO=?;";  
		PreparedStatement pst=null;
		ResultSet rs=null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Servidor.SERVIDOR, Servidor.USER, Servidor.PASS);
	        pst = conn.prepareStatement(url); 
	        pst.setInt(1, id_equipo);
	        rs = pst.executeQuery();
	        while(rs.next()){
	        	if(rs.getString("NOMBRE_EQUIPO")!=null){
	        		textFieldNombreEquipo.setText(rs.getString("NOMBRE_EQUIPO"));
	        	}
	        	
	        	if(rs.getString("MARCA")!=null){
	        		textFieldMarca.setText(rs.getString("MARCA"));
	        	}
	        	
	        	if(rs.getString("MODELO")!=null){
	        		textFieldModelo.setText(rs.getString("MODELO"));
	        		
	        	}
	        	
	        	if(rs.getString("SERIE")!=null){
	        		textFieldSerie.setText(rs.getString("SERIE"));
	        		
	        	}
	        	
	        	if(rs.getString("FABRICANTE")!=null){
	        		textFieldFabricante.setText(rs.getString("FABRICANTE"));
	        		
	        	}
	        	
	        	if(rs.getString("COLOR")!=null){
	        		textFieldColor.setText(rs.getString("COLOR"));	        		
	        	}
	        	
	        	if(rs.getString("SO")!=null){
	        		comboBoxSistemaOperativo.getSelectionModel().select(rs.getString("SO"));
	        	}
	        	
	        	if(rs.getString("PROCESADOR")!=null){
	        		textFieldProcesador.setText(rs.getString("PROCESADOR"));	        		
	        	}
	        	
	        	if(rs.getString("MEMORIA")!=null){
	        		textFieldMemoria.setText(rs.getString("MEMORIA"));	        		
	        	}
	        	
	        	if(rs.getString("DISCO")!=null){
	        		textFieldDisco.setText(rs.getString("DISCO"));	        		
	        	}
	        	
	        	if(rs.getString("DIRECION_IP")!=null){
	        		textFieldDireccionIPV4.setText(rs.getString("DIRECION_IP"));	        		
	        	}
	        	

	        	if(rs.getString("DIRECION_IP")!=null){
	        		textFieldDireccionIPV4.setText(rs.getString("DIRECION_IP"));	        		
	        	}
	        	
	        	if(rs.getString("DIRECCION_MAC")!=null){
	        		textFieldDirecionFisicaMac.setText(rs.getString("DIRECCION_MAC"));	        		
	        	}
	        	
	        	if(rs.getString("ARQUITECTURA")!=null){
	        		comboBoxArquitectura.getSelectionModel().select(rs.getString("ARQUITECTURA"));        		
	        	}

	        	if(rs.getString("TIPO_CASE")!=null){
	        		comboBoxTipoCase.getSelectionModel().select(rs.getString("TIPO_CASE"));        		
	        	}
	        	if(rs.getString("PERTENECE")!=null){
	        		comboBoxPertenece.getSelectionModel().select(rs.getString("PERTENECE"));        		
	        	}
	        	
	        	if(rs.getString("TIPO_CONEXION")!=null){
	        		comboBoxTipoConexion.getSelectionModel().select(rs.getString("TIPO_CONEXION"));        		
	        	}
	        	if(rs.getString("ESTADO")!=null){
	        		comboBoxEstado.getSelectionModel().select(rs.getString("ESTADO"));        		
	        	}
	        	if(rs.getString("CONDICION")!=null){
	        		comboBoxCondicion.getSelectionModel().select(rs.getString("CONDICION"));        		
	        	}
	        	if(rs.getBoolean("PUERTO")){
	        		textFieldNroPuertos.setText(rs.getString("PUERTO"));        		
	        	}
	        	
	        	if(rs.getBoolean("ID_TIPO_EQUIPO")){
	        		comboBoxTipoEquipo.getSelectionModel().select(new TipoEquipo(rs.getInt("ID_TIPO_EQUIPO"), null));
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
		
		//return equipos;
	}
	
}
