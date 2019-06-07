import { Component, OnInit, AfterContentInit } from '@angular/core';
import { BREAKPOINTS } from 'src/app/utils/Breakpoints';
import { ActivatedRoute, Router } from '@angular/router';
import { UserDetails } from 'src/app/models/user-details';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { Authorities } from 'src/app/models/enum/authorities';
import { InternalUpdateService } from 'src/app/services/internal-update.service';

@Component({
    selector: 'app-navigation',
    templateUrl: './navigation.component.html',
    styleUrls: ['./navigation.component.scss'],
})
export class NavigationComponent implements OnInit {
    constructor(
        private authenticationService: AuthenticationService,
        private internalUpdateService: InternalUpdateService,
        private router: Router
    ) {}
    navLinks = [
        {
            name: 'Login',
            path: 'login',
            restriction: Authorities.NONE,
        },
        {
            name: 'Logout',
            path: '/calendar',
            restriction: Authorities.AUTHENTICATED,
        },
        {
            name: 'Kalender anzeigen',
            path: 'calendar',
            restriction: Authorities.NONE,
        },
        {
            name: 'Urlaub eintragen',
            path: 'holiday/add',
            restriction: Authorities.TRAINER,
        },
        {
            name: 'Beratung eintragen',
            path: 'consultation/add',
            restriction: Authorities.AUTHENTICATED,
        },
        {
            name: 'Geburtstag eintragen',
            path: 'birthday/book',
            restriction: Authorities.NONE,
        },
        {
            name: 'Kurs eintragen',
            path: 'course/add',
            restriction: Authorities.AUTHENTICATED,
        },
        { name: 'Raum Mieten', path: 'rent', restriction: Authorities.NONE },
        {
            name: 'Neuen Trainer anlegen',
            path: 'trainer/add',
            restriction: Authorities.ADMIN,
        },
        {
            name: 'Alle Trainer anzeigen',
            path: 'trainer/list',
            restriction: Authorities.ADMIN,
        },
        {
            name: 'Alle Kurse anzeigen',
            path: 'course/view',
            restriction: Authorities.ADMIN,
        },
    ];

    contextNavLinks: { name: string; path: string; restriction: Authorities }[];

    showMenu = false;
    route: ActivatedRoute;

    // status of user who visits this page
    user: UserDetails;

    ngOnInit() {
        this.contextNavLinks = this.navLinks;
        this.contextNavLinks = this.contextNavLinks.filter((elem) =>
            this.isAccessible(elem)
        );

        this.internalUpdateService.loginStatusChanges$.subscribe((update) => {
            this.updateNavigation();
        });
    }

    toggleMenu() {
        if (screen.width < BREAKPOINTS.xl) {
            this.showMenu = !this.showMenu;
        }
    }

    isAccessible(navLink: {
        name: string;
        path: string;
        restriction: Authorities;
    }): boolean {
        if (navLink.restriction === Authorities.NONE) {
            return true;
        }
        if (navLink.restriction === Authorities.ADMIN) {
            if (this.user == null) {
                return false;
            }
            if (this.user.roles.includes(Authorities.ADMIN)) {
                return true;
            } else {
                return false;
            }
        }

        if (navLink.restriction === Authorities.TRAINER) {
            if (this.user == null) {
                return false;
            }

            if (this.user.roles.includes(Authorities.TRAINER)) {
                return true;
            } else {
                return false;
            }
        }

        if (navLink.restriction === Authorities.AUTHENTICATED) {
            if (this.user == null) {
                return false;
            } else {
                return true;
            }
        }

        return true;
    }

    checkForLogout(navLink: {
        name: string;
        path: string;
        restriction: Authorities;
    }): void {
        if (navLink.name === 'Logout') {
            this.authenticationService.logout();
            this.internalUpdateService.notifyLogout();
            this.router.navigate(['/calendar']);
        }
    }

    private updateNavigation(): void {
        this.contextNavLinks = this.navLinks;
        if (this.authenticationService.isLoggedIn) {
            this.authenticationService.getUserDetails().subscribe(
                (data: UserDetails) => {
                    this.user = data;
                    this.contextNavLinks = this.contextNavLinks.filter((elem) =>
                        this.isAccessible(elem)
                    );
                },
                (error: Error) => {
                    console.log(error.message);
                }
            );
        } else {
            this.user = null;
            this.contextNavLinks = this.contextNavLinks.filter((elem) =>
                this.isAccessible(elem)
            );
        }
    }
}
