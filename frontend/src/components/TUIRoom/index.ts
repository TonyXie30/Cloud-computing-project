import ConferenceMainView from './conference.vue';
import PreConferenceView from './preConference.vue';
import TUIRoomEngine from '@tencentcloud/tuiroom-engine-js';
import { conference, RoomEvent } from './conference';
import RoomMessageCard from './extension/RoomMessageCard/RoomMessageCard.vue';
export * from './components/common/base/index';

export { ConferenceMainView, PreConferenceView, TUIRoomEngine, conference, RoomEvent, RoomMessageCard };

ConferenceMainView.install = (app) => {
  app.component('ConferenceView', ConferenceMainView);
};

export default ConferenceMainView;


