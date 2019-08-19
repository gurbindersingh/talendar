import { Component, OnInit } from '@angular/core';
import { Birthday } from 'src/app/models/birthday';
import { BirthdayClient } from 'src/app/rest/birthday-client';

@Component({
  selector: 'app-birthday-type-view',
  templateUrl: './birthday-type-view.component.html',
  styleUrls: ['./birthday-type-view.component.scss']
})
export class BirthdayTypeViewComponent implements OnInit {
  loading: boolean;
  successMsg: string;
  errorMsg: string;
  birthdayTypes: Birthday[] = [];
  title: string;
  constructor(private birthdayTypeClient: BirthdayClient) { }

  ngOnInit() {
    this.title = 'Geburtstagstypen';
    this.loading = false;
    this.successMsg = '';
    this.errorMsg = '';
    this.birthdayTypeClient.getAllBirthdayTypes().subscribe(
      (birthdayTypeList: Birthday[]) => {
        this.birthdayTypes = birthdayTypeList;
        this.birthdayTypes.sort((a: Birthday, b: Birthday) => {
          return a.name.localeCompare(b.name);
        })
      },
      (error) => {

      });
  }

  delete(birthday: Birthday): void {
    console.log(birthday.id);
    this.birthdayTypeClient.deleteBirthdayType(birthday).subscribe(
      () => {
        this.ngOnInit();
      },
      (error) => {
        console.log(error);
      }
    )
  }
}
