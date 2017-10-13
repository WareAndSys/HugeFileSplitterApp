package com.wareandsy.filesplitter.app;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.wareandsy.filesplitter.AbortSignalSender;
import com.wareandsy.filesplitter.HugeFileSplitter;
import com.wareandsy.filesplitter.HugeFileSplitterEvent;
import com.wareandsy.filesplitter.HugeFileSplitterEvent.EventType;
import com.wareandsy.filesplitter.HugeFileSplitterEventListener;
import com.wareandsy.filesplitter.MessagePrinter;
import com.wareandsy.filesplitter.ReaderConfig;
import org.eclipse.wb.swt.SWTResourceManager;

public class MainView {

	protected Shell shell;
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_3;
	private Text text_4;
	private Table table;
	private TableViewer tableViewer;
	private ProgressBar progressBar;
	
	private ReaderConfig readerConfig;
	private SplitterRunable splitterRunable;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MainView window = new MainView();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		addColumns(table);
		
		progressBar = new ProgressBar(shell, SWT.SMOOTH);
		progressBar.setBounds(10, 401, 403, 28);
		progressBar.setMaximum(100);
		
		addDefaults();
		
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * 
	 */
	private void addDefaults() {
		readerConfig = new ReaderConfig("", "");
		
		text_1.setText(readerConfig.getFileSplitSizeMb() + "");
		text_2.setText(readerConfig.getBufferSizeMb() + "");
	}
	
	/**
	 * 
	 * @param t
	 */
	private void addColumns(Table t){
		TableColumn idColumn = new TableColumn(t, SWT.LEFT);
		TableColumn pathColumn = new TableColumn(t, SWT.LEFT);
		
		idColumn.setText("Num");
		idColumn.setWidth(100);
		
		pathColumn.setText("Path");
		pathColumn.setWidth(table.getBounds().width - 100);
		
		t.setHeaderVisible(true);
		
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell(SWT.SHELL_TRIM & (~SWT.RESIZE) & (~SWT.MAX));
		shell.setImage(SWTResourceManager.getImage(MainView.class, "/com/wareandsy/filesplitter/app/resources/if_puzzle_67324.png"));
		shell.setSize(640, 461);
		shell.setText("HugeFileSplitter");
		
		Label lblFileName = new Label(shell, SWT.NONE);
		lblFileName.setBounds(10, 13, 59, 21);
		lblFileName.setText("File path:");
		
		text = new Text(shell, SWT.BORDER);
		text.setBounds(76, 10, 454, 21);
		
		Button btnBrowse = new Button(shell, SWT.NONE);
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				openFileChooser(e);
			}
		});
		btnBrowse.setBounds(536, 6, 94, 32);
		btnBrowse.setText("Browse...");
		
		Group grpSettings = new Group(shell, SWT.NONE);
		grpSettings.setText("Settings");
		grpSettings.setBounds(10, 44, 620, 152);
		
		Label lblSplitFileBy = new Label(grpSettings, SWT.NONE);
		lblSplitFileBy.setBounds(10, 20, 71, 22);
		lblSplitFileBy.setText("Split file by:");
		
		text_1 = new Text(grpSettings, SWT.BORDER);
		text_1.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent arg0) {
				checkPositiveNumericValue(arg0);
			}
		});
		text_1.setBounds(93, 17, 190, 22);
		
		Label lblKb = new Label(grpSettings, SWT.NONE);
		lblKb.setBounds(289, 20, 26, 14);
		lblKb.setText("Mb");
		
		Label lblBufferSize = new Label(grpSettings, SWT.NONE);
		lblBufferSize.setBounds(10, 48, 71, 22);
		lblBufferSize.setText("Buffer size:");
		
		text_2 = new Text(grpSettings, SWT.BORDER);
		text_2.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent arg0) {
				checkPositiveNumericValue(arg0);
			}
		});
		text_2.setBounds(93, 45, 190, 22);
		
		Label lblMb = new Label(grpSettings, SWT.NONE);
		lblMb.setText("Mb");
		lblMb.setBounds(289, 48, 26, 14);
		
		text_3 = new Text(grpSettings, SWT.BORDER);
		text_3.setBounds(10, 96, 480, 22);
		
		Label lblExtension = new Label(grpSettings, SWT.NONE);
		lblExtension.setBounds(506, 76, 59, 20);
		lblExtension.setText("Extension:");
		
		text_4 = new Text(grpSettings, SWT.BORDER);
		text_4.setBounds(506, 96, 100, 22);
		
		Label lblFileSplitRoot = new Label(grpSettings, SWT.NONE);
		lblFileSplitRoot.setBounds(10, 76, 109, 20);
		lblFileSplitRoot.setText("File split root path:");
		
		Label label_1 = new Label(grpSettings, SWT.NONE);
		label_1.setBounds(496, 99, 8, 22);
		label_1.setText(".");
		
		tableViewer = new TableViewer(shell, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setBounds(10, 202, 620, 166);
		
		Button btnProceed = new Button(shell, SWT.NONE);
		btnProceed.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				proceed(e);
			}
		});
		btnProceed.setBounds(536, 401, 94, 28);
		btnProceed.setText("Proceed");
		
		Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				abort(e);
			}
		});
		btnCancel.setBounds(433, 401, 94, 28);
		btnCancel.setText("Abort");

	}
	
	/**
	 * 
	 * @param e
	 */
	protected void abort(SelectionEvent e) {
		splitterRunable.setAbort(true);      
	}

	/**
	 * @param arg0
	 */
	protected void checkPositiveNumericValue(VerifyEvent ve) {
		Text source = (Text) ve.getSource();
		String value = source.getText();
		if(value == null || "".equals(value)) {
			return;
		}
		
		try {
			String newValue = value.substring(0,  ve.start) + ve.text + value.substring(ve.end);
			double v = Double.parseDouble(newValue);
			ve.doit = v > 0 ? true : false;
		} catch (Exception e) {
			// Not a number
			ve.doit = false;
		}
		return;
	}

	/**
	 * 
	 * @param e
	 */
	protected void proceed(SelectionEvent e) {
		if(text.getText().trim().isEmpty() || text_1.getText().trim().isEmpty() || text_2.getText().trim().isEmpty() 
				|| text_3.getText().trim().isEmpty() || text_4.getText().trim().isEmpty()) {
			return;
		}
		readerConfig = new ReaderConfig(text_3.getText(), !text_4.getText().trim().equals("") ? "." + text_4.getText() : "");
		readerConfig.setFileSplitSize((long)(new Double(text_1.getText().trim()) * 1024 * 1024));
		readerConfig.setBufferSize((long)(new Double(text_2.getText().trim()) * 1024 * 1024));
		
		Display display = Display.getDefault();
		table.removeAll();
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				progressBar.setSelection(0);
			}
		});
		
		splitterRunable = new SplitterRunable(text.getText(), readerConfig, table);
		splitterRunable.setProgressBar(progressBar);
		
		Thread thread = new Thread(splitterRunable);
		thread.start();
	}
	
	
	
	

	/**
	 * 
	 * @param e
	 */
	protected void openFileChooser(SelectionEvent e) {
		FileDialog dialog = new FileDialog(shell, SWT.NULL);
		String path = dialog.open();
		if(path != null){
			text.setText(path);
			FileUtils fileUtils = new FileUtils(path);
			text_3.setText(fileUtils.getFileRootPath() + "_split");
			text_4.setText(fileUtils.getFileExtension());
		}
	}
}

