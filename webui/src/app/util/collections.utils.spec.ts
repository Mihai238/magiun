import {CollectionsUtils} from './collections.utils';
import {Tuple} from './tuple';

describe('Utils: CollectionsUtils', () => {

  it('should count the occurrences of the elements and return a map having the element as a key and the number of occurrences as a value', () => {
    // given
    const numberValues = [1, 2, 2, 3, 3, 3, 4, 4, 4, 4];
    const stringValues = ['a', 'a', 'a', 'b', 'b', 'c', 'd', 'd', 'd', 'd'];

    // when
    const numberOccurrences = CollectionsUtils.countOccurrences(numberValues);
    const stringOccurrences = CollectionsUtils.countOccurrences(stringValues);

    // then
    expect(numberOccurrences.size).toBe(4);
    expect(numberOccurrences.get(1)).toBe(1);
    expect(numberOccurrences.get(2)).toBe(2);
    expect(numberOccurrences.get(3)).toBe(3);
    expect(numberOccurrences.get(4)).toBe(4);

    expect(stringOccurrences.size).toBe(4);
    expect(stringOccurrences.get('a')).toBe(3);
    expect(stringOccurrences.get('b')).toBe(2);
    expect(stringOccurrences.get('c')).toBe(1);
    expect(stringOccurrences.get('d')).toBe(4);
  });

  it('should remove the value from the array', () => {
    expect(CollectionsUtils.deleteEntryFromArray([1], 1)).toEqual([]);
    expect(CollectionsUtils.deleteEntryFromArray([1, 2, 3, 4, 5], 5)).toEqual([1, 2, 3, 4]);
  });

  it('should return the map entry by index', () => {
    // given
    const map = new Map<number, string>();
    map.set(0, 'zero');
    map.set(1, 'one');
    map.set(2, 'two');
    map.set(3, 'three');

    // when & then
    expect(CollectionsUtils.getMapEntryByIndex(map, 2)).toEqual(new Tuple<number, string>(2, 'two'));
    expect(CollectionsUtils.getMapKeyByIndex(map, 1)).toBe(1);
    expect(CollectionsUtils.getMapValueByIndex(map, 3)).toBe('three');
  });

  it('should return the list without the specified element', () => {
    // given
    const values = ['a', 'b', 'c', 'd'];

    // when
    const newValues = CollectionsUtils.withoutElement(values, 'a');

    // then
    expect(newValues.length).toBe(3);
    expect(newValues.indexOf('a')).toBe(-1);
  });

  it('should return the list without the null elements', () => {
    // given
    const values = ['a', 'b', null, 'c', undefined, 'd'];

    // when
    const cleanValue = CollectionsUtils.cleanArray(values);

    // then
    expect(cleanValue.length).toBe(4);
    expect(cleanValue).toEqual(['a', 'b', 'c', 'd']);
  });

  it('should return an empty list when receiving an empty one', () => {
    // given
    const values = [];

    // when
    const cleanValue = CollectionsUtils.cleanArray(values);

    // then
    expect(cleanValue.length).toBe(0);
    expect(cleanValue).toEqual([]);
  });

  it('should return an empty list when receiving a list containing only null values', () => {
    // given
    const values = [null, null, null, null];

    // when
    const cleanValue = CollectionsUtils.cleanArray(values);

    // then
    expect(cleanValue.length).toBe(0);
    expect(cleanValue).toEqual([]);
  });
});
