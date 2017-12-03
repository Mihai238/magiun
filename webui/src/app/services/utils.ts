export class Utils {

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


}
