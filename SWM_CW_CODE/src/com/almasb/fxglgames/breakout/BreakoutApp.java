/*
 * The MIT License (MIT)
 *
 * FXGL - JavaFX Game Library
 *
 * Copyright (c) 2015-2017 AlmasB 
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.almasb.fxglgames.breakout;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.ecs.Entity;
import com.almasb.fxgl.ecs.component.IrremovableComponent;
import com.almasb.fxgl.effect.ParticleControl;
import com.almasb.fxgl.effect.ParticleEmitter;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.EntityView;
import com.almasb.fxgl.entity.RenderLayer;
import com.almasb.fxgl.entity.component.PositionComponent;
import com.almasb.fxgl.gameplay.Level;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.parser.text.TextLevelParser;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.settings.GameSettings;
import com.almasb.fxglgames.breakout.control.BallControl;
import com.almasb.fxglgames.breakout.control.BatControl;
import com.almasb.fxglgames.breakout.control.BrickControl;

import com.almasb.fxglgames.breakout.ParticleEmitters;
import javafx.animation.PathTransition;
import javafx.geometry.Point2D;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;


import java.util.Map;

/**
 * @author Almas Baimagambetov 
 */
public class BreakoutApp extends GameApplication {
	

    private BatControl getBatControl() {
        return getGameWorld().getSingleton(BreakoutType.BAT).get().getControl(BatControl.class);
    }

    //Build new method for getGameWorld. have getSingleton(), addEntity(), setLevel(level) method
    private Object getGameWorld() {
		// TODO Auto-generated method stub
		return null;
	}

	private BallControl getBallControl() {
        return getGameWorld().getSingleton(BreakoutType.BALL).get().getControl(BallControl.class);
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Breakout Underwater");
        settings.setVersion("0.2");
           settings.setWidth(600);
              settings.setHeight(800);
        settings.setIntroEnabled(false);
             settings.setMenuEnabled(false);
                    settings.setProfilingEnabled(false);
        settings.setCloseConfirmation(false);
        settings.setApplicationMode(ApplicationMode.DEVELOPER);
    }

    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                getBatControl().left();
            }
        }, KeyCode.A);

        getInput().addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                getBatControl().right();
            }
        }, KeyCode.D);
    }

    //Build new method for getInput object have addAction() method
    private Object getInput() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("lives", 3);
    }

    @Override
    protected void initGame() {
        initBGM();
        initLevel();
        initBubbles();
    }

    private void initBGM() {
        getAudioPlayer().loopBGM("BGM01.wav");
    }

    //Build new method for getAudioPlayer, object have loopBGM(String) method
    private Object getAudioPlayer() {
		// TODO Auto-generated method stub
		return null;
	}

	private void initLevel() {
        initBackground();

        TextLevelParser parser = new TextLevelParser(new BreakoutFactory());
        Level level = parser.parse("levels/level1.txt");
        getGameWorld().setLevel(level);
    }

    private void initBackground() {
        Rectangle bg0 = new Rectangle(getWidth(), getHeight(),
                new LinearGradient(getWidth() / 2, 0, getWidth() / 2, getHeight(),
                        false, CycleMethod.NO_CYCLE,
                        new Stop(0.2, Color.AQUA), new Stop(0.8, Color.BLACK)));

        Rectangle bg1 = new Rectangle(getWidth(), getHeight(), Color.color(0, 0, 0, 0.2));
        bg1.setBlendMode(BlendMode.DARKEN);

        EntityView bg = new EntityView(RenderLayer.BACKGROUND);
        bg.addNode(bg0);
        bg.addNode(bg1);

        // we add IrremovableComponent because regardless of the level
        // the background and screen bounds stay in the game world
        Entities.builder()
                .viewFromNode(bg)
                .with(new IrremovableComponent())
                .buildAndAttach(getGameWorld());

        Entity screenBounds = Entities.makeScreenBounds(40);
        screenBounds.addComponent(new IrremovableComponent());

        getGameWorld().addEntity(screenBounds);
    }

    private void initBubbles() {
        ParticleEmitter emitter = new ParticleEmitter();
        emitter.setSourceImage(getAssetLoader().loadTexture("bubble.png").getImage());
        emitter.setBlendMode(BlendMode.SRC_OVER);
        emitter.setEmissionRate(0.25);
        //(i, x, y) -> Duration.seconds(3) can't be recognized 
        //set object (i, x, y), it may means let i,x,y assign seconds(3)
        Duration i =  Duration.seconds(3);
        //it maight mean take i, x, y as input and return second(3);
       //?????????????????????????????????????????????????????????????
        emitter.setExpireFunction((i, x, y) -> Duration.seconds(3));
        emitter.setVelocityFunction((i, x, y) -> new Point2D(0, -FXGLMath.random(2f, 4f) * 60));
        emitter.setSpawnPointFunction((i, x, y) -> new Point2D(FXGLMath.random(0, (float)getWidth()), y - 20 + FXGLMath.random(50)));
        emitter.setScaleFunction((i, x, y) -> new Point2D(FXGLMath.random(-0.05f, 0), FXGLMath.random(-0.05f, 0)));
        emitter.setInterpolator(Interpolators.EXPONENTIAL.EASE_IN());

        Entity bubbles = new Entity();
        bubbles.addComponent(new PositionComponent(0, getHeight()));
        bubbles.addControl(new ParticleControl(emitter));

        getGameWorld().addEntity(bubbles);
    }

    //create new method, and return type should have loadTexture(string) method
    private Object getAssetLoader() {
		// TODO Auto-generated method stub
		return null;
	}

	//add new method for getHigh()
    private double getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
    protected void initPhysics() {
        getPhysicsWorld().setGravity(0, 0);

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(BreakoutType.BALL, BreakoutType.BRICK) {
            @Override
            protected void onCollisionBegin(Entity ball, Entity brick) {
                brick.getControl(BrickControl.class).onHit();
            }
        });
    }
	// Create method for getPhysicsWorld(), object have setGravity(), addCollisionHandler() method
    private Object getPhysicsWorld() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
    protected void initUI() {
        Text text = getUIFactory().newText("Level 1", Color.WHITE, 48);
        getGameScene().addUINode(text);

        QuadCurve curve = new QuadCurve(-100, 0, getWidth() / 2, getHeight(), getWidth() + 100, 0);

        PathTransition transition = new PathTransition(Duration.seconds(4), curve, text);
        transition.setOnFinished(e -> {
            getGameScene().removeUINode(text);
            getBallControl().release();
        });
        transition.play();
    }
	//Add new method
	private int getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	//Add new method by TDD. object have addUINode(), removeUINode() method
	private Object getGameScene() {
		// TODO Auto-generated method stub
		return null;
	}

	//Add new method for getUIFactory, object have newText() method
    private Object getUIFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) {
        launch(args);
    }
	//Create new method
	private static void launch(String[] args) {
		// TODO Auto-generated method stub
		
	}
}
