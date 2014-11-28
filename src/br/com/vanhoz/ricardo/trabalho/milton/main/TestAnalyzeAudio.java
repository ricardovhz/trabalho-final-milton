package br.com.vanhoz.ricardo.trabalho.milton.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import br.com.vanhoz.ricardo.trabalho.milton.data.BaseDAO;
import br.com.vanhoz.ricardo.trabalho.milton.data.BaseDAO.Registry;
import br.com.vanhoz.ricardo.trabalho.milton.rec.AudioRecorder;

public class TestAnalyzeAudio {
	public static void main(String[] args) {
		try {
			int id;
			String nome;
			
			System.out.println("Gravador e analisador de voz\n====================\n\n");
			Scanner sc = new Scanner(System.in);
			System.out.print("Digite o id da pessoa: ");
			id = sc.nextInt();
			System.out.print("Digite o nome da pessoa: ");
			nome = sc.next();
			sc.close();

			ArrayList<BaseDAO.Registry> registries = new ArrayList<BaseDAO.Registry>();
			
			registries.addAll(capture(id));
			
			System.out.println("Gravando dados de ["+nome+"] no arquivo...");
			BaseDAO inst = BaseDAO.getInstance();
			for (BaseDAO.Registry r : registries) {
				inst.addRegistry(r.getFrequency(), r.getValue(), r.getPosition(), r.getId());
			}
			addId(id, nome);
			System.out.println("Feito!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void addId(int id, String nome) throws IOException {
		File f = new File("id.txt");
		if (!f.exists()) {
			f.createNewFile();
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		bw.append(id+","+nome);
		bw.newLine();
		bw.flush();
		bw.close();
	}
	
	private static List<BaseDAO.Registry> capture(int id) throws IOException {
		try {
			AudioRecorder rec = new AudioRecorder();
			rec.captureFromFile("hi.wav");
			
			System.out.println("Gravando (pode começar a falar...)");
			ArrayList<BaseDAO.Registry> registries = new ArrayList<BaseDAO.Registry>();
			
			for (int j=0;j<5;j++) {
				System.out.println("Tentativa "+j);
//				rec.capture(2000);
//				rec.saveDataToWAV("/tmp/ad.wav");
				
				byte[] audio = rec.getCapturedAudio();
				SoundAnalyzer sa = new SoundAnalyzer(audio);
				List<AnalysisResult> result = sa.getFirstBiggestResults(5);
	
				int i=0;
				for (AnalysisResult ar : result) {
					if (j==0) {
						registries.add(new BaseDAO.Registry(ar.getFrequency(), ar.getValue(), i, id));
					} else {
						Registry rg = registries.get(j);
						rg.setFrequency(rg.getFrequency()+ar.getFrequency());
						rg.setValue(rg.getValue()+ar.getValue());
					}
					i++;
				}
			}
			
			for (BaseDAO.Registry ar : registries) {
				ar.setFrequency(ar.getFrequency()/registries.size());
				ar.setValue(ar.getValue()/registries.size());
			}
			return registries;
		} finally {
			
		}
	}
	
}
