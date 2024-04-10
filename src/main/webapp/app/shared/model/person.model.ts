import dayjs from 'dayjs';
import { IFamily } from 'app/shared/model/family.model';
import { Gender } from 'app/shared/model/enumerations/gender.model';

export interface IPerson {
  id?: number;
  name?: string | null;
  gender?: keyof typeof Gender | null;
  about?: string | null;
  fathersName?: string | null;
  dateOfBirth?: dayjs.Dayjs | null;
  phoneNumber1?: string | null;
  phoneNumber2?: string | null;
  whatsAppNo?: string | null;
  email?: string | null;
  currentLocation?: string | null;
  photoContentType?: string | null;
  photo?: string | null;
  family?: IFamily | null;
}

export const defaultValue: Readonly<IPerson> = {};
