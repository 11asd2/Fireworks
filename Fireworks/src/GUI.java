import java.awt.*;
import java.awt.geom.Line2D;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.event.*;
import java.util.ArrayList;

public class GUI extends JFrame {
	private JButton startButton; 
	private JButton exitButton;
	private JSlider windSlider, angleSlider; // sliders for changing wind and angle
	private JLabel windLabel, angleLabel; // labels corresponding to the sliders
	private JLabel instruct; // instruction for user
	private DrawPanel canvas; 
	private Timer timer; 
	private final int delay = 1000/60; // reasonable delay
	ParticleManager manager; 
	ArrayList<Particle> fireworks; // to store the fireworks obtained from the ParticleManager
	private double startTime; // time of launch of first star in tube.

	/* ----------------all private inner classes (including listeners)--------------------- */
	private class DrawPanel extends JPanel {
		public DrawPanel() {
			super();
			setBackground(Color.BLACK);
		}

		public void paint(Graphics g) {
			super.paint(g);
			paintTube(g);
			paintFireworks(g);

		}

		// paints the tube. Take the tube to be a line so it's much easier to animate since you only need 2 points.
		public void paintTube(Graphics g) {
			Graphics2D g2D = (Graphics2D) g;
			g2D.setColor(Color.RED); 
			g2D.setStroke(new BasicStroke(8)); // to make the line thicker, so it appears to be a rectangular tube.
			if(manager!=null) {  
				double[] pos = manager.getTubePosition(); // this is the coordinate at which the tube intersects a unit circle of radius 1, depending on the angle of the tube.
				// we don't want the length of the tube to be only 1, so multiply it by 20 to get a reasonable length while maintaining same angle.
				g2D.drawLine(getWidth() / 2, getHeight(), getWidth() / 2 + (int) (pos[0] * 20),  getHeight() - (int) (pos[1] * 20));
			}
			else  // default start-up position for tube - although the tube isn't "ALIVE" until start has been pressed.
				g2D.drawLine(getWidth() / 2, getHeight(), getWidth() / 2, getHeight() - 20); // pick reasonable dimensions for tube in starting position.
		}

		public void paintFireworks(Graphics g) {
			int renderSize, radius;
			double x, y; // in meters
			int xPix, yPix; // in pixels
			Color color;
			if(fireworks != null) { // only draw particles if fireworks array isn't empty, otherwise maintain black background only
				for(Particle firework : fireworks) {
					renderSize = firework.getRenderSize();
					x =  firework.getPosition()[0];
					y =  firework.getPosition()[1]; 
					xPix = (int) (x * getHeight() / 22.0); // scaling to panel size
					yPix = (int) (y * getHeight() / 22.0); // scaling to panel size
					color = translateColour(firework.getColour());
					g.setColor(color);
					xPix = getWidth() / 2 + xPix ; // shift the origin of x to the middle of panel
					yPix = getHeight() - yPix; // flip the y - axis 
					radius = renderSize / 2;
					g.fillOval(xPix - radius, yPix - radius, renderSize, renderSize); // draw oval so that it is centered at the coordinate of interest.
				}
			}
		}
	}

