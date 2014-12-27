package com.temasys.skylink.sample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Split2Fragment extends RoomFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_split_2, container, false);

        if (RoomManager.get().getRemoteVideoList().size() == 2) {
            ViewGroup parent = (ViewGroup) rootView.findViewById(R.id.video1);
            RoomManager.VideoInfo videoInfo = RoomManager.get().getRemoteVideoList().get(0);
            addVideoView(parent, videoInfo.getVideoView(),
                    videoInfo.getPeerId());
            // Set chat alerts.
            RoomManager.get().setChatAlert(rootView, videoInfo,
                    R.id.chatPrivate1, R.id.chatGroup1);

            parent = (ViewGroup) rootView.findViewById(R.id.video2);
            videoInfo = RoomManager.get().getRemoteVideoList().get(1);
            addVideoView(parent, videoInfo.getVideoView(),
                    videoInfo.getPeerId());
            // Set chat alerts.
            RoomManager.get().setChatAlert(rootView, videoInfo,
                    R.id.chatPrivate2, R.id.chatGroup2);
        }

        return rootView;
    }

}
