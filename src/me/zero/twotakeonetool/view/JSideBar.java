package me.zero.twotakeonetool.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

public class JSideBar extends JComponent{

	private static final long serialVersionUID = 1L;
	private boolean isOpen = true;
	
	private int height = 100;
	private BufferedImage button_control_sidebar = null;
	private ArrayList<JSideBarEntry> entrys = new ArrayList<>();
	private TwoTakeOneToolGui parent;
	private JSideBarEntry selectedEntry;
	
	private int entryYStart = 0;
	private int entryYEnd = 0;
	private int widthOpen = 200;
	private int widthClosed = 52;
	
	public JSideBar(int height,TwoTakeOneToolGui parent) {
		this.height = height;		
		this.parent = parent;
		
		this.setBackground(Color.BLUE);
		try {
			button_control_sidebar = ImageIO.read(getClass().getResource("/ressources/images/menu.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setColor(new Color(46,48,62));
		if(this.isOpen) {
			g2.fillRect(0, 0, this.widthOpen, this.height);
			g2.setColor(new Color(41,44,56));
			g2.drawRect(45, 20, button_control_sidebar.getWidth() + 10, button_control_sidebar.getHeight() + 10);
			g2.drawImage(button_control_sidebar, 50, 25, null);			
		}else {
			g2.fillRect(0, 0, this.widthClosed, this.height);
			g2.drawImage(button_control_sidebar, 1, 25, null);
		}		
		
		g2.setColor(new Color(126,86,194));
				
		g2.setFont(new Font("Arial",Font.PLAIN,20));
		entryYStart = 80;
		for(int i = 0; i < entrys.size();i++) {
			JSideBarEntry e = entrys.get(i);
			if(e.equals(this.getSelectedEntry())) {
				g2.setColor(new Color(57,61,79));
				g2.fillRect(2, 130 + (i*40)-22, this.getWidth()-10, 30);
				g2.setColor(new Color(126,86,194));
			}else if(this.isOpen) {
				g2.setColor(new Color(41,44,56));
				g2.drawRect(2, 130 + (i*40)-22, this.getWidth()-10, 30);
				g2.setColor(new Color(126,86,194));
			}else {
				g2.setColor(new Color(41,44,56));
				g2.drawRect(2, 130 + (i*40)-22, this.getWidth()-20, 30);
				g2.setColor(new Color(126,86,194));
			}			
			e.paint(g,2,130 + (i*40));
		}
		entryYEnd = 100 + (entrys.size()*40);
		g2.setColor(new Color(41,44,56));
		g2.fillRect(0, parent.getHeight()-100, this.getWidth(), height);
		g2.setColor(new Color(126,86,194));
		if(this.isOpen) {
			g2.drawString("2Take1Tool by", 25, parent.getHeight()-72);
			g2.drawString("1337Zero", 45, parent.getHeight()-52);
		}
	}
	
	public void setOpened(boolean opened) {
		this.isOpen = opened;
		parent.repaint();
	}
	public boolean isOpen() {
		return this.isOpen;
	}
	public void addEntry(JSideBarEntry e) {
		this.entrys.add(e);
		parent.repaint();
	}
	
	@Override
	public int getWidth() {
		if(this.isOpen) {
			return this.widthOpen;
		}
		return this.widthClosed;
	}

	public int getEntryYStart() {
		return entryYStart;
	}

	public int getEntryYEnd() {
		return entryYEnd;
	}
	
	public void mouseClicked(MouseEvent e) {
		for(JSideBarEntry entry : entrys) {
			entry.getListener().mouseClicked(e);
		}
	}

	public void mouseEntered(MouseEvent e) {
		for(JSideBarEntry entry : entrys) {
			entry.getListener().mouseEntered(e);
		}
	}

	public void mouseExited(MouseEvent e) {
		for(JSideBarEntry entry : entrys) {
			entry.getListener().mouseExited(e);
		}
	}

	public void mousePressed(MouseEvent e) {
		for(JSideBarEntry entry : entrys) {
			entry.getListener().mousePressed(e);
		}		
	}

	public void mouseReleased(MouseEvent e) {
		for(JSideBarEntry entry : entrys) {
			entry.getListener().mouseReleased(e);
		}
	}

	public void setSelectedEntry(JSideBarEntry entry) {
		if(entry.getText().length() > 0 && !entry.equals(selectedEntry)) {
			selectedEntry = entry;
			parent.gui.pane.loadSelectedEntry(selectedEntry);
		}
	}
	public JSideBarEntry getSelectedEntry() {
		return selectedEntry;
	}
	
}
