package com.example.as.ico;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @ViewById(R.id.myText)
    private TextView mText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);
        mText.setText("aaaaaa");
    }
    @OnClick(R.id.myText)
    @CheckNet("亲，请检查网络")
    private void click(View view){
        Toast.makeText(this, "aaa", Toast.LENGTH_SHORT).show();
    }
}
