export enum Distribution {
  UNKNOWN = " ",
  NORMAL_DISTRIBUTION = "Normal Distribution",
  GAMMA_DISTRIBUTION = "Gamma Distribution",
  BINOMIAL_DISTRIBUTION = "Binomial Distribution",
  BERNOULLI_DISTRIBUTION = "Bernoulli Distribution",
  UNIFORM_DISTRIBUTION = "Uniform Distribution",
  EXPONENTIAL_DISTRIBUTION = "Exponential Distribution",
  POISSON_DISTRIBUTION = "Poisson Distribution",
  MULTINOMIAL_DISTRIBUTION = "Multinomial Distribution"

}

export namespace Distribution {

  export function values(d :Distribution) {
    return Object.keys(Distribution).filter(
      (type) => {
        return !(d != null && type == "UNKNOWN") && isNaN(<any>type) && type !== 'values';
      }
    );
  }
}
