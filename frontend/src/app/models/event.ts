import { Trainer } from './trainer';
import { RoomUse } from './roomUse';
import { Customer } from './customer';
import { EventType } from './enum/eventType';
import { Tag } from './tag';

export class Event {
    public id: number;
    public name: string;
    public roomUses: RoomUse[];
    public created: Date;
    public updated: Date;
    public eventType: EventType;
    public customerDtos: Customer[];
    // used by non rent types
    public trainer: Trainer;
    // birthday specific
    public headcount: number;
    public ageToBe: number;
    public birthdayType: string;
    // course specific
    public endOfApplication: string;
    public price: number;
    public maxParticipants: number;
    public description: string;
    public minAge: number;
    public maxAge: number;
    //birthdays and courses
    public tag: string;
}
