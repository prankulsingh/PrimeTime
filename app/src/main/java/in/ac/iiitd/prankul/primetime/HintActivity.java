package in.ac.iiitd.prankul.primetime;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HintActivity extends AppCompatActivity {

    int state;
    TextView hint;
    Button return1,show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hint);

        show = (Button) findViewById(R.id.show);
        return1 = (Button) findViewById(R.id.return1);
        hint = (TextView) findViewById(R.id.hint);

        state = 0;
        if(savedInstanceState!=null)
        {
            state=savedInstanceState.getInt("state");
            if(state==1) {
                hint.setVisibility(View.VISIBLE);
                show.setEnabled(false);
            }
            else{
                hint.setVisibility(View.INVISIBLE);
                show.setEnabled(true);
            }
        }
    }

    void onClickBack(View view)
    {
        Intent intent = new Intent(this,GameActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        boolean a;
        if(state==1)
            a=true;
        else
            a=false;
        intent.putExtra("hinttaken",a);
        intent.putExtra("activity",1);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,GameActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        boolean a;
        if(state==1)
            a=true;
        else
            a=false;
        intent.putExtra("hinttaken",a);
        intent.putExtra("activity",1);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }

    void onClickShow(View view)
    {
        state=1;
        show.setEnabled(false);
        hint.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        //Log.i("CHEAT","in save instance");
        savedInstanceState.putInt("state", state);
        super.onSaveInstanceState(savedInstanceState);
    }
}
