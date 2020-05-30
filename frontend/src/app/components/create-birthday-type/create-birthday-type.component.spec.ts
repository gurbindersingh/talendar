import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateBirthdayTypeComponent } from './create-birthday-type.component';

describe('CreateBirthdayTypeComponent', () => {
  let component: CreateBirthdayTypeComponent;
  let fixture: ComponentFixture<CreateBirthdayTypeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CreateBirthdayTypeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateBirthdayTypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
