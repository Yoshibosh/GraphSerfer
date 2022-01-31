package com.example.coordinatesystem;



import org.dyn4j.collision.CollisionItem;
import org.dyn4j.collision.CollisionPair;
import org.dyn4j.collision.Fixture;
import org.dyn4j.collision.broadphase.*;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.TimeStep;
import org.dyn4j.geometry.*;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.world.PhysicsWorld;
import org.dyn4j.world.World;
import org.dyn4j.world.listener.StepListener;


import javax.swing.*;
import javax.swing.border.StrokeBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.Shape;
import java.awt.event.*;
import java.awt.geom.*;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.Normalizer;
import java.util.*;
import java.util.List;

public class MainClass{


    ArrayList<Body> circles = new ArrayList<>();
    ArrayList<Sides> sidesOfDisplay = new ArrayList<>();
    Body graphs;
    int approximationAccuracy = 1;


    JFrame frame;
//    ConstraintLayout constraintLayout;
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

//    Map<String,Double> XYmaxesAndMines = new HashMap<>();

    ArrayList<String> funcsWeDo = new ArrayList<>();
    int funcCount = 0;

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

    boolean isBallPlaceClick = false;

//  Colors

    ArrayList<FormulSistem.Pair<Integer,JButton>> colors = new ArrayList<>();
    Color colorNow = Color.RED;
//    LinearLayout colorChangeMenu;
//    ImageButton currentViewColorChange;
    HashMap<String,Color> colorRGB = new HashMap<>();
    int currentIndexOfChangeColorBtn = 0;

    ArrayList<Link> links = new ArrayList<>();



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


    boolean cameraFollowPlayer = false;

    long deltaTime = 1;

//    ImageButton closeFormInput;


    World<Body> world;
    Body ball;
    Body ground;


    javax.swing.JTextArea FormulaInput;
//    @Override
//    public void onBackPressed() {
//
//        isCloseFormInput = true;
//        Log.i("Norm","norm");
//
//        ConstraintLayout formInputContainer1 = (ConstraintLayout) findViewById(R.id.constraintLayoutActiv3IN);
//
//        closeFormInput.setImageResource(R.drawable.open_formula_input);
//
//        formInputContainer1.setVisibility(View.GONE);
//
//    }

    protected void onCreate() {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main4);

        frame = new JFrame();
        frame.setSize(1000,1000);
        frame.setLayout(new GridLayout(1,2));
        frame.setVisible(true);
//        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//        frame.setUndecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        drawView = new DrawView(1000,1000);
//        drawView.setVisible(true);
//        drawView.setSize(1000,1000);
        frame.add(drawView);


//        constraintLayout = findViewById(R.id.constraintLayoutActiv3);

//        drawView = new DrawView(this);
//        drawView.setId(R.id.DrawLayout);
//        Button IOPFJQOIJCQOIYR = findViewById(R.id.DrawLayout);
//        constraintLayout.removeView(IOPFJQOIJCQOIYR);
//        constraintLayout.addView(drawView,0,constraintLayout.getLayoutParams());
//
//        colorChangeMenu = (LinearLayout) findViewById(R.id.change_color_menu);
//
//
//
//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        int widthDisp = size.x;
//        int heightDisp = size.y;
//
//        ConstraintLayout formInputContainer = (ConstraintLayout) findViewById(R.id.constraintLayoutActiv3IN);
//        formInputContainer.setMaxHeight(heightDisp/2);
//        formInputContainer.setMinHeight(heightDisp/2);


//        XYmaxesAndMines.put("xMin",xMin);
//        XYmaxesAndMines.put("xMax",xMax);
//        XYmaxesAndMines.put("yMin",yMin);
//        XYmaxesAndMines.put("yMax",yMax);

//        Button build = findViewById(R.id.build);
//        LinearLayout lin = (LinearLayout)findViewById(R.id.formula_inputs_container_container);
//
//        EditText formI = (EditText)findViewById(R.id.FormulaInput);
//        ImageButton firstColorChanger = (ImageButton)findViewById(R.id.first_color_btn);
//
//        colors.add(new FormulSistem.Pair<>(firstColorChanger.getDrawingCacheBackgroundColor(),firstColorChanger));


        JPanel rightBox = new JPanel();
        rightBox.setLayout(new GridLayout(5,1));
        FormulaInput = new JTextArea("x^2");
//        FormulaInput.setSize(rightBox.getWidth(),FormulaInput.getHeight());
//        FormulaInput.setSize(200,100);
        rightBox.setVisible(true);
        rightBox.add(FormulaInput);

        frame.add(rightBox);

        JSlider friction = new JSlider();
        friction.setMaximum(1000);
        friction.setMinimum(0);
        friction.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                for (Body circle : circles){
                    circle.getFixture(0).setFriction(friction.getValue());
                }

            }
        });

        JSlider restitution = new JSlider();
        restitution.setMaximum(100);
        restitution.setMinimum(0);
        restitution.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                System.out.println("RestituationValue " + restitution.getValue() );

                for (Body circle : circles){
                    circle.getFixture(0).setRestitution(restitution.getValue()/100.0);
                }

            }
        });

        rightBox.add(friction);
        rightBox.add(restitution);

        colorRGB.put("RED",Color.BLACK);
        colorRGB.put("GREEN",Color.BLACK);
        colorRGB.put("BLUE",Color.BLACK);





//        constraintLayout.removeView(forChangeColor);


//        firstColorChanger.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (colorChangeMenu.getVisibility() == View.VISIBLE){
//                    colorChangeMenu.setVisibility(View.GONE);
//                    return;
//                }
//
//                currentViewColorChange = (ImageButton) v;
//
//                ImageView colorView = (ImageView) colorChangeMenu.getChildAt(3);
//
//                for (int i = 0;i < colors.size();i++){
//                    Pair<Integer,ImageButton> p = colors.get(i);
//                    if (p.second.equals(v)){
//                        colorNow = p.first;
//                        currentIndexOfChangeColorBtn = i;
//                    }
//                }
//                colorChangeMenu.setVisibility(View.VISIBLE);
//
//                colorView.setBackgroundColor(colorNow);
//            }
//        });


