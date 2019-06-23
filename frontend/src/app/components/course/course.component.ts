import { Component, OnInit } from '@angular/core';
import { Event } from 'src/app/models/event';
import { NgForm } from '@angular/forms';
import { EventClient } from 'src/app/rest/event-client';
import { DateTimeParserService } from 'src/app/services/date-time-parser.service';
import { Room } from 'src/app/models/enum/room';
import { EventType } from 'src/app/models/enum/eventType';
import { NgbDateStruct, NgbTimeStruct } from '@ng-bootstrap/ng-bootstrap';
import { RoomUse } from 'src/app/models/roomUse';
import { Trainer } from 'src/app/models/trainer';
import { ActivatedRoute } from '@angular/router';
import { CronMakerService } from 'src/app/services/cronMaker.service';
import { SessionStorageService } from 'src/app/services/session-storage.service';
import { Tag } from 'src/app/models/tag';
import { TagClient } from 'src/app/rest/tag-client';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import * as Croppie from 'croppie';
import { ImageClient } from 'src/app/rest/image-client';
import { Observable } from 'rxjs';
import { threadId } from 'worker_threads';

@Component({
    selector: 'app-course',
    templateUrl: './course.component.html',
    styleUrls: ['./course.component.scss'],
})
export class CourseComponent implements OnInit {
    title = 'Kurs eintragen';

    cronMaker: CronMakerService;

    roomOption = 0;
    otherRoom1String: string;
    otherRoom2String: string;

    repeatOptions = ['Nie', 'Jeden Tag', 'Jede Woche', 'Jeden Monat'];
    terminateAfterOption = ['Nie', 'Nach'];
    selectedRepeatOption: string;
    terminateModul: string;
    alleX: number;
    endedX: number;

    loading = false;

    minuteStep = 15;
    description = '';
    toggleOptions = false;
    maxParticipants: number;
    price: number;
    minAge: number;
    maxAge: number;
    startDate: NgbDateStruct;
    startTime: NgbTimeStruct;
    endTime: NgbTimeStruct;
    endOfApplicationDate: NgbDateStruct;
    endOfApplicationTime: NgbTimeStruct;
    isCreate: boolean;

    btnText: string;
    saveMode: boolean;
    valueEndOfApplication: NgbDateStruct;

    greenRadioButton: RadioNodeList;
    radioButtonSelected = '';

    numImg: number;
    binaryEncodedImages: any[] = [];

    // selected files to upload for a course
    files: File[] = [];
    croppedImages: Blob[] = [];

    private croppie: Croppie;
    promises: Promise<string>[] = [];

    event: Event = new Event();
    private roomUse: RoomUse = new RoomUse();
    private trainer: Trainer = new Trainer();
    private dateTimeParser: DateTimeParserService;

    private formDatas: FormData[] = [];

    errorMsg: string;
    successMsg: string;
    private tags: Tag[] = [];
    private tagStringSelected: string;
    tagStrings: string[] = [];

    constructor(
        private eventClient: EventClient,
        private imageClient: ImageClient,
        dateTimeParser: DateTimeParserService,
        private route: ActivatedRoute,
        cronMaker: CronMakerService,
        private sessionService: SessionStorageService,
        private tagClient: TagClient,
        private modalService: NgbModal
    ) {
        this.cronMaker = cronMaker;
        this.dateTimeParser = dateTimeParser;
        this.startTime = { hour: 13, minute: 0, second: 0 };
        this.endTime = { hour: 14, minute: 0, second: 0 };
        this.endOfApplicationTime = { hour: 13, minute: 0, second: 0 };
        this.selectedRepeatOption = this.repeatOptions[0];
        this.terminateModul = this.terminateAfterOption[0];
        this.alleX = 1;
        this.endedX = 1;
    }

    ngOnInit() {
        const id: number = this.route.snapshot.queryParams.id;

        if (id === undefined) {
            this.title = 'Kurs eintragen';
            this.btnText = 'Kurs eintragen';
            this.saveMode = true;
            this.isCreate = true;
            this.tagClient.getAll().subscribe(
                (tagList: Tag[]) => {
                    this.tags = tagList;
                    for (let _i = 0; _i < this.tags.length; _i++) {
                        this.tagStrings.push(this.tags[_i].tag);
                        console.log(this.tagStrings);
                    }
                },
                (error) => {
                    console.log(error);
                }
            );
        } else {
            this.title = 'Kurs bearbeiten';
            this.btnText = 'Bearbeiten';
            this.saveMode = false;
            this.isCreate = false;
            this.eventClient.getEventById(id).subscribe(
                (data: Event) => {
                    console.log(data);
                    this.event = data;
                },
                (error: Error) => {
                    this.errorMsg =
                        'Der ausgewählte Trainer konnte leider nicht geladen werden.';
                }
            );
            this.tagClient.getAll().subscribe(
                (tagList: Tag[]) => {
                    this.tags = tagList;
                    for (let _i = 0; _i < this.tags.length; _i++) {
                        this.tagStrings.push(this.tags[_i].tag);
                    }
                },
                (error) => {
                    console.log(error);
                }
            );
        }
    }

