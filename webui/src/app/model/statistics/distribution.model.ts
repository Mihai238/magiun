export interface Distribution {

  sample(size: number): number[];
  cdf(x: number): number;
  pdf(x: number): number;
}
