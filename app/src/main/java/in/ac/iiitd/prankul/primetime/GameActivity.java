package in.ac.iiitd.prankul.primetime;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    /*boolean isPrime(int x)
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
    }*/
    int n = 1000;
    boolean[] isPrime = new boolean[n+1];

    void generatePrime() {

        for (int i = 2; i <= n; i++) {
            isPrime[i] = true;
        }

        for (int factor = 2; factor*factor <= n; factor++) {

            if (isPrime[factor]) {
                for (int j = factor; factor*j <= n; j++) {
                    isPrime[factor*j] = false;
                }
            }
        }
        isPrime[0]=false;
        isPrime[1]=false;
    }

    // important variables
    int state = 0;
    int score = 0;
    int whichButtonPressed;
    int activityBackFromFlag = 0;
    Drawable defaultYesButton,defaultNoButton,defaultSkipButton;
    boolean cheatTaken, hintTaken, impFlag;
    Bundle b;
    int whatHelp = 0;

    // initial number;
    Calendar c = Calendar.getInstance();
    int seconds = c.get(Calendar.SECOND);
    Random random = new Random();
    int num;

    // widgets
    TextView number, scoreText, info;
    Button buttonYes, buttonNo, buttonSkip, hint, cheat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        generatePrime();

        // icon on toolbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.mipmap.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //initial number
        random.setSeed(seconds);
        num = random.nextInt(1000)+1;
        impFlag=true;

        //widget initialization
        number = (TextView) findViewById(R.id.number);
        buttonYes = (Button) findViewById(R.id.buttonYes);
        buttonNo = (Button) findViewById(R.id.buttonNo);
        buttonSkip = (Button) findViewById(R.id.buttonSkip);
        hint = (Button) findViewById(R.id.hint);
        cheat = (Button) findViewById(R.id.cheat);
        scoreText = (TextView) findViewById(R.id.score);
        info = (TextView) findViewById(R.id.info);

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
            info.setVisibility(View.INVISIBLE);
        }
        else {
            impFlag=false;
            num = savedInstanceState.getInt("num");
            state = savedInstanceState.getInt("state");
            whichButtonPressed = savedInstanceState.getInt("button");
            score = savedInstanceState.getInt("score");
            activityBackFromFlag=savedInstanceState.getInt("activity");
            hintTaken = savedInstanceState.getBoolean("hinttaken");
            cheatTaken = savedInstanceState.getBoolean("cheattaken");
            whatHelp = savedInstanceState.getInt("help");
            scoreText.setText(String.valueOf(score));

            //which help was taken, disablng buttons
            Log.i("GAME",""+whatHelp);
            if(whatHelp==0)
            {
                hint.setEnabled(true);
                cheat.setEnabled(true);
            }
            else if(whatHelp==1)
            {
                hint.setEnabled(false);
                cheat.setEnabled(true);
            }
            else if(whatHelp==2)
            {
                hint.setEnabled(false);
                cheat.setEnabled(false);
            }

            //state conditions
            if (state == 1) {
                buttonYes.setEnabled(false);
                buttonNo.setEnabled(false);
                hint.setEnabled(false);
                cheat.setEnabled(false);
                if(whichButtonPressed==1)
                {
                    if(isPrime[num])
                        defaultYesButton.setColorFilter(Color.GREEN, PorterDuff.Mode.DARKEN);
                    else
                        defaultYesButton.setColorFilter(Color.RED, PorterDuff.Mode.DARKEN);
                }
                else if(whichButtonPressed==0)
                {
                    if(isPrime[num])
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

            //info lable
            //Log.i("GAME",activityBackFromFlag+" "+hintTaken+" "+cheatTaken);
            if(activityBackFromFlag==0)
            {
                info.setVisibility(View.INVISIBLE);
            }
            else if(activityBackFromFlag==1 && hintTaken)
            {
                info.setVisibility(View.VISIBLE);
                info.setText(R.string.hintused);
            }
            else if(activityBackFromFlag==2 && cheatTaken)
            {
                info.setVisibility(View.VISIBLE);
                info.setText(R.string.cheatused);
            }
            number.setText(String.valueOf(num));
        }
    }

    /*@Override
    protected void onResume() {
        Log.i("GAME","OnResume");
        Bundle b = getIntent().getExtras();
        if(b!=null && impFlag)
        {
            if(b.getInt("num")!=0)
                num = b.getInt("num");
            score = b.getInt("score");
            scoreText.setText(String.valueOf(score));
            activityBackFromFlag = b.getInt("activity");
            if(activityBackFromFlag==1)
            {
                hintTaken = b.getBoolean("hinttaken");
            }
            if(activityBackFromFlag==2)
            {
                cheatTaken = b.getBoolean("cheattaken");
            }
        }

        //info lable
        Log.i("GAME",activityBackFromFlag+" "+hintTaken+" "+cheatTaken);
        if(activityBackFromFlag==0)
        {
            info.setVisibility(View.INVISIBLE);
            whatHelp=0;
        }
        else if(activityBackFromFlag==1 && hintTaken)
        {
            info.setVisibility(View.VISIBLE);
            info.setText(R.string.hintused);
            scoreText.setText(String.valueOf(score));
            whatHelp = 1;
            hint.setEnabled(false);
        }
        else if(activityBackFromFlag==2 && cheatTaken)
        {
            info.setVisibility(View.VISIBLE);
            info.setText(R.string.cheatused);
            scoreText.setText(String.valueOf(score));
            whatHelp=2;
            hint.setEnabled(false);
            cheat.setEnabled(false);
        }
        number.setText(String.valueOf(num));

        super.onResume();
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                activityBackFromFlag = data.getIntExtra("activity",1);
                hintTaken = data.getBooleanExtra("hinttaken",false);
            }
        }
        else if(requestCode==2)
        {
            if(resultCode == Activity.RESULT_OK){
                activityBackFromFlag = data.getIntExtra("activity",2);
                cheatTaken = data.getBooleanExtra("cheattaken",false);
            }
        }

        if(activityBackFromFlag==0)
        {
            info.setVisibility(View.INVISIBLE);
            whatHelp=0;
        }
        else if(activityBackFromFlag==1 && hintTaken)
        {
            info.setVisibility(View.VISIBLE);
            info.setText(R.string.hintused);
            score-=2;
            if(score<0)
                score=0;
            scoreText.setText(String.valueOf(score));
            whatHelp = 1;
            hint.setEnabled(false);
        }
        else if(activityBackFromFlag==2 && cheatTaken)
        {
            info.setVisibility(View.VISIBLE);
            info.setText(R.string.cheatused);
            score-=8;
            if(score<0)
                score=0;
            scoreText.setText(String.valueOf(score));
            whatHelp=2;
            hint.setEnabled(false);
            cheat.setEnabled(false);
        }
    }

    public void clickYes(View view)
    {
        state = 1;
        whichButtonPressed=1;
        buttonYes.setEnabled(false);
        buttonNo.setEnabled(false);
        cheat.setEnabled(false);
        hint.setEnabled(false);
        if(isPrime[num]) {
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
        cheat.setEnabled(false);
        hint.setEnabled(false);
        if(!isPrime[num]) {
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
            cheat.setEnabled(true);
            hint.setEnabled(true);
            info.setVisibility(View.INVISIBLE);
            activityBackFromFlag=0;
            whatHelp=0;

            defaultYesButton.setColorFilter(Color.WHITE, PorterDuff.Mode.LIGHTEN);
            defaultNoButton.setColorFilter(Color.WHITE, PorterDuff.Mode.LIGHTEN);
        }
    }

    public void clickHint(View view)
    {
        Intent intent = new Intent(this,HintActivity.class);
        startActivityForResult(intent,1);
    }

    public void clickCheat(View view)
    {
        Intent intent = new Intent(this,CheatActivity.class);
        intent.putExtra("num",num);
        intent.putExtra("isprime",isPrime[num]);
        startActivityForResult(intent,2);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt("state", state);
        savedInstanceState.putInt("num", num);
        savedInstanceState.putInt("button",whichButtonPressed);
        savedInstanceState.putInt("score",score);
        savedInstanceState.putInt("activity",activityBackFromFlag);
        savedInstanceState.putBoolean("cheattaken",cheatTaken);
        savedInstanceState.putBoolean("hinttaken",hintTaken);
        savedInstanceState.putInt("help",whatHelp);
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
                    cheat.setEnabled(true);
                    hint.setEnabled(true);
                    info.setVisibility(View.INVISIBLE);
                    activityBackFromFlag=0;
                    whatHelp=0;
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
