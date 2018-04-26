package com.lhy.xfif;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private SwitchCompat switchCompat;
    private TextView scoreTipLeftTextView;
    private TextView scoreTipTextView;
    private EditText scoreEditText;
    private Button okButton;

    private boolean isOpen;
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化视图
        intiView();
        //配置文件
        sharedPreferences = getSharedPreferences("xfif", MODE_WORLD_READABLE);
        editor = sharedPreferences.edit();
        //获取状态
        isOpen = sharedPreferences.getBoolean("isOpen", false);
        score = sharedPreferences.getInt("score", 80);
        //判断是否开启修改分数
        if (isOpen) {
            switchCompat.setChecked(true);
        } else {
            switchCompat.setChecked(false);
        }
        modifyEnable(isOpen);

        //设置switch状态
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                modifyEnable(b);
            }
        });

        //按钮点击修改分数
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = scoreEditText.getText().toString();
                if (!TextUtils.isEmpty(s)) {
                    if (Integer.parseInt(s) > 100) {
                        // TODO: 2018-04-26-0026 提示
                    }
                    //保存文件
                    editor.putInt("score", Integer.parseInt(s));
                    editor.commit();
                    //修改页面显示
                    scoreTipTextView.setText(s);
                } else {
                    Toast.makeText(MainActivity.this, "不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * 修改状态
     */
    private void modifyEnable(boolean isOpen) {
        if (!isOpen) {
            //设置不可用
            scoreTipLeftTextView.setText("当前状态不可用");
            scoreTipTextView.setText("");
            scoreEditText.setEnabled(false);
            okButton.setEnabled(false);
            editor.putBoolean("isOpen", false);
        } else {
            scoreTipLeftTextView.setText("当前分数已被修改为：");
            scoreTipTextView.setText(sharedPreferences.getInt("score", 80) + "");
            scoreEditText.setEnabled(true);
            okButton.setEnabled(true);
            editor.putBoolean("isOpen", true);
        }
        editor.commit();
    }

    /**
     * 初始化视图
     */
    private void intiView() {
        switchCompat = findViewById(R.id.switch_compat);
        scoreEditText = findViewById(R.id.score_text);
        scoreTipTextView = findViewById(R.id.score_tip);
        okButton = findViewById(R.id.ok_btn);
        scoreTipLeftTextView = findViewById(R.id.score_tip_left);
    }
}
