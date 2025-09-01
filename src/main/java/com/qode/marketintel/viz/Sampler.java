package com.qode.marketintel.viz;

import java.nio.file.Path;
import java.util.*;


public class Sampler {
  public static Map<String,Integer> hourlyCounts(Path parquet) {
    // Placeholder: real implementation should stream Parquet row groups.
    Map<String,Integer> m = new HashMap<>();
    for (int h=0; h<24; h++) m.put(String.format("%02d:00", h), (int)(Math.random()*200));
    return m;
  }
}
