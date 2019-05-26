import {Event} from '../models/event';
import {Room} from './enum/room';

export class RoomUse{
    public id: bigint;
    public begin: String;
    public end: String;
    public room: Room;
    public event: Event;
}

