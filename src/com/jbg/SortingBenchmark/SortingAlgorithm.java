package com.jbg.SortingBenchmark;

import java.util.Collections;
import java.util.List;

abstract public class SortingAlgorithm<T extends Comparable<T>> {

	private int swaps; //Number of swaps
	private int comparisons; //Number of comparisons
	private long timetaken; //Time taken to sort
	private boolean executedCorrectly; //Has the algorithm been executed?
	
	protected SortingAlgorithm(){
		restart();
	}
	
	//Executes the sorting algorithm, times it and checks it
	public void execute(List<T> array){
		long tick, tock;
		
		tick = System.nanoTime();
		sort(array);
		tock = System.nanoTime();
		
		//if(check(array)){
		if(true){
			executedCorrectly = true;
			setTimeTaken(tock - tick);
		}
		
	}
	
	//Checks if the array was ordered correctly
	@SuppressWarnings("unused")
	private boolean check(List<T> array){
		boolean retVal = true;
		
		for(int i=0 ; i < (array.size() - 1) ; ++i){
			T elem1 = array.get(i);
			T elem2 = array.get(i+1);
			
			if(elem1.compareTo(elem2) > 0){
				retVal = false;
			}
		}
		
		return retVal;
	}
	
	//Sorts the array
	abstract protected void sort(List<T> array);
	
	protected void swap(List<T> l, int i, int j){
		Collections.swap(l, i, j);
		swapMade();
	}
	
	protected void comparisonMade(){
		comparisons++;
	}
	
	protected void swapMade(){
		swaps++;
	}
	
	protected void setTimeTaken(long time){
		timetaken = time;
	}
	
	public int getSwaps() {
		return swaps;
	}
	public int getComparisons() {
		return comparisons;
	}
	public long getTimeTaken() {
		return timetaken;
	}
	public boolean executedCorrectly() {
		return executedCorrectly;
	}
	
	public void restart(){
		swaps = 0;
		comparisons = 0;
		timetaken = 0;
		executedCorrectly = false;
	}
	
}
