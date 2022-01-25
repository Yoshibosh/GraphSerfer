package com.example.coordinatesystem;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.ColorRes;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.graphics.drawable.DrawableWrapper;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.res.ConfigurationHelper;
import androidx.core.content.res.TypedArrayUtils;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.AndroidViewModel;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.LayerDrawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.text.method.Touch;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;


import com.google.android.material.tabs.TabLayout;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Ellipse;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Transform;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.world.World;

import java.text.Normalizer;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.stream.Stream;

public class MainActivity2 extends AppCompatActivity implements View.OnTouchListener {

    ArrayList<Body> circles = new ArrayList<>();
    ArrayList<Sides> sidesOfDisplay = new ArrayList<>();


    ConstraintLayout constraintLayout;
    DrawView drawView;

    //Коорд. плоскость будет строиться от xMin до xMax, от yMin до yMax
    double xMin = -5;
    double xMax = 5;
    double yMin = -5;
    double yMax = 5;

    int height = 0;
    int width = 0;

    double xCentre;
    double yCentre;

    double xMult;
    double yMult;

    double step;

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
    boolean isCloseFormInput = false;

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

    ArrayList<Object2D> _2Dobjects = new ArrayList<>();

//  Colors
    ArrayList<Pair<Integer,ImageButton>> colors = new ArrayList<>();
    int colorNow = Color.RED;
    LinearLayout colorChangeMenu;
    ImageButton currentViewColorChange;
    HashMap<String,Integer> colorRGB = new HashMap<>();
    int currentIndexOfChangeColorBtn = 0;

    LayerDrawable curLayerToChange;


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


    int cursorPosition;
    String textString = "";
    TextView outText;


    PointF playerPos = new PointF();
    float speed = 0.01f;
    int koefX;
    int koefY;
    float radiusOfP = 1000.0f;

    boolean cameraFollowPlayer = false;


    int deltaTime = 1;

    ImageButton closeFormInput;


    World<Body> world;
    Body ball;
    Body ground;

    @Override
    public void onBackPressed() {

        isCloseFormInput = true;
        Log.i("Norm","norm");

        ConstraintLayout formInputContainer1 = (ConstraintLayout) findViewById(R.id.constraintLayoutActiv3IN);

        closeFormInput.setImageResource(R.drawable.open_formula_input);

        formInputContainer1.setVisibility(View.GONE);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);


        constraintLayout = findViewById(R.id.constraintLayoutActiv3);

        drawView = new DrawView(this);
        drawView.setId(R.id.DrawLayout);
        Button IOPFJQOIJCQOIYR = findViewById(R.id.DrawLayout);
        constraintLayout.removeView(IOPFJQOIJCQOIYR);
        constraintLayout.addView(drawView,0,constraintLayout.getLayoutParams());

        colorChangeMenu = (LinearLayout) findViewById(R.id.change_color_menu);



        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int widthDisp = size.x;
        int heightDisp = size.y;

        ConstraintLayout formInputContainer = (ConstraintLayout) findViewById(R.id.constraintLayoutActiv3IN);
        formInputContainer.setMaxHeight(heightDisp/2);
        formInputContainer.setMinHeight(heightDisp/2);


        XYmaxesAndMines.put("xMin",xMin);
        XYmaxesAndMines.put("xMax",xMax);
        XYmaxesAndMines.put("yMin",yMin);
        XYmaxesAndMines.put("yMax",yMax);

        Button build = findViewById(R.id.build);
        LinearLayout lin = (LinearLayout)findViewById(R.id.formula_inputs_container_container);

        EditText formI = (EditText)findViewById(R.id.FormulaInput);
        ImageButton firstColorChanger = (ImageButton)findViewById(R.id.first_color_btn);

        colors.add(new Pair<>(firstColorChanger.getDrawingCacheBackgroundColor(),firstColorChanger));



        colorRGB.put("RED",0);
        colorRGB.put("GREEN",0);
        colorRGB.put("BLUE",0);




//        constraintLayout.removeView(forChangeColor);


        firstColorChanger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (colorChangeMenu.getVisibility() == View.VISIBLE){
                    colorChangeMenu.setVisibility(View.GONE);
                    return;
                }

                currentViewColorChange = (ImageButton) v;

                ImageView colorView = (ImageView) colorChangeMenu.getChildAt(3);

                for (int i = 0;i < colors.size();i++){
                    Pair<Integer,ImageButton> p = colors.get(i);
                    if (p.second.equals(v)){
                        colorNow = p.first;
                        currentIndexOfChangeColorBtn = i;
                    }
                }
                colorChangeMenu.setVisibility(View.VISIBLE);

