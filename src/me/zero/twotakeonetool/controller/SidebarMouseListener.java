package me.zero.twotakeonetool.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import me.zero.twotakeonetool.view.TwoTakeOneToolGui;

public class SidebarMouseListener implements MouseListener,MouseWheelListener{
	
	@Override
	public void mouseClicked(MouseEvent e) {
		TwoTakeOneToolGui.instance.mouseClicked(e);
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
		TwoTakeOneToolGui.instance.mouseEntered(e);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		TwoTakeOneToolGui.instance.mouseExited(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		TwoTakeOneToolGui.instance.mousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		TwoTakeOneToolGui.instance.mouseReleased(e);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		TwoTakeOneToolGui.instance.mouseWheelMoved(e);
	}

}
