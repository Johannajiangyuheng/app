package com.Johanna.test;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements OnClickListener{

    private View view;
    private final String TAG = "SCORE";


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public void onClick(View v) {


    }

    public void p1A(View v){
        showA(1);
    }

    public void p2A(View v){
        showA(2);
    }

    public void p3A(View v){
        showA(3);
    }

    public void p1B(View v){
        showB(1);
    }

    public void p2B(View v){
        showB(2);
    }

    public void p3B(View v){
        showB(3);
    }

    public void BR(View v){
        TextView outA = (TextView)findViewById(R.id.scoreA);
        TextView outB = (TextView)findViewById(R.id.scoreB);
        outA.setText("0");
        outB.setText("0");
    }

    public void openList(View btn) {
        Intent list = new Intent(this,RateListActivity.class);
        startActivity(list);

    }
    private void showA(int i){
        TextView outA = (TextView)findViewById(R.id.scoreA);
        String oldScoreA = (String)outA.getText();
        String newScoreA;
        newScoreA = String.valueOf(Integer.parseInt(oldScoreA)+i);
        outA.setText(newScoreA);
    }

    private void showB(int i){
        TextView outB = (TextView)findViewById(R.id.scoreB);
        String oldScoreB = (String)outB.getText();
        String newScoreB = String.valueOf(Integer.parseInt(oldScoreB)+i);
        outB.setText(newScoreB);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String scorea = ((TextView)findViewById(R.id.scoreA)).getText().toString();
        String scoreb = ((TextView)findViewById(R.id.scoreB)).getText().toString();

        Log.i(TAG,"onSaveInstancesState");
        outState.putString("teama_score",scorea);
        outState.putString("teamb_score",scoreb);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String scorea = savedInstanceState.getString("teama_score");
        String scoreb = savedInstanceState.getString("teamb_score");

        Log.i(TAG,"onRestoreInstanceState:");
        ((TextView)findViewById(R.id.scoreA)).setText(scorea);
        ((TextView)findViewById(R.id.scoreB)).setText(scoreb);

    }
}
