package ac.rs.singidunum.kriptologija2;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.HostKey;
import com.jcraft.jsch.HostKeyRepository;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.UserInfo;
import java.awt.HeadlessException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class KeyExchange {

    public KeyExchange() {

    }

    public void exchange() {
        try {
            JSch jsch = new JSch();
            String publicKeyPath = "";

            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Choose your public key (ex. ~/.ssh/id_dsa.pub)");
            chooser.setFileHidingEnabled(false);
            int returnVal = chooser.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                System.out.println("You chose "
                        + chooser.getSelectedFile().getAbsolutePath() + ".");
                publicKeyPath = chooser.getSelectedFile().getAbsolutePath();

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

            Session session = jsch.getSession(user, host, 22);

            UserInfo ui = new MyUserInfo();
            session.setUserInfo(ui);

            session.connect();

            {
                HostKey hk = session.getHostKey();
                System.out.println("HostKey: "
                        + hk.getHost() + " "
                        + hk.getType() + " "
                        + hk.getFingerPrint(jsch));
            }

            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp c = (ChannelSftp) channel;

            boolean exists = c.ls("/home/" + user).toString().contains(".ssh");
            if (!exists) {
                c.mkdir("/home/" + user + "/.ssh");
                c.put(publicKeyPath, "/home/" + user + "/.ssh/authorized_keys");
                c.chmod(0700, "/home/" + user + "/.ssh");
                c.chmod(0600, "/home/" + user + "/.ssh/authorized_keys");
            } else {
                c.put(publicKeyPath, "/home/" + user + "/.ssh/authorized_keys");
                //c.chmod(0700, "/home/"+user+"/.ssh");
                c.chmod(0600, "/home/" + user + "/.ssh/authorized_keys");
            }

            System.out.println("DONE!");

            c.disconnect();
            session.disconnect();

        } catch (JSchException | SftpException | HeadlessException e) {
            System.out.println(e);
        }
    }

}
