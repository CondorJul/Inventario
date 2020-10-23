package equipos.TipoEquipo;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import funciones.Conexion;
import funciones.Funciones;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TipoEquipoControler extends Funciones implements Initializable{
	
	public static TipoEquipo TIPO_EQUIPO_ULTIMA_INSERCION=null;
	
	
	@FXML private TextField textFieldNombreTipoEquipo;
    @FXML private JFXButton jfxButtonCerrar;
    @FXML private JFXButton jfxButtonValidar;
    @FXML private Label labelVerificacion;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		jfxButtonCerrar.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				Node node = (Node)arg0.getSource();
				Stage cerrar = (Stage)node.getScene().getWindow();
				cerrar.close();
			}
		});
		
		jfxButtonValidar.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				InsertarDatos();
			}
		});
	}
	
	public void InsertarDatos(){
		String mensaje =null;
		Connection conn = null;
		String url = "INSERT INTO TIPO_EQUIPO(NOMBRE_TIPO) VALUES(?);";
		PreparedStatement pst=null;
		ResultSet reS=null;
		try {
			Conexion.conexionDB();
			conn=Conexion.getConexion();
			pst=conn.prepareStatement(url, PreparedStatement.RETURN_GENERATED_KEYS);
			pst.setString(1, textFieldNombreTipoEquipo.getText().trim());
			pst.executeUpdate();
			reS=pst.getGeneratedKeys();
			
			if(reS.next()){
				TIPO_EQUIPO_ULTIMA_INSERCION=new TipoEquipo(reS.getInt(1),textFieldNombreTipoEquipo.getText().trim());
			}
			mensaje="Insercion Correcta";
            mostrarInformacion(labelVerificacion, mensaje, Funciones.CORRECTO);                  
		}catch (Exception e) {
			mensaje=("Error  "+e.getMessage());
			mostrarInformacion(labelVerificacion, mensaje, Funciones.INCORRECTO);
			e.printStackTrace();
		}finally{
			try{
				
				if(reS!=null){
					reS.close();
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
}
