
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Space {
	private List safeBodies;
	private double largeX = 0, largeY = 0;
	public Frame frame;

	public Space(Frame fr) {
		this.frame = fr;
		safeBodies = Collections.synchronizedList(new ArrayList<Body>());
		safeBodies.clear();
	}

	public void drawAll(Graphics2D g2d) {
		if (!this.safeBodies.isEmpty()) {
			synchronized (safeBodies) {
				Iterator<Body> it = safeBodies.iterator();
				while (it.hasNext()) {
					Body body = it.next();
					if (!body.getDeleteMe()) {
						body.drawBody(g2d);
						g2d.setColor(Color.white);
						g2d.drawString(String.valueOf(largeX), 10, 10);
						g2d.drawString(String.valueOf(largeY), 10, 30);
					}
				}
			}
		}
	}

	public void addBody(int x, int y) {
		Random rand = new Random();
		synchronized (safeBodies) {
			safeBodies
					.add(new Body(rand.nextInt(frame.getWidth()),
							rand.nextInt(frame.getHeight()), 10000, 0, 0, getRandomColor(), frame));
		}
		//
	}

	public boolean notEmpty() {
		if (safeBodies.isEmpty()) {
			return false;
		}
		return true;
	}

	public void update() {
		if (!this.safeBodies.isEmpty()) {
			synchronized (safeBodies) {
				Iterator<Body> it = safeBodies.iterator();
				while (it.hasNext()) {
					Body body = it.next();
					if (!body.getDeleteMe()) {
						body.update();
						if (this.largeX < body.getxVel()) {
							this.largeX = body.getxVel();
						}
						if (this.largeY < body.getyVel()) {
							this.largeY = body.getyVel();
						}
					}

				}
			}
		}
	}

	public void addForces() {
		synchronized (safeBodies) {
			for (Object a : safeBodies) {
				Body bodyOne = (Body) a;
				if (!bodyOne.getDeleteMe()) {
					for (Object b : safeBodies) {
						Body bodyTwo = (Body) b;
						if (!bodyOne.equals(bodyTwo) && !bodyTwo.getDeleteMe()) {
							bodyOne.addForce(bodyTwo);
						}
					}
				}
			}
		}
	}

	public int getSize() {
		return this.safeBodies.size();
	}

	public void delAll() {
		synchronized (safeBodies) {
			for (int i = 0; i < safeBodies.size(); i++) {
				safeBodies.remove(i);
			}
		}
	}

	public void resetAll() {
		synchronized (safeBodies) {
			for (Object a : safeBodies) {
				Body bodyOne = (Body) a;
				bodyOne.resetForce();
			}
		}
	}

	public void checkCollisionTwo() {
		Body bodyOne, bodyTwo;
		synchronized (safeBodies) {
			Iterator<Body> itOne = safeBodies.iterator();
			Iterator<Body> itTwo = safeBodies.iterator();

			for (int i = 0; i < safeBodies.size(); i++) {
				bodyOne = (Body) safeBodies.get(i);
				if (bodyOne.getxPos() < 0 || bodyOne.getxPos() > bodyOne.getRIGHT_WALL()) {
					bodyOne.setxVel((bodyOne.getxVel() * -0.999));
				}
				if (bodyOne.getyPos() > bodyOne.getBOTTOM() || bodyOne.getyPos() < 0) {
					bodyOne.setyVel((bodyOne.getyVel() * -0.999));
				}
				for (int j = 0; j < safeBodies.size(); j++) {
					bodyTwo = (Body) safeBodies.get(j);
					if (!bodyOne.equals(bodyTwo) && !bodyOne.getDeleteMe()) {
						if ((bodyOne.distTo(bodyTwo) < (bodyOne.getRadius() + bodyTwo.getRadius()) - 1)
								&& bodyOne.getMass() > 0 && bodyTwo.getMass() > 0) {
							if (bodyOne.getMass() > bodyTwo.getMass()) {
								bodyOne.collide(bodyTwo);
								bodyTwo.deleteThis();
							} else {
								bodyTwo.collide(bodyOne);
								bodyOne.deleteThis();
							}
						}
					}
				}
			}

			// while(itOne.hasNext())
			// {
			// bodyOne = itOne.next();
			// while(itTwo.hasNext())
			// {
			// bodyTwo = itTwo.next();
			// if(!bodyOne.equals(bodyTwo))
			// {
			// if((bodyOne.distTo(bodyTwo) < bodyOne.getRadius() +
			// bodyTwo.getRadius())
			// && bodyOne.getMass() > 0
			// && bodyTwo.getMass() > 0)
			// {
			// bodyOne.collide(bodyTwo);
			// bodyOne.deleteThis();
			// }
			// }
			// }
			// }
		}
	}

	public void deleteCrap() {
		synchronized (safeBodies) {
			for (int i = 0; i < safeBodies.size(); i++) {
				Body b = (Body) safeBodies.get(i);
				if (b.getDeleteMe()) {
					safeBodies.remove(i);
				}
			}
		}
	}
	
	public static Color getRandomColor() {
		int random = (int)(Math.random()*12);
		switch(random) {
		case 0:
			return Color.WHITE;
		case 1:
			return Color.BLUE;
		case 2:
			return Color.CYAN;
		case 3:
			return Color.BLUE;
		case 4:
			return Color.GRAY;
		case 5:
			return Color.GREEN;
		case 6:
			return Color.LIGHT_GRAY;
		case 7:
			return Color.MAGENTA;
		case 8:
			return Color.ORANGE;
		case 9:
			return Color.PINK;
		case 10:
			return Color.RED;
		default:
			return Color.YELLOW;
		}
	}
}