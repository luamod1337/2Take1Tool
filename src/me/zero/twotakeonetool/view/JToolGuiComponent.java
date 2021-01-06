package me.zero.twotakeonetool.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.function.Consumer;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import me.zero.twotakeonetool.controller.SidebarMouseListener;
import me.zero.twotakeonetool.type.SideBarEntryType;

public class JToolGuiComponent extends JComponent{

	private static final long serialVersionUID = 1L;
	private TwoTakeOneToolGui gui;
	private JSideBar bar;
	public JContentPane pane;
	private JToolToolBar toolBar;
	
	public JToolGuiComponent(TwoTakeOneToolGui gui) {
		this.gui = gui;
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		bar = new JSideBar(dim.height,gui);
		pane = new JContentPane(bar);
		toolBar = new JToolToolBar(bar, pane);
		pane.setToolBar(toolBar);
		pane.setSideBar(bar);
		
		try {
			addSidebarEntries(bar);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.addMouseListener(new SidebarMouseListener());		
		this.addMouseWheelListener(new SidebarMouseListener());		
		this.add(pane);
		this.add(bar);		
	}
	private void addSidebarEntries(JSideBar bar) throws IOException {		
		bar.addEntry(new JSideBarEntry("Sprites",ImageIO.read(getClass().getResource("/ressources/images/image.png")), bar, new Consumer<JSideBarEntry>() {
			@Override
			public void accept(JSideBarEntry t) {
				bar.setSelectedEntry(t);
			}
		}, SideBarEntryType.SPRITE));
		bar.addEntry(new JSideBarEntry("Lua Scripts",ImageIO.read(getClass().getResource("/ressources/images/lua.png")), bar, new Consumer<JSideBarEntry>() {
			
			@Override
			public void accept(JSideBarEntry t) {
				bar.setSelectedEntry(t);
			}
		}, SideBarEntryType.SCRIPT));
		bar.addEntry(new JSideBarEntry("SE Protections",ImageIO.read(getClass().getResource("/ressources/images/protection.png")), bar, new Consumer<JSideBarEntry>() {
			
			@Override
			public void accept(JSideBarEntry t) {
				bar.setSelectedEntry(t);
			}
		}, SideBarEntryType.PROTECTION));
		bar.addEntry(new JSideBarEntry("Teleports",ImageIO.read(getClass().getResource("/ressources/images/map.png")), bar, new Consumer<JSideBarEntry>() {
			
			@Override
			public void accept(JSideBarEntry t) {
				bar.setSelectedEntry(t);
			}
		}, SideBarEntryType.TELEPORT));
		bar.addEntry(new JSideBarEntry("Stats",ImageIO.read(getClass().getResource("/ressources/images/stats.png")), bar, new Consumer<JSideBarEntry>() {
			
			@Override
			public void accept(JSideBarEntry t) {
				bar.setSelectedEntry(t);
			}
		}, SideBarEntryType.STAT));
		bar.addEntry(new JSideBarEntry("Vehicle",ImageIO.read(getClass().getResource("/ressources/images/car.png")), bar, new Consumer<JSideBarEntry>() {
			
			@Override
			public void accept(JSideBarEntry t) {
				bar.setSelectedEntry(t);
			}
		}, SideBarEntryType.VEHICLE));
		bar.addEntry(new JSideBarEntry("Outfits",ImageIO.read(getClass().getResource("/ressources/images/outfit.png")), bar, new Consumer<JSideBarEntry>() {
			
			@Override
			public void accept(JSideBarEntry t) {
				bar.setSelectedEntry(t);
			}
		}, SideBarEntryType.OUTFIT));
		bar.addEntry(new JSideBarEntry("Objects",ImageIO.read(getClass().getResource("/ressources/images/object.png")), bar, new Consumer<JSideBarEntry>() {
			
			@Override
			public void accept(JSideBarEntry t) {
				bar.setSelectedEntry(t);
			}
		}, SideBarEntryType.OBJECT));
		bar.addEntry(new JSideBarEntry("Animations",ImageIO.read(getClass().getResource("/ressources/images/animation.png")), bar, new Consumer<JSideBarEntry>() {
			
			@Override
			public void accept(JSideBarEntry t) {
				bar.setSelectedEntry(t);
			}
		}, SideBarEntryType.ANIMATION));
		bar.addEntry(new JSideBarEntry("Font",ImageIO.read(getClass().getResource("/ressources/images/font.png")), bar, new Consumer<JSideBarEntry>() {
			
			@Override
			public void accept(JSideBarEntry t) {
				bar.setSelectedEntry(t);
			}
		}, SideBarEntryType.FONT));
		bar.addEntry(new JSideBarEntry("Language",ImageIO.read(getClass().getResource("/ressources/images/language.png")), bar, new Consumer<JSideBarEntry>() {
			
			@Override
			public void accept(JSideBarEntry t) {
				bar.setSelectedEntry(t);
			}
		}, SideBarEntryType.LANGUAGE));
		bar.addEntry(new JSideBarEntry("Config",ImageIO.read(getClass().getResource("/ressources/images/config.png")), bar, new Consumer<JSideBarEntry>() {
			
			@Override
			public void accept(JSideBarEntry t) {
				bar.setSelectedEntry(t);
			}
		}, SideBarEntryType.CONFIG));
		
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		bar.paint(g);
		pane.paint(g);
		toolBar.paint(g);
	}
	public TwoTakeOneToolGui getGui() {
		return gui;
	}
	public JSideBar getSideBar() {
		return bar;
	}
	public JContentPane getPane() {
		return pane;
	}
	public JToolToolBar getToolBar() {
		return toolBar;
	}
	
}
