import { Component, OnInit, Input } from '@angular/core';
import { TrainerClient, ImageClient } from 'src/app/rest';
import { Trainer } from 'src/app/models/trainer';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AuthenticationService, SessionStorageService } from 'src/app/services';
import { UserDetails } from 'src/app/models/user-details';
import { Authorities } from 'src/app/models/enum/authorities';

@Component({
    selector: 'ngbd-modal-confirm',
    template: `
        <div class="modal-header">
            <h4 class="modal-title" id="modal-title">Trainer löschen</h4>
            <button
                type="button"
                class="close"
                aria-describedby="modal-title"
                (click)="modal.dismiss('Cross click')"
            >
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
        <div class="modal-body">
            <p>
                <strong
                    >Soll Trainer
                    <span class="text-primary">{{
                        trainer.firstName + ' ' + trainer.lastName
                    }}</span>
                    wirklich gelöscht werden?
                </strong>
            </p>
            <p>
                Alle mit diesem Profil verbundenen Informationen werden
                gelöscht.
                <span class="text-danger"
                    >Diese Aktion kann nicht rückgängig gemacht werden.</span
                >
            </p>
        </div>
        <div class="modal-footer">
            <button
                type="button"
                class="btn btn-outline-secondary"
                (click)="modal.dismiss('cancel click')"
            >
                Cancel
            </button>
            <button
                type="button"
                class="btn btn-danger"
                (click)="modal.close('Ok click')"
            >
                Ok
            </button>
        </div>
    `,
})
export class NgbdModalConfirm {
    @Input() trainer: Trainer;
    @Input() trainerClient: TrainerClient;
    constructor(public modal: NgbActiveModal) {}
}

@Component({
    selector: 'app-trainer-list',
    templateUrl: './trainer-list.component.html',
    styleUrls: ['./trainer-list.component.scss'],
})
export class TrainerListComponent implements OnInit {
    title = 'Trainerliste';
    trainerList: Trainer[] = [];
    filteredTrainerList: Trainer[] = [];
    trainerListPage: Trainer[] = [];
    currentPage = 1;
    itemsPerPage = 8;

    private trainerIdsToHtmlElem: number[] = [];
    private role: Authorities;

    constructor(
        private trainerClient: TrainerClient,
        private imageClient: ImageClient,
        private modalService: NgbModal,
        private authenticationService: AuthenticationService,
        private sessionService: SessionStorageService
    ) {}

    binaryEncodedImages: any[] = [];

    ngOnInit() {
        // pre init role of this user
        this.authenticationService
            .getUserDetails()
            .subscribe((userDetails: UserDetails) => {
                if (userDetails.roles.includes(Authorities.ADMIN)) {
                    this.role = Authorities.ADMIN;
                } else if (userDetails.roles.includes(Authorities.TRAINER)) {
                    this.role = Authorities.TRAINER;
                }
            });

        this.trainerClient.getAll().subscribe(
            (list: Trainer[]) => {
                this.trainerList = list;
                this.filteredTrainerList = this.trainerList;
                this.updateListPage();
                this.binaryEncodedImages = new Array(this.trainerList.length);

                for (let i = 0; i < this.trainerList.length; i++) {
                    const trainer: Trainer = this.trainerList[i];
                    // load id into helper array
                    this.trainerIdsToHtmlElem.push(trainer.id);

                    if (trainer.picture != null) {
                        this.imageClient
                            .getProfilePicture(trainer.picture)
                            .subscribe(
                                // received data are octet stream (pure 'raw' binary data)
                                (bytes: any) => {
                                    const blob = new Blob([bytes]);
                                    this.extractBinaryDataFromFile(blob, i);
                                },
                                (error) => {
                                    // manual parsing required because (returntype is not json)
                                    const info = JSON.parse(error);
                                    console.log(
                                        'profile picture could not be loaded: ' +
                                            info.message
                                    );
                                }
                            );
                    }
                }
            },
            (error) => {
                console.error(error);
            }
        );
    }

    updateListPage(page?: number) {
        this.trainerListPage = this.filteredTrainerList.slice(
            (this.currentPage - 1) * this.itemsPerPage,
            this.currentPage * this.itemsPerPage
        );
    }

    filterList(pValue: string) {
        // Think about splitting up the string at white spaces
        const searchString = pValue
            .replace(/^\s+/, '') // Remove whitespaces at the start of the string
            .replace(/\s+$/, '') // Remove whitespaces at the end
            .replace(/\s{2,}/g, ' ') // Remove subsequent whitespaces
            .toLocaleLowerCase();

        if (searchString.length > 0 && searchString !== ' ') {
            this.filteredTrainerList = this.trainerList.filter(
                (trainer) =>
                    trainer.firstName
                        .toLocaleLowerCase()
                        .includes(searchString) ||
                    trainer.lastName.toLocaleLowerCase().includes(searchString)
            );
        } else {
            this.filteredTrainerList = this.trainerList;
        }
        this.updateListPage();
    }

    delete(trainer: Trainer, index: number) {
        const modalRef = this.modalService.open(NgbdModalConfirm);
        modalRef.componentInstance.trainer = trainer;
        modalRef.result.then(() => {
            // on close
            this.filteredTrainerList = this.filteredTrainerList.filter(
                (elem) => elem.id !== trainer.id
            );
            this.updateListPage();

            this.trainerClient.deleteTrainer(trainer.id).subscribe(
                () => {
                    this.ngOnInit();
                    this.binaryEncodedImages[index] = null;
                },
                (error) => {
                    console.log(error);
                }
            );
        });
    }

    isDeletionAllowed(index: number): boolean {
        if (this.role === Authorities.TRAINER) {
            return false;
        }

        if (this.sessionService.userId === this.trainerIdsToHtmlElem[index]) {
            return false;
        }

        return true;
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
