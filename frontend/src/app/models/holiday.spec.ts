import { Holiday } from './holiday';

describe('Holiday', () => {
  it('should create an instance', () => {
    expect(new Holiday(null, null, null, null)).toBeTruthy();
  });
});
