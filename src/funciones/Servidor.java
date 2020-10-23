package funciones;

public class Servidor {
	
	public static final String URL_INICIO ="jdbc:mysql://";
	public static final String HOST_NAME="localhost";
	public static final String BASE_DATOS="inventario_gorepa";
	public static final String UNICODE="?useUnicode=yes&characterEncoding=UTF-8";
	public static final String USER="gorepa";
	public static final String PASS="gorepa";
	public static final String SERVIDOR = URL_INICIO+HOST_NAME+"/"+BASE_DATOS + UNICODE;
}
