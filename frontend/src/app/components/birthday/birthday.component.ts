import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import {Event} from '../../models/event';
import {EventClient} from '../../rest/event-client';

@Component({
  selector: 'app-birthday',
  templateUrl: './birthday.component.html',
  styleUrls: ['./birthday.component.scss']
})
export class BirthdayComponent implements OnInit {
private ageList: Number[] = [5,6,7,8,9,10,11,12,13,14,15,16,17,18,19];
private ageListb: Number[] = [5,6,7,8,9,10,11,12,13,14,15];
  
  ngOnInit() {
  }
  
  postBirthday(form: NgForm){
    console.log("Passing Data to Rest Client");
    
  }

  
}
