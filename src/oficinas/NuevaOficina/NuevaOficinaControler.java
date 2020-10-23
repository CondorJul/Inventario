package oficinas.NuevaOficina;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.controlsfx.control.CheckComboBox;

import com.jfoenix.controls.JFXButton;

import funciones.Conexion;
import funciones.Funciones;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NuevaOficinaControler extends Funciones implements Initializable{
	
	private ObservableList<String> observableListSistemasUso=FXCollections.observableArrayList("SIAF", "SIGA", "SISGEDO");
	
	@FXML private TextField textFieldNombre;
    @FXML private TextField textFieldSiglas;
    @FXML private TextField textFieldAnexo;
    @FXML private TextField textFieldRangoIPInferior;
    @FXML private TextField textFieldRangoIPSuperior;
    @FXML private CheckComboBox<String> checkComboBoxSistemasUso;
    @FXML private JFXButton jfxButtonCerrar;
    @FXML private JFXButton jfxButtonValidar;
    @FXML private Label labelVerificacion;
		
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		inicializarComboBox();
		
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
				insertarDatosOficinas();
			}
		});
		
	}
	
	public void insertarDatosOficinas(){
		
		Task<Void> task = new Task<Void>(){

			@Override
			protected Void call() throws Exception {
		        
				String mensaje =null;
				Connection conn = null;
				String url = "INSERT INTO OFICINA(NOMBRE_OFICINA, SIGLAS, ANEXO, RANGO_IP_INF, RANGO_IP_SUP, SISTEMA_USA) "
						+ "VALUES(?,?,?,?,?,?);";
				PreparedStatement pst=null;
				
				try {
					Conexion.conexionDB();
					conn=Conexion.getConexion();
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
						pst.setString(3,textFieldSiglas.getText().trim());
					}else{
						pst.setNull(3, java.sql.Types.VARCHAR);
					}
					
					if(!textFieldRangoIPInferior.getText().trim().isEmpty()){
						pst.setString(4,textFieldRangoIPInferior.getText().trim());
					}else{
						pst.setNull(4, java.sql.Types.VARCHAR);
					}
					
					if(!textFieldRangoIPSuperior.getText().trim().isEmpty()){
						pst.setString(5,textFieldRangoIPSuperior.getText().trim());
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
					
					pst.executeUpdate();
					mensaje="Insercion Correcta";
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
				return null;
			}
			
		};
		Thread hilo=new Thread(task);
    	hilo.setDaemon(true);
    	hilo.start();
	}
	
	public void inicializarComboBox(){
		checkComboBoxSistemasUso.getItems().addAll(observableListSistemasUso);
	}

}
