/*
 *
 * Implementat de:
 *
 * Denis Moisa - implementare initiala
 * in cadrul Lucrarii de Licenta, Sesiunea 2017-2018
 *
 * Universitatea de Vest, Timisoara
 *
 *
 * in colaborare cu Syonic SRL
 *
 * Copyright Syonic SRL & Leonard Mada
*/
package data;

import java.awt.BorderLayout;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import weka.core.Drawable;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;


public class WekaGraphView extends JFrame {
	
	public WekaGraphView(final Drawable graph, final String sTitle) {
		super(sTitle);
		//
		this.setSize(960, 680); // 
		this.getContentPane().setLayout(new BorderLayout());

		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				WekaGraphView.this.dispose();
			}
		});

		final TreeVisualizer tv = this.getTree(graph);
		if(tv != null) {
			this.getContentPane().add(tv, BorderLayout.CENTER);
			this.addHierarchyListener(
					new HierarchyListener() {
						@Override
						public void hierarchyChanged(HierarchyEvent arg0) {
							tv.fitToScreen();
						}});
		}

		this.setVisible(true);
		if(tv != null) {
			tv.fitToScreen();
		}
	}
	public static void Create(final Drawable graph, final String sTitle) {
		SwingUtilities.invokeLater(
				new Runnable() {
					@Override
					public void run() {
						new WekaGraphView(graph, sTitle);
					}
				});
	}
	
	protected TreeVisualizer getTree(final Drawable graph) {
		try {
			final String sGraph = graph.graph();
			final TreeVisualizer tv = new TreeVisualizer(null,
					sGraph,
					new PlaceNode2());

			return tv;
		} catch (Exception e1) {
			e1.printStackTrace();
			return null;
		}
	}
}
