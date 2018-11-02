import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './python-result.reducer';
import { IPythonResult } from 'app/shared/model/python-result.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPythonResultDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class PythonResultDetail extends React.Component<IPythonResultDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { pythonResultEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            PythonResult [<b>{pythonResultEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="requestHash">Request Hash</span>
            </dt>
            <dd>{pythonResultEntity.requestHash}</dd>
            <dt>
              <span id="input">Input</span>
            </dt>
            <dd>{pythonResultEntity.input}</dd>
            <dt>
              <span id="output">Output</span>
            </dt>
            <dd>{pythonResultEntity.output}</dd>
          </dl>
          <Button tag={Link} to="/entity/python-result" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/python-result/${pythonResultEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ pythonResult }: IRootState) => ({
  pythonResultEntity: pythonResult.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PythonResultDetail);
