import { PropertyFinderWebPage } from './app.po';

describe('property-finder-web App', () => {
  let page: PropertyFinderWebPage;

  beforeEach(() => {
    page = new PropertyFinderWebPage();
  });

  it('should display welcome message', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('Welcome to app!');
  });
});
