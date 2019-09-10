import { Injectable } from '@angular/core';

import { Event } from '../models/event';
import { EventType } from '../models/enum/eventType';
import { RoomUse } from '../models/roomUse';

import * as Colors from '../utils/Colors';
import { Room } from '../models/enum/room';
import { Holiday } from '../models/holiday';

@Injectable({
    providedIn: 'root',
})
export class EventImportService {
    public mapAndAddHolidaysToEvents(
        events: Event[],
        holidays: Holiday[]
    ): Event[] {
        for (const holiday of holidays) {
            const event = new Event();
            event.name = holiday.title;
            event.title =
                event.name +
                ' ' +
                holiday.trainer.firstName +
                ' ' +
                holiday.trainer.lastName;
            event.description = holiday.description;
            event.start = new Date(holiday.holidayStart);
            event.end = new Date(holiday.holidayEnd);
            event.eventType = EventType.Holiday;
            event.color = Colors.Holiday;
            events.push(event);
            event.cssClass = 't-event-color';
        }
        return events;
    }

    // All holidays which are not from admin will get no name, only the information who created this
    public mapAndAddHolidaysToEventsById(
        events: Event[],
        holidays: Holiday[],
        id: number
    ): Event[] {
        for (const holiday of holidays) {
            const event = new Event();
            if (holiday.trainer && holiday.trainer.id === id) {
                event.title = holiday.title;
                event.name = holiday.title;
                event.description = holiday.description;
                event.color = Colors.Holiday;
            } else {
                event.title = holiday.title;
                event.name = holiday.title;
                event.color = Colors.HolidaySecondary;
            }
            event.start = new Date(holiday.holidayStart);
            event.end = new Date(holiday.holidayEnd);
            event.eventType = EventType.Holiday;
            events.push(event);
            event.cssClass = 't-event-color';
        }
        return events;
    }

    public mapEventsToCalendar(events: Event[]) {
        for (const event of events) {
            const roomOfEvent: RoomUse[] = event.roomUses;

            if (roomOfEvent.length > 1) {
                let i = 0;
                for (const roomUse of roomOfEvent) {
                    if (i === 0) {
                        event.start = new Date(roomUse.begin);
                        event.end = new Date(roomUse.end);
                        event.title = event.name;
                    } else {
                        const event2 = Object.assign({}, event);
                        event2.roomUses = [];
                        event2.roomUses.push(roomUse);
                        events.push(event2);
                    }
                    i++;
                }
            } else {
                event.start = new Date(roomOfEvent[0].begin);
                event.end = new Date(roomOfEvent[0].end);
                event.title = event.name;
            }

            // Calendar Events' specific properties (style, assigned actions etc)
            // this set up is not completed -> consider what has to be set...
            event.allDay = false;
            event.draggable = false;
            event.cssClass = 't-event-color';

            if (event.redacted || event.hide) {
                event.color = Colors.Reserved;
                let reservedName;
                const room = event.roomUses[0].room;

                if (room.includes(Room.Green)) {
                    reservedName = 'Grüner Raum';
                }
                if (room.includes(Room.Orange)) {
                    reservedName = 'Oranger Raum';
                }
                if (room.includes(Room.GroundFloor)) {
                    reservedName = 'Erdgeschoss';
                }
                reservedName += ' reserviert';
                event.title = reservedName;

                continue;
            }

            switch (event.eventType) {
                case EventType.Birthday:
                    event.color = Colors.Birthday;
                    break;

                case EventType.Course:
                    event.color = Colors.Course;
                    break;

                case EventType.Consultation:
                    event.color = Colors.Consultation;
                    break;

                case EventType.Holiday:
                    event.color = Colors.Holiday;
                    break;

                case EventType.Rent:
                    event.color = Colors.Rent;
                    break;

                default:
                    break;
            }
        }
        return events;
    }
}
