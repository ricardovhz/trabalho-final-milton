package br.com.vanhoz.ricardo.trabalho.milton.main;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import br.com.vanhoz.ricardo.trabalho.milton.data.PesosDAO;
import br.com.vanhoz.ricardo.trabalho.milton.neural.EntradaSimples;
import br.com.vanhoz.ricardo.trabalho.milton.neural.Linha;
import br.com.vanhoz.ricardo.trabalho.milton.neural.Rede;
import br.com.vanhoz.ricardo.trabalho.milton.rec.AudioRecorder;

public class RedeNeuralExecute {
	private double[][] pesos;
	private Rede rede;
	
	public RedeNeuralExecute() {
		pesos = PesosDAO.getInstance().getPesos();
		rede = new Rede(3, 31,10,4);
		rede.loadPesos(pesos);
	}
	
	public double getIdFromCapture() {
		try {
//			final String PATH="/tmp/audio.wav";
			AudioRecorder ar = new AudioRecorder();
//			ar.capture(2000);
//			ar.saveDataToWAV(PATH);
			ar.captureFromFile("ricardo-out.wav"); 
			
			SoundAnalyzer analyzer = new SoundAnalyzer(ar.getCapturedAudio());
			List<AnalysisResult> results = analyzer.getFirstBiggestResults(5);
			
			double[] ids = new double[5];
			
			for (int i=0;i<results.size();i++) {
				AnalysisResult result = results.get(i);
				System.out.println(result);
				double[] freq = Utils.getBits(result.getFrequency(), 11);
				double[] val = Utils.getBits(result.getValue(), 17);
				double[] pos = Utils.getBits(i, 3);
				
				for (int j=0;j<freq.length;j++) {
					rede.setValoresEntradas(j, new EntradaSimples(freq[j]));
				}

				for (int j=0;j<val.length;j++) {
					rede.setValoresEntradas(11+j, new EntradaSimples(val[j]));
				}

				for (int j=0;j<pos.length;j++) {
					rede.setValoresEntradas(28+j, new EntradaSimples(pos[j]));
				}
				
				Linha l = rede.getResult();
				
				double id = 0.0D;
				for (int j=0;j<l.getNeuronios().size();j++) {
					double d = l.getNeuronios().get(j).saida();
					System.out.println(d);
					id += (l.getNeuronios().size()-j) * new BigDecimal(d).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				}
				
				ids[i] = id;
			}
			
			double media = 0.0D;
			for (int i=0;i<ids.length;i++) {
				media += ids[i];
			}
			media /= ids.length;
			return media;
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("Um erro ocorreu");
	}
	
	public static void main(String[] args) {
		RedeNeuralExecute r = new RedeNeuralExecute();
		double id = r.getIdFromCapture();
		System.out.println("\n============\n"+id);
	}
}
