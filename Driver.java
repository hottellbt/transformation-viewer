import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Driver {
	
	private static DrawPanel drawPanel;
	
	private static JTextField fm00, fm01, fm02, fm10, fm11, fm12;
	
	public static void main(String[] args) {
		
		String imagePath;
		if (args.length == 1) {
			imagePath = args[0];
		} else {
			imagePath = System.getProperty("user.dir") + File.separator + "image.png";
		}
		File imageFile = new File(imagePath);
		
		if (!imageFile.isFile()) {
			System.err.println("Could not find " + imageFile
					+ ", please specify an argument or place a file at that location");
			System.exit(1);
			return;
		}
		
		BufferedImage image;
		try {
			image = ImageIO.read(imageFile);
		} catch (IOException e) {
			System.err.println("IOException when loading image: " + imageFile);
			e.printStackTrace(System.err);
			System.exit(1);
			return;
		}
		
		if (image == null) {
			System.err.println("Failed to decode image: " + imageFile);
			System.exit(1);
			return;
		}
		
		drawPanel = new DrawPanel(image);
		drawPanel.setBackground(Color.BLACK);
		
		JFrame frame = new JFrame();
		frame.setTitle("Image Transformer");
		frame.setPreferredSize(new Dimension(800, 600));
		frame.setMinimumSize(new Dimension(200, 200));
		
		frame.setLayout(new BorderLayout());
		
		frame.add(drawPanel, BorderLayout.CENTER);
		frame.add(createToolsPanel(), BorderLayout.WEST);
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
	}

	private static AffineTransform parseMatrix() {
		double m00 = Double.parseDouble(fm00.getText());
		double m01 = Double.parseDouble(fm01.getText());
		double m02 = Double.parseDouble(fm02.getText());
		double m10 = Double.parseDouble(fm10.getText());
		double m11 = Double.parseDouble(fm11.getText());
		double m12 = Double.parseDouble(fm12.getText());

		return new AffineTransform(m00, m10, m01, m11, m02, m12);
	}
	
	private static JPanel createToolsPanel() {
		
		fm00 = new JTextField("1.0");
		fm01 = new JTextField("0.0");
		fm02 = new JTextField("0.0");
		fm10 = new JTextField("0.0");
		fm11 = new JTextField("1.0");
		fm12 = new JTextField("0.0");
		
		JPanel matrixPanel = new JPanel();
		matrixPanel.setLayout(new GridLayout(2, 3));
		matrixPanel.setPreferredSize(new Dimension(250,60));

		matrixPanel.add(fm00);
		matrixPanel.add(fm01);
		matrixPanel.add(fm02);
		matrixPanel.add(fm10);
		matrixPanel.add(fm11);
		matrixPanel.add(fm12);

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.add(createButton("Identity", new ActionIdentity()));
		buttonsPanel.add(createButton("Apply", new ActionApply()));
		buttonsPanel.add(createButton("Reset Image", new ActionReset()));

		JPanel shortcutsPanel = new JPanel();
		shortcutsPanel.setLayout(new BoxLayout(shortcutsPanel, BoxLayout.Y_AXIS));
		shortcutsPanel.add(createButton("Rotate 1 deg.", new ActionRotateRads(Math.toRadians(1))));
		shortcutsPanel.add(createButton("Rotate 10 deg.", new ActionRotateRads(Math.toRadians(10))));
		shortcutsPanel.add(createButton("Rotate 50 deg.", new ActionRotateRads(Math.toRadians(50))));
		shortcutsPanel.add(createButton("Rotate 100 deg.", new ActionRotateRads(Math.toRadians(100))));
		shortcutsPanel.add(createButton("Rotate pi rad.", new ActionRotateRads(Math.PI)));
		shortcutsPanel.add(createButton("Rotate pi/2 rad.", new ActionRotateRads(Math.PI / 2)));

		JPanel toolsPanel = new JPanel();
		toolsPanel.setLayout(new BorderLayout());
		toolsPanel.add(matrixPanel, BorderLayout.NORTH);
		toolsPanel.add(buttonsPanel, BorderLayout.CENTER);
		toolsPanel.add(shortcutsPanel, BorderLayout.SOUTH);

		return toolsPanel;
	}
	
	private static JButton createButton(String text, ActionListener action) {
		JButton btn = new JButton();
		btn.setText(text);
		btn.addActionListener(action);
		return btn;
	}
	
	private static class ActionRotateRads implements ActionListener {

		protected final double theta;

		public ActionRotateRads(double theta) {
			this.theta = theta;
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			double m00 = Math.cos(theta);
			double m01 = -Math.sin(theta);
			double m10 = Math.sin(theta);
			double m11 = Math.cos(theta);

			fm00.setText(String.valueOf(m00));
			fm01.setText(String.valueOf(m01));
			fm02.setText("0.0");
			fm10.setText(String.valueOf(m10));
			fm11.setText(String.valueOf(m11));
			fm12.setText("0.0");
		}
	}

	private static class ActionIdentity implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			fm00.setText("1.0");
			fm01.setText("0.0");
			fm02.setText("0.0");
			fm10.setText("0.0");
			fm11.setText("1.0");
			fm12.setText("0.0");
		}
	}

	private static class ActionApply implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			drawPanel.addTransformation(parseMatrix());
		}
	}
	
	private static class ActionReset implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			drawPanel.setTransformation(null);
		}
	}
}
