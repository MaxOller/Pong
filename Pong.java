import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.AppGameContainer;

public class Pong extends BasicGame 
{
	private float xPos;
	private float yPos;
	
	private float xVelocity;
	private float yVelocity;
	private double velocity;
	
	private float width;
	private float height;

	private int leftScore;
	private int rightScore;
	
	private float leftBarYPos;
	private float leftBarXPos;
	private float leftYMiddle;
	
	private float rightBarYPos;
	private float rightBarXPos;
	private float rightYMiddle;
	
	private float barLengths;
	private float barWidths;
	private float barVelocities;
	
	private double speedMult;
	private double coin;
	private int coinMult;
	
	private Sound paddle;
	private Sound wall;
	private Sound miss;
	
	private float tempXVel;
	private float tempYVel;
	private float tempBarVel;
	
	private static final int SCREEN_WIDTH = 1680;
	private static final int SCREEN_HEIGHT = 1050;
	
	public Pong(String title) 
	{
		super(title);
		this.xVelocity = 10;
		this.yVelocity = (float) (Math.random() * 10);
		this.velocity = abs(yVelocity / xVelocity);
		
		this.width = 20;
		this.height = 20;
		
		this.xPos = SCREEN_WIDTH / 2;
		this.yPos = SCREEN_HEIGHT / 2;
		
		this.barLengths = 150;
		this.barWidths = 15;
		
		this.rightBarXPos = SCREEN_WIDTH - barWidths;
		this.leftBarXPos = 0;
		this.rightBarYPos = 300;
		this.leftBarYPos = 300;
		
		this.rightYMiddle = (rightBarYPos + (barLengths / 2));
		this.leftYMiddle = (leftBarYPos + (barLengths / 2));
		this.barVelocities = 20;
		
		this.leftScore = 0;
		this.rightScore = 0;
		
		this.speedMult = 1;
		this.coinMult = 1;
	}

	@Override
	public void render(GameContainer myGame, Graphics painter) throws SlickException 
	{
		miss = new Sound("res/miss.wav");
		controls(myGame, painter);

		//This renders the game title
		painter.setColor(Color.white);
		painter.drawString("Pong?", SCREEN_WIDTH - 50, 9);
		
		//This renders the y velocity
		painter.setColor(Color.white);
		painter.drawString(String.valueOf("Y Velocity: " + yVelocity), 10, 25);
		
		//This renders the x velocity
		painter.setColor(Color.white);
		painter.drawString(String.valueOf("X Velocity: " + xVelocity), 10, 40);
		
		//This renders the overall velocity
		painter.setColor(Color.white);
		painter.drawString(String.valueOf("Velocity: " + velocity), 10, 55);
		
		//This renders the ball
		painter.setColor(Color.white);
		painter.fillRect(xPos, yPos, width, height);
		
		//This renders the right paddle
		painter.setColor(Color.white);
		painter.fillRect(rightBarXPos, rightBarYPos, barWidths, barLengths);
		
		//This renders the left paddle
		painter.setColor(Color.white);
		painter.fillRect(leftBarXPos, leftBarYPos, barWidths, barLengths);
		
		//This renders the left score
		painter.setColor(Color.white);
		painter.drawString(String.valueOf(leftScore), (float) ((SCREEN_WIDTH / 2) - (SCREEN_WIDTH / 7.4)), 100);
		
		//This renders the right score
		painter.setColor(Color.white);
		painter.drawString(String.valueOf(rightScore), (float) ((SCREEN_WIDTH / 2) +  (SCREEN_WIDTH / 7.4)), 100);
		
		//This renders the line of squares in the middle
		for(int counter = 0; counter <= SCREEN_HEIGHT; counter += height * 1.25)
		{
			painter.setColor(Color.white);
			painter.fillRect((SCREEN_WIDTH / 2), counter, width / 2, height / 2);
		}
	}
	
	//This method resets the game except for the score
	private void reset(Graphics painter) 
	{
		speedMult = 1;
		
		coin();
		xVelocity = 10 * coinMult;
		
		coin();
		yVelocity = (float) (Math.random() * 10) * coinMult;
		
		xPos = SCREEN_WIDTH / 2;
		yPos = SCREEN_HEIGHT / 2;
		
		rightBarXPos = SCREEN_WIDTH - barWidths;
		leftBarXPos = 0;
		rightBarYPos = SCREEN_HEIGHT / 2;
		leftBarYPos = SCREEN_HEIGHT / 2;
		
		painter.setColor(Color.white);
		painter.fillRect(xPos, yPos, width, height);
		
		painter.setColor(Color.white);
		painter.fillRect(rightBarXPos, rightBarYPos, barWidths, barLengths);
		
		painter.setColor(Color.white);
		painter.fillRect(leftBarXPos, leftBarYPos, barWidths, barLengths);
	}
	
