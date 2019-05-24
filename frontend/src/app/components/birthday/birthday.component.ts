import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import {Event, EventType} from '../../models/event';
import {EventClient} from '../../rest/event-client';
import { RoomUse,Room } from 'src/app/models/roomUse';
import { Customer } from 'src/app/models/customer';
import {
  NgbDateParserFormatter,
  NgbDateStruct,
  NgbTimeStruct,
} from '@ng-bootstrap/ng-bootstrap';

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
private startDate:NgbDateStruct;
private startTime:NgbTimeStruct;
private parserFormatter: NgbDateParserFormatter;

  constructor(private eventClient: EventClient) {}

  ngOnInit() {
  }
  
  postBirthday(form: NgForm){
    this.room.begin = this.dateToString(this.startDate, this.startTime);
    this.startTime.hour = this.startTime.hour + 3;
    this.room.end = this.dateToString(this.startDate, this.startTime);
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
      if(this.startDate == null){
        return false;
      }
      if(this.startTime == null){
        return false;
      }
      if(this.room.room == null){
        return false;
      }
      return true;
  }

  private dateToString(date: NgbDateStruct, time: NgbTimeStruct) {
    let stringMinute = '';
    let stringHour = '';
    let isChangedHour = false;
    let isChangedMinute = false;

    if (time.minute == 1) {
        stringMinute = '01';
        isChangedMinute = true;
    }
    if (time.minute == 2) {
        stringMinute = '02';
        isChangedMinute = true;
    }
    if (time.minute == 3) {
        stringMinute = '03';
        isChangedMinute = true;
    }
    if (time.minute == 4) {
        stringMinute = '04';
        isChangedMinute = true;
    }
    if (time.minute == 5) {
        stringMinute = '05';
        isChangedMinute = true;
    }
    if (time.minute == 6) {
        stringMinute = '06';
        isChangedMinute = true;
    }
    if (time.minute == 7) {
        stringMinute = '07';
        isChangedMinute = true;
    }
    if (time.minute == 8) {
        stringMinute = '08';
        isChangedMinute = true;
    }
    if (time.minute == 9) {
        stringMinute = '09';
        isChangedMinute = true;
    }
if (time.hour == 1) {
        stringHour = '01';
        isChangedHour = true;
    }
    if (time.hour == 2) {
        stringHour = '02';
        isChangedHour = true;
    }
    if (time.hour == 3) {
        stringHour = '03';
        isChangedHour = true;
    }
    if (time.hour == 4) {
        stringHour = '04';
        isChangedHour = true;
    }
    if (time.hour == 5) {
        stringHour = '05';
        isChangedHour = true;
    }
    if (time.hour == 6) {
        stringHour = '06';
        isChangedHour = true;
    }
    if (time.hour == 7) {
        stringHour = '07';
        isChangedHour = true;
    }
    if (time.hour == 8) {
        stringHour = '08';
        isChangedHour = true;
    }
    if (time.hour == 9) {
        stringHour = '09';
        isChangedHour = true;
    }
if (isChangedHour && isChangedMinute) {
        return (
            this.parserFormatter.format(date) +
            'T' +
            stringHour +
            ':' +
            stringMinute +
            ':00'
        );
    }

    if (isChangedHour) {
        return (
            this.parserFormatter.format(date) +
            'T' +
            stringHour +
            ':' +
            time.minute +
            ':00'
        );
    }

    if (isChangedMinute) {
        return (
            this.parserFormatter.format(date) +
            'T' +
            time.hour +
            ':' +
            time.minute +
            ':00'
        );
    }

    return (
        this.parserFormatter.format(date) +
        'T' +
        time.hour +
        ':' +
        time.minute +
        ':00'
    );
}
}
