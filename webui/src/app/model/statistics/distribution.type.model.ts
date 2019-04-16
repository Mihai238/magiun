export enum Distribution {
  UNKNOWN = " ",
  NORMAL_DISTRIBUTION = "Normal",
  GAMMA_DISTRIBUTION = "Gamma",
  BINOMIAL_DISTRIBUTION = "Binomial",
  BERNOULLI_DISTRIBUTION = "Bernoulli",
  UNIFORM_DISTRIBUTION = "Uniform",
  EXPONENTIAL_DISTRIBUTION = "Exponential",
  POISSON_DISTRIBUTION = "Poisson",
  MULTINOMIAL_DISTRIBUTION = "Multinomial"

}

export namespace Distribution {

  export function values(d :Distribution): string[] {
    return Object.keys(Distribution).filter(
      (type) => {
        return !(d != null && d.toString().toUpperCase() != "UNKNOWN" && type == "UNKNOWN") &&
          isNaN(<any>type) && type !== 'values' && type !== `value` && type !== `isNullOrUnknown` && type !== `isDiscrete`;
      }
    );
  }

  export function value(d: string): Distribution {
    if (d == Distribution.UNKNOWN.valueOf()) {
      return Distribution.UNKNOWN;
    } else if (d == Distribution.NORMAL_DISTRIBUTION.valueOf()) {
      return Distribution.NORMAL_DISTRIBUTION;
    } else if (d == Distribution.GAMMA_DISTRIBUTION.valueOf()) {
      return Distribution.GAMMA_DISTRIBUTION;
    } else if (d == Distribution.BINOMIAL_DISTRIBUTION.valueOf()) {
      return Distribution.BINOMIAL_DISTRIBUTION;
    } else if (d == Distribution.BERNOULLI_DISTRIBUTION.valueOf()) {
      return Distribution.BERNOULLI_DISTRIBUTION;
    } else if (d == Distribution.UNIFORM_DISTRIBUTION.valueOf()) {
      return Distribution.UNIFORM_DISTRIBUTION;
    } else if (d == Distribution.EXPONENTIAL_DISTRIBUTION.valueOf()) {
      return Distribution.EXPONENTIAL_DISTRIBUTION;
    } else if (d == Distribution.POISSON_DISTRIBUTION.valueOf()) {
      return Distribution.POISSON_DISTRIBUTION;
    } else if (d == Distribution.MULTINOMIAL_DISTRIBUTION.valueOf()) {
      return Distribution.MULTINOMIAL_DISTRIBUTION;
    } else {
      throw new RangeError(`Unknown distribution ${d}`)
    }
  }

  export function isNullOrUnknown(d: Distribution): boolean {
    return d == null || d == Distribution.UNKNOWN || Distribution[d.toString().toUpperCase()] == Distribution.UNKNOWN;
  }

  export function isDiscrete(d: Distribution): boolean {
    return d == Distribution.BERNOULLI_DISTRIBUTION || d == Distribution.BINOMIAL_DISTRIBUTION || d == Distribution.POISSON_DISTRIBUTION
  }
}
