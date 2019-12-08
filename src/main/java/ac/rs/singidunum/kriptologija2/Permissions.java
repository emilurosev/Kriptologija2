package ac.rs.singidunum.kriptologija2;

import java.io.File;
import java.io.IOException;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Set;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

public class Permissions {

    public static void setPermissions() {

        try {
            Set<PosixFilePermission> permsForFolder = new HashSet<PosixFilePermission>();
            permsForFolder.add(PosixFilePermission.OWNER_READ);
            permsForFolder.add(PosixFilePermission.OWNER_WRITE);
            permsForFolder.add(PosixFilePermission.OWNER_EXECUTE);
            Set<PosixFilePermission> permsForFiles = new HashSet<PosixFilePermission>();
            permsForFiles.add(PosixFilePermission.OWNER_READ);
            permsForFiles.add(PosixFilePermission.OWNER_WRITE);
            String path = "";
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Choose .ssh folder");
            chooser.setFileHidingEnabled(false);
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnVal = chooser.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                System.out.println("You chose "
                        + chooser.getSelectedFile().getAbsolutePath() + ".");
                path = chooser.getSelectedFile().getAbsolutePath();

            }

            File folder = new File(path);
            Files.setPosixFilePermissions(folder.toPath(), permsForFolder);
            File[] files = folder.listFiles();
            for (File file : files) {
                Files.setPosixFilePermissions(file.toPath(), permsForFiles);
            }
            System.out.println("DONE");
        } catch (IOException ex) {
            Logger.getLogger(Permissions.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
