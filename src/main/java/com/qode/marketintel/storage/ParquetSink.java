package com.qode.marketintel.storage;

import com.qode.marketintel.model.TweetRecord;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import java.io.*;
import java.nio.file.Path;

public class ParquetSink implements AutoCloseable {
  private final ParquetWriter<GenericRecord> writer;
  private final Schema schema;

  public ParquetSink(Path out) throws IOException {
    this.schema = new Schema.Parser().parse(new File("schemas/tweet.avsc"));
    this.writer = AvroParquetWriter.<GenericRecord>builder(new org.apache.hadoop.fs.Path(out.toUri()))
      .withSchema(schema)
      .withCompressionCodec(CompressionCodecName.SNAPPY)
      .withPageSize(128*1024)
      .withRowGroupSize(64L*1024*1024)
      .build();
  }

  public void write(TweetRecord t) throws IOException {
    GenericRecord r = new GenericData.Record(schema);
    r.put("id", t.id());
    r.put("username", t.username());
    r.put("timestamp", t.timestamp());
    r.put("text", t.text());
    r.put("likeCount", t.likeCount());
    r.put("retweetCount", t.retweetCount());
    r.put("replyCount", t.replyCount());
    r.put("quoteCount", t.quoteCount());
    r.put("mentions", t.mentions());
    r.put("hashtags", t.hashtags());
    r.put("lang", t.lang());
    r.put("collectedAt", t.collectedAt());
    writer.write(r);
  }

  @Override public void close() throws IOException { writer.close(); }
}
