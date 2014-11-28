package br.com.vanhoz.ricardo.trabalho.milton.neural;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Linha {
	private List<Neuronio> neuronios;
	private boolean bias;
	private Linha ant;

	public Linha(int neuronios, Funcao funcao) {
		this(neuronios, funcao, false);
	}

	public Linha(int neuronios, Funcao funcao, boolean bias) {
		this.neuronios = new ArrayList<Neuronio>();
		for (int i = 0; i < neuronios; i++) {
			this.neuronios.add(new Neuronio(funcao));
		}

		if (bias) {
			Neuronio neuronioBias = new Neuronio(new FuncaoNada());
			neuronioBias.setBias(true);
			this.neuronios.add(neuronioBias);
		}
	}

	public Linha(List<Neuronio> neuronios) {
		super();
		this.neuronios = neuronios;
	}

	public List<Neuronio> getNeuronios() {
		return neuronios;
	}

	public boolean isBias() {
		return bias;
	}

	public void setBias(boolean bias) {
		this.bias = bias;
	}
	
	public void setEntradaToNeuronio(int neuronioIndex, Entrada entrada) {
		Neuronio n = neuronios.get(neuronioIndex);
		Entrada e = n.getEntradaAt(0);
		if (e != null) {
			n.getEntradas().set(0, entrada);
		} else {
			addEntradaToNeuronio(neuronioIndex, entrada);
		}
	}

	public void addEntradaToNeuronio(int neuronioIndex, Entrada entrada) {
		if (neuronioIndex >= neuronios.size()) {
			throw new RuntimeException("Neuronio index > size");
		}
		neuronios.get(neuronioIndex).addEntrada(entrada);
	}

	public int sizeWithouBias() {
		int count = 0;
		for (Neuronio n : neuronios) {
			if (!n.isBias()) {
				count++;
			}
		}
		return count;
	}

	@Override
	public String toString() {
		return "Linha [neuronios=" + neuronios + ", bias=" + bias + "]";
	}

	public String getStringValue() {
		StringBuilder sb = new StringBuilder();
		for (Neuronio n : neuronios) {
			BigDecimal bd = new BigDecimal(n.saida()).setScale(0,
					BigDecimal.ROUND_UP);
			sb.append(bd.intValue());
		}
		return sb.toString();
	}

	public double[] getDoubleValues() {
		double[] result = new double[neuronios.size()];
		for (int i = 0; i < neuronios.size(); i++) {
			result[i] = neuronios.get(i).saida();
		}
		return result;
	}

	public Linha getAnt() {
		return ant;
	}

	public void setAnt(Linha ant) {
		this.ant = ant;
	}

}
