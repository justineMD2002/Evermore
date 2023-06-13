package com.example.evermore;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class Flashcards extends AppCompatActivity {
    private AnimatorSet front_anim;
    private AnimatorSet back_anim;
    private CardView cardView, cardview2;
    private boolean isFront = true;
    TextView tvCtr, first_card_front, first_card_back, sec_card_front, sec_card_back, total;
    int count = 1;
    ImageView back, next;
    Intent intent;
    ArrayList<Model_Questionnaire> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcards);

        back = findViewById(R.id.btn_back);
        next = findViewById(R.id.btn_next);
        tvCtr = findViewById(R.id.ctr);
        tvCtr.setText(Integer.toString(count));
        cardView = findViewById(R.id.flashcard);
        cardview2 = findViewById(R.id.flashcard2);
        intent = getIntent();
        total = findViewById(R.id.textView12);
        first_card_front = findViewById(R.id.card_front2);
        first_card_back = findViewById(R.id.card_back2);
        sec_card_front = findViewById(R.id.card_front3);
        sec_card_back = findViewById(R.id.card_back3);

        String course = intent.getStringExtra("course");
        setTitle(course);
        if(course != null){
            if(course.equals("Medical-Surgical Nursing")) getArr("medsurg");
            else if(course.equals("Nursing Informatics")) getArr("informatics_nursing");
            else if(course.equals("Pathophysiology")) getArr("patho");
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        @SuppressLint("WrongViewCast") TextView card_front = findViewById(R.id.card_front2);
        @SuppressLint("WrongViewCast") TextView card_back = findViewById(R.id.card_back2);

        float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        card_front.setCameraDistance(8000 * scale);
        card_back.setCameraDistance(8000 * scale);

        front_anim = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.card_front_anim);
        back_anim = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.card_back_anim);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFront) {
                    front_anim.setTarget(card_front);
                    back_anim.setTarget(card_back);
                    front_anim.start();
                    back_anim.start();
                    isFront = false;
                } else {
                    front_anim.setTarget(card_back);
                    back_anim.setTarget(card_front);
                    back_anim.start();
                    front_anim.start();
                    isFront = true;
                }
            }
        });

        @SuppressLint("WrongViewCast") TextView card_front2 = findViewById(R.id.card_front3);
        @SuppressLint("WrongViewCast") TextView card_back2 = findViewById(R.id.card_back3);

        cardview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFront) {
                    front_anim.setTarget(card_front2);
                    back_anim.setTarget(card_back2);
                    front_anim.start();
                    back_anim.start();
                    isFront = false;
                } else {
                    front_anim.setTarget(card_back2);
                    back_anim.setTarget(card_front2);
                    back_anim.start();
                    front_anim.start();
                    isFront = true;
                }
            }
        });


        back.setOnClickListener(view -> {
            try {
                if (count > 1) {
                    count--;
                    tvCtr.setText(String.valueOf(count));

                    sec_card_front.setText(arrayList.get(count-1).getQuestion());
                    sec_card_back.setText((arrayList.get(count-1).getAnswer()));

                    AnimatorSet slideAnimation = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.slide_to_right);
                    slideAnimation.setTarget(cardView);
                    AnimatorSet secondCardAnimation = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.slide_from_left);
                    secondCardAnimation.setTarget(cardview2);
                    cardview2.setVisibility(View.INVISIBLE);
                    slideAnimation.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            cardView.setVisibility(View.INVISIBLE);
                            cardview2.setVisibility(View.VISIBLE);
                            secondCardAnimation.start();
                        }
                    });
                    slideAnimation.start();
                }
            } catch (Exception a) {
                sec_card_front.setText("End of lesson");
                sec_card_back.setText("End of lesson");
            }
        });

        next.setOnClickListener(view -> {
            try {
                if (count < arrayList.size()) {
                    count++;
                    tvCtr.setText(String.valueOf(count));

                    sec_card_front.setText(arrayList.get(count-1).getQuestion());
                    sec_card_back.setText((arrayList.get(count-1).getAnswer()));

                    AnimatorSet slideAnimation = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.slide_to_left);
                    slideAnimation.setTarget(cardView);
                    AnimatorSet secondCardAnimation = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.slide_from_right);
                    secondCardAnimation.setTarget(cardview2);
                    cardview2.setVisibility(View.INVISIBLE);
                    slideAnimation.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            cardView.setVisibility(View.INVISIBLE);
                            cardview2.setVisibility(View.VISIBLE);
                            secondCardAnimation.start();
                        }
                    });
                    slideAnimation.start();
                }
            } catch (Exception a) {
                sec_card_front.setText("End of lesson");
                sec_card_back.setText("End of lesson");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    protected void getArr(String collection) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference flashcardsRef = db.collection(collection);
        ArrayList<Model_Questionnaire> questionnaires = new ArrayList<>();
        flashcardsRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String question = document.getString("question");
                                String answer = document.getString("ans");
                                Model_Questionnaire m = new Model_Questionnaire(question, answer);
                                questionnaires.add(m);
                            }
                            total.setText("of " + questionnaires.size());
                            handleResult(questionnaires);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        // There's no need to return questionnaires here because the operation is asynchronous
    }
    protected void handleResult(ArrayList<Model_Questionnaire> questionnaires) {
        arrayList = new ArrayList<>(questionnaires);
        if (!arrayList.isEmpty() && arrayList.size() > 1) {
            sec_card_front.setText(arrayList.get(0).getQuestion());
            sec_card_back.setText((arrayList.get(0).getAnswer()));
        } else {
            // Handle case where no documents are found in the collection
        }
    }
}
