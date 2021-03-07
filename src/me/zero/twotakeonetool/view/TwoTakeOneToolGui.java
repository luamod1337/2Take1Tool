package me.zero.twotakeonetool.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.yaml.snakeyaml.Yaml;

import me.zero.twotakeonetool.FileLoader;
import me.zero.twotakeonetool.TwoTakeOneTool;
import me.zero.twotakeonetool.config.FileConfiguration;
import me.zero.twotakeonetool.lang.Language;
import me.zero.twotakeonetool.lang.LanguageKey;

public class TwoTakeOneToolGui extends JFrame{

	private static final long serialVersionUID = 1L;
	public static TwoTakeOneToolGui instance;
	public JToolGuiComponent gui;
	private String version = "0.3";

	private List<MouseListener> listener = Collections.synchronizedList(new ArrayList<MouseListener>());
	private List<MouseWheelListener> listenerWheel = Collections.synchronizedList(new ArrayList<MouseWheelListener>());

	public TwoTakeOneToolGui() {
		TwoTakeOneToolGui.instance = this;
		this.setTitle("2Take1Tool by 1337Zero");
		this.setBackground(Color.WHITE);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(700,700));
		gui = new JToolGuiComponent(this);
		checkForUpdate();
		this.add(gui);

		try {
			ImageIcon icon = new ImageIcon(ImageIO.read(getClass().getResource("/ressources/images/logo.png")));
			this.setIconImage(icon.getImage());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void checkForUpdate() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				String updateUrlFile = "https://raw.githubusercontent.com/luamod1337/2Take1Tool/master/src/ressources/files/update.yml";
				try {
					URL url = new URL(updateUrlFile);
					URLConnection urlConnection = url.openConnection();
					FileConfiguration config = new FileConfiguration(new Yaml(), urlConnection.getInputStream(), updateUrlFile);
					String updateUrl = config.getSettings().get("updateUrl").toString();
					String updateVersion = config.getSettings().get("version").toString();
					//String path = TwoTakeOneTool.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath().toString() + "\\2Take1Tool_Update_" + updateVersion + ".jar";
					String path = TwoTakeOneTool.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath().toString();

					if(updateUrl != null && updateVersion != null) {
						if(!updateVersion.toString().equalsIgnoreCase(version)) {
							int option = JOptionPane.showConfirmDialog(null, Language.getTranslatedString(LanguageKey.UPDATE_FOUND).replace("<updateVersion>", updateVersion), Language.getTranslatedString(LanguageKey.UPDATE_FOUND_TITLE),JOptionPane.OK_CANCEL_OPTION);
							if(option == JOptionPane.OK_OPTION) {
								try {
									BufferedInputStream in = new BufferedInputStream(new URL(updateUrl).openStream());
									FileOutputStream fileOutputStream = new FileOutputStream(path);
									byte dataBuffer[] = new byte[1024];
									int bytesRead;
									while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
										fileOutputStream.write(dataBuffer, 0, bytesRead);
									}
									fileOutputStream.flush();
									fileOutputStream.close();
									JOptionPane.showMessageDialog(null, Language.getTranslatedString(LanguageKey.UPDATE_DOWNLOADED).replace("<path>", path),Language.getTranslatedString(LanguageKey.SUCCESSFULL),JOptionPane.INFORMATION_MESSAGE);
									
									//int restart = JOptionPane.showConfirmDialog(null, "2Take1Tool needs to be restarted, in order to complete the update, restart now ?","Reboot 2Take1Tool ?",JOptionPane.OK_CANCEL_OPTION);
									int restart = JOptionPane.showConfirmDialog(null, Language.getTranslatedString(LanguageKey.REQUIRED_RESTART),Language.getTranslatedString(LanguageKey.REQUIRED_RESTART_TITLE),JOptionPane.OK_CANCEL_OPTION);

									if(restart == JOptionPane.OK_OPTION) {
										System.exit(0);
									}

								} catch (IOException e) {
									e.printStackTrace();
									JOptionPane.showMessageDialog(null, Language.getTranslatedString(LanguageKey.DOWNLOAD_ERROR).replace("<updateUrl>", updateUrl));
								}
							}
						}
					}
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, Language.getTranslatedString(LanguageKey.ERROR_WEBPAGE).replace("<updateUrl>", updateUrlFile),Language.getTranslatedString(LanguageKey.ERROR),JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
		});
		t.start();
	}

	@Override
	public void addMouseListener(MouseListener list) {
		this.listener.add(list);
	}
	@Override
	public void removeMouseListener(MouseListener list) {
		this.listener.remove(list);
	}
	public void mouseClicked(MouseEvent e) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				for(int i = 0; i < listener.size(); i++){
					MouseListener list = listener.get(i);
					list.mouseClicked(e);
				}
			}
		});
	}
	public void mouseEntered(MouseEvent e) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				for(int i = 0; i < listener.size(); i++){
					MouseListener list = listener.get(i);
					list.mouseEntered(e);
				}
			}
		});
	}
	public void mouseExited(MouseEvent e) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				for(int i = 0; i < listener.size(); i++){
					MouseListener list = listener.get(i);
					list.mouseExited(e);
				}
			}
		});
	}
	public void mousePressed(MouseEvent e) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				for(int i = 0; i < listener.size(); i++){
					MouseListener list = listener.get(i);
					list.mousePressed(e);
				}
			}
		});
	}
	public void mouseReleased(MouseEvent e) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				for(int i = 0; i < listener.size(); i++){
					MouseListener list = listener.get(i);
					list.mouseReleased(e);
				}
			}
		});
	}
	public void mouseWheelMoved(MouseWheelEvent e) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				for(int i = 0; i < listenerWheel.size(); i++){
					MouseWheelListener list = listenerWheel.get(i);
					list.mouseWheelMoved(e);
				}
			}
		});
	}
}
