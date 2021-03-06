import { Component, OnInit } from '@angular/core';
import { BREAKPOINTS } from 'src/app/utils/Breakpoints';
import { Router } from '@angular/router';
import { UserDetails } from 'src/app/models/user-details';
import { AuthenticationService, NotificationService } from 'src/app/services';
import { Authorities } from 'src/app/models/enum/authorities';

@Component({
    selector: 'app-navigation',
    templateUrl: './navigation.component.html',
    styleUrls: ['./navigation.component.scss'],
})
export class NavigationComponent implements OnInit {
    navLinks = [
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
            name: 'Beratungszeit eintragen',
            path: 'consultationtime/add',
            restriction: Authorities.TRAINER,
        },
        {
            name: 'Beratung eintragen',
            path: 'consultation/add',
            restriction: Authorities.NONE,
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
        {
            name: 'Raum mieten',
            path: 'rent',
            restriction: Authorities.NONE,
        },
        {
            name: 'Keywords eintragen',
            path: 'tag',
            restriction: Authorities.ADMIN,
        },
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
            restriction: Authorities.TRAINER,
        },
        {
            name: 'Geburtstagstypen anzeigen',
            path: 'birthdayType/view',
            restriction: Authorities.ADMIN,
        },
    ];

    loginLink = {
        name: 'Login',
        path: 'login',
        restriction: Authorities.UNAUTHENTICATED,
    };

    logoutLink = {
        name: 'Logout',
        path: 'calendar?logout',
        restriction: Authorities.AUTHENTICATED,
    };

    contextNavLinks: { name: string; path: string; restriction: Authorities }[];

    showMenu = false;

    // template properties
    authority: string;

    // status of user who visits this page
    user: UserDetails;

    constructor(
        public authenticationService: AuthenticationService,
        private notificationService: NotificationService,
        private router: Router
    ) { }

    ngOnInit() {
        this.updateNavigation();

        this.notificationService.loginStatusChanges$.subscribe((update) => {
            this.updateNavigation();
        });
    }

    toggleMenu() {
        if (screen.width < BREAKPOINTS.xl) {
            this.showMenu = !this.showMenu;
        }
    }

    /**
     * Check if logout clicked.
     *
     * If yes logout (will remove loggedIn Status from SessionStorage)
     * and notify this event to InternalUpdaeservice which will emit new event
     * for observables
     */
    logout(): void {
        this.authenticationService.logout();
        this.router.navigate(['/calendar']);
    }

    /**
     * Check if logged in and if yes load status of user (roles)
     *
     * After this update filter navigation links based on user type.
     */
    private updateNavigation(): void {
        this.contextNavLinks = this.navLinks;
        if (this.authenticationService.isLoggedIn) {
            this.authenticationService.getUserDetails().subscribe(
                (data: UserDetails) => {
                    this.user = data;
                    this.contextNavLinks = this.contextNavLinks.filter((elem) =>
                        this.isAccessible(elem)
                    );
                    // if (this.user.roles.includes(Authorities.ADMIN)) {
                    //     this.authority = 'Admin';
                    // } else {
                    //     this.authority = 'Betreuer';
                    // }
                },
                (error: Error) => {
                    
                }
            );
        } else {
            this.user = null;
            this.contextNavLinks = this.contextNavLinks.filter((elem) =>
                this.isAccessible(elem)
            );
            // this.authority = 'Gast';
        }
    }

    /**
     * Filter out navLinks based on access rights restrictions and the set
     * user profile (the active user)
     */
    isAccessible(navLink: {
        name: string;
        path: string;
        restriction: Authorities;
    }): boolean {
        if (navLink.restriction === Authorities.NONE) {
            return true;
        }

        if (navLink.restriction === Authorities.UNAUTHENTICATED) {
            return !this.authenticationService.isLoggedIn;
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
}
