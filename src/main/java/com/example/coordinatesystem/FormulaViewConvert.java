package com.example.coordinatesystem;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;


public class FormulaViewConvert {
    static String[] needToContainIn = new String[]{"ln","lg","cos","sin","acos","asin","tg","ctg","atg","actg","/","sqrt","^"};

    static HashMap<String,Object> needToContainInResourses = new HashMap<>();

    public class FormulaContainerForOut{
        ArrayList<FormulaContainerForOut> childContainers = new ArrayList<>();
        Object src;
        String name;

        FormulaContainerForOut(Object src){
            //Контейнер для элементов выражения\
            this.src = src;
        }
        FormulaContainerForOut(Object src,String name){
            //Контейнер для элементов выражения\
            this.src = src;
            this.name = name;
        }
    }

    void SetResourses(HashMap<String,Object> res){
        needToContainInResourses = res;
    }
    void setRes(String key,Object res){
        needToContainInResourses.put(key,res);
    }

    public FormulaContainerForOut ToGoodLook(ArrayList<FormulSistem.Pair<String,String>> func){
        ArrayList<FormulaContainerForOut> result = new ArrayList<>();
        for (int i = 0;i < func.size();i++){

            if (func.get(i).element1.equals("/")){
                FormulaContainerForOut div =  new FormulaContainerForOut(needToContainInResourses.get(func.get(i).element1),"Division");
                FormulaContainerForOut delimoe = (new FormulaContainerForOut("","Delimoe"));
                FormulaContainerForOut delitel  = (new FormulaContainerForOut("","Delitel"));

                int brackCounter = 0;
                int j = i;

                do{

                    j--;

                    if (func.get(j).element1.equals(")")){brackCounter--;}
                    if (func.get(j).element1.equals("(")){brackCounter++;}


                    delimoe.childContainers.add(new FormulaContainerForOut(needToContainInResourses.get(func.get(j).element1)));

                }
                while (brackCounter != 0);

                j = i;
                do{

                    j++;

                    if (func.get(j).element1.equals(")")){brackCounter--;}
                    if (func.get(j).element1.equals("(")){brackCounter++;}

//                    div.childContainers.add(new FormulaContainerForOut(needToContainInResourses.get(func.get(j).element1),"Delitel"));
                    delitel.childContainers.add(new FormulaContainerForOut(needToContainInResourses.get(func.get(j).element1)));

                }
                while (brackCounter != 0);

                div.childContainers.add(delimoe);
                div.childContainers.add(delitel);
                result.add(div);
            }
            else if (func.get(i).element1.equals("^")){
                FormulaContainerForOut stepen = new FormulaContainerForOut(needToContainInResourses.get(func.get(i).element1),"pow");

                int brackCounter = 0;
                int j = i;

                do{

                    j++;

                    if (func.get(j).element1.equals(")")){brackCounter--;}
                    if (func.get(j).element1.equals("(")){brackCounter++;}


                    stepen.childContainers.add(new FormulaContainerForOut(needToContainInResourses.get(func.get(j).element1)));

                }
                while (brackCounter != 0);

                result.add(stepen);
            }else{
                result.add(new FormulaContainerForOut(needToContainInResourses.get(func.get(i).element1)));
            }


        }

        FormulaContainerForOut realResult = new FormulaContainerForOut("");
        realResult.childContainers = result;
        return realResult;
    }
}
