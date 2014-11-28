package br.com.vanhoz.ricardo.trabalho.milton.main;

public class Utils {

	public static double[] getBits(double a, int size) {
		int trunc = (int) a;
		String truncd = Integer.toBinaryString(trunc);
		String result = String.format("%0"+size+"d", 0)+truncd;
		result = result.substring(result.length()-size);
		double[] r = new double[result.length()];
		for (int i=0;i<result.length();i++) {
			r[i] = Integer.parseInt(String.valueOf(result.charAt(i)));
		}
		return r;
	}

}
