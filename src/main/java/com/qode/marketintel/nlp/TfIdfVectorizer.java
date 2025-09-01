package com.qode.marketintel.nlp;

import org.apache.lucene.analysis.Analyzer;
import java.util.*;

public class TfIdfVectorizer {
  private final Analyzer analyzer;
  private final Map<String,Integer> vocab = new HashMap<>();
  private final Map<Integer,Double> idf = new HashMap<>();

  public TfIdfVectorizer(Analyzer analyzer){ this.analyzer = analyzer; }

  public List<Map<Integer,Double>> fitTransform(List<String> docs){
    List<Map<Integer,Double>> tfs = new ArrayList<>();
    List<Set<Integer>> seen = new ArrayList<>();
    for (String d: docs){
      Map<Integer,Double> tf = tokenizeToTf(d);
      tfs.add(tf);
      seen.add(tf.keySet());
    }
    // compute idf
    int N = docs.size();
    Map<Integer,Integer> df = new HashMap<>();
    for (Set<Integer> s : seen) for (int idx: s) df.merge(idx,1,Integer::sum);
    df.forEach((k,v)-> idf.put(k, Math.log( (N+1.0)/(v+1.0) ) + 1.0));

    // multiply
    for (Map<Integer,Double> tf : tfs){
      for (var e : tf.entrySet()) tf.put(e.getKey(), e.getValue()*idf.get(e.getKey()));
    }
    return tfs;
  }

  private Map<Integer,Double> tokenizeToTf(String text){
    Map<Integer,Double> tf = new HashMap<>();
    try (var ts = analyzer.tokenStream("f", text)){
      ts.reset();
      var attr = ts.addAttribute(org.apache.lucene.analysis.tokenattributes.CharTermAttribute.class);
      while (ts.incrementToken()){
        String w = attr.toString();
        int idx = vocab.computeIfAbsent(w, k->vocab.size());
        tf.merge(idx, 1.0, Double::sum);
      }
      ts.end();
    } catch (Exception ignore) {}
    // l2 norm
    double s = Math.sqrt(tf.values().stream().mapToDouble(x->x*x).sum());
    if (s>0) tf.replaceAll((k,v)->v/s);
    return tf;
  }
}
