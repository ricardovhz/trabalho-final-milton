package br.com.vanhoz.ricardo.trabalho.milton.neural;

import java.util.ArrayList;
import java.util.List;

public class Neuronio {

	List<Entrada> entradas;
	Funcao funcao;
	boolean bias;

	public Neuronio(Funcao funcao) {
		this.entradas = new ArrayList<Entrada>();
		this.funcao = funcao;
	}

	public Neuronio(List<Entrada> entradas, Funcao funcao) {
		this(funcao);
		this.entradas = entradas;
	}

	public boolean isBias() {
		return bias;
	}

	public void setBias(boolean bias) {
		this.bias = bias;
	}

	public double saida() {
		if (bias)
			return 1.0D;
		else
			return funcao.calcular(sum());
	}

	private double sum() {
		return entradas.parallelStream().map(Entrada::getValorReal)
				.reduce((t, u) -> (t + u)).get();
	}

	public List<Entrada> getEntradas() {
		return entradas;
	}

	public void addEntrada(Entrada entrada) {
		this.entradas.add(entrada);
	}

	public Funcao getFuncao() {
		return funcao;
	}

	public void setFuncao(Funcao funcao) {
		this.funcao = funcao;
	}

	@Override
	public String toString() {
		return "Neuronio [entradas=" + entradas + ", funcao=" + funcao
				+ ", bias=" + bias + "]";
	}
	
	public void removeEntradas() {
		this.entradas.clear();
	}
	
	public Entrada getEntradaAt(int index) {
		if (index >= 0 && index < entradas.size()) {
			return entradas.get(index);
		}
		return null;
	}

}
