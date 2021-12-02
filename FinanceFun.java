import java.util.Scanner;
import java.util.Properties;
import javax.tools.ToolProvider;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import static java.awt.event.InputEvent.CTRL_DOWN_MASK;
import javax.swing.*;
import javax.swing.text.*;


/* ************************** */
/*			MAIN CLASS		  */	
/* ************************** */

public class FinanceFun
{
	/* ************************************** */
	/*			MAIN CLASS VARIABLES		  */	
	/* ************************************** */	
	// MAIN CLASS VARIABLES
	private static long[] cislo = new long[3];
	private static String UserName, UserId, defGameTitle;
	private static long defaultBalance;
	private static String defaultUser, defaultId;
	
	static ConfigurationSettings config;

	/* ************************** */
	/*		  MAIN METHODS		  */	
	/* ************************** */
	public static void main(String args[])
	{
		defGameTitle = "FinanceFun";
		
		config = new ConfigurationSettings();
		config.loadProperties();
		
		
		setDefaultAccount(defaultUser, defaultId, defaultBalance, 0, 0);

		
		Gui obj1 = new Gui(UserName, UserId, defGameTitle);
		obj1.guiWindow();
	}

	/* ************************** */
	/*			METHODS			  */	
	/* ************************** */		
	static long getBalance()
	{
		return cislo[0];
	}
	static void setBalance(long balance)
	{
		cislo[0] = balance;
	}

	
	static long getLastTrans()
	{
		return cislo[1];
	}
	static void setLastTrans(long lastTrans)
	{
		cislo[1] = lastTrans;
	}

	
	static long getLostBalance()
	{
		return cislo[2];
	}
	static void setLostBalance(long lostBalance)
	{
		cislo[2] = lostBalance;
	}


	static String getUserName()
	{
		return UserName;
	}
	static void setUserName(String name)
	{
		UserName = name;
	}

	
	static String getUserId()
	{
		return UserId;
	}
	static void setUserId(String id)
	{
		UserId = id;
	}

	static long getDefaultBalance()
	{
		return defaultBalance;
	}
	static void setDefaultBalance(String defBalance)
	{
		try 
		{
			//Long.parseLong(defBalance);
			defaultBalance = Math.abs(Long.parseLong(defBalance));
		}
		catch (NumberFormatException e) 
		{
			defaultBalance = 0;
		}
	}

	static String getDefaultUser()
	{
		return defaultUser;
	}
	static void setDefaultUser(String defUser)
	{
		defaultUser = defUser;
	}
	
	static String getDefaultId()
	{
		return defaultId;
	}
	static void setDefaultId(String defId)
	{
		defaultId = defId;
	}
	
	static void setDefaultAccount(String name, String id, long balance, long lastTrans, long lostTrans)
	{
		cislo[0] = balance;
		cislo[1] = lastTrans;
		cislo[2] = lostTrans;
		
		UserName = name;
		UserId = id;
		
	}
}


/* ********************************************** */
/*			CREATES MAIN WINDOW - CLASS			  */	
/* ********************************************** */
class Gui
{
	/* ************************** */
	/*			VARIABLES		  */	
	/* ************************** */
	//"GUI" CLASS VARIABLES
	private String customerName, customerId, title;
	private final String defCustomerName, defCustomerId;
	private long transaction;   // VARIABLE FOR EVENT CLASSES
	
	// guiWindow() VARIABLES
	private int frameMinWidth = 800;
	private int frameMinHeight = 600;
	private int panelWestWidth = 300;
	private int panelTextWidth = frameMinWidth - 300;
	private int fromBorder = 10;
	private int labelHeight = 50;
	private int textAreaColumns = 41;
	private int textFieldColumnsLimit = 15;
	private int transactionFieldColumnsLimit = 18;
	private int myFontSize = 18;
	

