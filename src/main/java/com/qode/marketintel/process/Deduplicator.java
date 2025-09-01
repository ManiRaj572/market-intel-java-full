package com.qode.marketintel.process;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.qode.marketintel.model.TweetRecord;

import java.nio.charset.StandardCharsets;

public class Deduplicator {
  private final BloomFilter<CharSequence> bloom;
  public Deduplicator(int expected){ this.bloom = BloomFilter.create(Funnels.stringFunnel(StandardCharsets.UTF_8), expected, 0.01); }
  public boolean isNew(TweetRecord t){
    String key = t.id()+"|"+Integer.toHexString(t.text().hashCode());
    if (bloom.mightContain(key)) return false;
    bloom.put(key);
    return true;
  }
}
