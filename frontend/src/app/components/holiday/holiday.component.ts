import { Component, OnInit } from '@angular/core';
import { Holiday} from 'src/app/models/holiday';

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
  model = new Holiday(null, null, '01-04-2019 14:44', '01-04-2019 14:45');
  get diagnostic() {return JSON.stringify(this.model);}

}
