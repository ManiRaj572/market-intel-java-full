package com.qode.marketintel.collect;

import com.microsoft.playwright.Page;
import com.qode.marketintel.model.TweetRecord;

import java.time.Instant;
import java.util.*;

public class HtmlTweetParser {
  public static List<TweetRecord> extract(Page page) {
    var items = page.querySelectorAll("article div[data-testid='tweet']");
    List<TweetRecord> out = new ArrayList<>(items.size());
    for (var el : items) {
      try {
        String id = Optional.ofNullable(el.getAttribute("data-tweet-id")).orElse(UUID.randomUUID().toString());
        String user = Optional.ofNullable(el.querySelector("a[href^='/']")).map(e->e.innerText()).orElse("?");
        String text = Optional.ofNullable(el.querySelector("div[lang]")).map(e->e.innerText()).orElse("");
        String ts = Optional.ofNullable(el.querySelector("time")).map(e->e.getAttribute("datetime")).orElse(null);
        long epoch = ts!=null ? Instant.parse(ts).toEpochMilli() : System.currentTimeMillis();
        int like = parseIntSafe(el, "div[data-testid='like'] span");
        int rt = parseIntSafe(el, "div[data-testid='retweet'] span");
        int rp = parseIntSafe(el, "div[data-testid='reply'] span");
        int qt = parseIntSafe(el, "div[data-testid='unlike'] span");

        var hashtags = parseTokens(text, '#');
        var mentions = parseTokens(text, '@');
        var lang = "auto"; // could improve via ICU4J detection

        out.add(new TweetRecord(id, user, epoch, text, like, rt, rp, qt, mentions, hashtags, lang, System.currentTimeMillis()));
      } catch (Throwable ignore) {}
    }
    return out;
  }

  private static int parseIntSafe(com.microsoft.playwright.ElementHandle base, String css){
    try { var t = base.querySelector(css); return t==null?0:Integer.parseInt(t.innerText().replaceAll("[^0-9]","")); } catch (Exception e){ return 0; }
  }

  private static List<String> parseTokens(String text, char prefix){
    var list = new ArrayList<String>();
    for (String w : text.split("\\s+")) if (w.length()>1 && w.charAt(0)==prefix) list.add(w.replaceAll("[^\\p{Alnum}_]",""));
    return list;
  }
}
