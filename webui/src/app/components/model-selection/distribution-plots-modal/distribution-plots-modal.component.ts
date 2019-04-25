import {AfterViewInit, Component} from "@angular/core";
import {Column} from "../../../model/data-set.model";
import {DialogComponent, DialogService} from 'ng2-bootstrap-modal'
import {NormalDistribution} from "../../../model/statistics/normal.distribution.model";
import {StatisticsUtils} from "../../../util/statistics.utils";
import {ExponentialDistribution} from "../../../model/statistics/exponential.distribution.model";
import {UniformDistribution} from "../../../model/statistics/uniform.distribution.model";
import {PlotsUtil} from "../../../util/plots.util";

declare var Plotly: any;

export interface DistributionPlotsModal {
  column: Column
  data: number[]
}

@Component({
  selector: 'app-distribution-plots-modal',
  templateUrl: './distribution-plots-modal.component.html',
  styleUrls: ['./distribution-plots-modal.component.scss']
})
export class DistributionPlotsModalComponent extends DialogComponent<DistributionPlotsModal, [Column, number[]]> implements DistributionPlotsModal, AfterViewInit {

  private static PLOT_WIDTH = 450;
  private static PLOT_HEIGHT = 350;
  column: Column;
  data: number[];
  normalDistribution: NormalDistribution;
  normalDistributed: number[];
  exponentialDistribution: ExponentialDistribution;
  exponentiallyDistributed: number[];
  uniformDistribution: UniformDistribution;
  uniformDistributed: number[];

  constructor(dialogService: DialogService) {
    super(dialogService);
  }

  ngAfterViewInit(): void {
    this.prepareNeededData();
    this.plotHistogram();
    this.plotBoxPlot();
    this.plotViolinPlot();
    this.qqPlots();
    this.ppPlots();
    this.cdPlots();
  }

  private prepareNeededData() {
    const n = this.data.length;
    this.data = this.data.sort((n1, n2) => n2 - n1);
    let mean = StatisticsUtils.mean(this.data);
    let sd  = StatisticsUtils.sd(this.data);

    this.normalDistribution = new NormalDistribution(mean, sd);
    this.normalDistributed = this.normalDistribution.sample(n).sort((n1, n2) => n2 - n1);

    this.exponentialDistribution = new ExponentialDistribution();
    this.exponentiallyDistributed = this.exponentialDistribution.sample(n).sort((n1, n2) => n2 - n1);

    this.uniformDistribution = new UniformDistribution();
    this.uniformDistributed = this.uniformDistribution.sample(n).sort((n1, n2) => n2 - n1);
  }

  private plotHistogram(): void {
    const histogramData = {
      x: this.data,
      type: 'histogram',
      histnorm: 'probability density',
      showlegend: false
    };

    const normalDistProbs = this.data.map(x => this.normalDistribution.pdf(x));
    const expDistProbs = this.data.map(x => this.exponentialDistribution.pdf(x));
    const uniDistProbs = this.data.map(x => this.uniformDistribution.pdf(x));

    const normalDistributionLine = {
      x: this.data,
      y: normalDistProbs,
      mode: 'lines',
      name: 'Normal Distribution',
      type: 'scatter',
      xaxis: this.column.name,
      yaxis: "Density",
      showlegend: false
    };

    const exponentialDistributionLine = {
      x: this.data,
      y: expDistProbs,
      mode: 'lines',
      name: 'Exponential Distribution',
      type: 'scatter',
      xaxis: this.column.name,
      yaxis: "Density",
      showlegend: false
    };

    const uniformDistributionLine = {
      x: this.data,
      y: uniDistProbs,
      mode: 'lines',
      name: 'Uniform Distribution',
      type: 'scatter',
      xaxis: this.column.name,
      yaxis: "Density",
      showlegend: false
    };

    const plotData = [histogramData, normalDistributionLine, exponentialDistributionLine, uniformDistributionLine];

    const layout = {
      height: DistributionPlotsModalComponent.PLOT_HEIGHT,
      width: DistributionPlotsModalComponent.PLOT_WIDTH,
      bargap: 0.05,
      xaxis: {title: this.column.name, autorange: true, zeroline: false},
      yaxis: {title: "Density", autorange: true, showticklabels: true},
      title: "Histogram"
    };

    this.plot("histogram", plotData, layout)
  }

  private plotBoxPlot(): void {
    const trace = {
      y: this.data,
      type: 'box',
      name: this.column.name
    };

    const plotData = [trace];

    const layout = {height: DistributionPlotsModalComponent.PLOT_HEIGHT, width: DistributionPlotsModalComponent.PLOT_WIDTH, title: "Boxplot"};
    this.plot("boxPlot", plotData, layout)
  }



  private plotViolinPlot(): void {
    const trace = {
      type: 'violin',
      y: this.data,
      name: this.column.name,
      points: 'none',
      box: {
        visible: true
      },
      meanline: {
        visible: true
      }
    };

    const layout = {height: DistributionPlotsModalComponent.PLOT_HEIGHT, width: DistributionPlotsModalComponent.PLOT_WIDTH, title: "Violin Plot"};
    this.plot("violinPlot", [trace], layout)
  }

