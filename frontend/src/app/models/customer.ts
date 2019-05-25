import {Event} from '../models/event';

export class Customer{
    public id: bigint;
    public email: string;
    public phone: string;
    public firstName: string;
    public lastName: string;
    public events: Event[];
}
}
