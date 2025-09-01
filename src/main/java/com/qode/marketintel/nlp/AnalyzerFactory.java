package com.qode.marketintel.nlp;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LetterTokenizer;
import org.apache.lucene.analysis.icu.segmentation.ICUTokenizer;
import org.apache.lucene.analysis.miscellaneous.LowerCaseFilter;

public class AnalyzerFactory {
  public static Analyzer indicAnalyzer(){
    return new Analyzer() {
      @Override protected TokenStreamComponents createComponents(String fieldName) {
        var src = new ICUTokenizer();
        TokenStream tok = new LowerCaseFilter(src);
        return new TokenStreamComponents(r -> src.setReader(r), tok);
      }
    };
  }
}
