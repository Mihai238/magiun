import {Tuple} from './tuple';

export class CollectionsUtils {

  public static countOccurrences(values: any[]): Map<any, number> {
    const map = new Map();

    for (const key of values) {
      if (map.has(key)) {
        map.set(key, map.get(key) + 1);
      } else {
        map.set(key, 1);
      }
    }
    return map;
  };

  public static deleteEntryFromArray<T>(values: Array<T>, value: T): Array<T> {
    const index = values.indexOf(value);

    if (index === -1) {
      return values;
    } else {
      values.splice(index, 1);
      return values;
    }
  }

  public static getMapEntryByIndex<K, V>(map: Map<K, V>, index: number): Tuple<K, V> {
    if (index >= map.size) {
      return null;
    }

    if (index === 0) {
      const entry = map.entries().next().value;
      return new Tuple<K, V>(entry[0], entry[1]);
    } else {
      const entries = map.entries();
      let currentIndex = 0;
      while (currentIndex !== index) {
        currentIndex++;
        entries.next();
      }
      const entry = entries.next().value;
      return new Tuple<K, V>(entry[0], entry[1]);
    }
  }

  public static getMapValueByIndex<K, V>(map: Map<K, V>, index: number): V {
    return this.getMapEntryByIndex(map, index)._2;
  }

  public static getMapKeyByIndex<K, V>(map: Map<K, V>, index: number): K {
    return this.getMapEntryByIndex(map, index)._1;
  }
}
