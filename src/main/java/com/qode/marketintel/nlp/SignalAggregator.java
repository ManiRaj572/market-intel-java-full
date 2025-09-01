package com.qode.marketintel.nlp;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class SignalAggregator {
  public static class SignalCI { public final double mean, lo, hi; public SignalCI(double m,double l,double h){mean=m;lo=l;hi=h;} }

  // simple linear combo; weights can be learned later
  public static double composite(double tfidfScore, Map<String,Double> f){
    return 0.6*tfidfScore + 0.3*f.getOrDefault("engagement",0.0) + 0.1*f.getOrDefault("hashtag_ct",0.0);
  }

  public static SignalCI bootstrap(List<Double> signals, int B){
    double[] res = new double[B];
    ThreadLocalRandom rnd = ThreadLocalRandom.current();
    int n = signals.size();
    for (int b=0;b<B;b++){
      double m=0; for (int i=0;i<n;i++) m+=signals.get(rnd.nextInt(n));
      res[b]=m/n;
    }
    Arrays.sort(res);
    double mean = Arrays.stream(res).average().orElse(0);
    return new SignalCI(mean, res[(int)(0.05*B)], res[(int)(0.95*B)]);
  }
}
