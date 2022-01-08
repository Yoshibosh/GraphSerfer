package com.example.coordinatesystem;

import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.TypedArrayUtils;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.text.method.Touch;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;


import java.text.Normalizer;
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

    double lastXmax;
    double lastXmin;
    double lastYmax;
    double lastYmin;

    Map<String,Double> XYmaxesAndMines = new HashMap<>();

    ArrayList<ImageButton> imageButtons = new ArrayList<>();
    ArrayList<Pair<EditText,Integer>> formulaImputs = new ArrayList<>();

    ArrayList<String> funcsWeDo = new ArrayList<>();
    int funcCount = 0;
    boolean isClickAlready;

    //Цена деления
    double Cx = 1;
    double Cy = 1;

    boolean inTouch;

    TouchForMove touch = null;
    ArrayList<TouchForMove> scaleTouches = new ArrayList<>();

    boolean isAfter2MultiTouches = false;
    boolean is2TouchNow = false;
    PointF CentreOf2Touches = new PointF();
    boolean isFirst2Touch = false;

    boolean rightMove = true;
    boolean upMove = true;

    HashMap<String,ArrayList<FormulSistem.Pair<Double,Double>>> allCalculetblePoints = new HashMap<>();

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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);


        constraintLayout = findViewById(R.id.constraintLayoutActiv3);

        drawView = new DrawView(this);
        constraintLayout.addView(drawView,0,constraintLayout.getLayoutParams());


        XYmaxesAndMines.put("xMin",xMin);
        XYmaxesAndMines.put("xMax",xMax);
        XYmaxesAndMines.put("yMin",yMin);
        XYmaxesAndMines.put("yMax",yMax);
//        @SuppressLint("ResourceType") View editText = LayoutInflater.from(this).inflate(R.id.FormulaInput,null);


//        for (int i = 0;i < imageButtons.size();i++){
//            imageButtons.get(i).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    constraintLayout.removeView(v);
//                    constraintLayout.addView(editText,v.getId(),v.getLayoutParams());
//                }
//            });
//        }

        Button build = findViewById(R.id.build);
        LinearLayout lin = (LinearLayout)findViewById(R.id.formula_inputs_container_container);
        LinearLayout linH1 = (LinearLayout)findViewById(R.id.formula_inputs_container);
        LinearLayout linH2 = (LinearLayout)findViewById(R.id.formula_inputs_container2);



        formulaImputs.add(new Pair<>(findViewById(R.id.FormulaInput),0));

        EditText formI = (EditText)findViewById(R.id.FormulaInput);

        ImageButton create = (ImageButton)findViewById(R.id.ImageBtnCreate);
        ImageButton close = (ImageButton)findViewById(R.id.ImageBtnDestroy);

        Log.i("Create",create.toString());

        funcsWeDo.add(formI.getText().toString());

        create.setOnClickListener(v -> {

            LinearLayout linH = (LinearLayout) lin.getChildAt(0);
            EditText newFormulaInput = (EditText)linH.getChildAt(0);
            EditText formInpt = new EditText(v.getContext());

            linH = (LinearLayout) v.getParent();

            formInpt.setBackground(newFormulaInput.getBackground());
            formInpt.setTextSize(30);
            formInpt.setTextColor(newFormulaInput.getCurrentTextColor());
            formInpt.setTextAlignment(newFormulaInput.getTextAlignment());


            ImageButton newClose = new ImageButton(v.getContext());
            newClose.setOnClickListener(v1 -> {
                lin.removeView((View) v1.getParent());
                RefreshDrawParametrs();
            });

            linH.removeView(create);
            linH.addView(formInpt,0,newFormulaInput.getLayoutParams());
            linH.addView(newClose,close.getLayoutParams());
//            linH.getChildAt(1).setBackgroundColor(getColor(R.color.white));
//            linH.getChildAt(1).setVisibility(View.VISIBLE);
//            ImageButton newclose =  (ImageButton) linH.getChildAt(1);
//            newclose.setImageResource(R.drawable.close);

            funcsWeDo.add("");



            LinearLayout newLinH = new LinearLayout(linH.getContext());
            newLinH.setOrientation(LinearLayout.HORIZONTAL);
            newLinH.setGravity(Gravity.END);


            newLinH.addView(create);
            lin.addView(newLinH,linH.getLayoutParams());
        });

//        formI.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

//        formI.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                OnClickListnerRecurse(0);
//            }
//        });