	/* ****************************** */
	/*			COLOR VARIABLES		  */	
	/* ****************************** */
	// COLORS
	Color cblack = new Color(0,0,0,255);
	Color cgreen = new Color(0,255,0,255);
	Color cred = new Color(255,0,0,255);
	Color cblue = new Color(0,0,255,255);
	Color cwhite = new Color(255,255,255,255);
	
	Color cTransparent = new Color(1,1,1,0);
	
	Color cblackTrans = new Color(0,0,0,100);
	Color cgreenTrans = new Color(0,255,0,100);
	Color credTrans = new Color(255,0,0,100);
	Color cblueTrans = new Color(0,0,255,100);
	Color cwhiteTrans = new Color(255,255,255,100);

	/* ********************************** */
	/*			OBJECTS VARIABLES		  */	
	/* ********************************** */
	
	// TEXT OBJECTS VARIABLES
	JFrame frame;
	JPanel leftPanel, centerPanel, insidePanel, testPanel;
	JTextArea textArea;
	JTextField transactionField, changeCustomerField, changeIdField;		// LEFT PANEL
	JTextField textField2, textField3, textField4;		// CENTER PANEL (INSIDE PANEL)
	JLabel label1, label2;						// LEFT PANEL
	JLabel label3, label4, label5;				// CENTER PANE (INSIDE PANEL)
	JButton butBalance, butDeposit, butWithdraw; 		// LEFT PANEL
	JButton	doneConfig;									// CENTER PANEL (INSIDE PANEL)
	JMenuBar menuBar;
	JMenu fileMenu, aboutMenu, chUserInfo;
	JMenuItem blank, load, save, config, chUserName, chUserId, exitProgram;	// Menu FILE items
	JMenuItem aboutInfo;													// Menu ABOUT items
	JScrollPane scrollPane;
	

	// FONT
	Font myFont = new Font("Monospaced", Font.PLAIN, myFontSize);


	// WARNING MESSAGES VARIABLES
	private String warningMessage1 = "Missing Amount"; // MAX textFieldColumnsLimit characters
	
	/* ************************** */
	/*			CONSTRUCTOR		  */	
	/* ************************** */
	// CONSTRUKTOR FOR GUI CLASS
	Gui(String cname, String cid, String ctitle)
	{
		customerName = cname;
		customerId = cid;
		title = ctitle;
		
		defCustomerName = cname;
		defCustomerId = cid;
	}

	/* ****************** */
	/*   GETTER METHODS   */	
	/* ****************** */
	
	int getTextFieldColumnsLimit()
	{
		return textFieldColumnsLimit;
	}
	
	int getTransactionFieldColumnsLimit()
	{
		return transactionFieldColumnsLimit;
	}

	String getWarningMessage1()
	{
		return warningMessage1;
	}
	
	// QUICK LOOP FOR FINDING HOW MANY SPACES ARE NECESSARY
	String getSpaces(int spacesNeeded, String tmp)
	{
		String tmpOut = "";
		
		for (int i=1; i < spacesNeeded;i++)
		{
			tmpOut = tmpOut + tmp;
		}
		return tmpOut;
	}

	// CHECK IF BALANCE IS OVER LIMIT
	long getLostInTransaction(long balance, long transaction, long limit)
	{
		long lostFromTransaction = 0;
		
		if ( balance + transaction > limit )
		{
			transaction = -1*((balance + transaction) - limit);
		}

		if ( balance + transaction < limit )
		{
			transaction = -1*((balance + transaction) - limit);
		}		
		
		return lostFromTransaction;
	}
	
	// BIGGEST ALLOWED NUMBER IN BALANCE
	long getBiggestAllowedNumber()
	{
		long biggestAllowedNumber = (long) Math.pow(10, getTransactionFieldColumnsLimit()) -1; // Math.pow does => a^b => 10^15 (returns double)
		return biggestAllowedNumber;
	}	

