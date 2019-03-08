export function erfc(x) {
  const z = Math.abs(x);
  const t = 1 / (1 + z / 2);
  const r = t * Math.exp(-z * z - 1.26551223 + t * (1.00002368 +
    t * (0.37409196 + t * (0.09678418 + t * (-0.18628806 +
      t * (0.27886807 + t * (-1.13520398 + t * (1.48851587 +
        t * (-0.82215223 + t * 0.17087277)))))))));
  return x >= 0 ? r : 2 - r;
}

export function erf(x){
  // erf(x) = 2/sqrt(pi) * integrate(from=0, to=x, e^-(t^2) ) dt
  // with using Taylor expansion,
  //        = 2/sqrt(pi) * sigma(n=0 to +inf, ((-1)^n * x^(2n+1))/(n! * (2n+1)))
  // calculationg n=0 to 50 bellow (note that inside sigma equals x when n = 0, and 50 may be enough)

  let m = 1.00;
  let s = 1.00;
  let sum = x * 1.0;

  for(let i = 1; i < 50; i++){
    m *= i;
    s *= -1;
    sum += (s * Math.pow(x, 2.0 * i + 1.0)) / (m * (2.0 * i + 1.0));
  }
  return 2 * sum / Math.sqrt(Math.PI);
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
    return 0.5 * (1 + erf((x - this.mean)/(Math.sqrt(2) * this.sd)))
  }
}
