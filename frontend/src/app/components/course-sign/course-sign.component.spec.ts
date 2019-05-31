import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CourseSignComponent } from './course-sign.component';

describe('CourseSignComponent', () => {
  let component: CourseSignComponent;
  let fixture: ComponentFixture<CourseSignComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CourseSignComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CourseSignComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
