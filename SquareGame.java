package squaregame;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;

public class SquareGame implements ActionListener, MouseListener, KeyListener {
	
	public static SquareGame squaregame;
	
	public final int WIDTH = 1200, HEIGHT = 800;
	
	public final double GRAVITY = 0.9;
	
	public Renderer renderer;
	
	public Rectangle bird;
		
	
	public int ticks, yMotion, score, highScore = 0, counter=1, speed = 10;
	
	public boolean gameOver, started, upAccel, downAccel;
	
	public ArrayList<Rectangle> columns;
	
	public Random rand;
	

	public SquareGame() {
		
		JFrame jframe = new JFrame();
		Timer timer = new Timer(20,this);
		
		renderer = new Renderer();
		rand = new Random();
		
		
		jframe.add(renderer);
		jframe.setTitle("Square Game");
		jframe.setSize(WIDTH,HEIGHT);
		jframe.addMouseListener(this);
		jframe.addKeyListener(this);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setResizable(false);
		jframe.setVisible(true);
		
		bird = new Rectangle(WIDTH/2 - 40, HEIGHT/2 - 40, 80, 80);
		
		columns =new ArrayList<Rectangle>();
		

		   
	   
	

 
		
		addColumn(true);	
		addColumn(true);	
		addColumn(true);
		addColumn(true);	
		
		timer.start();
	
	}
	
	public void addColumn(boolean start) {
		
		int space=300;
		int width = 100;
		int height = 50 + rand.nextInt(300);
		
		if (start) 
		{
			columns.add(new Rectangle(WIDTH + width + columns.size()*300 + 200, HEIGHT - height, width, height));
			columns.add(new Rectangle(WIDTH + width + (columns.size()-1)*300 +100*counter,0, width, HEIGHT - height - space));
			
			columns.add(new Rectangle(columns.get(columns.size()-1).x+rand.nextInt(200), rand.nextInt(HEIGHT), 30, 30));
			columns.add(new Rectangle(columns.get(columns.size()-1).x+rand.nextInt(200), rand.nextInt(HEIGHT), 30, 30));
			
			counter= 2*rand.nextInt(2)-1;
			
			
		}
		else 
		{
			columns.add(new Rectangle(columns.get(columns.size()-1).x +600, HEIGHT - height, width, height));
			columns.add(new Rectangle(columns.get(columns.size()-1).x+100*counter, 0, width, HEIGHT - height - space));
			
			
			columns.add(new Rectangle(columns.get(columns.size()-1).x+rand.nextInt(200), rand.nextInt(HEIGHT), 30, 30));
			columns.add(new Rectangle(columns.get(columns.size()-1).x+rand.nextInt(200), rand.nextInt(HEIGHT), 30, 30));
			
			counter= 2*rand.nextInt(2)-1;
		}
	}
	