	private void controls(GameContainer myGame, Graphics painter)
	{
		Input input = myGame.getInput();
		
		//This resets the square and side bars and adds a point to the left score if the ball passes on the right side and resets speed mult
			if(xPos > SCREEN_WIDTH)
			{
				miss.play();
				leftScore++;
				reset(painter);
			}
			
			//This resets the square and side bars and adds a point to the right score if the ball passes on the left side
			if(xPos < 0)
			{	
				miss.play();
				rightScore++;
				reset(painter);
			}		
		
		//The following if statements control the left and right bar using the W, S and Up, Down keys respectively 
		if(input.isKeyDown(Input.KEY_W) && (leftBarYPos >= 0))
		{
			leftBarYPos -= barVelocities;
		}
		if(input.isKeyDown(Input.KEY_S) && ((leftBarYPos + barLengths) <= SCREEN_HEIGHT))
		{
			leftBarYPos += barVelocities;
		}
		
		if(input.isKeyDown(Input.KEY_UP) && (rightBarYPos >= 0))
		{
			rightBarYPos -= barVelocities;
		}
		if(input.isKeyDown(Input.KEY_DOWN) && ((rightBarYPos + barLengths) <= SCREEN_HEIGHT))
		{
			rightBarYPos += barVelocities;
		}
		
		//This terminates the program if the escape key is pressed
		if(input.isKeyPressed(Input.KEY_ESCAPE))
		{
			System.exit(0);
		}
				
		//This resets the entire game manually
		if(input.isKeyPressed(Input.KEY_R))
		{
			rightScore = 0;
			leftScore = 0;
			reset(painter);
		}
				
		//This pauses and unpauses the game
		if((xVelocity == 0) && (yVelocity == 0) && input.isKeyPressed(Input.KEY_SPACE))
		{
			xVelocity = tempXVel;
			yVelocity = tempYVel;
			barVelocities = tempBarVel;
		}
		else if(input.isKeyPressed(Input.KEY_SPACE))
		{
			tempXVel = xVelocity;
			tempYVel = yVelocity;
			
			xVelocity = 0;
			yVelocity = 0;
			
			tempBarVel = barVelocities;
			barVelocities = 0;
		}
		
		//This enables the bot
		if(input.isKeyDown(Input.KEY_B))
		{
			leftBot();
		}
	}

	//This method ""flips a coin" that decides which side the square to start off with
	public void coin()
	{
		this.coin = Math.random();
		
		if(coin < 0.50)
		{
			coinMult *= -1;
		}
	}
	
	@Override
	public void init(GameContainer myGame) throws SlickException 
	{
		
	}

	@Override
	public void update(GameContainer myGame, int counter) throws SlickException 
	{	
		
		leftBot();
//		rightBot();
		
		//These initialize the sound variables 
		paddle = new Sound("res/paddle.wav");
		wall = new Sound("res/wall.wav");
		
		//This controls the position of the square dependent on the current position and velocity
		xPos = xPos + xVelocity;
		yPos = yPos + yVelocity;
		
		//The following if statements switch the sign of the y velocity of the square if it hits the top of bottom of the window
		if(yPos == SCREEN_HEIGHT - height || yPos > SCREEN_HEIGHT - height) 
		{
			yVelocity *= -1;
			wall.play();
		}
		if(yPos == 0 || yPos < 0) 
		{
			yVelocity *= -1;
			wall.play();
		}
		//The following if statements bounce the square if it hits the hitbox of either bars, adjusts the speed based on whow it hits the paddle, and
		//makes sound if it hits the paddle
		if(xPos == leftBarXPos && (yPos > leftBarYPos - (height / 2)) && (yPos < (leftBarYPos + barLengths))) 
		{
			xVelocity *= -1;
			//This takes the difference of the middle Y Position of the paddle and the position of the ball relative to that, and divides it my the middle of the
			//paddle to calculate the speed multiplier
			speedMult = abs(((leftYMiddle - (leftYMiddle - yPos))) * 2 / leftYMiddle);
			yVelocity *= speedMult;
			paddle.play();
		}
		if(xPos == SCREEN_WIDTH - width && (yPos > rightBarYPos - (height / 2)) && (yPos < (rightBarYPos + barLengths))) 
		{
			xVelocity *= -1;
			//This takes the differe1nce of the middle Y Position of the paddle and the position of the ball relative to that, and divides it my the middle of the
			//paddle to calculate the speed multiplier
			speedMult = abs(((rightYMiddle - (rightYMiddle - yPos))) * 2 / rightYMiddle);
			yVelocity *= speedMult;
			paddle.play();
		}
	}
	
	private void leftBot()
	{
		leftYMiddle = (leftBarYPos + (barLengths / 2));
		
		if(leftYMiddle < yPos && ((leftBarYPos + barLengths) <= SCREEN_HEIGHT))
		{
			leftBarYPos += barVelocities;
		}
		else if(leftYMiddle > yPos && (leftBarYPos >= 0))
		{
			leftBarYPos -= barVelocities;
		}
	}
	
	private void rightBot()
	{
		rightYMiddle = (rightBarYPos + (barLengths / 2));
		
		if(rightYMiddle < yPos && ((rightBarYPos + barLengths) <= SCREEN_HEIGHT))
		{
			rightBarYPos += barVelocities;
		}
		else if(rightYMiddle > yPos && (rightBarYPos >= 0))
		{
			rightBarYPos -= barVelocities;
		}
	}

	private double abs(float f) 
	{
		if(f < 0)
		{
			return f * -1;
		}
		return f;
	}

	public static void main(String[] args) 
	{
		
		try {
			AppGameContainer myGame = new AppGameContainer(new Pong("Pong?"));
			myGame.setDisplayMode(SCREEN_WIDTH,  SCREEN_HEIGHT, false);
			myGame.setTargetFrameRate(60);
			myGame.start();
		}
		catch(SlickException exception)
		{
			System.out.println(exception.toString());
		}

	}

}
