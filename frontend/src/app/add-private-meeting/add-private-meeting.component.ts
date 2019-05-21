import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-add-private-meeting',
  templateUrl: './add-private-meeting.component.html',
  styleUrls: ['./add-private-meeting.component.scss']
})
export class AddPrivateMeetingComponent implements OnInit {

  constructor() { }


  ngOnInit() {
  }

  rooms: string[] = ["Orange", "Grün", "Erdgeschoss"];
  roomString: string = "Raum auswählen";
  
  ChangeSortOrder(room: string) { 
    this.roomString = room;
  }

}
