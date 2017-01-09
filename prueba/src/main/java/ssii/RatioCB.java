package ssii;

import org.apache.spark.sql.Row;

public class RatioCB {
	private Integer idItem1;
	private Integer idItemTentativo;
	private Integer indiceSimilitud;
	
	public RatioCB(Row _x, Integer index) {
		idItem1 =_x.getInt(0);
		idItemTentativo = _x.getInt(1);
		indiceSimilitud = index;
	}

	public Integer getIdItem1() {
		return idItem1;
	}

	public void setIdItem1(Integer idItem1) {
		this.idItem1 = idItem1;
	}

	public Integer getIdItemTentativo() {
		return idItemTentativo;
	}

	@Override
	public String toString() {
		return "RatioCB [idItem1=" + idItem1 + ", idItemTentativo=" + idItemTentativo + ", indiceSimilitud="
				+ indiceSimilitud + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idItem1 == null) ? 0 : idItem1.hashCode());
		result = prime * result + ((idItemTentativo == null) ? 0 : idItemTentativo.hashCode());
		result = prime * result + ((indiceSimilitud == null) ? 0 : indiceSimilitud.hashCode());
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
		RatioCB other = (RatioCB) obj;
		if (idItem1 == null) {
			if (other.idItem1 != null)
				return false;
		} else if (!idItem1.equals(other.idItem1))
			return false;
		if (idItemTentativo == null) {
			if (other.idItemTentativo != null)
				return false;
		} else if (!idItemTentativo.equals(other.idItemTentativo))
			return false;
		if (indiceSimilitud == null) {
			if (other.indiceSimilitud != null)
				return false;
		} else if (!indiceSimilitud.equals(other.indiceSimilitud))
			return false;
		return true;
	}

	public void setIdItemTentativo(Integer idItemTentativo) {
		this.idItemTentativo = idItemTentativo;
	}

	public Integer getIndiceSimilitud() {
		return indiceSimilitud;
	}

	public void setIndiceSimilitud(Integer indiceSimilitud) {
		this.indiceSimilitud = indiceSimilitud;
	}
	

	
}
