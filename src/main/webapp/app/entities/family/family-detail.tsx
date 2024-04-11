import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './family.reducer';

export const FamilyDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  // id?: number;
  // name?: string | null;
  // gender?: keyof typeof Gender | null;
  // married?: boolean | null;
  // about?: string | null;
  // fathersName?: string | null;
  // dateOfBirth?: dayjs.Dayjs | null;
  // phoneNumber1?: string | null;
  // phoneNumber2?: string | null;
  // whatsAppNo?: string | null;
  // email?: string | null;
  // currentLocation?: string | null;
  // photoContentType?: string | null;
  // photo?: string | null;
  // family?: IFamily | null;

  const familyEntity = useAppSelector(state => state.family.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="familyDetailsHeading">Family</h2>
        <table style={{ width: '35%', tableLayout: 'fixed', borderCollapse: 'collapse' }}>
          <tr>
            <td style={{ textAlign: 'center', verticalAlign: 'middle', border: '1px solid black' }}>
              <div style={{ textAlign: 'left' }}>
                🏠: {familyEntity.currentLocation}
                <br />
                👨: {familyEntity.father.name} <br />
                🎂: {familyEntity.father.dateOfBirth} <br />
                📲: {familyEntity.father.phoneNumber1} <br />
                📱: {familyEntity.father.phoneNumber1} <br />
                ✆: {familyEntity.father.phoneNumber1} <br />
                👧: {familyEntity.mother.name} <br />
                👩: {familyEntity.motherMaidenName} <br />
                🎂: {familyEntity.mother.dateOfBirth} <br />
                📲: {familyEntity.mother.phoneNumber1} <br />
                📱: {familyEntity.mother.phoneNumber1} <br />
                ✆: {familyEntity.mother.phoneNumber1} <br />
                💏: {familyEntity.marriageDate}
              </div>
            </td>
            <td style={{ textAlign: 'center', verticalAlign: 'middle', border: '1px solid black' }}>
              <img
                src="https://ucarecdn.com/4a3425bf-8368-4a88-9ef1-b9c1278e0377/-/resize/700/"
                alt="Image description"
                style={{ width: '100%', height: 'auto', maxWidth: '300px' }}
              />
            </td>
          </tr>
        </table>
        <br />
        <br />
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{familyEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{familyEntity.name}</dd>
          <dt>
            <span id="motherMaidenName">Mother Maiden Name</span>
          </dt>
          <dd>{familyEntity.motherMaidenName}</dd>
          <dt>
            <span id="currentLocation">Current Location</span>
          </dt>
          <dd>{familyEntity.currentLocation}</dd>
          <dt>
            <span id="marriageDate">Marriage Date</span>
          </dt>
          <dd>
            {familyEntity.marriageDate ? <TextFormat value={familyEntity.marriageDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="familyPhoto">Family Photo</span>
          </dt>
          <dd>
            {familyEntity.familyPhoto ? (
              <div>
                {familyEntity.familyPhotoContentType ? (
                  <a onClick={openFile(familyEntity.familyPhotoContentType, familyEntity.familyPhoto)}>
                    <img
                      src={`data:${familyEntity.familyPhotoContentType};base64,${familyEntity.familyPhoto}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                ) : null}
                <span>
                  {familyEntity.familyPhotoContentType}, {byteSize(familyEntity.familyPhoto)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>Father</dt>
          <dd>{familyEntity.father ? familyEntity.father.name : ''}</dd>
          <dt>Mother</dt>
          <dd>{familyEntity.mother ? familyEntity.mother.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/family" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/family/${familyEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default FamilyDetail;
