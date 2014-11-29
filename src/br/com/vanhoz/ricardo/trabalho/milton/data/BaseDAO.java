package br.com.vanhoz.ricardo.trabalho.milton.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public class BaseDAO {

	private static BaseDAO INSTANCE;

	private final File f = new File("base.txt");

	List<Registry> registry = null;

	public synchronized static BaseDAO getInstance() {
		if (INSTANCE == null)
			INSTANCE = new BaseDAO();
		return INSTANCE;
	}

	private BaseDAO() {
		init();
	}

	private void init() {
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {

			registry = new ArrayList<BaseDAO.Registry>();

			String line = null;
			while ((line = br.readLine()) != null) {
				if (!line.trim().isEmpty()) {
					StringTokenizer st = new StringTokenizer(line, ",");
					double freq = new Double(st.nextToken());
					double val = new Double(st.nextToken());
					int pos = new Integer(st.nextToken());
					int id = new Integer(st.nextToken());
					registry.add(new Registry(freq, val, pos, id));
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Registry> getRegistries() {
		return Collections.unmodifiableList(registry);
	}

	public void addRegistry(double frequency, double value, int pos, int id) {
		synchronized (registry) {
			registry.add(new Registry(frequency, value, pos, id));
			save();
		}
	}

	private synchronized void save() {
		f.delete();
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
			for (Registry r : registry) {
				int freq = (int) r.getFrequency();
				int value = (int) r.getValue();
				int pos = r.getPosition();
				int id = r.getId();
				bw.write(freq + "," + value + "," + pos + "," + id);
				bw.newLine();
			}
			bw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static class Registry {
		private double frequency;
		private double value;
		private int position;
		private int id;

		public Registry() {
			// TODO Auto-generated constructor stub
		}

		public Registry(double frequency, double value, int position, int id) {
			super();
			this.frequency = frequency;
			this.value = value;
			this.position = position;
			this.id = id;
		}

		public double getFrequency() {
			return frequency;
		}

		public void setFrequency(double frequency) {
			this.frequency = frequency;
		}

		public double getValue() {
			return value;
		}

		public void setValue(double value) {
			this.value = value;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getPosition() {
			return position;
		}

		public void setPosition(int position) {
			this.position = position;
		}

	}
        
        public void update() {
            init();
        }

}
