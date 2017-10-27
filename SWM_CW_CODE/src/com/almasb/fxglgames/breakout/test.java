package com.almasb.fxglgames.breakout;





import javafx.geometry.Point2D;
import javafx.util.Duration;

public class test {

	 
	private static int y;

	public static void main(String[] args) {
		Duration t = Duration.seconds(3);
		
		Point2D PD = new Point2D(0, -FXGLMath.random(2f, 4f) * 60);
		Point2D PDtwo =  new Point2D(FXGLMath.random(0, (float)getWidth()), y - 20 + FXGLMath.random(50));
		Point2D PDthree = new Point2D(FXGLMath.random(-0.05f, 0), FXGLMath.random(-0.05f, 0));
		//(i, x, y) -> t;
		System.out.println(t);
		System.out.println(PD);
		System.out.println(PDtwo);
		System.out.println("PRINT:"+PDthree);
	}

	private static float getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}
}
