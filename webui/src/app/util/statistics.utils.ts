import * as assert from "assert";

export class StatisticsUtils {

  public static percentile(data: number[], percentile: number): number {
    assert(percentile >=0 && percentile <= 100, `expected percentile between [0, 100], but got ${percentile}`);
    const sortedData = data.sort((n1, n2) => n2 - n1);

    const index = (percentile/100) * sortedData.length;
    if (Math.floor(index) == index) {
      return (data[index - 1] + data[index])/2
    } else {
      return data[Math.floor(index)]
    }
  }

  public static mean(data: number[]): number {
    const s = data.filter(v => v != null && !Number.isNaN(v) && Number.isFinite(v))
      .reduce((s, v) => {return s + v}, 0);

    return s/data.length;
  }

  public static sd(data: number[], mean: number = null): number {
    data = data.filter(v => v != null && !Number.isNaN(v) && Number.isFinite(v));
    if (mean == null) {
      mean = this.mean(data);
    }

    return Math.sqrt(this.mean(this.squareDiffs(data, mean)));
  }

  private static squareDiffs(data: number[], mean: number): number[] {
    return data.map(v => Math.pow(v - mean, 2))
  }

  public static getRandomNumber(min: number, max: number) {
    return Math.random() * (max - min) + min;
  }

}
