package com.qode.marketintel.viz;

import org.knowm.xchart.*;
import java.nio.file.Path;
import java.util.*;

public class Plotter {
  public static void plotHourlyCounts(Path parquet, Path outPng) throws Exception {
    Map<String,Integer> counts = Sampler.hourlyCounts(parquet);
    List<String> x = new ArrayList<>(counts.keySet());
    Collections.sort(x);
    List<Integer> y = x.stream().map(counts::get).toList();
    var chart = new XYChartBuilder().width(900).height(400).title("Hourly Tweet Counts").xAxisTitle("Hour").yAxisTitle("Tweets").build();
    chart.addSeries("tweets", x, y);
    BitmapEncoder.saveBitmap(chart, outPng.toString(), BitmapEncoder.BitmapFormat.PNG);
  }
}
