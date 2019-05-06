import {Algorithm} from "../algorithm/algorithm.model";

export interface RecommenderResponse {
  requestId: string
  recommendations: Algorithm[]
  message: string
}
