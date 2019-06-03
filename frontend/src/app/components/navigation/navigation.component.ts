import { Component, OnInit } from '@angular/core';
import { BREAKPOINTS } from 'src/app/utils/Breakpoints';

@Component({
    selector: 'app-navigation',
    templateUrl: './navigation.component.html',
    styleUrls: ['./navigation.component.scss'],
})
export class NavigationComponent implements OnInit {
    showMenu = false;

    navLinks = [
        { name: 'Kalender anzeigen', path: 'calendar' },
        { name: 'Urlaub eintragen', path: 'holiday' },
        { name: 'Beratung eintragen', path: 'consultation' },
        { name: 'Geburtstag eintragen', path: 'birthday' },
        { name: 'Kurs eintragen', path: 'course' },
        { name: 'Raum Mieten', path: 'rent' },
        { name: 'Neuen Trainer anlegen', path: 'trainer' },
        { name: 'Alle Trainer anzeigen', path: 'trainer/list' },
        { name: 'Alle Kurse anzeigen', path: 'course/view' },
    ];

    constructor() {}

    ngOnInit() {}

    toggleMenu() {
        if (screen.width < BREAKPOINTS.xl) {
            this.showMenu = !this.showMenu;
        }
    }
}
