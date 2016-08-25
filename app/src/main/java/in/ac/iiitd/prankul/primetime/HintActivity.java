package in.ac.iiitd.prankul.primetime;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HintActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hint);
    }

    void onClickBack(View view)
    {
        Intent intent = new Intent(this,GameActivity.class);
        startActivity(intent);
    }
}
