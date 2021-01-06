package me.zero.twotakeonetool.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.function.Consumer;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import me.zero.twotakeonetool.FileLoader;
import me.zero.twotakeonetool.TwoTakeOneTool;
import me.zero.twotakeonetool.config.FileConfiguration;
import me.zero.twotakeonetool.type.SideBarEntryType;

public class JToolToolBar extends JComponent{

	private static final long serialVersionUID = 1L;
	private JSideBar bar;
	private ArrayList<JSideBarEntry> entrys = new ArrayList<>();
	private String extraHeaderText = "";
	
	public JToolToolBar(JSideBar bar,JContentPane pane) {
		this.bar = bar;
		try {
			JSideBarEntry load = new JSideBarEntry("",ImageIO.read(getClass().getResource("/ressources/images/file.png")), bar, new Consumer<JSideBarEntry>() {				
				@Override
				public void accept(JSideBarEntry t) {
					if(bar.getSelectedEntry() != null){
						JFileChooser chooser = new JFileChooser();
						chooser.setDialogTitle("Select a pack of type '" + bar.getSelectedEntry().getType() + "'");
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
								File f = new File(TwoTakeOneTool.getInstallFolderBySelectedEntry(bar.getSelectedEntry().getType()) + "\\" + chooser.getSelectedFile().getName());								
								try {
									Files.move(chooser.getSelectedFile().toPath(), f.toPath());
								} catch (IOException e) {
									e.printStackTrace();
								}								
								FileConfiguration config = FileLoader.loadModFile(f, bar.getSelectedEntry().getType());
								pane.addTwoTakeOnePackView(new TwoTakeOnePackView(config, bar,bar.getSelectedEntry().getType()));							
						}
					}else if(bar.getSelectedEntry() != null) {
						System.out.println("unknown type " + bar.getSelectedEntry().getType().name());
						JOptionPane.showMessageDialog(null, "Not supported yet");
					}
				}
			}, SideBarEntryType.LOAD);
			load.setHeight(25);
			load.setWidth(25);
			entrys.add(load);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D)g;
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		
		g2.setColor(new Color(46,48,62));
		g2.fillRect(bar.getWidth(), 0, dim.width-bar.getWidth(), 100);
		
		g2.setColor(new Color(126,86,194));
		if(bar.getSelectedEntry() != null) {
			g2.drawString(bar.getSelectedEntry().getText() + extraHeaderText, bar.getWidth()+2, 40);
		}
		if(bar.getSelectedEntry() != null) {
			for(int i = 0; i < entrys.size();i++) {
				JSideBarEntry e = entrys.get(i);
				g2.setColor(new Color(41,44,56));
				g2.drawRect((bar.getWidth()) + (i*20)-2,80-25, e.getWidth()+4, e.getHeight() + 10);
				g2.setColor(new Color(126,86,194));
				e.paint(g2,(bar.getWidth()) + (i*20),80);
			}
		}
	}

	public void mouseClicked(MouseEvent e) {
		for(JSideBarEntry entry : entrys) {
			entry.getListener().mouseClicked(e);
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}
	@Override
	public int getWidth() {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		return (dim.width-bar.getWidth());
	}
	@Override
	public int getHeight() {
		return 100;
	}

	public String getExtraHeaderText() {
		return extraHeaderText;
	}

	public void setExtraHeaderText(String extraHeaderText) {
		this.extraHeaderText = extraHeaderText;
	}
	
}
