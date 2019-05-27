import { Component, OnInit } from '@angular/core';
import { EventClient } from 'src/app/rest/event-client';
import { ActivatedRoute, Router } from '@angular/router';
import {Event} from '../../models/event';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-cancel-event',
  templateUrl: './cancel-event.component.html',
  styleUrls: ['./cancel-event.component.scss']
})
export class CancelEventComponent implements OnInit {
private event: Event = new Event();
private title: String;
private textBox: String;
private valid: boolean;
private successMsg: String;
private errorMsg: String;

  constructor(
    private eventClient: EventClient,
    private route: ActivatedRoute,
    private router: Router,
    ) { }

  ngOnInit() {
    const id: number = this.route.snapshot.queryParams.id;
    if(id === undefined){
      this.title = "Dieser URL ist nicht gültig";
      this.textBox = "Sie hätten dieser Seite garnicht erreichen sollen."
      this.valid = false;
    }else{
      console.log('Getting event with id ' + id);
      this.eventClient.getEventById(id).subscribe(
        (got: Event) => {
          this.event = got;
        },
        (error) => {
          console.log(error);
        }
      )
      if(this.event === null || this.event=== undefined){
        this.title = "Fehler 404";
        this.textBox = "Dieser Ereignis existiert nicht in der Datenbank"
        this.valid =false;
      }else{
        console.log('Got event with id ' + this.event.id);
        this.title = "Hallo " + this.event.customerDtos[0].firstName + " " + this.event.customerDtos[0].lastName + "!";
        this.textBox = "Wollen sie wirklich " + this.event.name + " stornieren?"; 
        this.valid = true;
      }
    }
  }

  public cancelEvent(form: NgForm): void{
    console.log('Passing id to cancel information');
    const id: number = this.route.snapshot.queryParams.id;
    
    this.eventClient.cancelEvent(id).subscribe(
      () => {
        this.successMsg =
            'Der Event wurde erfolgreich storniert';
    },
    (error: Error) => {
        console.log(error.message);
        this.errorMsg =
            'Der event konnte nicht storniert werden ' +
            error.message;
    }
    )
  }
}
