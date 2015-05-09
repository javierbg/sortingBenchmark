package com.jbg.SortingBenchmark;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class Test {
	
	//final static String filename = "/home/javier/completeTest.csv";
	final static Charset encoding = StandardCharsets.UTF_8;
	
	/*static private class ResultsDoublePrecission{
		Double avgTimeTaken;
		Double avgSwaps;
	}*/
	
	static private class Results{
		int nElem;
		Long avgTimeTaken;
		//Long avgSwaps;
	}
	
	//Tests nTests runs of alg for nElem elements
	static private Results test(SortingAlgorithm<Integer> alg, int nElem, int nTests){
		Results result = new Results();
		List<Long> times = new ArrayList<>(nTests);
		//List<Integer> nSwaps = new ArrayList<>(nTests);
		List<Integer> list = new ArrayList<>(nElem);
		Random r = new Random();
		
		//result.nElem = nElem;
		
		for(int i=0 ; i < nTests ; ++i){
			//Generate new random list
			for(int j=0 ; j < nElem ; ++j){
				list.add(r.nextInt(nElem));
			}
			
			alg.execute(list);
			
			if(alg.executedCorrectly()){
				times.add(alg.getTimeTaken());
				//nSwaps.add(alg.getSwaps());
			}
			else{ //
				--i;
				System.out.println("Something went horribly wrong! RUN!");
			}
			alg.restart();
		}
		
		long avgTimeTaken = 0;
		for(Long time : times){
			avgTimeTaken += time;
		}
		avgTimeTaken /= nTests;
		
		/*long avgSwaps;
		long totalSwaps = 0;
		for(Integer n : nSwaps){
			totalSwaps += n.longValue();
		}
		avgSwaps = totalSwaps / nTests ;*/
		
		result.avgTimeTaken = avgTimeTaken;
		//result.avgSwaps = avgSwaps;
		
		return result;
	}
	
	//algs: Algorithms to test
	//nTests: Number of test to average
	//maxTimeNs: Max time allowed to run a test (will stop testing after said time)
	//maxElem: Maximum number of elements to run a test (will stop after reaching that number)
	//reached in an individual test)
	//IMPORTANT: if maxTimeNs or maxElem are negative, that bound will be ignored
	private static List<List<Results>> completeTest(Set<SortingAlgorithm<Integer>> algs, int nRuns, long maxTimeNs, int maxElem, int step){
		if(maxTimeNs < 0)
			maxTimeNs = Long.MAX_VALUE;
		
		if(maxElem < 0)
			maxElem = Integer.MAX_VALUE;
		
		List<List<Results>> retVal = new ArrayList<>(algs.size());
		
		for(SortingAlgorithm<Integer> alg : algs){
			long lastTime = 0;
			List<Results> results = new ArrayList<>();
			
			for(int i=1 ; (lastTime < maxTimeNs) && (i <= maxElem) ; i = i+step){
				Results r = test(alg, i, nRuns);
				r.nElem = i;
				results.add(r);
				lastTime = r.avgTimeTaken;
			}
			
			retVal.add(results);
			System.out.println(alg.getClass().getSimpleName() + " ended");
		}
		
		return retVal;
	}
	
	private static void saveResults(List<List<Results>> results, List<String> algNames, int nRuns, String filename){
		Path path = Paths.get(filename);
		try(BufferedWriter writer = Files.newBufferedWriter(path, encoding)){	
			
			//Header
			writer.write(";Time in ns (averaged over " + nRuns + " runs)\n");
			writer.write("Input size");
			for(String name : algNames){
				writer.write(";" + name);
			}
			writer.write("\n");
			
			//Get size of the biggest list
			int maxResults = 0;
                        int maxIndex = 0;
			for(List<Results> r : results){
				if(r.size() > maxResults){
					maxResults = r.size();
                                        maxIndex = results.indexOf(r);
                                }
			}
			
			//Body
			for(int i=0 ; i < maxResults ; ++i){
				writer.write(Integer.toString(results.get(maxIndex).get(i).nElem));
				
				for(List<Results> r : results){
					writer.write(";");
					if(i < r.size()){
						writer.write(r.get(i).avgTimeTaken.toString());
					}
				}
				
				writer.write("\n");
			}
			
			writer.close();
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
	
	public static void main(String[] args){
		Opts opt = new Opts();
		new JCommander(opt, args);
		
		System.out.println("File: <" + opt.filename + ">");
		System.out.println("Max time: <" + opt.maxTime + " ns>");
		System.out.println("Max elements: <" + opt.maxElem + ">");
		System.out.println("Step: <" + opt.step + ">");
		
		Set<SortingAlgorithm<Integer>> algs = new LinkedHashSet<>();
		//algs.add(new BubbleSort<Integer>());
		algs.add(new QuickSort<Integer>());
		algs.add(new MergeSort<Integer>());
		
		List<String> algNames = new ArrayList<>(algs.size());
		for(SortingAlgorithm<Integer> alg : algs){
			algNames.add(alg.getClass().getSimpleName());
		}
		
		List<List<Results>> complete = completeTest(algs, opt.nRuns, opt.maxTime, opt.maxElem, opt.step);
		
		System.out.println("All tests completed, saving results...");
		saveResults(complete, algNames, opt.nRuns, opt.filename);
	}
}

class Opts{
	@Parameter(names = {"-f"}, description = "Filename to save the results")
	public String filename = "sorting_benchmark.csv";
	
	@Parameter(names = {"-nr"}, description = "Number of runs to average")
	public int nRuns = 100;
	
	@Parameter (names = {"-mt"}, description = "Maximum execution time")
	public long maxTime = -1;
	
	@Parameter (names = {"-me"}, description = "Maximum number of elements")
	public int maxElem = 50;
	
	@Parameter (names = {"-s"}, description = "Step of number of elements between tests")
	public int step = 1;
	
	@Parameter(names = {"--help"}, help = true)
	private boolean help;
}
