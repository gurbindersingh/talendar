import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewBirthdayComponent } from './new-birthday.component';

describe('NewBirthdayComponent', () => {
  let component: NewBirthdayComponent;
  let fixture: ComponentFixture<NewBirthdayComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewBirthdayComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewBirthdayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
