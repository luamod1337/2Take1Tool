package me.zero.twotakeonetool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.yaml.snakeyaml.Yaml;

import me.zero.twotakeonetool.config.FileConfiguration;
import me.zero.twotakeonetool.controller.SidebarMouseListener;
import me.zero.twotakeonetool.type.SideBarEntryType;
import me.zero.twotakeonetool.view.JSideBar;
import me.zero.twotakeonetool.view.TwoTakeOnePackView;
import me.zero.twotakeonetool.view.TwoTakeOneToolGui;

public class FileLoader {

	private static FileConfiguration settings;
	private static HashMap<SideBarEntryType, String> lastLoadedFile = new HashMap<>();
	private static int startAmount = 3;
	
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
			e.printStackTrace();
			System.out.println("while loading package " + file.getName());
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

	public static FileConfiguration loadNextPack(SideBarEntryType type,JSideBar bar) {		
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
}
