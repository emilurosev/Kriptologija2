package ac.rs.singidunum.kriptologija2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

public class AddKeyToAuthorizedKeys {

    public static void add() {
        String publicKeyPath = "";
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Choose your public key");
        chooser.setFileHidingEnabled(false);
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose "
                    + chooser.getSelectedFile().getAbsolutePath() + ".");
            publicKeyPath = chooser.getSelectedFile().getAbsolutePath();

        }
        String authorizedKeysPath = "";
        JFileChooser chooser2 = new JFileChooser();
        chooser2.setDialogTitle("Choose your authorized_keys(ex. ~/.ssh/authorized_keys)");
        chooser2.setFileHidingEnabled(false);
        int returnVal2 = chooser2.showOpenDialog(null);
        if (returnVal2 == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose "
                    + chooser2.getSelectedFile().getAbsolutePath() + ".");
            authorizedKeysPath = chooser2.getSelectedFile().getAbsolutePath();
        }
        File publicKey = new File(publicKeyPath);
        File authorizedKeys = new File(authorizedKeysPath);
        try {
            FileInputStream fis = new FileInputStream(publicKey);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            FileWriter fw = new FileWriter(authorizedKeys, true);
            BufferedWriter out = new BufferedWriter(fw);

            String line = null;
            while ((line = in.readLine()) != null) {
                out.write(line);
                out.newLine();
            }
            in.close();
            out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AddKeyToAuthorizedKeys.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AddKeyToAuthorizedKeys.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
