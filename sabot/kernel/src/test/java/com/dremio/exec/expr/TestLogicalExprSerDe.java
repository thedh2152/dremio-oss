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
package com.dremio.exec.expr;

import com.dremio.BaseTestQuery;
import com.dremio.exec.ExecConstants;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test LogicalExpressions are serialized and deserialized properly when query is planned into
 * multiple fragments.
 */
public class TestLogicalExprSerDe extends BaseTestQuery {

  @BeforeClass
  public static void setSliceCount() throws Exception {
    // Set the slice count to low, so that query is divided into multiple fragments with exchanges.
    test("ALTER SESSION SET \"planner.slice_target\"=1;");
  }

  @Test // DRILL-2606
  public void castToBit() throws Exception {
    testBuilder()
        .sqlQuery(
            "SELECT CAST(CAST('true' as VARCHAR(20)) AS BOOLEAN) c1 FROM cp.\"region.json\" ORDER BY \"region_id\" LIMIT 1")
        .unOrdered()
        .baselineColumns("c1")
        .baselineValues(true)
        .go();
  }

  // TODO: Need to add better coverage for ExprParser

  @AfterClass
  public static void resetSliceCount() throws Exception {
    // Set the slice count to low, so that query is divided into multiple fragments with exchanges.
    test(
        String.format(
            "ALTER SESSION SET \"planner.slice_target\"=%d;", ExecConstants.SLICE_TARGET_DEFAULT));
  }
}
