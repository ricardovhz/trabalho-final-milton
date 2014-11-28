package br.com.vanhoz.ricardo.trabalho.milton.main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jtransforms.fft.DoubleFFT_1D;

public class SoundAnalyzer {
	
	private double[] soundData;
	private double[] fft;

	public SoundAnalyzer(byte[] soundData) {
		this.soundData = truncateData(soundData);
		applyHanning(this.soundData);
	}
	
	public List<AnalysisResult> getFirstBiggestResults(int qtd) {
		double[] fft = getFFT();
		List<AnalysisResult> result = new ArrayList<AnalysisResult>(qtd);
		for (int i=0;i<qtd;i++) {
			result.add(getBiggestValuePos(fft, i+1));
		}
		return result;
	}
	
	private double[] getFFT() {
		if (fft == null) {
			double[] r = new double[soundData.length*2];
			System.arraycopy(soundData, 0, r, 0, soundData.length);

			DoubleFFT_1D fft = new DoubleFFT_1D(soundData.length);
			fft.realForwardFull(r);
			
			// passa alta
			for (int i=0;i<200;i++) {
				r[i] = 0.0D;
			}
			
			// passa baixa
			if (r.length >= 4000) {
				for (int i=4000;i<r.length;i++) {
					r[i] = 0.0D;
				}
			}

			this.fft = r;
		}
		return fft;
	}
	
	private AnalysisResult getBiggestValuePos(double[] fft, int index) {
		int pos=0;
		Set<Integer> biggestPos = new HashSet<Integer>();
		double max = 0.0;
		double incA=0.0;
		double incB=0.0;
		
		for (int j=0;j<index;j++) {
			max = 0.0;
			for (int i=0;i<fft.length;i += 2) {
				if (biggestPos.contains(i))
					continue;
				double abs = Math.sqrt(Math.pow(fft[i], 2) + Math.pow(fft[i+1], 2));
				if (abs > max) {
					boolean ignore = false;
					if (i == 0)
						continue;
					for (int poses : biggestPos) {
						if (Math.abs(poses - i) < 20) {
							ignore = true;
						}
					}
					if (ignore)
						continue;
					max = abs;
					pos = i;
				}
			}
			if (!biggestPos.contains(pos)) {
				biggestPos.add(pos);
			}
		}
		
		return new AnalysisResult(pos/2, max, incA, incB);
	}
	
	private double[] getHanningWindow(int N) {
		double[] r = new double[N];
		double arg = 2 * Math.PI / (N-1);
		for (int i=0;i<N;i++) {
			r[i] = 0.5D * (1.0D - Math.cos(arg*i));
		}
		return r;
	}

	private double[] truncateData(byte[] data) {
		int exp = (int)(Math.log10(data.length) / Math.log10(2));
		int N = (int) Math.pow(2, exp);
		double[] result = new double[N];
		for (int i=0;i<N;i++) {
			result[i] = data[i];
		}
		return result;
	}
	
	private void applyHanning(double[] data) {
		double[] han = getHanningWindow(data.length);
		for (int i=0;i<data.length;i++) {
			data[i] *= han[i];
		}
	}
	
//	private void applyBandPass(double[] data) {
//		double[] fft = getFFT();
//		double[] result = 
//		DoubleFFT_1D func = new DoubleFFT_1D(soundData.length);
//		func.realInverseFull(arg0, arg1);
//	}

}
