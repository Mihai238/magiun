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


}
