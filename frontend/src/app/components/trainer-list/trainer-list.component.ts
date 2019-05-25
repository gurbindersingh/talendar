import { Component, OnInit } from '@angular/core';
import { TrainerClient } from 'src/app/rest/trainer-client';
import { Trainer } from 'src/app/models/trainer';

@Component({
  selector: 'app-trainer-list',
  templateUrl: './trainer-list.component.html',
  styleUrls: ['./trainer-list.component.scss']
})
export class TrainerListComponent implements OnInit {

  constructor(private trainerClient: TrainerClient) { }

  private title = 'Trainerliste';
  private trainerList: Trainer[] = [];

  ngOnInit() {
    console.log('Init Trainer List');
    this.trainerClient.getAll().subscribe(
      (list: Trainer[]) => {
        this.trainerList = list;
      },
      (error) => {
        console.log(error);
      }
    )
  }




  

}
