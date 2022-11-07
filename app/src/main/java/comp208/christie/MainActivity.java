package comp208.christie;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Game game;
    TableLayout matchGameBoard;
    final int ROWS = 4;
    final int COLS = 3;
    Card[][] matchGameGrid = new Card[ROWS][COLS];
    int[] rand = new int[]{1, 2, 3, 4, 5, 6, 1, 2, 3, 4, 5, 6};
    int initRand = 0;
    int card1, card2;
    List<Card[][]> shuffleCards;
    List<Integer> shuffleRand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        matchGameBoard = findViewById(R.id.matchGameBoard);
        initGame();
    }


    private void initGame() {
        game = new Game();
        game.selected = false;
        game.numGuesses = 0;
        game.matches = 0;
        shuffleCards = new ArrayList<>();
        shuffleRand = new ArrayList<>();

        for (int i : rand) {
            shuffleRand.add(rand[i]);
            Log.i("shuffleRand", "Before shuffle: " + shuffleRand);
        }

        Collections.shuffle(shuffleRand);

        Log.i("shuffleRand", "After shuffle" + shuffleRand);

        for (int row = 0; row < ROWS; ++row) {
            TableRow tableRow = (TableRow) matchGameBoard.getChildAt(row);

            for (int col = 0; col < COLS; ++col) {
                Card card = new Card();
                ImageView iv = (ImageView) tableRow.getChildAt(col);

                Log.i("Grid Count", "Row: " + row + " Col: " + col);

                iv.setOnClickListener(ivListener);
                iv.setImageResource(R.drawable.question);

                card.imageId = R.drawable.question;
                card.row = row;
                card.col = col;
                card.randomImage = shuffleRand.get(initRand);

                Log.i("Random Image", "Random image is: " + card.randomImage);

                initRand++;

                matchGameGrid[row][col] = card;

                iv.setTag(card);
            }
        }

        Collections.shuffle(shuffleCards);
    }

    View.OnClickListener ivListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            game.iv = (ImageView) view;
            Card card = (Card) game.iv.getTag();

            if (card.imageId == R.drawable.question) {
                game.numGuesses++;
                Log.i("ivListener", "Score (game.numGuesses): " + game.numGuesses);

                if (!game.selected) {
                    Log.i("ivListener", "I should be here when selected = false and image is a question mark... selected = " + game.selected);

                    switch (card.randomImage) {
                        case 1:
                            game.iv.setImageResource((R.drawable.python));
                            card.imageId = R.drawable.python;
                            break;
                        case 2:
                            game.iv.setImageResource((R.drawable.td));
                            card.imageId = R.drawable.td;
                            break;
                        case 3:
                            game.iv.setImageResource((R.drawable.moon));
                            card.imageId = R.drawable.moon;
                            break;
                        case 4:
                            game.iv.setImageResource((R.drawable.android));
                            card.imageId = R.drawable.android;
                            break;
                        case 5:
                            game.iv.setImageResource(R.drawable.google);
                            card.imageId = R.drawable.google;
                            break;
                        case 6:
                            game.iv.setImageResource(R.drawable.dot);
                            card.imageId = R.drawable.dot;
                            break;
                    }

                    card1 = card.imageId;

                    game.selected = true;
                }
                else {
                    Log.i("ivListener", "I should be here when selected = true and image is a question mark... selected = " + game.selected);
                    switch (card.randomImage) {
                        case 1:
                            game.iv.setImageResource((R.drawable.python));
                            card.imageId = R.drawable.python;
                            break;
                        case 2:
                            game.iv.setImageResource((R.drawable.td));
                            card.imageId = R.drawable.td;
                            break;
                        case 3:
                            game.iv.setImageResource((R.drawable.moon));
                            card.imageId = R.drawable.moon;
                            break;
                        case 4:
                            game.iv.setImageResource((R.drawable.android));
                            card.imageId = R.drawable.android;
                            break;
                        case 5:
                            game.iv.setImageResource(R.drawable.google);
                            card.imageId = R.drawable.google;
                            break;
                        case 6:
                            game.iv.setImageResource(R.drawable.dot);
                            card.imageId = R.drawable.dot;
                            break;
                    }

                    card2 = card.imageId;

                    Log.i("ivListener", "card1: " + card1 + " card2: " + card2);

                    if (card1 != card2) {
                        Log.i("ivListener", "Waiting");

                        new java.util.Timer().schedule(
                                new java.util.TimerTask() {
                                    @Override
                                    public void run() {
                                        for (int row = 0; row < ROWS; ++row) {
                                            game.tableRow = (TableRow) matchGameBoard.getChildAt(row);

                                            for (int col = 0; col < COLS; ++col) {
                                                if (matchGameGrid[row][col].imageId == card1 || matchGameGrid[row][col].imageId == card2) {
                                                    Log.i("ivListener", "card1: " + matchGameGrid[row][col].imageId + " == " + card1 + " card2: " + matchGameGrid[row][col].imageId + " == " + card2);
                                                    game.iv = (ImageView) game.tableRow.getChildAt(col);
                                                    game.iv.setImageResource(R.drawable.question);
                                                    matchGameGrid[row][col].imageId = R.drawable.question;
                                                }
                                            }
                                        }
                                        Log.i("ivListener", "Finished");
                                    }
                                },1000
                        );
                    }
                    else
                        game.matches++;

                    game.selected = false;

                    if (game.matches == 6) {
                        Log.i("ivListener", "Game Over! Score: " + game.numGuesses);
                        initRand = 0;
                        Intent intent = new Intent(MainActivity.this, ScoreActivity.class);
                        intent.putExtra("guesses", game.numGuesses);
                        startActivityForResult(intent, 1);
                    }
                }
            }
            else
                Log.i("ivListener", "Image is already selected");
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == 0)
            finish();
        else
            initGame();

        super.onActivityResult(requestCode, resultCode, data);
    }
}