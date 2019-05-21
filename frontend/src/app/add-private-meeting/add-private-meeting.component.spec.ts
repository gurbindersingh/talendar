import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddPrivateMeetingComponent } from './add-private-meeting.component';

describe('AddPrivateMeetingComponent', () => {
  let component: AddPrivateMeetingComponent;
  let fixture: ComponentFixture<AddPrivateMeetingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddPrivateMeetingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddPrivateMeetingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
