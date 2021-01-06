package me.zero.twotakeonetool.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import me.zero.twotakeonetool.config.FileConfiguration;
import me.zero.twotakeonetool.type.SideBarEntryType;
import me.zero.twotakeonetool.view.JSideBar;
import me.zero.twotakeonetool.view.TwoTakeOnePackView;
import me.zero.twotakeonetool.view.TwoTakeOneToolGui;

public class Description {

	private HashMap<Integer, String> posTostrings = new HashMap<>();
	private HashMap<Integer, BufferedImage> posToimages = new HashMap<>();
	private int length = 0;
	private TwoTakeOnePackView pack;
	private JSideBar bar;
	
	public Description(HashMap<Integer, String> posTostrings,HashMap<Integer, BufferedImage> posToimages,int length,TwoTakeOnePackView pack,JSideBar bar) {
		this.length = length;
		this.posToimages = posToimages;
		this.posTostrings = posTostrings;
		this.bar = bar;
		this.pack = pack;
	}	
	
	public static ArrayList<Description> fromDescriptionArray(FileConfiguration config,TwoTakeOnePackView pack,JSideBar bar){
		//System.out.println(config.getPath());
		ArrayList<Description> desc = new ArrayList<>();		
		ArrayList<Object> data = config.getList("Description");
		int totalOffset = 0;
		HashMap<Integer, String> posTostrings = new HashMap<>();
		HashMap<Integer, BufferedImage> posToimages = new HashMap<>();
		int length = 0;
				
		for(int i  = 0; i < data.size();i++) {
			if(totalOffset > 500) {
				desc.add(new Description(posTostrings, posToimages, length, pack, bar));
				posTostrings = new HashMap<>();
				posToimages = new HashMap<>();
				length = 0;
				totalOffset = 0;
			}
			Object o = data.get(i);
			if(o.getClass().equals(String.class)) {
				posTostrings.put(length++, o.toString());
				totalOffset+=60;
			}else if(o.getClass().equals(LinkedHashMap.class)){
				@SuppressWarnings("unchecked")
				HashMap<String, Object> subdata = (LinkedHashMap<String, Object>)o;						
				String imagePath = (String) subdata.get("Image");
				try {
					InputStream stream = config.loadImage(imagePath);
					if(stream != null) {
						BufferedImage img = ImageIO.read(stream);		
						posToimages.put(length++, img);
						totalOffset+=img.getHeight();
					}
				} catch (IOException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Couldn't find image '" + imagePath + "' inside pack!");
				}				
			}else {
				System.out.println("unprocessed class: " + o.getClass());
			}			
		}	
		//Letzte Seite
		desc.add(new Description(posTostrings, posToimages, length, pack, bar));
		posTostrings = new HashMap<>();
		posToimages = new HashMap<>();
		length = 0;
		return desc;
	}
	
	public void paint(Graphics2D g2) {
		int imageOffset = 0;		
		Font font = new Font("Open Sans, Lucida Sans", Font.PLAIN, 20);
		g2.setFont(font);
		for(int i  = 0; i < length;i++) {			
			if(posTostrings.containsKey(i)) {
				//draw String
				g2.drawString(posTostrings.get(i), bar.getWidth()+100, pack.getY()+60 + (i*20) + imageOffset);
			}else {
				//draw Image	
				if(pack.getType().equals(SideBarEntryType.SPRITE)) {					
					int width = (TwoTakeOneToolGui.instance.getWidth() - bar.getWidth() - 480)/2;
					g2.setColor(Color.WHITE);
					g2.drawRect(bar.getWidth()+ width-5, pack.getY()+154 + (i*20) + imageOffset - 5, 485, 97);
					g2.drawImage(posToimages.get(i), bar.getWidth()+ width, pack.getY()+154 + (i*20) + imageOffset, 480, 92, null);
					imageOffset += 92;
				}else if(pack.getType().equals(SideBarEntryType.VEHICLE) || pack.getType().equals(SideBarEntryType.OUTFIT) || pack.getType().equals(SideBarEntryType.FONT)){
					double ratio = 250.0/posToimages.get(i).getHeight();
					int newWidth = (int) (posToimages.get(i).getWidth()*ratio);
					int newHeight = (int) (posToimages.get(i).getHeight()*ratio);
					g2.drawImage(posToimages.get(i), bar.getWidth()+ 100, pack.getY()+60 + (i*20) + imageOffset, newWidth, newHeight, null);
					imageOffset += 92;
				}else {
					g2.drawImage(posToimages.get(i), bar.getWidth()+100, pack.getY()+60 + (i*20) + imageOffset, null);
					imageOffset += posToimages.get(i).getHeight();
				}
			}
		}	
	}
	
	
}
