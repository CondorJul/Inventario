package equipos;

import javafx.scene.control.MenuItem;

public class MenuItemOficina extends MenuItem {
	private int id_oficina=-1;
	private String of_nombre=null;
	private String of_siglas=null;
	
	public int getId_oficina() {
		return id_oficina;
	}
	public void setId_oficina(int id_oficina) {
		this.id_oficina = id_oficina;
	}
	public String getOf_nombre() {
		return of_nombre;
	}
	public void setOf_nombre(String of_nombre) {
		this.of_nombre = of_nombre;
	}
	public String getOf_siglas() {
		return of_siglas;
	}
	public void setOf_siglas(String of_siglas) {
		this.of_siglas = of_siglas;
	}
	
	public MenuItemOficina(int id_oficina, String of_nombre, String of_siglas) {
		super(of_nombre +" ("+of_siglas+")");
		this.id_oficina = id_oficina;
		this.of_nombre = of_nombre;
		this.of_siglas = of_siglas;
	}
	
	
	
}
