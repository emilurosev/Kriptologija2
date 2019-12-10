package ac.rs.singidunum.kriptologija2;

import com.jcraft.jsch.*;
import java.awt.HeadlessException;
import javax.swing.*;

public class UserAuthPubKey {

    private JSch jsch = null;
    private Session session = null;
    private Channel channel = null;

    public UserAuthPubKey() {

    }

    public void authPubKey() {

        try {
            jsch = new JSch();

            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Choose your privatekey(ex. ~/.ssh/id_dsa)");
            chooser.setFileHidingEnabled(false);
            int returnVal = chooser.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                System.out.println("You chose "
                        + chooser.getSelectedFile().getAbsolutePath() + ".");
                jsch.addIdentity(chooser.getSelectedFile().getAbsolutePath()
                //			 , "passphrase"
                );
            }

            JFileChooser chooser2 = new JFileChooser();
            chooser2.setDialogTitle("Choose your known_hosts(ex. ~/.ssh/known_hosts)");
            chooser2.setFileHidingEnabled(false);
            int returnVal2 = chooser2.showOpenDialog(null);
            if (returnVal2 == JFileChooser.APPROVE_OPTION) {
                System.out.println("You chose "
                        + chooser2.getSelectedFile().getAbsolutePath() + ".");
                jsch.setKnownHosts(chooser2.getSelectedFile().getAbsolutePath());
            }

            HostKeyRepository hkr = jsch.getHostKeyRepository();
            HostKey[] hks = hkr.getHostKey();
            if (hks != null) {
                System.out.println("Host keys in " + hkr.getKnownHostsRepositoryID());
                for (int i = 0; i < hks.length; i++) {
                    HostKey hk = hks[i];
                    System.out.println(hk.getHost() + " "
                            + hk.getType() + " "
                            + hk.getFingerPrint(jsch));
                }
                System.out.println("");
            }

            String host = null;

            host = JOptionPane.showInputDialog("Enter username@hostname",
                    System.getProperty("user.name")
                    + "@localhost");

            String user = host.substring(0, host.indexOf('@'));
            host = host.substring(host.indexOf('@') + 1);

            session = jsch.getSession(user, host, 22);

            // username and passphrase will be given via UserInfo interface.
            UserInfo ui = new MyUserInfo();
            session.setUserInfo(ui);

            session.setConfig("StrictHostKeyChecking", "yes");

            session.connect();

            {
                HostKey hk = session.getHostKey();
                System.out.println("HostKey: "
                        + hk.getHost() + " "
                        + hk.getType() + " "
                        + hk.getFingerPrint(jsch));
            }

            channel = session.openChannel("shell");

            channel.setInputStream(System.in);
            channel.setOutputStream(System.out);

            channel.connect();
        } catch (JSchException | HeadlessException e) {
            System.out.println(e);
        }
    }

    public void disconnect() {
        channel.disconnect();
        session.disconnect();
    }
    
    public boolean isConnected() {
        return channel.isConnected();
    }
}
