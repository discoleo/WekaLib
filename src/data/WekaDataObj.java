/*
 *
 * Implementat de:
 *
 * 1.) Romina Doros - implementare initiala
 * in cadrul Lucrarii de Licenta, Sesiunea 2017-2018
 *
 * Universitatea de Vest, Timisoara
 *
 * in colaborare cu Syonic SRL
 * 
 * 2.) Leonard Mada
 * Refactoring & Split into separate module
 *
 * Copyright Syonic SRL & Leonard Mada
*/
package data;

import java.io.IOException;
import java.io.InputStream;

import weka.core.Instances;
import weka.core.converters.ArffLoader;

// wrapper around the weka Instances object;
// used in other programs to access Weka functions
// without directly including the weka libraries in those programs;
public class WekaDataObj {
	
	protected Instances data = null;
	
	public Instances GetData() {
		return data;
	}
	public void SetData(final Instances data) {
		this.data = data;
	}
	public void SetData(final InputStream is) {
		this.data = this.LoadData(is);
	}
	
	public boolean IsNull() {
		return (data == null);
	}

	public Instances LoadData(final InputStream is) {
		// final InputTblStream is = new InputTblStream(table, this.GetARFFHeader(table, nCol));
		//
		final ArffLoader loader = new ArffLoader();
		try {
			loader.setSource(is);
		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
		}
		
		final Instances dataset;
		try {
			dataset = loader.getDataSet();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return dataset;
	}
}