                colorView.setBackgroundColor(colorNow);
            }
        });


        for (int i = 0;i < colorChangeMenu.getChildCount() - 1;i++){
            SeekBar seekBar = (SeekBar) colorChangeMenu.getChildAt(i);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    switch (seekBar.getId()){
                        case R.id.first_seek_bar_in_change_color_menu:
                            colorRGB.put("RED",progress);
                            break;
                        case R.id.second_seek_bar_in_change_color_menu:
                            colorRGB.put("GREEN",progress);
                            break;
                        case R.id.third_seek_bar_in_change_color_menu:
                            colorRGB.put("BLUE",progress);
                            break;
                    }
                    colorNow = Color.rgb(colorRGB.get("RED"),colorRGB.get("GREEN"),colorRGB.get("BLUE"));
                    colorChangeMenu.getChildAt(3).setBackgroundColor(colorNow);


                    ImageButton curBtn = colors.get(currentIndexOfChangeColorBtn).second;
                    curBtn.setBackgroundColor(colorNow);

                    colors.remove(currentIndexOfChangeColorBtn);
                    colors.add(currentIndexOfChangeColorBtn,new Pair<>(colorNow,curBtn));
//                    colors.get(currentIndexOfChangeColorBtn).second.setBackgroundColor(colorNow);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }


        ImageButton create = (ImageButton)findViewById(R.id.ImageBtnCreate);
        ImageButton close = (ImageButton)findViewById(R.id.ImageBtnDestroy);

        close.setOnClickListener(v -> {
            lin.removeViewAt(1);
            RefreshDrawParametrs();
        });

        Log.i("Create",create.toString());

        funcsWeDo.add(formI.getText().toString());

        create.setOnClickListener(v -> {


            LinearLayout linH = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.close_and_formula_input_fragment,lin,false);

            EditText newFormulaInput = (EditText) linH.getChildAt(0);

            ImageButton newClose = (ImageButton) linH.getChildAt(2);
            newClose.setOnClickListener(v1 -> {
                colors.remove(lin.indexOfChild((View) v1.getParent()) - 1);
                lin.removeView((View) v1.getParent());
                RefreshDrawParametrs();
            });

            ImageButton newColorChange = (ImageButton) linH.getChildAt(1);


            newColorChange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (colorChangeMenu.getVisibility() == View.VISIBLE){
                        colorChangeMenu.setVisibility(View.GONE);
                        return;
                    }

                    currentViewColorChange = (ImageButton) v;

                    ImageView colorView = (ImageView) colorChangeMenu.getChildAt(3);

                    for (int i = 0;i < colors.size();i++){
                        Pair<Integer,ImageButton> p = colors.get(i);
                        if (p.second.equals(v)){
                            colorNow = p.first;
                            currentIndexOfChangeColorBtn = i;
                        }
                    }
                    colorChangeMenu.setVisibility(View.VISIBLE);

                    colorView.setBackgroundColor(colorNow);
                }
            });
            Random random = new Random();
            colors.add(new Pair<>(random.nextInt(),newColorChange));

            linH.removeAllViews();
            linH = (LinearLayout) v.getParent();

            linH.removeAllViews();
            linH.addView(newFormulaInput,0);
            linH.addView(newColorChange);
            linH.addView(newClose);
            linH.setBackgroundResource(R.drawable.edittext_shadow);

            LinearLayout newLinH = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.create_button_fragment,lin,false);


            newLinH.addView(create);
            lin.addView(newLinH);


        });

        closeFormInput = (ImageButton)findViewById(R.id.close_open_formulas_input);
        closeFormInput.setOnClickListener(v -> {
            isCloseFormInput = !isCloseFormInput;

            ConstraintLayout formInputContainer1 = (ConstraintLayout) findViewById(R.id.constraintLayoutActiv3IN);


            if (isCloseFormInput){
                closeFormInput.setImageResource(R.drawable.open_formula_input);

                formInputContainer1.setVisibility(View.GONE);

            }else{
                closeFormInput.setImageResource(R.drawable.close_formula_input);

                formInputContainer1.setVisibility(View.VISIBLE);
            }

        });


        ScrollView scrollView = (ScrollView) formInputContainer.getChildAt(0);
       scrollView.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View v, MotionEvent event) {

               colorChangeMenu.setVisibility(View.GONE);
               return false;
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

                        touch.lastXMovePos = event.getX();
                    }
                    break;
            }

            return true;
        });




