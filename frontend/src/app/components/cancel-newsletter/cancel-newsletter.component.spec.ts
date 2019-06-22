import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CancelNewsletterComponent } from './cancel-newsletter.component';

describe('CancelNewsletterComponent', () => {
  let component: CancelNewsletterComponent;
  let fixture: ComponentFixture<CancelNewsletterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CancelNewsletterComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CancelNewsletterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
