package com.example.textsequencegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView
implements SurfaceHolder.Callback {
	
	//private Activity activity;
	private Point point0;
	private Point point1;
	private Point point2;
	private Paint point0paint;
	private Paint point1paint;
	private Paint point2paint;
	private GameThread thread;
	private long totalElapsedTime = 0;

	public GameView(Context context, AttributeSet attrs) {
	
		super(context, attrs);
		
		//activity = (Activity)context;
		
		getHolder().addCallback(this);
		
		point0 = new Point();
		point1 = new Point();
		point2 = new Point();
		
		// set initial pattern
		point0.x = 50;
		point0.y = 300;
		point1.x = 200;
		point1.y = 300;
		point2.x = 350;
		point2.y = 300;
		
		point0paint = new Paint();
		point1paint = new Paint();
		point2paint = new Paint();
		
	}
	
	public void checkGuess(MotionEvent e)
	{
		// lock the canvas to prevent any updates
		Canvas canvas = getHolder().lockCanvas();
		
		// check guess
		// if 
		
		if (canvas != null)
		{
			getHolder().unlockCanvasAndPost(canvas);
		}
		
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		
		
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		//gets called when the surface is added to the main layout
		// create initial color circles
		point0paint.setColor(Color.CYAN);
		point1paint.setColor(Color.MAGENTA);
		point2paint.setColor(Color.GRAY);

		Canvas canvas = null;

		try
		{
			canvas = holder.lockCanvas(null);               

			// lock the surfaceHolder for drawing
			synchronized(holder)
			{
				canvas.drawCircle(point0.x, point0.y, 50, point0paint);
				canvas.drawCircle(point1.x, point0.y, 50, point1paint);
				canvas.drawCircle(point2.x, point0.y, 50, point2paint);
			} // end synchronized block
		} // end try
		finally
		{
			if (canvas != null) 
				holder.unlockCanvasAndPost(canvas);
		} // end finally

		thread = new GameThread(holder);
		thread.start();
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
	      // ensure that thread terminates properly
	      boolean retry = true;
	      thread.setRunning(false);
	      
	      while (retry)
	      {
	         try
	         {
	            thread.join();
	            retry = false;
	         } // end try
	         catch (InterruptedException e)
	         {
	         } // end catch
	      } // end while
		
	}

	public void drawGameElements(Canvas canvas)
	   {
		// clear the background
		Paint backgroundPaint = new Paint(Color.BLACK);
	      canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), 
	         backgroundPaint);
	      
	     // change the colors of the circles
		point0paint.setColor(Color.BLUE);
		point1paint.setColor(Color.RED);
		point2paint.setColor(Color.GREEN);
		
		canvas.drawCircle(point0.x, point0.y, 50, point0paint);
		canvas.drawCircle(point1.x, point0.y, 50, point1paint);
		canvas.drawCircle(point2.x, point0.y, 50, point2paint);
	    
	   }

// Thread subclass to control the game loop
private class GameThread extends Thread
{
   private SurfaceHolder surfaceHolder; // for manipulating canvas
   private boolean threadIsRunning = true; // running by default
   
   // initializes the surface holder
   public GameThread(SurfaceHolder holder)
   {
      surfaceHolder = holder;
      setName("GameThread");
   } // end constructor
   
   // changes running state
   public void setRunning(boolean running)
   {
      threadIsRunning = running;
   } // end method setRunning
   
   // controls the game loop
   @Override
   public void run()
   {
      Canvas canvas = null; // used for drawing
      long previousFrameTime = System.currentTimeMillis(); 
      
     
      while (threadIsRunning)
      {
    	  long currentTime = System.currentTimeMillis();
		  double elapsedTimeMS = currentTime - previousFrameTime;
		  //System.out.println("TextSequenceGame: ElapsedTimeMs " + elapsedTimeMS);
		  totalElapsedTime += elapsedTimeMS / 1000.00; 
		  previousFrameTime = currentTime; // update previous time
		  //System.out.println("TextSequenceGame: Total elapsed time: " + totalElapsedTime);
		  
    	  if (totalElapsedTime < 5)
    	  {
    		  // do nothing
    	  }
    	  else
    	  {
    		  try
    		  {
    			  canvas = surfaceHolder.lockCanvas(null);               

    			  // lock the surfaceHolder for drawing
    			  synchronized(surfaceHolder)
    			  {
    				  //updatePositions(elapsedTimeMS); // update game state
    				  // redraw elements & reset elapsed time
    				  drawGameElements(canvas); // draw 
    				  totalElapsedTime = 0;
    			  } // end synchronized block
    		  } // end try
    		  finally
    		  {
    			  if (canvas != null) 
    				  surfaceHolder.unlockCanvasAndPost(canvas);
    		  } // end finally
    	  } // end else
    	  try {
			Thread.sleep(1000, 0);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      } // end while
   } // end method run
} // end nested class GameThread
} // end GameView