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
	// https://github.com/fracpete/dataset-weights-weka-package
	public void Weights(final WekaDataNative data, final Vector<Double> vWeights) {
		final Instances result = data.GetData();
		for (int i = 0; i < result.numInstances(); i++) {
			final Instance inst = (Instance) result.instance(i);
			inst.setWeight(vWeights.get(i));
		}
	}
	public Instances Weights(final Instances data, final Vector<Double> vWeights) {
		// TODO: check len
		final Instances result = new Instances(OutputFormat(data), data.numInstances());
		for (int i = 0; i < data.numInstances(); i++) {
			// uses Copy-construction
			final Instance inst = (Instance) data.instance(i).copy();
			inst.setWeight(vWeights.get(i));
			result.add(inst);
		}
		return result;
	}
	public Instances OutputFormat(final Instances inputFormat) {
		// throws Exception
	    return new Instances(inputFormat, 0);
	  }

	public WekaDataNative LoadData(final InputStream is, final int nCol) {
		// proper Weka
		final WekaDataNative wekaData = new WekaDataNative();
		wekaData.SetData(is);

		// setarea atributului de clasa (Stare Generala)
		wekaData.GetData().setClassIndex(nCol);

		return wekaData;
	}
	public WekaDataNative LoadBinaryData(final InputStream is, final int nCol, final int idCol) {
		final WekaDataNative dataset = this.LoadData(is, nCol);
		return this.LoadBinaryData(dataset, idCol);
	}
	public WekaDataNative LoadBinaryData(final WekaDataNative dataset, final int idCol) {
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

	public Instances Filter(final Instances dataset, final int idCol) {
		// Filter
		final Remove fltRemove = new Remove();

		// final String optionsFilter [] = { "-R", "" + idCol };
		// fltRemove.setOptions(optionsFilter);
		fltRemove.setAttributeIndicesArray(new int [] {idCol});
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

	public TreeObj J48(final WekaDataNative dataset, final String [] options) {
		// TODO: implement TreeObj
		return new TreeObj(this.J48(dataset.GetData(), options));
	}
	public J48 J48(final Instances dataset, final String [] options) {
		try {
			final J48 tree = new J48();
			tree.setOptions(options);
			//
			tree.buildClassifier(dataset);
			// System.out.println(tree.getCapabilities().toString());
			// System.out.println(tree.graph());
			this.Show(tree, "Tree Simptome");
			return tree;
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return null;
	}

	public String Prism(final WekaDataNative dataFiltered) {
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
