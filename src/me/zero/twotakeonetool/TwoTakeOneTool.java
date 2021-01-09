package me.zero.twotakeonetool;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import me.zero.twotakeonetool.type.SideBarEntryType;
import me.zero.twotakeonetool.view.TwoTakeOnePackView;
import me.zero.twotakeonetool.view.TwoTakeOneToolGui;

public class TwoTakeOneTool {

	private static TwoTakeOneToolGui gui = new TwoTakeOneToolGui();

	public static File mainFolder = new File(System.getenv("APPDATA") + "\\2Take1Tool");
	public static File spriteFolder = new File(System.getenv("APPDATA") + "\\2Take1Tool\\sprite");
	public static File scriptFolder = new File(System.getenv("APPDATA") + "\\2Take1Tool\\script");
	public static File protectionFolder = new File(System.getenv("APPDATA") + "\\2Take1Tool\\protection");
	public static File teleportFolder = new File(System.getenv("APPDATA") + "\\2Take1Tool\\teleport");
	public static File statFolder = new File(System.getenv("APPDATA") + "\\2Take1Tool\\stat");
	public static File vehicleFolder = new File(System.getenv("APPDATA") + "\\2Take1Tool\\vehicle");
	public static File outfitFolder = new File(System.getenv("APPDATA") + "\\2Take1Tool\\outfit");
	public static File objectFolder = new File(System.getenv("APPDATA") + "\\2Take1Tool\\object");
	public static File animationFolder = new File(System.getenv("APPDATA") + "\\2Take1Tool\\animation");
	public static File fontFolder = new File(System.getenv("APPDATA") + "\\2Take1Tool\\font");
	public static File languageFolder = new File(System.getenv("APPDATA") + "\\2Take1Tool\\language");
	public static File configFolder = new File(System.getenv("APPDATA") + "\\2Take1Tool\\config");
	public static File tempFolder = new File(System.getenv("APPDATA") + "\\2Take1Tool\\temp");
	
	public static File settingsFolder = new File(System.getenv("APPDATA") + "\\2Take1Tool\\settings");
	

	public static File scriptFolderMod = new File(System.getenv("APPDATA") + "\\PopstarDevs\\2Take1Menu\\scripts\\");
	public static File spriteFolderMod = new File(System.getenv("APPDATA") + "\\PopstarDevs\\2Take1Menu\\sprites\\");
	public static File cfgFolderMod = new File(System.getenv("APPDATA") + "\\PopstarDevs\\2Take1Menu\\cfg\\");
	public static File outfitFolderMod = new File(System.getenv("APPDATA") + "\\PopstarDevs\\2Take1Menu\\moddedOutfits\\");
	public static File vehicleFolderMod = new File(System.getenv("APPDATA") + "\\PopstarDevs\\2Take1Menu\\moddedVehicles\\");
	public static File languageFolderMod = new File(System.getenv("APPDATA") + "\\PopstarDevs\\2Take1Menu\\");
	public static File fontFolderMod = new File(System.getenv("APPDATA") + "\\PopstarDevs\\");

	
	
