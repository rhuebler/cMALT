package experimental;

import java.nio.charset.Charset;

public class percentIdentity {
	private static int alignmentLength;
	private static byte[][] alignment;
	static int tails = 5;
	boolean useBorderMatrix = true;
	static int modifiedLength=0;
	static int endReference=30;
	static int startReference=20;
	private static float getBorderMatrixPercentIdentity(){//TODO calculate PercentIdentityToIgnoreDamage    	
	    	if(alignment!= null && alignmentLength!=0){
	    		
	    		int matches = 0;
	    		int ignore =  0;
	    		byte[] query = alignment[0];
	    		byte[]	reference = alignment[2];	
	    		for(int i = 0;i < query.length;i++){
//	    			sequence+=(char)query[i];
//	    			ref+=(char) reference[i];
//	    			al+=(char) alignment[1][i];
	    			
	    			if(query[i]==reference[i]){
	    				matches++;
	    			}else{
		    			if(endReference>startReference + 1){
		    					if(i <= tails){
			    					if(query[i]=='T' && reference[i]=='C'){
			    						ignore++;
			    						//matches++;
			    					}	
			    				}
			    				else if(i>= alignmentLength-tails){
			    					if(query[i]=='A' && reference[i]=='G'){
			    						ignore++;	
			    						//matches++;
			    					}	
			    				}
		    			}else{
		    				if(i <= tails){
		    					if(query[i]=='A' && reference[i]=='G'){
		    						ignore++;
		    						//matches++;
		    					}
		    				}	
		    				else if(i>= alignmentLength-tails){
		    					if(query[i]=='T' && reference[i]=='C'){
		    						ignore++;
		    						//matches++;
		    					}
			    				}	
		    			}
	    			}
	    		}
	    		
	    		modifiedLength = alignmentLength-ignore;
	    		System.out.println(modifiedLength);
	    		System.out.println(alignmentLength);
	    		System.out.println(matches);
	    		//modifiedIdentities = matches;
//	    		System.out.println();
//	    		System.out.println(sequence);
//	    		System.out.println(al);
//	    		System.out.println(ref);
//	    		
//	    		System.out.println("Unmodified"+ (float) (100 * getIdentities())/ (float) alignmentLength);
//	    		System.out.println("Modified"+ (float) (100 * getIdentities())/ (float) modifiedLength);
	    		return (float) (100 * matches)/ (float) modifiedLength;
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
//	    	ATATTCCATTTTTTTGATGCATCCTTGATCGCCAAATAAACAACCTTCCGC
//	    	||| |||||||||||||||||||||||||||||||||||||||||||||||
//	    	ATACTCCATTTTTTTGATGCATCCTTGATCGCCAAATAAACAACCTTCCGC
	    	String query =		"ATATTCCATTTTTTTGATGCATCCTTGATCGCCAAATAAACAACCTTCCAT";
	    	String reference =	"ATACTCCATTTTTTTGATGCATCCTTGATCGCCAAATAAACAACCTTCCGC";
	    	alignmentLength = query.length();
	    	alignment = new byte[3][query.length()];
	    	alignment[0]=query.getBytes(Charset.forName("UTF-8"));
	    	alignment[2]=reference.getBytes(Charset.forName("UTF-8"));
	    	
	    	System.out.println(getBorderMatrixPercentIdentity());
	    }
}
