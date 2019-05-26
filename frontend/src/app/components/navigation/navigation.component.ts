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
        { name: 'Urlaub eintragen', path: 'holiday' },
        { name: 'Beratung eintragen', path: 'consultation' },
        { name: 'Geburtstag eintragen', path: 'birthday' },
        { name: 'Kurs eintragen', path: 'course' },
        { name: 'Meeting eintragen', path: 'meeting' },
        { name: 'Neuen Trainer anlegen', path: 'trainer' },
        { name: 'Trainer anzeigen', path: 'trainerList' },
    ];

    constructor() {}

    ngOnInit() {}

    toggleMenu() {
        this.showMenu = !this.showMenu;
    }
}
