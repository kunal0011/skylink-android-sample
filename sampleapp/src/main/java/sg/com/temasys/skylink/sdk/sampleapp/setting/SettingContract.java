package sg.com.temasys.skylink.sdk.sampleapp.setting;

import sg.com.temasys.skylink.sdk.sampleapp.BaseView;

/**
 * Created by muoi.pham on 28/08/18.
 */
public interface SettingContract {
    interface View extends BaseView<Presenter> {

        void onAudioHeadsetSelected();

        void onAudioSpeakerSelected();

        void onVideoHeadsetSelected();

        void onVideoSpeakerSelected();

        void onVideoResVGASelected();

        void onVideoResHDRSelected();

        void onVideoResFHDSelected();

        void onCameraFrontSelected();

        void onCameraBackSelected();
    }

    interface Presenter{

        void onProcessAudioSpeaker(boolean isAudioSpeaker);

        void onProcessVideoSpeaker(boolean isVideoSpeaker);

        void onProcessCameraBack(boolean isCameraBack);

        void onProcessVideoResolution(Config.VideoResolution videoResolution);

        void onViewExit();

        void onViewLayoutRequested();
    }
}