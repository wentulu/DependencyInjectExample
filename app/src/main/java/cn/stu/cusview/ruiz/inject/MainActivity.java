package cn.stu.cusview.ruiz.inject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.stu.cusview.ruiz.MainActivity_FindView;

import java.lang.reflect.Field;

import cn.stu.cusview.ruiz.annimations.BindView;

public class MainActivity extends AppCompatActivity {

    @BindView(value = R.id.tv)
    private TextView tv;

    @BindView(value = R.id.btn)
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity_FindView.findViewByIdAnnotation(this);
        tv.setText("123");
        btn.setText("456");
    }
}
