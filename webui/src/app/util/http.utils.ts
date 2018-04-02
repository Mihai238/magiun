import {HttpHeaders} from '@angular/common/http';
import {BlockComponent} from '../components/workflows/blocks/block.component';
import {BlockParameter} from '../components/workflows/blocks/block-parameter';
import {WireType} from '../components/workflows/blocks/wire-type';

export class HttpUtils {

  public static headers(): HttpHeaders {
    const headers = new HttpHeaders();
    headers.append('Content-Type', 'application/json');

    return headers;
  }

  public static optionsOnlyWithHeaders(): {} {
    return {headers: HttpUtils.headers()}
  }

  public static createBlockComponentBodyJson(component: BlockComponent): string {
    return '{\n' +
      '\"id\":\"' + component.id + '\",\n' +
      '\"type\":\"' + component.name + '\",\n' +
      '\"inputs\":[' + this.createInputsBodyJson(component.inputs) + '],\n' +
      '\"params\":{\n' + this.createParametersBodyJson(component.configurationParameters) + '}\n}'
  }

  private static createInputsBodyJson(inputs: WireType[]): string {
    let json = '';

    inputs.forEach(input => {
      json = json + '[\"' + input.i18nValue + '\", ' + 0 + ']';

      if (inputs.indexOf(input) !== (inputs.length - 1)) {
       json += ',';
      }
    });
    return json;
  }

  private static createParametersBodyJson(parameters: Array<BlockParameter<any>>): string {
    let json = '';

    parameters.forEach(p => {
      json = json + '\"' + p.name + '\":\"' + HttpUtils.createParameterValueBodyJson(p.value) + '\"';
      if (parameters.indexOf(p) === (parameters.length - 1)) {
        json += '\n';
      } else {
        json += ',\n';
      }

    });

    return json;
  }

  private static createParameterValueBodyJson(value: any) {
    if (value instanceof BlockParameter) {
      return String(value.name);
    } else {
      return String(value);
    }
  }
}