	public static void main(String[] args) {
		
		
		installIfNeeded();
		
		gui.setBounds(0, 0, 1000, 800);
		gui.setBackground(Color.WHITE);
		gui.setVisible(true);
		
		//FileChooser choose = new FileChooser();		
		//FileLoader.loadModFolder(choose.showOpenDialog(null));
	}
	
	
	private static void installIfNeeded() {
		if(!mainFolder.exists()) {
			//installieren
			mainFolder.mkdir();
			
			spriteFolder.mkdir();
			scriptFolder.mkdir();
			protectionFolder.mkdir();
			teleportFolder.mkdir();
			statFolder.mkdir();
			vehicleFolder.mkdir();
			outfitFolder.mkdir();
			objectFolder.mkdir();
			animationFolder.mkdir();
			fontFolder.mkdir();
			languageFolder.mkdir();
			configFolder.mkdir();
			tempFolder.mkdir();

			settingsFolder.mkdir();
			try {
				new File(settingsFolder.getAbsolutePath() + "\\data.yml").createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			JOptionPane.showMessageDialog(null, "Installation of 2Take1Tool successfull!");
		}
	}
	
	public static File getInstallFolderBySelectedEntry(SideBarEntryType type) {
		if(type.equals(SideBarEntryType.SPRITE)) {
			return spriteFolderMod;
		}else if(type.equals(SideBarEntryType.SCRIPT)) {
			return scriptFolderMod;
		}else if(type.equals(SideBarEntryType.VEHICLE)) {
			return vehicleFolderMod;
		}else if(type.equals(SideBarEntryType.LANGUAGE) || type.equals(SideBarEntryType.CONFIG)) {
			return languageFolderMod;
		}else if(type.equals(SideBarEntryType.FONT)) {
			return fontFolderMod;
		}else if(type.equals(SideBarEntryType.OUTFIT)) {
			return outfitFolderMod;
		}else if(type.equals(SideBarEntryType.PROTECTION) || type.equals(SideBarEntryType.ANIMATION) || type.equals(SideBarEntryType.STAT) || type.equals(SideBarEntryType.TELEPORT) || type.equals(SideBarEntryType.OBJECT)) {
			return cfgFolderMod;
		}else {
			JOptionPane.showMessageDialog(null, "Unknown install folder for type " + type.name());
		}
		return null;
	}
	public static File getInstallFolderBySelectedEntry(TwoTakeOnePackView twoTakeOnePackView) {
		return getInstallFolderBySelectedEntry(twoTakeOnePackView.getType());
	}
	public static File getPackFolderBySelectedEntry(SideBarEntryType type) {
		if(type.equals(SideBarEntryType.SPRITE)) {
			return spriteFolder;
		}else if(type.equals(SideBarEntryType.SCRIPT)) {
			return scriptFolder;
		}else if(type.equals(SideBarEntryType.VEHICLE)) {
			return vehicleFolder;
		}else if(type.equals(SideBarEntryType.LANGUAGE) || type.equals(SideBarEntryType.CONFIG)) {
			return languageFolder;
		}else if(type.equals(SideBarEntryType.FONT)) {
			return fontFolder;
		}else if(type.equals(SideBarEntryType.OUTFIT)) {
			return outfitFolderMod;
		}else if(type.equals(SideBarEntryType.PROTECTION) || type.equals(SideBarEntryType.ANIMATION) || type.equals(SideBarEntryType.STAT) || type.equals(SideBarEntryType.TELEPORT) || type.equals(SideBarEntryType.OBJECT)) {
			return configFolder;
		}else {
			JOptionPane.showMessageDialog(null, "Unknown install folder for type " + type.name());
		}
		return null;
	}
	public static File getPackFolderBySelectedEntry(TwoTakeOnePackView twoTakeOnePackView) {
		return getPackFolderBySelectedEntry(twoTakeOnePackView.getType());
	}
	
	public static String getIconUrlBySidebarEntry(SideBarEntryType type) {
		if(type.equals(SideBarEntryType.SPRITE)) {
			return "/ressources/images/image.png";
		}else if(type.equals(SideBarEntryType.SCRIPT)) {
			return "/ressources/images/lua.png";
		}else if(type.equals(SideBarEntryType.VEHICLE)) {
			return "/ressources/images/car.png";
		}else if(type.equals(SideBarEntryType.CONFIG)) {
			return "/ressources/images/config.png";
		}else if(type.equals(SideBarEntryType.LANGUAGE)) {
			return "/ressources/images/language.png";
		}else if(type.equals(SideBarEntryType.FONT)) {
			return "/ressources/images/font.png";
		}else if(type.equals(SideBarEntryType.OUTFIT)) {
			return "/ressources/images/outfit.png";
		}else if(type.equals(SideBarEntryType.PROTECTION)) {
			return "/ressources/images/protection.png";
		}else if(type.equals(SideBarEntryType.ANIMATION)) {
			return "/ressources/images/animation.png";
		}else if(type.equals(SideBarEntryType.STAT)) {
			return "/ressources/images/stats.png";
		}else if(type.equals(SideBarEntryType.TELEPORT) ) {
			return "/ressources/images/map.png";
		}else if(type.equals(SideBarEntryType.OBJECT)) {
			return "/ressources/images/object.png";
		}else {
			JOptionPane.showMessageDialog(null, "Unknown install folder for type " + type.name());
		}
		return null;
	}
}
