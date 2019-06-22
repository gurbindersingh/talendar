import { Event } from './event';

export class Trainer {
    public id: number;
    public firstName: string;
    public lastName: string;
    public password: string;
    public email: string;
    public phone: string;
    public birthday: Date;
    public birthdayTypes: string[] = [];
    // the filepath where it is stored on server side
    public picture: string;
    public created: Date;
    public updated: Date;
    public events: Event;
    public deleted: boolean;
}
