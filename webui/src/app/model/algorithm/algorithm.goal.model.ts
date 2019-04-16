export interface AlgorithmGoal {
  readonly displayName: string;
  readonly value: string;
}

export const GoalRegression: AlgorithmGoal = <AlgorithmGoal> {
  displayName: "Regression",
  value: "GoalRegression"
};

export const GoalClassification: AlgorithmGoal = <AlgorithmGoal> {
  displayName: "Classification",
  value: "GoalClassification"
};
