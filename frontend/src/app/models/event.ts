import {Trainer} from './trainer';
import {RoomUse} from './roomUse';
import {Customer} from './customer';


export class Event{
    public id: bigint;
    public name: string;
    public roomUses: RoomUse[];
    public created: Date;
    public updated: Date;
    public eventType: EventType;
    public customerDtos: Customer[];
    public trainer: Trainer;
    public headcount: number;
    public ageToBe: number;
    public birthdayType: string;
    public endOfApplication: Date;
    public price: number;
    public maxParticipant: number;
    public description: string;
    public minAge: number;
    public maxAge: number;
}

export enum EventType{
    Birthday,
    Consultation,
    Course,
    Rent
}