	// starting a new animation from the beginning.
	private class StartListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(timer != null)  // stop timer if running (so that we can start animation from start)
				timer.stop();
			try {
				double wind = windSlider.getValue(); 
				double angle = angleSlider.getValue(); 
				manager = new ParticleManager(wind, angle); // instantiate particle manager using slider values.
			} catch (EnvironmentException | EmitterException e1) {
				e1.getMessage();
			}  
			if(timer == null) // if Start button being clicked for first time, instantiate the Timer object.
				timer = new Timer(delay, new TimerListener());
			startTime = System.currentTimeMillis(); 
			manager.start(0); 
			timer.start(); 
			instruct.setText("Use sliders to adjust wind speed and launch angle!"); // can alter the wind speed and angle during a running animation
			startButton.setText("Restart"); // same button can be used to restart animation from beginning
		}
	}

	private class ExitListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(timer!=null)
				timer.stop(); // stop timer and exit program
			System.exit(0); 	
		}
	}

	private class SliderListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			int angle = angleSlider.getValue();
			int wind = windSlider.getValue();
			angleLabel.setText("Launch angle (degrees):" + angle);
			windLabel.setText("Wind (km/h) :" + wind);
			if(manager != null) // only if there exists a particle manager through which an animation is already running.
				if (!angleSlider.getValueIsAdjusting() && !windSlider.getValueIsAdjusting()) {
					manager.tiltTube(angle); // change angle of tube and wind (of environment) during mid-animation.
					manager.changeWind(wind);
				}
		}
	}

	private class TimerListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			double time = (System.currentTimeMillis() - startTime) / 1000; // convert to seconds
			fireworks = manager.getFireworks(time);
			canvas.repaint();      
			if(fireworks.size() == 0) 
				timer.stop(); // stop the timer after fireworks has become empty, because no point in continuing timer.
		}
	}

	/*----------------------------------------------------------------------------------------------*/

	/* ------- Constructor and methods for GUI construction ------------*/

	// simply building the user control part and the drawing panel part of the window.
	public GUI() {
		buildUserControl();
		buildDrawPanel();	
		setSize(800,600);
		getContentPane().setBackground(Color.BLACK);
		setTitle("Roman Candle Simulation: By Aranjit Daid");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void  buildUserControl() {
		// basically the South portion of the window's border layout is where we will add most of the components
		JPanel bottomPanel = new JPanel(new BorderLayout()); // create a panel in which we will ultimately put two sliders, two labels, and a Start and Exit button
		startButton = new JButton("Start"); 
		startButton.addActionListener(new StartListener());
		exitButton = new JButton("Exit");
		exitButton.addActionListener(new ExitListener());
		bottomPanel.add(startButton, BorderLayout.WEST); // add start button to the WEST side of the panel
		bottomPanel.add(exitButton, BorderLayout.EAST); // add exit button to the EAST side of the panel

		// in the empty middle portion of the above bottomPanel, we will now add another panel of GridLayout layout.
		// it will contain the two sliders and two labels, thus 4 components in total.
		JPanel bottomMiddle = new JPanel(new GridLayout(1,4));
		windSlider = new JSlider(JSlider.HORIZONTAL, -20, 20, 0); // create slider with limits on wind speed range
		windSlider.setMajorTickSpacing(5); 
		windSlider.setPaintTicks(true);
		windSlider.addChangeListener(new SliderListener());
		windLabel = new JLabel("Wind (km/h) : 0") ; // label for the slider
		windLabel.setHorizontalAlignment(JLabel.RIGHT); 
		angleSlider = new JSlider(JSlider.HORIZONTAL, -15, 15, 0); // create slider with limits on launch angle
		angleSlider.setMajorTickSpacing(5);
		angleSlider.setPaintTicks(true);
		angleSlider.addChangeListener(new SliderListener());
		angleLabel = new JLabel("Launch angle (degrees): 0"); // label for the slider
		angleLabel.setHorizontalAlignment(JLabel.RIGHT);

		// add all four components to the panel in this order
		bottomMiddle.add(windLabel);
		bottomMiddle.add(windSlider);
		bottomMiddle.add(angleLabel);
		bottomMiddle.add(angleSlider);
		// attach bottomMiddle to bottomPanel, and bottomPanel to main window as planned.
		bottomPanel.add(bottomMiddle, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH); // add the entire bottomPanel to the South region of the main window.

		// a brief instruction for the user; will add directly to main window.
		instruct = new JLabel("Click start to begin the animation");
		Font font = new Font("Arial", Font.PLAIN, 20);
		instruct.setFont(font);
		instruct.setForeground(Color.WHITE);
		instruct.setHorizontalAlignment(JLabel.CENTER);
		add(instruct, BorderLayout.NORTH);
	}

	private void buildDrawPanel() {
		canvas = new DrawPanel();
		add(canvas, BorderLayout.CENTER); // our drawing area will be the center of the window.
	}

	// for String to Color translation
	private Color translateColour(String colour) {
		Color returnColour = Color.BLACK;
		switch (colour.toLowerCase()) {
		case "blue" :
			returnColour = Color.BLUE;
			break;
		case "green" :
			returnColour = Color.GREEN;
			break;
		case "orange" :
			returnColour = Color.ORANGE;
			break;
		case "red" :
			returnColour = Color.RED;
			break;
		case "yellow" :
			returnColour = Color.YELLOW;
			break;
		case "white" :
			returnColour = Color.WHITE;
			break;
		case "cyan" :
			returnColour = Color.CYAN;
			break;
		case "magenta" :
			returnColour = Color.MAGENTA;
		}
		return returnColour;
	}

}