	/* *********************** */
	/*         METHODS         */	
	/* *********************** */	
	// PRINT TO TEXTAREA
	void printTransactionText(long transaction, String operation)
	{
		String newLine = "\n";
		String textTransaction = "" + transaction;
		String tmp = "";

		if (textTransaction.length() + operation.length() + 1 > textArea.getColumns()) //  that +1 is for "E" at the end of line
		{
			textArea.append("#  ERROR - Too many characters   #\n");
		}
		else 
		{
			tmp = getSpaces( (textArea.getColumns() - (textTransaction.length() + operation.length())), " " );
			textArea.append(operation + tmp + textTransaction + newLine);
		}	
	}
	
	// PRINT TO TEXTAREA
	void printBalanceText(long balance, String operation)
	{
		String newLine = "\n";
		String sAmount = "" + balance;
		String tmp = "";
		
		long minMaxLimit = getBiggestAllowedNumber();
		
		operation = operation + ": ";
		
		if (balance == minMaxLimit || balance == -minMaxLimit)    // checks if Number is bigger than allowed on account
		{
			sAmount = "" + balance;
			newLine = "E\n";
		}		
		else 
		{
			newLine = "\n";
		}
		
		if (sAmount.length() + operation.length() + 1 > textArea.getColumns()) //  that +1 is for "E" at the end of line
		{
			textArea.append("#  ERROR - Too many characters   #\n");
		}
		else 
		{
			tmp = getSpaces( (textArea.getColumns() - (sAmount.length() + operation.length())), " " );
			textArea.append(operation + tmp + sAmount + newLine);
		}	
	}



	
	// CREATES WINDOW
	void guiWindow()
	{
		/* ************************** */
		/*			VARIABLES		  */	
		/* ************************** */


		/* ********************************* */
		/*   MAIN FRAME + BACKGROUND IMAGE   */	
		/* ********************************* */		
		
		// MAIN FRAME
		frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.setMinimumSize(new Dimension(frameMinWidth+6, frameMinHeight+48));
		//frame.setLocationRelativeTo(null);
		frame.setLayout(new BorderLayout());
		frame.setResizable(false);
		//frame.setOpacity(false);
		//frame.setUndecorated(false);
		
		// ICON OF FRAME
		ImageIcon frameIcon = new ImageIcon("Img/Icon.png");
		frame.setIconImage(frameIcon.getImage());
		
		/* ************************** */
		/*		    MENU BAR		  */	
		/* ************************** */  
		
		// JMENUBAR
		menuBar = new JMenuBar();	
		frame.setJMenuBar(menuBar);
		
		// FILE
		fileMenu = new JMenu("FILE");
		fileMenu.setMnemonic('F');
		
		// ABOUT
		aboutMenu = new JMenu("ABOUT");
		aboutMenu.setMnemonic('A');
		
		// CHANGE USER INFO in FILE
		chUserInfo = new JMenu("Change User Info");
		
		
		// FILE DROP MENU
		blank = new JMenuItem("New");
			blank.setAccelerator(KeyStroke.getKeyStroke('N', CTRL_DOWN_MASK));
		load = new JMenuItem("Load");
		save = new JMenuItem("Save");
			save.setAccelerator(KeyStroke.getKeyStroke('S', CTRL_DOWN_MASK));
		chUserName = new JMenuItem("Change User Name");
		chUserId = new JMenuItem("Change User ID");
		config = new JMenuItem("Configuration");
			config.setAccelerator(KeyStroke.getKeyStroke('C', CTRL_DOWN_MASK));
		exitProgram = new JMenuItem("EXIT...  LOL");
			exitProgram.setAccelerator(KeyStroke.getKeyStroke('E', CTRL_DOWN_MASK));
		
		// ABOUT DROP MENU
		aboutInfo = new JMenuItem("<html>This was made for LOLs. To learn JAVA and<br> maybe to use it in tabletop games<br>such as monopoly, but fo only one player<br> (at the moment).<br>I was too lazy to make it for multiple users.<br><br>made by Wollwo<br><br>btw. this serves no purpose</html>");
		//aboutInfo.setEnabled(false);
		
		// ACTION LISTENER FOR ITEMS IN FILE MENU
		blank.addActionListener(new ActionListenerMenu());
		load.addActionListener(new ActionListenerMenu());
		save.addActionListener(new ActionListenerMenu());
		chUserName.addActionListener(new ActionListenerMenu());
		chUserId.addActionListener(new ActionListenerMenu());
		config.addActionListener(new ActionListenerMenu());
		exitProgram.addActionListener(new ActionListenerMenu());
		
		// STRUCTURE OF FILE MENU
		fileMenu.add(blank);
		fileMenu.addSeparator();
		
		fileMenu.add(load);
		fileMenu.add(save);
		fileMenu.addSeparator();
		
		fileMenu.add(chUserInfo);
			chUserInfo.add(chUserName);
			chUserInfo.add(chUserId);
		fileMenu.add(config);
		fileMenu.addSeparator();
		
		fileMenu.add(exitProgram);
		
		// STRUCTURE OF ABOUT MENU
		aboutMenu.add(aboutInfo);
		
		// STRUCTURE OF MENU BAR
 		menuBar.add(fileMenu);
 		menuBar.add(aboutMenu);	
 		
		//   https://www.javatpoint.com/java-jmenuitem-and-jmenu
			
		/* **************************** */
		/*   BACKGROUN IMAGE ON LABEL   */
		/* **************************** */
		// BACKGROUND IMAGE OF FRAME
		ImageIcon image = new ImageIcon("Img/Background.jpg");
		JLabel background = new JLabel("",image,JLabel.CENTER);
		background.setBounds(0, 0, frameMinWidth, frameMinHeight);
		frame.add(background, BorderLayout.CENTER);

		
			/* ************************** */
			/*   MAIN PANEL WITH LAYOUT   */
			/* ************************** */
			
			JPanel mainPanel = new JPanel();
			mainPanel.setBounds(0, 0, frameMinWidth, frameMinHeight);
			mainPanel.setOpaque(false);
			mainPanel.setLayout(new BorderLayout());
			background.add(mainPanel);			
			
		
			/* *********************************************** */
			/*   LEFT PANEL - IN BORDER LAYOUT OF MAIN PANEL   */	
			/* *********************************************** */			
			
			// LEFT JPANEL
			leftPanel = new JPanel();
			leftPanel.setPreferredSize(new Dimension(panelWestWidth,frameMinHeight));
			leftPanel.setBackground(cgreenTrans);  // TEST
			leftPanel.setLayout(null);
			mainPanel.add(leftPanel, BorderLayout.WEST);
			
			
				/* ****************************************************** */
				/*		    VARIABLES FOR CONPONENTS OF LEFT PANEL		  */	
				/* ****************************************************** */		
				
				// VARIABLES FOR CONTAINERS IN leftPanel
				int X=fromBorder;
				int Y=fromBorder;
				int W=panelWestWidth-2*fromBorder;
				int H=labelHeight;
				
				
				
				/* ************************************** */
				/*		    LABELS	- LEFT PANEL		  */	
				/* ************************************** */		
				
				
				// LABEL 1 - NAME
				label1 = makeLabel("Customer: " + customerName, true, cblue, cwhite, X, Y, W, H/2);
				leftPanel.add(label1);
				
					// TEXT FIELD -  CHANGE CUSTOMER NAME
					changeCustomerField = makeTextField("", false, 100, 0, 180, H/2, textFieldColumnsLimit);
					label1.add(changeCustomerField);
					
				// Change "Y" coordinate to better suit next Label
				Y=fromBorder+labelHeight/2;
				
				// LABEL 2 - ID
				label2 = makeLabel("ID      : " + customerId, true, cblue, cwhite, X, Y, W, H/2);
				leftPanel.add(label2);
				
					// TEXT FIELD-  CHANGE CUSTOMER ID
					changeIdField = makeTextField("", false, 100, 0, 180, H/2, textFieldColumnsLimit);
					label2.add(changeIdField);
				
				// Change "Y" coordinate to better suit next Text field
				Y=2*fromBorder+labelHeight;
				

				
				/* ****************************************** */
				/*		    TEXT FIELD	- LEFT PANEL		  */	
				/* ****************************************** */		
				
		
				// TEXT FIELD
				transactionField = makeTextField(warningMessage1, true, X, Y, W, H/2, transactionFieldColumnsLimit);
				leftPanel.add(transactionField);
				
				
				/* ************************************** */
				/*		    BUTTONS	- LEFT PANEL		  */	
				/* ************************************** */
				
				
				// Change "Y" coordinate to better suit next Button
				Y=3*fromBorder+labelHeight+labelHeight/2;
				
				// BUTTON 1
				butWithdraw = makeButton("Withdraw", true, X, Y, W, H );
				leftPanel.add(butWithdraw);
				
				// Change "Y" coordinate to better suit next Button
				Y=4*fromBorder+2*labelHeight+labelHeight/2;
				
				// BUTTON 2
				butDeposit = makeButton("Deposit", true, X, Y, W, H );
				leftPanel.add(butDeposit);
				
				// Change "Y" coordinate to better suit next Button
				Y=5*fromBorder+3*labelHeight+labelHeight/2;
		
				// BUTTON 3
				butBalance = makeButton("Balance", true, X, Y, W, H );
				leftPanel.add(butBalance);
				/*
				// Change "Y" coordinate to better suit next Button
				Y=6*fromBorder+4*labelHeight+labelHeight/2;
				*/
				
			
			/* ************************************************* */
			/*   CENTER PANEL - IN BORDER LAYOUT OF MAIN PANEL   */	
			/* ************************************************* */			
			
			// CENTER JPANEL
			centerPanel = new JPanel();
			centerPanel.setPreferredSize(new Dimension(panelTextWidth,frameMinHeight));
			centerPanel.setBackground(cblackTrans);  	 // TEST
			centerPanel.setLayout(null);
			mainPanel.add(centerPanel, BorderLayout.CENTER);
			
			
				/* ************************************************* */
				/*   INSIDE PANEL - IN NULL LAYOUT OF CENTER PANEL   */	
				/* ************************************************* */			
				
				//INSIDE CENTER PANEL
				insidePanel = new JPanel();
				insidePanel.setBounds(fromBorder, fromBorder, panelTextWidth-2*fromBorder, frameMinHeight-2*fromBorder);
				insidePanel.setOpaque(false);
				insidePanel.setLayout(null);
				insidePanel.setVisible(false);
				
				centerPanel.add(insidePanel);
				
					/* ****************************** */
					/*   COMPONENTS OF INSIDE PANEL   */	
					/* ****************************** */		
					
					
					int xx = 10;
					int yy = 1;
					int ww = 460;
					int hh = 1;
				
					//
					// check container, setLabelFor(), tabs for textArea, 
					//
					
					// LABEL of DEFAULT USER
					label3 = makeLabel("Defaul User: ", true, cblueTrans, cwhite, xx, 10, ww-260, 30);
					textField2 = makeTextField(FinanceFun.getDefaultUser(), true, xx+200, 10, ww-200, 30, textFieldColumnsLimit);
					insidePanel.add(label3);
					insidePanel.add(textField2);
					
					// LABEL of DEFAULT ID
					label4 = makeLabel("Defaul ID: ", true, cblueTrans, cwhite, xx, 50, ww-260, 30);
					textField3 = makeTextField(FinanceFun.getDefaultId(), true, xx+200, 50, ww-200, 30, textFieldColumnsLimit);
					insidePanel.add(label4);
					insidePanel.add(textField3);
					
					// LABEL of DEFAULT BALANCE
					label5 = makeLabel("Defaul Balance: ", true, cblueTrans, cwhite, xx, 90, ww-260, 30);
					textField4 = makeTextField(""+FinanceFun.getDefaultBalance(), true, xx+200, 90, ww-200, 30, transactionFieldColumnsLimit);
					insidePanel.add(label5);
					insidePanel.add(textField4);
				
					// BUTTON to CLOSE DEFAULT CONFIG
					doneConfig = makeButton("DONE", true, (int) insidePanel.getPreferredSize().getWidth() - 110, (int) insidePanel.getPreferredSize().getHeight() - 60, 100, 50 );
					insidePanel.add(doneConfig);
				
				/* ************************************************************ */
				/*   SCROLL PANE + TEXT AREA - IN NULL LAYOUT OF CENTER PANEL   */	
				/* ************************************************************ */
		
				// JTEXTAREA
				textArea = new JTextArea();    // maybe add image to background of JTextArea ?
				textArea.setColumns(textAreaColumns);
				textArea.setEditable(false);
				textArea.setFont(myFont);
				//SCROLL BAR FOR TEXTAREA
				scrollPane = new JScrollPane(textArea);	
				scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				scrollPane.setBounds(fromBorder, fromBorder, panelTextWidth-2*fromBorder, frameMinHeight-2*fromBorder);
				scrollPane.setVisible(true);
				centerPanel.add(scrollPane);
				
		
		/* ****************************** */
		/*		    FINISH FRAME		  */	
		/* ****************************** */		
		

        frame.pack();
        frame.setLocationRelativeTo(null);
		frame.repaint();
		frame.setVisible(true);
		
	}

