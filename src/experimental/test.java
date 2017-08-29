package experimental;

import java.io.IOException;
import jloda.util.ArgsOptions;
import jloda.util.CanceledException;
import jloda.util.ProgramProperties;
import jloda.util.UsageException;
import malt.MaltOptions;
import malt.align.AlignerOptions;
import malt.align.DNAScoringMatrix;

public class test {
	public static void main(String args[]) throws UsageException, IOException{
		try {
			jloda.util.ArgsOptions options = new ArgsOptions(args, ProgramProperties.getProgramName(), "Testing");
			
			double minComplexity = options.getOption("c", "minComp", "Set Minimum Query Complexity", 0.0); 
			System.out.println(minComplexity);
			
			boolean removeDups = options.getOption("r", "removeDup", "Remove Duplicates if cashing", false);
			System.out.println(removeDups);
			
			String customLocation = options.getOption("u", "customScoring", "Provide location to custom DNA scoring Matrix", "");
			System.out.println(customLocation);

			char c =  'C';
			byte b = (byte) c;
			System.out.println(b);
			MaltOptions mOptions = new MaltOptions();
			mOptions.setUserCustomDNAScoring(customLocation);
			mOptions.setRemoveDuplicates(removeDups);
			mOptions.setMinimumQueryComplexity(minComplexity);
			mOptions.setAncientScoringMatrix(options.getOption("asm", "ancientScoringMatrix", "Use DNAScoring Matrix desinged for aDNA", false));
			System.out.println("ANcient Scoring Matrix "+mOptions.getAncientScoringMatrix());
			AlignerOptions  alignerOptions= new AlignerOptions();
			if(mOptions.getAncientScoringMatrix())
				{
				alignerOptions.setScoringMatrix(new experimental.DNAScoringMatrix(alignerOptions.getMatchScore(), alignerOptions.getMismatchScore(),alignerOptions.getPercent()));
            	alignerOptions.setReferenceIsDNA(true);
            	}
			
		//	options.getOptionMandatory("m", "mode", "Program mode", BlastMode.values(), BlastMode.BlastX.toString()));
		} catch (CanceledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Great custom matrix works for now. Maybe easier to just change the specifed scores in the default matrix 
		experimental.DNAScoringMatrix exMatrix = new experimental.DNAScoringMatrix(10,-10,0.1);
		int[][] ex = exMatrix.getMatrix();
		for(int i = 0; i<ex.length;i++ ){
			for(int k = 0; k< ex[i].length;k++){
				System.out.print(ex[i][k]+" ");
			}
			System.out.println();
			
		}		
		malt.align.DNAScoringMatrix original = new malt.align.DNAScoringMatrix(10,-10);
		System.out.println(ex['c']['t']);
		System.out.println("orignal");
//		for(int i = 0; i<original.getMatrix().length;i++ )
//			System.out.println(original.getMatrix()[i]);
//    	for(String x : matrix.getMatrix().keySet()){
//    		System.out.println(x);
//    	}
//    	System.out.println(matrix.getScore('A', 'T'));
    }

}
