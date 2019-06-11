import { Event } from '../models/event';
import { Room } from './enum/room';

export class RoomUse {
    public id: bigint;
    public begin: string;
    public end: string;
    public room: Room;
    public event: Event;
}
