package me.zero.twotakeonetool.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.swing.JOptionPane;

import org.yaml.snakeyaml.Yaml;

import me.zero.twotakeonetool.TwoTakeOneTool;
import me.zero.twotakeonetool.lang.Language;
import me.zero.twotakeonetool.lang.LanguageKey;
import me.zero.twotakeonetool.view.TwoTakeOnePackView;

public class FileConfiguration {

	private Iterable<Object> itr;

	private HashMap<String, ArrayList<Object>> lists = new HashMap<>();
	private HashMap<String, Double> listDouble = new HashMap<>();
	private HashMap<String, String> listString = new HashMap<>();
	private ZipFile zipFile;
	private String path;
	private Yaml yaml;
	private HashMap<String, Object> data;
	private InputStream stream;

	public FileConfiguration(Yaml yaml,InputStream stream, ZipFile zipFile,String path) {
		itr = yaml.loadAll(stream);
		loadData(path);
		this.zipFile = zipFile;
		this.path = path;
		this.yaml = yaml;
		this.stream = stream;
	}
	public FileConfiguration(Yaml yaml,InputStream stream,String path) {
		//itr = yaml.loadAll(stream);
		//loadData();
		this.path = path;
		this.yaml = yaml;
		data = yaml.load(stream);
		if(data == null) {
			data = new HashMap<>();
		}
		this.stream = stream;
	}

	public HashMap<String, Object> getSettings(){
		return data;
	}

	public void storeData(String key,Object gData) {
		data.put(key, gData);
		try {
			yaml.dump(data, new FileWriter(new File(path)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void loadData(String file) {
		try {
			for (Object o : itr) {
	            LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>)o;
	            if(o == null) {
	            	JOptionPane.showMessageDialog(null, Language.getTranslatedString(LanguageKey.ERROR_LOADING).replace("<file>", file).replace("<object>", o.toString()));
	            }else {
	            	for(String keys : data.keySet()) {
	            		if( data.get(keys) == null) {
	                		//JOptionPane.showMessageDialog(null, "Error loading '" + file + "' -> " + o);
	                		JOptionPane.showMessageDialog(null, Language.getTranslatedString(LanguageKey.ERROR_LOADING).replace("<file>", file).replace("<object>", o.toString()));
	                	}else {
	                		if(data.get(keys).getClass().equals(ArrayList.class)) {
	                    		lists.put(keys, (ArrayList<Object>) data.get(keys));
	                    	}else if(data.get(keys).getClass().equals(Double.class)){
	                    		listDouble.put(keys, Double.valueOf(data.get(keys).toString()));
	                    	}else if(data.get(keys).getClass().equals(String.class)){
	                    		listString.put(keys, data.get(keys).toString());
	                    	}else {
	                    		System.out.println("unprocessed type found " + data.get(keys).getClass());
	                    	}
	                	}
	                }
	            }
	        }
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		}
	}

	public ArrayList<Object> getList(String key){
		return lists.get(key);
	}
	public Double getDouble(String key) {
		return listDouble.get(key);
	}
	public String getString(String key) {
		return listString.get(key);
	}

	public InputStream loadImage(String imagePath) throws IOException {
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		while(entries.hasMoreElements()){
	        ZipEntry entry = entries.nextElement();
	        if(entry.getName().equalsIgnoreCase(imagePath)) {
	        	return zipFile.getInputStream(entry);
	        }
	    }
		return null;
	}
	public InputStream loadFile(String imagePath) throws IOException {
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		while(entries.hasMoreElements()){
	        ZipEntry entry = entries.nextElement();
	        if(entry.getName().equalsIgnoreCase(imagePath)) {
	        	return zipFile.getInputStream(entry);
	        }
	    }
		return null;
	}
	public String getPath() {
		return path;
	}

	public void close() throws IOException {
		if(zipFile != null) {
			this.zipFile.close();
		}
		if(stream != null) {
			stream.close();
		}
	}

	public void install(File folder) throws IOException {
		ArrayList<Object> copyFolder = getList("CopyFolder");
		ArrayList<Object> CopyFiles = getList("CopyFiles");

		ZipInputStream zis = new ZipInputStream(new FileInputStream(this.path));
        ZipEntry zipEntry = zis.getNextEntry();
        byte[] buffer = new byte[1024];
	    while (zipEntry != null) {
	    	File newFile = new File(folder.getAbsolutePath() + "\\" + zipEntry.getName());
	        if (copyFolder != null && zipEntry.isDirectory() && isInList(zipEntry.getName(), copyFolder)) {
	        	if (!newFile.isDirectory() && !newFile.mkdirs()) {
	            	zis.close();
	        		throw new IOException("Failed to create directory " + newFile);
	        	}
	        } else if((CopyFiles != null && isInList(zipEntry.getName(), CopyFiles)) || (copyFolder != null && isInList(zipEntry.getName(), copyFolder))){
	        	// fix for Windows-created archives
	            File parent = newFile.getParentFile();
	            if (!parent.isDirectory() && !parent.mkdirs()) {
	            	zis.close();
	            	throw new IOException("Failed to create directory " + parent);
	            }
	            // write file content
	            FileOutputStream fos = new FileOutputStream(newFile);
	            int len;
	            while ((len = zis.read(buffer)) > 0) {
	            	fos.write(buffer, 0, len);
	            }
	            fos.close();
	        }
	        zipEntry = zis.getNextEntry();
	    }
	    zis.close();
	}
	public void deinstall(TwoTakeOnePackView pack) throws IOException {
		ArrayList<Object> copyFolder = getList("CopyFolder");
		ArrayList<Object> CopyFiles = getList("CopyFiles");
		if(CopyFiles != null) {
			for(Object o : CopyFiles) {
				try {
					Files.delete(new File(TwoTakeOneTool.getInstallFolderBySelectedEntry(pack).getAbsolutePath() + "\\" + o.toString()).toPath());
				}catch(NoSuchFileException e) {
					//JOptionPane.showMessageDialog(null, "Couldn't delete file, did it got deleted manually ?","Error",JOptionPane.ERROR_MESSAGE);
					JOptionPane.showMessageDialog(null, Language.getTranslatedString(LanguageKey.ERROR_MISSING_PACKFILE).replace("<packpath>", pack.getName()),"Error",JOptionPane.ERROR_MESSAGE);
				}

			}
		}
		if(copyFolder != null) {
			for(Object o : copyFolder) {
				deleteDirectory(new File(TwoTakeOneTool.scriptFolderMod.getAbsolutePath() + "\\" + o.toString()));
			}
		}
	}
	private boolean isInList(String str,ArrayList<Object> data) {
		if(data == null) {
			return false;
		}
		for(Object o : data) {
			if(str.contains(o.toString())) {
				return true;
			}
		}
		return false;
	}
	private boolean deleteDirectory(File directoryToBeDeleted) {
	    File[] allContents = directoryToBeDeleted.listFiles();
	    if (allContents != null) {
	        for (File file : allContents) {
	            deleteDirectory(file);
	        }
	    }
	    return directoryToBeDeleted.delete();
	}
}
