/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram{

	/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

	/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

	/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

	/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

	/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

	/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

	/** Separation between bricks */
	private static final int BRICK_SEP = 4;

	/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

	/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

	/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

	/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

	/** Number of turns */
	private static final int NTURNS = 3;
	
	/* The amount of time to pause between frames (48fps). */
	private static final double PAUSE_TIME = 1000.0 / 48; 


	/*main*/
	public void run() {
		setBricks(); //build bricks.
		makePaddle(); //add paddle.
		playGame(); // play game. 	
	}
	
	/*set bricks*/	
	private void setBricks(){
		for (int i = NBRICK_ROWS;i > 0 ; i--){
			double firstRow = 0;
			for (int j = 0; j < NBRICKS_PER_ROW ; j++){
				double x = firstRow + j* BRICK_WIDTH + j*BRICK_SEP;  
				double y = BRICK_Y_OFFSET + i*(BRICK_HEIGHT+BRICK_SEP);
				GRect brick = new GRect(x,y,BRICK_WIDTH,BRICK_HEIGHT);
				brick.setFilled(true);
				if (i==1|i==2){
					brick.setColor(Color.RED);
				}
				if (i==3|i==4){
					brick.setColor(Color.ORANGE);
				}
				if (i==5|i==6){
					brick.setColor(Color.YELLOW);
				}
				if (i==7|i==8){
					brick.setColor(Color.GREEN);
				}
				if (i==9|i==10){
					brick.setColor(Color.CYAN);
				}
				add(brick);
			}
		}
	}
	
	
	/* make a paddle which can move as mouse moves*/
	public void makePaddle(){
		double x = (APPLICATION_WIDTH - PADDLE_WIDTH)/2; 
		double y = APPLICATION_HEIGHT - PADDLE_Y_OFFSET;
		paddle = new GRect(x,y,PADDLE_WIDTH,PADDLE_HEIGHT);
		add(paddle);
	}	
		
	public void mouseMoved(MouseEvent e){
		paddle.setLocation(e.getX() - PADDLE_WIDTH/2, getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT); 	    
	}
	
	private void playGame(){
		for (int i=NTURNS ; i>0 ; i--){
			waitForClick();
			makeBall();
			addMouseListeners();
			moveBall();
			if (BricksNum <= 0){
				break;
			}
		}
	}
	


	/* make a ball located in the middle of the canvas*/
	public void makeBall(){
		int x = APPLICATION_WIDTH / 2 - BALL_RADIUS ; 
		int y = APPLICATION_HEIGHT / 2 - BALL_RADIUS;
		ball = new GOval(x,y,BALL_RADIUS,BALL_RADIUS);
		add(ball);
	}
	
	/*set vx,vy to make ball move,considering several collision events*/
	private void moveBall(){
		vy = 3.0;
		RandomGenerator rgen = RandomGenerator.getInstance();
		vx = rgen.nextDouble(1.0, 3.0);
		if (rgen.nextBoolean(0.5)){
			vx = -vx;
		}	
		while(true){
			ball.move(vx, vy);
			pause(PAUSE_TIME);
			getCollidingObject();
			
			/*break when bricks are cleared*/
			if (BricksNum <= 0){
				remove(ball);
				break;
			}
			
			/*bounce the ball when face the wall*/
			if (ball.getY() <= 0){
				vy = -vy;
			}
			if (ball.getX() + ball.getWidth() >= getWidth()){
				vx = -vx;
			}
			if (ball.getX() <= 0){
				vx = -vx;
			}
			/*bounce the ball when collider is the paddle*/
			if (collider == paddle){
				vy = -vy;
				collider = null;
			}	
			/*bounce the ball when collider is bricks*/
			if (collider != null && collider != paddle){
				vy = -vy;
				remove(collider);
				BricksNum = BricksNum - 1;
				collider = null;
			}
			/*break when the ball fall below the bottom*/
			if (ball.getY() + ball.getHeight() >= getHeight()){
				remove(ball);
				break;	
			}
		}	
	}
		
	/*check if ball meets an object*/
	private GObject getCollidingObject(){
		if (getElementAt(ball.getX(),ball.getY())!= null){
			collider = getElementAt(ball.getX(),ball.getY());
			return collider;
		}
		else if (getElementAt(ball.getX()+ ball.getWidth(),ball.getY())!= null){
			collider = getElementAt(ball.getX()+ ball.getWidth(),ball.getY());	
			return collider;
		}
		else if (getElementAt(ball.getX(),ball.getY()+ ball.getHeight())!= null){
			collider = getElementAt(ball.getX(),ball.getY()+ ball.getHeight());
			return collider;
		}
		else if (getElementAt(ball.getX()+ ball.getWidth(),ball.getY()+ ball.getHeight())!= null){
			collider = getElementAt(ball.getX()+ ball.getWidth(),ball.getY()+ ball.getHeight());	
			return collider;
		}
		else{
			return null;
		}
	}

	//add some instance variable.
	private GRect paddle;
	private GOval ball;
	private double vx,vy;
	private GObject collider;
	private int BricksNum = NBRICKS_PER_ROW*NBRICK_ROWS;

}