//        for (int i = 0;i < colorChangeMenu.getChildCount() - 1;i++){
//            SeekBar seekBar = (SeekBar) colorChangeMenu.getChildAt(i);
//            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//                @SuppressLint("NonConstantResourceId")
//                @Override
//                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                    switch (seekBar.getId()){
//                        case R.id.first_seek_bar_in_change_color_menu:
//                            colorRGB.put("RED",progress);
//                            break;
//                        case R.id.second_seek_bar_in_change_color_menu:
//                            colorRGB.put("GREEN",progress);
//                            break;
//                        case R.id.third_seek_bar_in_change_color_menu:
//                            colorRGB.put("BLUE",progress);
//                            break;
//                    }
//                    colorNow = Color.rgb(colorRGB.get("RED"),colorRGB.get("GREEN"),colorRGB.get("BLUE"));
//                    colorChangeMenu.getChildAt(3).setBackgroundColor(colorNow);
//
//
//                    ImageButton curBtn = colors.get(currentIndexOfChangeColorBtn).second;
//                    curBtn.setBackgroundColor(colorNow);
//
//                    colors.remove(currentIndexOfChangeColorBtn);
//                    colors.add(currentIndexOfChangeColorBtn,new Pair<>(colorNow,curBtn));
////                    colors.get(currentIndexOfChangeColorBtn).second.setBackgroundColor(colorNow);
//                }
//
//                @Override
//                public void onStartTrackingTouch(SeekBar seekBar) {
//
//                }
//
//                @Override
//                public void onStopTrackingTouch(SeekBar seekBar) {
//
//                }
//            });
//        }