	private JButton makeButton(String text, boolean visibility, int X, int Y, int W, int H)
	{
		// makeButton();
		
		JButton button = new JButton(text);
		button.setBounds(X,Y,W,H);
		button.addActionListener(new ActionListenerButtons());
		button.setFont(myFont);
		button.setVisible(visibility);
		
		return button;
	}

	private JLabel makeLabel(String text, boolean visibility, Color backgroundColor, Color foregroundColor, int X, int Y, int W, int H)
	{
		// makeLabel();
		
		JLabel label = new JLabel(text);
		label.setBounds(X,Y,W,H);
		label.setFont(myFont);
		label.setForeground(foregroundColor);
		label.setBackground(backgroundColor);
		label.setOpaque(true);       // TEST
		label.setVisible(visibility);
		
		return label;
	}
	
	private JTextField makeTextField(String text, boolean visibility, int X, int Y, int W, int H, int limit)
	{
		// makeTextField();
		
		JTextField textField = new JTextField();
		textField.setBounds(X,Y,W,H);
		textField.setFont(myFont);
		textField.setDocument(new JTextFieldLimit(limit));
		textField.addFocusListener(new FocusListenerField());
		textField.setText(text);
		textField.setVisible(visibility);
		
		return textField;
	}
	
	/* ****************************** */
	/*   INSIDE CLASSES - LISTENERS   */
	/* ****************************** */
	
