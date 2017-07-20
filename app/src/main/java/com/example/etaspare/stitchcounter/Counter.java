package com.example.etaspare.stitchcounter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import static java.lang.Math.round;

/**
 * Created by ETASpare on 6/8/2017.
 */

public class Counter extends AppCompatActivity implements Parcelable {

    protected int ID;
    protected int counterNumber = 0;
    protected int adjustment = 1;
    protected int totalRows;
    protected double progressPercent;
    private final int COUNTER_MIN = 0;
    private final int COUNTER_MAX = 9999;
    private TextView textCounter;
    private TextView textProgress;
    private String strResCounter;
    private String strResProgress;
    protected String projectName;
    private Button btnAdjustment1;
    private Button btnAdjustment5;
    private Button btnAdjustment10;
    private ProgressBar progressBar;
    private Resources res;
    private Context context;

    /* Defines the kind of object that will be parcelled */
    @Override
    public int describeContents() {
        return 0;
    }

    /*
     Actual object serialization happens here, Write object content
     to parcel, reading should be done according to this write order
    */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeInt(counterNumber);
        dest.writeInt(adjustment);
        dest.writeInt(totalRows);
        dest.writeString(projectName);
    }

    /*
    This field is needed for Android to be able to
    create new objects, individually or as arrays
     */
    public static final Parcelable.Creator<Counter> CREATOR = new Parcelable.Creator<Counter>() {

        public Counter createFromParcel(Parcel in) {
            return new Counter(in);
        }

        public Counter[] newArray(int size) {
            return new Counter[size];
        }
    };

    /*
    Parcelable Counter's constructor. Used to instantiate counters that need saved when navigation
    to the library occurs.
    */
    public Counter (Parcel in) {
        this.ID = in.readInt();
        this.counterNumber = in.readInt();
        this.adjustment = in.readInt();
        this.totalRows = in.readInt();
        this.projectName = in.readString();
    }
    /*
        Double Counter's constructor, instantiates new instance of counter class, setting all instance variables and context.
        Handles progress related instance variables.
     */
    public Counter (Context context, TextView textCounter, TextView textProgress, int strResCounterID, int strResProgressID, Button adjustment1, Button adjustment5, Button adjustment10, ProgressBar progress) {
        this.res = context.getResources();
        this.context = context;
        this.textCounter = textCounter;
        this.textProgress = textProgress;
        this.strResCounter = res.getString(strResCounterID);
        this.strResProgress = res.getString(strResProgressID);
        this.btnAdjustment1 = adjustment1;
        this.btnAdjustment5 = adjustment5;
        this.btnAdjustment10 = adjustment10;
        this.progressBar = progress;
        this.setCounter();
    }
    /*
        Single Counter's constructor, instantiates new instance of counter class, setting all instance variables and context.
        Doesn't have progress related instance variables.
     */
    public Counter (Context context, TextView textCounter, int strResCounterID, Button adjustment1, Button adjustment5, Button adjustment10) {
        this.res = context.getResources();
        this.context = context;
        this.textCounter = textCounter;
        this.strResCounter = res.getString(strResCounterID);
        this.btnAdjustment1 = adjustment1;
        this.btnAdjustment5 = adjustment5;
        this.btnAdjustment10 = adjustment10;
        this.setCounter();
    }

    /*
       Formats the counter string with counter number. Sets the
       counter TextView with formattedCounterNumber.
     */
    protected void setCounter() {
        String formattedCounterNumber = String.format(this.strResCounter, this.counterNumber);
        this.textCounter.setText(formattedCounterNumber);
    }

    public void setProjectName(String name) {
        this.projectName = name;
    }

    /* Takes counterNumber as a double and divides it by totalrows (which results in a fraction),
       multiplies that by 100 to get percent. Rounds the percent up.
    */
    private double findPercent() {
        this.progressPercent = round((double)this.counterNumber / this.totalRows * 100);
        return this.progressPercent;
    }
    /*
        Set's the counter's totalRows to passed num and sets the passed progress bar's max num to totalRows.
        Sets the progress.
     */
    public void setProgressBarMax(int num) {
        this.totalRows = num;
        this.progressBar.setMax(this.totalRows);
        setProgress();
    }

    /*
        If the progress bar exists and totalRows is greater than 0, set its progress
        (counterNumber / totalRows)
     */
    public void setProgress() {
        if (this.progressBar != null && this.totalRows > 0) { /* TODO when is totalrows first set, when should it be */
            this.progressBar.setProgress(this.counterNumber);
            String formattedProgressNumber = String.format(this.strResProgress, this.findPercent());
            this.textProgress.setText(formattedProgressNumber);
        }
    }

    /*
       If counter number is less than max, increase it by adjustment. Then calls setCounter to get
       the number to the appropriate TextView. If counterNumber goes above COUNTER_MAX (ex: at 9995,
       increase by 10 to go to 10005), set counterNumber to COUNTER_MAX (9999). Sets the progress.
       */
    public void incrementCounter() {
        if (this.counterNumber < this.COUNTER_MAX) this.counterNumber = this.counterNumber + this.adjustment;
        if (this.counterNumber > this.COUNTER_MAX) this.counterNumber = this.COUNTER_MAX;
        setCounter();
        setProgress();
    }

    /*
       If counter number is greater than min, decrease it by 1. Then calls setCounter to get the
       number to the appropriate TextView. If counterNumber drops below COUNTER_MIN (ex: at 5,
       decrease by 10 to go to -5), set counterNumber to COUNTER_MIN (0). Sets the progress.
     */
    public void decrementCounter () {
        if (this.counterNumber > this.COUNTER_MIN) this.counterNumber = this.counterNumber - this.adjustment;
        if (this.counterNumber < this.COUNTER_MIN) this.counterNumber = this.COUNTER_MIN;
        setCounter();
        setProgress();
    }

    /*
       Resets the appropriate counter to 0 and sets the progress bar's progress
    */
    public void resetCounter() {
        this.counterNumber = this.COUNTER_MIN;
        setCounter();
        setProgress();
        /* TODO set up alert that asks user if they're sure they want to reset the counter */
    }

    /*
    Creates a dialog that asks the user if they're sure they want to reset the counter and either
    resets the counter or does nothing, depending on if the user presses yes or no.
    */
    public void resetCounterCheck(String counterType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        String formattedDialogMessage = String.format(res.getString(R.string.dialog_reset_message), counterType);
        builder.setMessage(formattedDialogMessage)
                .setPositiveButton(R.string.button_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        resetCounter();
                    }
                })
                .setNegativeButton(R.string.button_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
        TextView dialogTextView = (TextView) dialog.findViewById(android.R.id.message);
        if (dialogTextView != null) {
            dialogTextView.setTextSize(20);
        }
    }

    /*
        Set's the counter's adjustment number to passed num.
     */
    public void changeAdjustmentNum (int num) {
        this.adjustment = num;
    }

    /*
       Sets the counter's adjustment variable to 1, 5, or 10 and calls setAdjustmentButtonColor
       depending on which adjustment button has been pressed.
    */
    public void changeAdjustment (int adjustmentNum) {
        switch(adjustmentNum)
        {
            case 1:
                this.changeAdjustmentNum(1);
                this.setActiveAdjustmentButtonColor(this.btnAdjustment1);
                this.setInActiveAdjustmentButtonColor(this.btnAdjustment5, this.btnAdjustment10);
                break;
            case 5:
                this.changeAdjustmentNum(5);
                this.setActiveAdjustmentButtonColor(this.btnAdjustment5);
                this.setInActiveAdjustmentButtonColor(this.btnAdjustment1, this.btnAdjustment10);
                break;
            case 10:
                this.changeAdjustmentNum(10);
                this.setActiveAdjustmentButtonColor(this.btnAdjustment10);
                this.setInActiveAdjustmentButtonColor(this.btnAdjustment1, this.btnAdjustment5);
                break;
            default:
                throw new RuntimeException("Unknown button");
        }
    }

    /*
        Sets the Active adjustment button's background and font colors. Has compatibility 'if' logic
        to handle getResources().getColor deprecation in Android Support Library 23.
    */
    private void setActiveAdjustmentButtonColor (Button activeButton) {
        /* Sets Button Color */
        if (Build.VERSION.SDK_INT < 23) {
            /* Android Support Library 22 and earlier compatible */
            ((GradientDrawable)activeButton.getBackground()).setColor(this.res.getColor(R.color.colorAccent));
        } else {
            /* Android Support Library 23 compatible */
            ((GradientDrawable)activeButton.getBackground()).setColor(ContextCompat.getColor(this.context, R.color.colorAccent));
        }

        /* Sets Font Color */
        activeButton.setTextColor(Color.parseColor("#FFFFFF"));
    }

    /*
        Sets the Inactive adjustment buttons' background and font colors.
    */
    private void setInActiveAdjustmentButtonColor (Button inactiveButton1, Button inactiveButton2) {
        /* Sets Button Color */
        ((GradientDrawable)inactiveButton1.getBackground()).setColor(Color.parseColor("#CFCFCF"));
        ((GradientDrawable)inactiveButton2.getBackground()).setColor(Color.parseColor("#CFCFCF"));

        /* Sets Font Color */
        inactiveButton1.setTextColor(Color.parseColor("#696969"));
        inactiveButton2.setTextColor(Color.parseColor("#696969"));
    }

    /* Save Counter */
    public void saveCounter(Counter counter1, Counter counter2) {
        WriteToDb writeToDb = new WriteToDb(this.context);
        if (counter2 != null) {
            writeToDb.execute(counter1, counter2);
        } else {
            writeToDb.execute(counter1);
        }
    }
    //TODO: create saveCounterDebouncer function
}

