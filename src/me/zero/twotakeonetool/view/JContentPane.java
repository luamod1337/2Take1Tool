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
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.function.Consumer;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.yaml.snakeyaml.Yaml;

import me.zero.twotakeonetool.FileLoader;
import me.zero.twotakeonetool.TwoTakeOneTool;
import me.zero.twotakeonetool.config.FileConfiguration;
import me.zero.twotakeonetool.lang.Language;
import me.zero.twotakeonetool.lang.LanguageKey;
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
		TwoTakeOneToolGui.instance.add(this);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if(bar == null || tbar == null) {
			return;
		}
		Graphics2D g2 = (Graphics2D)g;
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		g2.fillRect(bar.getWidth(), 0, dim.width-bar.getWidth(), dim.height);
		
		//
		int cnt = 0;
		try {
			for(String key : packKeys) {
				TwoTakeOnePackView pack = packs.get(key);
				//System.out.println("render pack " + key);
				pack.paint(g2,bar.getWidth()+2,tbar.getHeight()+2 + (cnt++*402)+offsetY);
				//pack.paint(g2);
			}
		}catch(ConcurrentModificationException e) {
			//Ignore it here, it will be drawn later again
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
			JOptionPane.showMessageDialog(null, Language.getTranslatedString(LanguageKey.DUPLICATE_PACK).replace("<name>", pack.config.getString("Name")).replace("<version>", pack.config.getString("Version")));
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
		
		if(tbar.getEntrys().size() == 0) {
			try {
				JSideBarEntry load = new JSideBarEntry("",ImageIO.read(getClass().getResource("/ressources/images/file.png")), bar, new Consumer<JSideBarEntry>() {				
					@Override
					public void accept(JSideBarEntry t) {
						if(bar.getSelectedEntry() != null){
							JFileChooser chooser = new JFileChooser();
							chooser.setDialogTitle(Language.getTranslatedString(LanguageKey.SELECT_PACK).replace("<typ>", bar.getSelectedEntry().getType().name()));
							chooser.setAcceptAllFileFilterUsed(false);
							chooser.setFileFilter(new FileFilter() {							
								@Override
								public String getDescription() {
									return "2take1pack";
								}							
								@Override
								public boolean accept(File f) {							
									if(f.getAbsolutePath().endsWith(".2take1pack") || f.isDirectory()) {
										return true;
									}
									return false;
								}
							});
							int returnVal = chooser.showOpenDialog(null);
							if(returnVal == JFileChooser.APPROVE_OPTION) {		
								System.out.println(TwoTakeOneTool.getPackFolderBySelectedEntry(bar.getSelectedEntry().getType()) + "\\" + chooser.getSelectedFile().getName());
									File f = new File(TwoTakeOneTool.getPackFolderBySelectedEntry(bar.getSelectedEntry().getType()) + "\\" + chooser.getSelectedFile().getName());								
									try {
										Files.move(chooser.getSelectedFile().toPath(), f.toPath());
									} catch (IOException e) {
										e.printStackTrace();
									}								
									FileConfiguration config = FileLoader.loadModFile(f, bar.getSelectedEntry().getType());
									addTwoTakeOnePackView(new TwoTakeOnePackView(config, bar,bar.getSelectedEntry().getType()));							
							}
						}else if(bar.getSelectedEntry() != null) {
							System.out.println("unknown type " + bar.getSelectedEntry().getType().name());
							JOptionPane.showMessageDialog(null, "Not supported yet");
						}
					}
				}, SideBarEntryType.LOAD);
				load.setWidth(25);
				tbar.addEntry(load);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}else {
			System.out.println("tbar entries: " + tbar.getEntrys().size());
		}
		
		this.packs.clear();
		this.packKeys.clear();
		this.offsetY = 0;		
		if(selectedEntry.getType().equals(SideBarEntryType.SCRIPT)) {
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			ArrayList<FileConfiguration> packs = FileLoader.loadModDirectory();
			for(FileConfiguration config : packs) {
				if(config != null) {
					this.addTwoTakeOnePackView(new TwoTakeOnePackView(config, bar,selectedEntry.getType()));
				}				
			}
			TwoTakeOneToolGui.instance.repaint();
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}else if(selectedEntry.getType().equals(SideBarEntryType.SPRITE)) {
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
			ArrayList<FileConfiguration> packs = FileLoader.loadSpriteDirectory();
			for(FileConfiguration config : packs) {
				this.addTwoTakeOnePackView(new TwoTakeOnePackView(config, bar,selectedEntry.getType()));
			}
			TwoTakeOneToolGui.instance.repaint();
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}else if(selectedEntry.getType().equals(SideBarEntryType.PROTECTION)) {
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
			ArrayList<FileConfiguration> packs = FileLoader.loadProtectionDirectory();
			for(FileConfiguration config : packs) {
				this.addTwoTakeOnePackView(new TwoTakeOnePackView(config, bar,selectedEntry.getType()));
			}
			TwoTakeOneToolGui.instance.repaint();
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}else if(selectedEntry.getType().equals(SideBarEntryType.TELEPORT)) {
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
			ArrayList<FileConfiguration> packs = FileLoader.loadTeleportDirectory();
			for(FileConfiguration config : packs) {
				this.addTwoTakeOnePackView(new TwoTakeOnePackView(config, bar,selectedEntry.getType()));
			}
			TwoTakeOneToolGui.instance.repaint();
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}else if(selectedEntry.getType().equals(SideBarEntryType.ANIMATION)) {
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
			ArrayList<FileConfiguration> packs = FileLoader.loadAnimationDirectory();
			for(FileConfiguration config : packs) {
				this.addTwoTakeOnePackView(new TwoTakeOnePackView(config, bar,selectedEntry.getType()));
			}
			TwoTakeOneToolGui.instance.repaint();
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}else if(selectedEntry.getType().equals(SideBarEntryType.OBJECT)) {
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
			ArrayList<FileConfiguration> packs = FileLoader.loadObjectDirectory();
			for(FileConfiguration config : packs) {
				this.addTwoTakeOnePackView(new TwoTakeOnePackView(config, bar,selectedEntry.getType()));
			}
			TwoTakeOneToolGui.instance.repaint();
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}else if(selectedEntry.getType().equals(SideBarEntryType.STAT)) {
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
			ArrayList<FileConfiguration> packs = FileLoader.loadStatDirectory();
			for(FileConfiguration config : packs) {
				this.addTwoTakeOnePackView(new TwoTakeOnePackView(config, bar,selectedEntry.getType()));
			}
			TwoTakeOneToolGui.instance.repaint();
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}else if(selectedEntry.getType().equals(SideBarEntryType.OBJECT)) {
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
			ArrayList<FileConfiguration> packs = FileLoader.loadObjectDirectory();
			for(FileConfiguration config : packs) {
				this.addTwoTakeOnePackView(new TwoTakeOnePackView(config, bar,selectedEntry.getType()));
			}
			TwoTakeOneToolGui.instance.repaint();
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}else if(selectedEntry.getType().equals(SideBarEntryType.FONT)) {
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
			ArrayList<FileConfiguration> packs = FileLoader.loadFontDirectory();
			for(FileConfiguration config : packs) {
				this.addTwoTakeOnePackView(new TwoTakeOnePackView(config, bar,selectedEntry.getType()));
			}
			TwoTakeOneToolGui.instance.repaint();
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}else if(selectedEntry.getType().equals(SideBarEntryType.CONFIG)) {
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
			ArrayList<FileConfiguration> packs = FileLoader.loadConfigDirectory();
			for(FileConfiguration config : packs) {
				this.addTwoTakeOnePackView(new TwoTakeOnePackView(config, bar,selectedEntry.getType()));
			}
			TwoTakeOneToolGui.instance.repaint();
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}else if(selectedEntry.getType().equals(SideBarEntryType.LANGUAGE)) {
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
			ArrayList<FileConfiguration> packs = FileLoader.loadLanguageDirectory();
			for(FileConfiguration config : packs) {
				this.addTwoTakeOnePackView(new TwoTakeOnePackView(config, bar,selectedEntry.getType()));
			}
			TwoTakeOneToolGui.instance.repaint();
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}else if(selectedEntry.getType().equals(SideBarEntryType.OUTFIT)) {
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
			ArrayList<FileConfiguration> packs = FileLoader.loadOutfitDirectory();
			for(FileConfiguration config : packs) {
				this.addTwoTakeOnePackView(new TwoTakeOnePackView(config, bar,selectedEntry.getType()));
			}
			TwoTakeOneToolGui.instance.repaint();
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}else if(selectedEntry.getType().equals(SideBarEntryType.VEHICLE)) {
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
			ArrayList<FileConfiguration> packs = FileLoader.loadVehicleDirectory();
			for(FileConfiguration config : packs) {
				this.addTwoTakeOnePackView(new TwoTakeOnePackView(config, bar,selectedEntry.getType()));
			}
			TwoTakeOneToolGui.instance.repaint();
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}else if(selectedEntry.getType().equals(SideBarEntryType.WEB)) {
			TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.WAIT_CURSOR));					
			try {
				String updateUrlFile = "https://raw.githubusercontent.com/luamod1337/2take1packs/master/packList.yml";
				URL url = new URL(updateUrlFile);
				URLConnection urlConnection = url.openConnection();
				FileConfiguration config = new FileConfiguration(new Yaml(), urlConnection.getInputStream(), updateUrlFile);	
				this.tbar.clear();
				
				for(String key : config.getSettings().keySet()) {	
					JSideBarEntry ent = new JSideBarEntry("",ImageIO.read(getClass().getResource(TwoTakeOneTool.getIconUrlBySidebarEntry(SideBarEntryType.valueOf(key.toUpperCase())))), bar, new Consumer<JSideBarEntry>() {				
						@Override
						public void accept(JSideBarEntry t) {
							Object o = config.getSettings().get(key);
							if(o.getClass().equals(ArrayList.class)) {
								@SuppressWarnings("unchecked")
								ArrayList<String> list = (ArrayList<String>)o;
								tbar.setSelectedEntryType(t.getType());
								for(String s : list) {
									FileLoader.clearTempPack();
									FileLoader.loadWebPacks(t.getType(),s);									
								}
							}else {
								System.out.println("unknown type '" + o.getClass() + "'");
								JOptionPane.showMessageDialog(null, "","Error",JOptionPane.ERROR_MESSAGE);
							}
						}
					}, SideBarEntryType.valueOf(key.toUpperCase()));
					ent.setWidth(25);
					this.tbar.addEntry(ent);
				}
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Error checking webpage for updates");
				e.printStackTrace();
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
				TwoTakeOnePackView pack;
				if(bar.getSelectedEntry().getType().equals(SideBarEntryType.WEB)) {
					pack = new TwoTakeOnePackView(config, bar,TwoTakeOneToolGui.instance.gui.pane.getTbar().getSelectedEntryType());
				}else {
					pack = new TwoTakeOnePackView(config, bar,bar.getSelectedEntry().getType());
				}
				this.packKeys.add((pack.config.getString("Name")+":" + pack.config.getString("Version")));
				this.packs.put(pack.getName()+":" + pack.getVersion(), pack);
			}
		}
		TwoTakeOneToolGui.instance.gui.repaint();
	}

	public JToolToolBar getTbar() {
		return tbar;
	}
	public void addPack(TwoTakeOnePackView pack) {
		this.packKeys.add((pack.config.getString("Name")+":" + pack.config.getString("Version")));
		this.packs.put(pack.getName()+":" + pack.getVersion(), pack);
	}
	public void clear() {
		this.packs.clear();
		this.packKeys.clear();
		this.offsetY = 0;
	}
}
