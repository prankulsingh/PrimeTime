package in.ac.iiitd.prankul.primetime;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    boolean isPrime(int x)
    {
        int primes[] = {2,3,5,7,11,13,17,19,23,29,31,37,41,43,47,53,59,61,67,71,73,79,83,89,97,101,103,107,109,113,127,131,137,139,149,151,157,163,167,173,179,181,191,193,197,199,211,223,227,229,233,239,241,251,257,263,269,271,277,281,283,293,307,311,313,317,331,337,347,349,353,359,367,373,379,383,389,397,401,409,419,421,431,433,439,443,449,457,461,463,467,479,487,491,499,503,509,521,523,541,547,557,563,569,571,577,587,593,599,601,607,613,617,619,631,641,643,647,653,659,661,673,677,683,691,701,709,719,727,733,739,743,751,757,761,769,773,787,797,809,811,821,823,827,829,839,853,857,859,863,877,881,883,887,907,911,919,929,937,941,947,953,967,971,977,983,991,997};
        for(int i=0;i<primes.length;i++) {
            if (x == primes[i]) {
                return true;
            } else if (x < primes[i]) {
                return false;
            }
        }
        return false;
    }

    // important variables
    int state = 0;
    int score = 0;
    int activityBackFromFlag = 0;
    int whichButtonPressed;
    Drawable defaultYesButton,defaultNoButton,defaultSkipButton;

    // initial number;
    Calendar c = Calendar.getInstance();
    int seconds = c.get(Calendar.SECOND);
    Random random = new Random();
    int num;

    // widgets
    TextView number, scoreText;
    Button buttonYes, buttonNo, buttonSkip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // icon on toolbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.mipmap.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //initial number
        random.setSeed(seconds);
        num = random.nextInt(1000)+1;
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if(b==null)
        {
            //started from this activity
            ;
        }
        else
        {
            activityBackFromFlag = b.getInt("activityFlag");
        }
        if(activityBackFromFlag==0)
        {

        }
        else if(activityBackFromFlag==1)
        {

        }
        else if(activityBackFromFlag==2)
        {

        }

        //widget initialization
        number = (TextView) findViewById(R.id.number);
        buttonYes = (Button) findViewById(R.id.buttonYes);
        buttonNo = (Button) findViewById(R.id.buttonNo);
        buttonSkip = (Button) findViewById(R.id.buttonSkip);
        scoreText = (TextView) findViewById(R.id.score);

        // init
        defaultYesButton = buttonYes.getBackground();
        defaultNoButton = buttonNo.getBackground();
        defaultSkipButton = buttonSkip.getBackground();

        defaultYesButton.setColorFilter(Color.WHITE, PorterDuff.Mode.LIGHTEN);
        defaultNoButton.setColorFilter(Color.GREEN, PorterDuff.Mode.LIGHTEN);
        defaultSkipButton.setColorFilter(Color.WHITE, PorterDuff.Mode.LIGHTEN);

        if(savedInstanceState==null) {
            number.setText(String.valueOf(num));
            scoreText.setText(String.valueOf(score));
        }
        else {
            num = savedInstanceState.getInt("num");
            state = savedInstanceState.getInt("state");
            whichButtonPressed = savedInstanceState.getInt("button");
            score = savedInstanceState.getInt("score");
            scoreText.setText(String.valueOf(score));
            if (state == 1) {
                buttonYes.setEnabled(false);
                buttonNo.setEnabled(false);
                if(whichButtonPressed==1)
                {
                    if(isPrime(num))
                        defaultYesButton.setColorFilter(Color.GREEN, PorterDuff.Mode.DARKEN);
                    else
                        defaultYesButton.setColorFilter(Color.RED, PorterDuff.Mode.DARKEN);
                }
                else if(whichButtonPressed==0)
                {
                    if(isPrime(num))
                        defaultNoButton.setColorFilter(Color.RED, PorterDuff.Mode.DARKEN);
                    else
                        defaultNoButton.setColorFilter(Color.GREEN, PorterDuff.Mode.DARKEN);
                }

            } else if (state == 2) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Really?")
                        .setMessage("Are you sure want to skip without answering? It will fetch you -5 points!")
                        .setPositiveButton("Yes", skipClickListener)
                        .setNegativeButton("No", skipClickListener)
                        .show();
            }
            number.setText(String.valueOf(num));
        }
    }

    public void clickYes(View view)
    {
        state = 1;
        whichButtonPressed=1;
        buttonYes.setEnabled(false);
        buttonNo.setEnabled(false);
        if(isPrime(num)) {
            Snackbar.make(view, "You are Right! You have got +10 Points", Snackbar.LENGTH_LONG).show();
            defaultYesButton.setColorFilter(Color.GREEN, PorterDuff.Mode.DARKEN);
            score+=10;
            if(score<0)
                score=0;
            scoreText.setText(String.valueOf(score));
        }
        else {
            Snackbar.make(view, "You are Wrong! You have got -10 Points", Snackbar.LENGTH_LONG).show();
            defaultYesButton.setColorFilter(Color.RED, PorterDuff.Mode.DARKEN);
            score-=10;
            if(score<0)
                score=0;
            scoreText.setText(String.valueOf(score));
        }
    }

    public void clickNo(View view)
    {
        state = 1;
        whichButtonPressed=0;
        buttonYes.setEnabled(false);
        buttonNo.setEnabled(false);
        if(!isPrime(num)) {
            Snackbar.make(view, "You are Right! You have got +10 Points", Snackbar.LENGTH_SHORT).show();
            defaultNoButton.setColorFilter(Color.GREEN, PorterDuff.Mode.DARKEN);
            score+=10;
            if(score<0)
                score=0;
            scoreText.setText(String.valueOf(score));
        }
        else {
            Snackbar.make(view, "You Are Wrong! You have got -10 Points", Snackbar.LENGTH_SHORT).show();
            defaultNoButton.setColorFilter(Color.RED, PorterDuff.Mode.DARKEN);
            score-=10;
            if(score<0)
                score=0;
            scoreText.setText(String.valueOf(score));
        }
    }

    public void clickNext(View view)
    {
        if(state==0)
        {
            state=2;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Really?")
                    .setMessage("Are you sure want to skip without answering? It will fetch you -5 points!")
                    .setPositiveButton("Yes", skipClickListener)
                    .setNegativeButton("No", skipClickListener)
                    .show();
        }
        else if(state==1)
        {
            state=0;
            num = random.nextInt(1000)+1;
            number.setText(String.valueOf(num));

            buttonNo.setEnabled(true);
            buttonYes.setEnabled(true);

            defaultYesButton.setColorFilter(Color.WHITE, PorterDuff.Mode.LIGHTEN);
            defaultNoButton.setColorFilter(Color.WHITE, PorterDuff.Mode.LIGHTEN);
        }
    }

    public void clickHint(View view)
    {
        Intent intent = new Intent(this,HintActivity.class);
        startActivity(intent);
    }

    public void clickCheat(View view)
    {
        Intent intent = new Intent(this,CheatActivity.class);
        intent.putExtra("num",num);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt("state", state);
        savedInstanceState.putInt("num", num);
        savedInstanceState.putInt("button",whichButtonPressed);
        savedInstanceState.putInt("score",score);
        super.onSaveInstanceState(savedInstanceState);
    }

    DialogInterface.OnClickListener skipClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    state=0;
                    num = random.nextInt(1000)+1;
                    number.setText(String.valueOf(num));
                    buttonNo.setEnabled(true);
                    buttonYes.setEnabled(true);
                    score-=5;
                    if(score<0)
                        score=0;
                    scoreText.setText(String.valueOf(score));
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    state=0;
                    break;
            }
        }
    };
}
