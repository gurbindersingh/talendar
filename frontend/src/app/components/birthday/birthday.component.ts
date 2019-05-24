import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import {Event, EventType} from '../../models/event';
import {EventClient} from '../../rest/event-client';
import { RoomUse,Room } from 'src/app/models/roomUse';
import { Customer } from 'src/app/models/customer';

@Component({
  selector: 'app-birthday',
  templateUrl: './birthday.component.html',
  styleUrls: ['./birthday.component.scss']
})
export class BirthdayComponent implements OnInit {
public ageList: Number[] = [5,6,7,8,9,10,11,12,13,14,15,16,17,18,19];
public ageListb: Number[] = [5,6,7,8,9,10,11,12,13,14,15];
private event: Event = new Event();
private room: RoomUse = new RoomUse();
private customer: Customer = new Customer();

  constructor(private eventClient: EventClient) {}

  ngOnInit() {
  }
  
  postBirthday(form: NgForm){
    this.room.end.setHours(this.room.begin.getHours() + 3);
    this.event.eventType = EventType.Birthday;
    let roomUses: RoomUse[] = [this.room];
    let customers: Customer[] = [this.customer];
    this.event.roomUses = roomUses;
    this.event.customers = customers;
    this.event.name = this.event.birthdayType + ' Geburtstag fuer ' + this.event.customers[0].firstName + ' ' + this.event.customers[0].lastName + ' am ' + this.event.roomUses[0].begin;
    this.event.roomUses[0].event = this.event;
    this.event.customers[0].events =  [this.event];
    console.log("Passing Data to Rest Client");
    this.eventClient.postNewEvent(this.event).subscribe(
      (data: Event) => {
          console.log(data);
      },
      (error) => {
          console.log(error);
      }
    );
  }

  public isComplete(): boolean{
      if(this.event.birthdayType == ''){
        return false;
      }
      if(this.event.headcount == null){
        return false;
      }
      if(this.event.ageToBe == null){
        return false;
      }
      if(this.customer.firstName == ''){
        return false;
      }
      if(this.customer.lastName == ''){
        return false;
      }
      if(this.customer.email == ''){
        return false;
      }
      if(this.customer.phone == ''){
        return false;
      }
      if(this.room.begin == null){
        return false;
      }
      if(this.room.room == null){
        return false;
      }
      return true;
  }
  
}
