package equipos;

import javafx.scene.control.MenuItem;

public class MenuItemPersonal extends MenuItem{
	
	private int id_personal = -1;
	private String per_nombre_completo = null;
	private String per_usuario = null;
	public int getId_personal() {
		return id_personal;
	}
	public void setId_personal(int id_personal) {
		this.id_personal = id_personal;
	}
	public String getPer_nombre_completo() {
		return per_nombre_completo;
	}
	public void setPer_nombre_completo(String per_nombre_completo) {
		this.per_nombre_completo = per_nombre_completo;
	}
	public String getPer_usuario() {
		return per_usuario;
	}
	public void setPer_usuario(String per_usuario) {
		this.per_usuario = per_usuario;
	}
	public MenuItemPersonal(int id_personal, String per_nombre_completo, String per_usuario) {
		super(per_nombre_completo+" ("+per_usuario+")");
		this.id_personal = id_personal;
		this.per_nombre_completo = per_nombre_completo;
		this.per_usuario = per_usuario;
	}
		
}
