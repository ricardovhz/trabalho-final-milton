package br.com.vanhoz.ricardo.trabalho.milton.neural;


public class EntradaComPeso extends EntradaSimples {
	double peso;
	double variacao;
	Neuronio neuronio;

	public EntradaComPeso(double valor, double peso, Neuronio neuronio) {
		super(valor);
		this.peso = peso;
		this.neuronio = neuronio;
	}

	@Override
	public double getValorReal() {
		return valor * peso;
	}

	public Neuronio getNeuronio() {
		return neuronio;
	}

	public double getPeso() {
		return peso;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}

	public double getVariacao() {
		return variacao;
	}

	public void setVariacao(double variacao) {
		this.variacao = variacao;
	}

	@Override
	public String toString() {
		return "EntradaComPeso [peso=" + peso + ", neuronio=" + neuronio
				+ ", valor=" + valor + "]";
	}

}
