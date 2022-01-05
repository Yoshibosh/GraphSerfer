package com.example.coordinatesystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.TypedArrayUtils;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.Touch;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;


import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class MainActivity2 extends AppCompatActivity implements View.OnTouchListener {

    ConstraintLayout constraintLayout;
    DrawView drawView;

    //Коорд. плоскость будет строиться от xMin до xMax, от yMin до yMax
    double xMin = -5;
    double xMax = 5;
    double yMin = -5;
    double yMax = 5;

    Map<String,Double> XYmaxesAndMines = new HashMap<>();


    String funcWeDo = "";
    boolean isClickAlready;

    //Цена деления
    double Cx = 1;
    double Cy = 1;

    boolean inTouch;

    TouchForMove touch = null;
    ArrayList<TouchForMove> scaleTouches = new ArrayList<>();

    boolean isAfter2MultiTouches = false;

    boolean rightMove = true;
    boolean upMove = true;
    boolean scaleMoves = false;

    ArrayList<FormulSistem.Pair<Double,Double>> calculetblePoints = null;

    int touchCounter = 0;

    static class TouchForMove{
        int id;
        double xStart = 0;
        double yStart = 0;
        double xMaxStart;
        double yMaxStart;
        double xMinStart;
        double yMinStart;
        double lastXMovePos;
        double lastYMovePos;
        TouchForMove(int id ,double xStart,double yStart,double xMaxStart, double yMaxStart, double xMinStart, double yMinStart){
            this.id = id;
            this.xStart = xStart;
            this.yStart = yStart;
            this.xMaxStart = xMaxStart;
            this.yMaxStart = yMaxStart;
            this.xMinStart = xMinStart;
            this.yMinStart = yMinStart;
            this.lastXMovePos = xStart;
            this.lastYMovePos = yStart;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);


        constraintLayout = findViewById(R.id.constraintLayoutActiv3);

        drawView = new DrawView(this);
        constraintLayout.addView(drawView,constraintLayout.getLayoutParams());


        XYmaxesAndMines.put("xMin",xMin);
        XYmaxesAndMines.put("xMax",xMax);
        XYmaxesAndMines.put("yMin",yMin);
        XYmaxesAndMines.put("yMax",yMax);


        Button cos = findViewById(R.id.cos);
        cos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funcWeDo = cos.getText().toString();
            }
        });
        Button sin = findViewById(R.id.sin);
        sin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funcWeDo = sin.getText().toString();
            }
        });
        Button exp = findViewById(R.id.exp);
        exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funcWeDo = exp.getText().toString();
            }
        });
        Button pow = findViewById(R.id.pow);
        pow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funcWeDo = pow.getText().toString();
            }
        });
        Button build = findViewById(R.id.build);

        //Присваивание переменным обьекты View, которые находятся в файле разметки activity_main2.xml ,
        // для дальнейшего взаимодействия с ними(Класс родитель их всех - View).
        EditText XmaxEditText = findViewById(R.id.Xmax);
        EditText XminEditText = findViewById(R.id.Xmin);
        EditText YmaxEditText = findViewById(R.id.Ymax);
        EditText YminEditText = findViewById(R.id.Ymin);

        TextView XmaxTextView = findViewById(R.id.xmaxView);
        TextView XminTextView = findViewById(R.id.xminView);
        TextView YmaxTextView = findViewById(R.id.ymaxView);
        TextView YminTextView = findViewById(R.id.yminView);

        EditText CenaDelXEdit = findViewById(R.id.CenaDelXEdit);
        EditText CenaDelYEdit = findViewById(R.id.CenaDelYEdit);
        TextView CenaDelXView = findViewById(R.id.CenaDelX);
        TextView CenaDelYView = findViewById(R.id.CenaDelY);

        SeekBar seekBarXP = findViewById(R.id.seekBarXP);
        SeekBar seekBarYP = findViewById(R.id.seekBarYP);
        SeekBar seekBarXN = findViewById(R.id.seekBarXN);
        SeekBar seekBarYN = findViewById(R.id.seekBarYN);
        seekBarXP.setMax(10);
        seekBarXN.setMax(10);
        seekBarYP.setMax(10);
        seekBarYN.setMax(10);
        ColorDrawable white = new ColorDrawable(getResources().getColor(R.color.black));
        white.setAlpha(255);
        seekBarXN.setBackground(white);
        seekBarXP.setBackground(white);
        seekBarYN.setBackground(white);
        seekBarYP.setBackground(white);
        seekBarXP.setAlpha(1);
        seekBarXN.setAlpha(1);
        seekBarYP.setAlpha(1);
        seekBarYN.setAlpha(1);

        EditText FormulaInput = findViewById(R.id.FormulaInput);



        //        seekBarXP.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                xMax = seekBar.getProgress();
