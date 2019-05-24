import { RoomUse } from './roomUse';
import { Customer } from './customer';
import { Trainer } from './trainer';
import { EventType } from 'src/app/models/enum/eventType';

export class Event {
    public id: bigint;
    public name: string;
    public roomUses: RoomUse[];
    public created: Date;
    public updated: Date;
    public eventType: EventType;
    public customerDtos: Customer[];
    // using by non rent types
    public trainer: Trainer;
    // birthday specific
    public headCount: number;
    public ageToBe: number;
    public birthdayType: string;
    // course specific
    public endOfApplication: string;
    public price: number;
    public maxParticipants: number;
    public description: string;
    public minAge: number;
    public maxAge: number;
}
