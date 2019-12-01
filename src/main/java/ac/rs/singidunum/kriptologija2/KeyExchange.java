package ac.rs.singidunum.kriptologija2;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.HostKey;
import com.jcraft.jsch.HostKeyRepository;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class KeyExchange {

    public static void main(String[] arg) {
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
            if (arg.length > 0) {
                host = arg[0];
            } else {
                host = JOptionPane.showInputDialog("Enter username@hostname",
                        System.getProperty("user.name")
                        + "@localhost");
            }
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

    public static class MyUserInfo implements UserInfo, UIKeyboardInteractive {

        public String getPassword() {
            return passwd;
        }

        public boolean promptYesNo(String str) {
            Object[] options = {"yes", "no"};
            int foo = JOptionPane.showOptionDialog(null,
                    str,
                    "Warning",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
            return foo == 0;
        }

        String passwd;
        JTextField passwordField = (JTextField) new JPasswordField(20);

        public String getPassphrase() {
            return null;
        }

        public boolean promptPassphrase(String message) {
            return true;
        }

        public boolean promptPassword(String message) {
            Object[] ob = {passwordField};
            int result
                    = JOptionPane.showConfirmDialog(null, ob, message,
                            JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                passwd = passwordField.getText();
                return true;
            } else {
                return false;
            }
        }

        public void showMessage(String message) {
            JOptionPane.showMessageDialog(null, message);
        }
        final GridBagConstraints gbc
                = new GridBagConstraints(0, 0, 1, 1, 1, 1,
                        GridBagConstraints.NORTHWEST,
                        GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0), 0, 0);
        private Container panel;

        public String[] promptKeyboardInteractive(String destination,
                String name,
                String instruction,
                String[] prompt,
                boolean[] echo) {
            panel = new JPanel();
            panel.setLayout(new GridBagLayout());

            gbc.weightx = 1.0;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.gridx = 0;
            panel.add(new JLabel(instruction), gbc);
            gbc.gridy++;

            gbc.gridwidth = GridBagConstraints.RELATIVE;

            JTextField[] texts = new JTextField[prompt.length];
            for (int i = 0; i < prompt.length; i++) {
                gbc.fill = GridBagConstraints.NONE;
                gbc.gridx = 0;
                gbc.weightx = 1;
                panel.add(new JLabel(prompt[i]), gbc);

                gbc.gridx = 1;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weighty = 1;
                if (echo[i]) {
                    texts[i] = new JTextField(20);
                } else {
                    texts[i] = new JPasswordField(20);
                }
                panel.add(texts[i], gbc);
                gbc.gridy++;
            }

            if (JOptionPane.showConfirmDialog(null, panel,
                    destination + ": " + name,
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE)
                    == JOptionPane.OK_OPTION) {
                String[] response = new String[prompt.length];
                for (int i = 0; i < prompt.length; i++) {
                    response[i] = texts[i].getText();
                }
                return response;
            } else {
                return null;  // cancel
            }
        }
    }

}