//        ImageButton create = (ImageButton)findViewById(R.id.ImageBtnCreate);
//        ImageButton close = (ImageButton)findViewById(R.id.ImageBtnDestroy);
//
//        close.setOnClickListener(v -> {
//            lin.removeViewAt(1);
//            RefreshDrawParametrs();
//        });
//
//        Log.i("Create",create.toString());
//
//        funcsWeDo.add(formI.getText().toString());
//
//        create.setOnClickListener(v -> {
//
//
//            LinearLayout linH = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.close_and_formula_input_fragment,lin,false);
//
//            EditText newFormulaInput = (EditText) linH.getChildAt(0);
//
//            ImageButton newClose = (ImageButton) linH.getChildAt(2);
//            newClose.setOnClickListener(v1 -> {
//                colors.remove(lin.indexOfChild((View) v1.getParent()) - 1);
//                lin.removeView((View) v1.getParent());
//                RefreshDrawParametrs();
//            });
//
//            ImageButton newColorChange = (ImageButton) linH.getChildAt(1);
//
//
//            newColorChange.setOnClickListener(v12 -> {
//                if (colorChangeMenu.getVisibility() == View.VISIBLE){
//                    colorChangeMenu.setVisibility(View.GONE);
//                    return;
//                }
//
//                currentViewColorChange = (ImageButton) v12;
//
//                ImageView colorView = (ImageView) colorChangeMenu.getChildAt(3);
//
//                for (int i = 0;i < colors.size();i++){
//                    Pair<Integer,ImageButton> p = colors.get(i);
//                    if (p.second.equals(v12)){
//                        colorNow = p.first;
//                        currentIndexOfChangeColorBtn = i;
//                    }
//                }
//                colorChangeMenu.setVisibility(View.VISIBLE);
//
//                colorView.setBackgroundColor(colorNow);
//            });
//            Random random = new Random();
//            colors.add(new Pair<>(random.nextInt(),newColorChange));
//
//            linH.removeAllViews();
//            linH = (LinearLayout) v.getParent();
//
//            linH.removeAllViews();
//            linH.addView(newFormulaInput,0);
//            linH.addView(newColorChange);
//            linH.addView(newClose);
//            linH.setBackgroundResource(R.drawable.edittext_shadow);
//
//            LinearLayout newLinH = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.create_button_fragment,lin,false);
//
//
//            newLinH.addView(create);
//            lin.addView(newLinH);
//
//
//        });
//
//        closeFormInput = (ImageButton)findViewById(R.id.close_open_formulas_input);
//        closeFormInput.setOnClickListener(v -> {
//            isCloseFormInput = !isCloseFormInput;
//
//
//
//            if (isCloseFormInput){
//                closeFormInput.setImageResource(R.drawable.open_formula_input);
//
//                formInputContainer.setVisibility(View.GONE);
//
//            }else{
//                closeFormInput.setImageResource(R.drawable.close_formula_input);
//
//                formInputContainer.setVisibility(View.VISIBLE);
//            }
//
//        });
//
//
//        ScrollView scrollView = (ScrollView) formInputContainer.getChildAt(0);
//       scrollView.setOnTouchListener(new View.OnTouchListener() {
//           @Override
//           public boolean onTouch(View v, MotionEvent event) {
//
//               colorChangeMenu.setVisibility(View.GONE);
//               return false;
//           }
//       });
        drawView.addMouseListener(new MouseListener() {



            @Override
            public void mouseClicked(MouseEvent e) {


            }

            @Override
            public void mousePressed(MouseEvent e) {

                if (isBallPlaceClick){
                    ball.getTransform().setTranslation((e.getX() - xCentre)/xMult,-(e.getY() - yCentre)/yMult);
                    isBallPlaceClick =false;
                }

                frame.requestFocus();

                touch = new TouchForMove(e.getID(),e.getX(),e.getY(),xMax,yMax,xMin,yMin);


            }

            @Override
            public void mouseReleased(MouseEvent e) {


            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        drawView.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {


                float xDiff = 0;
                float yDiff = 0;


                xDiff = (float)touch.xStart - e.getX();
                yDiff = (float)touch.yStart - e.getY();


                xMax = touch.xMaxStart + xDiff*(touch.xMaxStart - touch.xMinStart)/(drawView.width);
                xMin = touch.xMinStart + xDiff*(touch.xMaxStart - touch.xMinStart)/(drawView.width);
                yMax = touch.yMaxStart - yDiff*(touch.yMaxStart - touch.yMinStart)/(drawView.height);
                yMin = touch.yMinStart - yDiff*(touch.yMaxStart - touch.yMinStart)/(drawView.height);

                rightMove = (touch.lastXMovePos - e.getX()) > 0;
                upMove = !(yDiff > 0);




                touch.lastXMovePos = e.getX();

//                RefreshDrawParametrs();
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });

        drawView.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (!isFirst2Touch){
                    CentreOf2Touches.x = e.getX();
                    CentreOf2Touches.y = e.getY();
                }

                        double diff = e.getPreciseWheelRotation();

                        double speed = 50;

                        xMax += speed*(1 - CentreOf2Touches.x/drawView.width)*(diff)*(xMax - xMin)/(drawView.width);
                        xMin += speed*(CentreOf2Touches.x/drawView.width)*(- diff)*(xMax - xMin)/(drawView.width);
                        yMax += speed*(1 - CentreOf2Touches.y/drawView.height)*(diff)*(yMax - yMin)/(drawView.height);
                        yMin += speed*(CentreOf2Touches.y/drawView.height)*(-diff)*(yMax - yMin)/(drawView.height);

//                        Log.i("XYMAXMININTOUCH","[xMax = " + xMax + ", xMin = " + xMin + ",yMax = " + yMax + ", yMin = " + yMin + "] + ActionIndex = " + e.getPointerId(pointerIndex) + " size:" + scaleTouches.size());

//                        String s = "";
//                        for (TouchForMove t : scaleTouches){
//                            s += "\n[x=" + t.xStart + " y=" +t.yStart + "]";
//                        }
//                        Log.i("ScaleTouches",s);

//                        scaleTouches.get(0).lastXMovePos =  e.getX(0);
//                        scaleTouches.get(0).lastYMovePos =  e.getY(0);
//
//                        scaleTouches.get(1).lastXMovePos =  e.getX(1);
//                        scaleTouches.get(1).lastYMovePos =  e.getY(1);



                        isFirst2Touch = true;
//                        RefreshDrawParametrs();
                }
        });


        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

                float velocityAdd = 5;

                switch (e.getKeyCode()){
                    case KeyEvent.VK_LEFT:

                        for (Body circle : circles){
                            Vector2 prevVelocity = circle.getLinearVelocity();
                            circle.setLinearVelocity(prevVelocity.x - velocityAdd,prevVelocity.y);
                        }

                        break;
                    case KeyEvent.VK_RIGHT:

                        for (Body circle : circles){
                            Vector2 prevVelocity = circle.getLinearVelocity();
                            circle.setLinearVelocity(prevVelocity.x + velocityAdd,prevVelocity.y);
                        }

                        break;
                    case KeyEvent.VK_DOWN:

                        for (Body circle : circles){
                            Vector2 prevVelocity = circle.getLinearVelocity();
                            circle.setLinearVelocity(prevVelocity.x,prevVelocity.y - velocityAdd);
                        }

                        break;
                    case KeyEvent.VK_UP:

                        for (Body circle : circles){
                            Vector2 prevVelocity = circle.getLinearVelocity();
                            circle.setLinearVelocity(prevVelocity.x,prevVelocity.y + velocityAdd);
                        }

                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
//        constraintLayout.setOnTouchListener((v, event) -> {
//
//
//            int width = v.getWidth();
//            int height = v.getHeight();
//            touchCounter++;
//
//            int actionMask = event.getActionMasked();
//            // индекс касания
//            int pointerIndex = event.getActionIndex();
//            // число касаний
//            int pointerCount = event.getPointerCount();
//
//            switch (actionMask) {
//                case MotionEvent.ACTION_DOWN: // первое касание
////                    Log.i("TouchEvent","Index = " + event.getActionIndex() + "X = " + event.getX() + "Y = " + event.getY());
//
//                case MotionEvent.ACTION_POINTER_DOWN: // последующие касания
//                    Log.i("TouchEvent2","Index = " + event.getActionIndex() + "X = " + event.getX() + "Y = " + event.getY() + "ass = ");
//                    scaleTouches.add(new TouchForMove(event.getPointerId(pointerIndex),event.getX(pointerIndex),event.getY(pointerIndex),xMax,yMax,xMin,yMin));
//                    isFirst2Touch = !(pointerCount == 2);
//                    break;
//
//                case MotionEvent.ACTION_UP: // прерывание последнего касания
//                    scaleTouches.remove(0);
//                    touch = null;
//                    inTouch = false;
//                    break;
//                case MotionEvent.ACTION_POINTER_UP: // прерывания касаний
//                    isAfter2MultiTouches = true;
//                    scaleTouches.remove(pointerIndex);
//                    break;
//
//                case MotionEvent.ACTION_MOVE: // движение
//                    if (pointerCount == 2){
//
//
//
//                    }
//                    if(pointerCount == 1 && !isAfter2MultiTouches){
//
//
//
//                    }
//                    break;
//            }
//
//            return true;
//        });




//        build.setOnClickListener(v -> RefreshDrawParametrs());

//        build.setOnClickListener(v -> {
//            ball.getTransform().setTranslation( new Vector2(0,0) );
//        });


        deltaTime = 1;

        /*------Create a world------*/
        world=new World<Body>();//Create the world
//        world.setGravity(new Vector2(0,-10));//The acceleration of gravity is set to 10m·s^(-2)
        //The following are two other ways of writing
        world.setGravity(0,-9.8);
        // world.setGravity(Vector2.create(10,-Math.PI/2));
        world.getSettings().setStepFrequency(0.01);//Set the step frequency, the interval between two calculations is 1 millisecond
//        world.setGravity(0,-1);


//        world.addStepListener(new StepListener<Body>() {
//            @Override
//            public void begin(TimeStep timeStep, PhysicsWorld<Body, ?> physicsWorld) {
//
//            }
//
//            @Override
//            public void updatePerformed(TimeStep timeStep, PhysicsWorld<Body, ?> physicsWorld) {
//
//            }
//
//            @Override
//            public void postSolve(TimeStep timeStep, PhysicsWorld<Body, ?> physicsWorld) {
//
//            }
//
//            @Override
//            public void end(TimeStep timeStep, PhysicsWorld<Body, ?> physicsWorld) {
//
//                System.out.println("fixCount =  " +graphs.getFixtureCount());
//                System.out.println("inDraw = " + Thread.currentThread().getId());
////                for (Fixture segment : graphs.getFixtures()){
////                    Segment s = (Segment) segment.getShape();
////                    System.out.println( "p1 = " + s.getPoint1() + " p2 = " + s.getPoint2());
////                }
//                RefreshDrawParametrs();
//                if (world.getBroadphaseDetector().isUpdated(graphs)){
//                }
////                world.removeBody(graphs);
////                world.addBody(graphs);
//
//            }
//        });
        /*------Create a world------*/

        /*------Create entity------*/


        graphs = new Body();
        graphs.setMass(MassType.INFINITE);
//        graphs.translate(0,0);



        for (int i = 0;i < 10;i++){

            ball=new Body();//Create a container to store the balls
            ball.addFixture(new Ellipse(1,1));//Create a small ball and add it to the container, the width and height are both 0.1m, that is, the radius is 0.05m
            ball.getTransform().setTranslation(0,10);//Set the Y coordinate of the ball to 10m
            ball.setMass(MassType.NORMAL);//Automatically calculate the mass of the ball
            ball.setLinearVelocity(new Vector2(0,0));

            world.addBody(ball);
            circles.add(ball);
        }


        Convex fixture;

        //bottom
        ground =new Body();//The same method to create the ground
        fixture = new Segment(new Vector2(xMin,yMin), new Vector2(xMax,yMin));
        ground.addFixture(fixture);//The ground is a rectangle, 100m wide and 0.1m high
//        ground.getTransform().setTranslation(xMin + (xMax - xMin)/2,yMin);//Move the ground down 0.05m to ensure that the Y coordinate of the upper edge of the ground is 0
        ground.setMass(MassType.INFINITE);//

        world.addBody(ground);

        sidesOfDisplay.add(new Sides(ground,fixture));
//        sidesOfDisplay.add(new Sides(ground,(float) (xMax - xMin),0.1f,sideOfDisplay.BOTTOM));

        //top
        ground =new Body();//The same method to create the ground
        fixture = new Segment(new Vector2(xMin,yMax), new Vector2(xMax,yMax));
        ground.addFixture(fixture);//The ground is a rectangle, 100m wide and 0.1m high
//        ground.getTransform().setTranslation(xMin + (xMax - xMin)/2,yMax);//Move the ground down 0.05m to ensure that the Y coordinate of the upper edge of the ground is 0
        ground.setMass(MassType.INFINITE);//S

        world.addBody(ground);

        sidesOfDisplay.add(new Sides(ground,fixture));
//        sidesOfDisplay.add(new Sides(ground,(float) (xMax - xMin),0.1f,sideOfDisplay.TOP));

        //start
        ground = new Body();//The same method to create the ground
        fixture = new Segment(new Vector2(xMin,yMin), new Vector2( xMin,yMax));
        ground.addFixture(fixture);//The ground is a rectangle, 100m wide and 0.1m high
//        ground.getTransform().setTranslation(xMin,yMin + (yMax - yMin)/2);//Move the ground down 0.05m to ensure that the Y coordinate of the upper edge of the ground is 0
        ground.setMass(MassType.INFINITE);//S

        world.addBody(ground);

        sidesOfDisplay.add(new Sides(ground,fixture));


        //end
        ground = new Body();//The same method to create the ground
        fixture = new Segment(new Vector2(xMax,yMin) , new Vector2(xMax,yMax));
        ground.addFixture(fixture);//The ground is a rectangle, 100m wide and 0.1m high
//        ground.getTransform().setTranslation(xMax,yMin + (yMax - yMin)/2);//Move the ground down 0.05m to ensure that the Y coordinate of the upper edge of the ground is 0
        ground.setMass(MassType.INFINITE);//S

        world.addBody(ground);

        sidesOfDisplay.add(new Sides(ground,fixture));

        JButton upBtn = new JButton();
        JButton downBtn = new JButton();
        JButton rightBtn = new JButton();
        JButton leftBtn = new JButton();

        JButton PlaceMainCircle = new JButton();
        PlaceMainCircle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isBallPlaceClick = !isBallPlaceClick;
            }
        });

        rightBox.add(PlaceMainCircle);

        Body ewqeq =new Body();
        ewqeq.addFixture(new Link(new Vector2(0,0), new Vector2(5,0)));
        world.addBody(ewqeq);
        ewqeq =new Body();
        ewqeq.addFixture(new Link(new Vector2(0,0), new Vector2(5,0)));
        world.addBody(ewqeq);