//        build.setOnClickListener(v -> RefreshDrawParametrs());

        build.setOnClickListener(v -> {
            ball.getTransform().setTranslation( new Vector2(0,0) );
        });


        ImageButton upBtn = findViewById(R.id.upBtn);
        ImageButton downBtn = findViewById(R.id.downBtn);
        ImageButton rightBtn = findViewById(R.id.rightBtn);
        ImageButton leftBtn = findViewById(R.id.leftBtn);

        upBtn.setOnTouchListener((v, event) -> {

            int action = event.getAction();

            if (action == MotionEvent.ACTION_DOWN){
                koefY = -1;
            }else if
            (action == MotionEvent.ACTION_UP){
                koefY = 0;
            }
            return false;
        });
        downBtn.setOnTouchListener((v, event) -> {

            int action = event.getAction();

            if (action == MotionEvent.ACTION_DOWN){
                koefY = 1;
            }else if
            (action == MotionEvent.ACTION_UP){
                koefY = 0;
            }
            return false;
        });
        rightBtn.setOnTouchListener((v, event) -> {

            int action = event.getAction();

            if (action == MotionEvent.ACTION_DOWN){
                koefX = 1;
            }else if
            (action == MotionEvent.ACTION_UP){
                koefX = 0;
            }
            return false;
        });
        leftBtn.setOnTouchListener((v, event) -> {

            int action = event.getAction();

            if (action == MotionEvent.ACTION_DOWN){
                koefX = -1;
            }else if
            (action == MotionEvent.ACTION_UP){
                koefX = 0;
            }
            return false;
        });

                Thread t = new Thread() {

            @Override
            public void run() {
                while (!isInterrupted()) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            RefreshDrawParametrs();
//
//                            if (koefX != 0 || koefY != 0){
//                                playerPos.x += koefX * speed;
//                                playerPos.y += koefY * speed;
////                                RefreshDrawParametrs();
//                            }
//
//                            if (cameraFollowPlayer){
//                                xMin = (double)playerPos.x - (xMax - xMin)/2.0;
//                                xMax = (double)playerPos.x + (xMax - xMin)/2.0;
//                                yMin = (double)-playerPos.y - (yMax - yMin)/2.0;
//                                yMax = (double)-playerPos.y + (yMax - yMin)/2.0;
////                                RefreshDrawParametrs();
//                            }
                        }
                    });
                }
            }
        };

        t.start();

        SeekBar speedSeekBar = findViewById(R.id.speedSeekBar);
        SeekBar radiusSeekBar = findViewById(R.id.radiusSeekBar);
        speedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speed = (float)Math.pow(10,-speedSeekBar.getProgress());
                RefreshDrawParametrs();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        radiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                radiusOfP = (float)radiusSeekBar.getProgress()/10;
                RefreshDrawParametrs();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ImageButton cameraFollowsPlayer = findViewById(R.id.cameraFollowsPlayer);
        cameraFollowsPlayer.setOnClickListener(v -> {
            cameraFollowPlayer = !cameraFollowPlayer;
        });





        _2Dobjects.add(new Rect2D(-2,0,1));
        _2Dobjects.get(0).setSpeed(new MyVector2D(0.001f,0.002f));
        _2Dobjects.get(0).setMass(10);
        _2Dobjects.get(0).elastic = 0.5f;

        _2Dobjects.add(new Rect2D(3,0,1));
        _2Dobjects.get(1).setSpeed(new MyVector2D(0.001f,0.001f));
        _2Dobjects.get(1).setMass(10);
        _2Dobjects.get(1).elastic = 0.5f;

        _2Dobjects.add(new Rect2D(5,2,1));
        _2Dobjects.get(2).setSpeed(new MyVector2D(0.002f,0.001f));
        _2Dobjects.get(2).setMass(10);
        _2Dobjects.get(2).elastic = 0.5f;

        /*------Create a world------*/
        world=new World<Body>();//Create the world
