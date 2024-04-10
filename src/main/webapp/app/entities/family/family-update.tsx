import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPerson } from 'app/shared/model/person.model';
import { getEntities as getPeople } from 'app/entities/person/person.reducer';
import { IFamily } from 'app/shared/model/family.model';
import { getEntity, updateEntity, createEntity, reset } from './family.reducer';

export const FamilyUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const people = useAppSelector(state => state.person.entities);
  const familyEntity = useAppSelector(state => state.family.entity);
  const loading = useAppSelector(state => state.family.loading);
  const updating = useAppSelector(state => state.family.updating);
  const updateSuccess = useAppSelector(state => state.family.updateSuccess);

  const handleClose = () => {
    navigate('/family');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPeople({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.marriageDate = convertDateTimeToServer(values.marriageDate);

    const entity = {
      ...familyEntity,
      ...values,
      father: people.find(it => it.id.toString() === values.father?.toString()),
      mother: people.find(it => it.id.toString() === values.mother?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          marriageDate: displayDefaultDateTime(),
        }
      : {
          ...familyEntity,
          marriageDate: convertDateTimeFromServer(familyEntity.marriageDate),
          father: familyEntity?.father?.id,
          mother: familyEntity?.mother?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="nayaksFamilyApp.family.home.createOrEditLabel" data-cy="FamilyCreateUpdateHeading">
            Create or edit a Family
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="family-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Name" id="family-name" name="name" data-cy="name" type="text" />
              <ValidatedField
                label="Mother Maiden Name"
                id="family-motherMaidenName"
                name="motherMaidenName"
                data-cy="motherMaidenName"
                type="text"
              />
              <ValidatedField
                label="Marriage Date"
                id="family-marriageDate"
                name="marriageDate"
                data-cy="marriageDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedBlobField
                label="Family Photo"
                id="family-familyPhoto"
                name="familyPhoto"
                data-cy="familyPhoto"
                isImage
                accept="image/*"
              />
              <ValidatedField id="family-father" name="father" data-cy="father" label="Father" type="select">
                <option value="" key="0" />
                {people
                  ? people.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="family-mother" name="mother" data-cy="mother" label="Mother" type="select">
                <option value="" key="0" />
                {people
                  ? people.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/family" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default FamilyUpdate;