//        float velocityAdd = 1f;
//
//        upBtn.setOnTouchListener((v, event) -> {
//
//            int action = event.getAction();
//
//            if (action == MotionEvent.ACTION_DOWN){
//
//                for (Body circle : circles){
//                    Vector2 velocity = circle.getLinearVelocity();
//                    circle.setLinearVelocity(velocity.x,velocity.y + velocityAdd);
//                }
//
//            }else if
//            (action == MotionEvent.ACTION_UP){
//            }
//            return false;
//        });
//        downBtn.setOnTouchListener((v, event) -> {
//
//            int action = event.getAction();
//
//            if (action == MotionEvent.ACTION_DOWN){
//
//                for (Body circle : circles){
//                    Vector2 velocity = circle.getLinearVelocity();
//                    circle.setLinearVelocity(velocity.x,velocity.y - velocityAdd);
//                }
//
//            }else if
//            (action == MotionEvent.ACTION_UP){
//
//            }
//            return false;
//        });
//        rightBtn.setOnTouchListener((v, event) -> {
//
//            int action = event.getAction();
//
//            if (action == MotionEvent.ACTION_DOWN){
//
//                for (Body circle : circles){
//                    Vector2 velocity = circle.getLinearVelocity();
//                    circle.setLinearVelocity(velocity.x + velocityAdd,velocity.y);
//                }
//
//            }else if
//            (action == MotionEvent.ACTION_UP){
//
//            }
//            return false;
//        });
//        leftBtn.setOnTouchListener((v, event) -> {
//
//            int action = event.getAction();
//
//            if (action == MotionEvent.ACTION_DOWN){
//
//                for (Body circle : circles){
//                    Vector2 velocity = circle.getLinearVelocity();
//                    circle.setLinearVelocity(velocity.x - velocityAdd,velocity.y);
//                }
//
//            }else if
//            (action == MotionEvent.ACTION_UP){
//
//            }
//            return false;
//        });

