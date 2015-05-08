package com.jbg.SortingBenchmark;

import java.util.List;

public class QuickSort<T extends Comparable<T>> extends SortingAlgorithm<T> {

	public QuickSort() {
		super();
	}
	
	@Override
	protected void sort(List<T> array) {
		quicksort(array,0,array.size()-1);
	}
	
	//Algorithm extracted from Wikipedia
	// http://en.wikipedia.org/wiki/Quicksort
	//lo = index of first element
	//hi = index of last element (CAREFUL: must be size-1)
	private void quicksort(List<T> array, int lo, int hi){
		int part;
		
		if(lo < hi){
			part = partition(array, lo, hi);
			quicksort(array, lo, part-1);
			quicksort(array, part+1, hi);
		}
	}
	
	private int partition(List<T> array, int lo, int hi){
		int storeIndex = lo;
		T pivot = array.get(hi);
		
		for(int i=lo ; i < hi ; ++i){
			if(array.get(i).compareTo(pivot) <= 0){
				swap(array, i, storeIndex);
				++storeIndex;
			}
		}
		swap(array, storeIndex, hi);
		return storeIndex;
	}

}
