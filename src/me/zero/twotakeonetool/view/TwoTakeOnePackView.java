package me.zero.twotakeonetool.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

import org.yaml.snakeyaml.Yaml;

import me.zero.twotakeonetool.FileLoader;
import me.zero.twotakeonetool.TwoTakeOneTool;
import me.zero.twotakeonetool.config.FileConfiguration;
import me.zero.twotakeonetool.type.InstallStatus;
import me.zero.twotakeonetool.type.SideBarEntryType;
import me.zero.twotakeonetool.util.Description;

public class TwoTakeOnePackView extends JComponent{

	private static final long serialVersionUID = 1L;
	public FileConfiguration config;
	private int x;
	private int y;
	private JSideBar bar;
	private JSideBarEntry install;
	//private JSideBarEntry deinstall;
	private JSideBarEntry delete;
	private JSideBarEntry updateCheck;

	private JSideBarEntry forward;
	private JSideBarEntry backward;
	private String name;
	private String version;
	
	private SideBarEntryType type;
	
	public InstallStatus status = InstallStatus.READY_TO_INSTALL;
	private ArrayList<Description> desc = new ArrayList<>();
	
	private int page = 0;
	private String updateUrl;
	
	public TwoTakeOnePackView(FileConfiguration config,JSideBar bar,SideBarEntryType type) {
		this.config = config;
		this.bar = bar;		
		this.type = type;
		name = config.getString("Name");
		version = config.getString("Version");
		try {
			install = new JSideBarEntry("install",ImageIO.read(getClass().getResource("/ressources/images/install.png")), bar, new Consumer<JSideBarEntry>() {				
				@Override
				public void accept(JSideBarEntry t) {					
					FileLoader.installPack(t.getPack().getName() + ":" + t.getPack().getVersion());
					HashMap<String, Object> data = FileLoader.loadDataStorage().getSettings();
					@SuppressWarnings("unchecked")
					ArrayList<String> dataList = (ArrayList<String>) data.get("installedPack");
					if(dataList == null) dataList = new ArrayList<>();
					dataList.add(t.getPack().getName() + ":" + t.getPack().getVersion());
;					FileLoader.loadDataStorage().storeData("installedPack", dataList);
				}
			}, SideBarEntryType.INSTALL,this);
			install.setWidth(90);
			forward = new JSideBarEntry("",ImageIO.read(getClass().getResource("/ressources/images/forward.png")), bar, new Consumer<JSideBarEntry>() {				
				@Override
				public void accept(JSideBarEntry t) {
					page++;
				}
			}, SideBarEntryType.FORWARD,this);
			forward.setWidth(25);
			backward = new JSideBarEntry("",ImageIO.read(getClass().getResource("/ressources/images/back.png")), bar, new Consumer<JSideBarEntry>() {				
				@Override
				public void accept(JSideBarEntry t) {
					page--;
				}
			}, SideBarEntryType.BACKWARD,this);
			backward.setWidth(25);
			delete = new JSideBarEntry("",ImageIO.read(getClass().getResource("/ressources/images/close.png")), bar, new Consumer<JSideBarEntry>() {				
				@Override
				public void accept(JSideBarEntry t) {
					int returned = JOptionPane.showConfirmDialog(null, "This will delete " + t.getPack().getName() + " from your System");					
					if(returned == JOptionPane.OK_OPTION) {
						FileLoader.deletePack(t.getPack().getName() + ":" + t.getPack().getVersion());
					}					
				}
			}, SideBarEntryType.DELETE,this);
			delete.setWidth(40);
			delete.setHeight(40);
			this.updateUrl = config.getString("updateCheckUrl");
			if(updateUrl != null) {
				updateCheck = new JSideBarEntry("Check Updates",ImageIO.read(getClass().getResource("/ressources/images/refresh.png")), bar, new Consumer<JSideBarEntry>() {				
					@Override
					public void accept(JSideBarEntry t) {
						System.out.println("Check Updates " + t.getPack().getName() + "! " + "url = " + t.getPack().updateUrl);
						TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.WAIT_CURSOR));
						
						try {
							URL url = new URL(t.getPack().updateUrl);
							URLConnection urlConnection = url.openConnection();
							FileConfiguration config = new FileConfiguration(new Yaml(), urlConnection.getInputStream(), t.getPack().updateUrl);							
							String updateUrl = config.getSettings().get("updateUrl").toString();
							String updateVersion = config.getSettings().get("version").toString();
							
							if(updateUrl != null && updateVersion != null) {
								if(!updateVersion.toString().equalsIgnoreCase(t.getPack().getVersion().toString())) {
									int option = JOptionPane.showConfirmDialog(null, "Update '" + updateVersion + "' found, download ?","Update found",JOptionPane.OK_CANCEL_OPTION);
									if(option == JOptionPane.OK_OPTION) {
										try {
											BufferedInputStream in = new BufferedInputStream(new URL(updateUrl).openStream());
											String path = TwoTakeOneTool.getPackFolderBySelectedEntry(t.getPack()) + "\\" + t.getPack().getName() + "_" + updateVersion + ".2take1pack";
											FileOutputStream fileOutputStream = new FileOutputStream(path);
											byte dataBuffer[] = new byte[1024];
											int bytesRead;
											while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
												fileOutputStream.write(dataBuffer, 0, bytesRead);
											}
											fileOutputStream.flush();
											fileOutputStream.close();
											JOptionPane.showMessageDialog(null, "Successfully downloaded File to '" + path + "'","Success",JOptionPane.INFORMATION_MESSAGE);
										} catch (IOException e) {
											System.out.println("Error downloading '" + updateUrl + "'");
											JOptionPane.showMessageDialog(null, "Error downloading '" + updateUrl + "'");
										}
									}
								}else {
									JOptionPane.showMessageDialog(null, "Update File has the same Version as your local copy '" + t.getPack().getVersion() + "'");
								}
							}else {
								JOptionPane.showMessageDialog(null, "Error: No 'updateUrl' or 'version' found!","Error",JOptionPane.ERROR_MESSAGE);
							}							
						} catch (IOException e) {
							JOptionPane.showMessageDialog(null, "Error checking webpage '" + t.getPack().updateUrl + "'");
							e.printStackTrace();
						}
						TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					}
				}, SideBarEntryType.UPDATE,this);
			}
			
			desc = Description.fromDescriptionArray(config, this, bar);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		HashMap<String, Object> data = FileLoader.loadDataStorage().getSettings();
		@SuppressWarnings("unchecked")
		ArrayList<String> dataList = (ArrayList<String>) data.get("installedPack");
		
		if(dataList.contains(getName() + ":" + getVersion())) {
			//allready installed
			this.status = InstallStatus.INSTALLED;
			install.setText("deinstall");
			install.setWidth(110);
			install.setType(SideBarEntryType.DEINSTALL);
			install.setX(TwoTakeOneToolGui.instance.getWidth()-280);
			try {
				install.setImage(ImageIO.read(getClass().getResource("/ressources/images/close2.png")));
			} catch (IOException e) {
				e.printStackTrace();
			}
			install.setFunction(new Consumer<JSideBarEntry>() {				
				@Override
				public void accept(JSideBarEntry t) {
					FileLoader.deinstallPack(t.getPack().getName() + ":" + t.getPack().getVersion());
					HashMap<String, Object> data = FileLoader.loadDataStorage().getSettings();
					@SuppressWarnings("unchecked")
					ArrayList<String> dataList = (ArrayList<String>) data.get("installedPack");
					if(dataList == null) dataList = new ArrayList<>();
					dataList.remove(t.getPack().getName() + ":" + t.getPack().getVersion());
					FileLoader.loadDataStorage().storeData("installedPack", dataList);
				}
			});
		}
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
		g2.setColor(new Color(46,48,62));
		g2.fillRect(x, y, (TwoTakeOneToolGui.instance.getWidth()-bar.getWidth()-20), 400);
		
		//g2.setColor(new Color(41,44,56));
		g2.setColor(new Color(26,27,33));
		g2.fillRect(x, y, (TwoTakeOneToolGui.instance.getWidth()-bar.getWidth()-20), 40);
		
		g2.setColor(new Color(126,86,194));
		g2.setFont(new Font("Open Sans, Lucida Sans", Font.PLAIN, 30));
		//Name
		
		g2.drawString(name + " Version: " + version, bar.getWidth()+10, y+30);
		//Logo
		try {
			InputStream stream = config.loadImage(config.getString("Logo"));
			if(stream != null) {
				BufferedImage img = ImageIO.read(stream);
				g2.drawImage(img, bar.getWidth()+30, y+40, 50, 50, null);
			}				
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Beschreibung
		g2.setFont(new Font("Arial",Font.PLAIN,20));		
		desc.get(page).paint(g2);
		if(desc.size() > 1 && (page+1) < desc.size()) {
			this.forward.paint(g2, bar.getWidth()+35, y+380);
			g2.setColor(new Color(41,44,56));
			g2.drawRect(bar.getWidth()+35, y+355, forward.getWidth(), forward.getHeight() + 10);
			g2.setColor(new Color(126,86,194));
		}else {
			this.forward.paint(g2, -200, -200);
		}		
		if(page > 0) {
			this.backward.paint(g2, bar.getWidth()+10, y+380);
			g2.setColor(new Color(41,44,56));
			g2.drawRect(bar.getWidth()+10, y+355, backward.getWidth(), backward.getHeight() + 10);
			g2.setColor(new Color(126,86,194));
		}else {
			this.backward.paint(g2, -200, -200);
		}
		if(install.getType().equals(SideBarEntryType.INSTALL)) {

			g2.setColor(new Color(41,44,56));
			g2.drawRect(TwoTakeOneToolGui.instance.getWidth()-305, y+355, install.getWidth(), install.getHeight() + 10);
			g2.setColor(new Color(126,86,194));
			install.paint(g2, TwoTakeOneToolGui.instance.getWidth()-300, y+380);
		}else {
			g2.setColor(new Color(41,44,56));
			g2.drawRect(TwoTakeOneToolGui.instance.getWidth()-320, y+355, install.getWidth(), install.getHeight() + 10);
			g2.setColor(new Color(126,86,194));
			install.paint(g2, TwoTakeOneToolGui.instance.getWidth()-320, y+380);
		}		
		delete.paint(g2, TwoTakeOneToolGui.instance.getWidth()-55, y+20);
		g2.setColor(new Color(41,44,56));
		
		g2.drawRect(TwoTakeOneToolGui.instance.getWidth()-48, y+7, 25, 25);
		if(updateCheck != null) {
			g2.setColor(new Color(41,44,56));
			g2.drawRect(TwoTakeOneToolGui.instance.getWidth()-205, y+355, updateCheck.getWidth(), updateCheck.getHeight() + 10);
			g2.setColor(new Color(126,86,194));
			updateCheck.paint(g2, TwoTakeOneToolGui.instance.getWidth()-200, y+380);
		}		
	}
	@Override
	public int getX() {
		return this.x;
	}
	@Override
	public int getY() {
		return this.y;
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	public String getVersion() {
		return this.version;
	}
	public void close() throws IOException {
		install.close();
		//deinstall.close();
		delete.close();
		if(updateCheck != null) {
			updateCheck.close();
		}
		config.close();
	}

	public void install() {
		System.out.println("install " + this.name + " to folder " + TwoTakeOneTool.getInstallFolderBySelectedEntry(this).getAbsolutePath());
		try {
			config.install(TwoTakeOneTool.getInstallFolderBySelectedEntry(this));
			status = InstallStatus.INSTALLED;			
			install.setText("deinstall");
			install.setWidth(110);
			install.setType(SideBarEntryType.DEINSTALL);
			install.setX(TwoTakeOneToolGui.instance.getWidth()-280);
			install.setImage(ImageIO.read(getClass().getResource("/ressources/images/close2.png")));
			install.setFunction(new Consumer<JSideBarEntry>() {				
				@Override
				public void accept(JSideBarEntry t) {
					FileLoader.deinstallPack(t.getPack().getName() + ":" + t.getPack().getVersion());
					HashMap<String, Object> data = FileLoader.loadDataStorage().getSettings();
					@SuppressWarnings("unchecked")
					ArrayList<String> dataList = (ArrayList<String>) data.get("installedPack");
					if(dataList == null) dataList = new ArrayList<>();
					dataList.add(t.getPack().getName() + ":" + t.getPack().getVersion());
;					FileLoader.loadDataStorage().storeData("installedPack", dataList);
				}
			});
			this.repaint();
			JOptionPane.showMessageDialog(null, "Installed " + this.getName() + " Version: " + this.getVersion());
			} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void deinstall() {
		try {		
			config.deinstall(this);
			status = InstallStatus.READY_TO_INSTALL;			
			install.setText("install");
			install.setWidth(90);
			install.setType(SideBarEntryType.INSTALL);
			install.setX(TwoTakeOneToolGui.instance.getWidth()-300);
			install.setImage(ImageIO.read(getClass().getResource("/ressources/images/install2.png")));
			install.setFunction(new Consumer<JSideBarEntry>() {	
				@Override
				public void accept(JSideBarEntry t) {
					FileLoader.installPack(t.getPack().getName() + ":" + t.getPack().getVersion());
					HashMap<String, Object> data = FileLoader.loadDataStorage().getSettings();
					@SuppressWarnings("unchecked")
					ArrayList<String> dataList = (ArrayList<String>) data.get("installedPack");
					if(dataList == null) dataList = new ArrayList<>();
					dataList.remove(t.getPack().getName() + ":" + t.getPack().getVersion());
;					FileLoader.loadDataStorage().storeData("installedPack", dataList);
				}
			});
			this.repaint();
			JOptionPane.showMessageDialog(null, "Deinstalled " + this.getName() + " Version: " + this.getVersion());
			} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public SideBarEntryType getType() {
		return type;
	}

	public void setType(SideBarEntryType type) {
		this.type = type;
	}
}