//            }
//        });
//        seekBarXN.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                xMin = seekBar.getProgress();
//            }
//        });
//        seekBarYP.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                yMax = seekBar.getProgress();
//            }
//        });
//        seekBarYN.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                yMin = seekBar.getProgress();
//            }
//        });
        constraintLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isClickAlready){
                    //При нажатии на View constraintLayout(Основной контейнер в этой Активити) Обьекты становятся то видимыми, то невидимыми().
                    //Тоесть убирает GUI чтобы можно было посмотреть на график
                    isClickAlready = false;
                    XmaxEditText.setVisibility(View.VISIBLE);
                    YmaxEditText.setVisibility(View.VISIBLE);
                    XminEditText.setVisibility(View.VISIBLE);
                    YminEditText.setVisibility(View.VISIBLE);

                    cos.setVisibility(View.VISIBLE);
                    sin.setVisibility(View.VISIBLE);
                    exp.setVisibility(View.VISIBLE);
                    pow.setVisibility(View.VISIBLE);

                    XmaxTextView.setVisibility(View.VISIBLE);
                    XminTextView.setVisibility(View.VISIBLE);
                    YmaxTextView.setVisibility(View.VISIBLE);
                    YminTextView.setVisibility(View.VISIBLE);

                    CenaDelXEdit.setVisibility(View.VISIBLE);
                    CenaDelYEdit.setVisibility(View.VISIBLE);
                    CenaDelXView.setVisibility(View.VISIBLE);
                    CenaDelYView.setVisibility(View.VISIBLE);

                    seekBarXP.setVisibility(View.VISIBLE);
                    seekBarYP.setVisibility(View.VISIBLE);
                    seekBarXN.setVisibility(View.VISIBLE);
                    seekBarYN.setVisibility(View.VISIBLE);
                }else{
                    isClickAlready = true;
                    XmaxEditText.setVisibility(View.INVISIBLE);
                    YmaxEditText.setVisibility(View.INVISIBLE);
                    XminEditText.setVisibility(View.INVISIBLE);
                    YminEditText.setVisibility(View.INVISIBLE);

                    cos.setVisibility(View.INVISIBLE);
                    sin.setVisibility(View.INVISIBLE);
                    exp.setVisibility(View.INVISIBLE);
                    pow.setVisibility(View.INVISIBLE);

                    XmaxTextView.setVisibility(View.INVISIBLE);
                    XminTextView.setVisibility(View.INVISIBLE);
                    YmaxTextView.setVisibility(View.INVISIBLE);
                    YminTextView.setVisibility(View.INVISIBLE);

                    CenaDelXEdit.setVisibility(View.INVISIBLE);
                    CenaDelYEdit.setVisibility(View.INVISIBLE);
                    CenaDelXView.setVisibility(View.INVISIBLE);
                    CenaDelYView.setVisibility(View.INVISIBLE);

                    seekBarXP.setVisibility(View.INVISIBLE);
                    seekBarYP.setVisibility(View.INVISIBLE);
                    seekBarXN.setVisibility(View.INVISIBLE);
                    seekBarYN.setVisibility(View.INVISIBLE);
                }
            }
        });
        constraintLayout.setOnTouchListener((v, event) -> {

            int width = v.getWidth();
            int height = v.getHeight();
            touchCounter++;

            int actionMask = event.getActionMasked();
            // индекс касания
            int pointerIndex = event.getActionIndex();
            // число касаний
            int pointerCount = event.getPointerCount();

            Log.i("JopaTouch","{" + pointerCount + ", " + pointerIndex + "}");

            float xDiff = 0;
            float yDiff = 0;

            switch (actionMask) {
                case MotionEvent.ACTION_DOWN: // первое касание
//                    Log.i("TouchEvent","Index = " + event.getActionIndex() + "X = " + event.getX() + "Y = " + event.getY());
                    touch = new TouchForMove(event.getPointerId(pointerIndex),event.getX(),event.getY(),xMax,yMax,xMin,yMin);
                    isAfter2MultiTouches = false;
//                    scaleTouches.add(touch);
                    inTouch = true;
                case MotionEvent.ACTION_POINTER_DOWN: // последующие касания
                    Log.i("TouchEvent2","Index = " + event.getActionIndex() + "X = " + event.getX() + "Y = " + event.getY() + "ass = ");
                    scaleTouches.add(new TouchForMove(event.getPointerId(pointerIndex),event.getX(pointerIndex),event.getY(pointerIndex),xMax,yMax,xMin,yMin));
                    break;

                case MotionEvent.ACTION_UP: // прерывание последнего касания
                    scaleTouches.remove(0);
                    touch = null;
                    inTouch = false;
                    break;
                case MotionEvent.ACTION_POINTER_UP: // прерывания касаний
                    isAfter2MultiTouches = true;
                    scaleTouches.remove(pointerIndex);
                    break;

                case MotionEvent.ACTION_MOVE: // движение
                    if (pointerCount == 2){
                        double[] xDiffA = new double[]{0,0};
                        double[] yDiffA = new double[]{0,0};
                        int secondTouchIndex = 1;
                        int firstTuuchIndex = 0;
                        if (event.getActionIndex() == 0){secondTouchIndex = 1;}
                        int posOrNegDiffX = 1;
                        int posOrNegDiffY = 1;
                        int whoHeier = 0;
                        int whoStayRight = 0;
                        if (event.getX(0) < event.getX(1)){whoStayRight = 1;}
                        if (event.getY(0) > event.getY(1)){whoHeier = 1;}


                        xDiffA[0] = (float)scaleTouches.get(0).lastXMovePos - event.getX(0);
                        yDiffA[0] = (float)scaleTouches.get(0).lastYMovePos - event.getY(0);
                        xDiffA[1] = (float)scaleTouches.get(1).lastXMovePos - event.getX(1);
                        yDiffA[1] = (float)scaleTouches.get(1).lastYMovePos - event.getY(1);

                        double[] sumDiff = new double[]{0,0};
//                        sumDiff[0] += Math.sqrt(Math.pow(xDiffA[0],2) + Math.pow(yDiffA[0],2));
                        for (int i = 0;i < pointerCount;i++){
                            if (Math.abs(xDiffA[i]) > Math.abs(yDiffA[i])){sumDiff[i] = xDiffA[i];}else{sumDiff[i] = yDiffA[i];}
                        }
//                        sumDiff[1] = Math.sqrt(Math.pow(xDiffA[1],2) + Math.pow(yDiffA[1],2));

                        double superSumDiff = sumDiff[0] + sumDiff[1];


                        xMax = xMax + xDiffA[whoStayRight]*(xMax - xMin)/(width) - xDiffA[Math.abs(whoStayRight - 1)]*(xMax - xMin)/(width);
                        xMin = xMin - xDiffA[whoStayRight]*(xMax - xMin)/(width) + xDiffA[Math.abs(whoStayRight - 1)]*(xMax - xMin)/(width);
                        yMax = yMax - yDiffA[whoHeier]*(yMax - yMin)/(height) + yDiffA[Math.abs(whoHeier - 1)]*(yMax - yMin)/(height);
                        yMin = yMin + yDiffA[whoHeier]*(yMax - yMin)/(height) - yDiffA[Math.abs(whoHeier - 1)]*(yMax - yMin)/(height);

                        Log.i("XYMAXMININTOUCH","[xMax = " + xMax + ", xMin = " + xMin + ",yMax = " + yMax + ", yMin = " + yMin + "] + ActionIndex = " + event.getPointerId(pointerIndex) + " size:" + scaleTouches.size());

                        String s = "";
                        for (TouchForMove t : scaleTouches){
                            s += "\n[x=" + t.xStart + " y=" +t.yStart + "]";
                        }
                        Log.i("ScaleTouches",s);

                        scaleTouches.get(0).lastXMovePos =  event.getX(0);
                        scaleTouches.get(0).lastYMovePos =  event.getY(0);

                        scaleTouches.get(1).lastXMovePos =  event.getX(1);
                        scaleTouches.get(1).lastYMovePos =  event.getY(1);

                        scaleMoves = true;

                        constraintLayout.removeView(drawView);
                        constraintLayout.addView(drawView,constraintLayout.getLayoutParams());
                    }
                    if(pointerCount == 1 && !isAfter2MultiTouches){


                        xDiff = (float)touch.xStart - event.getX();
                        yDiff = (float)touch.yStart - event.getY();


                        xMax = touch.xMaxStart + xDiff*(touch.xMaxStart - touch.xMinStart)/(width);
                        xMin = touch.xMinStart + xDiff*(touch.xMaxStart - touch.xMinStart)/(width);
                        yMax = touch.yMaxStart - yDiff*(touch.yMaxStart - touch.yMinStart)/(height);
                        yMin = touch.yMinStart - yDiff*(touch.yMaxStart - touch.yMinStart)/(height);

                        rightMove = (touch.lastXMovePos - event.getX()) > 0;
                        upMove = !(yDiff > 0);

                        Log.i("xDiff","" + xDiff);

                        constraintLayout.removeView(drawView);
                        constraintLayout.addView(drawView,constraintLayout.getLayoutParams());
//                        if (sumDiff >= (width+height)/40){
//                            constraintLayout.removeView(drawView);
//                            constraintLayout.addView(drawView,constraintLayout.getLayoutParams());
//                        }
                        touch.lastXMovePos = event.getX();
                    }
                    break;
            }

            return true;
        });




        build.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraintLayout.removeView(drawView);

                xMax = Double.parseDouble(XmaxEditText.getText().toString());
                xMin = Double.parseDouble(XminEditText.getText().toString());
                yMax = Double.parseDouble(YmaxEditText.getText().toString());
                yMin = Double.parseDouble(YminEditText.getText().toString());
