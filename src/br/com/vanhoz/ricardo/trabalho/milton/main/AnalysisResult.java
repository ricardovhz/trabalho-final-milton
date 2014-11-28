package br.com.vanhoz.ricardo.trabalho.milton.main;

public class AnalysisResult {

	private double frequency;
	private double value;
	private double incA;
	private double incB;

	public AnalysisResult(double frequency, double value, double incA,
			double incB) {
		super();
		this.frequency = frequency;
		this.value = value;
		this.incA = incA;
		this.incB = incB;
	}

	public double getFrequency() {
		return frequency;
	}

	public void setFrequency(double frequency) {
		this.frequency = frequency;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double getIncA() {
		return incA;
	}

	public void setIncA(double incA) {
		this.incA = incA;
	}

	public double getIncB() {
		return incB;
	}

	public void setIncB(double incB) {
		this.incB = incB;
	}

	@Override
	public String toString() {
		return "AnalysisResult [frequency=" + frequency + ", value=" + value
				+ ", incA=" + incA + ", incB=" + incB + "]";
	}

}