//                Thread t = new Thread() {
//
//            @Override
//            public void run() {
//                while (!isInterrupted()) {
//                    try {
//                        Thread.sleep(1);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    RefreshDrawParametrs();
//                }
//            }
//        };
//
//        t.start();

//        SeekBar RestitutionSeekBar = findViewById(R.id.RestitutionSeekBar);
//        SeekBar FrictionSeekBar = findViewById(R.id.FrictionSeekBar);
//        FrictionSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//
//                for (Body ball : circles){
//                    ball.getFixture(0).setFriction(FrictionSeekBar.getProgress());
//                }
//
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
//
//            }
//        });
//
//        RestitutionSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//
//                for (Body ball : circles){
//                    ball.getFixture(0).setRestitution(RestitutionSeekBar.getProgress()/10.0);
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
//
//        ImageButton cameraFollowsPlayer = findViewById(R.id.cameraFollowsPlayer);
//        cameraFollowsPlayer.setOnClickListener(v -> {
//
//            ball.getFixture(0).setDensity(ball.getFixture(0).getDensity() + 10);
//        });



        /*------Create entity------*/



//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);




        frame.pack();
        frame.setSize(1280,900);
        frame.repaint();
    }



    //Один кадр
    void RefreshDrawParametrs(){


//        System.out.println("Hello before world");

        Body eqfkqo = new Body();
        eqfkqo.setMass(MassType.INFINITE);

//        graphs.removeAllFixtures();
        int counter = 0;
        int counter2 = 0;
        is2TouchNow = !is2TouchNow;
        ArrayList<Link> newLinks = links;

        for (Link link : newLinks){
//            System.out.println(" p1 = " + link.getPoint1() + " p2 = " + link.getPoint2());
            eqfkqo.addFixture(link);
        }

        world.removeBody(world.getBodyCount() - 2);
        world.addBody(eqfkqo);
//
//        eqfkqo.removeAllFixtures();
//        eqfkqo.addFixture((new Link(new Vector2(0,0),new Vector2(5,5))));
//        graphs.getTransform().setTranslation(0,0);
        System.out.println("fixCount =  " + eqfkqo.getFixtureCount());
        System.out.println("bodyCount =  " + world.getBodyCount());
//        world.removeBody(eqfkqo);

//        eqfkqo;
        world.step(1);


//        System.out.println("Hello after world");

//        Log.i("FixtureCount","" + graphs.getFixtureCount());


//        LinearLayout lin = (LinearLayout)findViewById(R.id.formula_inputs_container_container);
        allCalculetblePoints.clear();
        funcsWeDo.clear();
        for (int i = 0;i < 1;i++) {
//            LinearLayout linH = (LinearLayout)lin.getChildAt(i);
//            EditText FormulaInput = (EditText)linH.getChild   At(0);
            try {

//                Log.i("GetText", ""  + FormulaInput.getText().toString() + " size = " + FormulaInput.getText().toString().length());
//                System.out.println("Hello before calc");
                FormulSistem.Calculate(FormulaInput.getText(),0,1,0.2);
//                System.out.println("Hello after calc");

                funcsWeDo.add(FormulaInput.getText());

                allCalculetblePoints.put(FormulaInput.getText(),null);

//                drawView.paint();
//                System.out.println("Hello before draw");


                drawView.repaint();
//                System.out.println("Hello after draw");


            }catch (Exception e){

                System.out.println("ERROROFSEX: " + e.getMessage());
            }
        }




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

    protected FormulSistem.Pair<Double,Double> DrobAndCelayaChast(double d){

//        System.out.println("DrobChast" + " = " + d);

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
        return new FormulSistem.Pair<Double,Double>(Double.parseDouble(cel.substring(0,cel.length() - 1)),Double.parseDouble(minus +"0." + drob));
    }

//    protected String GetTheEditTextText(EditText view){
//        return view.getText().toString();
//    }

//    @Override
//    public boolean onTouch(View view, MotionEvent event) {
//        // событие
//        return false;
//    }

    protected class Path{
        ArrayList<Vector2> path;

        Path(){
            path = new ArrayList<>();
        }

        void moveTo(double x,double y){
            path.add(null);
        }

        void lineTo(double x,double y){
            path.add(new Vector2(x,y));
        }
    }

    ArrayList<Path2D.Double> FuncPointsCalculate(double xMult,double yMult,double xCentre, double yCentre,double step){

        System.out.println("thread in calc = " + Thread.currentThread().getId());

        double fromx = xMin;
        double tox = xMax;
        try {

            for (int i = 0;i < funcsWeDo.size();i++){
                String func = funcsWeDo.get(i);

                ArrayList<FormulSistem.Pair<Double,Double>> calculetblePoints = allCalculetblePoints.get(func);

//                Log.i("Nigger","" + func);

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
                calculetblePoints = FormulSistem.Calculate(func, xMin, xMax, step);

                allCalculetblePoints.remove(func);
                allCalculetblePoints.put(func,calculetblePoints);
                System.out.println("Calculate " + " func = " + func + " xMin = " + xMin
                        + " xMax = " + xMax + " step = " + step
                        + " calculetblePoints.size() = " +  allCalculetblePoints.get(func).size());

            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }


//        Log.i("allCalculetblePoints","" + allCalculetblePoints.size() + "\nКЛЮЧ = \t" + allCalculetblePoints.keySet().toString());
//            for (ArrayList<FormulSistem.Pair<Double,Double>> p : allCalculetblePoints.values()){
//                Log.i("calcPoints","" + p.size());
//            }

        ArrayList<Path2D.Double> paths = new ArrayList<>();


        int qweoipcj = 0;
        links.clear();

        for (int pathCount = 0;pathCount < funcsWeDo.size(); pathCount++){
            Path2D.Double path = new Path2D.Double();
            ArrayList<FormulSistem.Pair<Double,Double>> calculetblePoints = null;

            try {
                calculetblePoints = allCalculetblePoints.get(funcsWeDo.get(pathCount));

            }catch (Exception e){
                e.printStackTrace();
                System.out.println("errorFunc FWDsize = " + funcsWeDo.size() + " pathC = "+ pathCount);
                continue;
            }

            if (calculetblePoints != null){

                PointF prevPoint = null;
                PointF curPoint = null;
                int counter = 0;
                ArrayList<Vector2> polygon = new ArrayList<>();

                for (int i = 0; i < calculetblePoints.size();i++){


                    counter++;

                    if (calculetblePoints.get(i).element2.isNaN()){qweoipcj = i + 1; continue;}
                    if (calculetblePoints.get(i).element2.isInfinite()){
//                        Log.i("SEX","TRAAAAAAAAAAH");
                        float asscum;
                        if (calculetblePoints.get(i).element2 > 0){
                            asscum = -10000.0f;
                        }else{
                            asscum = 10000.0f;
                        }
                        PointF point = ToDisplayCoords(calculetblePoints.get(i).element1,asscum);
                        path.moveTo(point.x,point.y);

                        prevPoint = new PointF( (float) (double) calculetblePoints.get(i).element1,asscum);

                        qweoipcj = i;
                        continue;
                    }

                    curPoint = new PointF((float)(double)calculetblePoints.get(i).element1,(float)(double)calculetblePoints.get(i).element2);
                    PointF point = ToDisplayCoords(curPoint.x,curPoint.y);

                    if (i <= qweoipcj){path.moveTo(point.x,point.y);}
                    path.lineTo(point.x,point.y);



//                    Доделать надо, чтобы могли быть пробелы
                    if (prevPoint != null && counter >= approximationAccuracy){
                        counter = 0;
//                        Convex shape = new Segment();

//                        graphs.addFixture();


                        links.add( new Link(new Vector2(prevPoint.x,prevPoint.y),new Vector2(curPoint.x,curPoint.y)));
//                        System.out.println("linkP " + "prevX = " + prevPoint.x + " prevY = " + prevPoint.y + " x = " + curPoint.x + " y = " + curPoint.y );

                        prevPoint = curPoint;
                    }
                    if (i < 1){prevPoint = curPoint;}

                }

//                System.out.println("pathCOunt = " + counter + " calc.size = " + calculetblePoints.size());
            }

            paths.add(path);
        }
        System.out.println(" calculetblePoints.size() = " +  allCalculetblePoints.get(funcsWeDo.get(0)).size()
                + " Thread.activeCount() = " + Thread.activeCount()
        );
        return paths;
    }


    PointF ToDisplayCoords(double x ,double y){
        PointF result = new PointF();

        result.x = (float) (x*xMult + xCentre);
        result.y = (float) (-y*yMult + yCentre);
        return result;
    }

    class MyPaint{
        Color color;

        int red;
        int green;
        int blue;
        int alpha;

        MyPaint(Color color){
            this.color = color;
        }

        void setColor(Color color){
           red = color.getRed();
           green = color.getGreen();
           blue = color.getBlue();
        }

        void setAlpha(int alpha){
            this.alpha = alpha;
        }

        private void Refresh(){
            this.color = new Color(red,green,blue,alpha);
        }
    }


    class DrawView extends JPanel{


        MyPaint p;

        int height;
        int width;

        Graphics2D g;

        public DrawView(int height,int width) {
            this.height = height;
            this.width = width;

            p = new MyPaint(new Color(133,32,32));
        }


        @Override
        public void paint(Graphics g) {
            super.paint(g);


            g.setPaintMode();

            // заливка канвы цветом.
            Graphics2D canvas = (Graphics2D) g;
            canvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            canvas.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_PURE);
            canvas.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);

            g.drawRect(0,0,5,5);

//                    .drawARGB(80, 255, 255, 255);
//            canvas.setBitmap();

//            System.out.println("Fuck");


            p.setColor(Color.RED);
            // толщина линии = 10
//            canvas.setStroke();

//            p.setAntiAlias(true);

            //Высота и ширина экрана
//            width = getWidth();
//            height = getHeight();

//            System.out.println("funcsweDo  " + funcsWeDo.toString() + " " + funcsWeDo.size());

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
//            System.out.println("Cy" + Cy);

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


            canvas.setPaint(p.color);

            canvas.draw(new Line2D.Double(xCentre,height,(int)xCentre,0));
            canvas.draw(new Line2D.Double(0,yCentre,width,yCentre));



            //Деления
            //Строиться для каждой полуоси отдельно(4 цикла) выводится текст и строятся деления
//            p.setStrokeWidth(3);
//            p.setTextSize(30);
//            p.setFakeBoldText(false);
//            p.setTextAlign(Paint.Align.CENTER);

            canvas.setPaint(Color.BLACK);

            //Колво знаков после запятой выводится
            int decimalPlacesX = DecimalPlaces(Cx);
            int decimalPlacesY = DecimalPlaces(Cy);

            System.out.println("Decmalplases" + decimalPlacesX + " " + decimalPlacesY);
//            System.out.println("VspomogatZnach" + "[" +Cex + ',' + Cey + ']' + '+' + '[' + xCentre + ',' + yCentre + ']' );
//            System.out.println("assHole" + "[xMax " + xMax + "|xMin " + xMin + "|yMax " + yMax + "|yMin " + yMin + ']');


//          Создание коорд сетки
            double count;
//            p.setStyle(Paint.Style.FILL);
            count = DrobAndCelayaChast(xMin/Cx).element1*Cx - Cx;
            double extra = (1 - DrobAndCelayaChast(xMin/Cx).element2)*Cex;
            for (double x = extra - 2*Cex;x < width;x += Cex){


                p.setAlpha(100);
                canvas.draw(new Line2D.Double(x,0,x,height));
                p.setAlpha(50);
                for (double i = x;i < x + Cex; i += Cex/5){
                    canvas.draw(new Line2D.Double(i,0,i,height));
                }
                p.setAlpha(255);

                // 10^n отображение при масштабировании.
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
                    canvas.drawString(String.format("%." + decimalPlacesX +"f",count), (float)(x), (float) (height*0.04));
                }else if (yCentre > height){
                    canvas.drawString(String.format("%." + decimalPlacesX +"f",count), (float)(x), (float) (height*0.98));
                }else{
                    canvas.drawString(String.format("%." + decimalPlacesX +"f",count), (float)(x), (float) yCentre);
                }


                count += Cx;
            }
            count = DrobAndCelayaChast(yMin/Cy).element1*Cy;
            extra = (1 - DrobAndCelayaChast(yMin/Cy).element2)*Cey;
            for (double y = height + Cey - extra;y > 0 - Cey;y -= Cey){

                p.setAlpha(100);
                canvas.draw(new Line2D.Double(0,y,width,y));
                p.setAlpha(50);
                for (double i = y;i < y + Cey; i += Cey/5){
                    canvas.draw( new Line2D.Double(0,i,width,i));
                }
                p.setAlpha(255);

                if (y == yCentre){count += Cy;continue;}

                if (xCentre < 0){
                    canvas.drawString(String.format("%." + decimalPlacesY +"f",count), (float)(width*(0.02 + Math.pow((decimalPlacesY),1.0001)/100)), (float) (y));
                }else if (xCentre > width){
                    canvas.drawString(String.format("%." + decimalPlacesY +"f",count), (float)(width*(0.98 - Math.pow((decimalPlacesY),1.0001)/100)), (float) (y));
                }else {
                    canvas.drawString(String.format("%." + decimalPlacesY + "f", count), (float) xCentre, (float) (y));
                }
                count += Cy;
            }
//            p.setStyle(Paint.Style.STROKE);

//            p.setStrokeWidth(5);

            p.setColor(Color.RED);

            //Строиться с точностью до 0.005*ЦенаДеления
            step = 0.005*Cx;
            System.out.println("thread in actually draw = " + Thread.currentThread().getId());

            ArrayList<Path2D.Double> paths = FuncPointsCalculate(xMult,yMult,xCentre,yCentre,step);


//            Log.i("CenaDel","[Cx " + Cx + "|Cy " + Cy + "|Cex " + Cex + "|Cey " + Cey + ']');

            //Построение графика заданной функции
//            p.setStrokeWidth(3);
//            p.setStyle(Paint.Style.STROKE);

            int indexChisto = 0;
//            Stroke stroke = new BasicStroke(2,BasicStroke.CAP_SQUARE,BasicStroke.JOIN_BEVEL);
//            canvas.setStroke(stroke);
            System.out.println("WeDrawNow");
            for (Path2D.Double path : paths){
                System.out.println( "Thread.activeCount() = " + Thread.activeCount() +
                        "pathLengt = " + paths.size() +
                        " funcsWeDo.get(0) = " + funcsWeDo.get(0) +
                        " allCalculetblePoints.size() = " + allCalculetblePoints.size()
                        + "  allCalculetblePoints.keySet() = " +  allCalculetblePoints.keySet()

                );

                System.out.println("CaclSizeWhenDraw  = " + allCalculetblePoints.get( funcsWeDo.get(0)).size());

                //                p.setColor(colors.get(indexChisto++).element1);
//                Log.i("Nigga","" +colors.get(indexChisto - 1).first + "Jopa = " + colors.get(indexChisto - 1).second.getDrawingCacheBackgroundColor());

//                Vector2 prevpoint = null;
                canvas.setPaint(Color.BLUE);
                canvas.draw(path);

////                canvas.setStroke();
//                boolean isFirst = true;
//
//                Path2D.Double path1 = new Path2D.Double();
//                for (Vector2 point : path.path){
//
//
//
//
////                    if (path.path.indexOf(point) == 0){prevpoint = point; continue;}
////                    System.out.println("PathDraw, " + " prevpoint.x = " + prevpoint.x
////                            + " prevpoint.y = " + prevpoint.y + " point.x = " + point.x + " point.y = " + point.y );
//
//                    if (point != null){
//
//                        if (isFirst){
//                            path1.moveTo(point.x,point.y);
//                        }
//                        isFirst = false;
//
//                        path1.lineTo(point.x,point.y);
//                    }
//
//                    prevpoint = point;
//                }

            }
            p.setColor(Color.RED);
//            p.setStyle(Paint.Style.STROKE);


            g.setColor(Color.RED);

            for (Sides body : sidesOfDisplay){

                body.Refresh();

//                Log.i("PosRect", "xStart = " + body.posStart.x + " yStart = " + body.posStart.y + "xEnd = " + body.posEnd.x + " yEnd = " + body.posEnd.y);


                PointF lineS = ToDisplayCoords(body.line.getPoint1().x,body.line.getPoint1().y);
                PointF lineE = ToDisplayCoords(body.line.getPoint2().x,body.line.getPoint2().y);


                canvas.draw( new Line2D.Double(lineS.x,lineS.y,lineE.x,lineE.y));

            }

            canvas.setPaint(Color.BLUE);
//            Ellipse2D ellipse2D = new Ellipse2D.Double(0,0,500,500);
////            System.out.println("ElipsePos " + " x = " + ellipse2D.getX() + " y = " + ellipse2D.getY() );
//            canvas.draw(ellipse2D);

            for (Body body : circles){


                Ellipse ellipse = (Ellipse) body.getFixture(0).getShape();

                PointF circleStart = ToDisplayCoords(body.getTransform().getTranslationX() - ellipse.getHalfWidth(),body.getTransform().getTranslationY() + ellipse.getHalfHeight());

//                PointF circleEnd = ToDisplayCoords(body.getTransform().getTranslationX() + ellipse.getHalfWidth(),body.getTransform().getTranslationY() + ellipse.getHalfHeight());

                Ellipse2D ell = new Ellipse2D.Double(circleStart.x,circleStart.y,ellipse.getWidth()*xMult,ellipse.getHeight()*yMult);
                canvas.draw(ell);


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
            this.posStart = new PointF((float) this.body.getTransform().getTranslation().x - this.width/2,(float) this.body.getTransform().getTranslation().y - this.height/2);
            this.posEnd = new PointF(this.posStart.x + this.width, this.posStart.y + this.height);
        }

    }


    enum sideOfDisplay{
        START,
        END,
        TOP,
        BOTTOM
    }
    class Sides{
        Body body;

        Segment line;

        Sides(Body body,Convex segment){
            this.body = body;
            this.line = (Segment) segment;
        }

        void Refresh() {

//            this.body.removeAllFixtures();
//
//            if (side == sideOfDisplay.START){
//
//                this.height = (float) ((yMax - yMin) - (xMax - xMin)/4);
//
//                this.body.addFixture(new Rectangle(0.1,this.height));
//
////                this.body.translate(new Vector2( xMin - this.posStart.x,yMin - this.posStart.y ));
//                this.body.getTransform().setTranslation(xMin + (xMax - xMin)/4,yMin + (yMax - yMin)/4);
//
//            }else if (side == sideOfDisplay.END){
//
//                this.height = (float) ((yMax - yMin) - (xMax - xMin)/4);
//
//                this.body.addFixture(new Rectangle(0.1,this.height));
//
////                this.body.translate(new Vector2( xMax - this.posEnd.x,yMin - this.posStart.y ));
//                this.body.getTransform().setTranslation(xMax - (xMax - xMin)/4,yMin + (yMax - yMin)/4);
//
//            }else if (side == sideOfDisplay.TOP){
//
//                this.width = (float) ((xMax - xMin) - (xMax - xMin)/4);
//
//                this.body.addFixture(new Rectangle(this.width,0.1));
//
////                this.body.translate(new Vector2( xMin - this.posStart.x,yMax - this.posEnd.y ));
//                this.body.getTransform().setTranslation(xMin + (xMax - xMin)/4,yMax - (yMax - yMin)/4);
//
//            }else if (side == sideOfDisplay.BOTTOM){
//
//                this.width = (float) ((xMax - xMin) - (xMax - xMin)/4);
//
//                this.body.addFixture(new Rectangle(this.width,0.1));
//
////                this.body.translate(new Vector2( xMin - this.posStart.x,yMin - this.posStart.y ));
//                this.body.getTransform().setTranslation(xMin + (xMax - xMin)/4,yMin + (yMax - yMin)/4);
//
//            }


        }
    }


    boolean IsInBetween(float x,float startx,float endx){
        return (x > startx && x < endx) || (x < startx && x > endx);
    }


    public class PointF{
        float x;
        float y;

        PointF(float x, float y){
            this.x = x;
            this.y = y;
        }

        public PointF() {
            this.x = 0;
            this.y = 0;
        }
    }

    public static void main(String[] args) {
        MainClass good = new MainClass();

        long totalTime = 0;

        good.onCreate();
        while (true){

            try {

                long startTime = System.nanoTime();

                long time;

//                System.out.println(time);

                good.RefreshDrawParametrs();


                long endTime   = System.nanoTime();
                totalTime = endTime - startTime;

                if (totalTime/1000000 >= 10){time = 0;}
                else {time = 10 - totalTime/1000000;}

                if (totalTime/1000000 >= 10){
                    System.out.println("TIME = " + totalTime/1000000 + " : " + totalTime/1000 + " : " + totalTime);
                }
                System.out.println("inMain = " + Thread.currentThread().getId());

                Thread.sleep(time);

            }catch (Exception e){
                e.printStackTrace();
            }

        }

//        JFrame frame = new JFrame();
//        frame.setSize(500,500);
    }
}
