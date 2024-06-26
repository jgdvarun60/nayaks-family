import dayjs from 'dayjs';
import { IPerson } from 'app/shared/model/person.model';

export interface IFamily {
  id?: number;
  name?: string | null;
  motherMaidenName?: string | null;
  currentLocation?: string | null;
  marriageDate?: dayjs.Dayjs | null;
  familyPhotoContentType?: string | null;
  familyPhoto?: string | null;
  father?: IPerson | null;
  mother?: IPerson | null;
}

export const defaultValue: Readonly<IFamily> = {};
