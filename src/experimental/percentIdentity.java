package experimental;

import java.nio.charset.Charset;

public class percentIdentity {
	private static int alignmentLength;
	private static byte[][] alignment;
	static int tails = 5;
	boolean useBorderMatrix = true;
	private static float getBorderMatrixPercentIdentity(){//TODO calculate PercentIdentityToIgnoreDamage    	
	    	if(alignment!= null && alignmentLength!=0){
	    		
	    		int matches = 0;
	    		int ignore =  0;
	    		byte[] query = alignment[0];
	    		byte[]	reference = alignment[2];	
	    		for(int i = 0;i < query.length;i++){
	    			if(query[i]==reference[i]){
	    				matches++;
	    			}else{
	    				
	    				if(i <= tails || i>= alignmentLength-tails){
	    					if((query[i]=='A' && reference[i]=='G') || 
		    						query[i]=='T' && reference[i]=='C'){
	    						System.out.println("here");
		    				ignore++;
		    				}
	    				}
	    			}
	    		}
	    		double f = matches / (alignmentLength-ignore);
	    		System.out.println(matches);
	    		System.out.println(alignmentLength-ignore);
	    		System.out.println();
	    		return  (float) (100 * matches)/ (float) (alignmentLength-ignore);
	    	}else{
	    		return 0;
	    	}
	    }
	    public float getPercentIdentity() {
	    	//getBorderMatrix Alignment by calculating it from ALignment
	        if(useBorderMatrix){
	        	return getBorderMatrixPercentIdentity();
	        }
	        else
	        	return 0;
	    }
	    public static void main(String[] args){
	    	
	    	String query =		"ATTCTCGGGACGTTTGGAAACCTGCGGGCCCAAGGTTGCCAAAAGA";
	    	String reference =	"GCCCTCGGGACGTTTGGAAACCTGCGGGCCCAAGGCTGCCAGGAGG";
	    	alignmentLength = query.length();
	    	alignment = new byte[3][query.length()];
	    	alignment[0]=query.getBytes(Charset.forName("UTF-8"));
	    	alignment[2]=reference.getBytes(Charset.forName("UTF-8"));
	    	
	    	System.out.println(getBorderMatrixPercentIdentity());
	    }
}