//        EditText ed = (EditText) lin.getChildAt(0);
//        ed.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                OnClickListnerRecurse(0);
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });



        constraintLayout.setOnTouchListener((v, event) -> {

            int width = v.getWidth();
            int height = v.getHeight();
            touchCounter++;

            int actionMask = event.getActionMasked();
            // индекс касания
            int pointerIndex = event.getActionIndex();
            // число касаний
            int pointerCount = event.getPointerCount();

            is2TouchNow = pointerCount == 2;

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
                    isFirst2Touch = !(pointerCount == 2);
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


                        if (!isFirst2Touch){
                            CentreOf2Touches.x = (event.getX(0) + event.getX(1))/2;
                            CentreOf2Touches.y = (event.getY(0) + event.getY(1))/2;
                        }

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

                        double speed = 2;

                        xMax += speed*(1 - CentreOf2Touches.x/width)*(xDiffA[whoStayRight] - xDiffA[Math.abs(whoStayRight - 1)])*(xMax - xMin)/(width);
                        xMin += speed*(CentreOf2Touches.x/width)*(- xDiffA[whoStayRight] + xDiffA[Math.abs(whoStayRight - 1)])*(xMax - xMin)/(width);
                        yMax += speed*(CentreOf2Touches.y/height)*(- yDiffA[whoHeier] + yDiffA[Math.abs(whoHeier - 1)])*(yMax - yMin)/(height);
                        yMin += speed*(1 - CentreOf2Touches.y/height)*(yDiffA[whoHeier] - yDiffA[Math.abs(whoHeier - 1)])*(yMax - yMin)/(height);

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

                        isFirst2Touch = true;
                        constraintLayout.removeView(drawView);
                        constraintLayout.addView(drawView,0,constraintLayout.getLayoutParams());
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
                        constraintLayout.addView(drawView,0,constraintLayout.getLayoutParams());
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




        build.setOnClickListener(v -> RefreshDrawParametrs());

//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }


    void RefreshDrawParametrs(){
        LinearLayout lin = (LinearLayout)findViewById(R.id.formula_inputs_container_container);
        allCalculetblePoints.clear();
        funcsWeDo.clear();
        for (int i = 0;i < lin.getChildCount() - 1;i++) {
            LinearLayout linH = (LinearLayout)lin.getChildAt(i);
            EditText FormulaInput = (EditText)linH.getChildAt(0);
            try {
                Log.i("GetText", ""  + FormulaInput.getText().toString() + " size = " + FormulaInput.getText().toString().length());
                FormulSistem.Calculate(FormulaInput.getText().toString(),0,1,0.2);
                funcsWeDo.add(FormulaInput.getText().toString());

                allCalculetblePoints.put(FormulaInput.getText().toString(),null);


                constraintLayout.removeView(drawView);
                constraintLayout.addView(drawView,0,constraintLayout.getLayoutParams());

            }catch (Exception e){
                Log.i("ERROROFSEX",e.getMessage());
            }
        }
    }

    void OnClickListnerRecurse(int index){
        LinearLayout lin = (LinearLayout)findViewById(R.id.formula_inputs_container);
        if (index >= lin.getChildCount()){return;}
        EditText FormulaInput = (EditText) lin.getChildAt(index);

        FormulaInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int thisIndex = lin.indexOfChild(v);

                if (thisIndex < lin.getChildCount()){
                    OnClickListnerRecurse(thisIndex + 1);
                }

                EditText newFormulaInput = (EditText)lin.getChildAt(thisIndex);
                Log.i("childCount","" + lin.getChildCount() + " i = " + thisIndex);
                if (lin.getChildCount() <= thisIndex) {
                    EditText formInpt = new EditText(newFormulaInput.getContext());
                    formInpt.setBackground(newFormulaInput.getBackground());
                    formInpt.setTextSize(newFormulaInput.getTextSize());
                    formInpt.setTextColor(newFormulaInput.getCurrentTextColor());
                    formInpt.setTextAlignment(newFormulaInput.getTextAlignment());


                    funcsWeDo.add("");
                    lin.addView(formInpt, newFormulaInput.getLayoutParams());
                }


                for (int i = 0;i < lin.getChildCount();i++){
                    if (newFormulaInput.getText().toString().equals("") && i != lin.getChildCount()){
                        lin.removeView(newFormulaInput);
                    }
                }

            }
        });


//            Rec


//        else



//            R
    }

