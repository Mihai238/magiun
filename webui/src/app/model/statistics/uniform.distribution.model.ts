import {Distribution} from "./distribution.model";
import * as assert from "assert";

/**
 * @see https://en.wikipedia.org/wiki/Uniform_distribution_(continuous)
 */
export class UniformDistribution implements Distribution {

  /**
   * @param lower Lower bound of this distribution (inclusive).
   * @param upper Upper bound of this distribution (exclusive).
   * lower < upper
   */
  constructor(private lower: number = 0, private upper: number = 1) {
    assert(lower < upper, `Expected lowe < upper, but got lower: ${lower} and upper: ${upper}`)
  }
  
  cdf(x: number): number {
    if (x < this.lower) {
      return 0;
    } else if (x >= this.upper) {
      return 1;
    } else {
      return (x - this.lower) / (this.upper - x);
    }
  }

  pdf(x: number): number {
    if (x >= this.lower && x <= this.upper) {
      return 1 / (this.upper - this.lower);
    } else {
      return 0;
    }
  }

  sample(size: number): number[] {
    const data: number[] = [];
    for (let i = 0; i < size; i++) {
      const r = Math.random();
      data[i] = r * this.upper + (1 - r) * this.lower;
    }

    return data;
  }

}
