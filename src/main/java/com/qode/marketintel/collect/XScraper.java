package com.qode.marketintel.collect;

import com.microsoft.playwright.*;
import com.qode.marketintel.model.TweetRecord;
import com.qode.marketintel.util.Env;
import com.qode.marketintel.util.RateLimiter;

import java.time.*;
import java.util.*;

public class XScraper implements AutoCloseable {
  private final Duration perQueryBudget;
  private final RateLimiter limiter = new RateLimiter(8); // max ~8 actions/sec overall

  public XScraper(Duration perQueryBudget) { this.perQueryBudget = perQueryBudget; }

  public List<TweetRecord> scrapeRecent(List<String> hashtags, int target) {
    try (Playwright pw = Playwright.create()) {
      Browser browser = pw.chromium().launch(new BrowserType.LaunchOptions()
          .setHeadless(true)
          .setArgs(List.of("--disable-blink-features=AutomationControlled")));

      BrowserContext ctx = browser.newContext();
      // Load session cookie (user supplies in .env)
      Env.loadCookies(ctx);

      Page page = ctx.newPage();
      List<TweetRecord> out = new ArrayList<>();
      for (String tag : hashtags) {
        long t0 = System.currentTimeMillis();
        String url = "https://x.com/search?q=" + urlEncode(tag) + "&f=live";
        page.navigate(url);
        page.waitForLoadState(LoadState.NETWORKIDLE);

        while ((System.currentTimeMillis()-t0) < perQueryBudget.toMillis() && out.size() < target) {
          // Extract visible tweets from DOM
          out.addAll(HtmlTweetParser.extract(page));
          // Scroll to load more
          page.mouse().wheel(0, 1600);
          limiter.sleepJitter(120, 350);
        }
      }
      return out;
    }
  }

  private static String urlEncode(String s){return java.net.URLEncoder.encode(s, java.nio.charset.StandardCharsets.UTF_8);}
  @Override public void close() {}
}