	// DISPLAY MESSAGE IF FOCUS IS ON TEXTFIELD
	class FocusListenerField implements FocusListener
	{
		public void focusGained(FocusEvent action)
		{
			if (action.getSource() == transactionField)
			{
				String tmpString = transactionField.getText();
				
				if ( tmpString.equals(warningMessage1) )
				{
					transactionField.setText("");
				}
			}
		}
		public void focusLost(FocusEvent action)
		{
			if (action.getSource() == changeCustomerField)
			{
				label1.setText("Customer: " + changeCustomerField.getText());
				changeCustomerField.setVisible(false);
				customerName = changeCustomerField.getText();
				
			}
			
			if (action.getSource() == changeIdField)
			{
				label2.setText("Customer: " + changeIdField.getText());
				changeIdField.setVisible(false);
				customerId = changeIdField.getText();
			}
		}
	}	

	class ActionListenerButtons implements ActionListener
	{
		public void actionPerformed(ActionEvent action)
		{
			// CLICK ON "WITHDRAW" BUTTON
			if (action.getSource() == butWithdraw)
			{
				String tmpString = transactionField.getText();
				
				if ( ! tmpString.equals(warningMessage1) )
				{
					if ( tmpString.equals("") )
					{
						transaction = 0;
					}
					else
					{
						try 
						{
							//Long.parseLong(tmpString);
							transaction = Math.abs(Long.parseLong(tmpString));
						}
						catch (NumberFormatException e) 
						{
							transaction = 0;
						}
					}
	
					if (transaction != 0)
					{			
						transactionField.setText(warningMessage1);
						FinanceFun.setBalance(FinanceFun.getBalance()-transaction);
						FinanceFun.setLastTrans(-transaction);
						transaction = FinanceFun.getLastTrans();
						printTransactionText(transaction, "Withdraw");
					}
					else
					{
						transactionField.setText(warningMessage1);
					}
				}
			}
			
			// CLICK ON "DEPOSIT" BUTTON
			if (action.getSource() == butDeposit)
			{
				String tmpString = transactionField.getText();
				
				if ( ! tmpString.equals(warningMessage1) )
				{
					if ( tmpString.equals("") )
					{
						transaction = 0;
					}
					else
					{
						try 
						{
							//Long.parseLong(tmpString);
							transaction = Math.abs(Long.parseLong(tmpString));
						}
						catch (NumberFormatException e) 
						{
							transaction = 0;
						}
					}
	
					if (transaction != 0)
					{	
						transactionField.setText(warningMessage1);
						FinanceFun.setBalance(FinanceFun.getBalance()+transaction);
						FinanceFun.setLastTrans(transaction);
						transaction = FinanceFun.getLastTrans();
						printTransactionText(transaction, "Deposit");
					}
					else
					{
						transactionField.setText(warningMessage1);
					}
				}
			}			

			// CLICK ON "BALANCE" BUTTON
			if (action.getSource() == butBalance)
			{
				String tmpString, tmp;
				tmpString = transactionField.getText().toUpperCase();
				tmp = "";

				long lostBalance, balance, lastTransaction, maxLimit, minLimit;
				lostBalance = FinanceFun.getLostBalance();
				balance = FinanceFun.getBalance();
				lastTransaction = FinanceFun.getLastTrans();
				maxLimit = getBiggestAllowedNumber();
				minLimit = -getBiggestAllowedNumber();
			
				tmp = getSpaces( textArea.getColumns() + 1, "=");

				textArea.append(tmp+"\n");
				transactionField.setText(warningMessage1);
	
				if ( balance > maxLimit )
				{
					FinanceFun.setLostBalance(lostBalance + (balance - maxLimit));
					lostBalance = FinanceFun.getLostBalance();
					FinanceFun.setBalance(maxLimit);
					balance = FinanceFun.getBalance();
				}
						
				if ( balance < minLimit )
				{
					FinanceFun.setLostBalance(lostBalance + (balance - minLimit));
					lostBalance = FinanceFun.getLostBalance();
					FinanceFun.setBalance(minLimit);
					balance = FinanceFun.getBalance();
				}			
			
			
				if ( tmpString.equals("LOST") )
				{
					printBalanceText(lostBalance, "LOST");
				}
				else
				{
					printBalanceText(balance, "Balance");
				}
			}
			// CLICK ON "DONE" BUTTON to close CONFIG
			if (action.getSource() == doneConfig)
			{
				String tmpString;
				
				scrollPane.setVisible(true);
				insidePanel.setVisible(false);
				frame.repaint();
				
				FinanceFun.setDefaultUser(textField2.getText());
				FinanceFun.setDefaultId(textField3.getText());
				FinanceFun.setDefaultBalance(textField4.getText());
				
				
				FinanceFun.config.saveProperties(FinanceFun.getDefaultUser(), FinanceFun.getDefaultId(), "" + FinanceFun.getDefaultBalance());
			}
		}
	}


