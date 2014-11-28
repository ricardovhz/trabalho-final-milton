package br.com.vanhoz.ricardo.trabalho.milton.neural;

public class FuncaoSigmoide implements Funcao {

	@Override
	public double calcular(double valor) {
		return 1.0D/(1.0D + Math.exp(valor*(-1)));
	}

}
