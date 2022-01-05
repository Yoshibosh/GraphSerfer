package com.example.coordinatesystem;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FormulSistem{

    static String function;
    static char CurSimbol;
    static int CurSimbolNum = 0; 
    static String CurToken = "";

    static String CurOperation = "";
    static String prevOperation = "";

    static char binops[] = {'+','-','*','/','^','(',')'};
    static char simbols[] ={'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
    static char numbers[] ={'0','1','2','3','4','5','6','7','8','9','e','.',',',};

    static String levelOp1[] = new String[]{"^"};
    static String levelOp2[] = new String[]{"*","/"};
    static String levelOp3[] = new String[]{"+","-"};

    static String lastfunc = "";
    static NamedFunction lastFunc = null;

    public interface Function{
    }
    class NamedFunction implements Function{
        String name;
        Function arg;

        NamedFunction(String name, Function arg){
            this.name = name;
            this.arg = arg;
        }

        @Override
        public String toString() {
            if (arg == null){
                return name;
            }else if(name.equals("f")){
                return arg.toString();
            }
            return name + arg.toString();
            
        }
    }
    class Constant implements Function{
        double value;
        Constant(double value){
            this.value = value;
        }
        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }
    public class Binop2 implements Function{
        Function f1;
        Function f2;
        String op;
        Binop2(Function leftoperand, Function rightoperand, String op){
            this.f1 = leftoperand;
            this.f2 = rightoperand;
            this.op = op;
        }

        @Override
        public String toString() {
            return "[" + f1 + " " + op +" "+ f2 + "]";
        }
    }
    
    static ArrayList<Pair<Function,int[]>> functions = new ArrayList<Pair<Function,int[]>>();
    public static Pair<Function,int[]> TransformToFunc2(Pair<String,String>[] func,int start){



        Pair<Function,int[]> leftOperand = null;
        Pair<Function,int[]> rightOperand = null;
        Pair<String,String> leftOperandS = null;
        Pair<String,String> rightOperandS = null;
        boolean leftOperIsString = true;
        boolean rightOperIsString = true;
        FormulSistem formulSistem = new FormulSistem();

        Pair<String,String> tok;
        int level = 0;
        boolean isAloneEl = false;

        
        for (int curTokIndex = start;curTokIndex < func.length;curTokIndex += 1){

            for (Pair<Function,int[]> p : functions){
                for (int border = p.element2[0];border <= p.element2[1];border++){
                    if (border == curTokIndex){curTokIndex = p.element2[1] + 1;}
                }
            }
            tok = func[curTokIndex];

            if (tok.element2 == "op"){
                if (level == 0){
                    switch (tok.element1){
                        case "(":
                            Pair<String,String> maybeFunc = func[curTokIndex - 1];
                            Pair<Function,int[]> curFunc = TransformToFunc2(func, curTokIndex+1);

                            if (maybeFunc.element2 == "simbol"){
                                functions.add(0,new Pair<Function,int[]>(formulSistem.new NamedFunction(maybeFunc.element1,curFunc.element1),new int[]{curTokIndex - 1,curFunc.element2[1]}));
                                functions.remove(curFunc);
                                if (functions.get(0).element2[0] == 0 && functions.get(0).element2[1] == func.length - 1){
                                    Pair<Function,int[]> result = functions.get(0);
                                    functions.clear();
                                    return result;
                                }
                            }else{
                                System.out.print("Нет названия функции перед раскрывающимися скобками");
                                return null;                             
                            }
                            break;
                        case ")":
                            if (func[curTokIndex - 2].element1.equals("(")){
                                isAloneEl = true;
                            }

                            level = 1;
                            curTokIndex = start - 1;
                            break;
                        default:
                            break;
                    }
                }else if (true){

                    if (isAloneEl){
                        Pair<String,String> alone = func[curTokIndex - 1];
                        isAloneEl = false;
                        switch (alone.element2){
                            case "number":
                                functions.add(0,new Pair<Function,int[]>(formulSistem.new Constant(Double.valueOf(alone.element1)),new int[]{curTokIndex - 2,curTokIndex}));
                                return functions.get(0);
                            case "simbol":
                                functions.add(0,new Pair<Function,int[]>(formulSistem.new NamedFunction(alone.element1, null),new int[]{curTokIndex - 2,curTokIndex}));
                                return functions.get(0);
                        }
                    }


                    if (tok.element1.equals(")")){
                        level++;
                        curTokIndex = start - 1;
                        if (level > 3){
                            functions.get(0).element2[0] -= 1;
                            functions.get(0).element2[1] += 1;
                            return functions.get(0);
                        }
                        continue;
                    }

                    if (level == 1){if (!haveElements(levelOp1, tok.element1)){ continue;}}
                    if (level == 2){if (!haveElements(levelOp2, tok.element1)){ continue;}}
                    if (level == 3){if (!haveElements(levelOp3, tok.element1)){ continue;}}
                    
                    
                    leftOperand = null;
                    rightOperand = null;
                    leftOperandS = func[curTokIndex-1];
                    rightOperandS = func[curTokIndex+1];
                    leftOperIsString = true;
                    rightOperIsString = true;

                    for (int i = 0;i < functions.size();i++ ){
                        Pair<Function,int[]> p = functions.get(i);
                        if (p.element2[1] == curTokIndex - 1){
                            leftOperand = p;
                            leftOperIsString = false;
                            functions.remove(p);
                            i = -1;
                        }
                        else if (p.element2[0] == curTokIndex + 1){
                            rightOperand = p;
                            rightOperIsString = false;
                            functions.remove(p);
                            i = -1;
                        }
                        if(functions.size() == 0){break;}
                    }

                    if (leftOperIsString){
                        switch (leftOperandS.element2){
                            case "number":
                                leftOperand = new Pair<Function,int[]>(formulSistem.new Constant(Double.valueOf(leftOperandS.element1)),new int[]{curTokIndex-1,curTokIndex-1});
                                break;
                            case "simbol":
                                leftOperand = new Pair<Function,int[]>(formulSistem.new NamedFunction(leftOperandS.element1, null),new int[]{curTokIndex-1,curTokIndex-1});
                                break;
                        }
                    }
                    if (rightOperIsString){
                        switch (rightOperandS.element2){
                            case "number":
                                rightOperand = new Pair<Function,int[]>(formulSistem.new Constant(Double.valueOf(rightOperandS.element1)),new int[]{curTokIndex+1,curTokIndex+1});
                                break;
                            case "simbol":
                                rightOperand = new Pair<Function,int[]>(formulSistem.new NamedFunction(rightOperandS.element1, null),new int[]{curTokIndex+1,curTokIndex+1});
                                break;
                        }
                    }

                    if (leftOperand.element1.getClass().equals(Constant.class) && rightOperand.element1.getClass().equals(Constant.class)){
                        Constant left = (Constant)leftOperand.element1;
                        Constant right = (Constant)rightOperand.element1;
                        functions.add(0,new Pair<Function,int[]>(formulSistem.new Constant(left.value + right.value),new int[]{leftOperand.element2[0],rightOperand.element2[1]}));
                    }else{
                        functions.add(0,new Pair<Function,int[]>(formulSistem.new Binop2(leftOperand.element1, rightOperand.element1, tok.element1),new int[]{leftOperand.element2[0],rightOperand.element2[1]}));
                    }


                }
            }

        }

        return functions.get(0);
    }


    static void nextChar(){
        CurSimbol = function.charAt(CurSimbolNum++);
    }

    
    static Boolean haveElements(char[] mass, char element){
        for (char i : mass){
            if (i == element) return true;
        }
        return false;
    }
    static Boolean haveElements(String[] mass, String element){
        for (String i : mass){
            if (i.equals(element)) return true;
        }
        return false;
    }


    public static ArrayList<Pair<String,String>> Parse(String func){

        ArrayList<Pair<String,String>> incomingFunction = new ArrayList<>();

        CurSimbolNum = 0;
        function = " " + func + "/";
        while(CurSimbolNum < function.length()){

            nextChar();
            if (CurSimbol == ' '){continue;}

            
            

            if (haveElements(simbols,CurSimbol)){
                CurOperation = "simbol";
            }
            else if (haveElements(numbers,CurSimbol)){
                CurOperation = "number";
            }
            else if (haveElements(binops,CurSimbol)){
                CurOperation = "op";
                if (prevOperation == "op") {CurOperation = "opp";}
            }
            if (CurOperation != prevOperation){
                if (prevOperation == "opp") {incomingFunction.add(new Pair<String,String>(CurToken,"op"));}
                else incomingFunction.add(new Pair<String,String>(CurToken,prevOperation));
                CurToken = "";
            }
            CurToken += CurSimbol;
            prevOperation = CurOperation;
        }incomingFunction.remove(0);
        return incomingFunction;
    }

    
    static class Pair<T1,T2>{
        T1 element1;
        T2 element2;
        Pair(T1 el1,T2 el2){
            this.element1 = el1;
            this.element2 = el2;
        };

        @Override
        public String toString() {
            return '[' + String.valueOf(element1) + ", " + String.valueOf(element2) + "]\n";
        }
    }

    static String[] supportedFuncs = {"cos","sin","ln","lg","f","abs"};
    public static ArrayList<Pair<Double, Double>> Calculate(String func,double argFrom,double argTo,double step) throws Exception{

        ArrayList<Pair<Double, Double>> result = new ArrayList<Pair<Double,Double>>();
        NamedFunction arg;

        if (func.equals(lastfunc)){
            arg = lastFunc;
        }else{
            arg = (NamedFunction)TransformToFunc2(ToWorkbleVid(func),0).element1;;
            lastFunc = arg;
            lastfunc = func;
        }




        
        for (double x = argFrom;x <= argTo; x += step){
            if (x < 0 && x + step > 0){x = 0;}
            result.add(new Pair<Double,Double>(x, Unfolding(arg, x)));
        }

        return result;
    }

    public static double Calculate(String func) throws Exception{
        Function convertFunc = TransformToFunc2(ToWorkbleVid(func),0).element1;

        NamedFunction arg = (NamedFunction)convertFunc;
        
        return Unfolding(arg, 0);
    }

    static double Unfolding(Function func,double x) throws Exception{

        if (func.getClass().equals(NamedFunction.class)){
            NamedFunction f = (NamedFunction)func;
            if (f.arg == null){
                return x;
            }else{
                double arg = Unfolding(f.arg, x);

                if (!haveElements(supportedFuncs, f.name)){throw new Exception(f.name + " <- This func not supported");}

                switch (f.name){
                    case "lg":
                        return Math.log10(arg);
                    case "ln":
                        if (arg == 0){return Double.NEGATIVE_INFINITY;}
                        return Math.log(arg);
                    case "cos":
                        return Math.cos(arg);
                    case "sin":
                        return Math.sin(arg);
                    case "f":
                        return arg;
                    case "abs":
                        return Math.abs(arg);
                    case "arccos":
                        return Math.acos(arg);
                    case "arcsin":
                        return Math.asin(arg);
                    case "arctg":
                        return Math.atan(arg);
                    case "arcctg":
                        return 1/Math.atan(arg);
                    default:
                        throw new Exception(f.name + "This func not supported");
                }
            }
        }else if (func.getClass().equals(Binop2.class)){
            double c1;
            double c2;
            Binop2 argNew = (Binop2)func;
            c1 = Unfolding(argNew.f1, x);
            c2 = Unfolding(argNew.f2, x);

            switch (argNew.op){
                case "+":
                    return c1 + c2;
                case "-":
                    return c1 - c2;
                case "*":
                    return c1 * c2;
                case "/":
                    return c1 / c2;
                case "^":
                    return Math.pow(c1, c2);
                default:
                    throw new Exception("NotSupportedOperator");
            }
        }else if (func.getClass().equals(Constant.class)){
            Constant C = (Constant)func;
            return C.value;
        }
        return 0;
    }

    static String ErorCheck(String func){
        


        return func;
    }

    static Pair<String,String>[] ToWorkbleVid(String func) throws Exception{

        func = "f("+ func + ")";
        ArrayList<Pair<String,String>> parsedFunc = Parse(func);

        Pair<String,String> curentToken;
        Pair<String,String> prevToken;

        for (int i = 1;i < parsedFunc.size();i++){
            int k = 0;
            if (i >= 1){k = 1;}
            curentToken = parsedFunc.get(i);
            prevToken = parsedFunc.get(i-k);
            if (curentToken.element1.equals("(") && prevToken.element2.equals("op")){
                parsedFunc.add(i, new Pair<String,String>("f","simbol"));
            }
            switch (curentToken.element2){
                case "number":
                    int dothCounter = 0;
                    for (int j = 0;j < curentToken.element1.length();j++){

                        char curChar = curentToken.element1.charAt(j);

                        if (curChar == '.'){dothCounter++;}
                        if (dothCounter > 1){throw new Exception("В числе больше одной точки.");}
                        if (!haveElements(numbers, curChar)){throw new Exception("В числе есть недопустимые символы");}

                    }
                    break;
                case "op":
                    if (curentToken.element1.equals("-") && prevToken.element1.equals("(")){
                        parsedFunc.add(i, new Pair<String,String>("0","number"));
                    }
                    for (int j = 0;j < curentToken.element1.length();j++){

                        char curChar = curentToken.element1.charAt(j);

                        if (!haveElements(binops,curChar)){throw new Exception("В операторе содержаться недопустимые символы");}
                    }
                    break;
                case "simbol":
                    switch (curentToken.element1) {
                        case "pi":
                        parsedFunc.remove(i);
                        parsedFunc.add(i, new Pair<String,String>(String.valueOf(Math.PI),"number"));
                        break;

                        case "e":
                        parsedFunc.remove(i);
                        parsedFunc.add(i, new Pair<String,String>(String.valueOf(Math.exp(1)),"number"));
                        break;
                    
                        default:
                            break;
                    }    
                    break;
            }
        }


        Pair<String,String> funcArr[] = (Pair<String,String>[])  Array.newInstance((new Pair("2","23").getClass()), parsedFunc.size());
        
        for (int i = 0;i < parsedFunc.size();i++){
            funcArr[i] = parsedFunc.get(i);
        }
        return funcArr;
    }
   
    
   
    public static void main(String[] args) throws Exception{
        String func = "(e^(-x*0.001)*cos(1000*x))";
        
        ArrayList<Pair<Double,Double>> results = Calculate(func, -10, 10, 1);
        System.out.println(results);

        func = "(e^(x))";
        
        results = Calculate(func, -10, 10, 1);
        System.out.println(results);

        func = "sin(pi)";
        System.out.println(Calculate(func));
        System.out.println(Calculate("sin(2*pi)"));
        System.out.println(Calculate("sin(pi/2)"));
        System.out.println(Calculate("sin(pi/6)"));
        System.out.println((Math.sin(Math.PI)));
    }

}