/**
 * 
 * @author fangbe
 *
 */
class HugeFileSplitterEventHandler implements HugeFileSplitterEventListener {
	
	private Table table;
	private ProgressBar progressBar;
	
	

	/**
	 * @param table
	 */
	public HugeFileSplitterEventHandler(Table table, ProgressBar progressBar) {
		super();
		this.table = table;
		this.progressBar = progressBar;
	}



	@Override
	public void onEvent(HugeFileSplitterEvent event) {
		if(event.getEventType() == EventType.FILE_CREATED) {
			String path = (String) event.getParameter("path");
			Display display = Display.getDefault();
			
			display.asyncExec(new Runnable() {
				@Override
				public void run() {
					TableItem item = new TableItem(table, SWT.NONE);
					item.setText(new String[]{(Integer) event.getParameter("index") + "", path, ""});
					int part = (int)((double) event.getParameter("part") * progressBar.getMaximum());
					progressBar.setSelection(progressBar.getSelection() + part + 1);
				}
			});
			
		}
	}
	
}

/**
 * 
 * @author fangbe
 *
 */
class SplitterRunable implements Runnable, AbortSignalSender {
	
	private String path;
	private ReaderConfig readerConfig;
	private Table table;
	
	private ProgressBar progressBar;
	volatile boolean abort;
	
	

	/**
	 * @param path
	 */
	public SplitterRunable(String path, ReaderConfig readerConfig, Table table) {
		this.path = path;
		this.readerConfig = readerConfig;
		this.table = table;
	}
	
	/**
	 * @return the progressBar
	 */
	public ProgressBar getProgressBar() {
		return progressBar;
	}


	/**
	 * @param progressBar the progressBar to set
	 */
	public void setProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
	}
	
	

	/**
	 * @return the abort
	 */
	public boolean isAbort() {
		return abort;
	}

	/**
	 * @param abort the abort to set
	 */
	public void setAbort(boolean abort) {
		this.abort = abort;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		HugeFileSplitter splitter = new HugeFileSplitter(path, readerConfig, new DefaultMessagePrinter());
		splitter.getListeners().add(new HugeFileSplitterEventHandler(table, progressBar));
		splitter.setAbortSignalSender(this);
		splitter.split();
	}
	
}

/**
 * 
 * @author fangbe
 *
 */
class DefaultMessagePrinter implements MessagePrinter {

	@Override
	public void print(String message) {
		System.out.print(message);
	}

	@Override
	public void println(String message) {
		System.out.println(message);
	}

	@Override
	public void error(Exception e) {
		e.printStackTrace();
	}
	
};
