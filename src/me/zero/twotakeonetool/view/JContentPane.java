package me.zero.twotakeonetool.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import me.zero.twotakeonetool.FileLoader;
import me.zero.twotakeonetool.config.FileConfiguration;
import me.zero.twotakeonetool.type.SideBarEntryType;


public class JContentPane extends JComponent implements MouseWheelListener{

	private static final long serialVersionUID = 1L;
	
	private JSideBar bar;
	private JToolToolBar tbar;
	//private HashMap<String, TwoTakeOnePackView> packs = new HashMap<>();
	
	private HashMap<String, TwoTakeOnePackView> packs = new HashMap<>();
	private ArrayList<String> packKeys = new ArrayList<>();
	
	private int offsetY = 0;
	private int loaded = 3;
	
	public JContentPane(JSideBar bar) {
		this.bar = bar;		
		TwoTakeOneToolGui.instance.addMouseWheelListener(this);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if(bar == null || tbar == null) {
			System.out.println("paint: out");
			return;
		}
		Graphics2D g2 = (Graphics2D)g;
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		g2.fillRect(bar.getWidth(), 0, dim.width-bar.getWidth(), dim.height);
		
		//
		int cnt = 0;
		for(String key : packKeys) {
			TwoTakeOnePackView pack = packs.get(key);
			pack.paint(g2,bar.getWidth()+2,tbar.getHeight()+2 + (cnt++*402)+offsetY);
			pack.paint(g2);
		}
		
		if(bar.getSelectedEntry() == null && packs.size() == 0) {
			g2.setColor(new Color(46,48,62));
			//System.out.println("paint-selected: " + bar.getSelectedEntry().getName());
			Font font = new Font("Open Sans, Lucida Sans", Font.PLAIN, 20);
			g2.setFont(font);
			g2.drawString("Welcome to 2Take1Tool by 1337Zero", ((TwoTakeOneToolGui.instance.getWidth()-bar.getWidth())/2)-10, tbar.getHeight()+20);

			g2.drawString("Disclaimer: This tool has no relation to 2Take1Menu, nor is it needed to run 2Take1Menu!", bar.getWidth() + 50, tbar.getHeight()+100);
			g2.drawString("if you encounter Problems, please tell @1337Zero in discord about it either than asking in support!", bar.getWidth() + 50, tbar.getHeight()+130);
			
			g2.drawString("This tool can only process files of \"2take1pack\"",bar.getWidth() + 50, tbar.getHeight()+150);
			g2.drawString("This is basicly a renamed zip file with a \"install.yml\" inside",bar.getWidth() + 50, tbar.getHeight()+170);
		}
		
		
	}
	
	@Override
	public int getX() {
		return bar.getWidth();
	}
	
	@Override
	public int getY() {
		return 0;
	}
	public void addTwoTakeOnePackView(TwoTakeOnePackView pack) {
		if(!packs.containsKey((pack.config.getString("Name")+":" + pack.config.getString("Version")))) {
			packKeys.add((pack.config.getString("Name")+":" + pack.config.getString("Version")));
			this.packs.put((pack.config.getString("Name")+":" + pack.config.getString("Version")),pack);
		}else {
			JOptionPane.showMessageDialog(null, "Duplicate Pack found '" + (pack.config.getString("Name")+":" + pack.config.getString("Version")) + "'");
		}
	}

	public void setSideBar(JSideBar bar) {
		this.bar = bar;		
	}
	public void setToolBar(JToolToolBar tbar) {
		this.tbar = tbar;		
	}

