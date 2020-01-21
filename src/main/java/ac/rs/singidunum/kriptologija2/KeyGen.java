package ac.rs.singidunum.kriptologija2;

import com.jcraft.jsch.*;
import java.io.IOException;
import javax.swing.*;

class KeyGen {

    public KeyGen() {

    }

    public void generate() {
        int algorithm = 0;
        int keyLength = 0;
        Object[] optAlgo = {"DSA", "RSA", "ECDSA"};
        Object[] optDefManu = {"MANUALLY", "DEFAULT"};
        Object[] optDSA = {1024, 2048};
        Object[] optRSA = {1024, 2048, 4096};
        Object[] optECDSA = {256, 384, 521};
        int x = JOptionPane.showOptionDialog(null, "Choose how to set key algorithm", "Type", 0, 0, null, optDefManu, optDefManu[1]);
        switch (x) {
            case 0:
                int y = JOptionPane.showOptionDialog(null, "Set key algorithm", "Algorithm", 0, 0, null, optAlgo, optAlgo[0]);
                switch (y) {
                    case 0:
                        algorithm = KeyPair.DSA;
                        int dsa = JOptionPane.showOptionDialog(null, "Set key size", "Size", 0, 0, null, optDSA, optDSA[1]);
                        switch (dsa) {
                            case 0:
                                keyLength = 1024;
                                break;
                            case 1:
                                keyLength = 2048;
                                break;
                        }
                        System.out.println("DSA " + keyLength);
                        break;
                    case 1:
                        algorithm = KeyPair.RSA;
                        int rsa = JOptionPane.showOptionDialog(null, "Set key size", "Size", 0, 0, null, optRSA, optRSA[1]);
                        switch (rsa) {
                            case 0:
                                keyLength = 1024;
                                break;
                            case 1:
                                keyLength = 2048;
                                break;
                            case 2:
                                keyLength = 4096;
                                break;
                        }
                        System.out.println("RSA " + keyLength);
                        break;
                    case 2:
                        algorithm = KeyPair.ECDSA;
                        int ecdsa = JOptionPane.showOptionDialog(null, "Sey key size", "Size", 0, 0, null, optECDSA, optECDSA[2]);
                        switch (ecdsa) {
                            case 0:
                                keyLength = 256;
                                break;
                            case 1:
                                keyLength = 384;
                                break;
                            case 2:
                                keyLength = 521;
                                break;
                        }
                        System.out.println("ECDSA " + keyLength);
                        break;
                }
                break;
            case 1:
                algorithm = KeyPair.RSA;
                keyLength = 2048;
                System.out.println("RSA 2048 DEFAULT");
                break;
        }

        String filepath = null;
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select path where to create keys");
        chooser.setFileHidingEnabled(false);

        int returnVal = chooser.showOpenDialog(null);
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
            KeyPair kpair = KeyPair.genKeyPair(jsch, algorithm, keyLength);
            kpair.setPassphrase(passphrase);
            kpair.writePrivateKey(filepath);
            kpair.writePublicKey(filepath + ".pub", comment);
            System.out.println("Finger print: " + kpair.getFingerPrint());
            kpair.dispose();
        } catch (JSchException | IOException e) {
            System.out.println(e);
        }

    }
}
