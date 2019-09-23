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
 * - Refactoring, Split & Move into separate module;
 * - enable weights;
 *
 * Copyright Syonic SRL & Leonard Mada
*/
package data;

import java.io.InputStream;
import java.util.Vector;

import weka.classifiers.Classifier;
import weka.classifiers.rules.Prism;
import weka.classifiers.trees.J48;
import weka.core.Drawable;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;


public class WekaApiNative {
	
	// TODO: use count/frecventa
	// Add weights to data instances;
	// https://github.com/fracpete/dataset-weights-weka-package
	public void Weights(final WekaDataObj data, final Vector<Double> vWeights) {
		final Instances result = data.GetData();
		if(result.numInstances() > vWeights.size()) {
			// TODO: check len
		}
		
		// adds the weight to the existing instance;
		for (int i = 0; i < result.numInstances(); i++) {
			final Instance inst = (Instance) result.instance(i);
			inst.setWeight(vWeights.get(i));
		}
	}
	public Instances Weights(final Instances data, final Vector<Double> vWeights) {
		if(data.numInstances() > vWeights.size()) {
			// TODO: check len
		}
		
		final Instances result = new Instances(OutputFormat(data), data.numInstances());
		for (int i = 0; i < data.numInstances(); i++) {
			// uses Copy-construction
			final Instance inst = (Instance) data.instance(i).copy();
			inst.setWeight(vWeights.get(i));
			result.add(inst);
		}
		return result;
	}
	
	// Get the Format of the data
	public Instances OutputFormat(final Instances inputFormat) {
		// throws Exception
	    return new Instances(inputFormat, 0);
	}
	
	// ++++ Load Data ++++

	// Load the data
	public WekaDataObj LoadData(final InputStream is, final int nColClass) {
		// proper Weka
		final WekaDataObj wekaData = new WekaDataObj();
		wekaData.SetData(is);

		// setarea atributului de clasa (Stare Generala)
		wekaData.GetData().setClassIndex(nColClass);

		return wekaData;
	}
	public WekaDataObj LoadBinaryData(final InputStream is, final int nCol, final int idCol) {
		final WekaDataObj dataset = this.LoadData(is, nCol);
		return this.LoadBinaryData(dataset, idCol);
	}
	public WekaDataObj LoadBinaryData(final WekaDataObj dataset, final int idCol) {
		final Instances dataFiltered;
		if(idCol >= 0) {
			dataFiltered = this.Filter(dataset.GetData(), idCol);
		} else {
			dataFiltered = dataset.GetData();
		}
		if(dataFiltered == null) {
			System.out.println("Weka: NO data!");
		}
		dataset.SetData(dataFiltered);
		return dataset;
	}

	// Filter out columns: e.g. ID-column
	public Instances Filter(final Instances dataset, final int idCol) {
		return this.Filter(dataset, new int [] {idCol});
	}
	public Instances Filter(final Instances dataset, final int [] idCols) {
		// Filter
		final Remove fltRemove = new Remove();

		// final String optionsFilter [] = { "-R", "" + idCol };
		// fltRemove.setOptions(optionsFilter);
		fltRemove.setAttributeIndicesArray(idCols);
		fltRemove.setInvertSelection(false);
		try {
			fltRemove.setInputFormat(dataset);
			final Instances dataFiltered = Filter.useFilter(dataset, fltRemove);
			return dataFiltered;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// ++++ Display ++++
	
	public void Show(final Drawable graph, final String sName) {
		WekaGraphView.Create(graph, sName);
	}

	// ++++ Trees ++++

	public TreeObj J48(final WekaDataObj dataset, final String [] options) {
		final J48 tree = this.J48(dataset.GetData(), options);
		this.Show(tree, "Tree Simptome");
		// TODO: implement TreeObj
		return new TreeObj(tree);
	}
	public J48 J48(final Instances dataset, final String [] options) {
		try {
			final J48 tree = new J48();
			tree.setOptions(options);
			//
			tree.buildClassifier(dataset);
			// System.out.println(tree.getCapabilities().toString());
			// System.out.println(tree.graph());
			return tree;
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return null;
	}

	// library simpleeducationallearningschemes-1.0.2.jar
	public String Prism(final WekaDataObj dataFiltered) {
		if(dataFiltered == null) {
			return null;
		}
		// Extract Rules
		final String sRules = this.Prism(dataFiltered.GetData());
		return sRules;
	}
	public String Prism(final Instances dataset) {
		final Classifier cls = new Prism();
		try {
			cls.buildClassifier(dataset);
			// System.out.println(cls.toString());
			return cls.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