    public roomOption0Clicked(): void {
        this.roomOption = 0;
    }

    public roomOption1Clicked(): void {
        this.roomOption = 1;
    }

    public roomOption2Clicked(): void {
        this.roomOption = 2;
    }

    public roomOption3Clicked(): void {
        this.roomOption = 3;
    }

    public roomOption4Clicked(): void {
        this.roomOption = 4;
    }

    public isRepeat(): boolean {
        if (this.selectedRepeatOption === 'Nie') {
            return false;
        }
        return true;
    }

    public togg(): void {
        if (this.toggleOptions === false) {
            this.toggleOptions = true;
        } else {
            this.toggleOptions = false;
            this.selectedRepeatOption = this.repeatOptions[0];
            this.terminateModul = this.terminateAfterOption[0];
        }
    }

    public isTerminate(): boolean {
        if (this.terminateModul === 'Nie') {
            return false;
        }
        return true;
    }

    public getCron(): string {
        return this.cronMaker.createCron(
            this.startDate,
            this.startTime,
            this.startDate,
            this.endTime,
            this.toggleOptions,
            this.selectedRepeatOption,
            this.alleX,
            this.terminateModul,
            this.endedX
        );
    }

    public postMeeting(form: NgForm): void {
        console.log(this.formDatas.length);
        console.log('DEBUGGGG!!:' + this.promises.length);

        if (this.promises.length === 0) {
            this.postData(form);
        } else {
            console.log('Post pictures');
            Promise.all(this.promises).then(
                (data: string[]) => {
                    this.event.pictures = data;
                    this.postData(form);
                },
                (error) => {
                    /**
                     *  manual parsing required because this endpoint
                     *  returns plain text (no json)
                     */
                    const info = JSON.parse(error);
                    this.errorMsg = info.message;
                }
            );
        }
    }

    public onFileSelected(event: any, croppieModal: any): void {
        this.files = [];
        this.binaryEncodedImages = [];
        this.formDatas = [];
        for (let i = 0; i < event.target.files.length; i++) {
            const file: File = event.target.files[i];
            this.files.push(file);
            this.extractBinaryDataFromFile(file, i);
        }

        this.numImg = 0;

        this.startCroppie(croppieModal);
    }

    public removeFile(index: number): void {
        const newListFiles: File[] = [];
        const newListFormData: FormData[] = [];
        for (let i = 0; i < this.files.length; i++) {
            if (i !== index) {
                newListFiles.push(this.files[i]);
                newListFormData.push(this.formDatas[i]);
            }
        }

        if (newListFiles.length > 0) {
            this.files = newListFiles;
            this.formDatas = newListFormData;
        } else {
            this.files = [];
            this.formDatas = [];
        }
    }

    public saveCropped(croppieModal: any): void {
        console.log('SAVE');
        this.croppie.bind({ url: '' });
        this.croppie
            .result({ type: 'blob', quality: 1, format: 'png' })
            .then((image: Blob) => {
                this.croppedImages[this.numImg] = image as File;
                this.formDatas.push(new FormData());
                this.formDatas[this.numImg].append(
                    'file',
                    this.croppedImages[this.numImg]
                );
                this.promises.push(
                    this.imageClient
                        .postCoursePicture(this.formDatas[this.numImg])
                        .toPromise()
                );

                if (this.numImg < this.binaryEncodedImages.length - 1) {
                    this.startCroppie(croppieModal);
                }

                this.numImg++;
            })
            .catch((error) => {
                console.log(error);
                this.errorMsg =
                    'Das Bild konnte leider nicht gespeichert werden. ' +
                    'Bitte versuchen Sie es erneut.';
            });
    }

    public cancel(): void {
        this.files = [];
        this.binaryEncodedImages = [];
        this.croppedImages = [];
        this.numImg = 0;
    }

    private resetFormular(): void {
        this.event.name = '';
        this.event.description = '';
        this.event.maxParticipants = undefined;
        this.event.price = undefined;
        this.event.minAge = undefined;
        this.event.maxAge = undefined;
        this.endOfApplicationDate = null;
        this.startDate = null;
        this.toggleOptions = false;
        this.selectedRepeatOption = this.repeatOptions[0];
        this.terminateModul = this.terminateAfterOption[0];
        this.alleX = 1;
        this.endedX = 1;
    }

    public isRoomChoosed(): boolean {
        if (this.radioButtonSelected === '') {
            return false;
        }
        return true;
    }

