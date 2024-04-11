import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IFamily } from 'app/shared/model/family.model';
import { getEntities as getFamilies } from 'app/entities/family/family.reducer';
import { IPerson } from 'app/shared/model/person.model';
import { Gender } from 'app/shared/model/enumerations/gender.model';
import { getEntity, updateEntity, createEntity, reset } from './person.reducer';

export const PersonUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const families = useAppSelector(state => state.family.entities);
  const personEntity = useAppSelector(state => state.person.entity);
  const loading = useAppSelector(state => state.person.loading);
  const updating = useAppSelector(state => state.person.updating);
  const updateSuccess = useAppSelector(state => state.person.updateSuccess);
  const genderValues = Object.keys(Gender);

  const [isChecked, setIsChecked] = useState(personEntity.married);

  const handleClose = () => {
    navigate('/person');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getFamilies({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const handleCheckboxChange = event => {
    setIsChecked(event.target.checked);
  };

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.dateOfBirth = convertDateTimeToServer(values.dateOfBirth);

    const entity = {
      ...personEntity,
      ...values,
      family: families.find(it => it.id.toString() === values.family?.toString()),
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
          dateOfBirth: displayDefaultDateTime(),
        }
      : {
          gender: 'MALE',
          ...personEntity,
          dateOfBirth: convertDateTimeFromServer(personEntity.dateOfBirth),
          family: personEntity?.family?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="nayaksFamilyApp.person.home.createOrEditLabel" data-cy="PersonCreateUpdateHeading">
            Create or edit a Person
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="person-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Name" id="person-name" name="name" data-cy="name" type="text" />
              <ValidatedField label="Gender" id="person-gender" name="gender" data-cy="gender" type="select">
                {genderValues.map(gender => (
                  <option value={gender} key={gender}>
                    {gender}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label="Married"
                id="person-married"
                name="married"
                data-cy="married"
                check
                type="checkbox"
                checked={isChecked}
                onChange={handleCheckboxChange}
              />
              <ValidatedField label="About" id="person-about" name="about" data-cy="about" type="text" />
              <ValidatedField label="Fathers Name" id="person-fathersName" name="fathersName" data-cy="fathersName" type="text" />
              <ValidatedField
                label="Date Of Birth"
                id="person-dateOfBirth"
                name="dateOfBirth"
                data-cy="dateOfBirth"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label="Phone Number 1" id="person-phoneNumber1" name="phoneNumber1" data-cy="phoneNumber1" type="text" />
              <ValidatedField label="Phone Number 2" id="person-phoneNumber2" name="phoneNumber2" data-cy="phoneNumber2" type="text" />
              <ValidatedField label="Whats App No" id="person-whatsAppNo" name="whatsAppNo" data-cy="whatsAppNo" type="text" />
              <ValidatedField label="Email" id="person-email" name="email" data-cy="email" type="text" />
              {isChecked ? null : (
                <>
                  <ValidatedField
                    label="Current Location"
                    id="person-currentLocation"
                    name="currentLocation"
                    data-cy="currentLocation"
                    type="text"
                  />
                  <ValidatedBlobField label="Photo" id="person-photo" name="photo" data-cy="photo" isImage accept="image/*" />
                  <ValidatedField id="person-family" name="family" data-cy="family" label="Family" type="select">
                    <option value="" key="0" />
                    {families
                      ? families.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.name}
                          </option>
                        ))
                      : null}
                  </ValidatedField>
                </>
              )}
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/person" replace color="info">
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

export default PersonUpdate;
