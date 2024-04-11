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

  const familyEntity = useAppSelector(state => state.family.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="familyDetailsHeading">Family</h2>
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
                    <img src={`data:${familyEntity.photoContentType};base64,${familyEntity.photo}`} style={{ maxHeight: '30px' }} />
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
