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
	
	public int getIdFromMicrofone() throws IOException {
		final String PATH="/tmp/audio.wav";
		AudioRecorder ar = new AudioRecorder();
		ar.capture(2000);
		ar.saveDataToWAV(PATH);
		return getIdFromCapture(ar.getCapturedAudio());
	}

	public int getIdFromCaptureFile(String file) throws IOException {
		AudioRecorder ar = new AudioRecorder();
		ar.captureFromFile(file);
		return getIdFromCapture(ar.getCapturedAudio());
	}

	private int getIdFromCapture(byte[] data) {
		SoundAnalyzer analyzer = new SoundAnalyzer(data);
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
			for (int j=l.getNeuronios().size()-1;j>=0;j--) {
				double d = l.getNeuronios().get(j).saida();
				System.out.println(d);
//					System.out.println(Math.pow(2, j));
//					System.out.println(new BigDecimal(d).setScale(1, BigDecimal.ROUND_HALF_DOWN));
				id += Math.pow(2, j) * (new BigDecimal(d).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue());
//					id += Math.pow(2, j) * d;
			}
			
			ids[i] = id;
		}
		
		double media = 0.0D;
		for (int i=0;i<ids.length;i++) {
			media += ids[i];
		}
		media /= ids.length;
		return (int)media;
	}
	
	public static void main(String[] args) {
		try {
			RedeNeuralExecute r = new RedeNeuralExecute();
			int id = r.getIdFromCaptureFile("hi.wav");
			System.out.println("\n============\n"+id);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
