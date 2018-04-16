import {CollectionsUtils} from './collections.utils';
import {Tuple} from './tuple';

describe('Utils: CollectionsUtils', () => {

  it('should remove the value from the array', () => {
    expect(CollectionsUtils.deleteEntryFromArray([1], 1)).toEqual([]);
    expect(CollectionsUtils.deleteEntryFromArray([1, 2, 3, 4, 5], 5)).toEqual([1, 2, 3, 4]);
  });

  it('should return the map entry by index', () => {
    const map = new Map<number, string>();
    map.set(0, 'zero');
    map.set(1, 'one');
    map.set(2, 'two');
    map.set(3, 'three');

    expect(CollectionsUtils.getMapEntryByIndex(map, 2)).toEqual(new Tuple<number, string>(2, 'two'));
    expect(CollectionsUtils.getMapKeyByIndex(map, 1)).toBe(1);
    expect(CollectionsUtils.getMapValueByIndex(map, 3)).toBe('three');
  })
});
