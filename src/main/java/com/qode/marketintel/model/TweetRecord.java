package com.qode.marketintel.model;

import java.util.List;

public record TweetRecord(
  String id,
  String username,
  long timestamp,
  String text,
  int likeCount,
  int retweetCount,
  int replyCount,
  int quoteCount,
  List<String> mentions,
  List<String> hashtags,
  String lang,
  long collectedAt
) {}
