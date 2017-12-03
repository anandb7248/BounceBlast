package game;

import game.ball.Ball;
import javafx.application.Platform;
import javafx.scene.shape.Shape;
import layout.CreateAccount;
import layout.Main;
import layout.MainMenu;
import javafx.scene.paint.Color;
import java.util.List;
import javax.swing.JOptionPane;

public class GameEngine implements Runnable {

	private static final double UPDATE_CAP = 1.0/60.0;
	private static final int HANDLED = 1;
	private GameScene scene;
	private Main app;
	
	private boolean goingRight = true; //initial direction
	private boolean goingDown = false;
	private boolean goingLeft = false;
	private boolean goingUp = false;
	private double delta = 5;
	private double podLength = 50;
	private int alertCount = 0;
	
<<<<<<< .mine
	boolean[][] isExiting;
	
	public GameEngine(GameScene scene) {
||||||| .r122
	public GameEngine(GameScene scene) {
=======
	public GameEngine(Main a, GameScene scene) {
>>>>>>> .r124
		this.scene = scene;
		this.app = a;
	}
	
	@SuppressWarnings("static-access")
	public void run() {
		boolean render = false;
		isExiting = new boolean[scene.getAllPlayers().size()][scene.getAllPlayers().size()];
		
		double firstTime = 0; 
		double passedTime = 0; 
		double unprocessedTime = 0;
		double lastTime = System.nanoTime() / 1000000000.0;
		double frameTime = 0; 
		//double frames = 0; 
		//double fps = 0;
		
		scene.h.setStartX(scene.board.getX());
		scene.h.setStartY(scene.board.getY());
		scene.h.setEndX(scene.board.getX());
		scene.h.setEndY(scene.board.getY());
		
		scene.v.setStartX(-100);
		scene.v.setStartY(-100);
		scene.v.setEndX(-100);
		scene.v.setEndY(-100);
		
		while(!Thread.currentThread().interrupted()) {
			render = false;
			firstTime = System.nanoTime() / 1000000000.0;
			passedTime = firstTime - lastTime;
			lastTime = firstTime;
			
			unprocessedTime += passedTime;
			frameTime += passedTime;
			
			while(unprocessedTime >= UPDATE_CAP) {
				unprocessedTime -= UPDATE_CAP;
				render = true;
				// Adjust frame values
				if (frameTime >= 1.0) {
					frameTime = 0;
					//fps = frames;
					//frames = 0;
					//System.out.println("FPS: " + fps);
				}
			}
			
			if (render) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						
						for (Ball player : scene.getAllPlayers()) {
							updateGame(player);
							player.updatePosition();
						}
						
						// Move Platform Clockwise
						movePlatform();
						ballHitsPlatform();
						
						handleCollisions(scene.getAllPlayers());	
					}
				});
				//frames++;
			}
		}
		//System.out.println("Game is done");
	}
	
	public void updateGame(Ball player) {
		if (scene.board.isTouchingTop(player) || scene.board.isTouchingBottom(player)
				&& !player.getVertExiting()) {
			player.reflectVertical();
			player.setVertExiting(true);
		}
		if (scene.board.isTouchingLeft(player) || scene.board.isTouchingRight(player)
				&& !player.getHorizontalExiting()) {
			player.reflectHorizontal();
			player.setHorizontalExiting(true);
		}

		// Prevent ball from getting stuck in wall
		if (!(scene.board.isTouchingTop(player) || scene.board.isTouchingBottom(player)) && player.getVertExiting()) {
			player.setVertExiting(false);
		}
		
		if ( !(scene.board.isTouchingLeft(player) || scene.board.isTouchingRight(player)) && player.getHorizontalExiting()) {
			player.setHorizontalExiting(false);
		}
	}
	
	public void movePlatform() {
		if (goingRight) {
			//end
			//hasn't reached the end yet
			if (scene.h.getEndX() < scene.board.xmax()) {
				scene.h.setEndX(scene.h.getEndX() + delta);
			}
			else if (!goingDown){
				goingDown = true;
				scene.v.setStartX(scene.board.xmax());
				scene.v.setStartY(scene.board.getY());
				scene.v.setEndX(scene.board.xmax());
				scene.v.setEndY(scene.board.getY());
			}
			if (scene.h.getStartX() < scene.board.xmax()) {
				// pod growing in size
				if ((scene.h.getEndX() - scene.board.getX() >= podLength) || (scene.h.getEndX() >= scene.board.xmax())) {
					scene.h.setStartX(scene.h.getStartX() + delta);
				}
			}
			else {
				goingRight = false;
				scene.h.setStartX(-100);
				scene.h.setStartY(-100);
				scene.h.setEndX(-100);
				scene.h.setEndY(-100);
			}
		}
		if (goingDown) {
			//end
			//hasn't reached the end yet
			if (scene.v.getEndY() < scene.board.ymax()) {
				scene.v.setEndY(scene.v.getEndY() + delta);
			}
			else if (!goingLeft){
				goingLeft = true;
				scene.h.setStartX(scene.board.xmax());
				scene.h.setStartY(scene.board.ymax());
				scene.h.setEndX(scene.board.xmax());
				scene.h.setEndY(scene.board.ymax());
			}
			
			if (scene.v.getStartY() < scene.board.ymax()) {
				// pod growing in size
				if ((scene.v.getEndY() - scene.board.getY() >= podLength) || (scene.v.getEndY() >= scene.board.ymax())) {
					scene.v.setStartY(scene.v.getStartY() + delta);
				}
			}
			else {
				goingDown = false;
				scene.v.setStartX(-100);
				scene.v.setStartY(-100);
				scene.v.setEndX(-100);
				scene.v.setEndY(-100);
			}
		}
		if (goingLeft) {
			//end
			//hasn't reached the end yet
			if (scene.h.getEndX() > scene.board.getX()) {
				scene.h.setEndX(scene.h.getEndX() - delta);
			}
			else if (!goingUp){
				goingUp = true;
				scene.v.setStartX(scene.board.getX());
				scene.v.setStartY(scene.board.ymax());
				scene.v.setEndX(scene.board.getX());
				scene.v.setEndY(scene.board.ymax());
			}
			
			if (scene.h.getStartX() > scene.board.getX()) {
				// pod growing in size
				if ((scene.board.xmax() - scene.h.getEndX() >= podLength) || (scene.h.getEndX() >= scene.board.xmax())) {
					scene.h.setStartX(scene.h.getStartX() - delta);
				}
			}
			else {
				goingLeft = false;
				scene.h.setStartX(-100);
				scene.h.setStartY(-100);
				scene.h.setEndX(-100);
				scene.h.setEndY(-100);
			}
		}
		
		if (goingUp) {
			//end
			//hasn't reached the end yet
			if (scene.v.getEndY() > scene.board.getY()) {
				scene.v.setEndY(scene.v.getEndY() - delta);
			}
			else if (!goingRight){
				goingRight = true;
				scene.h.setStartX(scene.board.getX());
				scene.h.setStartY(scene.board.getY());
				scene.h.setEndX(scene.board.getX());
				scene.h.setEndY(scene.board.getY());
			}
			
			if (scene.v.getStartY() > scene.board.getY()) {
				// pod growing in size
				if ((scene.board.ymax() - scene.v.getEndY() >= podLength) || (scene.v.getEndY() >= scene.board.ymax())) {
					scene.v.setStartY(scene.v.getStartY() - delta);
				}
			}
			else {
				goingUp = false;
				scene.v.setStartX(-100);
				scene.v.setStartY(-100);
				scene.v.setEndX(-100);
				scene.v.setEndY(-100);
			}		
		}
	}
	
	public void ballHitsPlatform() {
		if (scene.v.getBoundsInParent().intersects(scene.mainPlayer.getBoundsInParent()) ||
				scene.h.getBoundsInParent().intersects(scene.mainPlayer.getBoundsInParent())) {
			scene.mainPlayer.setFill(Color.WHITE);

			if(alertCount==0)
			{
				alertCount++;
				JOptionPane.showMessageDialog(null, "Game Over!", "BounceBlast", JOptionPane.INFORMATION_MESSAGE);
				
				app.updateScene(new MainMenu(app));
			}
		}
	}
	
	public void handleCollisions(List<Ball> players) {
		System.out.println("Handling collision");
		boolean[][] collisionsHandled = new boolean[players.size()][players.size()];
		for (int i = 0; i < players.size(); i++) {
			for (int j = 0; j < players.size(); j++) {
				// ball doesn't collide with itself
				if (i != j) {
					Shape intersect = Shape.intersect(players.get(i), players.get(j));
					boolean isTouching = intersect.getBoundsInLocal().getWidth() != -1;
					System.out.println("Is touching : " + isTouching);
					if (isTouching && !isExiting[i][j]) {
						Ball.handleCollision(players.get(i), players.get(j));
						isExiting[i][j] = true;
						isExiting[j][i] = true;
						collisionsHandled[i][j] = true;
						collisionsHandled[j][i] = true;
					}
					if (isExiting[i][j] && intersect.getBoundsInLocal().getWidth() == -1) {
						isExiting[i][j] = false;
						isExiting[j][i] = false;
					}
				}
				
			}
		}
	}
	
}
