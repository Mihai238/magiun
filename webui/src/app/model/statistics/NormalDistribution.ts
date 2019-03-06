export class NormalDistribution {

  private DOUBLE_PI = Math.PI * 2;

  constructor(private mean: number = 0, private sd: number = 1) {
  }

  sample(size: number): number[] {
    var sample: number[] = [];

    for (let i = 0; i < size; i ++) {
      const r1 = Math.random();
      const r2 = Math.random();

      let z0 = Math.sqrt(-2.0 * Math.log(r1)) * Math.cos(this.DOUBLE_PI * r2);

      sample[i] = z0 * this.sd + this.mean;
    }

    return sample;
  }

}
