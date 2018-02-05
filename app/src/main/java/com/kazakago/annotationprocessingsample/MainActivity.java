package com.kazakago.annotationprocessingsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.kazakago.annotationprocessing.ClassAnnotation;
import com.kazakago.annotationprocessing.FieldAnnotation;

@ClassAnnotation
public class MainActivity extends AppCompatActivity {

    @FieldAnnotation
    String firstName = "Taro";
    @FieldAnnotation
    String lastName = "Yamada";

    private MainActivityToastProvider toastProvider = new MainActivityToastProvider(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button firstNameButton = findViewById(R.id.firstNameButton);
        firstNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastProvider.showFirstNameToast();
            }
        });
        Button lastNameButton = findViewById(R.id.lastNameButton);
        lastNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastProvider.showFirstNameToast();
            }
        });
    }

}
