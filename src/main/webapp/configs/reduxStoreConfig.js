/*
 * @sbansal
 *
 * Separated into Dev (with middleware) and Prod (without middleware) configurations
 *
 * Set up the redux store here, by making it aware of all reducers
 * and specifying any middleware, if needed
 */
import {applyMiddleware, compose, createStore, combineReducers} from 'redux';
import {createLogger} from 'redux-logger';
import reduxImmutableStateInvariant from 'redux-immutable-state-invariant';
import headerReducer from '../modules/commonLayout/header/reducer.js';
import accountReducer from '../modules/layout/body/reducer.js';
import createCiReducer from '../modules/clientInitiativeCreation/reducer.js';
import updateCIReducer from '../modules/clientInitiativeViewUpdate/reducer.js';
import createActivityReducer from '../modules/activityCreation/reducer.js';
import updateActivityReducer from '../modules/activityViewUpdate/reducer.js';
import srInquiryReducer from '../modules/srAnalystInquiry/reducer.js';
import srBriefingReducer from '../modules/srExpBriefing/reducer.js';
import editMemberReducer from '../modules/memberDetailsViewUpdate/reducer.js';
import manageMCPReducer from '../modules/manageMCP/reducer.js';
import templateReducer from '../modules/template/body/reducer.js';
import templateActivityReducer from '../modules/templateActivity/body/reducer.js';

const rootReducer = combineReducers({
	// define your reducers (comma seperated) here
});

//  configure redux store
export default function configureStore() {
  console.log('= Initializing redux store for ' + process.env.NODE_ENV + ' environment... ='); //process.env.NODE_ENV is defined in webpack define plugin
  if (process.env.NODE_ENV === 'production') {
    return createStore(rootReducer);
  } else {
    // Configure middleware only for non-production environment (added redux-logging, state mutation detector)
    const composedStore = compose(applyMiddleware(createLogger({collapsed: true}), reduxImmutableStateInvariant()))(createStore);
    return composedStore(rootReducer, window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__()); // supports redux-dev-tools
  }
}
