
package com.indago.tr2d.plugins.seg;

import com.indago.tr2d.ui.model.Tr2dModel;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.integer.IntType;
import org.scijava.Context;
import org.scijava.log.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;

public class LabkitPanel {

	private final Logger log;
	private final SegmentationComponent segmentation;
	private List<RandomAccessibleInterval<IntType>> outputs;

	public LabkitPanel(Context context, Tr2dModel model, Logger log) {
		this.log = log;
		segmentation = createSegmentationComponent(context, model);
	}

	private SegmentationComponent createSegmentationComponent(Context context,
		Tr2dModel model)
	{
		try {
			return new SegmentationComponent(context, model.getRawData());
		}
		catch (NoClassDefFoundError e) {
			return null;
		}
	}

	public JPanel getPanel() {
		JPanel panel = new JPanel();
		JButton store = new JButton("recalculate");
		store.addActionListener(l -> this.calculateOutputs());
		panel.setLayout(new BorderLayout());
		if (isUsable()) panel.add(segmentation.getComponent());
		panel.add(store, BorderLayout.PAGE_END);
		return panel;
	}

	private void calculateOutputs() {
		outputs = isUsable() ? segmentation.getSegmentations(new IntType())
			: Collections.emptyList();
	}

	public boolean isUsable() {
		return segmentation != null;
	}

	public List<RandomAccessibleInterval<IntType>> getOutputs() {
		if (outputs == null || outputs.isEmpty()) calculateOutputs();
		return outputs;
	}
}
