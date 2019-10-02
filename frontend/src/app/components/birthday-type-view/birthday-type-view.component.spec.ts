import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BirthdayTypeViewComponent } from './birthday-type-view.component';

describe('BirthdayTypeViewComponent', () => {
  let component: BirthdayTypeViewComponent;
  let fixture: ComponentFixture<BirthdayTypeViewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BirthdayTypeViewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BirthdayTypeViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
