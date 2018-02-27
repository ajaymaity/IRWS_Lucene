package ie.tcd;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Take cares of command line arguments
 * Copied CLI code from http://www.thinkplexx.com/blog/simple-apache-commons-cli-example-java-command-line-arguments-parsing
 * @author Ajay Maity Local
 *
 */

@SuppressWarnings("deprecation")
public class Cli {

	private String[] args = null;
	private Options options = new Options();
	
	/**
	 * Constructor
	 * @param args: Command line arguments
	 */
	public Cli(String[] args) {
		
		this.args = args;
		options.addOption("h", "help", false, "Show help.");
		options.addOption("d", "data", true, "Directory where Cran data is present.");
		options.addOption("a", "analyzer", true, "Lucene Analyzer. One of English, Keyword, Simple, Standard, Stop, WhiteSpace.");
		options.addOption("s", "similarity", true, "Lucene Similarity. One of BM25, TFIDF, LMDirichlet.");
		options.addOption("p", "hpp", true, "Hits Per Page. Can be any positive non-zero integer.");
	}
	
	/**
	 * Parse the command line arguments
	 * @return a hash map of command line arguments in a key-value pair.
	 */
	public Map<String, String> parse() {
		
		Map<String, String> values = new HashMap<String, String>();
		
		CommandLineParser parser = new BasicParser();
		CommandLine cmd = null;
		try {
			
			cmd = parser.parse(options, args);
			if (cmd.hasOption("h")) help();
			values.put("DataDir", cmd.getOptionValue("d", "data/cran.tar"));
			values.put("Analyzer", cmd.getOptionValue("a", "English"));
			values.put("Similarity", cmd.getOptionValue("s", "BM25"));
			values.put("HPP", cmd.getOptionValue("p", "1000"));
		}
		catch (ParseException e) {
			
			System.out.println("Failed to parse command line arguments.");
			help();
		}
		
		return values;
	}

	/**
	 * Help method
	 */
	private void help() {
		
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("Help", options);
		System.exit(0);
	}
}
