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
		//Just for Sidebar Check
		if(e.getPoint().x < TwoTakeOneToolGui.instance.gui.getSideBar().getWidth()) {
			if(e.getPoint().y < TwoTakeOneToolGui.instance.gui.getSideBar().getEntryYStart() || e.getPoint().y > TwoTakeOneToolGui.instance.gui.getSideBar().getEntryYEnd()) {
				TwoTakeOneToolGui.instance.gui.getSideBar().setOpened(!TwoTakeOneToolGui.instance.gui.getSideBar().isOpen());
				TwoTakeOneToolGui.instance.gui.getSideBar().repaint();
				return;
			}
		}
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
