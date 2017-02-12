package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class Config {
	public static final boolean DEBUG = false;
	
	public static final int WIDTH = 1344;
	public static final int HEIGHT = 756;
	
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
