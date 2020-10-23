package equipos.TipoEquipo;

public class TipoEquipo {

	
	private int idTipoEquipo=-1;
	private String nombreTipoEquipo=null;
	
	
	public TipoEquipo(int idTipoEquipo, String nombreTipoEquipo){
		this.idTipoEquipo=idTipoEquipo;
		this.nombreTipoEquipo=nombreTipoEquipo;
	
	}

	public int getIdTipoEquipo() {
		return idTipoEquipo;
	}

	public void setIdTipoEquipo(int idTipoEquipo) {
		this.idTipoEquipo = idTipoEquipo;
	}

	public String getNombreTipoEquipo() {
		return nombreTipoEquipo;
	}

	public void setNombreTipoEquipo(String nombreTipoEquipo) {
		this.nombreTipoEquipo = nombreTipoEquipo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idTipoEquipo;
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
		TipoEquipo other = (TipoEquipo) obj;
		if (idTipoEquipo != other.idTipoEquipo)
			return false;
		return true;
	}
	
	
	
	@Override
	public String toString(){
		return nombreTipoEquipo;
	}
	
	
}
