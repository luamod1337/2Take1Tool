package me.zero.twotakeonetool.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

import javax.swing.JComponent;

import me.zero.twotakeonetool.controller.SideBarEntryMouseListener;
import me.zero.twotakeonetool.type.SideBarEntryType;

public class JSideBarEntry extends JComponent{
	
	private static final long serialVersionUID = 1L;
	private String text;
	private int x;
	private int y;
	private BufferedImage image;
	private JSideBar parent;
	private SideBarEntryMouseListener listener;
	private int width = 170;
	private int height = 25;
	private SideBarEntryType type; 
	
	private Consumer<JSideBarEntry> function;
	private TwoTakeOnePackView pack;
	private boolean enable = true;
	
		
	public JSideBarEntry(String text,BufferedImage image,JSideBar parent,Consumer<JSideBarEntry> function,SideBarEntryType type) {
		super();
		this.text = text;
		this.image = image;
		this.parent = parent;
		this.function = function;
		this.type = type;
		listener = new SideBarEntryMouseListener(this, parent);
		register();
	}
	public JSideBarEntry(String text,BufferedImage image,JSideBar parent,Consumer<JSideBarEntry> function,SideBarEntryType type,TwoTakeOnePackView pack) {
		super();
		this.text = text;
		this.image = image;
		this.parent = parent;
		this.function = function;
		this.type = type;
		listener = new SideBarEntryMouseListener(this, parent);
		this.pack = pack;
		register();
	}

	public void paint(Graphics g,int x,int y) {
		this.x = x;
		this.y = y;
		this.paint(g);
	}
	
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D)g;
		if(parent.isOpen()) {
			g2.drawImage(image, x, y-20, null);
			g2.drawString(text, x+30, y);
		}else {
			g2.drawImage(image, x, y-20, null);
		}
	}

	public String getText() {
		return text;
	}

	public SideBarEntryMouseListener getListener() {
		return listener;
	}
	@Override
	public int getHeight() {
		return this.height;
	}
	@Override
	public int getWidth() {
		return this.width;
	}
	
	@Override
	public int getX() {
		return this.x;
	}
	@Override
	public int getY() {
		return this.y-20;
	}
	public void onClick() {
		if(function != null && enable) {
			function.accept(this);
		}
	}
	public SideBarEntryType getType() {
		return type;
	}
	public TwoTakeOnePackView getPack() {
		return pack;
	}
	public void close() {
		TwoTakeOneToolGui.instance.removeMouseListener(listener);
	}
	public void setEnabled(boolean enabled) {
		this.enable = enabled;
	}
	@Override
	public boolean isEnabled() {
		return this.enable;
	}
	public void register() {
		TwoTakeOneToolGui.instance.addMouseListener(listener);
	}
	public BufferedImage getImage() {
		return image;
	}
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	public Consumer<JSideBarEntry> getFunction() {
		return function;
	}
	public void setFunction(Consumer<JSideBarEntry> function) {
		this.function = function;
	}
	public void setText(String text) {
		this.text = text;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	public void setType(SideBarEntryType type) {
		this.type = type;
	}
}
