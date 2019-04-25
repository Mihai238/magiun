import {StatisticsUtils} from "./statistics.utils";

export class PlotsUtil {

  public static calculateLineCoordinates(sample: number[], theoretical: number[]): [number, number, number, number] {
    const dx = StatisticsUtils.percentile(theoretical, 75) - StatisticsUtils.percentile(theoretical, 25);
    const dy = StatisticsUtils.percentile(sample, 75) - StatisticsUtils.percentile(sample, 25);

    const b = dy / dx;

    const xc = (StatisticsUtils.percentile(theoretical, 25) + StatisticsUtils.percentile(theoretical, 75)) / 2;
    const yc = (StatisticsUtils.percentile(sample, 25) + StatisticsUtils.percentile(sample, 75)) / 2;

    const xmax = Math.max.apply(Math, theoretical);
    const xmin = Math.min.apply(Math, theoretical);
    const ymax = yc + b * (xmax - xc);
    const ymin = yc - b * (xc - xmin);

    return [xmin, ymin, xmax, ymax]
  }

}
