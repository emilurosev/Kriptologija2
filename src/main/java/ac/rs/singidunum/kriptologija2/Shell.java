package ac.rs.singidunum.kriptologija2;

import com.jcraft.jsch.*;
import java.awt.HeadlessException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.swing.*;

public class Shell {

    private JSch jsch = null;
    private Session session = null;
    private Channel channel = null;

    private boolean isConnected = false;

    public Shell() {

    }

    public void shell() {

        try {
            jsch = new JSch();

            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Choose your known_hosts(ex. ~/.ssh/known_hosts)");
            chooser.setFileHidingEnabled(false);
            int returnVal = chooser.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                System.out.println("You chose "
                        + chooser.getSelectedFile().getAbsolutePath() + ".");
                jsch.setKnownHosts(chooser.getSelectedFile().getAbsolutePath());
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

            // username and password will be given via UserInfo interface.
            UserInfo ui = new MyUserInfo();
            session.setUserInfo(ui);

            /*
            // In adding to known_hosts file, host names will be hashed. 
            session.setConfig("HashKnownHosts",  "yes");
             */
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

            isConnected = session.isConnected();

            System.out.println();
            System.out.println("Client version is " + session.getClientVersion());
            System.out.println("Server version is " + session.getServerVersion());
            System.out.println();
        } catch (JSchException | HeadlessException e) {
            System.out.println(e);
        }
    }

    public void disconnect() {
        channel.disconnect();
        session.disconnect();
    }

    public void setInputStream(InputStream in) {
        channel.setInputStream(in);
    }

    public InputStream getInputStream() throws IOException {
        return channel.getInputStream();
    }

    public void setOutputStream(OutputStream os) {
        channel.setOutputStream(os);
    }

    public OutputStream getOutputStream() throws IOException {
        return channel.getOutputStream();
    }

    public boolean isConnected() {
        return isConnected;
    }

}
