package org.hestudio.deviceinformationget.Get;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.TextInputDialogBuilder;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import org.hestudio.deviceinformationget.Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class VNCXstartupGet {
    private static String ArchPath() {
        String ARMhfPath = "/data/data/com.termux/files/home/kali-armhf";
        String ARM64Path = "/data/data/com.termux/files/home/kali-arm64";
        File ARMhf = new File(ARMhfPath);
        File ARM64 = new File(ARM64Path);
        if (ARMhf.exists()) {
            return ARMhfPath;
        } else if (ARM64.exists()) {
            return ARM64Path;
        } else {
            Screen screen = null;
            try {
                Terminal terminal = new DefaultTerminalFactory().createTerminal();
                screen = new TerminalScreen(terminal);
                screen.startScreen();

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            final WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
            MessageDialog.showMessageDialog(textGUI, Main.AppName, "我们并没有在你的设备中找到Kali Linux文件。可能你未安装它，也可能它不是由我们的安装系统安装的，或者你修改了它的名称。检查工具将在按下确定后继续运行。");

            return null;
        }
    }
    private static boolean isRoot() {
        Screen screen;
        try {
            Terminal terminal = new DefaultTerminalFactory().createTerminal();
            screen = new TerminalScreen(terminal);
            screen.startScreen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Panel panel = new Panel();
        BasicWindow window = new BasicWindow();
        MultiWindowTextGUI gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));
        final boolean[] Root = new boolean[1];

        panel.addComponent(new Label("你使用的是不是Root账户。"));
        panel.addComponent(new Button(" 是", () -> {
            Root[0] = true;
            gui.removeWindow(window);
            window.close();
        }));
        panel.addComponent(new Button(" 否", () -> {
            Root[0] = false;
            gui.removeWindow(window);
            window.close();
        }));

        window.setComponent(panel);
        window.setTitle(Main.AppName);

        gui.addWindowAndWait(window);

        return Root[0];
    }
    private static String UserPath(String ArchPath, boolean isRoot) {
        // Setup terminal and screen layers
        Screen screen;
        try {
            Terminal terminal = new DefaultTerminalFactory().createTerminal();
            screen = new TerminalScreen(terminal);
            screen.startScreen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Setup WindowBasedTextGUI for dialogs
        final WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
        if (!isRoot) {
            String Username = new TextInputDialogBuilder()
                    .setTitle(Main.AppName)
                    .setDescription("请输入Kali Linux的用户名。如果是默认值(kali)，请输入 kali ")
                    .build()
                    .showDialog(textGUI);
            if (Username.equals("root")) {
                MessageDialog.showMessageDialog(textGUI, Main.AppName, "你输入的是 root ，我们将按照你具有root权限处置。");
                return UserPath(ArchPath, true);
            } else {
                File path = new File(String.format("%s/home/%s", ArchPath, Username));
                if (path.exists() && path.isDirectory()) {
                    return String.format("%s/home/%s", ArchPath, Username);
                } else {
                    MessageDialog.showMessageDialog(textGUI, Main.AppName, "该用户不存在或者创建后从未登陆过。");
                    return UserPath(ArchPath, false);
                }
            }
        } else {
            return String.format("%s/root", ArchPath);
        }
    }
    private static String[] GetXstartup(String Path) {
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(String.format("cat %s", Path));
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String[] Xstartup;
            String outputLine;
            StringBuilder Info = new StringBuilder();

            while ((outputLine = stdInput.readLine()) != null) {
                Info.append(outputLine).append("\n");
            }
            Xstartup = Info.toString().split("\n");
            return Xstartup;

        } catch (IOException e) {
            Screen screen;
            try {
                Terminal terminal = new DefaultTerminalFactory().createTerminal();
                screen = new TerminalScreen(terminal);
                screen.startScreen();

            } catch (IOException ex) {
                throw new RuntimeException(e);
            }
            final WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
            MessageDialog.showMessageDialog(textGUI, Main.AppName, "Error: 程序在获取xstartup信息时遇到错误，请联系heStudio处理。");
            System.exit(-1);
        }
        return new String[0];
    }

    public static String[] Main() {
        String ArchPath = ArchPath();
        if (ArchPath == null) {
            return new String[0];
        } else {
            boolean root = isRoot();
            String UserPath = UserPath(ArchPath, root);
            return GetXstartup(String.format("%s/.vnc/xstartup", UserPath));
        }
    }
}
