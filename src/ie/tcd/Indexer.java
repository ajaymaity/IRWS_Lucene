package ie.tcd;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

 class Indexer {

	public void createCranIndex(Path indexFile, List<Map<String, String>> cranList, String analyzerString, String similarity) {
		
		try {

			// Create analyzer
			Analyzer analyzer = null;
			if (analyzerString.equals("Standard")) analyzer = new StandardAnalyzer(EnglishAnalyzer.getDefaultStopSet());
			else if (analyzerString.equals("Keyword")) analyzer = new KeywordAnalyzer();
			else if (analyzerString.equals("WhiteSpaceAnalyzer")) analyzer = new WhitespaceAnalyzer();
			else if (analyzerString.equals("Simple")) analyzer = new SimpleAnalyzer();
			else if (analyzerString.equals("Stop")) analyzer = new StopAnalyzer();
			else {
				
				analyzer = new EnglishAnalyzer();
				analyzerString = "English";
			}
						
			// Store index on disk
			Directory directory = FSDirectory.open(indexFile);
			
			// Create index writer
			IndexWriterConfig config = new IndexWriterConfig(analyzer);
			config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
			if (similarity.equals("TFIDF")) config.setSimilarity(new ClassicSimilarity());
			else if (similarity.equals("LMDirichlet")) config.setSimilarity(new LMDirichletSimilarity());
			else {
				
				config.setSimilarity(new BM25Similarity());
				similarity = "BM25";
			}
			
			System.out.println("Creating index using " + analyzerString + " analyzer and " + similarity + " similarity.");
			
			IndexWriter iwriter = new IndexWriter(directory, config);
			
			// Add documents to index
			for (int i = 0; i < cranList.size(); i++)
				addCranDocument(iwriter, cranList.get(i));
			
			iwriter.close();
			directory.close();
		}
		catch (IOException e) {
			
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private void addCranDocument(IndexWriter iwriter, Map<String, String> cranDict) throws IOException {
		
		Document document = new Document();
		document.add(new StringField("ID", cranDict.get("ID"), Field.Store.YES));
		document.add(new TextField("Title", cranDict.get("Title"), Field.Store.YES));
		document.add(new TextField("Locations", cranDict.get("Locations"), Field.Store.YES));
		document.add(new TextField("Authors", cranDict.get("Authors"), Field.Store.YES));
		document.add(new TextField("Abstract", cranDict.get("Abstract"), Field.Store.YES));
		iwriter.addDocument(document);
	}

	public static void main(String[] args) {
		
		Map<String, String> values = new Cli(args).parse();
		String analyzer = values.get("Analyzer");
		String similarity = values.get("Similarity");
		String dataDir = values.get("DataDir");
		
		System.out.println("Parsing CRAN data...");
		FileIO fileIO = new FileIO();
		List<Map<String, String>> cranList = fileIO.parseCran(dataDir);
		System.out.println("Parsing done!\n");
		
		System.out.println("Deleting previous index files, if they exist...");
		fileIO.deleteDir(new File("index"));
		System.out.println("Done!\n");
		
		System.out.println("Indexing data...");
		Indexer indexer = new Indexer();
		indexer.createCranIndex(Paths.get("index/cran.index"), cranList, analyzer, similarity);
		System.out.println("Indexing done, and saved on disk!");
	}
}
