import { Component, OnInit } from '@angular/core';
import { TagClient } from 'src/app/rest';
import { NgForm } from '@angular/forms';
import { Tag } from 'src/app/models/tag';

@Component({
    selector: 'app-tag',
    templateUrl: './tag.component.html',
    styleUrls: ['./tag.component.scss'],
})
export class TagComponent implements OnInit {
    public tag: string;
    errorMsg: string;
    successMsg: string;
    private tagDto: Tag = new Tag();

    constructor(private tagClient: TagClient) {}

    ngOnInit() {}

    public postTag(form: NgForm): void {
        this.tagDto.tag = this.tag;
        this.tagClient.postTag(this.tagDto).subscribe(
            (data: Tag) => {
                this.successMsg = 'Das Keyword wurde erfolgreich gespeichert';
                form.reset();
            },
            (error: Error) => {
                this.errorMsg =
                    'Das Keyword konnte nicht angelegt werden: ' +
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

    clearInfoMsg(): void {
        this.errorMsg = undefined;
        this.successMsg = undefined;
    }
}
