package usuarios;

import javafx.scene.image.Image;

public class Usuarios {
	private int user_id_usuario;
	private String user_dni;
	private String user_nombre;
	private String user_apellido;
	private String user_cargo;
	private Image user_foto;
	private int user_rol;
	private String user_rol_string;
	private int user_estado;
	private String user_nom_apell;
	
	
	public int getUser_id_usuario() {
		return user_id_usuario;
	}
	public void setUser_id_usuario(int user_id_usuario) {
		this.user_id_usuario = user_id_usuario;
	}
	public String getUser_dni() {
		return user_dni;
	}
	public void setUser_dni(String user_dni) {
		this.user_dni = user_dni;
	}
	public String getUser_nombre() {
		return user_nombre;
	}
	public void setUser_nombre(String user_nombre) {
		this.user_nombre = user_nombre;
	}
	public String getUser_apellido() {
		return user_apellido;
	}
	public void setUser_apellido(String user_apellido) {
		this.user_apellido = user_apellido;
	}
	public String getUser_cargo() {
		return user_cargo;
	}
	public void setUser_cargo(String user_cargo) {
		this.user_cargo = user_cargo;
	}
	public Image getUser_foto() {
		return user_foto;
	}
	public void setUser_foto(Image user_foto) {
		this.user_foto = user_foto;
	}
	public int getUser_rol() {
		return user_rol;
	}
	public void setUser_rol(int user_rol) {
		this.user_rol = user_rol;
	}
	public int getUser_estado() {
		return user_estado;
	}
	public void setUser_estado(int user_estado) {
		this.user_estado = user_estado;
	}
	
	public String getUser_nom_apell() {
		return user_nom_apell;
	}
	public void setUser_nom_apell(String user_nom_apell) {
		this.user_nom_apell = user_nom_apell;
	}
	public String getUser_rol_string() {
		return user_rol_string;
	}
	public void setUser_rol_string(String user_rol_string) {
		this.user_rol_string = user_rol_string;
	}
	
	public Usuarios(int user_id_usuario, String user_dni, String user_nombre, String user_apellido, String user_cargo,
			Image user_foto, int user_rol, int user_estado) {
		super();
		this.user_id_usuario = user_id_usuario;
		this.user_dni = user_dni;
		this.user_nombre = user_nombre;
		this.user_apellido = user_apellido;
		this.user_cargo = user_cargo;
		this.user_foto = user_foto;
		this.user_rol = user_rol;
		this.user_estado = user_estado;
		this.user_nom_apell=user_nombre+", "+user_apellido;
		this.user_rol_string=(user_rol==1)?"Administrador":"Usuario";
	
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + user_id_usuario;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuarios other = (Usuarios) obj;
		if (user_id_usuario != other.user_id_usuario)
			return false;
		return true;
	}	
}
