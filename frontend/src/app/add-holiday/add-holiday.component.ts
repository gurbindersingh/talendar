import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-add-holiday',
  templateUrl: './add-holiday.component.html',
  styleUrls: ['./add-holiday.component.scss']
})
export class AddHolidayComponent implements OnInit {
  title = 'Einen Urlaub hinzufügen';

  clicked = false;

  handleClick() {
    this.clicked = true;
  }

  constructor() { }

  ngOnInit() {
  }

}
