package com.mikhaellopez.dualshot.controller.activity;

import android.os.Bundle;

import com.mikhaellopez.dualshot.R;
import com.mikhaellopez.dualshot.controller.activity.generic.ABaseBackActivity;
import com.mikhaellopez.dualshot.controller.fragment.Camera2BasicFragment;
import com.mikhaellopez.dualshot.service.event.TakeImageEvent;

import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by Mikhael LOPEZ on 06/01/16.
 */
public class CameraActivity extends ABaseBackActivity {

    public static final String RESULT_BACK_PICTURE_PATH = "result_back_picture_path";
    public static final String RESULT_FRONT_PICTURE_PATH = "result_front_picture_path";

    private Camera2BasicFragment cameraInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // Register as a subscriber
        EventBus.getDefault().register(this);

        if (null == savedInstanceState) {
            cameraInstance = Camera2BasicFragment.newInstance();

            Bundle args = new Bundle();
            args.putBoolean(Camera2BasicFragment.ARG_CAMERA_FRONT, false);
            cameraInstance.setArguments(args);

            getFragmentManager().beginTransaction()
                    .replace(R.id.containerBack, cameraInstance)
                    .commit();
        }
    }

    @OnClick(R.id.picture)
    protected void pictureOnClick() {
        cameraInstance.takePicture();
    }

    public void onEventMainThread(TakeImageEvent event) {
        cameraInstance.showPicture();
        if (!event.isFront()) {
            cameraInstance = Camera2BasicFragment.newInstance();

            Bundle args = new Bundle();
            args.putBoolean(Camera2BasicFragment.ARG_CAMERA_FRONT, true);
            cameraInstance.setArguments(args);

            getFragmentManager().beginTransaction()
                    .replace(R.id.containerFront, cameraInstance)
                    .commit();
        } else {
            setResult(RESULT_OK, getIntent());
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        // Unregister
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