  private qqPlots(): void {
    this.qqPlot(this.normalDistributed, this.data, "Normal Q-Q Plot", "normalQQPlot");
    this.qqPlot(this.exponentiallyDistributed, this.data, "Exponential Q-Q Plot", "expQQPlot");
    this.qqPlot(this.uniformDistributed, this.data, "Uniform Q-Q Plot", "uniQQPlot");
  }

  private qqPlot(xdata: number[], ydata: number[], title: string, target: string): void {
    const trace = {
      x: xdata,
      y: ydata,
      type: 'scatter',
      mode: "markers",
      name: this.column.name,
      marker: {
        size: 4
      }
    };

    const coordinates = PlotsUtil.calculateLineCoordinates(ydata, xdata);

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
      height: DistributionPlotsModalComponent.PLOT_HEIGHT,
      width: DistributionPlotsModalComponent.PLOT_WIDTH,
      title: title,
      xaxis: {
        showgrid: true,
        showline: true,
        title: 'Theoretical Quantiles'
      },
      yaxis: {
        nticks: 11,
        showgrid: true,
        showline: true,
        title: "Sample Quantiles"
      },
      shapes: [straightLine]
    };

    this.plot(target, plotData, layout)
  }

  private ppPlots(): void {
    let normalEmpiricalCD = this.data.map(x => this.normalDistribution.cdf(x)).sort((n1, n2) => n2 - n1);
    let normDistTheoreticalCD = this.normalDistributed.map(x => this.normalDistribution.cdf(x)).sort((n1, n2) => n2- n1);
    let expEmpiricalCD = this.data.map(x => this.exponentialDistribution.cdf(x)).sort((n1, n2) => n2 - n1);
    let expDistTheoreticalCD = this.exponentiallyDistributed.map(x => this.exponentialDistribution.cdf(x)).sort((n1, n2) => n2- n1);
    let uniEmpiricalCD = this.data.map(x => this.exponentialDistribution.cdf(x)).sort((n1, n2) => n2 - n1);
    let uniDistTheoreticalCD = this.uniformDistributed.map(x => this.uniformDistribution.cdf(x)).sort((n1, n2) => n2- n1);

    this.ppPlot(normDistTheoreticalCD, normalEmpiricalCD, "Normal P-P Plot", "normalPPPlot");
    this.ppPlot(expDistTheoreticalCD, expEmpiricalCD, "Exponential P-P Plot", "expPPPlot");
    this.ppPlot(uniDistTheoreticalCD, uniEmpiricalCD, "Uniform P-P Plot", "uniPPPlot");
  }

  private ppPlot(xdata: number[], ydata:number[], title: string, target: string): void {
    const trace = {
      x: xdata,
      y: ydata,
      type: 'scatter',
      mode: "markers",
      name: this.column.name,
      marker: {
        size: 4
      }
    };

    const coordinates = PlotsUtil.calculateLineCoordinates(ydata, xdata);

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
      height: DistributionPlotsModalComponent.PLOT_HEIGHT,
      width: DistributionPlotsModalComponent.PLOT_WIDTH,
      title: title,
      xaxis: {
        showgrid: true,
        showline: true,
        title: 'Theoretical Cumulative Distribution'
      },
      yaxis: {
        nticks: 11,
        showgrid: true,
        showline: true,
        title: "Empirical Cumulative Distribution"
      },
      shapes: [straightLine]
    };

    this.plot(target, plotData, layout)
  }

  private cdPlots(): void {
    let normalEmpiricalCD = this.data.map(x => this.normalDistribution.cdf(x)).sort((n1, n2) => n2 - n1);
    let expEmpiricalCD = this.data.map(x => this.exponentialDistribution.cdf(x)).sort((n1, n2) => n2 - n1);
    let uniEmpiricalCD = this.data.map(x => this.uniformDistribution.cdf(x)).sort((n1, n2) => n2 - n1);

    this.cdPlot(this.data, normalEmpiricalCD, "Normal Cumulative Distribution", "normalCDPlot");
    this.cdPlot(this.data, expEmpiricalCD, "Exponential Cumulative Distribution", "expCDPlot");
    this.cdPlot(this.data, uniEmpiricalCD, "Uniform Cumulative Distribution", "uniCDPlot");
  }

  private cdPlot(xdata: number[], ydata: number[], title: string, target: string): void {
    const trace = {
      x: xdata,
      y: ydata,
      type: 'scatter',
      mode: "markers",
      name: this.column.name,
      marker: {
        size: 4
      }
    };

    const plotData = [trace];

    const layout = {
      height: DistributionPlotsModalComponent.PLOT_HEIGHT,
      width: DistributionPlotsModalComponent.PLOT_WIDTH,
      title: title,
      xaxis: {
        showgrid: true,
        showline: true,
        title: this.column.name
      },
      yaxis: {
        nticks: 11,
        showgrid: true,
        showline: true,
        title: "CDF"
      }
    };

    this.plot(target, plotData, layout)
  }

  private plot(target: String, data: any[], layout: any): void {
    Plotly.newPlot(target, data, layout)
  }
}