	class JTextFieldLimit extends PlainDocument 
	{
		private int limit;
		JTextFieldLimit(int limit) 
		{
			super();
			this.limit = limit;
		}
		
		public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException 
		{
			if (str == null)
			return;

			if ((getLength() + str.length()) <= limit) 
			{
				super.insertString(offset, str, attr);
			}
		}
	}

	
	class ActionListenerMenu implements ActionListener
	{
		public void actionPerformed(ActionEvent action)
		{
			// CLICK ON "CLOSE" IN FILE MENU
			if (action.getSource() == exitProgram)
			{
				System.exit(0);
			}
			
			// CLICK ON "NEW" IN FILE MENU
			if (action.getSource() == blank)
			{
				textArea.setText("");
				FinanceFun.setBalance(FinanceFun.getDefaultBalance());
				label1.setText("Customer: " + FinanceFun.getDefaultUser());
				label2.setText("ID:       " + FinanceFun.getDefaultId());
				customerName = FinanceFun.getDefaultUser();
				customerId = FinanceFun.getDefaultId();
			}
			
			// CLICK ON "LOAD" IN FILE MENU
			if (action.getSource() == load)
			{
				
				
				
				
				
				
				
			}
			
			// CLICK ON "SAVE" IN FILE MENU
			if (action.getSource() == save)
			{
				//String toArea = "Text to, Text from, Text above, Text below";
				//String[] toPrint;
				
				
			}
			
			// CLICK ON "Change User Name" IN FILE MENU
			if (action.getSource() == chUserName)
			{
				changeCustomerField.setVisible(true);
				changeCustomerField.setText(customerName);
			}
			
			// CLICK ON "Change User ID" IN FILE MENU
			if (action.getSource() == chUserId)
			{
				changeIdField.setVisible(true);
				changeCustomerField.setText(customerId);
			}
			
			// CLICK ON "configuration" IN FILE MENU
			if (action.getSource() == config)
			{
				scrollPane.setVisible(false);
				insidePanel.setVisible(true);
				frame.repaint();
			}
			
		}
	}

	
  //https://crunchify.com/java-properties-file-how-to-read-config-properties-values-in-java/
	
}

