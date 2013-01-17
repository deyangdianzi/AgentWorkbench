/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.core.charts.xyChart.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.EtchedBorder;

import agentgui.core.application.Language;
import agentgui.core.ontologies.gui.DynForm;
import agentgui.core.ontologies.gui.OntologyClassWidget;
import agentgui.ontology.XyChart;

public class XyWidget extends OntologyClassWidget implements ActionListener {

	private static final long serialVersionUID = -9148282428244484633L;
	
	private XyChart currChart = null;  //  @jve:decl-index=0:
	private XyChartEditorJDialog xycejd = null;
	
	private JButton jButtonEdit = null;
	
	
	/**
	 * Instantiates a new xy widget.
	 *
	 * @param dynForm the {@link DynForm}
	 * @param startArgIndex the current start argument index
	 */
	public XyWidget(DynForm dynForm, int startArgIndex) {
		super(dynForm, startArgIndex);
		initialize();
	}
	
	/**
	 * This method initializes this.
	 */
	private void initialize() {
        
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        
        this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        
        this.setLayout(new GridBagLayout());
        this.add(getJButtonEdit(), gridBagConstraints);
			
	}

	/**
	 * This method initializes jButtonEdit.
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonEdit() {
		if (jButtonEdit == null) {
			jButtonEdit = new JButton();
			jButtonEdit.setText("Bearbeiten");
			jButtonEdit.setText(Language.translate(jButtonEdit.getText()));
			jButtonEdit.setToolTipText(Language.translate("Daten bearbeiten"));
			jButtonEdit.addActionListener(this);
			getJButtonEdit().setText("");
			getJButtonEdit().setIcon(new ImageIcon(this.getXyChartEditorJDialog().getChartThumb()));
		}
		return jButtonEdit;
	}
	
	/**
	 * @return the xycejd
	 */
	private XyChartEditorJDialog getXyChartEditorJDialog() {
		if(xycejd == null){
			xycejd = new XyChartEditorJDialog(dynForm, startArgIndex);
		}
		return xycejd;
	}

	/* (non-Javadoc)
	 * @see agentgui.core.charts.timeseriesChart.gui.OntologyClassWidget#setOntologyClassInstance(java.lang.Object)
	 */
	@Override
	public void setOntologyClassInstance(Object objectInstance) {
		this.currChart = (XyChart) objectInstance;
		ImageIcon icon = new ImageIcon(this.getXyChartEditorJDialog().getChartThumb());
		if(icon != null){
			// Replace text by thumbnail if available
			this.getJButtonEdit().setText("");
			this.getJButtonEdit().setIcon(icon);
		}
	}
	/* (non-Javadoc)
	 * @see agentgui.core.charts.timeseriesChart.gui.OntologyClassWidget#getOntologyClassInstance()
	 */
	@Override
	public Object getOntologyClassInstance() {
		return this.currChart;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		Object source = ae.getSource();
		if (source==jButtonEdit) {
			
			this.getXyChartEditorJDialog().setVisible(true);
//			if(! this.getXyChartDialog().isCanceled()){
				
				XyChart xyChart =  (XyChart) this.getXyChartEditorJDialog().getOntologyClassInstance();
				this.setNewOntologyClassInstance(xyChart);
				
				if(this.getXyChartEditorJDialog().getChartThumb() != null){
					getJButtonEdit().setText("");
					getJButtonEdit().setIcon(new ImageIcon(this.getXyChartEditorJDialog().getChartThumb()));
				}
//			}
		}

	}

}
