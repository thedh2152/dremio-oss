/*
 * Copyright (C) 2017-2019 Dremio Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dremio.exec.store.parquet;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import org.apache.arrow.memory.ArrowBuf;
import org.apache.parquet.bytes.BytesInput;
import org.apache.parquet.format.PageHeader;
import org.apache.parquet.format.Util;
import org.apache.parquet.io.SeekableInputStream;

public class ColumnDataReader {
  static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ColumnDataReader.class);

  private final long endPosition;
  public final SeekableInputStream input;

  public ColumnDataReader(SeekableInputStream input, long start, long length) throws IOException {
    this.input = input;
    this.input.seek(start);
    this.endPosition = start + length;
  }

  public PageHeader readPageHeader() throws IOException {
    return Util.readPageHeader(input);
  }

  public SeekableInputStream getInputStream() {
    return input;
  }

  public BytesInput getPageAsBytesInput(int pageLength) throws IOException {
    byte[] b = new byte[pageLength];
    input.read(b);
    return new HadoopBytesInput(b);
  }

  public void loadPage(ArrowBuf target, int pageLength) throws IOException {
    target.clear();
    ByteBuffer directBuffer = target.nioBuffer(0, pageLength);
    input.readFully(directBuffer);
    target.writerIndex(pageLength);
  }

  public void clear() {
    try {
      input.close();
    } catch (IOException ex) {
      logger.warn("Error while closing input stream.", ex);
    }
  }

  public boolean hasRemainder() throws IOException {
    return input.getPos() < endPosition;
  }

  public class HadoopBytesInput extends BytesInput {

    private final byte[] pageBytes;

    public HadoopBytesInput(byte[] pageBytes) {
      super();
      this.pageBytes = pageBytes;
    }

    @Override
    public byte[] toByteArray() throws IOException {
      return pageBytes;
    }

    @Override
    public long size() {
      return pageBytes.length;
    }

    @Override
    public void writeAllTo(OutputStream out) throws IOException {
      out.write(pageBytes);
    }
  }
}
