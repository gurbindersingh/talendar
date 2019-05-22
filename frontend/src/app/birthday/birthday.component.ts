import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-birthday',
  templateUrl: './birthday.component.html',
  styleUrls: ['./birthday.component.scss']
})
export class BirthdayComponent implements OnInit {
private ageList: Number[] = [5,6,7,8,9,10,11,12,13,14,15,16,17,18,19]
private ageListb: Number[] = [5,6,7,8,9,10,11,12,13,14,15]

  constructor() { }

  ngOnInit() {
  }

}
