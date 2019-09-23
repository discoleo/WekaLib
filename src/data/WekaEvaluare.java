/*
 *
 * Implementat de:
 *
 * Romina Doros - implementare initiala
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

import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.rules.Prism;
import weka.core.Instances;

public class WekaEvaluare <C extends Classifier> {
	
	private final C cls;
	
	public WekaEvaluare(final C cls) {
		this.cls = cls;
	}
	
	public static WekaEvaluare<Prism> GetPrismEvaluator() {
		return new WekaEvaluare<> (new Prism());
	}
	public static void EvaluatePrism(final WekaDataObj dataFiltered) {
		new WekaEvaluare<> (new Prism()).Evaluate(dataFiltered);
	}
	
	// ++++++++ MEMBER FUNCTIONS +++++++++++

	public void Evaluate(final WekaDataObj dataFiltered) {
		this.Evaluate(dataFiltered.GetData());
	}
	public void Evaluate(final Instances dataFiltered) {
		try {
			final int folds = 5;
			final int rndSeed = 1;
			
			final Random rand = new Random(rndSeed);
			final Instances randData = new Instances(dataFiltered);
			randData.randomize(rand);
			randData.stratify(folds);
			
			Instances train = null;
			Instances test  = null;

			for (int idFold = 0; idFold < folds; idFold++) {
				System.out.println("processing fold = " + idFold);
				train = randData.trainCV(folds, idFold);
				test  = randData.testCV(folds, idFold);
				// Evaluate
				this.Evaluate(train, test);
				// Predict
				// this.Classify(train, test);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void Evaluate(final Instances train, final Instances test) {
		// Evaluare
		if(test == null) {
			System.out.println("NO Test Data");
			return;
		}
		try {
			cls.buildClassifier(train);
			final Evaluation eval = new Evaluation(train);
			eval.evaluateModel(cls, test);

			final String sMsg = "\nResults\n======\n";
			final String sEval = eval.toSummaryString(sMsg, false) + "\n"
					+ eval.toMatrixString();

			this.Display(sEval);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void Classify(final Instances train, final Instances test) {
		// Predict class type: aplicare clasificator
		try {
			cls.buildClassifier(train);
			//
			for (int i = 0; i < test.numInstances(); i++) {
				double clsLabel = cls.classifyInstance(test.instance(i));
				test.instance(i).setClassValue(clsLabel);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void Display(final String sEval) {
		final JFrame frame = new JFrame("Evaluare Prism");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500,500);


		final JTextArea txt = new JTextArea();

		txt.setBounds(300, 300, 300, 300);
		
		txt.setText(sEval);
		txt.setVisible(true);
		txt.setEditable(false);
		frame.add(txt);
		frame.requestFocus();
		frame.requestFocusInWindow();
		frame.setVisible(true);
	}
}
	


