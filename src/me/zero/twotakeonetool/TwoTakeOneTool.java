package me.zero.twotakeonetool;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.yaml.snakeyaml.Yaml;

import me.zero.twotakeonetool.config.FileConfiguration;
import me.zero.twotakeonetool.lang.Language;
import me.zero.twotakeonetool.lang.LanguageKey;
import me.zero.twotakeonetool.type.SideBarEntryType;
import me.zero.twotakeonetool.view.TwoTakeOnePackView;
import me.zero.twotakeonetool.view.TwoTakeOneToolGui;

public class TwoTakeOneTool {


	public static File mainFolder = new File(System.getenv("APPDATA") + "\\2Take1Tool");
	public static File spriteFolder = new File(System.getenv("APPDATA") + "\\2Take1Tool\\sprite");
	public static File uiFolder = new File(System.getenv("APPDATA") + "\\2Take1Tool\\ui");
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
	public static File spriteFolderMod = new File(System.getenv("APPDATA") + "\\PopstarDevs\\2Take1Menu\\ui\\headers\\");
	public static File uiFolderMod = new File(System.getenv("APPDATA") + "\\PopstarDevs\\2Take1Menu\\ui\\");
	public static File cfgFolderMod = new File(System.getenv("APPDATA") + "\\PopstarDevs\\2Take1Menu\\cfg\\");
	public static File outfitFolderMod = new File(System.getenv("APPDATA") + "\\PopstarDevs\\2Take1Menu\\moddedOutfits\\");
	public static File vehicleFolderMod = new File(System.getenv("APPDATA") + "\\PopstarDevs\\2Take1Menu\\moddedVehicles\\");
	public static File languageFolderMod = new File(System.getenv("APPDATA") + "\\PopstarDevs\\2Take1Menu\\");
	public static File fontFolderMod = new File(System.getenv("APPDATA") + "\\PopstarDevs\\2Take1Menu\\ui\\fonts\\");
	public static File profileModFolder = new File(System.getenv("APPDATA") + "\\PopstarDevs\\2Take1Menu\\profiles\\");

	public static TwoTakeOneToolGui gui;
	
