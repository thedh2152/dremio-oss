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

package com.dremio.common.util;

public class DecimalScalePrecisionModFunction extends ScalePrecisionFunctionBase {

  public DecimalScalePrecisionModFunction(
      int leftPrecision, int leftScale, int rightPrecision, int rightScale) {
    super(leftPrecision, leftScale, rightPrecision, rightScale);
  }

  @Override
  public void computeScalePrecision(
      int leftPrecision, int leftScale, int rightPrecision, int rightScale) {

    // compute the output scale and precision here
    outputScale = Math.max(leftScale, rightScale);
    int leftIntegerDigits = leftPrecision - leftScale;

    outputPrecision = CoreDecimalUtility.getPrecisionRange(outputScale + leftIntegerDigits);

    if (outputScale + leftIntegerDigits > outputPrecision) {
      outputScale = outputPrecision - leftIntegerDigits;
    }

    // Output precision should atleast be greater or equal to the input precision
    outputPrecision = Math.max(outputPrecision, Math.max(leftPrecision, rightPrecision));
  }
}
