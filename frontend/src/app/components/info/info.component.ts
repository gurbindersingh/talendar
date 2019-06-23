import { Component, OnInit } from '@angular/core';
import { InfoClient } from 'src/app/rest/info-client';

@Component({
    selector: 'app-info',
    templateUrl: './info.component.html',
    styleUrls: ['./info.component.scss'],
})
export class InfoComponent implements OnInit {
    title = 'E-Mail-basierter Informationsexport von Kundendaten';
    mail: string;
    errorMsg: string;
    successMsg: string;

    constructor(private infoClient: InfoClient) {}
    ngOnInit() {}

    public downloadInfoPdf(): void {
        this.clearInfoMsg();
        this.infoClient.getPdf(this.mail).subscribe(
            (response: any) => {
                
                const file = new Blob([response], { type: 'application/pdf' });
                const link = window.URL.createObjectURL(file);
                const paw = window.open(link);
                this.successMsg =
                    'Das PDF konnte erfolgreich erstellt und geöffnet werden.';
            },
            (error: Error) => {
                
                this.errorMsg =
                    'Es konnten am Server keine Kunden gefunden werden,' +
                    ' die diese E-Mail-Adresse haben';
            }
        );
    }

    public clearInfoMsg(): void {
        this.errorMsg = undefined;
        this.successMsg = undefined;
    }
}
