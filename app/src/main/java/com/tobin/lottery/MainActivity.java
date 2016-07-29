package com.tobin.lottery;

import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.tobin.lottery.adapter.BlueBallAdapter;
import com.tobin.lottery.adapter.RedBallAdapter;
import com.tobin.lottery.adapter.RedBallAdapter.redGridViewHolder;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private GridView gridViRedBall, gridViBlueBall;
    private RedBallAdapter adpRedBall;
    private BlueBallAdapter adpBlueBall;
    private ArrayList<String> arrRandomRed, arrRandomBlue;

    private Button btnRandomSelect, btnClearNumber, btnPurchaseBall;
    private TextView txt_result,txtShowRedBall,txtShowBlueBall;

    private String strRedBall = "", strBlueBall = "";
    private int selectHongNumber = 6, selectLanNumber = 1;

    private ShakeListener mShakeListener = null;
    private Vibrator mVibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        // 对红球GridView进行监听，获得红球选中的数。
        gridViRedBall.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Calculator calculatorDao = new Calculator();
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 在每次获取点击的item时将对应的checkbox状态改变，同时修改map的值。
                redGridViewHolder vHollder = (redGridViewHolder) view.getTag();
                vHollder.chkRed.toggle();
                adpRedBall.getSelected().put(position, vHollder.chkRed.isChecked());
                int temp = 0;
                for (int i = 0; i < gridViRedBall.getCount(); i++) {
                    if (adpRedBall.hisSelected.get(i)) {
                        ++temp;
                        selectHongNumber = temp;
                        redGridViewHolder vHollder_hong = (redGridViewHolder) gridViRedBall.getChildAt(i).getTag();
                        strRedBall = strRedBall + vHollder_hong.chkRed.getText() + "  ";

                        vHollder_hong.chkRed.setTextColor(getResources().getColor(android.R.color.white));
                    } else {
                        RedBallAdapter.redGridViewHolder vHollder_hong = (RedBallAdapter.redGridViewHolder) gridViRedBall.getChildAt(i).getTag();
                        vHollder_hong.chkRed.setTextColor(ContextCompat.getColor(MainActivity.this,android.R.color.black));
                    }
                }
                txtShowRedBall.setText(strRedBall);
                strRedBall = "";
                long zhushu = Calculator.calculateBetNum(selectHongNumber, selectLanNumber);
                txt_result.setText("共" + String.valueOf(zhushu) + "注" + (zhushu * 2) + "元");
            }
        });

        // 对篮球GridView进行监听，获取选球状态。
        gridViBlueBall.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BlueBallAdapter.LanGridViewHolder vHollder = (BlueBallAdapter.LanGridViewHolder) view.getTag();
                vHollder.chkBlue.toggle();
                BlueBallAdapter.lisSelected.put(position, vHollder.chkBlue.isChecked());
                int templan = 0;
                for (int i = 0; i < gridViBlueBall.getCount(); i++) {
                    if (BlueBallAdapter.lisSelected.get(i)) {
                        ++templan;
                        selectLanNumber = templan;
                        BlueBallAdapter.LanGridViewHolder vHollder_lan = (BlueBallAdapter.LanGridViewHolder) gridViBlueBall.getChildAt(i).getTag();
                        strBlueBall = strBlueBall + vHollder_lan.chkBlue.getText() + "  ";
                        vHollder_lan.chkBlue.setTextColor(getResources().getColor(android.R.color.white));
                    } else {
                        BlueBallAdapter.LanGridViewHolder vHollder_lan = (BlueBallAdapter.LanGridViewHolder) gridViBlueBall.getChildAt(i).getTag();
                        vHollder_lan.chkBlue.setTextColor(getResources().getColor(android.R.color.black));
                    }
                }

                long zhushu = Calculator.calculateBetNum(selectHongNumber, selectLanNumber);
                txt_result.setText("共" + String.valueOf(zhushu) + "注" + (zhushu * 2) + "元");
            }
        });

        // 当手机摇晃时
        mShakeListener.setOnShakeListener(new ShakeListener.OnShakeListener() {
            public void onShake() {
                mShakeListener.stop();
                startVibrato(); // 开始 震动
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        randomSelect();
                        mVibrator.cancel();
                        mShakeListener.start();
                    }
                }, 1000);
            }
        });
    }

    // 定义震动
    public void startVibrato() {
        mVibrator.vibrate(new long[] { 200, 500, 200, 500 }, -1);
        // 第一个｛｝里面是节奏数组，
        // 第二个参数是重复次数，-1为不重复，非-1则从pattern的指定下标开始重复
    }

    @Override
    protected void onStart() {
        super.onStart();
        mShakeListener.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mShakeListener != null) {
            mShakeListener.stop();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 清空选号按钮
            case R.id.btn_clear_number:
                adpRedBall.clearData();
                adpBlueBall.clearData();
                break;
            // 随机选号按钮
            case R.id.btn_random_select:
                randomSelect();
                break;
            // 投注按钮
            case R.id.btn_purchase_ball:
                break;
            default:
                break;
        }

    }

    private void randomSelect() {
        getBallNumber();
        adpRedBall.updateData(arrRandomRed);
        adpBlueBall.updateData(arrRandomBlue);
        String hq = "";
        for (int i = 0; i < arrRandomRed.size(); i++) {
            int temp = (Integer.parseInt(arrRandomRed.get(i)) + 1);
            hq = hq + temp + "  ";
        }
        txtShowRedBall.setText(hq);

        String lq = "";
        for (int i = 0; i < arrRandomBlue.size(); i++) {
            int temp = (Integer.parseInt(arrRandomBlue.get(i)) + 1);
            lq = lq + temp + "  ";
        }
        txtShowBlueBall.setText(lq);
        txt_result.setText("共1注2元");
    }

    private void init() {
        // 获得振动器服务
        mVibrator = (Vibrator) getApplication().getSystemService(VIBRATOR_SERVICE);
        // 实例化加速度传感器检测类
        mShakeListener = new ShakeListener(this);

        gridViRedBall = (GridView) findViewById(R.id.gridview_hongqiu);
        gridViBlueBall = (GridView) findViewById(R.id.gridview_lanqiu);

        txtShowRedBall = (TextView) findViewById(R.id.txt_showhongqiu);
        txtShowBlueBall = (TextView) findViewById(R.id.txt_showlanqiu);
        txt_result = (TextView) findViewById(R.id.txt_result);

        btnRandomSelect = (Button) findViewById(R.id.btn_random_select);
        btnClearNumber = (Button) findViewById(R.id.btn_clear_number);
        btnPurchaseBall = (Button) findViewById(R.id.btn_purchase_ball);

        btnRandomSelect.setOnClickListener(this);
        btnClearNumber.setOnClickListener(this);
        btnPurchaseBall.setOnClickListener(this);

        adpRedBall = new RedBallAdapter(this, arrRandomRed);
        adpBlueBall = new BlueBallAdapter(this, arrRandomBlue);

        gridViRedBall.setAdapter(adpRedBall);
        gridViBlueBall.setAdapter(adpBlueBall);

    }

    private void getBallNumber() {
        arrRandomRed = RandomMadeBall.getRedBall();
        arrRandomBlue = RandomMadeBall.getBlueBall();
    }

}
