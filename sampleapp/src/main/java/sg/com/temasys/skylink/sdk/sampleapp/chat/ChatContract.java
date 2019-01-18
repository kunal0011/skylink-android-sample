package sg.com.temasys.skylink.sdk.sampleapp.chat;

import java.util.List;

import sg.com.temasys.skylink.sdk.sampleapp.BaseService;
import sg.com.temasys.skylink.sdk.sampleapp.BaseView;
import sg.com.temasys.skylink.sdk.sampleapp.service.model.SkylinkPeer;

/**
 * Created by muoi.pham on 20/07/18.
 * This interface is responsible for specify behaviors of View, Presenter, Service
 */

public interface ChatContract {

    interface View extends BaseView<Presenter> {

        /**
         * Update info about the connected room {roomId}
         */
        void onPresenterRequestUpdateRoomInfo(String roomInfo);

        /**
         * Update info about the local peer in action bar
         */
        void onPresenterRequestUpdateUIConnected(String localUserName);

        /**
         * Update UI details when new remote peer joins at a specific index the room
         */
        void onPresenterRequestChangeUiRemotePeerJoin(SkylinkPeer newPeer, int index);

        /**
         * Update UI details when peers are in room
         */
        void onPresenterRequestChangeUiRemotePeerLeft(List<SkylinkPeer> peersList);

        /**
         * Update UI details when need to clear the input
         */
        void onPresenterRequestClearInput();

        /**
         * Update UI details when we need to update the messages list
         */
        void onPresenterRequestUpdateChatCollection();
    }

    interface Presenter {

        /**
         * process data to display on view
         */
        void onViewRequestConnectedLayout();

        /**
         * get list of chat message
         */
        List<String> onViewRequestGetChatCollection();

        /**
         * process change state when view exit/closed
         */
        void onViewRequestExit();

        /**
         * process get peer info at specific index
         */
        SkylinkPeer onViewRequestGetPeerByIndex(int index);

        /**
         * process selecting the specific remote peer to send message to
         */
        void onViewRequestSelectedRemotePeer(int index);

        /**
         * process sending message
         */
        void onViewRequestSendMessage(String message);

        /**
         * process selecting message type: server or P2P
         */
        void onViewRequestSelectedMessageType(ChatPresenter.MESSAGE_TYPE message_type);

        /**
         * process get current selected peer index
         */
        int onViewRequestGetCurrentSelectedPeer();
    }

    interface Service extends BaseService<Presenter> {

    }
}
