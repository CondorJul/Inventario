package perfil.CambiarContrasena;

import java.net.URL;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.mysql.jdbc.Connection;

import funciones.Funciones;
import funciones.Servidor;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import sesion.Sesion;

public class CambiarContrasenaControler extends Funciones implements Initializable {
	
	@FXML private JFXButton jfxButtonCerrar;
    @FXML private JFXButton jfxButtonValidar;
    @FXML private Label labelVerificacion;
    @FXML private PasswordField passwordFieldActualPass;
    @FXML private PasswordField passwordFieldNuevaPass;
    @FXML private PasswordField passwordFieldRepetirNuevaPass;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		jfxButtonCerrar.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				Node salir = (Node)arg0.getSource();
				Stage cerrar = (Stage)salir.getScene().getWindow();
				cerrar.close();
			}
		});
		
		jfxButtonValidar.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String mensaje="";
				if (passwordFieldActualPass.getText().trim().isEmpty() || passwordFieldNuevaPass.getText().trim().isEmpty() || passwordFieldRepetirNuevaPass.getText().trim().isEmpty()) {
					mensaje="Rellene los campos requeridos";
				}else{
					if (Sesion.CONTRASENA.equals(encriptar(passwordFieldActualPass.getText()))) {
						if (passwordFieldNuevaPass.getText().equals(passwordFieldRepetirNuevaPass.getText())) {
							actualizarDatosContrasena(encriptar(passwordFieldNuevaPass.getText()));
							Sesion.CONTRASENA=encriptar(passwordFieldNuevaPass.getText());
							return;
						}else{
							mensaje="Las contraseñas no coinciden";
							}
					}else{
						mensaje="La contraseña actual es incorrecta";
					}
				}
				mostrarInformacion(labelVerificacion, mensaje, Funciones.INCORRECTO);
			}
		});
	}
	
	public void actualizarDatosContrasena(String nuevaContrasenaEncriptada){

		String mensaje = null;
		Connection conn = null;
		String url = "UPDATE USUARIO SET CONTRASENNA=? WHERE DNI=?";
		PreparedStatement pst = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = (Connection) DriverManager.getConnection(Servidor.SERVIDOR, Servidor.USER, Servidor.PASS);
	        pst = conn.prepareStatement(url);
	        pst.setString(1, nuevaContrasenaEncriptada);
	        pst.setString(2, Sesion.DNI);
	        int rs = pst.executeUpdate();
	        if(rs==1){
	        	mensaje = "Contraseña cambiado con exito";
	        }
			mostrarInformacion(labelVerificacion, mensaje, Funciones.CORRECTO);

		} catch (Exception e) {
			mensaje=("Error  "+e.getMessage());
	        mostrarInformacion(labelVerificacion, mensaje, Funciones.INCORRECTO);
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
