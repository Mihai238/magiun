import {AfterViewInit, Component} from "@angular/core";
import {Column} from "../../../model/data-set.model";
import {DialogComponent, DialogService} from 'ng2-bootstrap-modal'
import {NormalDistribution} from "../../../model/statistics/normal.distribution.model";
import {StatisticsUtils} from "../../../util/statistics.utils";

declare var Plotly: any;

export interface PlotsModal {
  column: Column
  data: number[]
}

@Component({
  selector: 'app-plots-modal',
  templateUrl: './plots-modal.component.html',
  styleUrls: ['./plots-modal.component.scss']
})
export class PlotsModalComponent extends DialogComponent<PlotsModal, [Column, number[]]> implements PlotsModal, AfterViewInit {

  private static PLOT_WIDTH = 450;
  private static PLOT_HEIGHT = 350;
  column: Column;
  data: number[];
  normalDistribution: NormalDistribution;
  normalDistributed: number[];

  constructor(dialogService: DialogService) {
    super(dialogService);
  }

  ngAfterViewInit(): void {
    this.prepareNeededData();
    this.plotHistogram();
    this.plotBoxPlot();
    this.plotViolinPlot();
    this.qqPlot();
    this.ppPlot();
    this.cdPlot();
  }

  private prepareNeededData() {
    this.data = this.data.sort((n1, n2) => n2 - n1);
    let mean = StatisticsUtils.mean(this.data);
    let sd  =StatisticsUtils.sd(this.data);

    this.normalDistribution = new NormalDistribution(mean, sd);
    this.normalDistributed = this.normalDistribution.sample(this.data.length).sort((n1, n2) => n2 - n1);
  }

  private plotHistogram(): void {
    const histogramData = {
      x: this.data,
      type: 'histogram',
      histnorm: 'probability density',
      showlegend: false
    };

    const probs = this.data.map(x => this.normalDistribution.pdf(x));
    const normalDistributionLine = {
      x: this.data,
      y: probs,
      mode: 'lines',
      name: 'Normal Distribution',
      type: 'scatter',
      xaxis: this.column.name,
      yaxis: "Density"
    };

    const plotData = [histogramData, normalDistributionLine];

    const layout = {
      height: PlotsModalComponent.PLOT_HEIGHT,
      width: PlotsModalComponent.PLOT_WIDTH,
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

    const layout = {height: PlotsModalComponent.PLOT_HEIGHT, width: PlotsModalComponent.PLOT_WIDTH, title: "Boxplot"};
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

    const layout = {height: PlotsModalComponent.PLOT_HEIGHT, width: PlotsModalComponent.PLOT_WIDTH, title: "Violin Plot"};
    this.plot("violinPlot", [trace], layout)
  }

  private qqPlot(): void {
    const trace = {
      x: this.normalDistributed,
      y: this.data.sort((n1, n2) => n2 - n1),
      type: 'scatter',
      mode: "markers",
      name: this.column.name,
      marker: {
        size: 8
      }
    };

    const coordinates = this.calculateLineCoordinates(this.data, this.normalDistributed);

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
      height: PlotsModalComponent.PLOT_HEIGHT,
      width: PlotsModalComponent.PLOT_WIDTH,
      title: "Normal Q-Q Plot",
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

    this.plot("qqPlot", plotData, layout)
  }

  private ppPlot(): void {
    let theoreticalCD = this.normalDistributed.map(x => this.normalDistribution.cdf(x)).sort((n1, n2) => n2- n1);
    let empiricalCD = this.data.map(x => this.normalDistribution.cdf(x)).sort((n1, n2) => n2 - n1);

    const trace = {
      x: theoreticalCD,
      y: empiricalCD,
      type: 'scatter',
      mode: "markers",
      name: this.column.name,
      marker: {
        size: 8
      }
    };

    const coordinates = this.calculateLineCoordinates(empiricalCD, theoreticalCD);

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
      height: PlotsModalComponent.PLOT_HEIGHT,
      width: PlotsModalComponent.PLOT_WIDTH,
      title: "Normal P-P Plot",
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

    this.plot("ppPlot", plotData, layout)
  }

  private cdPlot(): void {
    let empiricalCD = this.data.map(x => this.normalDistribution.cdf(x)).sort((n1, n2) => n2 - n1);

    const trace = {
      x: this.data.sort((n1, n2) => n2 - n1),
      y: empiricalCD,
      type: 'scatter',
      mode: "markers",
      name: this.column.name,
      marker: {
        size: 8
      }
    };

    const plotData = [trace];

    const layout = {
      height: PlotsModalComponent.PLOT_HEIGHT,
      width: PlotsModalComponent.PLOT_WIDTH,
      title: "Normal Cumulative Distribution",
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

    this.plot("cdPlot", plotData, layout)
  }

  private plot(target: String, data: any[], layout: any): void {
    Plotly.newPlot(target, data, layout)
  }

  private calculateLineCoordinates(sample: number[], theoretical: number[]): [number, number, number, number] {
    const dx = StatisticsUtils.percentile(theoretical, 75) - StatisticsUtils.percentile(theoretical, 25);
    const dy = StatisticsUtils.percentile(sample, 75) - StatisticsUtils.percentile(sample, 25);

    const b = dy / dx;

    const xc = (StatisticsUtils.percentile(theoretical, 25) + StatisticsUtils.percentile(theoretical, 75)) / 2;
    const yc = (StatisticsUtils.percentile(sample, 25) + StatisticsUtils.percentile(sample, 75)) / 2;

    const xmax = Math.max.apply(Math, theoretical);
    const xmin = Math.min.apply(Math, theoretical);
    const ymax = yc + b * (xmax - xc);
    const ymin = yc - b * (xc - xmin);

    return [xmin, ymin, xmax, ymax]
  }

}
