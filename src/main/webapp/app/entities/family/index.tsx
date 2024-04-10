import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Family from './family';
import FamilyDetail from './family-detail';
import FamilyUpdate from './family-update';
import FamilyDeleteDialog from './family-delete-dialog';

const FamilyRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Family />} />
    <Route path="new" element={<FamilyUpdate />} />
    <Route path=":id">
      <Route index element={<FamilyDetail />} />
      <Route path="edit" element={<FamilyUpdate />} />
      <Route path="delete" element={<FamilyDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FamilyRoutes;