	public static void main(String[] args) {

		installIfNeeded();
		TwoTakeOneTool.gui = new TwoTakeOneToolGui();
		gui.setBounds(0, 0, 1000, 800);
		gui.setBackground(Color.WHITE);
		gui.setVisible(true);

		//FileChooser choose = new FileChooser();
		//FileLoader.loadModFolder(choose.showOpenDialog(null));

		HashMap<String, Object> data = FileLoader.loadDataStorage().getSettings();

		@SuppressWarnings("unchecked")
		ArrayList<HashMap<String, String>> dataList = (ArrayList<HashMap<String, String>>) data.get("installedPack");
		if(dataList == null) dataList = new ArrayList<>();
		try {
			for(HashMap<String, String> installedPack : dataList) {
				String Packpath = installedPack.get("Path");
				if(Packpath == null) {
					System.out.println("error checking " + installedPack.get("Name"));
					for(String key : installedPack.keySet()) {
						System.out.println(key + ": " + installedPack.get(key));
					}
				}
				File packFile = new File(Packpath);
				if(packFile.exists()) {
					TwoTakeOnePackView pack = new TwoTakeOnePackView(FileLoader.loadModFile(packFile, SideBarEntryType.LOAD), TwoTakeOneToolGui.instance.gui.getSideBar(), SideBarEntryType.LOAD);
					TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.WAIT_CURSOR));
					if(pack.updateUrl != null) {
						try {
							URL url = new URL(pack.updateUrl);
							URLConnection urlConnection = url.openConnection();
							FileConfiguration config = new FileConfiguration(new Yaml(), urlConnection.getInputStream(), pack.updateUrl);
							String updateUrl = config.getSettings().get("updateUrl").toString();
							String updateVersion = config.getSettings().get("version").toString();

							if(updateUrl != null && updateVersion != null) {
								if(!updateVersion.toString().equalsIgnoreCase(pack.getVersion().toString())) {
									//int option = JOptionPane.showConfirmDialog(null, "Update '" + updateVersion + "' found, download ?","Update found",JOptionPane.OK_CANCEL_OPTION);
									int option = JOptionPane.showConfirmDialog(null, Language.getTranslatedString(LanguageKey.UPDATE_FOUND).replace("<updateVersion>", updateVersion), Language.getTranslatedString(LanguageKey.UPDATE_FOUND_TITLE),JOptionPane.OK_CANCEL_OPTION);
									if(option == JOptionPane.OK_OPTION) {
										try {
											BufferedInputStream in = new BufferedInputStream(new URL(updateUrl).openStream());
											String path = TwoTakeOneTool.getPackFolderBySelectedEntry(pack) + "\\" + pack.getName() + "_" + updateVersion + ".2take1pack";
											FileOutputStream fileOutputStream = new FileOutputStream(path);
											byte dataBuffer[] = new byte[1024];
											int bytesRead;
											while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
												fileOutputStream.write(dataBuffer, 0, bytesRead);
											}
											fileOutputStream.flush();
											fileOutputStream.close();
											//JOptionPane.showMessageDialog(null, "Successfully downloaded File to '" + path + "'","Success",JOptionPane.INFORMATION_MESSAGE);
											JOptionPane.showMessageDialog(null, Language.getTranslatedString(LanguageKey.UPDATE_DOWNLOADED).replace("<path>", path),Language.getTranslatedString(LanguageKey.SUCCESSFULL),JOptionPane.INFORMATION_MESSAGE);
										} catch (IOException e) {
											//JOptionPane.showMessageDialog(null, "Error downloading '" + updateUrl + "'");
											JOptionPane.showMessageDialog(null, Language.getTranslatedString(LanguageKey.DOWNLOAD_ERROR).replace("<updateUrl>", updateUrl));
										}
									}
								}
							}else {
								//JOptionPane.showMessageDialog(null, "Error: No 'updateUrl' or 'version' found!","Error",JOptionPane.ERROR_MESSAGE);
								JOptionPane.showMessageDialog(null, Language.getTranslatedString(LanguageKey.UPDATE_CONFIGURATION_ERROR),Language.getTranslatedString(LanguageKey.ERROR),JOptionPane.ERROR_MESSAGE);
							}
							pack.close();
						} catch (IOException e) {
							//JOptionPane.showMessageDialog(null, "Error checking webpage '" + pack.updateUrl + "'");
							JOptionPane.showMessageDialog(null, Language.getTranslatedString(LanguageKey.ERROR_WEBPAGE).replace("<updateUrl>", pack.updateUrl),Language.getTranslatedString(LanguageKey.ERROR),JOptionPane.ERROR_MESSAGE);
							e.printStackTrace();
						}
					}
					pack = null;
					TwoTakeOneToolGui.instance.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}else {
					JOptionPane.showMessageDialog(null, Language.getTranslatedString(LanguageKey.ERROR_MISSING_PACKFILE).replace("<Packpath>", Packpath).replace("<packpath>", Packpath),Language.getTranslatedString(LanguageKey.ERROR),JOptionPane.ERROR_MESSAGE);
				}
			}
		}catch(ClassCastException ex) {
			//JOptionPane.showMessageDialog(null, "Your config is old and outdated, please reinstall packs to register them","Error",JOptionPane.ERROR_MESSAGE);
			JOptionPane.showMessageDialog(null, Language.getTranslatedString(LanguageKey.OUTDATE_CONFIG),Language.getTranslatedString(LanguageKey.ERROR),JOptionPane.ERROR_MESSAGE);
			try {
				FileLoader.loadDataStorage().close();
				File dataFile = new File(TwoTakeOneTool.settingsFolder.toPath() + "\\data.yml");
				Files.delete(dataFile.toPath());
				dataFile.createNewFile();

				BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile));
				writer.write("installedPack: []");
				writer.flush();
				writer.close();
				data.clear();
			} catch (IOException e) {
				e.printStackTrace();
				//JOptionPane.showMessageDialog(null, "Couldnt clear settings file!","Error",JOptionPane.ERROR_MESSAGE);
				JOptionPane.showMessageDialog(null, Language.getTranslatedString(LanguageKey.FILE_ERROR),Language.getTranslatedString(LanguageKey.ERROR),JOptionPane.ERROR_MESSAGE);
			}
		}
		
		
		if(args.length > 0) {
			String path = args[0];
			File f = new File(args[0]);
			if(f.exists()) {
				TwoTakeOnePackView pack = new TwoTakeOnePackView(FileLoader.loadModFile(new File(path), SideBarEntryType.LOAD), TwoTakeOneToolGui.instance.gui.getSideBar(), SideBarEntryType.LOAD);
				
				String type = pack.loadPackType();
				try {
					pack.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				if(type == null) {
					JDialog dialog = new JDialog();
					JPanel panel = new JPanel(new BorderLayout());
					JLabel label = new JLabel(Language.getTranslatedString(LanguageKey.SELECT_PACK_TYP));
					JComboBox<String> box = new JComboBox<>();
					
					for(SideBarEntryType typ : SideBarEntryType.values()) {
						box.addItem(typ.name());
					}
					JButton button = new JButton("Select");
					button.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							loadPackWithType(box.getSelectedItem().toString(), f);
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
				}else {
					loadPackWithType(type, f);
				}	
			}else {
				JOptionPane.showMessageDialog(null, Language.getTranslatedString(LanguageKey.ERROR_MISSING_PACKFILE).replace("<Packpath>", path),Language.getTranslatedString(LanguageKey.ERROR),JOptionPane.ERROR_MESSAGE);
			}
		}	
	}

	private static void loadPackWithType(String type,File file) {
		//File verschieben			
		SideBarEntryType packTyp = SideBarEntryType.valueOf(type.toUpperCase());
		File movedPack = new File(getInstallFolderBySelectedEntry(packTyp).getAbsolutePath() + "\\" + file.getName());
		try {
			Files.move(file.toPath(), movedPack.toPath());
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage(),Language.getTranslatedString(LanguageKey.ERROR),JOptionPane.ERROR_MESSAGE);
		}	
		TwoTakeOnePackView pack = new TwoTakeOnePackView(FileLoader.loadModFile(movedPack, packTyp), TwoTakeOneToolGui.instance.gui.getSideBar(), packTyp);
		int choose = JOptionPane.showConfirmDialog(null, Language.getTranslatedString(LanguageKey.QUESTION_INSTALL_PACK).replace("<pack>", pack.getName()),Language.getTranslatedString(LanguageKey.QUESTION_INSTALL_PACK),JOptionPane.YES_NO_OPTION);
		if(choose == JOptionPane.OK_OPTION) {
			pack.install();
		}
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

			JOptionPane.showMessageDialog(null, Language.getTranslatedString(LanguageKey.INSTALLED));
		}
		if(!uiFolder.exists()) {
			uiFolder.mkdir();
		}
	}

	public static File getInstallFolderBySelectedEntry(SideBarEntryType type) {
		if(type.equals(SideBarEntryType.SPRITE)) {
			return spriteFolderMod;
		}else if(type.equals(SideBarEntryType.SCRIPT)) {
			return scriptFolderMod;
		}else if(type.equals(SideBarEntryType.UI)) {
			return uiFolderMod;
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
			//JOptionPane.showMessageDialog(null, "Unknown install folder for type " + type.name());
			JOptionPane.showMessageDialog(null, Language.getTranslatedString(LanguageKey.UNKNOWN_TYP).replace("<typ>", type.name()));
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
		}else if(type.equals(SideBarEntryType.UI)) {
			return uiFolder;
		}else if(type.equals(SideBarEntryType.VEHICLE)) {
			return vehicleFolder;
		}else if(type.equals(SideBarEntryType.LANGUAGE)) {
			return languageFolder;
		}else if(type.equals(SideBarEntryType.FONT)) {
			return fontFolder;
		}else if(type.equals(SideBarEntryType.OUTFIT)) {
			return outfitFolderMod;
		}else if(type.equals(SideBarEntryType.TELEPORT)) {
			return teleportFolder;
		}else if(type.equals(SideBarEntryType.ANIMATION)) {
			return animationFolder;
		}else if(type.equals(SideBarEntryType.STAT)) {
			return statFolder;
		}else if(type.equals(SideBarEntryType.OBJECT)) {
			return objectFolder;
		}else if(type.equals(SideBarEntryType.CONFIG)) {
			return configFolder;
		}else if(type.equals(SideBarEntryType.PROTECTION)) {
			return protectionFolder;
		}else {
			JOptionPane.showMessageDialog(null, Language.getTranslatedString(LanguageKey.UNKNOWN_TYP).replace("<typ>", type.name()));
		}
		return null;
	}
	public static File getPackFolderBySelectedEntry(TwoTakeOnePackView twoTakeOnePackView) {
		return getPackFolderBySelectedEntry(twoTakeOnePackView.getType());
	}

	public static String getIconUrlBySidebarEntry(SideBarEntryType type) {
		if(type.equals(SideBarEntryType.SPRITE) || type.equals(SideBarEntryType.UI)) {
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
			JOptionPane.showMessageDialog(null, Language.getTranslatedString(LanguageKey.UNKNOWN_TYP).replace("<typ>", type.name()));
		}
		return null;
	}
}
