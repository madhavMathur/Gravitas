
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.util.Random;

import javax.swing.JFrame;

public class Frame extends JFrame {

	private Canvas canvas;
	private BufferStrategy buffer;
	private Body b;
	private Space space;
	private boolean switcher;
	private int i = 0;

	public Frame() {
		super();
		switcher = false;
		space = new Space(this);
		final Random rand = new Random();
		this.setTitle("Gravitas | Alpha 1.3 | Madhav Mathur");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(1350, 700));
		this.setResizable(false);
		this.setIgnoreRepaint(true);

		canvas = new Canvas();

		this.getContentPane().add(canvas);
		this.setLocationRelativeTo(null);

		canvas = new Canvas();
		canvas.setIgnoreRepaint(true);
		canvas.setBackground(Color.white);
		this.getContentPane().add(canvas);
		this.setVisible(true);

		canvas.createBufferStrategy(2);
		buffer = canvas.getBufferStrategy();

		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == 1) {
					for (int j = 0; j < 100; j++) {
						space.addBody(0, 0);
					}
				} else if (e.getButton() == 3) {
					if (switcher) {
						switcher = false;
					} else {
						switcher = true;
						i = 0;
					}
					System.out.println(switcher);
				} else {
					space.delAll();
					System.out.println(space.getSize());
				}
			}
		});
	}

	public void render() {
		do {
			do {
				Graphics2D g2d = (Graphics2D) buffer.getDrawGraphics();
				g2d.setColor(Color.BLACK);
				g2d.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
				renderParticles(g2d);
				g2d.dispose();
			} while (buffer.contentsRestored());
			buffer.show();
		} while (buffer.contentsLost());

	}

	public void renderParticles(Graphics2D g2d) {
		if (space.notEmpty()) {
			space.drawAll(g2d);
		}
	}

	public void loop() {

		while (true) {

			if (switcher) {
				space.addForces();
			}
			space.update();
			render();

			space.resetAll();
			space.checkCollisionTwo();
			try {
				Thread.sleep(1000 / 60);
			} catch (InterruptedException e) {
				System.out.println("Interrupted Exception " + e);
			}
			space.deleteCrap();
		}
	}
}
