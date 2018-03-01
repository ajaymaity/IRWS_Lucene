### CS7IS3 Assignment 1: Lucene and Cranfield

*Indexer.jar*: creates an index of the cran collection, and stores in disk.

To execute this file, run the following command:<br/>
```java -jar Indexer.jar```

Command line arguments can also be passed as follows. Run ```java -jar Indexer.jar --help```.<br/>
&nbsp;-a,--analyzer <arg>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Lucene Analyzer. One of English, Keyword, Simple,<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Standard, Stop, WhiteSpace.<br/>
&nbsp;-d,--data <arg>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Directory where Cran data is present.<br/>
&nbsp;-h,--help&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Show help.<br/>
&nbsp;-p,--hpp <arg>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Hits Per Page. Can be any positive non-zero<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;integer.<br/>
&nbsp;-s,--similarity <arg>&nbsp;&nbsp;&nbsp;Lucene Similarity. One of BM25, TFIDF,<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;LMDirichlet.<br/>

One example:<br/>
```java -jar Indexer.jar --analyzer Standard --similarity TFIDF --hpp 40 --data data/cran.tar```

Default command line arguments:<br/>
analyzer: English<br/>
data: data/cran.tar<br/>
hpp: 1000<br/>
similarity: BM25<br/>

*Searcher.jar*: searches through the index, and returns results. The output results are stored in output directory.

To execute this file, run the following command:<br/>
```java -jar Searcher.jar```

Same command line arguments as *Indexer.jar*.

The files present in output directory, namely QRelsCorrectedforTRECeval.txt and results.txt can be used to evaluate metrics using TREC Eval tool.<br/>
The command is as follows:<br/>
```../trec_eval.9.0/trec_eval -l 3 -m all_trec output/QRelsCorrectedforTRECeval.txt output/results.txt```

Additional file *Evaluator.jar*: generates MAP and Mean Recall metrics

To execute this file, run the following command:<br/>
```java -jar Evaluator.jar```

Same command line arguments as *Indexer.jar* and *Similarity.jar*.

May use TREC Eval tool instead of using *Evaluator.jar*.

The source code is present in: [GitLab](http://gitlab.scss.tcd.ie/maitya/IRWS_Lucene.git)