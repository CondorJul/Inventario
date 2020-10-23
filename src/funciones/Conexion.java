package funciones;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion extends Funciones{
	static Connection conn=null;
	public static void conexionDB(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Servidor.SERVIDOR, Servidor.USER, Servidor.PASS);
		} catch (Exception e) {
			
		} 
	}
	public static Connection getConexion() {
		return conn;
	}
}
