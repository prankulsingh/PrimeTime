package in.ac.iiitd.prankul.primetime;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    int num, state;
    boolean isprime;

    TextView lable1, lable2, number;
    Button return1,show;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        state=0;
        lable1 = (TextView) findViewById(R.id.lable1);
        lable2 = (TextView) findViewById(R.id.lable2);
        number = (TextView) findViewById(R.id.number);
        return1 = (Button) findViewById(R.id.return1);
        show = (Button) findViewById(R.id.show);

        if(getIntent().getExtras()!=null)
        {
            Intent intent = getIntent();
            Bundle b = intent.getExtras();
            num = b.getInt("num");
            isprime = b.getBoolean("isprime");
        }
        if(savedInstanceState==null)
        {
            ;
        }
        else
        {
            Bundle b = savedInstanceState;
            num = b.getInt("num");
            isprime = b.getBoolean("isprime");
            state = b.getInt("state");
            if(state==1)
            {
                show.setEnabled(false);
                lable1.setVisibility(View.VISIBLE);
                lable2.setVisibility(View.VISIBLE);
                number.setVisibility(View.VISIBLE);
            }
        }

        number.setText(String.valueOf(num));
        if(isprime)
        {
            lable1.setText("Yes,");
            lable2.setText("is a Prime number.");
        }
        else
        {
            TextView lable1 = (TextView) findViewById(R.id.lable1);
            TextView lable2 = (TextView) findViewById(R.id.lable2);
            lable1.setText("No,");
            lable2.setText("is not a Prime number.");
        }
    }

    void onClickBack(View view)
    {
        Intent intent = new Intent(this,GameActivity.class);
        boolean a;
        if(state==1)
            a=true;
        else
            a=false;
        intent.putExtra("cheattaken",a);
        intent.putExtra("activity",2);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this,GameActivity.class);
        boolean a;
        if(state==1)
            a=true;
        else
            a=false;
        intent.putExtra("cheattaken",a);
        intent.putExtra("activity",2);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }

    void onClickShow(View view)
    {
        show.setEnabled(false);
        state=1;
        lable1.setVisibility(View.VISIBLE);
        lable2.setVisibility(View.VISIBLE);
        number.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        //Log.i("CHEAT","in save instance");
        savedInstanceState.putInt("num", num);
        savedInstanceState.putInt("state", state);
        savedInstanceState.putBoolean("isprime",isprime);
        super.onSaveInstanceState(savedInstanceState);
    }
}
