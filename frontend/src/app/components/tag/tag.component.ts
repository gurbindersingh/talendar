import { Component, OnInit } from '@angular/core';
import { TagClient } from 'src/app/rest/tag-client';
import { NgForm } from '@angular/forms';
import { Tag } from 'src/app/models/tag';

@Component({
  selector: 'app-tag',
  templateUrl: './tag.component.html',
  styleUrls: ['./tag.component.scss']
})
export class TagComponent implements OnInit {
  public tag: string;
  private errorMsg: string;
  private successMsg: string;
  private tagDto: Tag = new Tag();

  constructor(private tagClient: TagClient, ) {

  }

  ngOnInit() {
  }

  public postTag(form: NgForm): void {
    this.tagDto.tag = this.tag;
    this.tagClient.postTag(this.tagDto).subscribe(
      (data: Tag) => {
        console.log(data);
        this.successMsg =
          'Der Tag wurde erfolgreich gespeichert';
        form.reset();
      },
      (error: Error) => {
        this.errorMsg =
          'Der Tag konnte nicht angelegt werden: ' +
          error.message;
      }
    );

  }

  goBack() {
    window.history.back();
  }

  public isComplete(): boolean {
    if (this.tag === null) {
      return false;
    }
    if (this.tag === '') {
      return false;
    }
    return true;
  }

}
