package trainer;

import java.util.HashMap;
import java.util.Map;

import rbm.ClassRBM;
import rbm.Connection;
import rbm.RBM;
import data.Data;
import data.Datapoint;
import rbm.Error;

public class TrainerCD extends Trainer {
	
	Map<Connection, Integer> positive;
	Map<Connection, Integer> negative;
	double learningRate = 0.5;
	double errorThreshold = 0.1;
	int maxSteps = 500;
	
	public TrainerCD(RBM rbm, Data data) {
		super(rbm, data);
	}
	
	public void trainData(int epochs) {
		for (int epoch = 0; epoch < epochs; epoch++) {
			System.out.println("Epoch: "+epoch);
			int failures = 0;
			for (int i = 0; i < data.numDatapoints(); i++) {
				Datapoint datapoint = data.get(i);
				int[] vector = data.vectorLabeled(datapoint);
				int step = 0;
				while (Error.mse(vector, rbm.sample(vector)) > errorThreshold && step++ < maxSteps)
					trainDataPoint(datapoint);
				double error = Error.mse(vector, rbm.sample(vector));
				if (error > errorThreshold) {
					failures++;
				} else {
					if (img != null)
						img.showImage(rbm.readVisible());
				}
				
			}
			System.out.println("FAILURES: "+failures);
			//  500: 92 46 27
			// 1000: 77 50 73 18
		}
	}
	
	public void trainDataPoint(Datapoint datapoint) {
		rbm.setVisibleNodes(data.vectorLabeled(datapoint));
		rbm.hidden.sample();
		positive = getGradient();
		rbm.visible.sample();
		rbm.hidden.sample();
		negative = getGradient();
		updateWeights();
	}
	
	private void updateWeights() {
		for (Connection connection : rbm.connections)
			connection.updateWeight(learningRate*(positive.get(connection) - negative.get(connection)));
	}
	
	/**
	 * Counts the number of instances where connected units are both on and increments in map
	 */
	public Map<Connection, Integer> getGradient() {
		Map<Connection, Integer> map = new HashMap<>();
		for (Connection connection : rbm.connections) {
			if (connection.getNeuronA().on() && connection.getNeuronB().on()) {
				map.put(connection, 1);
			} else {
				map.put(connection, 0);
			}
		}
		return map;
	}

}
