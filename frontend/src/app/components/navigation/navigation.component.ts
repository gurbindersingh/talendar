import { Component, OnInit } from '@angular/core';
import { BREAKPOINTS } from 'src/app/utils/Breakpoints';
import { ActivatedRoute } from '@angular/router';

@Component({
    selector: 'app-navigation',
    templateUrl: './navigation.component.html',
    styleUrls: ['./navigation.component.scss'],
})
export class NavigationComponent implements OnInit {
    navLinks = [
        { name: 'Kalender anzeigen', path: 'calendar' },
        { name: 'Urlaub eintragen', path: 'holiday/add' },
        { name: 'Beratung eintragen', path: 'consultation/add' },
        { name: 'Geburtstag eintragen', path: 'birthday/book' },
        { name: 'Kurs eintragen', path: 'course/add' },
        { name: 'Raum Mieten', path: 'rent' },
        { name: 'Neuen Trainer anlegen', path: 'trainer/add' },
        { name: 'Alle Trainer anzeigen', path: 'trainer/list' },
        { name: 'Alle Kurse anzeigen', path: 'course/view' },
    ];
    showMenu = false;
    route: ActivatedRoute;

    constructor() {}

    ngOnInit() {}

    toggleMenu() {
        if (screen.width < BREAKPOINTS.xl) {
            this.showMenu = !this.showMenu;
        }
    }
}
