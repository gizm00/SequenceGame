package com.example.textsequencegame;


import android.os.Bundle;
import android.app.Activity;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;

public class MainActivity extends Activity {

	private GameView gameView;
	private GestureDetector gestureDetector;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameView = (GameView) findViewById(R.id.gameView);
        
     // initialize the GestureDetector
        gestureDetector = new GestureDetector(this, gestureListener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
     

       // call the GestureDetector's onTouchEvent method
       return gestureDetector.onTouchEvent(event);
    } // end method onTouchEvent

    // listens for touch events sent to the GestureDetector
    SimpleOnGestureListener gestureListener = new SimpleOnGestureListener()
    {
       // called when the user double taps the screen
       @Override
       public boolean onDoubleTap(MotionEvent e)
       {
          gameView.checkGuess(e); // fire the cannonball
          return true; // the event was handled
       } // end method onDoubleTap
    }; // end gestureListener
}