//    void OnClickListnerRecurse(int index){
//        LinearLayout lin = (LinearLayout)findViewById(R.id.formula_inputs_container);
//        if (index >= lin.getChildCount()){return;}
//        EditText FormulaInput = (EditText) lin.getChildAt(index);
//
//        FormulaInput.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//
//                int thisIndex = lin.indexOfChild(v);
//                if (thisIndex < lin.getChildCount()){
//                    OnClickListnerRecurse(thisIndex + 1);
//                }
//
//                EditText newFormulaInput = (EditText)lin.getChildAt(thisIndex);
//                Log.i("childCount","" + lin.getChildCount() + " i = " + index);
//                if (lin.getChildCount() <= index + 1) {
//                    EditText formInpt = new EditText(newFormulaInput.getContext());
//                    formInpt.setBackground(newFormulaInput.getBackground());
//                    formInpt.setTextSize(newFormulaInput.getTextSize());
//                    formInpt.setTextColor(newFormulaInput.getCurrentTextColor());
//                    formInpt.setTextAlignment(newFormulaInput.getTextAlignment());
//
//
//                    funcsWeDo.add("");
//                    lin.addView(formInpt, newFormulaInput.getLayoutParams());
//                }
//
//
//                if (FormulaInput.getText().toString().equals("") && index != lin.getChildCount()){
//                    lin.removeView(newFormulaInput);
//                }
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//    }


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
        }else if (string.length > 3 && Value < 1){
            if (string[0] == '1' && string[3] == 'E'){
                return Integer.parseInt(String.valueOf(string[string.length - 1]));
            }else{
                return 0;
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
        String minus = "";
        if (d < 0){minus = "-";}
        return new Pair<Double,Double>(Double.parseDouble(cel.substring(0,cel.length() - 1)),Double.parseDouble(minus +"0." + drob));
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

            p.setAntiAlias(true);

            //Высота и ширина экрана
            int width = getWidth();
            int height = getHeight();

            Log.i("funcsweDo",funcsWeDo.toString() + " " + funcsWeDo.size());

            //Пердел увелечения и уменьшения масштаба
            if (xMax - xMin < 0.000001){xMax = lastXmax;xMin = lastXmin;}
            if (yMax - yMin < 0.000001){yMax = lastYmax;yMin = lastYmin;}
            if (xMax - xMin > 1000000000){xMax = lastXmax;xMin = lastXmin;}
            if (yMax - yMin > 1000000000){yMax = lastYmax;yMin = lastYmin;}
            lastXmax = xMax;
            lastXmin = xMin;
            lastYmax = yMax;
            lastYmin = yMin;

            double koef;
            if (Math.log10(xMax - xMin) < 1){koef = -0.5;}else{koef = 0.2;}
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
            if (Math.log10(yMax - yMin) < 1){koef = -0.5;}else{koef = 0.2;}
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

            double step;
            //Центры координатных осей относительно сторон экрана
            double xCentre = ( - xMin/(xMax - xMin))*width;
            double yCentre = (yMax/(yMax - yMin))*height;

            //Цена деления c учётом разрешения экрана
            double Cex = (double)width/((xMax - xMin)/Cx);
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


            double count;
            p.setStyle(Paint.Style.FILL);
            count = DrobAndCelayaChast(xMin/Cx).first*Cx - Cx;
            double extra = (1 - DrobAndCelayaChast(xMin/Cx).second)*Cex;
            for (double x = extra - 2*Cex;x < width;x += Cex){


                p.setAlpha(100);
                canvas.drawLine((float)x,0,(float)x,(float)height,p);
                p.setAlpha(50);
                for (double i = x;i < x + Cex; i += Cex/5){
                    canvas.drawLine((float)i,0,(float)i,(float)height,p);
                }
                p.setAlpha(255);

//                if (Cx >= 1000){
//                    if (yCentre < 0){
//                        canvas.drawText("10", (float)(x), (float) (height*0.04), p);
//
//                        p.setTextSize(20);
//                        canvas.drawText(String.format("%.0f",Math.log10(Cx)), (float)(x + width*0.01), (float) (height*0.04 - height*0.01), p);
//                        p.setTextSize(30);
//                    }else if (yCentre > height){
//                        canvas.drawText( + "10", (float)(x), (float) (height*0.98), p);
//
//                        p.setTextSize(20);
//                        canvas.drawText(String.format("%.0f",Math.log10(Cx)), (float)(x + width*0.01), (float) (height*0.98 - height*0.01), p);
//                        p.setTextSize(30);
//                    }else{
//                        canvas.drawText("10", (float)(x), (float) yCentre, p);
//
//                        p.setTextSize(20);
//                        canvas.drawText(String.format("%.0f",Math.log10(Cx)), (float)(x + width*0.01), (float) (yCentre - height*0.01), p);
//                        p.setTextSize(30);
//                    }
//                }else{
//
//                }

                if (yCentre < 0){
                    canvas.drawText(String.format("%." + decimalPlacesX +"f",count), (float)(x), (float) (height*0.04), p);
                }else if (yCentre > height){
                    canvas.drawText(String.format("%." + decimalPlacesX +"f",count), (float)(x), (float) (height*0.98), p);
                }else{
                    canvas.drawText(String.format("%." + decimalPlacesX +"f",count), (float)(x), (float) yCentre, p);
                }


                count += Cx;
            }
            count = DrobAndCelayaChast(yMin/Cy).first*Cy;
            extra = (1 - DrobAndCelayaChast(yMin/Cy).second)*Cey;
            for (double y = height + Cey - extra;y > 0 - Cey;y -= Cey){

                p.setAlpha(100);
                canvas.drawLine(0,(float)y,(float)width,(float)y,p);
                p.setAlpha(50);
                for (double i = y;i < y + Cey; i += Cey/5){
                    canvas.drawLine(0,(float)i,(float)width,(float)i,p);
                }
                p.setAlpha(255);

                if (y == yCentre){count += Cy;continue;}

                if (xCentre < 0){
                    canvas.drawText(String.format("%." + decimalPlacesY +"f",count), (float)(width*(0.02 + Math.pow((decimalPlacesY),1.0001)/100)), (float) (y), p);
                }else if (xCentre > width){
                    canvas.drawText(String.format("%." + decimalPlacesY +"f",count), (float)(width*(0.98 - Math.pow((decimalPlacesY),1.0001)/100)), (float) (y), p);
                }else {
                    canvas.drawText(String.format("%." + decimalPlacesY + "f", count), (float) xCentre, (float) (y), p);
                }
                count += Cy;
            }
            p.setStyle(Paint.Style.STROKE);

            p.setStrokeWidth(5);

            p.setColor(Color.RED);

            //Строиться с точностью до 0.01*ЦенаДеления
            step = 0.005*Cx;

            double fromx = xMin;
            double tox = xMax;
            try {

                for (String func : funcsWeDo){

                    ArrayList<FormulSistem.Pair<Double,Double>> calculetblePoints = allCalculetblePoints.get(func);

                    Log.i("Nigger","" + func);

                    if (false) {
//                    if (calculetblePoints != null) {

                        if (calculetblePoints.size() != 0 ) {
                            fromx = calculetblePoints.get(calculetblePoints.size() - 1).element1;

                            while (calculetblePoints.get(0).element1 < xMin) {
                                calculetblePoints.remove(0);
                            }

                            calculetblePoints.addAll(FormulSistem.Calculate(func, fromx, tox, step));

                            fromx = xMin;
                            tox = calculetblePoints.get(0).element1;

                            for (int i = calculetblePoints.size() - 1; calculetblePoints.get(i).element1 > xMax; i--) {
                                calculetblePoints.remove(i);
                            }

                            calculetblePoints.addAll(0, FormulSistem.Calculate(func, fromx, tox, step));
                        }else{
                            calculetblePoints = FormulSistem.Calculate(func, xMin, xMax, step);
                        }
                    }else {
                        calculetblePoints = FormulSistem.Calculate(func, xMin, xMax, step);
                    }
                    allCalculetblePoints.remove(func);
                    allCalculetblePoints.put(func,calculetblePoints);
                }

            }
            catch (Exception e) {
                e.printStackTrace();
            }


            Log.i("allCalculetblePoints","" + allCalculetblePoints.size() + "\nКЛЮЧИ = \t" + allCalculetblePoints.keySet().toString());
//            for (ArrayList<FormulSistem.Pair<Double,Double>> p : allCalculetblePoints.values()){
//                Log.i("calcPoints","" + p.size());
//            }

            @SuppressLint("DrawAllocation") ArrayList<Path> paths = new ArrayList<>(funcCount);

            int qweoipcj = 0;
            for (int pathCount = 0;pathCount < funcsWeDo.size(); pathCount++){
                @SuppressLint("DrawAllocation") Path path = new Path();
                ArrayList<FormulSistem.Pair<Double,Double>> calculetblePoints = allCalculetblePoints.get(funcsWeDo.get(pathCount));

                if (calculetblePoints != null){
                    for (int i = 0; i < calculetblePoints.size();i++){

                        if (calculetblePoints.get(i).element2.isNaN()){qweoipcj = i + 1; continue;}
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

                    }
                }
                Log.i("ass",path.toString());
                paths.add(path);
            }

//            Log.i("assHole",s);
            Log.i("CenaDel","[Cx " + Cx + "|Cy " + Cy + "|Cex " + Cex + "|Cey " + Cey + ']');
//            Log.i("Mult","[" + xMult + '|' + yMult + ']');

//            canvas.drawLines(new float[]{100,100,100,500},p);
            //Построение графика заданной функции
            p.setStrokeWidth(3);
            p.setStyle(Paint.Style.STROKE);

            for (Path path : paths){
                canvas.drawPath(path,p);
            }

            p.setStyle(Paint.Style.STROKE);
//            canvas.drawLines(pointsf,p);

        }

    }
}