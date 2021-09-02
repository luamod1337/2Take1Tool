package me.zero.twotakeonetool.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
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
import me.zero.twotakeonetool.util.PackHelper;
import me.zero.twotakeonetool.view.TwoTakeOnePackView;
import net.sf.sevenzipjbinding.ExtractOperationResult;
import net.sf.sevenzipjbinding.IInArchive;
import net.sf.sevenzipjbinding.ISequentialOutStream;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.SevenZipException;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
import net.sf.sevenzipjbinding.simple.ISimpleInArchiveItem;

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
	            			//JOptionPane.showMessageDialog(null, Language.getTranslatedString(LanguageKey.ERROR_LOADING).replace("<file>", file).replace("<object>", o.toString()));
	            			System.out.println(Language.getTranslatedString(LanguageKey.ERROR_LOADING).replace("<file>", file).replace("<object>", o.toString()));
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

		
		
		
		System.out.println("install called");
		ZipInputStream zis = new ZipInputStream(new FileInputStream(this.path));
        ZipEntry zipEntry = zis.getNextEntry();
        byte[] buffer = new byte[1024];
	    while (zipEntry != null) {
	    	File newFile = new File(folder.getAbsolutePath() + "\\" + zipEntry.getName());
            System.out.println("processing: " + newFile);
	        if (copyFolder != null && zipEntry.isDirectory() && isInList(zipEntry.getName(), copyFolder)) {
	        	if (!newFile.isDirectory() && !newFile.mkdirs()) {
	            	zis.close();
	        		throw new IOException("Failed to create directory " + newFile);
	        	}
	        } else if((CopyFiles != null && isInList(zipEntry.getName(), CopyFiles)) || (copyFolder != null && isInList(zipEntry.getName(), copyFolder))){
	        	// fix for Windows-created archives
	        	File parent = newFile.getParentFile();
	            if (parent.isDirectory() && !parent.exists() && !parent.mkdirs()) {
	            	zis.close();
	            	throw new IOException("Failed to create directory " + parent);
	            }
	            
	            if(!newFile.exists()) {
	            	System.out.println("create " + newFile);
	            	newFile.createNewFile();
	            }
	            // write file content
	            FileOutputStream fos = new FileOutputStream(newFile);
	            System.out.println("writting " + newFile);
	            int len;
	            while ((len = zis.read(buffer)) > 0) {
	            	fos.write(buffer, 0, len);
	            }
	            fos.close();
	            
	            if(zipEntry.getName().endsWith(".zip")) {	            	
	            	//installZipFile(newFile);
	            	installRarFile(newFile);
	            }else if(zipEntry.getName().endsWith(".rar")){
	            	installRarFile(newFile);
	            }else if(zipEntry.getName().endsWith(".7zip")){
	            	installRarFile(newFile);
	            }
	        }
	        zipEntry = zis.getNextEntry();
	    }
	    zis.close();
	}
	public void installRarFile(File folder) {
		try {
			RandomAccessFile randomAccessFile = new RandomAccessFile(folder.getPath().toString(), "r");
			IInArchive archive = SevenZip.openInArchive(null,new RandomAccessFileInStream(randomAccessFile));
		    //BufferedWriter writer = new BufferedWriter(new FileWriter(new File("test.ini")));
		    for(ISimpleInArchiveItem s : archive.getSimpleInterface().getArchiveItems()) {
		    	if (!s.isFolder()) {
                    processZipedFile(s);
                }
		    }		    
		    archive.close();
		    randomAccessFile.close();
			folder.delete();
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	public void processZipedFile(ISimpleInArchiveItem s) throws IOException {
		ExtractOperationResult result;
    	final int[] hash = new int[] { 0 };    	
        final long[] sizeArray = new long[1];
                
        if(s.getPath().endsWith(".dds")){
        	File dds = new File(TwoTakeOneTool.spriteFolderMod + File.separator + PackHelper.createOldFileName(this.listString.getOrDefault("Name", "Error")).replace(".zip", "").replace(".rar", "") + ".dds");	
        	//BufferedWriter writer = new BufferedWriter(new FileWriter(dds));
        	FileOutputStream writer = new FileOutputStream(dds);
        	if(dds.exists()) {	
	            int awnser = JOptionPane.showConfirmDialog(null, "There is allready a dds file, override ?","Warning",JOptionPane.YES_NO_OPTION);				            
	            if(awnser == JOptionPane.YES_OPTION) {
	            	result = s.extractSlow(new ISequentialOutStream() {
	                    public int write(byte[] data) throws SevenZipException {
	                        hash[0] ^= Arrays.hashCode(data); // Consume data
	                        sizeArray[0] += data.length;
	                        try {
								//writer.write(new String(data, StandardCharsets.UTF_8));
	                        	writer.write(data);								
							} catch (IOException e) {
								e.printStackTrace();
							}
	                        return data.length; // Return amount of consumed data
	                    }
	                });				            
	            }else {
	            	System.out.println("dds skipped!");
	            }
        	}else {
        		result = s.extractSlow(new ISequentialOutStream() {
                    public int write(byte[] data) throws SevenZipException {
                        hash[0] ^= Arrays.hashCode(data); // Consume data
                        sizeArray[0] += data.length;
                        //System.out.println(new String(data, StandardCharsets.UTF_8));
                        sizeArray[0] += data.length;
                        try {
							//writer.write(new String(data, StandardCharsets.UTF_8));
                        	writer.write(data);
						} catch (IOException e) {
							e.printStackTrace();
						}
                        return data.length; // Return amount of consumed data
                    }
                });
        	}
        	writer.flush();
        	writer.close();
        }else if(s.getPath().contains("lang.ini")){
        	File lang = new File(TwoTakeOneTool.languageFolderMod + File.separator + "lang.ini");	
        	BufferedWriter writer = new BufferedWriter(new FileWriter(lang));
        	if(lang.exists()) {
        		result = s.extractSlow(new ISequentialOutStream() {
                    public int write(byte[] data) throws SevenZipException {
                        hash[0] ^= Arrays.hashCode(data); // Consume data
                        sizeArray[0] += data.length;
                        //System.out.println(new String(data, StandardCharsets.UTF_8));
                        sizeArray[0] += data.length;
                        try {
							writer.write(new String(data, StandardCharsets.UTF_8));
						} catch (IOException e) {
							e.printStackTrace();
						}
                        return data.length; // Return amount of consumed data
                    }
                });
        	}else {
        		int awnser = JOptionPane.showConfirmDialog(null, "Pack includes a language file, do you want to copy that?","Warning",JOptionPane.YES_NO_OPTION);				            
	            if(awnser == JOptionPane.YES_OPTION) {
	            	result = s.extractSlow(new ISequentialOutStream() {
	                    public int write(byte[] data) throws SevenZipException {
	                        hash[0] ^= Arrays.hashCode(data); // Consume data
	                        sizeArray[0] += data.length;
	                        //System.out.println(new String(data, StandardCharsets.UTF_8));
	                        sizeArray[0] += data.length;
	                        try {
								writer.write(new String(data, StandardCharsets.UTF_8));
							} catch (IOException e) {
								e.printStackTrace();
							}
	                        return data.length; // Return amount of consumed data
	                    }
	                });				            
	            }
        	}
        	writer.flush();
        	writer.close();        	
        }else if(s.getPath().endsWith(".ini")){
        	String profileName = TwoTakeOneTool.uiFolderMod + File.separator + PackHelper.createOldFileName(this.listString.getOrDefault("Name", "Error")).replace(".zip", "").replace(".rar", "") + ".ini";
        	File profile = new File(profileName);		            	
        	BufferedWriter writer = new BufferedWriter(new FileWriter(profile));
        	result = s.extractSlow(new ISequentialOutStream() {
                public int write(byte[] data) throws SevenZipException {
                    hash[0] ^= Arrays.hashCode(data); // Consume data
                    sizeArray[0] += data.length;
                    //System.out.println(new String(data, StandardCharsets.UTF_8));
                    sizeArray[0] += data.length;
                    try {
						writer.write(new String(data, StandardCharsets.UTF_8));
					} catch (IOException e) {
						e.printStackTrace();
					}
                    return data.length; // Return amount of consumed data
                }
            });	
        	
        	//Edit the profile to use the altered header
            ArrayList<String> file = new ArrayList<String>();			            
            BufferedReader reader = new BufferedReader(new FileReader(profile));
            String readed = reader.readLine();
            while(readed != null) {			            	
            	file.add(readed);			            	
            	readed = reader.readLine();
            }
            
            reader.close();
            BufferedWriter writer2 = new BufferedWriter(new FileWriter(profile));			            
            for(String read : file) {
            	if(read.contains("header_lbl=")) {
            		writer2.write("header_lbl=" + PackHelper.createOldFileName(this.listString.getOrDefault("Name", "Error")).replace(".zip", "").replace(".rar", ""));
            	}else {
            		writer2.write(read);
            	}
            	writer2.newLine();
            }
            writer2.flush();
            writer2.close();
            writer.flush();
            writer.close();
        }
        
	}
	
	public void deinstall(TwoTakeOnePackView pack) throws IOException {		
		for(String f : getInstalledFilesFromZippedFile(this.path)) {
			f = f.replace(" ", "_");
			if(f.endsWith(".dds")) {
				File dds = new File(TwoTakeOneTool.spriteFolderMod + File.separator + PackHelper.createOldFileName(this.listString.getOrDefault("Name", "Error")).replace(".zip", "").replace(".rar", "") + ".dds");	
				dds.delete();
			}else if(f.endsWith(".ini")) {
				if(f.contains("lang.ini")) {
					File lang = new File(TwoTakeOneTool.languageFolderMod + File.separator + "lang.ini");	
					lang.delete();
				}else {
		        	File profile = new File(TwoTakeOneTool.uiFolderMod + File.separator + PackHelper.createOldFileName(this.listString.getOrDefault("Name", "Error")).replace(".zip", "").replace(".rar", "") + ".ini");
		        	profile.delete();
				}
			}else {
				System.out.println("unknown filetyp " + f);
			}
		}
	}
	
	private ArrayList<String> getInstalledFilesFromZippedFile(String zipped) {
		ArrayList<String> toDelete = new ArrayList<String>();
		try {
			RandomAccessFile randomAccessFile = new RandomAccessFile(zipped, "r");
			IInArchive archive = SevenZip.openInArchive(null,new RandomAccessFileInStream(randomAccessFile));
		    //BufferedWriter writer = new BufferedWriter(new FileWriter(new File("test.ini")));
		    for(ISimpleInArchiveItem s : archive.getSimpleInterface().getArchiveItems()) {
		    	if (!s.isFolder()) {
		    		if(s.getPath().toString().endsWith(".rar") || s.getPath().toString().endsWith(".zip") || s.getPath().toString().endsWith(".7zip")) {
		    			final int[] hash = new int[] { 0 };   
		    			final long[] sizeArray = new long[1];
		    			
		    			//BufferedWriter writer = new BufferedWriter(new FileWriter(new File("C:\\temp\temp\\" + s.getPath())));
		    			File path = new File("C:\\temp");
		    			File output = new File("C:\\temp\\" + s.getPath());
		    			if(!path.exists()) {
		    				path.mkdirs();
		    			}
		    			if(!output.exists()) {
		    				output.createNewFile();
		    			}
		    			FileOutputStream writer = new FileOutputStream(output);
		    			//unzip temp
		    			ExtractOperationResult result = s.extractSlow(new ISequentialOutStream() {
		                    public int write(byte[] data) throws SevenZipException {		                    	
		                        hash[0] ^= Arrays.hashCode(data); // Consume data
		                        sizeArray[0] += data.length;
		                        //System.out.println(new String(data, StandardCharsets.UTF_8));
		                        sizeArray[0] += data.length;
		                        try {
									//writer.write(new String(data, StandardCharsets.UTF_8));
		                        	writer.write(data);
								} catch (IOException e) {
									e.printStackTrace();
								}
		                        return data.length; // Return amount of consumed data
		                    }
		                });
		    			writer.flush();
		    			writer.close();
		    			
		    			 for(String file : getInstalledFilesFromZippedFile(output)) {
		    				 toDelete.add(file);
		    			 }
		    			 
		    			//delete temp
		    			output.delete();
		    		}else {
		    			if(s.getPath().contains("install.yml")) {
		    				
		    				File path = new File("C:\\temp");
			    			File output = new File("C:\\temp\\" + s.getPath());
			    			if(!path.exists()) {
			    				path.mkdirs();
			    			}
			    			if(!output.exists()) {
			    				output.createNewFile();
			    			}
		    				
		    				FileOutputStream writer = new FileOutputStream(output);
		    				final int[] hash = new int[] { 0 };   
			    			final long[] sizeArray = new long[1];
			    			//unzip temp
			    			ExtractOperationResult result = s.extractSlow(new ISequentialOutStream() {
			                    public int write(byte[] data) throws SevenZipException {		                    	
			                        hash[0] ^= Arrays.hashCode(data); // Consume data
			                        sizeArray[0] += data.length;
			                        //System.out.println(new String(data, StandardCharsets.UTF_8));
			                        sizeArray[0] += data.length;
			                        try {
										//writer.write(new String(data, StandardCharsets.UTF_8));
			                        	writer.write(data);
									} catch (IOException e) {
										e.printStackTrace();
									}
			                        return data.length; // Return amount of consumed data
			                    }
			                });
			    			writer.flush();
			    			writer.close();
			    			
			    			loadInstallYml(output);
			    			
			    			 toDelete.add(s.getPath());		
			    			
			    			output.delete();
			    			
		    			}else {
		    				System.out.println("content not processed: " + s.getPath());
		    			}
		    		}                    
                }
		    }		    
		    archive.close();
		    randomAccessFile.close();
		} catch (IOException e) {			
			e.printStackTrace();
		}
		return toDelete;	
	}
	private void loadInstallYml(File f) {
		
	}
	private ArrayList<String> getInstalledFilesFromZippedFile(File zip) {
		ArrayList<String> filesToDelete = new ArrayList<String>();
		try {
			RandomAccessFile randomAccessFile = new RandomAccessFile(zip, "r");
			IInArchive archive = SevenZip.openInArchive(null,new RandomAccessFileInStream(randomAccessFile));
		    //BufferedWriter writer = new BufferedWriter(new FileWriter(new File("test.ini")));
		    for(ISimpleInArchiveItem s : archive.getSimpleInterface().getArchiveItems()) {
		    	if (!s.isFolder()) {		    		          
		    		if(s.getPath().endsWith(".dds")){
		    			filesToDelete.add(s.getPath());
		            }else if(s.getPath().contains("lang.ini")){
		    			filesToDelete.add(s.getPath());
		            }else if(s.getPath().endsWith(".ini")){
		    			filesToDelete.add(s.getPath());
		            }
                }
		    }		    
		    archive.close();
		    randomAccessFile.close();
		} catch (IOException e) {			
			e.printStackTrace();
		}
		return filesToDelete;
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