class ConfigurationSettings
{

	
	void ConfigurationSettings()
	{
		// Constructor
		// https://www.codejava.net/coding/reading-and-writing-configuration-for-java-application-using-properties-class
	}

	public void loadProperties()
	{
		File configFile = new File("Config/FinanceFun.properties");
 
		try {
			FileReader reader = new FileReader(configFile);
			Properties props = new Properties();
			props.load(reader);
		
			String defaultUser = props.getProperty("defaultUser");
			String defaultId = props.getProperty("defaultId");
			String defaultBalance = props.getProperty("defaultBalance");
			
			FinanceFun.setDefaultUser(defaultUser);
			FinanceFun.setDefaultId(defaultId);
			FinanceFun.setDefaultBalance(defaultBalance);

			
			reader.close();
		} catch (FileNotFoundException ex) {
			// file does not exist
		} catch (IOException ex) {
			// I/O error
		}
	}
		
	public void saveProperties(String user, String acount, String defBalance)
	{
		File configFile = new File("Config/FinanceFun.properties");
		
		try {
			Properties props = new Properties();
			props.setProperty("defaultUser", user);
			props.setProperty("defaultId", acount);
			props.setProperty("defaultBalance", defBalance);
			FileWriter writer = new FileWriter(configFile);
			props.store(writer, "FinanceFun settings");
			writer.close();
		} catch (FileNotFoundException ex) {
			// file does not exist
		} catch (IOException ex) {
			// I/O error
		}
	}
	// maybe add to properties LAST OPENED and then read save if exist
}


