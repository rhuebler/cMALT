package experimental;
//Seems workable enough for now
import java.util.HashMap;

import malt.align.IScoringMatrix;

public class DNAScoringMatrix implements IScoringMatrix{
	double percent;
    int matchScore;
    int mismatchScore;
    private int[][] matrix = new int[128][128];
    //????????? //scoring  map fuer alles was moeglich ist  N gegen N vermutllich 0 
    // Standard scoring  map case invariant damge ignorieren oder wenigstens verringern 
    // option scoring  map anzugeben 
    // optionsmanager anschauen 
    //Seed pattern optional setzen 
   // bei malt build moeglich??? 
    // duplicate removal muesste hashing wenn sich read wiederholen wird keine berechnung durch gefuert sondern ergebnisse wieder eingetragen 
    // geht nur mit merged reads was machen wir mit single reads  
    public DNAScoringMatrix(int matchScore, int mismatchScore, double percent) { // you have to case sensitive  
    	
    	
    	// Initialize custom matrix 
    	// reassign stuff we are interested in
    	for (int i = 0; i < 128; i++) {
            matrix[i][i] = matchScore;
            for (int j = i + 1; j < 128; j++)
                matrix[i][j] = matrix[j][i] = mismatchScore;
        }
    	
       this.matchScore = matchScore;
       this.mismatchScore=mismatchScore;
       int gentleScore = (int) Math.round(mismatchScore - ( mismatchScore*percent)); //TODO make damage mismatches less severe in a more intelligent way 
       for(char c1 : "atcguATCGU".toCharArray()){
    	   for(char c2 : "atcguATCGU".toCharArray()){
    		   String s = c1+""+c2;
        	  if(c1 == 'c' && c2 == 't' || c1 == 't' && c2 == 'c'){ // would you work and do you have to be case sensitive ?
        		    matrix[c1][c2] =  gentleScore;
        	   }else if(c1 == 'g' && c2 == 'a' || c1 == 'a' && c2 == 'g'){
        		   
        		    matrix[c1][c2] =  gentleScore;
        	   }else if(c1 == 'C' && c2 == 'T' || c1 == 'T' && c2 == 'C'){
       		    	
       		    	matrix[c1][c2] =  gentleScore;
        	   }else if(c1 == 'G' && c2 == 'A' || c1 == 'A' && c2 == 'G'){
        		  
        		   matrix[c1][c2] =  gentleScore;
        	   }
           }
       }
       for(char c1: "nkryswbvhdx.-NKRYSWBVHDX".toCharArray()){
    	   for(char c2: "nkryswbvhdx.-NKRYSWBVHDX".toCharArray()){
    		   String s = c1+""+c2;
    		   if(c1 == c2){
        		  
        		    matrix[c1][c2] = 0;
        	   }
           }
       }
    }

    /**
     * get score for letters a and b
     *
     * @param a
     * @param b
     * @return score
     */
    public int getScore(byte a, byte b) {
    	//TODO convert byte to char
    	return  matrix[a][b];
    }

   
  //TODO instead of keeping as a HashMap maybe better transfer to int[][] array to be usable with interface but how do I do it?
    public int[][] getMatrix() {
        return matrix;
    }

	@Override
	public void setMatrix(int[][] matrix) {
		this.matrix = matrix;
		// TODO Auto-generated method stub
		
	}
    
}

