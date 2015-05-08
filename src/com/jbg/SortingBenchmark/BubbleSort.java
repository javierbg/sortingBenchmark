package com.jbg.SortingBenchmark;

import java.util.List;

public class BubbleSort<T extends Comparable<T>> extends SortingAlgorithm<T> {

	public BubbleSort() {
		super();
	}
	
	@Override
	protected void sort(List<T> array) {
		int size = array.size();
		T elem1, elem2;
		boolean sorted = false;
		
		while(!sorted){
			sorted = true;
			for(int i=0 ; i < (size-1) ; ++i){
				elem1 = array.get(i);
				elem2 = array.get(i+1);
				
				if(elem1.compareTo(elem2) > 0){
					swap(array, i, i+1);
					sorted = false;
				}
				
				//--size;
			}
		}
	}

}
