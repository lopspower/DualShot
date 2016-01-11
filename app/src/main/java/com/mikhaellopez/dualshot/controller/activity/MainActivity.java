package com.mikhaellopez.dualshot.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikhaellopez.dualshot.R;
import com.mikhaellopez.dualshot.controller.activity.generic.ABaseActivity;
import com.mikhaellopez.dualshot.controller.fragment.Camera2BasicFragment;
import com.mikhaellopez.dualshot.library.SmallBang;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by mlopez on 07/01/16.
 */
public class MainActivity extends ABaseActivity {

    private static final String FUSION_FILE_NAME = "pic_fusion.jpg";

    @Bind(R.id.image_back)
    protected ImageView mImageBack;
    @Bind(R.id.image_front)
    protected ImageView mImageFront;
    @Bind(R.id.image_action)
    protected ImageView mImageAction;
    @Bind(R.id.text_nb_like)
    protected TextView mTextNbLike;
    @Bind(R.id.layout_value)
    protected View mLayoutValue;

    private SmallBang mSmallBang;
    private boolean mFlagIsLike = false;
    private boolean mPreviousPicture = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSmallBang = SmallBang.attach2Window(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    @OnClick(R.id.image_action)
    public void onClickAction(View view) {
        if (mPreviousPicture) {
            if (!mFlagIsLike) {
                mImageAction.setImageResource(R.drawable.ic_like_red);
                mSmallBang.bang(view);
                mTextNbLike.setText("1");
                mFlagIsLike = true;
            } else {
                mImageAction.setImageResource(R.drawable.ic_like);
                mTextNbLike.setText("0");
                mFlagIsLike = false;
            }
        } else {
            mImageAction.setImageResource(R.drawable.ic_camera);
            startActivityForResult(new Intent(this, CameraActivity.class), 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Create Fusion Image
                        Bitmap backBmp = BitmapFactory.decodeFile(getExternalFilesDir(null) + File.separator + Camera2BasicFragment.BACK_FILE_NAME);
                        Bitmap frontBmp = BitmapFactory.decodeFile(getExternalFilesDir(null) + File.separator + Camera2BasicFragment.FRONT_FILE_NAME);
                        Bitmap bmpFusion = overlay(backBmp, frontBmp);
                        // Save Fusion Image
                        FileOutputStream output = null;
                        try {
                            File file = new File(getExternalFilesDir(null), FUSION_FILE_NAME);
                            output = new FileOutputStream(file);
                            bmpFusion.compress(Bitmap.CompressFormat.JPEG, 100, output);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (null != output) {
                                try {
                                    output.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    }
                }).start();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    private void initView() {
        initViewWithImagesTakes();
        if (!mPreviousPicture) {
            mImageAction.setImageResource(R.drawable.ic_camera);
            mLayoutValue.setVisibility(View.INVISIBLE);
            mImageBack.setImageResource(0);
            mImageFront.setImageResource(0);
        } else {
            if (mFlagIsLike) {
                mImageAction.setImageResource(R.drawable.ic_like_red);
            } else {
                mImageAction.setImageResource(R.drawable.ic_like);
            }
            mLayoutValue.setVisibility(View.VISIBLE);
        }
    }

    private void initViewWithImagesTakes() {
        mPreviousPicture = loadImageViewWithPictureFile(mImageBack, getExternalFilesDir(null) + File.separator + Camera2BasicFragment.BACK_FILE_NAME)
                & loadImageViewWithPictureFile(mImageFront, getExternalFilesDir(null) + File.separator + Camera2BasicFragment.FRONT_FILE_NAME);
    }

    private boolean loadImageViewWithPictureFile(ImageView imageView, String picturePath) {
        File imageFile = new File(picturePath);
        if (imageFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            imageView.setImageBitmap(bitmap);
            return true;
        }
        return false;
    }

    private void shareImage(String pathFile) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///" + pathFile));
        startActivity(Intent.createChooser(share, "Share Image"));
    }

    private Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        bmp1 = Bitmap.createScaledBitmap(bmp1, bmp2.getWidth(), bmp2.getHeight(), true);
        Bitmap bmOverlay = Bitmap.createBitmap(bmp2.getWidth(), bmp2.getHeight() * 2, bmp2.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, 0, bmp1.getHeight(), null);

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawLine(bmp2.getHeight(), 0, bmp2.getHeight(), bmp2.getWidth(), paint);

        return bmOverlay;
    }

    //region Menu Implememtatiom
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear_picture:
                new File(getExternalFilesDir(null), Camera2BasicFragment.BACK_FILE_NAME).delete();
                new File(getExternalFilesDir(null), Camera2BasicFragment.FRONT_FILE_NAME).delete();
                new File(getExternalFilesDir(null), FUSION_FILE_NAME).delete();
                initView();
                break;
            case R.id.action_share_picture:
                File fusionFile = new File(getExternalFilesDir(null), FUSION_FILE_NAME);
                if (fusionFile.exists()) {
                    shareImage(fusionFile.getAbsolutePath());
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion
}
