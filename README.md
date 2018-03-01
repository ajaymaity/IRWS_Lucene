### CS7IS3 Assignment 1: Lucene and Cranfield

*Indexer.jar*: creates an index of the cran collection, and stores in disk.

To execute this file, run the following command:
```java -jar Indexer.jar```

Command line arguments can also be passed as follows. Run ```java -jar Indexer.jar --help```.
 -a,--analyzer <arg>     Lucene Analyzer. One of English, Keyword, Simple,
                         Standard, Stop, WhiteSpace.
 -d,--data <arg>         Directory where Cran data is present.
 -h,--help               Show help.
 -p,--hpp <arg>          Hits Per Page. Can be any positive non-zero
                         integer.
 -s,--similarity <arg>   Lucene Similarity. One of BM25, TFIDF,
                         LMDirichlet.

One example:
```java -jar Indexer.jar --analyzer Standard --similarity TFIDF --hpp 40 --data data/cran.tar```

Default command line arguments:
analyzer: English
data: data/cran.tar
hpp: 1000
similarity: BM25

*Searcher.jar*: searches through the index, and returns results. The output results are stored in output directory.

To execute this file, run the following command:
```java -jar Searcher.jar```

Same command line arguments as *Indexer.jar*.

The files present in output directory, namely QRelsCorrectedforTRECeval.txt and results.txt can be used to evaluate metrics using TREC Eval tool.
The command is as follows:
```../trec_eval.9.0/trec_eval -l 3 -m all_trec output/QRelsCorrectedforTRECeval.txt output/results.txt```

Additional file *Evaluator.jar*: generates MAP and Mean Recall metrics

To execute this file, run the following command:
```java -jar Evaluator.jar```

Same command line arguments as *Indexer.jar* and *Similarity.jar*.

May use TREC Eval tool instead of using *Evaluator.jar*.

The source code is present in: [GitLab](http://gitlab.scss.tcd.ie/maitya/IRWS_Lucene.git)