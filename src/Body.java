
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Body
{
    private double xPos, yPos, mass, xVel, yVel, xFor, yFor;
    private static final double GRAVITY = 6.673e-9;
    private Color c;
    private int radius;
    private int BOTTOM = 690;
    private final int LEFT_WALL = 30;
    private int RIGHT_WALL = 1350;
    private final int TOP = 35;
    private int counter;
    private Shape shape;
    private boolean deleteMe;
    ArrayList<Point2D> been;

    public Body(double xPos, double yPos, double mass, double xVel, double yVel, Color c, Frame fr)
    {
        this.xPos = xPos;
        this.yPos = yPos;
        this.mass = mass;
        this.xVel = xVel;
        this.yVel = yVel;
        this.c = c;//new Color(((int) Math.random() * 256), ((int) Math.random() * 256), ((int) Math.random() * 256));
        this.radius = this.calcRadius(this.mass);
        this.BOTTOM = fr.getHeight() - 5;
        this.RIGHT_WALL = fr.getWidth() - 5;
        been = new ArrayList<Point2D>();
        
        counter = 0;
        deleteMe = false;
    }
    
    public double getAngle()
    {
        double result = ((Math.atan2(yVel, xVel)) / (Math.PI/180));
        
        if(result < 0)
        {
            return 360 + result;
        }
        return result;
    }
    
    public double distTo(Body otherBody)
    {
        double dx = xPos - otherBody.getxPos();
        double dy = yPos - otherBody.getyPos();
        return Math.sqrt((dx * dx) + (dy * dy));
    }
    
    public void drawBody(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(c);
        shape = new Ellipse2D.Float((float)xPos - radius,(float) yPos - radius, radius, radius);
        
        if(been.size() > 1) {
	        for(int i = 0; i < been.size() - 1; i++) {
	        	g2d.draw(new Line2D.Double(been.get(i).getX(), been.get(i).getY(), been.get(i+1).getX(), been.get(i+1).getY()));
	        }
        }
        g2d.fill(shape);
        g2d.dispose();
    }
    
    public double getArea()
    {
        return Math.PI * radius * radius;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Area: %f ", this.getArea()));
        sb.append(String.format("Angle: %f ", this.getAngle()));
        sb.append(String.format("x: %f, y: %f\n", xPos, yPos));
        return sb.toString();
    }
    
    public void update()
    {
        counter++;
        xVel += counter * xFor / mass;
        yVel += counter * yFor / mass;
        
        xPos -= counter * xVel;
        yPos -= counter * yVel;
        
        been.add(new Point2D.Double(xPos, yPos));
    }
    
    public void resetForce()
    {
        xFor = 0;
        yFor = 0;
    }
    
    public void moveTo(double x, double y)
    {
        xFor = x - xPos;
        yFor = y - yPos;
    }
    
    public boolean isOut()
    {
        if(xPos > getRIGHT_WALL() || xPos < getLEFT_WALL() || yPos > getBOTTOM() || yPos < TOP)
        {
            return true;
        }
        return false;
    }
    
    public void addForce(Body otherBody)
    {
        double soften = 143; //143
        double dx = xPos - otherBody.getxPos();
        double dy = yPos - otherBody.getyPos();
        
        double dist = Math.sqrt((dx * dx) + (dy * dy));
        
        double force = (GRAVITY * this.mass * otherBody.getMass()) / (dist + soften * soften);
        
        xFor += (force * dx / dist);
        yFor += force * dy / dist;
    }
    
    public void collide(Body otherBody)
    {   
        this.xVel = (((this.mass * this.xVel) + (otherBody.mass * otherBody.xVel)) / ( this.mass + otherBody.mass));
        this.yVel = (((this.mass * this.yVel) + (otherBody.mass * otherBody.yVel)) / ( this.mass + otherBody.mass));
        
        System.out.printf("Old mass: %.2f Old Radius: %d\n", this.mass, this.radius);
        this.mass += otherBody.mass;
        if(this.radius < 20)
        {
            this.radius = this.calcRadius(this.mass);
        }
        System.out.printf("New mass: %.2f New Radius: %d\n", this.mass, this.radius);
        System.out.printf("xVel: %f yVel: %f\n", this.xVel, this.yVel);
    }
    
    public void deleteThis()
    {
        System.out.println(this.toString());
        this.deleteMe = true;
    }
   
    
    public final int calcRadius(double mass)
    {
        int result = 0;
        
        if(mass < 5000) {
        	result = 1;
        }else if(mass < 100000)
        {
            result = 4;
        }else if(mass < 200000)
        {
            result = 6;
        }else if(mass < 400000)
        {
            result = 7;
        }else if(mass < 600000)
        {
            result = 8;
            setC(Color.GREEN);
        }else if(mass < 800000)
        {
            result = 9;
        }else if(mass < 1000000)
        {
            result = 11;
            
        }else if(mass < 2500000)
        {
            result = 15;
            setC(Color.YELLOW);
        }else if(mass < 7500000)
        {
            result = 16;
        }else if(mass < 10000000)
        {
        
            result = 18;
            setC(Color.ORANGE);
            
        }else if(mass < 20000000)
        {
            result = 22;
            
        }else if(mass < 80000000)
        {
            this.deleteThis();
        }
        return result;
    }

    //<editor-fold defaultstate="collapsed" desc="Getter and setter crap">
    public void setDeleteMe(boolean deleteMe)
    {
        this.deleteMe = deleteMe;
    }
    
    public boolean getDeleteMe()
    {
        return deleteMe;
    }
    
    public void setShape(Shape shape)
    {
        this.shape = shape;
    }
    
    public Shape getShape()
    {
        return this.shape;
    }
    
    public double getxPos()
    {
        return xPos;
    }
    
    public void setxPos(double xPos)
    {
        this.xPos = xPos;
    }
    
    public double getyPos()
    {
        return yPos;
    }
    
    public void setyPos(double yPos)
    {
        this.yPos = yPos;
    }
    
    public double getMass()
    {
        return mass;
    }
    
    public void setMass(double mass)
    {
        this.mass = mass;
    }
    
    public double getxVel()
    {
        return xVel;
    }
    
    public void setxVel(double xVel)
    {
        this.xVel = xVel;
    }
    
    public double getyVel()
    {
        return yVel;
    }
    
    public void setyVel(double yVel)
    {
        this.yVel = yVel;
    }
    
    public double getxFor()
    {
        return xFor;
    }
    
    public void setxFor(double xFor)
    {
        this.xFor = xFor;
    }
    
    public double getyFor()
    {
        return yFor;
    }
    
    public void setyFor(double yFor)
    {
        this.yFor = yFor;
    }
    
    public Color getC()
    {
        return c;
    }
    
    public void setC(Color c)
    {
        this.c = c;
    }
    
    public int getRadius()
    {
        return radius;
    }
    
    public void setRadius(int radius)
    {
        this.radius = radius;
    }
    //</editor-fold>

	public int getLEFT_WALL() {
		return LEFT_WALL;
	}

	public int getRIGHT_WALL() {
		return RIGHT_WALL;
	}

	public int getBOTTOM() {
		return BOTTOM;
	}
}
