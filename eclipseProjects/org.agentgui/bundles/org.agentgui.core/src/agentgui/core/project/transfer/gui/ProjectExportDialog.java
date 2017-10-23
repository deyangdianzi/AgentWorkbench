package agentgui.core.project.transfer.gui;

import javax.swing.JPanel;

import agentgui.core.application.Language;
import agentgui.core.project.Project;
import agentgui.core.project.transfer.ProjectExportSettings;
import agentgui.core.project.transfer.ProjectExportSettings.ProductVersion;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JCheckBox;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;

public class ProjectExportDialog extends JDialog implements ActionListener{
	
	private static final long serialVersionUID = 7642101726572826993L;
	private JLabel jLabelHeader;
	private JCheckBox jCheckBoxIncludeProduct;
	private JComboBox<ProjectExportSettings.ProductVersion> jComboBoxSelectOS;
	private JCheckBox jCheckBoxIncludeAllSetups;
	private JPanel jPanelConfirmCancel;
	private JButton jButtonOk;
	private JButton jButtonCancel;
	
	private JList<String> jListSetupSelection;
	private DefaultListModel<String> listModel;
	
	private Project project;
	private ProjectExportSettings exportSettings;
	
	private boolean canceled = false;
	private JScrollPane scrollPane;
	
	
	public ProjectExportDialog(Project project) {
		this.project = project;
		this.initialize();
	}
	
	private void initialize() {
		
		this.setTitle(Language.translate("Projekt exportieren"));
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		GridBagConstraints gbc_jLabelHeader = new GridBagConstraints();
		gbc_jLabelHeader.anchor = GridBagConstraints.WEST;
		gbc_jLabelHeader.gridwidth = 2;
		gbc_jLabelHeader.insets = new Insets(5, 10, 5, 10);
		gbc_jLabelHeader.gridx = 0;
		gbc_jLabelHeader.gridy = 0;
		getContentPane().add(getJLabelHeader(), gbc_jLabelHeader);
		
		GridBagConstraints gbc_jCheckBoxIncludeProduct = new GridBagConstraints();
		gbc_jCheckBoxIncludeProduct.insets = new Insets(5, 10, 5, 10);
		gbc_jCheckBoxIncludeProduct.anchor = GridBagConstraints.WEST;
		gbc_jCheckBoxIncludeProduct.gridx = 0;
		gbc_jCheckBoxIncludeProduct.gridy = 1;
		getContentPane().add(getJCheckBoxIncludeProduct(), gbc_jCheckBoxIncludeProduct);
		
		GridBagConstraints gbc_jComboBoxSelectOS = new GridBagConstraints();
		gbc_jComboBoxSelectOS.anchor = GridBagConstraints.WEST;
		gbc_jComboBoxSelectOS.insets = new Insets(5, 10, 5, 10);
		gbc_jComboBoxSelectOS.gridx = 1;
		gbc_jComboBoxSelectOS.gridy = 1;
		getContentPane().add(getJComboBoxSelectOS(), gbc_jComboBoxSelectOS);
		
		GridBagConstraints gbc_jCheckBoxIncludeAllSetups = new GridBagConstraints();
		gbc_jCheckBoxIncludeAllSetups.insets = new Insets(5, 10, 5, 10);
		gbc_jCheckBoxIncludeAllSetups.anchor = GridBagConstraints.NORTHWEST;
		gbc_jCheckBoxIncludeAllSetups.gridx = 0;
		gbc_jCheckBoxIncludeAllSetups.gridy = 2;
		getContentPane().add(getJCheckBoxIncludeAllSetups(), gbc_jCheckBoxIncludeAllSetups);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.insets = new Insets(5, 10, 5, 10);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 3;
		getContentPane().add(getScrollPane(), gbc_scrollPane);
		
		GridBagConstraints gbc_jPanelConfirmCancel = new GridBagConstraints();
		gbc_jPanelConfirmCancel.gridwidth = 2;
		gbc_jPanelConfirmCancel.fill = GridBagConstraints.BOTH;
		gbc_jPanelConfirmCancel.gridx = 0;
		gbc_jPanelConfirmCancel.gridy = 4;
		getContentPane().add(getJPanelConfirmCancel(), gbc_jPanelConfirmCancel);
		
		this.pack();
		this.setModal(true);
	}
	
	private JLabel getJLabelHeader() {
		if (jLabelHeader == null) {
			jLabelHeader = new JLabel("Exporting project <name>");
		}
		return jLabelHeader;
	}
	
	private JCheckBox getJCheckBoxIncludeProduct() {
		if (jCheckBoxIncludeProduct == null) {
			jCheckBoxIncludeProduct = new JCheckBox("Include product");
			jCheckBoxIncludeProduct.addActionListener(this);
		}
		return jCheckBoxIncludeProduct;
	}
	
	private JComboBox<ProductVersion> getJComboBoxSelectOS() {
		if (jComboBoxSelectOS == null) {
			jComboBoxSelectOS = new JComboBox<ProductVersion>();
			jComboBoxSelectOS.setModel(new DefaultComboBoxModel<>(ProjectExportSettings.ProductVersion.values()));
			jComboBoxSelectOS.setEnabled(false);
		}
		return jComboBoxSelectOS;
	}
	
