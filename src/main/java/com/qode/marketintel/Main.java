package com.qode.marketintel;

import com.qode.marketintel.collect.XScraper;
import com.qode.marketintel.model.TweetRecord;
import com.qode.marketintel.process.Cleaner;
import com.qode.marketintel.process.Deduplicator;
import com.qode.marketintel.storage.ParquetSink;
import com.qode.marketintel.viz.Plotter;

import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

public class Main {
  public static void main(String[] args) throws Exception {
    var out = Path.of("data/curated/tweets.parquet");
    var hashtags = List.of("#nifty50", "#sensex", "#intraday", "#banknifty");

    try (var sink = new ParquetSink(out)) {
      var dedup = new Deduplicator(100_000);
      var cleaner = new Cleaner();
      var scraper = new XScraper(Duration.ofMinutes(30));

      List<TweetRecord> batch = scraper.scrapeRecent(hashtags, 2_200); // target > 2000
      for (var t : batch) {
        var ct = cleaner.clean(t);
        if (dedup.isNew(ct)) {
          sink.write(ct);
        }
      }
    }

    // Minimal memory viz example
    Plotter.plotHourlyCounts(Path.of("data/curated/tweets.parquet"), Path.of("data/plots/hourly_counts.png"));
  }
}
