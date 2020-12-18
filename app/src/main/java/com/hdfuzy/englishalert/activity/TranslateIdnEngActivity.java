package com.hdfuzy.englishalert.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.common.model.RemoteModelManager;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.TranslateRemoteModel;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.hdfuzy.englishalert.R;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;

public class TranslateIdnEngActivity extends AppCompatActivity {
    TextView mTranslatedText;
    EditText mSourceText;
    FrameLayout mTranslateBtn;
    ImageView microphone, speaker;
    TextToSpeech textToSpeech;
    String textTranslated;
    int positionSpeaker = 0;
    int positionMicrophone = 0;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate_idn_eng);
        FrameLayout EngtoIdn = findViewById(R.id.eng_to_idn);
        EngtoIdn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoEng = new Intent(TranslateIdnEngActivity.this, TranslateEngIdnActivity.class);
                startActivity(gotoEng);
                overridePendingTransition(0,0);
                textToSpeech.stop();
                finish();
            }
        });

        microphone = findViewById(R.id.microphone);
        microphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                microphone.setBackgroundResource(R.drawable.ic_mic_red);
                inputSuara();
            }
        });

        ProgressDialog progressDialog = new ProgressDialog(this);
        ImageView backButton = findViewById(R.id.back_button);
        mTranslatedText = findViewById(R.id.translatedText);
        mSourceText = findViewById(R.id.sourceText);
        mTranslateBtn = findViewById(R.id.translate);
        mTranslateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Translating...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                String sourceText = mSourceText.getText().toString();
                if (sourceText.isEmpty()) {
                    mSourceText.setError("Masukkan text yang ingin diterjemahkan");
                    progressDialog.dismiss();
                } else {
                    char lastChar = sourceText.charAt(sourceText.length()-1);
                    if (lastChar != '.' || lastChar != '!' || lastChar != '?') {
                        sourceText = sourceText + ".";
                    }
                    DownloadModels();
                    TranslatorOptions options = new TranslatorOptions.Builder()
                            .setSourceLanguage(TranslateLanguage.INDONESIAN)
                            .setTargetLanguage(TranslateLanguage.ENGLISH)
                            .build();
                    final Translator indoEnglishTranslator = Translation.getClient(options);
                    indoEnglishTranslator.translate(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            progressDialog.dismiss();
                            mTranslatedText.setText(s);
                            textTranslated = s;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(TranslateIdnEngActivity.this, "Sedang mengunduh package kamus! Sekali mengunduh untuk digunakan bahkan saat luring / offline.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });

        speaker = findViewById(R.id.speaker);
        speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speaker.setBackgroundResource(R.drawable.ic_mic_green);
                if (textTranslated == "") {
                    Toast.makeText(TranslateIdnEngActivity.this, "Belum ada teks yang dimasukkan", Toast.LENGTH_SHORT).show();
                } else {
                    String toSpeak = textTranslated;
                    textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TranslateIdnEngActivity.super.onBackPressed();
                textToSpeech.stop();
            }
        });
    }

    @Override
    protected void onPause() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onPause();
    }

    private void inputSuara() {
        Intent record = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        record.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        record.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        record.putExtra(RecognizerIntent.EXTRA_PROMPT, "Silahkan bicara untuk diterjemahkan");
        try {
            startActivityForResult(record, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), "Sorry, your device not supported this.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mSourceText.setText(result.get(0));
                }
                break;
            }
        }
    }

    private void DownloadModels() {

        // Get Downloaded Models
        RemoteModelManager modelManager = RemoteModelManager.getInstance();
        modelManager.getDownloadedModels(TranslateRemoteModel.class)
                .addOnSuccessListener(new OnSuccessListener<Set<TranslateRemoteModel>>() {
                    @Override
                    public void onSuccess(Set<TranslateRemoteModel> translateRemoteModels) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        // Download Indo Models
        TranslateRemoteModel indoModels = new TranslateRemoteModel.Builder(TranslateLanguage.INDONESIAN).build();
        DownloadConditions conditions = new DownloadConditions.Builder()
                .build();
        modelManager.download(indoModels, conditions)
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error.
                    }
                });

        // Download English Models
        TranslateRemoteModel englishModels = new TranslateRemoteModel.Builder(TranslateLanguage.ENGLISH).build();
        DownloadConditions conditions2 = new DownloadConditions.Builder()
                .build();
        modelManager.download(indoModels, conditions2)
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error.
                    }
                });
    }

}