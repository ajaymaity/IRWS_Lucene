package ie.tcd;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

public class HelloLucene {
	
	private static void addDoc(IndexWriter writer, String title, String isbn) throws IOException {
     
		Document doc = new Document();
        doc.add(new TextField("title", title, Field.Store.YES));
        doc.add(new StringField("isbn", isbn, Field.Store.YES));
        writer.addDocument(doc);
    }

	public static void main(String[] args) throws IOException, ParseException {
        
		// Specify the analyzer for tokenizing text
        Analyzer analyzer = new StandardAnalyzer(EnglishAnalyzer.getDefaultStopSet());
        // TODO: Try different analyzers, specifically EnglishAnalyzer, and one which has stemming functionality.

        // Create the index and store on disk
        Directory directory = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        IndexWriter iwriter = new IndexWriter(directory, config);
        
        // Add documents to index
        addDoc(iwriter, "Lucene in Action", "193398817");
        addDoc(iwriter, "Lucene for Dummies", "55320055Z");
        addDoc(iwriter, "Managing Gigabytes", "55063554A");
        addDoc(iwriter, "The Art of Computer Science", "9900333X");
        iwriter.close();
//        directory.close();

        // Query
        String querystr = args.length > 0 ? args[0] : "lucene";
        Query q = new QueryParser("title", analyzer).parse(querystr);

        // Search
        int hitsPerPage = 10;
        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs docs = searcher.search(q, hitsPerPage);
        ScoreDoc[] hits = docs.scoreDocs;

        // Display results
        System.out.println("Found " + hits.length + " hits.");
        for(int i=0;i<hits.length;++i) {
            
        	int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            System.out.println((i + 1) + ". " + d.get("isbn") + "\t" + d.get("title"));
        }

        directory.close();
        reader.close();
    }
}