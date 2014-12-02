package br.com.vanhoz.ricardo.trabalho.milton.main;

import java.util.Arrays;

import br.com.vanhoz.ricardo.trabalho.milton.data.BaseDAO;
import br.com.vanhoz.ricardo.trabalho.milton.data.BaseDAO.Registry;
import br.com.vanhoz.ricardo.trabalho.milton.data.PesosDAO;
import br.com.vanhoz.ricardo.trabalho.milton.neural.train.TreinadorRede;

public class TestTrainNet {
	
	public static void main(String[] args) {
		TreinadorRede.DEBUG=Boolean.TRUE;
		
		BaseDAO inst = BaseDAO.getInstance();
		
		System.out.println("Treinando rede...");
		double[][] entries = new double[inst.getRegistries().size()][35];
		int i=0;
		for (Registry r : inst.getRegistries()) {
			double[] ent = new double[35];
//			ent[0] = r.getFrequency()/10000.0D;
//			ent[1] = r.getValue()/100000.0D;
//			ent[2] = r.getPosition()/10.0D;
			System.arraycopy(Utils.getBits(r.getFrequency(), 11), 0, ent, 0, 11);
			System.arraycopy(Utils.getBits(r.getValue(), 17), 0, ent, 11, 17);
			System.arraycopy(Utils.getBits(r.getPosition(), 3), 0, ent, 28, 3);
			
			System.arraycopy(Utils.getBits(r.getId(), 4), 0, ent, 31, 4);
			
			entries[i] = ent;
			i++;
		}
		
		System.out.println("Obtidos "+entries.length+" entradas para treinamento");
		System.out.println(Arrays.toString(entries[0]));
		
		TreinadorRede rede = new TreinadorRede(3, 31,10,4);
		double[][] result = rede.train(entries, 1000);
		
		for (double[] d : result) {
			System.out.println("Resultado: "+Arrays.toString(d));
		}
		
		System.out.println("Salvando os pesos no arquivo...");
		
		salvarPesos(result);
		
	}
	
	private static void salvarPesos(double[][] result) {
		PesosDAO.getInstance().salvar(result);
	}
	
}