	public void paintColumn(Graphics g, Rectangle column) {
		if(column.height !=30) {
		g.setColor(new Color(120, 173, 122));
		g.fillRect(column.x, column.y, column.width, column.height);
		}
		else {
			g.setColor(new Color(175, 91, 93));
			g.fillRect(column.x, column.y, column.width, column.height);
		}
	}
	public void move() {

		if(!gameOver) {
		if(upAccel) {
			yMotion -=3;
		}
		else if (downAccel){
			yMotion +=3;
		}
		else if (!upAccel && !downAccel) {
			yMotion *=GRAVITY;
		}
		
		if(yMotion>=6) {
			yMotion=6;
		}
		else if(yMotion<=-6) {
			yMotion=-6;
		}
		bird.y +=yMotion;
		
		if (bird.y<0) {
			bird.y=0;
		}
		if (bird.y>HEIGHT-bird.height) {
			bird.y=HEIGHT-bird.height;
			
		}
		}
	
		
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		//speed=10;
		
		ticks++;
		
		if(started) {
			
			
			
			for(int i =0; i<columns.size(); i++) {
				Rectangle column = columns.get(i);
				column.x -= speed;
			}
			
			
			for(int i =0; i<columns.size(); i++) {
				Rectangle column = columns.get(i);
				
				if (column.x +column.width <0) {
					columns.remove(column);
					
					
					if (column.y==0) {
						addColumn(false);
					}
				}
			}
			
			bird.y += yMotion;
			
			for(Rectangle column : columns) {
				if(column.y ==0 &&bird.x + bird.width/2> column.x +column.width/2 -5 && bird.x + bird.width/2 < column.x +column.width/2 +5 ) {
					//score++;
				}
				
				if (column.intersects(bird)) {
					if(column.height!=30) {
					gameOver = true;
					if(score>highScore)
					{
						highScore =score;
					}
					
					if(bird.x <= column.x)
					{
					bird.x = column.x - bird.width;
					}
					else
					{
						if(column.y!=0)
						{
							bird.y = column.y - bird.height;
						}
						else if (bird.y< column.height)
						{
							bird.y = column.height;
						}
					}
				}
					else if(column.height==30) {
						score++;
						if (score%5 ==0) {
							speed+=score/10;
							actionPerformed(e);
						}
						column.height= 0;
					}
				}

				if(bird.y + yMotion >= HEIGHT ) 
				{
					bird.y = HEIGHT  - bird.height;
				}
			}
		}
			
		renderer.repaint();
	}
	

	public void repaint(Graphics g) {
		
		g.setColor(new Color(159, 219, 254));
		g.fillRect(0,0,WIDTH, HEIGHT);
		
		g.setColor(new Color(35, 64, 96));
		g.fillRect(bird.x, bird.y, bird.width, bird.height);
		
		
		
		for(Rectangle column : columns) {
			paintColumn(g, column);
		}
		
		g.setColor(Color.white);
		g.setFont(new Font("Arial", 1, 100));
		
		if (!started) {
			g.drawString("Click to start!", 75, HEIGHT/2 -50);
		}
		
		if (gameOver) {
			g.drawString("Game Over!", 100, HEIGHT/2 -50);
			g.setFont(new Font("Arial", 1, 100));
			g.drawString(String.valueOf(score), WIDTH/2 - 25, 100);
			
			if (score == highScore)
			{
				g.setFont(new Font("Arial", 1, 60));
				g.drawString("New High Score!", 50, 650);	
			}

		}
		
		if (!gameOver && started) {
			g.setFont(new Font("Arial", 1, 100));
			g.drawString(String.valueOf(score), WIDTH/2 - 25, 100);
			g.setFont(new Font("Arial", 1, 60));
			g.drawString("High Score: " + String.valueOf(highScore), 50, 650);
		}
	}
	
	
	public static void main(String[] args) {
		
		squaregame = new SquareGame();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(gameOver) {
			
			speed=10;
			
			bird = new Rectangle(WIDTH/2 - 40, HEIGHT/2 - 60, 80, 120);
			columns.clear();
			yMotion =0;
			score = 0;
			
			addColumn(true);	
			addColumn(true);	
			addColumn(true);	
			addColumn(true);	
			
			
			gameOver = false;
		}
		if(!started) {
			started = true;
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()== KeyEvent.VK_UP) {
			upAccel = true;
			move();
		}
		if(e.getKeyCode()== KeyEvent.VK_DOWN) {
			downAccel = true;
			move();
		}
		if(e.getKeyCode()==KeyEvent.VK_SPACE) {
			if(gameOver) {
			
				speed=10;
				bird = new Rectangle(WIDTH/2 - 40, HEIGHT/2 - 40, 80, 180);
				columns.clear();
				yMotion =0;
				score = 0;
			
				addColumn(true);	
				addColumn(true);	
				addColumn(true);	
				addColumn(true);	
				
				
			
				gameOver = false;
		}
			if(!started) {
				started = true;
		}
		}

	}
		
	

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode()== KeyEvent.VK_UP) {
			upAccel = false;
		}
		if(e.getKeyCode()== KeyEvent.VK_DOWN) {
			downAccel = false;
		}
		
	}


}
