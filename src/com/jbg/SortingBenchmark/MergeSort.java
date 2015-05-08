package com.jbg.SortingBenchmark;

import java.util.ArrayList;
import java.util.List;

public class MergeSort<T extends Comparable<T>> extends SortingAlgorithm<T> {

	@Override
	protected void sort(List<T> array) {
		List<T> sorted = mergesort(array);
		array.clear();
		array.addAll(sorted);
	}
	
	private List<T> mergesort (List<T> array){
		int size = array.size();
		int middle = size / 2;
		List<T> left = new ArrayList<>(middle);
		List<T> right = new ArrayList<>(middle +1);
		List<T> result = new ArrayList<>(size);
		
		if(array.size() <= 1)
			return array;
		
		for(int i=0 ; i < middle ; ++i){
			left.add(array.get(i));
		}
		
		for(int i=middle ; i < size ; ++i){
			right.add(array.get(i));
		}
		
		left = mergesort(left);
		right = mergesort(right);
		
		result = merge(left, right);
		
		return result;
	}

	private List<T> merge(List<T> first, List<T> last){
		List<T> retVal = new ArrayList<>(first.size() + last.size());
		
		while(first.size() > 0 && last.size() > 0){
			T topFirst = first.get(0);
			T topLast = last.get(0);
			if(topFirst.compareTo(topLast) <= 0){
				retVal.add(topFirst);
				first.remove(0);
			}
			else{
				retVal.add(topLast);
				last.remove(0);
			}
		}
		
		if(first.size() > 0){
			retVal.addAll(first);
		}
		else if (last.size() > 0){
			retVal.addAll(last);
		}
		
		return retVal;
	}
}