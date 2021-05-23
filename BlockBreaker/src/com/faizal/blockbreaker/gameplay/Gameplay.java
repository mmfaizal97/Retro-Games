package com.faizal.blockbreaker.gameplay;

import com.faizal.blockbreaker.map.MapGenerator;
import javafx.scene.shape.Circle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Gameplay extends JPanel implements KeyListener, ActionListener {

    private static final int PLAYER_MAX_LEFT_EXTENT = 10;
    private static final int PLAYER_MAX_RIGHT_EXTENT = 590;
    private static final int PLAYER_SINGLE_CLICK_MOVEMENT = 20;
    private static final int BALL_DIAMETER = 20;
    private static final int PLAYER_INITIAL_POSITION = 300;
    private static final int PLAYER_INITIAL_SCORE = 0;
    private static final int MAP_BRICK_ROWS = 4;
    private static final int MAP_BRICK_COLUMNS = 12;
    private static final int MAP_TOTAL_BRICKS = MAP_BRICK_ROWS * MAP_BRICK_COLUMNS;
    private static final int BALL_INITIAL_POSITION_X = 340;
    private static final int BALL_INITIAL_POSITION_Y = 530;
    private static final int BALL_INITIAL_SPEED_X = 0;
    private static final int BALL_INITIAL_SPEED_Y = -2;

    private MapGenerator map;
    private final Timer timer;
    private int score;
    private int totalBricks;
    private int playerPositionX;
    private int ballPositionX;
    private int ballPositionY;
    private int ballXDirectionSpeed;
    private int ballYDirectionSpeed;
    private boolean play;

    public Gameplay() {
        map = new MapGenerator(MAP_BRICK_ROWS, MAP_BRICK_COLUMNS);
        int delay = 8;
        timer = new Timer(delay, this);
        score = PLAYER_INITIAL_SCORE;
        totalBricks = MAP_TOTAL_BRICKS;
        playerPositionX = PLAYER_INITIAL_POSITION;
        ballPositionX = BALL_INITIAL_POSITION_X;
        ballPositionY = BALL_INITIAL_POSITION_Y - 1;
        ballXDirectionSpeed = BALL_INITIAL_SPEED_X;
        ballYDirectionSpeed = BALL_INITIAL_SPEED_Y;
        play = false;
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer.start();
    }

    public void paint(Graphics g) {
        // background
        g.setColor(Color.BLACK);
        g.fillRect(1, 1, 692, 592);

        // drawing map
        map.draw((Graphics2D) g);

        // borders
        g.setColor(Color.YELLOW);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);
        g.fillRect(0, 568, 692, 3);

        // the scores
        g.setColor(Color.WHITE);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("Score : " + score, 570, 30);
        //g.drawString("Time : " + timer.toString(), 30, 30);

        // the paddle
        g.setColor(Color.GREEN);
        g.fillRect(playerPositionX, 550, 100, 8);

        // the ball
        g.setColor(Color.YELLOW);
        g.fillOval(ballPositionX, ballPositionY, BALL_DIAMETER, BALL_DIAMETER);

        // when you won the game
        if (totalBricks <= 0) {
            play = false;
            ballXDirectionSpeed = 0;
            ballYDirectionSpeed = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("You Won", 260, 300);

            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press (Enter) to Restart", 230, 350);
        }

        // when you lose the game
        if (ballPositionY > 570) {
            play = false;
            ballXDirectionSpeed = 0;
            ballYDirectionSpeed = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Over, Score: " + score, 190, 300);

            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press (Enter) to Restart", 230, 350);
        }

        g.dispose();
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerPositionX >= PLAYER_MAX_RIGHT_EXTENT) {
                playerPositionX = PLAYER_MAX_RIGHT_EXTENT;
            } else {
                moveRight();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerPositionX < PLAYER_MAX_LEFT_EXTENT) {
                playerPositionX = PLAYER_MAX_LEFT_EXTENT;
            } else {
                moveLeft();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!play) {
                play = true;
                ballPositionX = BALL_INITIAL_POSITION_X;
                ballPositionY = BALL_INITIAL_POSITION_Y;
                ballXDirectionSpeed = BALL_INITIAL_SPEED_X;
                ballYDirectionSpeed = BALL_INITIAL_SPEED_Y;
                playerPositionX = PLAYER_INITIAL_POSITION;
                score = PLAYER_INITIAL_SCORE;
                totalBricks = MAP_TOTAL_BRICKS;
                map = new MapGenerator(MAP_BRICK_ROWS, MAP_BRICK_COLUMNS);

                repaint();
            }
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void moveRight() {
        play = true;
        if (playerPositionX + PLAYER_SINGLE_CLICK_MOVEMENT >= PLAYER_MAX_RIGHT_EXTENT)
            playerPositionX = PLAYER_MAX_RIGHT_EXTENT;
        else
            playerPositionX += PLAYER_SINGLE_CLICK_MOVEMENT;
    }

    public void moveLeft() {
        play = true;
        if (playerPositionX - PLAYER_SINGLE_CLICK_MOVEMENT <= PLAYER_MAX_LEFT_EXTENT)
            playerPositionX = PLAYER_MAX_LEFT_EXTENT;
        else
            playerPositionX -= PLAYER_SINGLE_CLICK_MOVEMENT;
    }

    boolean intersects(Circle circle, Rectangle rect) {
        double circleDistance_x = Math.abs(circle.getCenterX() - (rect.x + rect.width / 2.0));
        double circleDistance_y = Math.abs(circle.getCenterY() - (rect.y + rect.height / 2.0));
        if (circleDistance_x > (rect.width / 2.0 + circle.getRadius())) {
            return false;
        } else if (circleDistance_y > (rect.height / 2.0 + circle.getRadius())) {
            return false;
        } else if (circleDistance_x <= (rect.width / 2.0)) {
            return true;
        } else if (circleDistance_y <= (rect.height / 2.0)) {
            return true;
        }
        double cornerDistance_sq = Math.pow(circleDistance_x - rect.width / 2.0, 2) + Math.pow(circleDistance_y - rect.height / 2.0, 2);
        return (cornerDistance_sq <= Math.pow(circle.getRadius(), 2));
    }

    public void actionPerformed(ActionEvent e) {
        timer.start();
        if (play) {
            Circle ballCircle = new Circle(ballPositionX + BALL_DIAMETER / 2.0, ballPositionY + BALL_DIAMETER / 2.0, BALL_DIAMETER / 2.0);
            if (intersects(ballCircle, new Rectangle(playerPositionX, 550, 10, 8))) {
                ballYDirectionSpeed = -ballYDirectionSpeed;
                ballXDirectionSpeed = ballXDirectionSpeed < 0 ? ballXDirectionSpeed : -ballXDirectionSpeed - 1;
            } else if (intersects(ballCircle, new Rectangle(playerPositionX + 90, 550, 10, 8))) {
                ballYDirectionSpeed = -ballYDirectionSpeed;
                ballXDirectionSpeed = ballXDirectionSpeed > 0 ? ballXDirectionSpeed : ballXDirectionSpeed + 1;
            } else if (intersects(ballCircle, new Rectangle(playerPositionX + 10, 550, 80, 8))) {
                ballYDirectionSpeed = -ballYDirectionSpeed;
            }

            // check map collision with the ball
            A:
            for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        //scores++;
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        ballCircle = new Circle(ballPositionX + BALL_DIAMETER / 2.0, ballPositionY + BALL_DIAMETER / 2.0, BALL_DIAMETER / 2.0);

                        if (intersects(ballCircle, rect)) {
                            map.setBrickValue(0, i, j);
                            score += 5;
                            if (score % 20 == 0) {
                                ballXDirectionSpeed = ballXDirectionSpeed >= 0 ? ballXDirectionSpeed + 1 : ballXDirectionSpeed - 1;
                                ballYDirectionSpeed = ballYDirectionSpeed >= 0 ? ballYDirectionSpeed + 1 : ballYDirectionSpeed - 1;
                            }
                            totalBricks--;

                            // when ball hit right or left of brick
                            if (ballPositionX + 19 <= rect.x || ballPositionX + 1 >= rect.x + rect.width) {
                                ballXDirectionSpeed = -ballXDirectionSpeed;
                            }
                            // when ball hits top or bottom of brick
                            else {
                                ballYDirectionSpeed = -ballYDirectionSpeed;
                            }

                            break A;
                        }
                    }
                }
            }

            ballPositionX += ballXDirectionSpeed;
            ballPositionY += ballYDirectionSpeed;

            if (ballPositionX < 0) {
                ballXDirectionSpeed = -ballXDirectionSpeed;
            }
            if (ballPositionY < 0) {
                ballYDirectionSpeed = -ballYDirectionSpeed;
            }
            if (ballPositionX > 670) {
                ballXDirectionSpeed = -ballXDirectionSpeed;
            }

            repaint();
        }
    }
}
