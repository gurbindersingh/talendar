import {Event} from '../models/event';

export class RoomUse{
    public id: bigint;
    public begin: String;
    public end: String;
    public room: Room;
    public event: Event;
}

export enum Room{
    Green,
    Orange,
    GroundFloor
}