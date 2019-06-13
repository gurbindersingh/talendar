import { Injectable } from '@angular/core';

import { Event } from '../models/event';
import { EventType } from '../models/enum/eventType';
import { RoomUse } from '../models/roomUse';

import { BirthdayColors } from '../utils/BirthdayColors';
import { ConsultationColors } from '../utils/ConsultationColors';
import { CourseColors } from '../utils/CourseColors';
import { HolidayColors } from '../utils/HolidayColors';
import { RentColors } from '../utils/RentColors';

@Injectable({
    providedIn: 'root',
})
export class EventImportService {
    public mapEventsToCalendar(events: Event[]) {
        for (const event of events) {
            const roomOfEvent: RoomUse[] = event.roomUses;

            event.start = new Date(roomOfEvent[0].begin);
            event.end = new Date(roomOfEvent[0].end);
            event.title = event.name;

            // Calendar Events' specific properties (style, assigned actions etc)
            // this set up is not completed -> consider what has to be set...
            event.allDay = false;
            event.draggable = false;
            event.cssClass = 't-event-color';

            switch (event.eventType) {
                case EventType.Birthday:
                    event.color = BirthdayColors;
                    break;

                case EventType.Course:
                    event.color = CourseColors;
                    break;

                case EventType.Consultation:
                    event.color = ConsultationColors;
                    break;

                case EventType.Holiday:
                    event.color = HolidayColors;
                    break;

                case EventType.Rent:
                    event.color = RentColors;
                    break;

                default:
                    break;
            }
            console.warn(event.color);
        }
        return events;
    }
}