//        world.setGravity(new Vector2(0,-10));//The acceleration of gravity is set to 10m·s^(-2)
        //The following are two other ways of writing
        // world.setGravity(0,-10);
        // world.setGravity(Vector2.create(10,-Math.PI/2));
        world.getSettings().setStepFrequency(0.001);//Set the step frequency, the interval between two calculations is 1 millisecond
        /*------Create a world------*/

        /*------Create entity------*/
        for (int i = 0;i < 10;i++){

            ball=new Body();//Create a container to store the balls
            ball.addFixture(new Ellipse(1,1));//Create a small ball and add it to the container, the width and height are both 0.1m, that is, the radius is 0.05m
            ball.getTransform().setTranslation(i,i);//Set the Y coordinate of the ball to 10m
            ball.setMass(MassType.NORMAL);//Automatically calculate the mass of the ball
            ball.setLinearVelocity(new Vector2(1,1));

            world.addBody(ball);
            circles.add(ball);
        }




        //bottom
        ground =new Body();//The same method to create the ground
        ground.addFixture(new Rectangle((xMax - xMin)*2,0.1));//The ground is a rectangle, 100m wide and 0.1m high
        ground.getTransform().setTranslation(xMin,yMin);//Move the ground down 0.05m to ensure that the Y coordinate of the upper edge of the ground is 0
        ground.setMass(MassType.INFINITE);//S

        world.addBody(ground);

        sidesOfDisplay.add(new Sides(ground,(float) (xMax - xMin),0.1f,sideOfDisplay.BOTTOM));

        //top
        ground =new Body();//The same method to create the ground
        ground.addFixture(new Rectangle((xMax - xMin),0.1));//The ground is a rectangle, 100m wide and 0.1m high
        ground.getTransform().setTranslation(xMin,yMax);//Move the ground down 0.05m to ensure that the Y coordinate of the upper edge of the ground is 0
        ground.setMass(MassType.INFINITE);//S

        world.addBody(ground);

        sidesOfDisplay.add(new Sides(ground,(float) (xMax - xMin),0.1f,sideOfDisplay.TOP));

        //start
        ground = new Body();//The same method to create the ground
        ground.addFixture(new Rectangle(0.1,(yMax - yMin)));//The ground is a rectangle, 100m wide and 0.1m high
        ground.getTransform().setTranslation(xMin,yMin);//Move the ground down 0.05m to ensure that the Y coordinate of the upper edge of the ground is 0
        ground.setMass(MassType.INFINITE);//S

        world.addBody(ground);

        sidesOfDisplay.add(new Sides(ground,0.1f,(float) (yMax - yMin),sideOfDisplay.START));


        //end
        ground = new Body();//The same method to create the ground
        ground.addFixture(new Rectangle(0.1,(yMax - yMin)));//The ground is a rectangle, 100m wide and 0.1m high
        ground.getTransform().setTranslation(xMax,yMin);//Move the ground down 0.05m to ensure that the Y coordinate of the upper edge of the ground is 0
        ground.setMass(MassType.INFINITE);//S

        world.addBody(ground);

        sidesOfDisplay.add(new Sides(ground,0.1f,(float) (yMax - yMin),sideOfDisplay.END));


        /*------Create entity------*/

//        _2Dobjects.add(new Rect2D(4,0,2));
//        _2Dobjects.get(1).setSpeed(new MyVector2D(0.003f,0.001f));
//        _2Dobjects.get(1).setMass(10);
//
//        _2Dobjects.add(new Rect2D(5,0,2));
//        _2Dobjects.get(1).setSpeed(new MyVector2D(0.001f,0.003f));
//        _2Dobjects.get(1).setMass(10);

//        outText = findViewById(R.id.outText);
//        Button cosBtn = findViewById(R.id.cosBtn);
//        Button sinBtn = findViewById(R.id.sinBtn);
//        Button tgBtn = findViewById(R.id.tgBtn);
//        Button ctgBtn = findViewById(R.id.ctgBtn);
//        ImageButton spaceBtn = findViewById(R.id.spaceBtn);
//
////        Thread t = new Thread() {
////
////            @Override
////            public void run() {
////                while (!isInterrupted()) {
////                    try {
////                        Thread.sleep(10);
////                    } catch (InterruptedException e) {
////                        e.printStackTrace();
////                    }
////                    runOnUiThread(new Runnable() {
////                        @Override
////                        public void run() {
////                            outText.setText(textString);
////                        }
////                    });
////                }
////            }
////        };
////
////        t.start();
//
//
//        outText.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return false;
//            }
//        });
//
//        cosBtn.setOnClickListener(v -> {
//            textString = textString.substring(0,cursorPosition) + "cos()" + textString.substring(cursorPosition);
//        });
//        sinBtn.setOnClickListener(v -> {
//            textString = textString.substring(0,cursorPosition) + "sin()" + textString.substring(cursorPosition);
//        });
//        tgBtn.setOnClickListener(v -> {
//            textString = textString.substring(0,cursorPosition) + "tg()" + textString.substring(cursorPosition);
//        });
//        ctgBtn.setOnClickListener(v -> {
//            textString = textString.substring(0,cursorPosition) + "ctg()" + textString.substring(cursorPosition);
//        });
//        spaceBtn.setOnClickListener(v ->{
//            textString = textString.substring(0,cursorPosition) + " " + textString.substring(cursorPosition);
//        });



