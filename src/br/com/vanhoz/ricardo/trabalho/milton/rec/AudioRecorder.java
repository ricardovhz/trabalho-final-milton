package br.com.vanhoz.ricardo.trabalho.milton.rec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioRecorder {
	
	private byte[] capturedAudio;
	private byte[] convertedAudio;
	private final AudioFormat format = new AudioFormat(8000, 8, 1, true, false);
	
	public byte[] getCapturedAudio() {
		return Arrays.copyOf(capturedAudio, capturedAudio.length);
	}

	public byte[] getConvertedAudio() {
		return Arrays.copyOf(convertedAudio, convertedAudio.length);
	}

	public void capture(int timeMilis) {
		Info info = new DataLine.Info(TargetDataLine.class, format);
		if (!AudioSystem.isLineSupported(info)) {
			throw new RuntimeException("Microfone não suportado");
		}
		
		TargetDataLine line = null;
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			line = (TargetDataLine) AudioSystem.getLine(info);
			line.open(format, 1024);
			line.start();
			
			byte[] buf = new byte[1024];
			
			long t1,t2;
			t1 = System.currentTimeMillis();
			
			for (;;) {
				line.read(buf, 0, buf.length);
				os.write(buf);
				
				t2 = System.currentTimeMillis();
				if ((t2-t1) >= timeMilis) {
					break;
				}
			}
			
			line.stop();
			
			os.close();
			
			capturedAudio = os.toByteArray();
		} catch (LineUnavailableException | IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			line.close();
		}
	}
	
	public void saveDataToWAV(String filePath) throws IOException {
		if (capturedAudio == null) {
			throw new RuntimeException("Áudio não capturado");
		}
		
		try (ByteArrayInputStream bais = new ByteArrayInputStream(capturedAudio);
				AudioInputStream ais = new AudioInputStream(bais, format, capturedAudio.length)) {
			Files.deleteIfExists(Paths.get(filePath));
			AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new File(filePath));
		}
	}
	
	public void reduceNoise(String filePath) {
		final String trimCmd = "sox "+filePath+" /tmp/audio-trimmed.wav trim 0.3";
//		final String highPassCmd = "sox "+filePath+" /tmp/audio-high.wav highpass 100";
//		final String lowPassCmd = "sox /tmp/audio-high.wav /tmp/audio-low.wav lowpass 4000";
		final String noiseProfileCmd = "sox /tmp/audio-trimmed.wav -n noiseprof /tmp/noisep";
		final String noiseRedCmd = "sox "+filePath+" /tmp/audio-result.wav noisered /tmp/noisep 0.24";
		
		try {
			execCmd(new String[] {trimCmd,noiseProfileCmd,noiseRedCmd});
			
			File f = new File("/tmp/audio-result.wav");
			if (!f.exists()) {
				throw new RuntimeException("Um erro ocorreu ao tentar aplicar o efeito no audio");
			}
			
			try (AudioInputStream is = AudioSystem.getAudioInputStream(f);
					ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
				byte[] buf = new byte[1024];
				int count=-1;
				while ((count = is.read(buf)) != -1) {
					baos.write(buf, 0, count);
				}
				this.convertedAudio = baos.toByteArray();
			} catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void execCmd(String... cmd) throws IOException {
		for (String c : cmd) {
			Process p = Runtime.getRuntime().exec(c.split(" "));
			try {
				p.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
			}
		}
	}
	
	public static void main(String[] args) {
		
		AudioRecorder ar = new AudioRecorder();
		
		ar.capture(2000);
		
		try {
			ar.saveDataToWAV("/tmp/audio.wav");
			ar.reduceNoise("/tmp/audio.wav");
			byte[] ca = ar.getCapturedAudio();
			byte[] c = ar.getConvertedAudio();
			
			System.out.println("ca => "+ca.length);
			System.out.println("c => "+c.length);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void captureFromFile(String file) throws IOException {
		try {
			AudioInputStream is = AudioSystem.getAudioInputStream(format, AudioSystem.getAudioInputStream(new File(file)));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int count=-1;
			while ((count=is.read(buf))>0) {
				baos.write(buf, 0, count);
			}
			baos.close();
			is.close();
			capturedAudio = baos.toByteArray();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