	public void loadSelectedEntry(JSideBarEntry selectedEntry) {	
		
		for(String packName : packs.keySet()) {
			TwoTakeOnePackView pack = packs.get(packName);
			try {
				pack.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		this.packs.clear();
		this.packKeys.clear();
		this.offsetY = 0;
		if(selectedEntry.getType().equals(SideBarEntryType.SCRIPT)) {
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			ArrayList<FileConfiguration> packs = FileLoader.loadModDirectory();
			//tbar.setExtraHeaderText(" (" + packs.size() + ")");
			for(FileConfiguration config : packs) {
				this.addTwoTakeOnePackView(new TwoTakeOnePackView(config, bar,selectedEntry.getType()));
			}
			TwoTakeOneToolGui.instance.repaint();
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}else if(selectedEntry.getType().equals(SideBarEntryType.SPRITE)) {
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
			ArrayList<FileConfiguration> packs = FileLoader.loadSpriteDirectory();
			//tbar.setExtraHeaderText(" (" + packs.size() + ")");
			for(FileConfiguration config : packs) {
				this.addTwoTakeOnePackView(new TwoTakeOnePackView(config, bar,selectedEntry.getType()));
			}
			TwoTakeOneToolGui.instance.repaint();
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}else if(selectedEntry.getType().equals(SideBarEntryType.PROTECTION)) {
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
			ArrayList<FileConfiguration> packs = FileLoader.loadProtectionDirectory();
			//tbar.setExtraHeaderText(" (" + packs.size() + ")");
			for(FileConfiguration config : packs) {
				this.addTwoTakeOnePackView(new TwoTakeOnePackView(config, bar,selectedEntry.getType()));
			}
			TwoTakeOneToolGui.instance.repaint();
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}else if(selectedEntry.getType().equals(SideBarEntryType.TELEPORT)) {
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
			ArrayList<FileConfiguration> packs = FileLoader.loadTeleportDirectory();
			//tbar.setExtraHeaderText(" (" + packs.size() + ")");
			for(FileConfiguration config : packs) {
				this.addTwoTakeOnePackView(new TwoTakeOnePackView(config, bar,selectedEntry.getType()));
			}
			TwoTakeOneToolGui.instance.repaint();
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}else if(selectedEntry.getType().equals(SideBarEntryType.ANIMATION)) {
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
			ArrayList<FileConfiguration> packs = FileLoader.loadAnimationDirectory();
			//tbar.setExtraHeaderText(" (" + packs.size() + ")");
			for(FileConfiguration config : packs) {
				this.addTwoTakeOnePackView(new TwoTakeOnePackView(config, bar,selectedEntry.getType()));
			}
			TwoTakeOneToolGui.instance.repaint();
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}else if(selectedEntry.getType().equals(SideBarEntryType.OBJECT)) {
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
			ArrayList<FileConfiguration> packs = FileLoader.loadObjectDirectory();
			//tbar.setExtraHeaderText(" (" + packs.size() + ")");
			for(FileConfiguration config : packs) {
				this.addTwoTakeOnePackView(new TwoTakeOnePackView(config, bar,selectedEntry.getType()));
			}
			TwoTakeOneToolGui.instance.repaint();
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}else if(selectedEntry.getType().equals(SideBarEntryType.STAT)) {
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
			ArrayList<FileConfiguration> packs = FileLoader.loadStatDirectory();
			//tbar.setExtraHeaderText(" (" + packs.size() + ")");
			for(FileConfiguration config : packs) {
				this.addTwoTakeOnePackView(new TwoTakeOnePackView(config, bar,selectedEntry.getType()));
			}
			TwoTakeOneToolGui.instance.repaint();
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}else if(selectedEntry.getType().equals(SideBarEntryType.OBJECT)) {
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
			ArrayList<FileConfiguration> packs = FileLoader.loadObjectDirectory();
			//tbar.setExtraHeaderText(" (" + packs.size() + ")");
			for(FileConfiguration config : packs) {
				this.addTwoTakeOnePackView(new TwoTakeOnePackView(config, bar,selectedEntry.getType()));
			}
			TwoTakeOneToolGui.instance.repaint();
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}else if(selectedEntry.getType().equals(SideBarEntryType.FONT)) {
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
			ArrayList<FileConfiguration> packs = FileLoader.loadFontDirectory();
			//tbar.setExtraHeaderText(" (" + packs.size() + ")");
			for(FileConfiguration config : packs) {
				this.addTwoTakeOnePackView(new TwoTakeOnePackView(config, bar,selectedEntry.getType()));
			}
			TwoTakeOneToolGui.instance.repaint();
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}else if(selectedEntry.getType().equals(SideBarEntryType.CONFIG)) {
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
			ArrayList<FileConfiguration> packs = FileLoader.loadConfigDirectory();
			//tbar.setExtraHeaderText(" (" + packs.size() + ")");
			for(FileConfiguration config : packs) {
				this.addTwoTakeOnePackView(new TwoTakeOnePackView(config, bar,selectedEntry.getType()));
			}
			TwoTakeOneToolGui.instance.repaint();
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}else if(selectedEntry.getType().equals(SideBarEntryType.LANGUAGE)) {
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
			ArrayList<FileConfiguration> packs = FileLoader.loadLanguageDirectory();
			//tbar.setExtraHeaderText(" (" + packs.size() + ")");
			for(FileConfiguration config : packs) {
				this.addTwoTakeOnePackView(new TwoTakeOnePackView(config, bar,selectedEntry.getType()));
			}
			TwoTakeOneToolGui.instance.repaint();
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}else if(selectedEntry.getType().equals(SideBarEntryType.OUTFIT)) {
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
			ArrayList<FileConfiguration> packs = FileLoader.loadOutfitDirectory();
			//tbar.setExtraHeaderText(" (" + packs.size() + ")");
			for(FileConfiguration config : packs) {
				this.addTwoTakeOnePackView(new TwoTakeOnePackView(config, bar,selectedEntry.getType()));
			}
			TwoTakeOneToolGui.instance.repaint();
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}else if(selectedEntry.getType().equals(SideBarEntryType.VEHICLE)) {
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
			ArrayList<FileConfiguration> packs = FileLoader.loadVehicleDirectory();
			//tbar.setExtraHeaderText(" (" + packs.size() + ")");
			for(FileConfiguration config : packs) {
				this.addTwoTakeOnePackView(new TwoTakeOnePackView(config, bar,selectedEntry.getType()));
			}
			TwoTakeOneToolGui.instance.repaint();
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}else{
			System.out.println("not implemented loading of packets of type " + selectedEntry.getType());
		}
	}
	public TwoTakeOnePackView getPack(String name) {
		return packs.get(name);
	}

	public void removePack(String name) {
		packKeys.remove(name);
		packs.remove(name);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(e.getWheelRotation() == -1) {
			offsetY+= (e.getScrollAmount()*10);
		}else{
			offsetY-= (e.getScrollAmount()*10);
		}
		if(offsetY > 0) {
			offsetY = 0;
		}
		if(offsetY/400 > loaded) {
			loaded++;
		}
		if(bar.getSelectedEntry() != null && (TwoTakeOneToolGui.instance.getHeight()-tbar.getHeight() + (offsetY*-1)) > (this.packs.size()*400)) {
			FileConfiguration config = FileLoader.loadNextPack(bar.getSelectedEntry().getType(), bar);			
			if(config != null) {		
				TwoTakeOnePackView pack = new TwoTakeOnePackView(config, bar,bar.getSelectedEntry().getType());
				this.packKeys.add((pack.config.getString("Name")+":" + pack.config.getString("Version")));
				this.packs.put(pack.getName()+":" + pack.getVersion(), pack);
			}
		}
		TwoTakeOneToolGui.instance.gui.repaint();
	}
}