	private JCheckBox getJCheckBoxIncludeAllSetups() {
		if (jCheckBoxIncludeAllSetups == null) {
			jCheckBoxIncludeAllSetups = new JCheckBox("Include all setups");
			jCheckBoxIncludeAllSetups.setSelected(true);
			jCheckBoxIncludeAllSetups.addActionListener(this);
		}
		return jCheckBoxIncludeAllSetups;
	}
	
	private JPanel getJPanelConfirmCancel() {
		if (jPanelConfirmCancel == null) {
			jPanelConfirmCancel = new JPanel();
			GridBagLayout gbl_jPanelConfirmCancel = new GridBagLayout();
			gbl_jPanelConfirmCancel.columnWidths = new int[]{0, 0, 0};
			gbl_jPanelConfirmCancel.rowHeights = new int[]{0, 0};
			gbl_jPanelConfirmCancel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
			gbl_jPanelConfirmCancel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			jPanelConfirmCancel.setLayout(gbl_jPanelConfirmCancel);
			GridBagConstraints gbc_jButtonOk = new GridBagConstraints();
			gbc_jButtonOk.insets = new Insets(5, 0, 10, 15);
			gbc_jButtonOk.ipadx = 45;
			gbc_jButtonOk.weightx = 0.5;
			gbc_jButtonOk.anchor = GridBagConstraints.EAST;
			gbc_jButtonOk.gridx = 0;
			gbc_jButtonOk.gridy = 0;
			jPanelConfirmCancel.add(getJButtonOk(), gbc_jButtonOk);
			GridBagConstraints gbc_jButtonCancel = new GridBagConstraints();
			gbc_jButtonCancel.insets = new Insets(5, 15, 10, 0);
			gbc_jButtonCancel.fill = GridBagConstraints.VERTICAL;
			gbc_jButtonCancel.ipadx = 45;
			gbc_jButtonCancel.weightx = 0.5;
			gbc_jButtonCancel.anchor = GridBagConstraints.WEST;
			gbc_jButtonCancel.gridx = 1;
			gbc_jButtonCancel.gridy = 0;
			jPanelConfirmCancel.add(getJButtonCancel(), gbc_jButtonCancel);
		}
		return jPanelConfirmCancel;
	}
	
	private JButton getJButtonOk() {
		if (jButtonOk == null) {
			jButtonOk = new JButton("OK");
			jButtonOk.addActionListener(this);
		}
		return jButtonOk;
	}
	
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton("Cancel");
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}

	private JList<String> getJListSetupSelection() {
		if (jListSetupSelection == null) {
			jListSetupSelection = new JList<String>();
			jListSetupSelection.setEnabled(false);
			jListSetupSelection.setModel(this.getListModel());
			jListSetupSelection.setCellRenderer(new CheckBoxListCellRenderer());
			this.selectAllListElements(jListSetupSelection);
		}
		return jListSetupSelection;
	}
	
	private void selectAllListElements(JList list) {
		int[] selectedIndices = new int[list.getModel().getSize()];
		for(int i=0; i<selectedIndices.length; i++) {
			selectedIndices[i] = i;
		}
		list.setSelectedIndices(selectedIndices);
	}

	private DefaultListModel<String> getListModel() {
		if(listModel == null) {
			listModel = new DefaultListModel<>();
			for(String setup : project.getSimulationSetups().keySet()) {
				listModel.addElement(setup);
			}
		}
		return listModel;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == this.getJCheckBoxIncludeProduct()) {
			boolean selectionState = this.getJCheckBoxIncludeProduct().isSelected();
			this.getExportSettings().setIncludeProduct(selectionState);
			this.getJComboBoxSelectOS().setEnabled(selectionState);
		} else if (ae.getSource() == this.getJCheckBoxIncludeAllSetups()) {
			boolean selectionState = this.getJCheckBoxIncludeAllSetups().isSelected();
			this.getExportSettings().setIncludeAllSetups(selectionState);
			this.getJListSetupSelection().setEnabled(!selectionState);
			if(selectionState == true) {
				this.selectAllListElements(getJListSetupSelection());
			}else {
				this.getJListSetupSelection().clearSelection();
			}
		}
			
		if (ae.getSource() == this.getJButtonOk()) {
			this.canceled = false;
			this.dispose();
		}else
			
		if (ae.getSource() == this.getJButtonCancel()) {
			this.canceled = true;
			this.dispose();
		}
	}
	
	public ProjectExportSettings getExportSettings() {
		if(exportSettings == null) {
			exportSettings = new ProjectExportSettings();
		}
		return exportSettings;
	}

	/**
	 * @return the canceled
	 */
	public boolean isCanceled() {
		return canceled;
	}
	
	public void showProjectExportDialog() {
		this.setVisible(true);
	}
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getJListSetupSelection());
		}
		return scrollPane;
	}
}
