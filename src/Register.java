import java.awt.GridLayout; 
 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Register extends JPanel implements ActionListener {
//	Create Labels and Buttons
	JLabel userL = new JLabel("Username: ");
	JTextField userTF = new JTextField();
	JLabel passL = new JLabel("Password: ");
	JPasswordField passTF = new JPasswordField();
	JLabel passCL = new JLabel("Confirm Your Password: ");
	JPasswordField passC = new JPasswordField();
	
	JButton register = new JButton("Register");
	JButton back = new JButton("Back");
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
//		check if user clicked register and the password field's length and username field's length are bigger than 0
		if(e.getSource() == register && passTF.getPassword().length > 0 && userTF.getText().length() > 0) {
//			put both password and password confirmation into variables
			String pass = new String(passTF.getPassword());
			String cPass = new String(passC.getPassword());
//			check if password field matchs password confirmation
			if(pass.equals(cPass)) {				
				try {
//					read the password text file and put it into 'input' variable
					BufferedReader input = new BufferedReader(new FileReader("password.txt"));
//					Separate every line at password text file (input variable) and put it into 'line' variable
					String line = input.readLine();
//					make sure that the line is not empty
					while(line != null) {
//						use string tokenizer to break every line to peaces to move between every peace of the line
//						which in this case move between username and password in every line
						StringTokenizer st = new StringTokenizer(line);
//						check if username equals any of the usernames at the password text file
						if(userTF.getText().equals(st.nextToken())) {
							System.out.println("User Already Exists");
							return;
						}
						line = input.readLine(); 
					}
					input.close();
//					encrypt user password by SHA-256 Algorithm
					MessageDigest md = MessageDigest.getInstance("SHA-256");
					md.update(pass.getBytes());
					byte byteData[] = md.digest(); 
					StringBuffer sb = new StringBuffer();
					for(int i = 0; i < byteData.length; i++)
						sb.append(Integer.toString((byteData[i] & 0xFF) + 0x100, 16).substring(1));
//					write the username and encrypted password into the password text file
					BufferedWriter output = new BufferedWriter(new FileWriter("password.txt", true));
					output.write(userTF.getText()+" "+sb.toString()+"\n");
					output.close();
//					return the user to the login screen
					Login login = (Login) getParent();
					login.cl.show(login, "login"); 
				} catch(FileNotFoundException e1) {
					e1.printStackTrace();
				} catch(IOException e1) {
					e1.printStackTrace();
				} catch (NoSuchAlgorithmException e1) {
					e1.printStackTrace();
				}
			}
		}
		if(e.getSource() == back){
			Login login = (Login) getParent();
			login.cl.show(login, "login");
		}
	}
//  Create Constructor
	public Register() {		
		JPanel registerP = new JPanel();
		registerP.setLayout(new GridLayout(4, 2));
		registerP.add(userL);
		registerP.add(userTF);
		registerP.add(passL);
		registerP.add(passTF);
		registerP.add(passCL);
		registerP.add(passC);
		registerP.add(register);
		registerP.add(back);
		register.addActionListener(this);
		back.addActionListener(this);
		add(registerP);
	}
	
	
}
