import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.Objects;

import javax.swing.JPanel;

public class DrawPanel extends JPanel {
	
	protected BufferedImage image;
	protected ImageObserver imageObserver;
	protected AffineTransform transform;
	
	public DrawPanel() {
		super();
	}
	
	public DrawPanel(BufferedImage image) {
		setImage(image);
	}
	
	public void setImage(BufferedImage image) {
		this.image = Objects.requireNonNull(image, "image");
	}
	
	public void setImageObserver(ImageObserver imageObserver) {
		this.imageObserver = imageObserver;
	}
	
	public void setTransformation(AffineTransform transform) {
		this.transform = transform;
		repaint();
	}
	
	public AffineTransform getTransform() {
		if (transform != null) {
			return (AffineTransform) transform.clone();
		} else {
			return new AffineTransform();
		}
	}
	
	public void addTransformation(AffineTransform add) {
		if (add == null) {
			return;
		}
		if (transform == null) {
			setTransformation((AffineTransform) add.clone());
			return;
		}
		// add.concatenate(transform);
		// setTransformation(add);
		transform.concatenate(add);
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		
		Graphics2D g = (Graphics2D) graphics;
		
		int pw = this.getWidth();
		int ph = this.getHeight();
		
		int iw = image.getWidth();
		int ih = image.getHeight();
		
		double mx = pw / 2 - iw / 2 + 20.0;
		double my = ph / 2 - ih / 2;
		
		// moves the image so that way it's center is on the origin
		// see https://docs.oracle.com/javase/tutorial/2d/advanced/transforming.html

		AffineTransform at = new AffineTransform();
		at.translate((iw / 2), (ih / 2));
		at.translate(mx, my);
		at.concatenate(getTransform());
		at.translate(-(iw / 2), -(ih / 2));

		g.transform(at);

		graphics.drawImage(image, 0, 0, imageObserver);
	}
}
