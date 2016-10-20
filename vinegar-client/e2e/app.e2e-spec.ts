import { VinegarClientPage } from './app.po';

describe('vinegar-client App', function() {
  let page: VinegarClientPage;

  beforeEach(() => {
    page = new VinegarClientPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
