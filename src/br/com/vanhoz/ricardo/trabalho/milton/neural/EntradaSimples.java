package br.com.vanhoz.ricardo.trabalho.milton.neural;

public class EntradaSimples implements Entrada {

	protected double valor;

	public EntradaSimples(double valor) {
		super();
		this.valor = valor;
	}

	@Override
	public double getValorReal() {
		return valor;
	}

	@Override
	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	@Override
	public String toString() {
		return "EntradaSimples [valor=" + valor + "]";
	}

}
