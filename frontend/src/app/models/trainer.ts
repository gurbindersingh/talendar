import { Event } from './event';

export class Trainer {
    public id: number;
    public firstName: string;
    public lastName: string;
    public email: string;
    public phone: string;
    public birthday: Date;
    public birthdayTypes: string[] = [];
    public created: Date;
    public updated: Date;
    public events: Event;
}
