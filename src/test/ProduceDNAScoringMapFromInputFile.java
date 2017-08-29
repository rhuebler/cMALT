package experimental;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

// read input file and adapt DNAScoring matrix for it 
public class ProduceDNAScoringMapFromInputFile {
	int matchScore;
	int misMatchScore;
	malt.align.DNAScoringMatrix scoringMatrix = new malt.align.DNAScoringMatrix(matchScore,misMatchScore);
	String path;
	public ProduceDNAScoringMapFromInputFile(int matchScore, int misMatchScore, String location){
		this.matchScore = matchScore;
		this.misMatchScore = misMatchScore;
		try {
			processFile(location);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public malt.align.DNAScoringMatrix getScoringMatrix(){
		return this.scoringMatrix;
	}
	private void processFile(String path) throws FileNotFoundException{
		HashMap<String,Integer> map = new HashMap<String,Integer>();
		// read in File here
		Scanner reader = new Scanner(new File(path));
		while(reader.hasNext()){	
			String line = reader.next();
			String[] fragments = line.split("\t");
			map.put(fragments[0], Integer.parseInt(fragments[1]));
		}
		reader.close();
		int[][] matrix = scoringMatrix.getMatrix();
		for(String key: map.keySet()){
			char[] chars = key.toCharArray();
			matrix[chars[0]][chars[1]] = map.get(key);
		}
		scoringMatrix.setMatrix(matrix);
	}
	
}
