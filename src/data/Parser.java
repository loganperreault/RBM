package data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Parser {
	
	Data data;
	String delim = " ";
	
	public Parser(Data data) {
		this.data = data;
	}
	
	public void parseLabelFile(String filePath, String label) {
		parseLabelFile(filePath, label, Integer.MAX_VALUE);
	}
	
	/**
	 * Parses a file that contains datapoints for a single label.
	 * @return	A list of datapoints
	 */
	public void parseLabelFile(String filePath, String label, int numExamples) {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filePath));
			String line = null;
			int example = 0;
			while ((line = reader.readLine()) != null && example < numExamples) {
				String[] elements = line.split(delim);
				int[] points = new int[elements.length];
				for (int i = 0; i < elements.length; i++)
					points[i] = Integer.valueOf(elements[i]);
				data.add(points, label);
				example++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
