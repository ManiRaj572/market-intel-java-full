package com.qode.marketintel.util;

import com.microsoft.playwright.BrowserContext;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.nio.file.Path;

public class Env {
  public static void loadCookies(BrowserContext ctx) {
    try {
      var f = Path.of(".env.cookies.json").toFile();
      if (!f.exists()) return;
      ObjectMapper om = new ObjectMapper();
      var arr = om.readTree(f);
      // Playwright cookie wiring left simple for user to adapt.
      // Real code should convert JSON cookie objects to Playwright's Cookie objects and add via ctx.addCookies(...)
    } catch (Exception e) {
      // ignore - user must provide cookies manually
    }
  }
}
