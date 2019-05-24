import {Event} from '../models/event';

export class RoomUse{
    public id: bigint;
    public begin: Date;
    public end: Date;
    public room: Room;
    public event: Event;
}

export enum Room{
    Green,
    Orange,
    GroundFloor
}