//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


    }


    void RefreshDrawParametrs(){


        for (Object2D obj : _2Dobjects){



            world.step(1);

            if (obj.getClass().equals(Rect2D.class)){
                Rect2D rect = (Rect2D) obj;

                if (rect.OnCollision()){

                    if (!(rect.color == Color.BLUE)){rect.color = Color.GREEN;}

                    for (Object2D objInCol : rect.objInCollision.first){

                        if (rect.equals(objInCol)){continue;}
                        objInCol.color = Color.BLUE;

                        float movementAngle1 = (float) Math.atan(rect.speed.y/rect.speed.x);
                        float movementAngle2 = (float) Math.atan(objInCol.speed.y/objInCol.speed.x);

                        float scalarSpeed1 = (float) Math.sqrt(Math.pow(rect.speed.x,2) + Math.pow(rect.speed.y,2));
                        float scalarSpeed2 = (float)  Math.sqrt(Math.pow(objInCol.speed.x,2) + Math.pow(objInCol.speed.y,2));


                        float collisionAngle = (float) Math.PI;

                        Log.i("ZnachMove","movementAngle1 = " + movementAngle1 + " movementAngle2 = " +
                                movementAngle2 + " scalarSpeed1 = " + scalarSpeed1 + " scalarSpeed2 = " + scalarSpeed2 + " collisionAngle = " + collisionAngle);





//                        if (rect.speed.x < 0){scalarSpeed1 *= -1;}
//                        if (objInCol.speed.x < 0){scalarSpeed2 *= -1;}
//                        float k =
//
//                        rect.speed.x = (rect.mass - rect.elastic * objInCol.mass) * scalarSpeed1 + objInCol.mass*(1 + rect.elastic)*scalarSpeed2;
//                        rect.speed.x /= rect.mass + objInCol.mass;
//
//                        if (rect.speed.y > 0){scalarSpeed1 *= -1;}
//                        if (objInCol.speed.y > 0){scalarSpeed2 *= -1;}
//
//                        rect.speed.y = (rect.mass - rect.elastic * objInCol.mass) * scalarSpeed1 + objInCol.mass*(1 + rect.elastic)*scalarSpeed2;
//                        rect.speed.y /= rect.mass + objInCol.mass;



//                        rect.speed.x = (float) ((scalarSpeed1 * Math.cos(movementAngle1 - collisionAngle)*(rect.mass - objInCol.mass) +
//                                2 * objInCol.mass * scalarSpeed2 * Math.cos(movementAngle2 - collisionAngle))*Math.cos(collisionAngle)
//                                /(rect.mass + objInCol.mass)
//                                + scalarSpeed1 * Math.sin(movementAngle1 - collisionAngle) * Math.cos(collisionAngle + Math.PI/2));
//
//                        rect.speed.y = (float) ((scalarSpeed1 * Math.cos(movementAngle1 - collisionAngle)*(rect.mass - objInCol.mass) +
//                                2 * objInCol.mass * scalarSpeed2 * Math.cos(movementAngle2 - collisionAngle))*Math.sin(collisionAngle)
//                                /(rect.mass + objInCol.mass)
//                                + scalarSpeed1 * Math.sin(movementAngle1 - collisionAngle) * Math.sin(collisionAngle + Math.PI/2));

//                        objInCol.speed.x = (float) ((scalarSpeed2 * Math.cos(movementAngle2 - collisionAngle)*(objInCol.mass - rect.mass) +
//                                2 * rect.mass * scalarSpeed1 * Math.cos(movementAngle1 - collisionAngle))*Math.cos(collisionAngle)
//                                /(rect.mass + objInCol.mass)
//                                + scalarSpeed2 * Math.sin(movementAngle2 - collisionAngle) * Math.cos(collisionAngle + Math.PI/2));
//
//                        objInCol.speed.y = (float) ((scalarSpeed2 * Math.cos(movementAngle2 - collisionAngle)*(objInCol.mass - rect.mass) +
//                                2 * rect.mass * scalarSpeed1 * Math.cos(movementAngle1 - collisionAngle))*Math.sin(collisionAngle)
//                                /(rect.mass + objInCol.mass)
//                                + scalarSpeed2 * Math.sin(movementAngle2 - collisionAngle) * Math.sin(collisionAngle + Math.PI/2));



                    }
                }else{
                    rect.color = Color.RED;
                }
            }


            if (obj.OnBorderCollision() == WhatCollisionOn.WIDTH){
                obj.speed.x *= -1;
                obj.speed.y *= 1;
            }
            if (obj.OnBorderCollision() == WhatCollisionOn.HEIGHT){
                obj.speed.x *= 1;
                obj.speed.y *= -1;
            }


            obj.setX(obj.x + obj.speed.x);
            obj.setY(obj.y + obj.speed.y);

        }



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

    ArrayList<Path> FuncPointsCalculate(double xMult,double yMult,double xCentre, double yCentre,double step){
        double fromx = xMin;
        double tox = xMax;
        try {

            for (String func : funcsWeDo){

                ArrayList<FormulSistem.Pair<Double,Double>> calculetblePoints = allCalculetblePoints.get(func);

                Log.i("Nigger","" + func);

                if (false) {
//                    if (calculetblePoints != null) {
//
//                        if (calculetblePoints.size() != 0 ) {
//                            fromx = calculetblePoints.get(calculetblePoints.size() - 1).element1;
//
//                            while (calculetblePoints.get(0).element1 < xMin) {
//                                calculetblePoints.remove(0);
//                            }
//
//                            calculetblePoints.addAll(FormulSistem.Calculate(func, fromx, tox, step));
//
//                            fromx = xMin;
//                            tox = calculetblePoints.get(0).element1;
//
//                            for (int i = calculetblePoints.size() - 1; calculetblePoints.get(i).element1 > xMax; i--) {
//                                calculetblePoints.remove(i);
//                            }
//
//                            calculetblePoints.addAll(0, FormulSistem.Calculate(func, fromx, tox, step));
//                        }else{
//                            calculetblePoints = FormulSistem.Calculate(func, xMin, xMax, step);
//                        }
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
            paths.add(path);
        }
        return paths;
    }


    PointF ToDisplayCoords(double x ,double y){
        PointF result = new PointF();

        result.x = (float) (x*xMult + xCentre);
        result.y = (float) (-y*yMult + yCentre);
        return result;
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
            width = getWidth();
            height = getHeight();

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

            //Центры координатных осей относительно сторон экрана
            xCentre = ( - xMin/(xMax - xMin))*width;
            yCentre = (yMax/(yMax - yMin))*height;

            //Цена деления c учётом разрешения экрана
            double Cex = (double)width/((xMax - xMin)/Cx);
            double Cey = (double)height/((yMax - yMin)/Cy);

            //Множители X и Y для соотсвествия разрешению экрана
            xMult = (double)width/(xMax - xMin);
            yMult = (double)height/(yMax - yMin);




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


//          Создание коорд сетки
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

            ArrayList<Path> paths = FuncPointsCalculate(xMult,yMult,xCentre,yCentre,step);


            Log.i("CenaDel","[Cx " + Cx + "|Cy " + Cy + "|Cex " + Cex + "|Cey " + Cey + ']');

            //Построение графика заданной функции
            p.setStrokeWidth(3);
            p.setStyle(Paint.Style.STROKE);

            int indexChisto = 0;
            for (Path path : paths){
                p.setColor(colors.get(indexChisto++).first);
                Log.i("Nigga","" +colors.get(indexChisto - 1).first + "Jopa = " + colors.get(indexChisto - 1).second.getDrawingCacheBackgroundColor());
                canvas.drawPath(path,p);
            }
            p.setColor(Color.RED);
            p.setStyle(Paint.Style.STROKE);
            float radius = (float)Math.log(radiusOfP);

            float xOfPlayer = playerPos.x*(float)xMult + (float)xCentre;
            float yOfPlayer = playerPos.y*(float)yMult + (float)yCentre;
//            canvas.drawCircle(xOfPlayer,yOfPlayer,radius,p);

//            canvas.drawPath(circleOfLife,p);
//            canvas.drawRect(xOfPlayer,yOfPlayer,xOfPlayer + radius*(float)xMult,yOfPlayer + radius*(float)yMult,p);

//            Rect2D rect = (Rect2D) _2Dobjects.get(0);

//            for(Object2D obj :  _2Dobjects){
//
//                Rect2D rect = (Rect2D)obj;
//
//                Log.i("rectPos","x = " + rect.x + " y = " + rect.y);
//
//                p.setColor(rect.color);
//                canvas.drawLine(rect.x*(float) xMult + (float) xCentre,-rect.y*(float) yMult + (float) yCentre,(rect.x + rect.speed.x*1000)*(float) xMult+ (float) xCentre, -(rect.y + rect.speed.y*1000)*(float) yMult + (float) yCentre,p);
//
//                canvas.drawRect((float) (rect.x*xMult + xCentre),(float) (-rect.y*yMult + yCentre),(float) ((rect.x + rect.side)*xMult + xCentre),(float) (-(rect.y + rect.side)*yMult + yCentre),p);
//            }




            for (Sides body : sidesOfDisplay){

                body.Refresh();

                Log.i("PosRect", "x = " + body.posStart.x + " y = " + body.posStart.y);


                PointF rect = ToDisplayCoords(body.body.getTransform().getTranslationX(),body.body.getTransform().getTranslationY());
                PointF endRect = ToDisplayCoords(body.body.getTransform().getTranslationX() + body.width,body.body.getTransform().getTranslationY() + body.height);


                canvas.drawLine(rect.x,rect.y,endRect.x,endRect.y,p);

            }


            for (Body body : circles){


                PointF circle = ToDisplayCoords(body.getTransform().getTranslationX(),body.getTransform().getTranslationY());
                canvas.drawCircle((float) circle.x,circle.y,0.5f* (float) xMult,p);

                Log.i("Pos", "x = " + body.getTransform().getTranslationX() + " y = " + body.getTransform().getTranslationY());

            }

        }

    }

    class Rect2DMy{
        Body body;
        PointF posStart;
        PointF posEnd;
        float width;
        float height;

        Rect2DMy(Body body,float width,float height){
            this.body = body;
            this.posStart = new PointF((float) body.getTransform().getTranslationX(),(float) body.getTransform().getTranslationY());
            this.width = width;
            this.height = height;
        }

        void Refresh(){
            this.posStart = new PointF((float) body.getTransform().getTranslationX(),(float) body.getTransform().getTranslationY());
            this.posEnd = new PointF(this.posStart.x + width, this.posStart.y + height);
        }

    }


    enum sideOfDisplay{
        START,
        END,
        TOP,
        BOTTOM
    }
    class Sides extends Rect2DMy{

        sideOfDisplay side;

        Sides(Body body,float width,float height,sideOfDisplay side){
            super(body,width,height);
            this.side = side;
        }

        @Override
        void Refresh() {
            super.Refresh();

            this.body.removeAllFixtures();

            if (side == sideOfDisplay.START){

                this.body.addFixture(new Rectangle(0.1,(yMax - yMin)/2));

//                this.body.translate(new Vector2( xMin - this.posStart.x,yMin - this.posStart.y ));
                this.body.getTransform().setTranslation(xMin + (xMax - xMin)/2,yMin + (yMax - yMin)/2);

            }else if (side == sideOfDisplay.END){

                this.body.addFixture(new Rectangle(0.1,(yMax - yMin)/2));

//                this.body.translate(new Vector2( xMax - this.posEnd.x,yMin - this.posStart.y ));
                this.body.getTransform().setTranslation(xMax - (xMax - xMin)/2,yMin + (yMax - yMin)/2);

            }else if (side == sideOfDisplay.TOP){

                this.body.addFixture(new Rectangle((xMax - xMin)/2,0.1));

//                this.body.translate(new Vector2( xMin - this.posStart.x,yMax - this.posEnd.y ));
                this.body.getTransform().setTranslation(xMin + (xMax - xMin)/2,yMax - (yMax - yMin)/2);

            }else if (side == sideOfDisplay.BOTTOM){

                this.body.addFixture(new Rectangle((xMax - xMin)/2,0.1));

//                this.body.translate(new Vector2( xMin - this.posStart.x,yMin - this.posStart.y ));
                this.body.getTransform().setTranslation(xMin + (xMax - xMin)/2,yMin + (yMax - yMin)/2);

            }
        }
    }

    class Object2D{
        MyVector2D speed;
        protected float y;
        protected float x;
        protected int color;
        protected float elastic;

        protected float mass;

        Object2D(){
        }

        public void setX(float x) {
            this.x = x;
        }

        public void setY(float y) {
            this.y = y;
        }

        public void setMass(float mass) {
            this.mass = mass;
        }

        public WhatCollisionOn OnBorderCollision(){
            if ((x >= xMax) || (x <= xMin)){return WhatCollisionOn.WIDTH;}
            if ((y >= yMax) || (y <= yMin)){return WhatCollisionOn.HEIGHT;}
            return null;
        }

        public boolean OnCollision() {
            for (String func : funcsWeDo){
                ArrayList<FormulSistem.Pair<Double,Double>> curGraph = allCalculetblePoints.get(func);
//                Log.i("KeysAndGays", " key = " + func + " gay = " +  allCalculetblePoints.containsKey(func) + "jopa = " + allCalculetblePoints.keySet());
                if (curGraph == null){continue;}
                Log.i("Anal","ajfop");
                Log.i("Collision","step = " + step + " xGraph = " + curGraph.get(curGraph.size() - 1).element1 + " this.x = " + this.x + " yGraph = " + curGraph.get(curGraph.size() - 1).element2 + " this.y = " + this.y );

                for (FormulSistem.Pair<Double,Double> point : curGraph){
                    if (point.element1 - (this.x ) < step){
                        return true;
                    }
                    if (point.element2 - this.y < step){
                        return true;
                    }
                }
            }
            return false;
        }


        public void setSpeed(MyVector2D speed) {
            this.speed = speed;
        }
    }

    class Rect2D extends Object2D{
        float side;
        PointF start = new PointF(this.x,this.y);
        PointF end = new PointF(this.x + side,this.y + side);

        @Override
        public void setX(float x) {
            super.setX(x);
            start.x = x;
            end.x = x + side;
        }

        @Override
        public void setY(float y) {
            super.setY(y);
            start.y = y;
            end.y = y + side;
        }

        public void setSide(float side){
            this.side = side;
        }

        Pair<ArrayList<Object2D>,Float> objInCollision = new Pair<>(new ArrayList<>(),0f);

        Rect2D(float x,float y,float side){
            this.x = x;
            this.y = y;
            this.side = side;
        }

        @Override
        public WhatCollisionOn OnBorderCollision(){
            if ((x + side >= xMax) || (x <= xMin)){return WhatCollisionOn.WIDTH;}
            if ((y + side >= yMax) || (y <= yMin)){return WhatCollisionOn.HEIGHT;}
            return null;
        }



        @Override
        public boolean OnCollision() {
            this.start.x = this.x;
            this.start.y = this.y;
            this.end.x = this.x + side;
            this.end.y = this.y + side;

            boolean onCollision = false;
            for (Object2D obj : _2Dobjects){

                if (obj.getClass().equals(Rect2D.class)){

                    Rect2D anotherRect = (Rect2D) obj;

//                    Log.i("SwingerParty","x = " + anotherRect.x + " y = " + anotherRect.x + " ");


                    if (anotherRect.equals(this)){continue;}
                    objInCollision.first.remove(anotherRect);


                    if (
                            this.start.x < anotherRect.end.x &&
                            this.end.x > anotherRect.start.x && 
                            this.start.y < anotherRect.end.y &&
                            this.end.y > anotherRect.start.y
                    )
                    {
//                        if (
//                                this.start.y < anotherRect.end.y &&
//                                this.end.y > anotherRect.start.y &&
//                                this.end.x < anotherRect.start.x + anotherRect.side/2
//                        ){
//                            objInCollision.second =
//                        }

                        objInCollision.first.add(anotherRect);
                        onCollision = true;
                    }

//                    float newXRect = this.x + this.speed.x;
//                    float newYRect = this.y + this.speed.y;
//
//                    float newXAnotherRect = anotherRect.x + anotherRect.speed.x;
//                    float newYAnotherRect = anotherRect.y + anotherRect.speed.y;
//
//                    if (IsInBetween(newYRect,anotherRect.y - anotherRect.side,newYAnotherRect) && !IsInBetween(newXRect,anotherRect.x - anotherRect.side,newXAnotherRect))
//                    {
//                        objInCollision.add(anotherRect);
//                        onCollision = true;
//                    }

                }
            }
            return onCollision;
        }
    }

    class Circle2D extends Object2D{
        float radius;

        Circle2D(float x,float y,float radius){
            this.x = x;
            this.y = y;
            this.radius = radius;
        }

        @Override
        public WhatCollisionOn OnBorderCollision(){
            if ((x + radius >= xMax) || (x - radius <= xMin)){return WhatCollisionOn.WIDTH;}
            if ((y + radius >= yMax) || (y - radius <= yMin)){return WhatCollisionOn.HEIGHT;}
            return null;
        }


        @Override
        public boolean OnCollision() {
            for (String func : funcsWeDo){
                ArrayList<FormulSistem.Pair<Double,Double>> curGraph = allCalculetblePoints.get(func);
//                Log.i("KeysAndGays", " key = " + func + " gay = " +  allCalculetblePoints.containsKey(func) + "jopa = " + allCalculetblePoints.keySet());
                if (curGraph == null){continue;}

                for (FormulSistem.Pair<Double,Double> point : curGraph){
//                    if (Math.pow(point.element1 - this.x,2) + Math.pow(point.element2 - this.y,2) == this.radius){
//                        Log.i("CollisionDJIOQW","step = " + step + " xGraph = " + point.element1 + " this.x = " + this.x + " yGraph = " + point.element2 + " this.y = " + this.y);
//                        return true;
//                    }

                    if ((((this.x - radius) < point.element1) && (point.element1 < (this.x + radius))) && (((this.y - radius) < point.element2) && (point.element2 < (this.y + radius)))){
                        Log.i("CollisionDJIOQW","step = " + step + " xGraph = " + point.element1 + " this.x = " + this.x + " yGraph = " + point.element2 + " this.y = " + this.y);


                        return true;
                    }
                }
            }
            return false;
        }
    }

    enum WhatCollisionOn{
        WIDTH,
        HEIGHT,
        LEFT,
        RIGHT,
        TOP,
        BOTTOM
    }



    class MyVector2D{
        float x;
        float y;
        MyVector2D(float x,float y){
            this.x = x;
            this.y = y;
        }
    }

    boolean IsInBetween(float x,float startx,float endx){
        return (x > startx && x < endx) || (x < startx && x > endx);
    }
}