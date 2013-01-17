package agentgui.core.charts.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import agentgui.core.ontologies.gui.DynForm;
/**
 * Abstract super class for chart implementations of OntologyClassEditorJDialog
 * @author Nils
 *
 */
public abstract class ChartEditorJDialog extends JDialog {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 1820851101239120387L;
	/**
	 * The Dialog is just a container for a ChartEditorJPanel implementation that "does the work"
	 */
	protected ChartEditorJPanel contentPane;
	
	// Swing components
	protected JPanel buttonPane;
	protected JButton btnApply;
	protected JButton btnCancel;
	
	protected DynForm dynForm = null;  //  @jve:decl-index=0:
	protected int startArgIndex = -1;
	
	public ChartEditorJDialog(DynForm dynForm, int startArgIndex) {
		
		this.dynForm = dynForm;
		this.startArgIndex = startArgIndex;
		
		this.setContentPane(this.getContentPane());
		this.getContentPane().add(getButtonPane(), BorderLayout.SOUTH);
		
		setModal(true);
		setSize(600, 450);
		
	}
	
	protected JPanel getButtonPane(){
		if(buttonPane == null){
			buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			buttonPane.add(getBtnApply());
			buttonPane.add(getBtnCancel());
		}
		return buttonPane;
	}
	
	protected JButton getBtnApply(){
		if(btnApply == null){
			btnApply = new JButton("OK");
			btnApply.addActionListener(this.contentPane);
		}
		return btnApply;
	}
	
	protected JButton getBtnCancel(){
		if(btnCancel == null){
			btnCancel = new JButton("Cancel");
			btnCancel.addActionListener(this.contentPane);
		}
		return btnCancel;
	}
	
	public BufferedImage getChartThumb(){
		return this.contentPane.getChartThumb();
	}
	
	/**
	 * Gets the dialogs content pane, which must be a ChartEditorJPanel implementation. 
	 */
	public abstract ChartEditorJPanel getContentPane();

}
