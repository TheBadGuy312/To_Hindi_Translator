package com.example.languagedetector;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
        Button button ;
        EditText input;
        EditText output;
        boolean isdowloaded= false;
        static String language ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        input = findViewById(R.id.editText);
        output = findViewById(R.id.editText2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(input.getText().toString().isEmpty()){Toast a = Toast.makeText(MainActivity.this, "Please enter Text", Toast.LENGTH_SHORT);
                    a.show();}
                else{
                FirebaseLanguageIdentification languageIdentifier =
                        FirebaseNaturalLanguage.getInstance().getLanguageIdentification();
                languageIdentifier.identifyLanguage(input.getText().toString())
                        .addOnSuccessListener(
                                new OnSuccessListener<String>() {
                                    @Override
                                    public void onSuccess(@Nullable String languageCode) {
                                        if (languageCode != "und") {
                                            language = languageCode;

                                            translate(language);
                                        } else {
                                            Toast a = Toast.makeText(MainActivity.this, "gkjcgskg", Toast.LENGTH_SHORT);
                                            a.show();
                                        }
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Model couldn’t be loaded or other internal error.
                                        // ...
                                    }
                                });
            }}
        });
    }

public void translate(String lang){

            FirebaseTranslatorOptions options =
                    new FirebaseTranslatorOptions.Builder()
                            .setSourceLanguage(FirebaseTranslateLanguage.languageForLanguageCode(lang))
                            .setTargetLanguage(FirebaseTranslateLanguage.HI)
                            .build();
            final FirebaseTranslator englishGermanTranslator =
                    FirebaseNaturalLanguage.getInstance().getTranslator(options);
            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                    .build();
            englishGermanTranslator.downloadModelIfNeeded(conditions)
                    .addOnSuccessListener(
                            new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void v) {
                                    // Model downloaded successfully. Okay to start translating.
                                    // (Set a flag, unhide the translation UI, etc.)
                                    isdowloaded = true;

                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Model couldn’t be downloaded or other internal error.
                                    // ...
                                    Toast a = Toast.makeText(MainActivity.this, "Model couldn’t be downloaded Please check internet connection", Toast.LENGTH_LONG);
                                    a.show();
                                }
                            });


            if (isdowloaded) {
                englishGermanTranslator.translate(input.getText().toString())
                        .addOnSuccessListener(
                                new OnSuccessListener<String>() {
                                    @Override
                                    public void onSuccess(@NonNull String translatedText) {
                                        // Translation successful.
                                        output.setText(translatedText);
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Error.
                                        // ...
                                    }
                                });
            }

    }
}

