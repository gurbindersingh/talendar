import { Trainer } from './trainer';

export class ConsultationTime {
    public id: number;
    public trainer: Trainer;
    public title: string;
    public description: string;
    public consultingTimeStart: string;
    public consultingTimeEnd: string;
}
