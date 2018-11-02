import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import PythonResult from './python-result';
import PythonResultDetail from './python-result-detail';
import PythonResultUpdate from './python-result-update';
import PythonResultDeleteDialog from './python-result-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PythonResultUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PythonResultUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PythonResultDetail} />
      <ErrorBoundaryRoute path={match.url} component={PythonResult} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={PythonResultDeleteDialog} />
  </>
);

export default Routes;
