import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IPythonResult, defaultValue } from 'app/shared/model/python-result.model';

export const ACTION_TYPES = {
  FETCH_PYTHONRESULT_LIST: 'pythonResult/FETCH_PYTHONRESULT_LIST',
  FETCH_PYTHONRESULT: 'pythonResult/FETCH_PYTHONRESULT',
  CREATE_PYTHONRESULT: 'pythonResult/CREATE_PYTHONRESULT',
  UPDATE_PYTHONRESULT: 'pythonResult/UPDATE_PYTHONRESULT',
  DELETE_PYTHONRESULT: 'pythonResult/DELETE_PYTHONRESULT',
  RESET: 'pythonResult/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPythonResult>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type PythonResultState = Readonly<typeof initialState>;

// Reducer

export default (state: PythonResultState = initialState, action): PythonResultState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PYTHONRESULT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PYTHONRESULT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_PYTHONRESULT):
    case REQUEST(ACTION_TYPES.UPDATE_PYTHONRESULT):
    case REQUEST(ACTION_TYPES.DELETE_PYTHONRESULT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_PYTHONRESULT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PYTHONRESULT):
    case FAILURE(ACTION_TYPES.CREATE_PYTHONRESULT):
    case FAILURE(ACTION_TYPES.UPDATE_PYTHONRESULT):
    case FAILURE(ACTION_TYPES.DELETE_PYTHONRESULT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_PYTHONRESULT_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_PYTHONRESULT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_PYTHONRESULT):
    case SUCCESS(ACTION_TYPES.UPDATE_PYTHONRESULT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_PYTHONRESULT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/python-results';

// Actions

export const getEntities: ICrudGetAllAction<IPythonResult> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_PYTHONRESULT_LIST,
  payload: axios.get<IPythonResult>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IPythonResult> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PYTHONRESULT,
    payload: axios.get<IPythonResult>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IPythonResult> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PYTHONRESULT,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IPythonResult> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PYTHONRESULT,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPythonResult> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PYTHONRESULT,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
