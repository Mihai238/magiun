import {Distribution} from "./distribution";

export class DataSet {
  id: number;
  name: string;
  schema: Schema;

  constructor(id: number, name: string, schema: Schema) {
    this.id = id;
    this.name = name;
    this.schema = schema;
  }

  public equals(d: DataSet): boolean {
    return this.id == d.id && this.name == d.name && this.schema.equals(d.schema);
  }

}

export class Schema {
  columns: Column[];
  totalCount: number;


  constructor(columns: Column[], totalCount: number) {
    this.columns = columns;
    this.totalCount = totalCount;
  }

  public equals(s: Schema): boolean {
    if (this.totalCount == s.totalCount && this.columns.length == s.columns.length) {
      for (let i = 0; i < this.columns.length; i++) {
        if (!this.columns[i].equals(s.columns[i])) {
          return false;
        }
      }
      return true;
    }
    return false;
  }
}

export class Column {
  index: number;
  name: string;
  type: ColumnType;
  distribution: Distribution;


  constructor(index: number, name: string, type: ColumnType, distribution: Distribution = null) {
    this.index = index;
    this.name = name;
    this.type = type;
    this.distribution = distribution;
  }

  public equals(c: Column): boolean {
    return this.index == c.index && this.name == c.name && this.type == c.type;
  }
}

export enum ColumnType {
  String = 'String',
  Boolean = 'Boolean',
  Date = 'Date',
  Int = 'Int',
  Double = 'Double',
  Unknown = 'Unknown'
}
