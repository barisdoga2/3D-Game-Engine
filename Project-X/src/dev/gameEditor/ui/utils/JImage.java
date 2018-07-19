package dev.gameEditor.ui.utils;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.swing.JButton;

public class JImage extends JButton{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Dimension dim;
	private BufferedImage bi;
	
	public JImage(Dimension dim) {
		super();
		
		this.dim = dim;
		this.bi = new BufferedImage((int)dim.getWidth(), (int)dim.getHeight(), BufferedImage.TYPE_INT_RGB);
		this.setSize(dim);
		this.setPreferredSize(dim);
		this.setMinimumSize(dim);
		this.setMaximumSize(dim);
	}
	
	public void setImage(BufferedImage bi) {
		this.bi = bi;
		
		BufferedImage after = new BufferedImage((int)dim.getWidth(), (int)dim.getHeight(), BufferedImage.TYPE_INT_ARGB);
		AffineTransform at = new AffineTransform();
		at.scale(dim.getWidth() / bi.getWidth(), dim.getHeight() / bi.getHeight());
		AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		after = scaleOp.filter(bi, after);
		
		this.bi = after;
		this.revalidate();
		this.repaint();
	}
	
	@Override
	public Dimension getPreferredSize() {
		return dim;
	}
	
	@Override
	public Dimension getMaximumSize() {
		return dim;
	}
	
	@Override
	public Dimension getMinimumSize() {
		return dim;
	}
	
	@Override
	public Dimension getSize() {
		return dim;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(bi, 0, 0, this);
	}
	
}
