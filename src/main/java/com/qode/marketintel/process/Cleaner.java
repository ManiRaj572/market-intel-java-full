package com.qode.marketintel.process;

import com.ibm.icu.text.Transliterator;
import com.qode.marketintel.model.TweetRecord;

public class Cleaner {
  private static final Transliterator NFKC = Transliterator.getInstance("Any-NFKC");
  private static final Transliterator REMOVE_CTRL = Transliterator.getInstance("[\\u0000-\\u001F] Remove");

  public TweetRecord clean(TweetRecord t){
    String norm = NFKC.transliterate(t.text());
    norm = REMOVE_CTRL.transliterate(norm).trim();
    norm = norm.replaceAll("\\n+"," ").replaceAll("\\s+"," ");
    return new TweetRecord(
      t.id(), t.username(), t.timestamp(), norm,
      t.likeCount(), t.retweetCount(), t.replyCount(), t.quoteCount(),
      t.mentions(), t.hashtags(), t.lang(), t.collectedAt());
  }
}
