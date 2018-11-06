export interface Recommendations {
  map: Map<number, Recommendation>
}

export interface Recommendation {
  colTypes: string[];
  operations: string[];
}

