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

  export function values(d :Distribution) {
    return Object.keys(Distribution).filter(
      (type) => {
        return !(d != null && type == "UNKNOWN") && isNaN(<any>type) && type !== 'values';
      }
    );
  }
}