    public isCompleted(): boolean {
        if (this.saveMode && (this.event.tag === undefined || this.event.tag === '')) {
            return false;
        }
        if (this.event.name === undefined || this.event.name === '') {
            return false;
        }
        if (
            this.event.description === undefined ||
            this.event.description === ''
        ) {
            return false;
        }
        if (this.event.price === undefined || this.event.price === null) {
            return false;
        }
        if (
            this.event.maxParticipants === undefined ||
            this.event.maxParticipants === null
        ) {
            return false;
        }
        if (this.event.minAge === undefined || this.event.minAge === null) {
            return false;
        }
        if (this.event.maxAge === undefined || this.event.maxAge === null) {
            return false;
        }
        if (this.isCreate) {
            if (this.startDate === undefined) {
                return false;
            }
            if (this.endTime === undefined) {
                return false;
            }
            if (this.radioButtonSelected === '') {
                return false;
            }
        }
        return true;
    }

    public greenSelected(): void {
        this.radioButtonSelected = 'Grün';
        this.otherRoom1String = 'Orange';
        this.otherRoom2String = 'Erdgeschoss';
    }

    public orangeSelected(): void {
        this.radioButtonSelected = 'Orange';
        this.otherRoom1String = 'Grün';
        this.otherRoom2String = 'Erdgeschoss';
    }

    public groundFloorSelected(): void {
        this.radioButtonSelected = 'Erdgeschoss';
        this.otherRoom1String = 'Grün';
        this.otherRoom2String = 'Orange';
    }

    public getSelectedRadioButtonRoom(): Room {
        if (this.radioButtonSelected === 'Grün') {
            return Room.Green;
        }
        if (this.radioButtonSelected === 'Orange') {
            return Room.Orange;
        }
        return Room.GroundFloor;
    }

    public clearInfoMsg(): void {
        this.errorMsg = undefined;
        this.successMsg = undefined;
    }

    private postData(form: NgForm): void {
        if (this.isCreate) {
            this.trainer.id = this.sessionService.userId;
            this.event.eventType = EventType.Course;
            this.event.trainer = this.trainer;

            this.roomUse.begin = this.dateTimeParser.dateTimeToString(
                this.startDate,
                this.startTime
            );
            this.roomUse.end = this.dateTimeParser.dateTimeToString(
                this.startDate,
                this.endTime
            );
            this.roomUse.room = this.getSelectedRadioButtonRoom();

            this.event.endOfApplication = this.dateTimeParser.dateTimeToString(
                this.endOfApplicationDate,
                this.endOfApplicationTime
            );

            if (this.toggleOptions) {
                this.roomUse.cronExpression = this.getCron();
                this.roomUse.roomOption = this.roomOption;
            }
            this.event.roomUses = [this.roomUse];
            this.loading = true;

            this.eventClient.postNewEvent(this.event).subscribe(
                (data: Event) => {
                    console.log(data);
                    this.successMsg =
                        'Deine Reservierung wurde erfolgreich gespeichert';
                    this.resetFormular();
                    this.errorMsg = '';
                    this.loading = false;
                },
                (error: Error) => {
                    console.log(error);
                    this.errorMsg = error.message;
                    this.successMsg = '';
                    this.loading = false;
                }
            );
        } else {
            // TODO
            this.eventClient.update(this.event).subscribe(
                (data: Event) => {
                    console.log(data);
                    this.successMsg = 'Der Kurs wurde erfolgreich aktualisiert';
                    this.errorMsg = '';
                    this.loading = false;
                },
                (error: Error) => {
                    console.log(error.message);
                    this.errorMsg =
                        'Der Kurs konnte nicht erfolgreich aktualisiert werden: ' +
                        error.message;
                    this.successMsg = '';
                    this.loading = false;
                }
            );
        }
    }

    private startCroppie(croppieModal: any): void {
        this.modalService.open(croppieModal, { size: 'lg' });

        setTimeout(() => {
            const img = document.getElementById('profilePicture');
            const boundaryWidth = 766;
            const viewportWidth = boundaryWidth - 50;
            const ratio = 16 / 9;

            this.croppie = new Croppie(img as HTMLImageElement, {
                boundary: {
                    width: boundaryWidth,
                    height: boundaryWidth / ratio,
                },
                viewport: {
                    width: viewportWidth,
                    height: viewportWidth / ratio,
                },
                showZoomer: true,
            });
        }, 100);
    }

    /**
     * This method can be used to extract the content of a file as binary data.
     * I.e. <img src"..."> can display images given their binary representation.
     *
     * @param file the wrapper of the content. 'File' can be also used as param as
     *             it extends Blob!
     */
    private extractBinaryDataFromFile(file: Blob, index: number): void {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onloadend = () => {
            this.binaryEncodedImages[index] = reader.result;
        };
    }
}
