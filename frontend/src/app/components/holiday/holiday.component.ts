import { Component, OnInit } from '@angular/core';
import { Holiday} from 'src/app/models/holiday';
import {NgbDateParserFormatter, NgbDateStruct, NgbTimeStruct} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-holiday',
  templateUrl: './holiday.component.html',
  styleUrls: ['./holiday.component.scss']
})

export class HolidayComponent implements OnInit {

  constructor() {}

  ngOnInit() {
  }
  title = 'Neuen Urlaub einrichten';
  stime = {hour: 13, minute: 30};
  etime = {hour: 14, minute: 30};

  startDate = ('2019-06-17T03:24');
  endDate = ('2019-06-18T03:24');

  model = new Holiday(null, null, this.startDate, this.endDate);

  holidayModel = new Holiday(null, null, this.startDate, this.endDate);

  get diagnostic() {return JSON.stringify(this.model);}

  onSubmit() {
    console.log(this.holidayModel);
  }

}
