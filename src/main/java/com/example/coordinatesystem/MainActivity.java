package com.example.coordinatesystem;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.coordinatesystem.databinding.ActivityMainBinding;
//
//public class MainActivity extends AppCompatActivity {
//
//    private AppBarConfiguration appBarConfiguration;
//    private ActivityMainBinding binding;
//
//
//    Intent intent;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        intent = new Intent(this, MainActivity2.class);
//
//
//        TextView textView1 = (TextView) findViewById(R.id.textView);
//        TextView textView2 = (TextView) findViewById(R.id.textView2);
//        textView1.setText("Xmax\nXmin");
//        textView2.setText("Ymax\nYmin");
//
//        Button btn2 = (Button)findViewById(R.id.button3);
//        Button btn3 = (Button)findViewById(R.id.button4);
//        Button btn4 = (Button)findViewById(R.id.button5);
//        Button btn5 = (Button)findViewById(R.id.button6);
//        btn2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                anotherBtnClick(btn2.getText().toString());
//            }
//        });
//        btn3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                anotherBtnClick(btn3.getText().toString());
//            }
//        });
//        btn4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                anotherBtnClick(btn4.getText().toString());
//            }
//        });
//        btn5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                anotherBtnClick(btn5.getText().toString());
//            }
//        });
//
//
//        Button btn = (Button)findViewById(R.id.button);
//        EditText[] editTexts = new EditText[]{(EditText) findViewById(R.id.editTextTextPersonName),(EditText) findViewById(R.id.editTextTextPersonName2),(EditText) findViewById(R.id.editTextTextPersonName3),(EditText) findViewById(R.id.editTextTextPersonName4)};
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                btnClick(Double.parseDouble(editTexts[0].getText().toString()),Double.parseDouble(editTexts[1].getText().toString()),Double.parseDouble(editTexts[2].getText().toString()),Double.parseDouble(editTexts[3].getText().toString()));
//            }
//        });
////        View.OnClickListener
//////        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
////        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
////        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//
//    }
//
//    void btnClick(double xMax,double yMax,double xMin,double yMin){
//        intent.putExtra("xMax",xMax);
//        intent.putExtra("yMax",yMax);
//        intent.putExtra("xMin",xMin);
//        intent.putExtra("yMin",yMin);
//        startActivity(intent);
//    }
//    void anotherBtnClick(String funcName){
//        intent.putExtra("funcName",funcName);
//    }
//
////    @Override
//////    public boolean onSupportNavigateUp() {
////////        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//////        return NavigationUI.navigateUp(navController, appBarConfiguration)
//////                || super.onSupportNavigateUp();
//////    }
//}