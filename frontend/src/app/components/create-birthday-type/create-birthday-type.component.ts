import { Component, OnInit } from '@angular/core';
import { Birthday } from 'src/app/models/birthday';
import { NgForm } from '@angular/forms';
import { BirthdayClient } from 'src/app/rest/birthday-client';

@Component({
  selector: 'app-create-birthday-type',
  templateUrl: './create-birthday-type.component.html',
  styleUrls: ['./create-birthday-type.component.scss']
})
export class CreateBirthdayTypeComponent implements OnInit {
  public name: string;
  public price: number;
  errorMsg: string;
  successMsg: string;
  private birthdayDto: Birthday = new Birthday();

  constructor(private birthdayClient: BirthdayClient) { }

  ngOnInit() { }

  public postTag(form: NgForm): void {
    this.birthdayDto.name = this.name;
    this.birthdayDto.price = this.price;
    this.birthdayClient.postBirthdayType(this.birthdayDto).subscribe(
      (data: Birthday) => {
        this.successMsg = 'Der Geburtstagstyp wurde erfolgreich gespeichert';
        form.reset();
      },
      (error: Error) => {
        this.errorMsg =
          'Der Geburtstagstyp konnte nicht angelegt werden: ' +
          error.message;
      }
    );
  }

  goBack() {
    window.history.back();
  }

  public isComplete(): boolean {
    if (this.name === null) {
      return false;
    }
    if (this.name === '') {
      return false;
    }
    if (this.price === undefined) {
      return false;
    }
    return true;
  }

  clearInfoMsg(): void {
    this.errorMsg = undefined;
    this.successMsg = undefined;
  }
}
