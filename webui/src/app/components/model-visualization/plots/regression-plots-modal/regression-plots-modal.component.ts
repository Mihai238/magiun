import {AfterViewInit, Component} from "@angular/core";
import {DialogComponent, DialogService} from "ng2-bootstrap-modal";
import {NormalDistribution} from "../../../../model/statistics/normal.distribution.model";
import {StatisticsUtils} from "../../../../util/statistics.utils";
import {PlotsUtil} from "../../../../util/plots.util";

declare var Plotly: any;

export interface RegressionPlotsModal {
  residuals: number[]
  fittedValues: number[]
  dataSample: number[]
}

@Component({
  selector: 'app-regression-plots-modal',
  templateUrl: './regression-plots-modal.component.html',
  styleUrls: ['./regression-plots-modal.component.scss']
})
export class RegressionPlotsModalComponent extends DialogComponent<RegressionPlotsModal, [number[], number[], number[]]> implements RegressionPlotsModal, AfterViewInit {

  private static PLOT_WIDTH = 450;
  private static PLOT_HEIGHT = 350;

  residuals: number[];
  fittedValues: number[];
  dataSample: number[];
  standardizedResiduals: number[];
  sqrtStandardizedResiduals: number[];

  constructor(dialogService: DialogService) {
    super(dialogService);
  }


  ngAfterViewInit(): void {
    this.prepareData();
    this.plotBoxPlot();
    this.scatterPlots();
  }

  private prepareData() {
    let residualsSd = StatisticsUtils.sd(this.residuals);
    this.standardizedResiduals = this.residuals.map(r => r/residualsSd);
    this.sqrtStandardizedResiduals = this.standardizedResiduals.map(r => Math.sqrt(r));
  }

  private plotBoxPlot() {
    const trace = {
      y: this.residuals,
      type: 'box',
      name: 'residuals'
    };

    const plotData = [trace];

    const layout = {height: RegressionPlotsModalComponent.PLOT_HEIGHT, width: RegressionPlotsModalComponent.PLOT_WIDTH, title: "Residuals Boxplot"};
    this.plot("residualsBoxPlot", plotData, layout);
  }

  private scatterPlots() {

    let normalDistributedByResiduals = this.getSortedNormalDistributedData(this.residuals);
    let sortedResiduals = this.residuals.sort((n1, n2) => n2 - n1);

    let normalDistributedByStandardizedResiduals = this.getSortedNormalDistributedData(this.standardizedResiduals);
    let sortedStandardizedResiduals = this.standardizedResiduals.sort((n1, n2) => n2 - n1);

    this.scatterPlotWithLine(normalDistributedByResiduals, sortedResiduals, "Normal Q-Q Plot", "Theoretical Quantiles", "Residuals", "residualsQQPlot");
    this.scatterPlotWithLine(normalDistributedByStandardizedResiduals, sortedStandardizedResiduals, "Normal Q-Q Plot", "Theoretical Quantiles", "Standardized Residuals", "standardizedResidualsQQPlot");
    this.scatterPlotWithLine(this.fittedValues, this.residuals, "Residuals vs Fitted", "Fitted Values", "Residuals", "residualsVsFittedPlot");
    this.scatterPlotWithLine(this.fittedValues, this.standardizedResiduals, "Standardized Residuals vs Fitted", "Fitted Values", "Standardized Residuals", "standardizedResidualsVsFittedPlot");
    this.scatterPlotWithLine(this.fittedValues, this.dataSample, "Actual vs Fitted", "Fitted Values", "Actual Values", "actualVsFittedPlot");
  }

  private scatterPlotWithLine(xData: number[], yData: number[],  title: string, xTitle: string, yTitle: string, target: string) {
    const trace = {
      x: xData,
      y: yData,
      type: 'scatter',
      mode: "markers",
      marker: {
        size: 4
      }
    };

    const coordinates = PlotsUtil.calculateLineCoordinates(yData, xData);

    const straightLine = {
      x0: coordinates[0],
      y0: coordinates[1],
      x1: coordinates[2],
      y1: coordinates[3],
      type: "line",
      line: {
        color: 'rgb(139,0,0)',
        width: 3
      }
    };

    const plotData = [trace];

    const layout = {
      height: RegressionPlotsModalComponent.PLOT_HEIGHT,
      width: RegressionPlotsModalComponent.PLOT_WIDTH,
      title: title,
      xaxis: {
        showgrid: true,
        showline: true,
        title: xTitle
      },
      yaxis: {
        nticks: 11,
        showgrid: true,
        showline: true,
        title: yTitle
      },
      shapes: [straightLine]
    };

    this.plot(target, plotData, layout)
  }

  private plot(target: String, data: any[], layout: any): void {
    Plotly.newPlot(target, data, layout)
  }

  private getSortedNormalDistributedData(referenceData: number[]): number[] {
    let n = referenceData.length;
    let mean = StatisticsUtils.mean(referenceData);
    let sd  = StatisticsUtils.sd(referenceData);

    let normalDistribution = new NormalDistribution(mean, sd);
    return normalDistribution.sample(n).sort((n1, n2) => n2 - n1);
  }
}
