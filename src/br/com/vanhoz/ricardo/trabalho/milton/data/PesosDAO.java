package br.com.vanhoz.ricardo.trabalho.milton.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PesosDAO {
	private static final String FILENAME = "pesos.txt";

	private static PesosDAO INSTANCE;

	private double[][] pesos;

	public static synchronized PesosDAO getInstance() {
		try {
			if (INSTANCE == null)
				INSTANCE = new PesosDAO();
			return INSTANCE;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private PesosDAO() throws IOException {
		init();
	}

	private void init() throws IOException {
		File f = new File(FILENAME);
		int maxSizeArray = -1;
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line = null;
		List<Double[]> doubles = new ArrayList<Double[]>();
		while ((line = br.readLine()) != null) {
			StringTokenizer st = new StringTokenizer(line, ",");
			int j = 0;

			if (maxSizeArray < st.countTokens())
				maxSizeArray = st.countTokens();

			Double[] ent = new Double[st.countTokens()];
			while (st.hasMoreElements()) {
				String token = st.nextToken();
				double valor = Double.parseDouble(token);
				ent[j] = new Double(valor);
				j++;
			}
			doubles.add(ent);
		}
		br.close();

		pesos = new double[doubles.size()][maxSizeArray];
		for (int i = 0; i < doubles.size(); i++) {
			Double[] p = doubles.get(i);
			for (int j = 0; j < p.length; j++) {
				pesos[i][j] = p[j].doubleValue();
			}
		}
	}

	public double[][] getPesos() {
		return Arrays.copyOf(pesos, pesos.length);
	}
	
	public void salvar(double[][] result) {
		StringBuilder sb = new StringBuilder();
		for (int i=0;i<result.length;i++) {
			for (int j=0;j<result[i].length;j++) {
				if (j > 0)
					sb.append(",");
				
				sb.append(result[i][j]);
			}
			sb.append("\n");
		}
		
		try {
			File f = new File("pesos.txt");
			if (f.exists()) {
				f.delete();
			}
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			bw.write(sb.toString());
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
        
        public void update() {
            try {
                init();
            } catch (IOException ex) {
                Logger.getLogger(PesosDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

}
