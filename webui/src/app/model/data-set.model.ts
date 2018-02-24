export interface DataSet {
  id: number;
  name: string;
  schema: Schema;
}

export interface Schema {
  columns: Column[];
}

export interface Column {
  index: number;
  name: string;
  type: ColumnType;
}

export enum ColumnType {
  String = 'String',
  Boolean = 'Boolean',
  Date = 'Date',
  Int = 'Int',
  Double = 'Double',
  Unknown = 'Unknown'
}
