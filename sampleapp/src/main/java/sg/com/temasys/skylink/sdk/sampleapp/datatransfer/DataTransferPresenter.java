package sg.com.temasys.skylink.sdk.sampleapp.datatransfer;

import android.content.Context;
import android.util.Log;

import sg.com.temasys.skylink.sdk.sampleapp.BasePresenter;
import sg.com.temasys.skylink.sdk.sampleapp.R;
import sg.com.temasys.skylink.sdk.sampleapp.service.DataTransferService;
import sg.com.temasys.skylink.sdk.sampleapp.service.model.PermRequesterInfo;
import sg.com.temasys.skylink.sdk.sampleapp.service.model.SkylinkPeer;
import sg.com.temasys.skylink.sdk.sampleapp.utils.Constants;
import sg.com.temasys.skylink.sdk.sampleapp.utils.PermissionUtils;

import static sg.com.temasys.skylink.sdk.sampleapp.utils.Utils.toastLog;

/**
 * Created by muoi.pham on 20/07/18.
 * This class is responsible for implementing data transfer logic.
 */

public class DataTransferPresenter extends BasePresenter implements DataTransferContract.Presenter {

    private final String TAG = DataTransferPresenter.class.getName();

    private Context context;

    private DataTransferContract.View dataTransferView;
    private DataTransferService dataTransferService;
    //utils to process permission
    private PermissionUtils permissionUtils;

    // the index of the peer on the action bar that user selected to send message privately
    // default is 0 - send message to all peers
    private int selectedPeerIndex = 0;

    public DataTransferPresenter(Context context) {
        this.context = context;
        this.dataTransferService = new DataTransferService(context);
        this.dataTransferService.setPresenter(this);
        this.permissionUtils = new PermissionUtils();
    }

    public void setView(DataTransferContract.View view) {
        dataTransferView = view;
        dataTransferView.setPresenter(this);
    }

    //----------------------------------------------------------------------------------------------
    // Override methods from BasePresenter for view to call
    // These methods are responsible for processing requests from view
    //----------------------------------------------------------------------------------------------

    /**
     * Triggered when View request data to display to the user when entering room
     * Try to connect to room when entering room
     */
    @Override
    public void processConnectedLayout() {

        Log.d(TAG, "onViewLayoutRequested");

        //start to connect to room when entering room
        //if not being connected, then connect
        if (!dataTransferService.isConnectingOrConnected()) {

            //connect to room on Skylink connection
            dataTransferService.connectToRoom(Constants.CONFIG_TYPE.DATA);

            //after connected to skylink SDK, UI will be updated later on processRoomConnected

            Log.d(TAG, "Try to connect when entering room");

        }
    }

    /**
     * process file permission that comes from the app
     * when user first choose browsing file from device, permission request dialog will be display
     */
    @Override
    public boolean processFilePermission() {
        return permissionUtils.requestFilePermission(context,
                dataTransferView.getInstance());
    }

    /**
     * display a warning if user deny the file permission
     */
    @Override
    public void processDenyPermission() {
        permissionUtils.displayFilePermissionWarning(context);
    }

    /**
     * process result of permission that comes from SDK
     */
    @Override
    public void processPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // delegate to the PermissionUtils to process the permission
        permissionUtils.onRequestPermissionsResultHandler(requestCode, permissions, grantResults, TAG);
    }

    /**
     * Save the current index of the selected peer
     */
    @Override
    public void processSelectRemotePeer(int index) {
        // check the selected index with the current selectedPeerIndex
        // if it is equal which means user in selects the peer
        if (this.selectedPeerIndex == index) {
            this.selectedPeerIndex = 0;
        } else {
            this.selectedPeerIndex = index;
        }
    }

    /**
     * Get the current index of selected peer
     */
    @Override
    public int processGetCurrentSelectedPeer() {
        return this.selectedPeerIndex;
    }

    /**
     * Get the specific peer object according to the index
     */
    @Override
    public SkylinkPeer processGetPeerByIndex(int index) {
        return dataTransferService.getPeerByIndex(index);
    }

    @Override
    public void processSendData(byte[] data) {
        // Do not allow button actions if there are no remote Peers in the room.
        if (dataTransferService.getTotalPeersInRoom() < 2) {
            String log = context.getString(R.string.warn_no_peer_message);
            toastLog(TAG, context, log);
            return;
        }

        // send data to all peers in room if user do not select any specific peer
        // send data to specific peer id if user choose the peer
        String remotePeerId = null;
        if (selectedPeerIndex != 0) {
            remotePeerId = dataTransferService.getPeerByIndex(selectedPeerIndex).getPeerId();
        }

        // delegate to service layer to implement sending data
        dataTransferService.sendData(remotePeerId, data);
    }

    @Override
    public void processExit() {
        //process disconnect from room
        dataTransferService.disconnectFromRoom();

        // need to call disposeLocalMedia to clear all local media objects as disconnectFromRoom no longer dispose local media
        dataTransferService.disposeLocalMedia();
        //after disconnected from skylink SDK, UI will be updated latter on processRoomDisconnected
    }

    //----------------------------------------------------------------------------------------------
    // Override methods from BasePresenter for service to call
    // These methods are responsible for processing requests from service
    //----------------------------------------------------------------------------------------------

    @Override
    public void processRoomConnected(boolean isSuccessful) {
        if (isSuccessful)
            processUpdateStateConnected();
    }

    @Override
    public void processRemotePeerConnected(SkylinkPeer newPeer) {
        // Fill the new peer in button in custom bar
        dataTransferView.updateUIRemotePeerConnected(newPeer,
                dataTransferService.getTotalPeersInRoom() - 2);
    }

    @Override
    public void processRemotePeerDisconnected(SkylinkPeer remotePeer, int removeIndex) {
        // do not process if the left peer is local peer
        if (removeIndex == -1)
            return;

        // Remove the peer in button in custom bar
        dataTransferView.updateUIRemotePeerDisconnected(dataTransferService.getPeersList());
    }

    /**
     * process SDK permission
     */
    @Override
    public void processPermissionRequired(PermRequesterInfo info) {
        // delegate to the PermissionUtils to process the permission
        permissionUtils.onPermissionRequiredHandler(info, TAG, context,
                dataTransferView.getInstance());
    }

    @Override
    public void processDataReceive(Context context, String remotePeerId, byte[] data) {
        SkylinkPeer remotePeer = dataTransferService.getPeerById(remotePeerId);
        dataTransferView.updateUIDataReceived(remotePeer, data);

        toastLog("DataTransfer", this.context, "You have received an array of data");
    }


    //----------------------------------------------------------------------------------------------
    // private methods for internal process
    //----------------------------------------------------------------------------------------------


    /**
     * Update UI when connected to room
     */
    private void processUpdateStateConnected() {

        // Update the view into connected state
        dataTransferView.updateUIConnected(processGetRoomId());
    }


    /**
     * Get the room id info
     */
    private String processGetRoomId() {
        return dataTransferService.getRoomId();
    }
}

