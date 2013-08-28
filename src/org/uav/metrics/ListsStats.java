package org.uav.metrics;

import java.util.List;

/**
 * A class to ease basic calculus on list of Integer (sum, mean, std)
 * @author Julien Schleich
 *
 */
public class ListsStats {

    private ListsStats() {
    }

    public static double sum(List<Integer> l){
	Integer sum = 0;
	for (Integer value : l){
	    sum += value;
	}
	return sum;
    }

    public static double mean(List<Integer> l){
	double mean = ListsStats.sum(l);
	return (double)mean / (double)l.size();
    }


    public static double std(List<Integer> l){
	int sum = 0;
	Double mean = ListsStats.mean(l);
	for (Integer i : l){
	    sum += Math.pow((i - mean), 2);
	}
	return Math.sqrt( sum / ( l.size() - 1 ) ); 
    }

}
