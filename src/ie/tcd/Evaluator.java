package ie.tcd;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.lucene.queryparser.classic.ParseException;

/**
 * Implements evaluation functionality. May use TREC Eval instead.
 * @author Ajay Maity Local
 *
 */
public class Evaluator {
	
	/**
	 * Calculate MAP and Mean Recall
	 * @param resultDict: dictionary of results obtained from Searcher wrapper
	 * @param cranRelDict: Reference cran query results dictionary
	 * @return
	 */
	private Map<String, Double> calculateMetrics(Map<String, List<String>> resultDict, Map<String, List<List<String>>> cranRelDict) {
		
		Map<String, Double> metrics = new HashMap<String, Double>();
		
		List<Double> avgPrecisionList = new ArrayList<Double>();
		List<Double> recallList = new ArrayList<Double>();
		Iterator<Entry<String, List<String>>> resultEntry = resultDict.entrySet().iterator();
		
		// Loop through each results
		while (resultEntry.hasNext()) {
			
			Map.Entry<String, List<String>> resultPair = (Map.Entry<String, List<String>>) resultEntry.next();
			String resultID = resultPair.getKey();
			List<String> resultList = resultPair.getValue();
			
			List<List<String>> cranRelList = cranRelDict.get(resultID);
			List<String> cranRelRelevantList = cranRelList.get(0);
						
			List<Double> precisionList = new ArrayList<Double>();
			int docCount = 0;
			double recall = 0.0;
			for (int i = 0; i < resultList.size(); i++) {
				
				String docID = resultList.get(i);
				if (cranRelRelevantList.contains(docID)) {
					
					docCount++;
					precisionList.add((double) docCount / (i + 1));
					recall = docCount / cranRelRelevantList.size();
				}				
			}
			
			double avgPrecision = 0.0;
			if (precisionList.size() > 0) { 
				
				for (int i = 0; i < precisionList.size(); i++) avgPrecision += precisionList.get(i);
				avgPrecision /= precisionList.size();
			}

			avgPrecisionList.add(avgPrecision); 
			recallList.add(recall);
		}
		
		// Calculate MAP
		double map = 0.0;
		for (int i = 0; i < avgPrecisionList.size(); i++) map += avgPrecisionList.get(i);
		map /= avgPrecisionList.size();
	
		// Calculate Mean Recall
		double meanRecall = 0.0;
		for (int i = 0; i < recallList.size(); i++) meanRecall += recallList.get(i);
		meanRecall /= recallList.size();
		
		metrics.put("MAP", map);
		metrics.put("Mean Recall", meanRecall);
		return metrics;
	}

	/**
	 * Main Method
	 * @param args: Command line arguments
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
		
		Map<String, String> values = new Cli(args).parse();
		String analyzer = values.get("Analyzer");
		String similarity = values.get("Similarity");
		String hpp = values.get("HPP");
		
		System.out.println("Parsing CRAN Queries...");
		FileIO fileIO = new FileIO();
		List<Map<String, String>> cranQueryList = fileIO.parseCranQueries("data/cran.tar");
		System.out.println("Parsing done!\n");
		
		System.out.println("Searching data...");
		Searcher searcher = new Searcher();
		Map<String, List<String>> resultDict = searcher.searchCranQueries(Paths.get("index/cran.index"), cranQueryList, analyzer, similarity, hpp);
		System.out.println("Searching done!\n");
		
		System.out.println("Parsing CRAN Relevancy Judgements...");
		Map<String, List<List<String>>> cranRelDict = fileIO.parseCranRel("data/cran.tar");
		System.out.println("Parsing Done!\n");
		
		System.out.println("Evaluating Search Engine...");
		Evaluator evaluator = new Evaluator();
		Map<String, Double> metrics = evaluator.calculateMetrics(resultDict, cranRelDict);
		System.out.println("Mean Average Precision: " + metrics.get("MAP"));
		System.out.println("Mean Recall: " + metrics.get("Mean Recall"));
		System.out.println("Evaluation done!");
	}
}
