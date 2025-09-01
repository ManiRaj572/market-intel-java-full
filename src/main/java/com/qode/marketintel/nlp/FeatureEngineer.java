package com.qode.marketintel.nlp;

import com.qode.marketintel.model.TweetRecord;
import java.util.Map;

public class FeatureEngineer {
  public static Map<String,Double> features(TweetRecord t){
    double e = 1 + t.likeCount() + 2.0*t.retweetCount() + 0.5*t.replyCount() + 1.5*t.quoteCount();
    double len = Math.min(1.0, t.text().length()/280.0);
    double hashCt = t.hashtags().size();
    return Map.of(
      "engagement", Math.log(e),
      "length", len,
      "hashtag_ct", hashCt
    );
  }
}
