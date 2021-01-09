package me.zero.twotakeonetool.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import me.zero.twotakeonetool.view.JSideBar;
import me.zero.twotakeonetool.view.JSideBarEntry;
import me.zero.twotakeonetool.view.TwoTakeOneToolGui;

public class SideBarEntryMouseListener implements MouseListener{

	private JSideBarEntry entry;
	private JSideBar bar;
	
	public SideBarEntryMouseListener(JSideBarEntry entry,JSideBar bar) {
		this.entry = entry;
		this.bar = bar;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {	
		
		if(e.getPoint().x < (this.entry.getX() + this.entry.getWidth()) && e.getPoint().x > this.entry.getX()) {
			if(e.getPoint().y < (this.entry.getY() + this.entry.getHeight()) && e.getPoint().y > this.entry.getY()) {
				this.entry.onClick();
				bar.getParent().repaint();
			}
		}
		/*System.out.println("-----");
		System.out.println("click at " + e.getPoint());
		System.out.println("sidebar x = 0," + TwoTakeOneToolGui.instance.gui.getSideBar().getWidth());
		System.out.println("sidebar y = " + TwoTakeOneToolGui.instance.gui.getSideBar().getEntryYStart() + "," + TwoTakeOneToolGui.instance.gui.getSideBar().getEntryYEnd());
		System.out.println("/-----");
		System.out.println();*/
		//Just for Sidebar Check
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}
}
