package br.com.vanhoz.ricardo.trabalho.milton.neural;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Rede {
	
	private static final Funcao DEFAULT_FUNCTION = new FuncaoSigmoide();
	private static final Funcao FUNCTION_NOTHING = new FuncaoNada();

	List<Linha> linhas;
	double[][] pesos;
	
	public Rede(int linhas, int... neuroniosLinha) {
		if (neuroniosLinha.length != linhas) {
			throw new RuntimeException("Linha != neuroniosLinha");
		}
		this.linhas = new ArrayList<Linha>();
		for (int i=0;i<linhas;i++) {
			Linha linha = new Linha(neuroniosLinha[i], (i==0 ? FUNCTION_NOTHING : DEFAULT_FUNCTION), (i == (linhas-1) ? false : true));
			if (i > 0) {
				linha.setAnt(this.linhas.get(i-1));
			}
			this.linhas.add(linha);
		}
	}
	
	public void setValoresEntradas(int neuronio, EntradaSimples entrada) {
		linhas.get(0).setEntradaToNeuronio(neuronio, entrada);
	}
	
	public void loadPesos(double[][] pesos) {
		this.pesos = pesos;
	}
	
	public int getNumeroNeuroniosSaida() {
		return linhas.get(linhas.size()-1).sizeWithouBias();
	}
	
	private Linha getResult(int index, int indexPesos) {
		if (index == 0) {
			return linhas.get(0);
		}
		
		List<Neuronio> ant = getResult(index-1, indexPesos-linhas.get(index).sizeWithouBias()).getNeuronios();
		List<Neuronio> cur = linhas.get(index).getNeuronios();
		
		for (int i=cur.size()-1;i>= 0;i--) {
			Neuronio n = cur.get(i);
			if (!n.isBias()) {
				for (int j=0;j<ant.size();j++) {
					Entrada e = n.getEntradaAt(j);
					if (e == null) {
						n.addEntrada(new EntradaComPeso(ant.get(j).saida(), pesos[indexPesos-i][j], ant.get(j)));
					} else {
						EntradaComPeso ecp = (EntradaComPeso) e;
//						ecp.setPeso(pesos[indexPesos-i][j]);
						ecp.setValor(ant.get(j).saida());
					}
				}
			}
		}
		
		return linhas.get(index);
	}
	
	public Linha getResult() {
		return getResult(linhas.size()-1, pesos.length-1);
	}
	
	public static void main(String[] args) {
		Rede rede = new Rede(3, 4,4,2);
		rede.loadPesos(new double[][] {{0.5257144971539268,0.49455196485546304,-6.312861400274365,-6.645955017744132,8.721520799357702},{-8.914999426088436,2.3629605125569713,-0.14621774467058127,0.08113521166786408,1.992886690771857},{-5.235736057133266,-1.915204440985507,-2.648872047653137,2.5278165592233446,-4.333508001802203},{2.068390572619184,-8.484061729674833,-0.6669373518241657,-0.748244388963299,2.2385724815041796},{-0.5061279353452893,-11.562420710952075,-0.4148439418491548,-12.5637381472033,6.219831290353038},{-12.320972759966404,-0.2434836697943823,-11.365030980044025,-0.28472225100570614,5.927388753480272}});
//		rede.loadPesos(new double[][] {{50,100,-30},{-50,-100,120},{50,100,-120}});
		
		double e1 = 0.0D;
		double e2 = 0.0D;
		double e3 = 0.0D;
		double e4 = 0.0D;
		
		Scanner sc = new Scanner(System.in);
		e1 = sc.nextDouble();
		e2 = sc.nextDouble();
		e3 = sc.nextDouble();
		e4 = sc.nextDouble();
		sc.close();
		
		rede.setValoresEntradas(0, new EntradaSimples(e1));
		rede.setValoresEntradas(1, new EntradaSimples(e2));
		rede.setValoresEntradas(2, new EntradaSimples(e3));
		rede.setValoresEntradas(3, new EntradaSimples(e4));
		
		Linha l = rede.getResult();
		double s1 = l.getNeuronios().get(0).saida();
		double s2 = l.getNeuronios().get(1).saida();
		BigDecimal saida1 = new BigDecimal(s1).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal saida2 = new BigDecimal(s2).setScale(2, BigDecimal.ROUND_HALF_UP);
		System.out.println(s1+" - "+saida1);
		System.out.println(s2+" - "+saida2);
	}

	public List<Linha> getLinhas() {
		return linhas;
	}
	
	public void updatePesos() {
		int i=0;
		for (Linha l : linhas) {
			for (Neuronio n : l.getNeuronios()) {
				boolean updated=false;
				int j=0;
				
				for (Entrada entrada : n.getEntradas()) {
					if (entrada instanceof EntradaComPeso) {
						updated=true;
						pesos[i][j] = ((EntradaComPeso) entrada).getPeso();
						j++;
					}
				}
				if (updated)
					i++;
			}
		}
	}
	
	public double[][] getPesos() {
		return this.pesos;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Linha l : linhas) {
			sb.append("[");
			for (Neuronio n : l.getNeuronios()) {
				sb.append(n.hashCode());
				sb.append("(");
				for (Entrada e : n.getEntradas()) {
					if (e instanceof EntradaComPeso) {
						EntradaComPeso ecp = (EntradaComPeso) e;
						sb.append(ecp.getNeuronio().hashCode());
						sb.append(",");
					}
				}
				sb.append("),");
			}
			sb.append("]\n");
		}
		return sb.toString();
	}
	
}