//                xMax = seekBarXP.getProgress();
//                xMin = -seekBarXN.getProgress();
//                yMax = seekBarYP.getProgress();
//                yMin = -seekBarYN.getProgress();
                Cx = Double.parseDouble(CenaDelXEdit.getText().toString());
                Cy = Double.parseDouble(CenaDelYEdit.getText().toString());

                funcWeDo = FormulaInput.getText().toString();

                calculetblePoints = null;

                constraintLayout.addView(drawView,constraintLayout.getLayoutParams());
            }
        });

        seekBarXN.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                constraintLayout.removeView(drawView);

                xMin = -seekBar.getProgress();

                constraintLayout.addView(drawView,constraintLayout.getLayoutParams());

            }
        });
        seekBarXP.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                constraintLayout.removeView(drawView);

                xMax = seekBar.getProgress();

                constraintLayout.addView(drawView,constraintLayout.getLayoutParams());

            }
        });
        seekBarYN.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                constraintLayout.removeView(drawView);

                yMin = - seekBar.getProgress();

                constraintLayout.addView(drawView,constraintLayout.getLayoutParams());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarYP.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                constraintLayout.removeView(drawView);

                yMax = seekBar.getProgress();

                constraintLayout.addView(drawView,constraintLayout.getLayoutParams());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }


    //Пока не нужные функции
    protected double[] XYMaxValues(){
        Bundle arguments = getIntent().getExtras();
        return new double[]{(Double)arguments.get("xMin"),(Double)arguments.get("xMax"),(Double)arguments.get("yMin"),(Double)arguments.get("yMax")};
    }
    protected String WhatFunctionWeDo(){
        Bundle arguments = getIntent().getExtras();
        return String.valueOf(arguments.get("funcName"));
    }
    //Кол-во знаков до запятой
    protected int DecimalPlaces(double Value){
        int decimalPlases = 0;
        char[] string = String.valueOf(Value).toCharArray();
        if (string[0] == '0'){
            for (char s : string){
                decimalPlases++;
            }
        }else{
            return 0;
        }
        return decimalPlases - 2;
    }

    protected Pair<Double,Double> DrobAndCelayaChast(double d){
        char[] s = String.valueOf(d).toCharArray();
        String drob = "";
        String cel = "";
        boolean isDrob = false;
        for (char ch : s){
            if (isDrob){drob += ch;}
            else{ cel += ch; }
            if (ch == '.'){isDrob = true;}
        }
        if (!isDrob){return null;}
        return new Pair<Double,Double>(Double.parseDouble(cel.substring(0,cel.length() - 1)),Double.parseDouble("0." + drob));
    }

    protected String GetTheEditTextText(EditText view){
        return view.getText().toString();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        // событие
        return false;
    }

    class DrawView extends View {


        Paint p;
        Rect rect;

        public DrawView(Context context) {
            super(context);
            p = new Paint();
            rect = new Rect();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            // заливка канвы цветом
            canvas.drawARGB(80, 255, 255, 255);
//            canvas.setBitmap();

            // настройка кисти
            // красный цвет
            p.setColor(Color.RED);
            // толщина линии = 10
            p.setStrokeWidth(5);

            //Высота и ширина экрана
            int width = getWidth();
            int height = getHeight();

            double koef;
            if (Math.log10(xMax - xMin) < 1){koef = -1;}else{koef = 0;}
            int jija = (int)Math.round(Math.log10(xMax - xMin) + koef);
            String jojo = "";
            if (jija < 0){
                jojo += "0.";
                for (int i = jija;i < -1; i++){
                    jojo += "0";
                }
                jojo += "1";
            }else{
                jojo += "1";
                for (int i = 0;i < jija - 1; i++){
                    jojo += "0";
                }
            }
            Cx = Double.parseDouble(jojo);
            if (Math.log10(yMax - yMin) < 1){koef = -1;}else{koef = 0;}
            jija = (int)Math.round(Math.log10(yMax - yMin) + koef);
            jojo = "";
            if (jija < 0){
                jojo += "0.";
                for (int i = jija;i < -1; i++){
                    jojo += "0";
                }
                jojo += "1";
            }else{
                jojo += "1";
                for (int i = 0;i < jija - 1; i++){
                    jojo += "0";
                }
            }
            Cy = Double.parseDouble(jojo);
//            Cy = DrobAndCelayaChast(Math.log10(yMax - yMin)).first;
            Log.i("Cy","" + Cy);


            //Лист с точками функции
            ArrayList<Double> points = new ArrayList<>();

            double step;
            //Центры координатных осей относительно сторон экрана
            double xCentre = ( - xMin/(xMax - xMin))*width;
            double yCentre = (yMax/(yMax - yMin))*height;
            double xRealCentre = xCentre;
            double yRealCentre = yCentre;

            if (xMax < 0){xRealCentre = width;}
            if (xMin > 0){xRealCentre = 0;}
            if (yMax < 0){yRealCentre = 0;}
            if (yMin > 0){yRealCentre = height;}

            //Кол-во делений на коорд сетке
            int kx = (int) ((xMax - xMin)/Cx);
            int ky = (int) ((yMax - yMin)/Cy);

            //Цена деления c учётом разрешения экрана
            double Cex = (double)width/kx;
            double Cey = (double)height/((yMax - yMin)/Cy);

            //Множители X и Y для соотсвествия разрешению экрана
            double xMult = (double)width/(xMax - xMin);
            double yMult = (double)height/(yMax - yMin);


            //Координатная сетка
            p.setColor(Color.BLACK);
            p.setAlpha(100);
            canvas.drawLine((float)xCentre,(float) height,(float)xCentre,0,p);
            canvas.drawLine(0,(float)yCentre,(float)width,(float)yCentre,p);

            //Деления
            //Строиться для каждой полуоси отдельно(4 цикла) выводится текст и строятся деления
            p.setStrokeWidth(3);
            p.setTextSize(30);
            p.setFakeBoldText(false);
            p.setTextAlign(Paint.Align.CENTER);
            //Колво знаков после запятой выводится
            int decimalPlacesX = DecimalPlaces(Cx);
            int decimalPlacesY = DecimalPlaces(Cy);

            Log.i("Decmalplases",decimalPlacesX + " " + decimalPlacesY);
            Log.i("VspomogatZnach","[" +Cex + ',' + Cey + ']' + '+' + '[' + xCentre + ',' + yCentre + ']' );
            Log.i("assHole","[xMax " + xMax + "|xMin " + xMin + "|yMax " + yMax + "|yMin " + yMin + ']');
            double count = 0;
            double Korrektirovka;
            double extraX = 0;

            if (xRealCentre == 0){
                count = DrobAndCelayaChast(xMin/Cx).first*Cx + Cx;

                extraX = (1 - DrobAndCelayaChast(xMin/Cx).second)*Cex;
                Log.i("DROB","" + DrobAndCelayaChast(xMin/Cx).first);
            }
            for (double x = xRealCentre + extraX;x < width + Cex;x += (Cex)){


                p.setAlpha(100);
                canvas.drawLine((float)x,0,(float)x,(float)height,p);
                p.setAlpha(255);


                p.setStyle(Paint.Style.FILL);
                Korrektirovka = 0;


                if (xRealCentre != 0){
//                    if(x + Cex > width){Korrektirovka = -Cex*0.1;}
//                    if(x == xRealCentre){Korrektirovka = Cex*0.1;}
                }
                canvas.drawText(String.format("%." + decimalPlacesX +"f",count), (float)(x + Korrektirovka), (float) yCentre, p);
                count += Cx;
                p.setStyle(Paint.Style.STROKE);
//                canvas.drawText(String.format(,String.valueOf(x / Cex - xCentre / width)), (float)x, (float) yCentre, p);
            }

            extraX = 0;
            count = 0;
            if (xRealCentre == 0){extraX = -Cex;}
            if (xRealCentre == width){
                count = DrobAndCelayaChast( xMax/Cx).first*Cx - Cx;

                extraX = (DrobAndCelayaChast(xMax/Cx).second)*Cex;
                Log.i("DROB","" + DrobAndCelayaChast(xMin/Cx).first);
            }
            for (double x = xRealCentre + extraX;x > (0 - Cex);x -= Cex){

                p.setAlpha(100);
                canvas.drawLine((float)x,0,(float)x,(float)height,p);
                p.setAlpha(255);

                p.setStyle(Paint.Style.FILL);
                Korrektirovka = 0;
//                if(x - Cex < 0){Korrektirovka = Cex*0.1;}
//                if(x == (xRealCentre - Cex)){Korrektirovka = -Cex*0.1;}
                canvas.drawText(String.format("%." + decimalPlacesX +"f",count), (float)(x + Korrektirovka), (float) yCentre, p);
                count -= Cx;
                p.setStyle(Paint.Style.STROKE);
//                canvas.drawText(String.format(,String.valueOf(x / Cex - xCentre / width)), (float)x, (float) yCentre, p);
            }

            double extraY = 0;
            count = -Cy;
            if (yRealCentre == 0){
                count = DrobAndCelayaChast(yMax/Cy).first*Cy - Cy;

                extraY = (DrobAndCelayaChast(yMax/Cy).second)*Cey;
                Log.i("DROB","" + DrobAndCelayaChast(yMin/Cy).first);
            }
            for (double y = yRealCentre + Cey - extraY;y < height;y += Cey){
                p.setAlpha(100);
                canvas.drawLine(0,(float)y,(float)width,(float)y,p);
                p.setAlpha(255);

                p.setStyle(Paint.Style.FILL);
                Korrektirovka = 0;
//                if(y + Cey > height){Korrektirovka = -Cey*0.1;}
//                if(y == (yRealCentre + Cey)){Korrektirovka = +Cey*0.1;}
                canvas.drawText(String.format("%." + decimalPlacesY +"f",count), (float)xCentre, (float) (y + Korrektirovka), p);
                count -= Cy;
                p.setStyle(Paint.Style.STROKE);
            }
            count = Cy;
            if (yRealCentre == height){
                count = DrobAndCelayaChast(yMin/Cy).first*Cy + Cy;

                extraY = (DrobAndCelayaChast(yMin/Cy).second)*Cey;
                Log.i("DROB","" + DrobAndCelayaChast(yMin/Cy).first);
            }
            for (double y = yRealCentre - Cey + extraY;y > 0;y -= Cey){
                p.setAlpha(100);
                canvas.drawLine(0,(float)y,(float)width,(float)y,p);
                p.setAlpha(255);

                p.setStyle(Paint.Style.FILL);
                Korrektirovka = 0;
//                if(y - Cey < 0){Korrektirovka = Cey*0.1;}
//                if(y == (yRealCentre - Cey)){Korrektirovka = Cey*0.1;}
                canvas.drawText(String.format("%." + decimalPlacesY +"f",count), (float)xCentre, (float) (y + Korrektirovka), p);
                count += Cy;
                p.setStyle(Paint.Style.STROKE);
            }
            p.setStrokeWidth(5);

            p.setColor(Color.RED);

            //Строиться с точностью до 0.01*ЦенаДеления
            step = 0.005*Cx;


            double fromx = xMin;
            double tox = xMax;
            try {


                if (calculetblePoints != null) {

                    if (calculetblePoints.size() != 0 ) {
                        fromx = calculetblePoints.get(calculetblePoints.size() - 1).element1;

                        while (calculetblePoints.get(0).element1 < xMin) {
                            calculetblePoints.remove(0);
                        }

                        calculetblePoints.addAll(FormulSistem.Calculate(funcWeDo, fromx, tox, step));

                        fromx = xMin;
                        tox = calculetblePoints.get(0).element1;

                        for (int i = calculetblePoints.size() - 1; calculetblePoints.get(i).element1 > xMax; i--) {
                            calculetblePoints.remove(i);
                        }

                        calculetblePoints.addAll(0, FormulSistem.Calculate(funcWeDo, fromx, tox, step));
                    }

                    fromx = xMin;
                    tox = xMax;


//                    if (rightMove) {
//                        fromx = calculetblePoints.get(calculetblePoints.size() - 1).element1;
//
//                        while (calculetblePoints.get(0).element1 < xMin){
//                            calculetblePoints.remove(0);
//                        }
//
//                        calculetblePoints.addAll(FormulSistem.Calculate(funcWeDo, fromx, tox,step));
//                        Log.i("fromTO1","from " + fromx + " to " + tox);
//                    }else{
//                        tox = calculetblePoints.get(0).element1;
//
//                        for (int i = calculetblePoints.size() - 1;calculetblePoints.get(i).element1 > xMax;i--){
//                            calculetblePoints.remove(i);
//                        }
//
//                        calculetblePoints.addAll(0,FormulSistem.Calculate(funcWeDo, fromx, tox, step));
//                        Log.i("fromTO2","from " + fromx + " to " + tox);
//                    }
////                    Log.i("afterCalc","1 = " + counter + " 2 = " + counter2);

                }else {
                    calculetblePoints = FormulSistem.Calculate(funcWeDo, xMin, xMax, step);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }



            Path path = new Path();
            int qweoipcj = 0;
            if (calculetblePoints != null){
                for (int i = 0; i < calculetblePoints.size();i++){
//
                    if (calculetblePoints.get(i).element2.isNaN()){qweoipcj = i; continue;}
                    if (calculetblePoints.get(i).element2.isInfinite()){
                        Log.i("SEX","TRAAAAAAAAAAH");
                        float asscum;
                        if (calculetblePoints.get(i).element2 > 0){
                            asscum = -10000.0f;
                        }else{
                            asscum = 10000.0f;
                        }
                        path.moveTo((float)(double)calculetblePoints.get(i).element1*(float)xMult + (float)xCentre,asscum );
                        qweoipcj = i;
                        continue;
                    }

                    if (i <= qweoipcj){path.rMoveTo((float)(double)calculetblePoints.get(i).element1*(float)xMult + (float)xCentre,(float)-calculetblePoints.get(i).element2*(float)yMult + (float)yCentre);}
                    path.lineTo((float)(double)calculetblePoints.get(i).element1*(float)xMult + (float)xCentre,(float)-calculetblePoints.get(i).element2*(float)yMult + (float)yCentre);
//
//                    Log.i("calculateble",calculetblePoints.get(i).toString());
//                    if (i >= 1){
//                        points.add(calculetblePoints.get(i).element1*xMult + xCentre);
//                        points.add(-calculetblePoints.get(i).element2*yMult + yCentre);
//                    }
//                    points.add(calculetblePoints.get(i).element1*xMult + xCentre);
//                    points.add(-calculetblePoints.get(i).element2*yMult + yCentre);
                }
            }


            float[] pointsf = new float[points.size()];
            Number[] jopa = new Number[points.size()];
            for (int i = 0;i < points.size();i++){
                jopa[i] = points.get(i);
                pointsf[i] = (jopa[i] != null ? jopa[i].floatValue() : Float.NaN);
            }
//
//            canvas.drawLine(100,100,500,500,p);
            String s = "";
            for (float f : pointsf){
                s += f + " | ";
            }
//            Log.i("assHole",s);
            Log.i("CenaDel","[Cx " + Cx + "|Cy " + Cy + "|Cex " + Cex + "|Cey " + Cey + ']');
//            Log.i("Mult","[" + xMult + '|' + yMult + ']');

//            canvas.drawLines(new float[]{100,100,100,500},p);
            //Построение графика заданной функции
            p.setStrokeWidth(3);
            p.setAntiAlias(true);
            p.setStyle(Paint.Style.STROKE);
            canvas.drawPath(path,p);
            p.setStyle(Paint.Style.STROKE);
//            canvas.drawLines(pointsf,p);

        }

    }
}