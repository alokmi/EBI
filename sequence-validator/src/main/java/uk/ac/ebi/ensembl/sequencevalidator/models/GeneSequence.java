package uk.ac.ebi.ensembl.sequencevalidator.models;

public class GeneSequence {

	private String chromosomeName;
	private long startSegment;
	private long endSegment;

	public String getChromosomeName() {
		return chromosomeName;
	}

	public void setChromosomeName(String chromosomeName) {
		this.chromosomeName = chromosomeName;
	}

	public long getStartSegment() {
		return startSegment;
	}

	public void setStartSegment(long startSegment) {
		this.startSegment = startSegment;
	}

	public long getEndSegment() {
		return endSegment;
	}

	public void setEndSegment(long endSegment) {
		this.endSegment = endSegment;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chromosomeName == null) ? 0 : chromosomeName.hashCode());
		result = prime * result + (int) (endSegment ^ (endSegment >>> 32));
		result = prime * result + (int) (startSegment ^ (startSegment >>> 32));
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
		GeneSequence other = (GeneSequence) obj;
		if (chromosomeName == null) {
			if (other.chromosomeName != null)
				return false;
		} else if (!chromosomeName.equals(other.chromosomeName))
			return false;
		if (endSegment != other.endSegment)
			return false;
		if (startSegment != other.startSegment)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GeneSequence [chromosomeName=" + chromosomeName + ", startSegment=" + startSegment + ", endSegment="
				+ endSegment + "]";
	}

}
