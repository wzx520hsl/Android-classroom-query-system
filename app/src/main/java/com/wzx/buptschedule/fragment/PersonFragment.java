package com.wzx.buptschedule.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;
import com.avoscloud.leanchatlib.utils.PhotoUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wzx.buptschedule.R;
import com.wzx.buptschedule.TasApp;
import com.wzx.buptschedule.activity.LoginActivity;
import com.wzx.buptschedule.activity.MainActivity;
import com.wzx.buptschedule.base.BaseFragment;
import com.wzx.buptschedule.bean.UserInfo;
import com.wzx.buptschedule.service.AVService;
import com.wzx.buptschedule.util.PathUtils;
import com.wzx.buptschedule.util.SingleToast;

import java.io.File;


/**
 * User information fragment
 *
 * @author wzx
 */
public class PersonFragment extends BaseFragment implements OnClickListener {

    private View rootView;
    private TextView mTxUserName;
    private TextView mTxNickName;
    private TextView mTxPhone;

    private ImageView mImgNickName;
    private ImageView mImgTel;
    private ImageView mImgAvatar;
    private Dialog mDialogOrder;

    private TasApp mApp;
    private UserInfo mUserInfo;

    // Chang user nickname
    private static final int UR_NICKNAME = 0;
    // Change user phone number
    private static final int UR_PHONE = UR_NICKNAME + 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_personinfo, container,
                false);
        mTxUserName = (TextView) rootView.findViewById(R.id.tx_username);
        mTxNickName = (TextView) rootView.findViewById(R.id.tx_nickname);
        mTxPhone = (TextView) rootView.findViewById(R.id.tx_phone);
        mImgNickName = (ImageView) rootView.findViewById(R.id.img_nickname);
        mImgTel = (ImageView) rootView.findViewById(R.id.img_phone);
        mImgAvatar = (ImageView) rootView.findViewById(R.id.img_avatar_view);


        mImgNickName.setOnClickListener(this);
        mImgTel.setOnClickListener(this);
        mImgAvatar.setOnClickListener(this);

        mApp = (TasApp) mAct.getApplication();
        mUserInfo = mApp.getUserInfo();

        mTxUserName.setText(String.format(
                getString(R.string.account_myusername), mApp.getUserName()));
        mTxNickName
                .setText(String.format(getString(R.string.account_mynickname),
                        mUserInfo.getNickName()));
        mTxPhone.setText(String.format(getString(R.string.account_phone),
                mUserInfo.getPhone()));

        ImageLoader.getInstance().displayImage(mUserInfo.getAvatarUrl(), mImgAvatar, PhotoUtils.avatarImageOptions);

        return rootView;
    }

    private static final int IMAGE_PICK_REQUEST = 1;
    private static final int CROP_REQUEST = 2;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_nickname:
                // Change the NickName
                showOrderDialog(UR_NICKNAME);
                break;
            case R.id.img_phone:
                // Change the phone number
                showOrderDialog(UR_PHONE);
                break;
            case R.id.img_avatar_view:
                //Change the avatar
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, IMAGE_PICK_REQUEST);
                break;
            default:
                break;
        }
    }

    // Logout
    private void loginOut() {
        new AlertDialog.Builder(mAct)
                .setTitle(
                        mAct.getResources().getString(
                                R.string.dialog_message_title))
                .setMessage(
                        mAct.getResources().getString(
                                R.string.loginout_info))
                .setNegativeButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                                logout();
                            }
                        })
                .setPositiveButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        }).show();
    }

    private void logout() {
        AVService.logout();
        Intent loginIntent = new Intent(mAct, LoginActivity.class);
        loginIntent.putExtra("type", "loginout");
        startActivity(loginIntent);
        mAct.finish();
        mAct.overridePendingTransition(R.anim.slide_in_bottom,
                R.anim.slide_out_top);
    }

    //  Show order dialog
    private void showOrderDialog(final int type) {
        mDialogOrder = new Dialog(mAct, R.style.DialogStyle);
        mDialogOrder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialogOrder.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mDialogOrder.setContentView(R.layout.view_acount_dialog);
        final TextView txTitle = (TextView) mDialogOrder
                .findViewById(R.id.tx_targ);
        final EditText etContent = (EditText) mDialogOrder
                .findViewById(R.id.et_content);

        switch (type) {
            case UR_NICKNAME:
                txTitle.setText(R.string.account_mynickname_title);
                etContent.setHint(R.string.account_mynickname_hint);
                etContent.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case UR_PHONE:
                txTitle.setText(R.string.account_phone_title);
                etContent.setHint(R.string.account_phone_hint);
                etContent.setInputType(InputType.TYPE_CLASS_PHONE);
                break;
            default:
                break;
        }

        Button sure = (Button) mDialogOrder.findViewById(R.id.btn);

        sure.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // change user information
                switch (type) {
                    case UR_NICKNAME:
                        String nickName = etContent.getText().toString();
                        if (TextUtils.isEmpty(nickName)) {
                            SingleToast.showToast(mAct,
                                    R.string.account_mynickname_null, 2000);
                            return;
                        } else {
                            mTxNickName.setText(String.format(
                                    getString(R.string.account_mynickname),
                                    nickName));
                            mUserInfo.setNickName(nickName);
                            break;
                        }
                    case UR_PHONE:
                        String phone = etContent.getText().toString();
                        if (TextUtils.isEmpty(phone)) {
                            SingleToast.showToast(mAct,
                                    R.string.account_phone_null, 2000);
                            return;
                        } else {
                            mTxPhone.setText(String.format(
                                    getString(R.string.account_phone), phone));
                            mUserInfo.setPhone(phone);
                            break;
                        }
                    default:
                        break;
                }

                // Save the data on the server
                mUserInfo.saveInBackground(new SaveCallback() {

                    @Override
                    public void done(AVException arg0) {
                        if (arg0 == null) {
                            SingleToast.showToast(mAct,
                                    R.string.acount_modify_succ, 2000);
                        } else {
                            SingleToast.showToast(mAct,
                                    R.string.acount_modify_failed, 2000);
                        }
                    }
                });
                mDialogOrder.dismiss();
            }
        });
        try {
            mDialogOrder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGE_PICK_REQUEST) {
                Uri uri = data.getData();
                startImageCrop(uri, 200, 200, CROP_REQUEST);
            } else {
                if (requestCode == CROP_REQUEST) {
                    final String path = saveCropAvatar(data);
                    getUserInfo().saveAvatar(path, new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                SingleToast.showToast(mAct,
                                        R.string.acount_modify_avatar_succ, 2000);
                                //更新头像
                                ImageLoader.getInstance().displayImage(getUserInfo().getAvatarUrl(),
                                        mImgAvatar, PhotoUtils.avatarImageOptions);
                               if(mAct instanceof MainActivity) {
                                   ((MainActivity) mAct).refUserInfo();
                               }
                            } else {
                                SingleToast.showToast(mAct,
                                        R.string.acount_modify_avatar_failed, 2000);
                            }
                        }
                    });
                }
            }
        }
    }

    //Get the picture url
    public Uri startImageCrop(Uri uri, int outputX, int outputY,
                              int requestCode) {
        Intent intent = null;
        intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        String outputPath = PathUtils.getAvatarTmpPath();
        Uri outputUri = Uri.fromFile(new File(outputPath));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", false); // face detection
        startActivityForResult(intent, requestCode);
        return outputUri;
    }

    private String saveCropAvatar(Intent data) {
        Bundle extras = data.getExtras();
        String path = null;
        if (extras != null) {
            Bitmap bitmap = extras.getParcelable("data");
            if (bitmap != null) {
                bitmap = PhotoUtils.toRoundCorner(bitmap, 10);
                path = PathUtils.getAvatarCropPath();
                PhotoUtils.saveBitmap(path, bitmap);
                if (bitmap != null && bitmap.isRecycled() == false) {
                    bitmap.recycle();
                }
            }
        }
        return path;
    }

}
