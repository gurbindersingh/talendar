import { Component, OnInit, Input } from '@angular/core';
import { TrainerClient } from '../../rest/trainer-client';
import { Trainer } from '../../models/trainer';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';

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
    private trainerList: Trainer[] = [];
    filteredTrainerList: Trainer[] = [];
    trainerListPage: Trainer[] = [];
    currentPage = 1;
    itemsPerPage = 10;

    constructor(
        private trainerClient: TrainerClient,
        private modalService: NgbModal
    ) {}

    ngOnInit() {
        console.log('Init Trainer List');
        this.trainerClient.getAll().subscribe(
            (list: Trainer[]) => {
                this.trainerList = list;
                this.filteredTrainerList = this.trainerList;
                this.updateListPage();
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

    open(trainer: Trainer) {
        const modalRef = this.modalService.open(NgbdModalConfirm);
        modalRef.componentInstance.trainer = trainer;
        modalRef.result.then(() => {
            // on close
            console.log('Trying to DELETE Trainer with id ' + trainer.id);
            this.trainerClient.deleteTrainer(trainer.id).subscribe(
                () => {
                    this.ngOnInit();
                },
                (error) => {
                    console.log(error);
                }
            );
        });
    }
}
