import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './person.reducer';

export const PersonDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const personEntity = useAppSelector(state => state.person.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="personDetailsHeading">Person</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{personEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{personEntity.name}</dd>
          <dt>
            <span id="gender">Gender</span>
          </dt>
          <dd>{personEntity.gender}</dd>
          <dt>
            <span id="about">About</span>
          </dt>
          <dd>{personEntity.about}</dd>
          <dt>
            <span id="fathersName">Fathers Name</span>
          </dt>
          <dd>{personEntity.fathersName}</dd>
          <dt>
            <span id="dateOfBirth">Date Of Birth</span>
          </dt>
          <dd>{personEntity.dateOfBirth ? <TextFormat value={personEntity.dateOfBirth} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="phoneNumber1">Phone Number 1</span>
          </dt>
          <dd>{personEntity.phoneNumber1}</dd>
          <dt>
            <span id="phoneNumber2">Phone Number 2</span>
          </dt>
          <dd>{personEntity.phoneNumber2}</dd>
          <dt>
            <span id="whatsAppNo">Whats App No</span>
          </dt>
          <dd>{personEntity.whatsAppNo}</dd>
          <dt>
            <span id="email">Email</span>
          </dt>
          <dd>{personEntity.email}</dd>
          <dt>
            <span id="currentLocation">Current Location</span>
          </dt>
          <dd>{personEntity.currentLocation}</dd>
          <dt>
            <span id="photo">Photo</span>
          </dt>
          <dd>
            {personEntity.photo ? (
              <div>
                {personEntity.photoContentType ? (
                  <a onClick={openFile(personEntity.photoContentType, personEntity.photo)}>
                    <img src={`data:${personEntity.photoContentType};base64,${personEntity.photo}`} style={{ maxHeight: '30px' }} />
                  </a>
                ) : null}
                <span>
                  {personEntity.photoContentType}, {byteSize(personEntity.photo)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>Family</dt>
          <dd>{personEntity.family ? personEntity.family.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/person" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/person/${personEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default PersonDetail;
