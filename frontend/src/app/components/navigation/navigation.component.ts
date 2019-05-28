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
        { name: 'Kalender anzeigen', path: '' },
        { name: 'Urlaub eintragen', path: 'holiday' },
        { name: 'Beratung eintragen', path: 'consultation' },
        { name: 'Geburtstag eintragen', path: 'birthday' },
        { name: 'Kurs eintragen', path: 'course' },
        { name: 'Raum Mieten', path: 'meeting' },
        { name: 'Neuen Trainer anlegen', path: 'trainer' },
        { name: 'Alle Trainer anzeigen', path: 'trainerList' },
        { name: 'Alle Kurse anzeigen', path: 'courseView' },
    ];

    constructor() {}

    ngOnInit() {}

    toggleMenu() {
        this.showMenu = !this.showMenu;
    }
}
