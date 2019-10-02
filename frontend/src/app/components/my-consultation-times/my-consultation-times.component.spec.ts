import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MyConsultationTimesComponent } from './my-consultation-times.component';

describe('MyConsultationTimesComponent', () => {
  let component: MyConsultationTimesComponent;
  let fixture: ComponentFixture<MyConsultationTimesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MyConsultationTimesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MyConsultationTimesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
