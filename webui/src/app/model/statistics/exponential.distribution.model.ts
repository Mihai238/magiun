import {Distribution} from "./distribution.model";
import {StatisticsUtils} from "../../util/statistics.utils";
import * as assert from "assert";

/**
 * @see https://en.wikipedia.org/wiki/Exponential_distribution
 */
export class ExponentialDistribution implements Distribution {

  constructor(private rate: number = 1) {
    assert(rate > 0, `Expected rate > 0, but got ${rate}`)
  }

  cdf(x: number): number {
    if (x < 0) {
      return 0;
    } else {
      return 1 - Math.pow(Math.E, -1 * this.rate * x);
    }
  }

  pdf(x: number): number {
    if (x < 0) {
      return 0;
    } else {
      return this.rate * Math.pow(Math.E, -1 * this.rate * x);
    }
  }

  sample(size: number): number[] {
    const sample: number[] = [];

    for (let i = 0; i < size; i++) {
      sample[i] = Math.log(1 - StatisticsUtils.getRandomNumber(0, 0.999)) / (-1 * this.rate);
    }

    return sample;
  }

}
