package util;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class Config {
	private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	public static final boolean DEBUG = false;
	
	public static final int WIDTH = (int) screenSize.getWidth();
	public static final int HEIGHT = (int) screenSize.getHeight();
	
	public static int getScore() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File("score.txt")));
			String line = reader.readLine();
			reader.close();
			return Integer.parseInt(line.split(" ")[0]);
		} catch(Exception e) {
			return 0;
		}
	}
	
	public static void saveScore(int score, String details) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("score.txt")));
			writer.write(score + " " + details + "\n");
			writer.close();
		} catch(Exception e) {
			e.printStackTrace();
			System.err.println("Error on saving file.");
		}
	}
}
