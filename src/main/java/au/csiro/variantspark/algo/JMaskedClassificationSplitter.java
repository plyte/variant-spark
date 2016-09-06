package au.csiro.variantspark.algo;

import java.util.Arrays;

import au.csiro.variantspark.metrics.Gini;


/**
 * Fast gini based splitter.
 * NOT MULITHEADED !!! (Caches state to avoid heap allocations)
 * 
 * @author szu004
 *
 */
public class JMaskedClassificationSplitter {
	private final int[] leftSplitCounts;
	private final int[] rightSplitCounts;
	private final double[] leftRigtGini = new double[2];
	private final int[] labels;
		
	public JMaskedClassificationSplitter(int[] labels, int nCategories) {
		this.labels = labels;
		this.leftSplitCounts = new int[nCategories];
		this.rightSplitCounts = new int[nCategories];
	}
		 
	 public SplitInfo findSplit(double[] data,int[] splitIndices) {	
	    SplitInfo result = null;
	    double minGini = 1.0;
	    // let's think about like this 
	    // on the first pass we both calculate the splits as well as determine which split points are actually present
	    // in this dataset
	    // as 0 are most likely we will  do 0 as the initial pass

	    long splitCandidateSet = 0L; 
		for(int i:splitIndices) {
			splitCandidateSet|=(1 << (int)data[i]);
		}
		
		int sp  = 0;
		while(splitCandidateSet != 0L) {
			while (splitCandidateSet != 0L && (splitCandidateSet & 1) == 0) {
				sp ++;
				splitCandidateSet >>= 1;
			}
			splitCandidateSet >>= 1;
			// only run if there is at least one more index to try in this subset
			if (splitCandidateSet != 0L) {
				Arrays.fill(leftSplitCounts, 0);
				Arrays.fill(rightSplitCounts, 0);
				for(int i:splitIndices) {
					if ((int)data[i] <=sp) {
						leftSplitCounts[labels[i]]++;
					} else {
						rightSplitCounts[labels[i]]++;					
					}
				}
				double g = FastGini.splitGini(leftSplitCounts, rightSplitCounts, leftRigtGini);
				if (g < minGini ) {
					result = new SplitInfo(sp, g, leftRigtGini[0], leftRigtGini[1]);
					minGini = g;
				}
				sp++;
			}
		}
		return result;
	 }
}