package me.zero.twotakeonetool;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.yaml.snakeyaml.Yaml;

import me.zero.twotakeonetool.config.FileConfiguration;
import me.zero.twotakeonetool.lang.Language;
import me.zero.twotakeonetool.lang.LanguageKey;
import me.zero.twotakeonetool.type.SideBarEntryType;
import me.zero.twotakeonetool.view.JSideBar;
import me.zero.twotakeonetool.view.TwoTakeOnePackView;
import me.zero.twotakeonetool.view.TwoTakeOneToolGui;

public class FileLoader {

	private static FileConfiguration settings,lang;
	private static HashMap<SideBarEntryType, String> lastLoadedFile = new HashMap<>();
	private static int startAmount = 3;

	private static ArrayList<String> webpacks = new ArrayList<>();

	private static Thread loadWebPack;

	private static String selected_lang = "English";
	
	public static FileConfiguration loadModFile(File file,SideBarEntryType type) {
		ZipFile zipFile;
		try {
			zipFile = new ZipFile(file);
			lastLoadedFile.put(type, file.getName());
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
		    while(entries.hasMoreElements()){
		        ZipEntry entry = entries.nextElement();
		        if(entry.getName().equalsIgnoreCase("install.yml")) {
		        	InputStream stream = zipFile.getInputStream(entry);
		        	FileConfiguration config = new FileConfiguration(new Yaml(),stream,zipFile, file.getAbsolutePath());
			        stream.close();
			        return config;
		        }
		    }
		} catch (ZipException e) {
			JOptionPane.showMessageDialog(null, Language.getTranslatedString(LanguageKey.PACK_ERROR).replace("<error>", e.getMessage()).replace("<name>", file.getName()),Language.getTranslatedString(LanguageKey.ERROR),JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("while loading package " + file.getName());
		}
		return null;
	}

	public static ArrayList<FileConfiguration> loadModDirectory(){
		ArrayList<FileConfiguration> files = new ArrayList<>();
		String[] sFiles = TwoTakeOneTool.scriptFolder.list();
		for(String sFile : sFiles) {
			if(sFile.endsWith(".2take1pack")) {
				files.add(loadModFile(new File(TwoTakeOneTool.scriptFolder.getAbsoluteFile() + "\\" + sFile), SideBarEntryType.SCRIPT));
			}
		}
		return files;
	}
	public static ArrayList<FileConfiguration> loadSpriteDirectory(){
		ArrayList<FileConfiguration> files = new ArrayList<>();
		String[] sFiles = TwoTakeOneTool.spriteFolder.list();
		int cnt = 0;
		for(String sFile : sFiles) {
			if(cnt > startAmount) {
				return files;
			}
			if(sFile.endsWith(".2take1pack")) {
				files.add(loadModFile(new File(TwoTakeOneTool.spriteFolder.getAbsoluteFile() + "\\" + sFile),  SideBarEntryType.SPRITE));
				cnt++;
			}
		}
		return files;
	}
	public static ArrayList<FileConfiguration> loadUIDirectory() {
		ArrayList<FileConfiguration> files = new ArrayList<>();
		String[] sFiles = TwoTakeOneTool.uiFolder.list();
		int cnt = 0;
		for(String sFile : sFiles) {
			if(cnt > startAmount) {
				return files;
			}
			if(sFile.endsWith(".2take1pack")) {
				files.add(loadModFile(new File(TwoTakeOneTool.uiFolder.getAbsoluteFile() + "\\" + sFile),  SideBarEntryType.UI));
				cnt++;
			}
		}
		return files;
	}
	public static ArrayList<FileConfiguration> loadProtectionDirectory(){
		ArrayList<FileConfiguration> files = new ArrayList<>();
		String[] sFiles = TwoTakeOneTool.protectionFolder.list();

		int cnt = 0;
		for(String sFile : sFiles) {
			if(cnt > startAmount) {
				return files;
			}
			if(sFile.endsWith(".2take1pack")) {
				files.add(loadModFile(new File(TwoTakeOneTool.protectionFolder.getAbsoluteFile() + "\\" + sFile),  SideBarEntryType.PROTECTION));
				cnt++;
			}
		}
		return files;
	}
	public static ArrayList<FileConfiguration> loadTeleportDirectory(){
		ArrayList<FileConfiguration> files = new ArrayList<>();
		String[] sFiles = TwoTakeOneTool.teleportFolder.list();

		int cnt = 0;
		for(String sFile : sFiles) {
			if(cnt > startAmount) {
				return files;
			}
			if(sFile.endsWith(".2take1pack")) {
				files.add(loadModFile(new File(TwoTakeOneTool.teleportFolder.getAbsoluteFile() + "\\" + sFile),  SideBarEntryType.TELEPORT));
				cnt++;
			}
		}
		return files;
	}
	public static ArrayList<FileConfiguration> loadAnimationDirectory(){
		ArrayList<FileConfiguration> files = new ArrayList<>();
		String[] sFiles = TwoTakeOneTool.animationFolder.list();

		int cnt = 0;
		for(String sFile : sFiles) {
			if(cnt > startAmount) {
				return files;
			}
			if(sFile.endsWith(".2take1pack")) {
				files.add(loadModFile(new File(TwoTakeOneTool.animationFolder.getAbsoluteFile() + "\\" + sFile),  SideBarEntryType.ANIMATION));
				cnt++;
			}
		}
		return files;
	}
	public static ArrayList<FileConfiguration> loadObjectDirectory(){
		ArrayList<FileConfiguration> files = new ArrayList<>();
		String[] sFiles = TwoTakeOneTool.objectFolder.list();

		int cnt = 0;
		for(String sFile : sFiles) {
			if(cnt > startAmount) {
				return files;
			}
			if(sFile.endsWith(".2take1pack")) {
				files.add(loadModFile(new File(TwoTakeOneTool.objectFolder.getAbsoluteFile() + "\\" + sFile),  SideBarEntryType.OBJECT));
				cnt++;
			}
		}
		return files;
	}
	public static ArrayList<FileConfiguration> loadStatDirectory(){
		ArrayList<FileConfiguration> files = new ArrayList<>();
		String[] sFiles = TwoTakeOneTool.statFolder.list();

		int cnt = 0;
		for(String sFile : sFiles) {
			if(cnt > startAmount) {
				return files;
			}
			if(sFile.endsWith(".2take1pack")) {
				files.add(loadModFile(new File(TwoTakeOneTool.statFolder.getAbsoluteFile() + "\\" + sFile),  SideBarEntryType.STAT));
				cnt++;
			}
		}
		return files;
	}
	public static ArrayList<FileConfiguration> loadVehicleDirectory(){
		ArrayList<FileConfiguration> files = new ArrayList<>();
		String[] sFiles = TwoTakeOneTool.vehicleFolder.list();

		int cnt = 0;
		for(String sFile : sFiles) {
			if(cnt > startAmount) {
				return files;
			}
			if(sFile.endsWith(".2take1pack")) {
				files.add(loadModFile(new File(TwoTakeOneTool.vehicleFolder.getAbsoluteFile() + "\\" + sFile),  SideBarEntryType.VEHICLE));
				cnt++;
			}
		}
		return files;
	}
	public static ArrayList<FileConfiguration> loadOutfitDirectory(){
		ArrayList<FileConfiguration> files = new ArrayList<>();
		String[] sFiles = TwoTakeOneTool.outfitFolder.list();

		int cnt = 0;
		for(String sFile : sFiles) {
			if(cnt > startAmount) {
				return files;
			}
			if(sFile.endsWith(".2take1pack")) {
				files.add(loadModFile(new File(TwoTakeOneTool.outfitFolder.getAbsoluteFile() + "\\" + sFile),  SideBarEntryType.OUTFIT));
				cnt++;
			}
		}
		return files;
	}
	public static ArrayList<FileConfiguration> loadLanguageDirectory(){
		ArrayList<FileConfiguration> files = new ArrayList<>();
		String[] sFiles = TwoTakeOneTool.languageFolder.list();

		int cnt = 0;
		for(String sFile : sFiles) {
			if(cnt > startAmount) {
				return files;
			}
			if(sFile.endsWith(".2take1pack")) {
				files.add(loadModFile(new File(TwoTakeOneTool.languageFolder.getAbsoluteFile() + "\\" + sFile),  SideBarEntryType.LANGUAGE));
				cnt++;
			}
		}
		return files;
	}
	public static ArrayList<FileConfiguration> loadFontDirectory(){
		ArrayList<FileConfiguration> files = new ArrayList<>();
		String[] sFiles = TwoTakeOneTool.fontFolder.list();

		int cnt = 0;
		for(String sFile : sFiles) {
			if(cnt > startAmount) {
				return files;
			}
			if(sFile.endsWith(".2take1pack")) {
				files.add(loadModFile(new File(TwoTakeOneTool.fontFolder.getAbsoluteFile() + "\\" + sFile),  SideBarEntryType.FONT));
				cnt++;
			}
		}
		return files;
	}
	public static ArrayList<FileConfiguration> loadConfigDirectory(){
		ArrayList<FileConfiguration> files = new ArrayList<>();
		String[] sFiles = TwoTakeOneTool.configFolder.list();

		int cnt = 0;
		for(String sFile : sFiles) {
			if(cnt > startAmount) {
				return files;
			}
			if(sFile.endsWith(".2take1pack")) {
				files.add(loadModFile(new File(TwoTakeOneTool.configFolder.getAbsoluteFile() + "\\" + sFile),  SideBarEntryType.CONFIG));
				cnt++;
			}
		}
		return files;
	}
	public static void deletePack(String name) {
		TwoTakeOnePackView pack = TwoTakeOneToolGui.instance.gui.pane.getPack(name);
		TwoTakeOneToolGui.instance.gui.pane.removePack(name);
		try {
			pack.close();
			Files.delete(new File(pack.config.getPath()).toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void installPack(String name) {
		TwoTakeOnePackView pack = TwoTakeOneToolGui.instance.gui.pane.getPack(name);
		pack.install();
	}
	public static String installWebPack(String name) {
		TwoTakeOnePackView pack = TwoTakeOneToolGui.instance.gui.pane.getPack(name);
		pack.install();
		//Copy Pack to right Folder
		try {
			pack.close();
			TwoTakeOneToolGui.instance.gui.pane.removePack(name);
			File target = new File(TwoTakeOneTool.getPackFolderBySelectedEntry(pack.getType()).getAbsolutePath() + "\\" + pack.getName().replace(" ", "_").replace(".2take1pack", "") + ".2take1pack");
			File source = new File(pack.config.getPath());
			System.out.println("copy " + source + "(" + source.exists() + ")" + " to " + target + "(" + target.exists() + ")");
			
			if(!target.exists()) {
				Files.move(
						source.toPath(),
						target.toPath(),
						StandardCopyOption.REPLACE_EXISTING);
			}			
			//return TwoTakeOneTool.getPackFolderBySelectedEntry(pack.getType()).getAbsolutePath() + "\\" + pack.getName().replace(" ", "_").replace(".2take1pack", "") + ".2take1pack";
			return target.getPath();
		}catch(IOException e) {
			e.printStackTrace();
		}		
		return null;
	}
	public static void deinstallPack(String name) {
		TwoTakeOnePackView pack = TwoTakeOneToolGui.instance.gui.pane.getPack(name);
		pack.deinstall();
	}

	public static FileConfiguration loadDataStorage() {
		if(settings == null) {
			try {
				settings = new FileConfiguration(new Yaml(),new FileInputStream(new File(TwoTakeOneTool.settingsFolder.getAbsolutePath() + "\\data.yml")), TwoTakeOneTool.settingsFolder.getAbsolutePath() + "\\data.yml");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return settings;
	}
	
	private static void selectLanguage() {
		//Select Language
		if(!new File(TwoTakeOneTool.settingsFolder.getAbsolutePath() + "/lang.yml").exists()) {
			JDialog dialog = new JDialog();
			JPanel panel = new JPanel(new BorderLayout());
			JLabel label = new JLabel("Please select a language");
			JComboBox<String> box = new JComboBox<>();
			box.addItem("English");
			box.addItem("Deutsch");
			box.addItem("French");
			JButton button = new JButton("Select");
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					selected_lang = box.getSelectedItem().toString();
					dialog.dispose();
				}
			});
			panel.add(label,BorderLayout.NORTH);
			panel.add(box,BorderLayout.CENTER);
			panel.add(button,BorderLayout.SOUTH);
			
			dialog.add(panel);
			dialog.setLocationRelativeTo(null);
			dialog.pack();
			dialog.setModal(true);
			dialog.setVisible(true);
		}
	}
	
	public static void installLang(String lang) {
		File langConf = new File(TwoTakeOneTool.settingsFolder.getAbsolutePath() + "\\lang.yml");
		
			try {
				langConf.createNewFile();
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileLoader().getClass().getResourceAsStream("/ressources/files/lang_" + lang + ".yml")));
				BufferedWriter writer = new BufferedWriter(new FileWriter(langConf));
				String line;
		        while ((line = reader.readLine()) != null) {
		            //resultStringBuilder.append(line).append("\n");
		        	writer.write(line);
		        	writer.newLine();
		        }
		        writer.close();
		        reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}					
		
	}
	
	public static FileConfiguration loadLanguageStorage() {
		File langConf = new File(TwoTakeOneTool.settingsFolder.getAbsolutePath() + "\\lang.yml");
		if(lang == null) {
			selectLanguage();
			try {
				installLang(selected_lang);
				lang = new FileConfiguration(new Yaml(),new FileInputStream(langConf), TwoTakeOneTool.settingsFolder.getAbsolutePath() + "\\data.yml");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return lang;
	}

	public static FileConfiguration loadNextPack(SideBarEntryType type,JSideBar bar) {
		if(type.equals(SideBarEntryType.WEB)) {
			return loadNextWebPack(type, bar);
		}
		ZipFile zipFile;
		String[] sFiles = TwoTakeOneTool.getPackFolderBySelectedEntry(bar.getSelectedEntry().getType()).list();
		boolean nextFile = false;
		for(String sFile : sFiles) {
			try {
				if(sFile.endsWith(".2take1pack") && FileLoader.lastLoadedFile.get(type) != null && FileLoader.lastLoadedFile.get(type).equalsIgnoreCase(sFile)) {
					nextFile = true;
				}else if(nextFile) {
					FileLoader.lastLoadedFile.put(type, sFile);
					File file = new File(TwoTakeOneTool.getPackFolderBySelectedEntry(bar.getSelectedEntry().getType()) + "\\" + sFile);
					zipFile = new ZipFile(file);
					lastLoadedFile.put(type, file.getName());
					Enumeration<? extends ZipEntry> entries = zipFile.entries();
					while(entries.hasMoreElements()){
						ZipEntry entry = entries.nextElement();
					    if(entry.getName().equalsIgnoreCase("install.yml")) {
					    	InputStream stream = zipFile.getInputStream(entry);
					        FileConfiguration config = new FileConfiguration(new Yaml(),stream,zipFile, file.getAbsolutePath());
						    stream.close();
						    return config;
					    }
					}
				}
			} catch (ZipException e) {
				e.printStackTrace();
				System.out.println("while loading package " + sFiles);
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("while loading package " + sFiles);
			}
		}
		return null;
	}

	private static FileConfiguration loadNextWebPack(SideBarEntryType type,JSideBar bar) {
		boolean next = false;
		for(String url : webpacks) {
			if(next) {
				FileConfiguration config;
				if(type.equals(SideBarEntryType.WEB)) {
					type = TwoTakeOneToolGui.instance.gui.pane.getTbar().getSelectedEntryType();
					config = loadWebPack(type, url);
				}else {
					config = loadWebPack(type, url);
				}
				TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				return config;
			}
			if (url.contains(lastLoadedFile.get(type))) {
				next = true;
			}
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public static void loadWebPacks(SideBarEntryType type, String webPackFile) {

		if(loadWebPack != null) {
			loadWebPack.interrupt();
			loadWebPack.stop();
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					TwoTakeOneToolGui.instance.gui.pane.clear();
					clearTempPack();
				}
			});

		}

		loadWebPack = new Thread(new Runnable() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				try {
					URL url = new URL(webPackFile);
					URLConnection urlConnection = url.openConnection();
					FileConfiguration config = new FileConfiguration(new Yaml(), urlConnection.getInputStream(), webPackFile);
					Object o = config.getSettings().get("packs");
					if(o != null) {
						if(o.getClass().equals(ArrayList.class)) {
							ArrayList<String> wpacks = (ArrayList<String>)o;
							for(String webpackUrl  : wpacks) {
								if(loadWebPack.isInterrupted()) {
									return;
								}else {
									if(TwoTakeOneToolGui.instance.gui.getToolBar().getSelectedEntryType().equals(type)) {
										FileConfiguration webPackconfig = loadWebPack(type, webpackUrl);
										if(webPackconfig != null) {
											TwoTakeOnePackView pack = new TwoTakeOnePackView(webPackconfig, TwoTakeOneToolGui.instance.gui.getSideBar(), type);
											pack.setWebPack(true);
											TwoTakeOneToolGui.instance.gui.pane.addTwoTakeOnePackView(pack);
											TwoTakeOneToolGui.instance.gui.repaint();
										}
									}else {
										loadWebPack.interrupt();
									}
								}
							}
							TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
						}else {
							System.out.println("unknown type " + o.getClass());
						}
					}else{
						System.out.println("corrupted webpack " + webPackFile);
					}
					
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}catch (IOException e) {
					JOptionPane.showMessageDialog(null, "An Error occured while connecting to '" + webPackFile + "'","Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		loadWebPack.start();
	}
	private static FileConfiguration loadWebPack(SideBarEntryType type, String webpackUrl) {
		if(webpackUrl.contains("packList.yml")) {
			return null;
		}
		webpackUrl = webpackUrl.replace(" ", "%20");
		TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		String[] splittedString = webpackUrl.split("/");
		String name = splittedString[splittedString.length-1].split(".2take1pack")[0] + ".2take1pack";
		try {
			BufferedInputStream in = new BufferedInputStream(new URL(webpackUrl).openStream());
			String path = TwoTakeOneTool.tempFolder + "\\" + name;
			FileOutputStream fileOutputStream = new FileOutputStream(path);
			byte dataBuffer[] = new byte[1024];
			int bytesRead;
			while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
				fileOutputStream.write(dataBuffer, 0, bytesRead);
			}
			fileOutputStream.flush();
			fileOutputStream.close();

			FileLoader.lastLoadedFile.put(type, path);
			File file = new File(path);
			@SuppressWarnings("resource")
			ZipFile zipFile = new ZipFile(file);
			lastLoadedFile.put(SideBarEntryType.WEB, file.getName());
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while(entries.hasMoreElements()){
				ZipEntry entry = entries.nextElement();
			    if(entry.getName().equalsIgnoreCase("install.yml")) {
			    	InputStream stream = zipFile.getInputStream(entry);
			        FileConfiguration config = new FileConfiguration(new Yaml(),stream,zipFile, file.getAbsolutePath());
				    stream.close();
				    return config;
			    }
			}
		} catch (IOException e) {
			
			JOptionPane.showMessageDialog(null, "Error downloading '" + webpackUrl + "'");
			System.out.println("Error downloading webpack '" + webpackUrl + "'");
		}
		return null;
	}
	public static void clearTempPack() {
		deleteDirectory(TwoTakeOneTool.tempFolder);
		TwoTakeOneTool.tempFolder.mkdir();
	}
	private static boolean deleteDirectory(File directoryToBeDeleted) {
	    File[] allContents = directoryToBeDeleted.listFiles();
	    if (allContents != null) {
	        for (File file : allContents) {
	            deleteDirectory(file);
	        }
	    }
	    return directoryToBeDeleted.delete();
	}

	/**
	 * Dont use this, it will fill your ram since it has to check every packet...
	 * @param name
	 * @param version
	 * @return
	 */
	public static TwoTakeOnePackView findPackByNameAndVersion(String name, String version) {
		File dir = TwoTakeOneTool.mainFolder;
		for(File packDir : dir.listFiles()) {
			if(packDir.isDirectory()) {
				for(File pack : packDir.listFiles()) {
					if(pack.getName().endsWith(".2take1pack")) {
						FileConfiguration config = FileLoader.loadModFile(pack, SideBarEntryType.LOAD);
						if(config != null) {
							TwoTakeOnePackView loadedPack = new TwoTakeOnePackView(config, TwoTakeOneToolGui.instance.gui.getSideBar(), SideBarEntryType.LOAD);
							if(loadedPack.getName().equals(name) && loadedPack.getVersion().equals(version)) {
								return loadedPack;
							}else {
								try {
									loadedPack.close();
									loadedPack = null;
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}						
					}
				}
			}
		}
		System.gc();
		return null;
	}

	
}
