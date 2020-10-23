package personal;

public class Personal {
	private int id_personal;
	private String per_dni;
	private String per_nombre_completo;
	private String per_usuario;
	private String per_cargo;
	private String per_sistema_usa;
	private String per_id_oficina;
	
	public int getId_personal() {
		return id_personal;
	}
	public void setId_personal(int id_personal) {
		this.id_personal = id_personal;
	}
	public String getPer_dni() {
		return per_dni;
	}
	public void setPer_dni(String per_dni) {
		this.per_dni = per_dni;
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
	public String getPer_cargo() {
		return per_cargo;
	}
	public void setPer_cargo(String per_cargo) {
		this.per_cargo = per_cargo;
	}
	public String getPer_sistema_usa() {
		return per_sistema_usa;
	}
	public void setPer_sistema_usa(String per_sistema_usa) {
		this.per_sistema_usa = per_sistema_usa;
	}
	public String getPer_id_oficina() {
		return per_id_oficina;
	}
	public void setPer_id_oficina(String per_id_oficina) {
		this.per_id_oficina = per_id_oficina;
	}
	public Personal(int id_personal, String per_dni, String per_nombre_completo, String per_usuario, String per_cargo,
			String per_sistema_usa, String per_id_oficina) {
		super();
		this.id_personal = id_personal;
		this.per_dni = per_dni;
		this.per_nombre_completo = per_nombre_completo;
		this.per_usuario = per_usuario;
		this.per_cargo = per_cargo;
		this.per_sistema_usa = per_sistema_usa;
		this.per_id_oficina = per_id_oficina;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id_personal;
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
		Personal other = (Personal) obj;
		if (id_personal != other.id_personal)
			return false;
		return true;
	}
	
	
}
