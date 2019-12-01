package ac.rs.singidunum.kriptologija2;

import com.jcraft.jsch.*;
import javax.swing.*;

class KeyGen {

    public static void main(String[] arg) {
        /*
        if (arg.length < 3) {
            System.err.println(
                    "usage: java KeyGen rsa output_keyfile comment\n"
                    + "       java KeyGen dsa  output_keyfile comment");
            System.exit(-1);
        }
    
        String _type = arg[0];
        int type = 0;
        if (_type.equals("rsa")) {
            type = KeyPair.RSA;
        } else if (_type.equals("dsa")) {
            type = KeyPair.DSA;
        } else {
            System.err.println(
                    "usage: java KeyGen rsa output_keyfile comment\n"
                    + "       java KeyGen dsa  output_keyfile comment");
            System.exit(-1);
        }
        String filename = arg[1];
        String comment = arg[2];
         */
        
    
       
        String _type = null;
        int type = 0;
        _type = JOptionPane.showInputDialog("Choose between rsa and dsa", "rsa");
        if (_type.toLowerCase().equals("rsa")) {
            type = KeyPair.RSA;
        } else if (_type.toLowerCase().equals("dsa")) {
            type = KeyPair.DSA;
        } else {
            System.err.println("Enter dsa or rsa");

            System.exit(-1);
        }
        String filepath = null;
        
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("chooser");
        chooser.setFileHidingEnabled(false);

        int returnVal = chooser.showDialog(null, null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose "
                    + chooser.getSelectedFile().getAbsolutePath() + ".");
            filepath = chooser.getSelectedFile().getAbsolutePath();

        }
        
        String comment = null;
        comment = JOptionPane.showInputDialog("Comment");

        JSch jsch = new JSch();

        String passphrase = "";
        JTextField passphraseField = (JTextField) new JPasswordField(20);
        Object[] ob = {passphraseField};
        int result
                = JOptionPane.showConfirmDialog(null, ob, "Enter passphrase (empty for no passphrase)",
                        JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            passphrase = passphraseField.getText();
        }

        try {
            KeyPair kpair = KeyPair.genKeyPair(jsch, type, 4096);
            kpair.setPassphrase(passphrase);
            kpair.writePrivateKey(filepath);
            kpair.writePublicKey(filepath + ".pub", comment);
            System.out.println("Finger print: " + kpair.getFingerPrint());
            kpair.dispose();
        } catch (Exception e) {
            System.out.println(e);
        }
        System.exit(0);
    }
}
