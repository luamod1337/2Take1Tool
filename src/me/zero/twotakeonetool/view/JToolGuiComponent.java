package me.zero.twotakeonetool.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.function.Consumer;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import me.zero.twotakeonetool.controller.SidebarMouseListener;
import me.zero.twotakeonetool.lang.Language;
import me.zero.twotakeonetool.lang.LanguageKey;
import me.zero.twotakeonetool.type.SideBarEntryType;

public class JToolGuiComponent extends JComponent{

	private static final long serialVersionUID = 1L;
	private TwoTakeOneToolGui gui;
	private JSideBar bar;
	public JContentPane pane;
	private JToolToolBar toolBar;
	
	public JToolGuiComponent(TwoTakeOneToolGui gui) {
		super();
		this.gui = gui;
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		bar = new JSideBar(dim.height,gui);
		pane = new JContentPane(bar);
		toolBar = new JToolToolBar(bar, pane);
		pane.setToolBar(toolBar);
		pane.setSideBar(bar);
		this.add(pane);
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
		bar.addEntry(new JSideBarEntry(Language.getTranslatedString(LanguageKey.TYP_SPRITES),ImageIO.read(getClass().getResource("/ressources/images/image.png")), bar, new Consumer<JSideBarEntry>() {
			@Override
			public void accept(JSideBarEntry t) {
				bar.setSelectedEntry(t);
			}
		}, SideBarEntryType.UI));
		bar.addEntry(new JSideBarEntry(Language.getTranslatedString(LanguageKey.TYP_UI),ImageIO.read(getClass().getResource("/ressources/images/image.png")), bar, new Consumer<JSideBarEntry>() {
			@Override
			public void accept(JSideBarEntry t) {
				bar.setSelectedEntry(t);
			}
		}, SideBarEntryType.UI));
		bar.addEntry(new JSideBarEntry(Language.getTranslatedString(LanguageKey.TYP_LUA_SCRIPTS),ImageIO.read(getClass().getResource("/ressources/images/lua.png")), bar, new Consumer<JSideBarEntry>() {
			
			@Override
			public void accept(JSideBarEntry t) {
				bar.setSelectedEntry(t);
			}
		}, SideBarEntryType.SCRIPT));
		bar.addEntry(new JSideBarEntry(Language.getTranslatedString(LanguageKey.TYP_SE_PROTECTIONS),ImageIO.read(getClass().getResource("/ressources/images/protection.png")), bar, new Consumer<JSideBarEntry>() {
			
			@Override
			public void accept(JSideBarEntry t) {
				bar.setSelectedEntry(t);
			}
		}, SideBarEntryType.PROTECTION));
		bar.addEntry(new JSideBarEntry(Language.getTranslatedString(LanguageKey.TYP_TELEPORTS),ImageIO.read(getClass().getResource("/ressources/images/map.png")), bar, new Consumer<JSideBarEntry>() {
			
			@Override
			public void accept(JSideBarEntry t) {
				bar.setSelectedEntry(t);
			}
		}, SideBarEntryType.TELEPORT));
		bar.addEntry(new JSideBarEntry(Language.getTranslatedString(LanguageKey.TYP_STATS),ImageIO.read(getClass().getResource("/ressources/images/stats.png")), bar, new Consumer<JSideBarEntry>() {
			
			@Override
			public void accept(JSideBarEntry t) {
				bar.setSelectedEntry(t);
			}
		}, SideBarEntryType.STAT));
		bar.addEntry(new JSideBarEntry(Language.getTranslatedString(LanguageKey.TYP_VEHICLES),ImageIO.read(getClass().getResource("/ressources/images/car.png")), bar, new Consumer<JSideBarEntry>() {
			
			@Override
			public void accept(JSideBarEntry t) {
				bar.setSelectedEntry(t);
			}
		}, SideBarEntryType.VEHICLE));
		bar.addEntry(new JSideBarEntry(Language.getTranslatedString(LanguageKey.TYP_OUTFITS),ImageIO.read(getClass().getResource("/ressources/images/outfit.png")), bar, new Consumer<JSideBarEntry>() {
			
			@Override
			public void accept(JSideBarEntry t) {
				bar.setSelectedEntry(t);
			}
		}, SideBarEntryType.OUTFIT));
		bar.addEntry(new JSideBarEntry(Language.getTranslatedString(LanguageKey.TYP_OBJECTS),ImageIO.read(getClass().getResource("/ressources/images/object.png")), bar, new Consumer<JSideBarEntry>() {
			
			@Override
			public void accept(JSideBarEntry t) {
				bar.setSelectedEntry(t);
			}
		}, SideBarEntryType.OBJECT));
		bar.addEntry(new JSideBarEntry(Language.getTranslatedString(LanguageKey.TYP_ANIMATIONS),ImageIO.read(getClass().getResource("/ressources/images/animation.png")), bar, new Consumer<JSideBarEntry>() {
			
			@Override
			public void accept(JSideBarEntry t) {
				bar.setSelectedEntry(t);
			}
		}, SideBarEntryType.ANIMATION));
		bar.addEntry(new JSideBarEntry(Language.getTranslatedString(LanguageKey.TYP_FONTS),ImageIO.read(getClass().getResource("/ressources/images/font.png")), bar, new Consumer<JSideBarEntry>() {
			
			@Override
			public void accept(JSideBarEntry t) {
				bar.setSelectedEntry(t);
			}
		}, SideBarEntryType.FONT));
		bar.addEntry(new JSideBarEntry(Language.getTranslatedString(LanguageKey.TYP_LANGUAGES),ImageIO.read(getClass().getResource("/ressources/images/language.png")), bar, new Consumer<JSideBarEntry>() {
			
			@Override
			public void accept(JSideBarEntry t) {
				bar.setSelectedEntry(t);
			}
		}, SideBarEntryType.LANGUAGE));
		bar.addEntry(new JSideBarEntry(Language.getTranslatedString(LanguageKey.TYP_CONFIGS),ImageIO.read(getClass().getResource("/ressources/images/config.png")), bar, new Consumer<JSideBarEntry>() {
					
					@Override
					public void accept(JSideBarEntry t) {
						bar.setSelectedEntry(t);
					}
				}, SideBarEntryType.CONFIG));
		
		bar.addEntry(new JSideBarEntry(Language.getTranslatedString(LanguageKey.TYP_WEBPACKS),ImageIO.read(getClass().getResource("/ressources/images/web.png")), bar, new Consumer<JSideBarEntry>() {
			
			@Override
			public void accept(JSideBarEntry t) {
				bar.setSelectedEntry(t);
			}
		}, SideBarEntryType.WEB));
		
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
