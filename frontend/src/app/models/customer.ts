import { Event } from '../models/event';

export class Customer {
    public id: number;
    public email: string;
    public phone: string;
    public firstName: string;
    public lastName: string;
    public events: Event[];
    public emailId: number;
}
