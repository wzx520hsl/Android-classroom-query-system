package com.wzx.buptschedule.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.avoscloud.leanchatlib.utils.PhotoUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wzx.buptschedule.R;
import com.wzx.buptschedule.base.BaseFragmentActivity;
import com.wzx.buptschedule.bean.UserInfo;
import com.wzx.buptschedule.fragment.AboutFragment;
import com.wzx.buptschedule.fragment.FreeRoomsFragment;
import com.wzx.buptschedule.fragment.PersonFragment;
import com.wzx.buptschedule.service.AVService;
import com.wzx.buptschedule.util.SingleToast;
import com.wzx.buptschedule.widget.DragLayout;

import java.util.Random;



public class MainActivity extends BaseFragmentActivity {

    private DragLayout dl;
    private ListView lv;
    private TextView mTxUser;
    private TextView mTxUserType;
    private ImageView mImgAvatar;

    private int back = 0;// Record how many back has  been pressed

    public static int mUserTypeMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDragLayout();
        initView();
        ChatManager.getInstance().openClient(null);

    }

    private void initDragLayout() {
        dl = (DragLayout) findViewById(R.id.dl);
        dl.setDragListener(new DragLayout.DragListener() {
            @Override
            public void onOpen() {
                lv.smoothScrollToPosition(new Random().nextInt(30));
            }

            @Override
            public void onClose() {
            }

            @Override
            public void onDrag(float percent) {
            }
        });
    }

    public void home_back(View view) {
        dl.open();
    }

    private void initView() {
        changeFragment(new FreeRoomsFragment());

        mTxUser = (TextView) findViewById(R.id.tx_user);
        mTxUserType = (TextView) findViewById(R.id.tx_usertype);
        mImgAvatar = (ImageView)findViewById(R.id.iv_bottom);
        lv = (ListView) findViewById(R.id.lv);
        mUserTypeMode = 0;
        final String[] meuns = this.getResources().getStringArray(
                R.array.user_menu);
        lv.setAdapter(new ArrayAdapter<String>(MainActivity.this,
                R.layout.view_menu_item, meuns));
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                switch (position) {
                    case 0:
                        changeFragment(new PersonFragment());
                        break;
                    case 1:
                        changeFragment(new FreeRoomsFragment());
                        break;
                    case 2:
                        changeFragment(new AboutFragment());
                        break;
                    case 3:
                        loginOut();
                        return;
                    default:
                        break;
                }
                dl.close();
            }
        });
        refUserInfo();
    }

    /**
     * Change the fragment
     *
     * @param targetFragment
     */
    public void changeFragment(Fragment targetFragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public void refUserInfo(){
        //更新头像
        ImageLoader.getInstance().displayImage(getUserInfo().getAvatarUrl(),
                mImgAvatar, PhotoUtils.avatarImageOptions);
        mTxUser.setText(getUserInfo().getUserName());
        mTxUserType.setText(getUserInfo().getNickName());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ChatManager.getInstance().closeWithCallback(null);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back++;
            switch (back) {
                case 1:
                    SingleToast.showToast(MainActivity.this,
                            getString(R.string.exit_again), 1500);
                    // Quit the applicaiton in 3 seconds
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            back = 0;
                        }
                    }, 3000);
                    break;
                case 2:
                    back = 0;
                    MainActivity.this.finish();
                    android.os.Process.killProcess(android.os.Process.myPid());// 关闭进程
                    break;
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    // Logout
    private void loginOut() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(
                        getResources().getString(
                                R.string.dialog_message_title))
                .setMessage(
                        getResources().getString(
                                R.string.loginout_info))
                .setNegativeButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                                logout();
                            }
                        })
                .setPositiveButton("CANCEL",
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

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.finish();
    }




}
