package com.qode.marketintel.util;

public class RateLimiter {
  private final int maxPerSec;
  public RateLimiter(int maxPerSec){ this.maxPerSec = maxPerSec; }
  public void sleepJitter(int minMs, int maxMs){
    try { Thread.sleep(minMs + (int)(Math.random()*(maxMs-minMs))); } catch (InterruptedException ignored) {}
  }
}
