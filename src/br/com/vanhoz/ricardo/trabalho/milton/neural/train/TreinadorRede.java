package br.com.vanhoz.ricardo.trabalho.milton.neural.train;

import br.com.vanhoz.ricardo.trabalho.milton.neural.Entrada;
import br.com.vanhoz.ricardo.trabalho.milton.neural.EntradaComPeso;
import br.com.vanhoz.ricardo.trabalho.milton.neural.EntradaSimples;
import br.com.vanhoz.ricardo.trabalho.milton.neural.Linha;
import br.com.vanhoz.ricardo.trabalho.milton.neural.Neuronio;
import br.com.vanhoz.ricardo.trabalho.milton.neural.Rede;

public class TreinadorRede {

	public static Boolean DEBUG=Boolean.FALSE;
	private static final double ALFA = 0.5D;
	
	private Rede rede;
	private double[][] pesos;
	
	public TreinadorRede(int linhas, double[][] pesos, int... neuroniosLinha) {
		this.rede = new Rede(linhas, neuroniosLinha);
		this.pesos = pesos;
		randomize();
		rede.loadPesos(this.pesos);
	}
	
	public double[][] train(double[][] e) {
		return train(e, 1000);
	}
	
	public double[][] train(double[][] e, int iteracoes) {
		for (int i=0;i<iteracoes;i++) {
			double err = 0.0D;
			Double backPropTime=0.0D;
			
			TicTac tc = new TicTac();
			tc.start();
			for (int j=0;j<1000;j++) {
				int cs=(int)(Math.random() * 1000.0D) % e.length;
				err += train(e[cs], backPropTime);
//				rede.updatePesos();
				pesos = rede.getPesos();
			}
			tc.stop();
			if (DEBUG) {
				System.out.println("Tempo decorrido na iteração de 1000 processos: "+tc.getTime()+"ms");
				System.out.println("Tempo decorrido na iteração de 1000 backpropagation: "+backPropTime+"ms");
			}
			System.out.println(err);
		}
		rede.updatePesos();
		return rede.getPesos();
	}
	
	private double train(double[] e, Double backPropTime) {
		int numeroNeuronioSaida = rede.getNumeroNeuroniosSaida();
		double[] en = new double[e.length-numeroNeuronioSaida];
		double[] t = new double[numeroNeuronioSaida];
		System.arraycopy(e, 0, en, 0, e.length - numeroNeuronioSaida);
		System.arraycopy(e, e.length-numeroNeuronioSaida, t, 0, numeroNeuronioSaida);
		for (int i=0;i<en.length;i++) {
			rede.setValoresEntradas(i, new EntradaSimples(en[i]));
		}
		Linha result = rede.getResult();
		
		double err = 0.0D;
		for (int i=0;i<result.sizeWithouBias();i++) {
			double saida = result.getNeuronios().get(i).saida();
			err += (t[i] - saida) * (t[i] - saida);
		}
		backPropagation(t, result, backPropTime);
		return err;
	}
	
	private void backPropagation(double[] t, Linha linha, Double time) {
		Linha cur = linha, ant=null;
		boolean isSaida = true;
		double[] dAnt = null;
		
		TicTac tc = new TicTac();
		tc.start();
		while (cur != null) {
			double[] ds = new double[cur.sizeWithouBias()];
			for (int i=0;i<cur.getNeuronios().size();i++) {
				Neuronio n = cur.getNeuronios().get(i);
				double d,saida;
				
				if (n.isBias())
					continue;
				
				saida = n.saida();
				if (isSaida) {
					d = (t[i] - saida) * saida * (1-saida);
				} else {
					d = saida * (1-saida) * calcularD(n, ant, dAnt);
				}
				ds[i] = d;
				update(d, n);
			}
			tc.stop();
			
			time = time + tc.getTime();
			
			dAnt = ds;
			ant = cur;
			cur = cur.getAnt();
			isSaida=false;
		}
	}
	
	private double calcularD(Neuronio n, Linha ant, double[] dAnt) {
		double d = 0.0D;
		int i=0;
		for (Neuronio ne : ant.getNeuronios()) {
			for (Entrada e : ne.getEntradas()) {
				if (e instanceof EntradaComPeso) {
					EntradaComPeso ecp = (EntradaComPeso) e;
					if (ecp.getNeuronio() == n) {
						d += ecp.getPeso() * dAnt[i];
					}
				}
			}
			i++;
		}
		return d;
	}
	
	private void update(double d, Neuronio neuronio) {
		for (Entrada e : neuronio.getEntradas()) {
			if (e instanceof EntradaComPeso) {
				EntradaComPeso ecp = (EntradaComPeso) e;
				double peso = ecp.getPeso();
				double saidaAnt = ecp.getNeuronio().saida();
				double nw = peso + (ALFA*d*saidaAnt) + ALFA*ecp.getVariacao();
				ecp.setVariacao(nw - peso);
				ecp.setPeso(nw);
			}
		}
	}
	
	private void randomize() {
		for (int i=0;i<pesos.length;i++) {
			for (int j=0;j<pesos[i].length;j++) {
				pesos[i][j] = getRandomValue();
			}
		}
	}
	
	private double getRandomValue() {
		return Math.random()*10 - 5D;
	}

	/*
	public static void main(String[] args) {
		TreinadorRede t = new TreinadorRede(3, new double[][] {{0,0,0,0,0},{0,0,0,0,0},{0,0,0,0,0},{0,0,0,0,0},{0,0,0,0,0},{0,0,0,0,0}}, 4,4,2);
		double[][] r = t.train(new double[][] {{0,0,0,0,0,0},{0,0,0,1,0,0},{0,0,1,0,0,0},{0,0,1,1,0,1},{0,1,0,0,0,0},{0,1,0,1,0,0},{0,1,1,0,0,0},{0,1,1,1,0,1},{1,0,0,0,0,0},{1,0,0,1,0,0},{1,0,1,0,0,0},{1,0,1,1,0,1},{1,1,0,0,1,0},{1,1,0,1,1,0},{1,1,1,0,1,0},{1,1,1,1,1,1}});
		StringBuilder sb = new StringBuilder("{");
		for (int i=0;i<r.length;i++) {
			if (i>0)
				sb.append(",");
			sb.append("{");
			for (int j=0;j<r[i].length;j++) {
				if (j>0)
					sb.append(",");
				sb.append(r[i][j]);
			}
			sb.append("}");
		}
		sb.append("}");
		System.out.println(sb.toString());
	}
	*/
	
	public static class TicTac {
		private Long t1;
		private Long t2;
		private Long dec;
		
		public TicTac() {
		}
		
		public void start() {
			t1 = System.currentTimeMillis();
		}
		
		public void stop() {
			t2 = System.currentTimeMillis();
			dec = (t2-t1);
		}
		
		public double getTime() {
			return dec;
		}
		
	}
	
}
