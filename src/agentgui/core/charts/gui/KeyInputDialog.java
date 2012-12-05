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
package agentgui.core.charts.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;

public abstract class KeyInputDialog extends JDialog implements ActionListener {
	protected JLabel lblMessage;
	protected JButton btnConfirm;
	protected JButton btnCancel;
	
	private boolean canceled = false;

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 3531606096232250974L;
	
	public KeyInputDialog(Window owner){
		super(owner);
	}
	
	protected void initialize(String message){
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		GridBagConstraints gbc_lblMessage = new GridBagConstraints();
		gbc_lblMessage.insets = new Insets(0, 0, 5, 0);
		gbc_lblMessage.gridx = 0;
		gbc_lblMessage.gridy = 0;
		gbc_lblMessage.anchor = GridBagConstraints.EAST;
		getContentPane().add(getLblMessage(message), gbc_lblMessage);
		GridBagConstraints gbc_InputComponent = new GridBagConstraints();
		gbc_InputComponent.fill = GridBagConstraints.HORIZONTAL;
		gbc_InputComponent.insets = new Insets(0, 0, 5, 0);
		gbc_InputComponent.gridx = 1;
		gbc_InputComponent.gridy = 0;
		getContentPane().add(getInputComponent(), gbc_InputComponent);
		GridBagConstraints gbc_btnConfirm = new GridBagConstraints();
		gbc_btnConfirm.insets = new Insets(0, 0, 0, 5);
		gbc_btnConfirm.gridx = 0;
		gbc_btnConfirm.gridy = 1;
		getContentPane().add(getBtnConfirm(), gbc_btnConfirm);
		GridBagConstraints gbc_btnCancel = new GridBagConstraints();
		gbc_btnCancel.gridx = 1;
		gbc_btnCancel.gridy = 1;
		getContentPane().add(getBtnCancel(), gbc_btnCancel);

		pack();
		setVisible(true);
	}
	protected abstract JComponent getInputComponent();
	protected JLabel getLblMessage(String message) {
		if (lblMessage == null) {
			lblMessage = new JLabel(message);
		}
		return lblMessage;
	}
	protected JButton getBtnConfirm() {
		if (btnConfirm == null) {
			btnConfirm = new JButton("Apply");
			btnConfirm.addActionListener(this);
		}
		return btnConfirm;
	}
	protected JButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new JButton("Cancel");
			btnCancel.addActionListener(this);
		}
		return btnCancel;
	}
	
	/**
	 * @return the canceled
	 */
	public boolean isCanceled() {
		return canceled;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == getBtnConfirm()){
			setVisible(false);
		}else if(e.getSource() == getBtnCancel()){
			canceled = true;
			setVisible(false);
		}
	}
	
	public abstract Number getValue();

}
