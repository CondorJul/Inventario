package equipos;

public class Equipos {
	
	private int id_equipo;
	private int id_oficina;
	private int id_personal;
	private String per_apellYNombres;
	private String per_usuario;
	private String  per_cargo;
	private String of_nombre;
	private String of_siglas;
	private String of_anexo;
	private String eq_tipoEquipo;
	private String eq_nombre;
	private String eq_marca;
	private String eq_modelo;
	private String eq_serie;
	private String eq_color;
	private String eq_sistemaOperativo;
	private String eq_procesador;
	private String eq_memoria;
	private String eq_disco;
	private String eq_direccinMAC;
	private String eq_direccionIP;
	private String eq_tipoCase;
	private String eq_arquitectura;
	private String eq_pertenece;
	private String eq_tipoConexion;
	private String eq_estado;
	private String eq_condicion;
		
	public int getId_equipo() {
		return id_equipo;
	}
	public void setId_equipo(int id_equipo) {
		this.id_equipo = id_equipo;
	}
	public int getId_oficina() {
		return id_oficina;
	}
	public void setId_oficina(int id_oficina) {
		this.id_oficina = id_oficina;
	}
	public int getId_personal() {
		return id_personal;
	}
	public void setId_personal(int id_personal) {
		this.id_personal = id_personal;
	}
	public String getPer_apellYNombres() {
		return per_apellYNombres;
	}
	public void setPer_apellYNombres(String per_apellYNombres) {
		this.per_apellYNombres = per_apellYNombres;
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
	public String getEq_tipoEquipo() {
		return eq_tipoEquipo;
	}
	public void setEq_tipoEquipo(String eq_tipoEquipo) {
		this.eq_tipoEquipo = eq_tipoEquipo;
	}
	public String getEq_nombre() {
		return eq_nombre;
	}
	public void setEq_nombre(String eq_nombre) {
		this.eq_nombre = eq_nombre;
	}
	public String getEq_marca() {
		return eq_marca;
	}
	public void setEq_marca(String eq_marca) {
		this.eq_marca = eq_marca;
	}
	public String getEq_modelo() {
		return eq_modelo;
	}
	public void setEq_modelo(String eq_modelo) {
		this.eq_modelo = eq_modelo;
	}
	public String getEq_serie() {
		return eq_serie;
	}
	public void setEq_serie(String eq_serie) {
		this.eq_serie = eq_serie;
	}
	public String getEq_color() {
		return eq_color;
	}
	public void setEq_color(String eq_color) {
		this.eq_color = eq_color;
	}
	public String getEq_sistemaOperativo() {
		return eq_sistemaOperativo;
	}
	public void setEq_sistemaOperativo(String eq_sistemaOperativo) {
		this.eq_sistemaOperativo = eq_sistemaOperativo;
	}
	public String getEq_procesador() {
		return eq_procesador;
	}
	public void setEq_procesador(String eq_procesador) {
		this.eq_procesador = eq_procesador;
	}
	public String getEq_memoria() {
		return eq_memoria;
	}
	public void setEq_memoria(String eq_memoria) {
		this.eq_memoria = eq_memoria;
	}
	public String getEq_disco() {
		return eq_disco;
	}
	public void setEq_disco(String eq_disco) {
		this.eq_disco = eq_disco;
	}
	public String getEq_direccinMAC() {
		return eq_direccinMAC;
	}
	public void setEq_direccinMAC(String eq_direccinMAC) {
		this.eq_direccinMAC = eq_direccinMAC;
	}
	public String getEq_direccionIP() {
		return eq_direccionIP;
	}
	public void setEq_direccionIP(String eq_direccionIP) {
		this.eq_direccionIP = eq_direccionIP;
	}
	public String getEq_pertenece() {
		return eq_pertenece;
	}
	public void setEq_pertenece(String eq_pertenece) {
		this.eq_pertenece = eq_pertenece;
	}
	public String getEq_tipoConexion() {
		return eq_tipoConexion;
	}
	public void setEq_tipoConexion(String eq_tipoConexion) {
		this.eq_tipoConexion = eq_tipoConexion;
	}
	public String getEq_estado() {
		return eq_estado;
	}
	public void setEq_estado(String eq_estado) {
		this.eq_estado = eq_estado;
	}
	public String getEq_condicion() {
		return eq_condicion;
	}
	public void setEq_condicion(String eq_condicion) {
		this.eq_condicion = eq_condicion;
	}
	public String getEq_arquitectura() {
		return eq_arquitectura;
	}
	public void setEq_arquitectura(String eq_arquitectura) {
		this.eq_arquitectura = eq_arquitectura;
	}	
	public String getEq_tipoCase() {
		return eq_tipoCase;
	}
	public void setEq_tipoCase(String eq_tipoCase) {
		this.eq_tipoCase = eq_tipoCase;
	}
	public Equipos(int id_equipo, int id_oficina, int id_personal, String per_apellYNombres, String per_usuario,
			String per_cargo, String of_nombre, String of_siglas, String of_anexo, String eq_tipoEquipo,
			String eq_nombre, String eq_marca, String eq_modelo, String eq_serie, String eq_color,
			String eq_sistemaOperativo, String eq_procesador, String eq_memoria, String eq_disco, String eq_direccinMAC,
			String eq_direccionIP, String eq_tipoCase, String eq_arquitectura, String eq_pertenece, String eq_tipoConexion, String eq_estado, String eq_condicion
			) {
		super();
		this.id_equipo = id_equipo;
		this.id_oficina = id_oficina;
		this.id_personal = id_personal;
		this.per_apellYNombres = per_apellYNombres;
		this.per_usuario = per_usuario;
		this.per_cargo = per_cargo;
		this.of_nombre = of_nombre;
		this.of_siglas = of_siglas;
		this.of_anexo = of_anexo;
		this.eq_tipoEquipo = eq_tipoEquipo;
		this.eq_nombre = eq_nombre;
		this.eq_marca = eq_marca;
		this.eq_modelo = eq_modelo;
		this.eq_serie = eq_serie;
		this.eq_color = eq_color;
		this.eq_sistemaOperativo = eq_sistemaOperativo;
		this.eq_procesador = eq_procesador;
		this.eq_memoria = eq_memoria;
		this.eq_disco = eq_disco;
		this.eq_direccinMAC = eq_direccinMAC;
		this.eq_direccionIP = eq_direccionIP;
		this.eq_tipoCase = eq_tipoCase;
		this.eq_arquitectura = eq_arquitectura;
		this.eq_pertenece = eq_pertenece;
		this.eq_tipoConexion = eq_tipoConexion;
		this.eq_estado = eq_estado;
		this.eq_condicion = eq_condicion;
		
	}
	
	public Equipos(int id_equipo) {
		super();
		this.id_equipo=id_equipo;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id_equipo;
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
		Equipos other = (Equipos) obj;
		if (id_equipo != other.id_equipo)
			return false;
		return true;
	}
	
	

	
	
	
	
	
	
	
}
