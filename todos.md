﻿# Talendar Scrum Board

## Tasks

### Grüne Raum darf nicht am we gebucht werden

-   Am Wochende soll der Obergeschoss gemeinsam gebucht werden(moglicherweise unter Orangenenraum gekennzeichnet)
-   Backend: Validierung und exception

## In Progress

### Trainer und Admin können nicht Titel/Beschreibung von andere Urlauben sehen

-   Sollte wenn man nach Trainer filtered als reserved angezeigt und im default sicht garnicht angezeigt werden. Nur für zugehörigen User sichtbar.

### Admin kann nicht beratungen sehen

-   Wird als Beratungstermin angezeigt. Kein Titel oder zusätzliche Informationen.

### Beratungen Trainer variable(Nikita)

-   Trainer sollten den Preis ihre Beratungen selbst festlegen
-   Frontend: eine Übersict oder gut plaziertes Feld dafür
-   Backend: Bei Trainer eine Variable die Ihren Preis angibt und eine möglichkeit dies zu bearbeiten

### Trainer dürfen Beratungszeiten angeben(Michi)

-   Wie Urlaube, Trainer haben nur gewisse Zeiten wo sie beratungen haben sollten
-   Frontend: Ein Menü wo man Beratungszeiten bearbeiten kann im trainer + beratung filter dieser Zeiten anzeigen
-   Backend: Über alle schichten ein Beratungszeiten CRUD objekt implementieren und die Methoden fürs Frontend

## Testing

-   Recaptcha beim anmelden (Guri)

### Ganzes haus buchen(Michi)

-   Sollte bei private Mietungen die Option geben den ganzes Haus zu buchen
-   Frontend: Extra select Feld bei Mietungen
-   Backend: Alle räume Buchen wenn das felt selected wird. Preis anpassen

### Geburtstag price eintragen bei Arterstellung(Nikita)

-   Geburtstagstypen sollen eigene Preise haben.
-   Frontend: Geburtstagtypen ubersicht einfuhren und bei geburtstag buchen Liste aus backend laden
-   Backend: Geburtstag CRUD funktionalitat und validierung

## Done

-
