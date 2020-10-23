package oficinas;

public class Oficinas {
	
	private int id_oficina=-1;
	private String of_nombre;
	private String of_siglas;
	private String of_anexo;
	private String of_rango_ip_inf;
	private String of_rango_ip_sup;
	private String of_sistema_usa;
	
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
	public String getOf_anexo() {
		return of_anexo;
	}
	public void setOf_anexo(String of_anexo) {
		this.of_anexo = of_anexo;
	}
	public String getOf_rango_ip_inf() {
		return of_rango_ip_inf;
	}
	public void setOf_rango_ip_inf(String of_rango_ip_inf) {
		this.of_rango_ip_inf = of_rango_ip_inf;
	}
	public String getOf_rango_ip_sup() {
		return of_rango_ip_sup;
	}
	public void setOf_rango_ip_sup(String of_rango_ip_sup) {
		this.of_rango_ip_sup = of_rango_ip_sup;
	}
	public String getOf_sistema_usa() {
		return of_sistema_usa;
	}
	public void setOf_sistema_usa(String of_sistema_usa) {
		this.of_sistema_usa = of_sistema_usa;
	}
	public Oficinas(int id_oficina, String of_nombre, String of_siglas, String of_anexo, String of_rango_ip_inf,
			String of_rango_ip_sup, String of_sistema_usa) {
		super();
		this.id_oficina = id_oficina;
		this.of_nombre = of_nombre;
		this.of_siglas = of_siglas;
		this.of_anexo = of_anexo;
		this.of_rango_ip_inf = of_rango_ip_inf;
		this.of_rango_ip_sup = of_rango_ip_sup;
		this.of_sistema_usa = of_sistema_usa;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id_oficina;
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
		Oficinas other = (Oficinas) obj;
		if (id_oficina != other.id_oficina)
			return false;
		return true;
	}
	
	
	
	
	

}
