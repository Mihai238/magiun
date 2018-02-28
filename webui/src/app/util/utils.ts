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

  public static isKeyPresentInTheMap(map: Map<any, any>, key: any): boolean {
    if (map.size === 0) {
      return false;
    } else {
      const keys = map.keys();
      let elem = keys.next().value;

      while (elem !== null && elem !== undefined) {
        if (elem === key) {
          return true;
        } else {
          elem = keys.next().value;
        }
      }

      return false;
    }
  }

}
