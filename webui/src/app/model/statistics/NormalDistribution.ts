export function erfc(x) {
  const z = Math.abs(x);
  const t = 1 / (1 + z / 2);
  const r = t * Math.exp(-z * z - 1.26551223 + t * (1.00002368 +
    t * (0.37409196 + t * (0.09678418 + t * (-0.18628806 +
      t * (0.27886807 + t * (-1.13520398 + t * (1.48851587 +
        t * (-0.82215223 + t * 0.17087277)))))))));
  return x >= 0 ? r : 2 - r;
}

export class NormalDistribution {

  private DOUBLE_PI = Math.PI * 2;

  constructor(private mean: number = 0, private sd: number = 1) {
  }

  sample(size: number): number[] {
    var sample: number[] = [];

    for (let i = 0; i < size; i++) {
      const r1 = Math.random();
      const r2 = Math.random();

      let z0 = Math.sqrt(-2.0 * Math.log(r1)) * Math.cos(this.DOUBLE_PI * r2);

      sample[i] = z0 * this.sd + this.mean;
    }

    return sample;
  }

  cdf(x: number): number {
    return 0.5 * erfc(-(x - this.mean) / (this.sd * Math.sqrt(2)));
  }
}
