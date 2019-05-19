import { Component, OnInit } from '@angular/core';
import { BREAKPOINTS } from 'src/utils/Breakpoints';

@Component({
    selector: 'app-navigation',
    templateUrl: './navigation.component.html',
    styleUrls: ['./navigation.component.scss'],
})
export class NavigationComponent implements OnInit {
    showMenu = false;

    constructor() {}

    ngOnInit() {}

    toggleMenu() {
        this.showMenu = !this.showMenu;
    }
}
