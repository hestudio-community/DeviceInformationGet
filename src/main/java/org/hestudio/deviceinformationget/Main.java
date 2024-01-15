package org.hestudio.deviceinformationget;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import org.hestudio.deviceinformationget.Tool.DirectoryDelete;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Main {
    public static String WorkDirPath = "/data/data/com.termux/files/home/.heStudio/DeviceInformation";
    public static String AppName = "DeviceInformationGet by heStudio";

    private static void MainProcess() {
        // ready
        Screen screen;
        try {
            Terminal terminal = new DefaultTerminalFactory().createTerminal();
            screen = new TerminalScreen(terminal);
            screen.startScreen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Panel panel = new Panel();
        Label label = new Label("初始化...");
        panel.addComponent(label);

        BasicWindow window = new BasicWindow();
        window.setComponent(panel);
        window.setTitle(AppName);
        MultiWindowTextGUI gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));
        gui.addWindow(window);

        File InfoDir = new File(String.format("%s/info", WorkDirPath));
        Runtime runtime = Runtime.getRuntime();
        if (!InfoDir.mkdirs()) {
            final WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
            MessageDialog.showMessageDialog(textGUI, AppName, "工作目录创建失败，程序已退出。");
            System.exit(-1);
        }

        // variables
        panel.removeComponent(label);
        label.setText("我们正在采集你的数据...");
        panel.addComponent(label);
        try {
            gui.updateScreen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String[] TermuxInfo = org.hestudio.deviceinformationget.Get.TermuxInfo.Main();
        try {
            gui.updateScreen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String[] CPUInfo = org.hestudio.deviceinformationget.Get.CPUInfo.Main();
        try {
            gui.updateScreen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String[] BinList = org.hestudio.deviceinformationget.Get.BinList.Main();
        try {
            gui.updateScreen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String[] StartkaliConfig = org.hestudio.deviceinformationget.Get.StartkaliConfig.Main();
        try {
            gui.updateScreen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String[] VNCXstartup = org.hestudio.deviceinformationget.Get.VNCXstartupGet.Main();
        try {
            gui.updateScreen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // write data
        panel.removeComponent(label);
        label.setText("正在处理数据...");
        panel.addComponent(label);
        try {
            gui.updateScreen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            FileWriter TermuxInfoWriter = new FileWriter(String.format("%s/info/TermuxInfo.txt", WorkDirPath));
            for (String line : TermuxInfo) {
                TermuxInfoWriter.write(line + "\n");
            }
            TermuxInfoWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            FileWriter CPUInfoWriter = new FileWriter(String.format("%s/info/CPUInfo.txt", WorkDirPath));
            for (String line : CPUInfo) {
                CPUInfoWriter.write(line + "\n");
            }
            CPUInfoWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            FileWriter BinListWriter = new FileWriter(String.format("%s/info/BinList.txt", WorkDirPath));
            for (String line : BinList) {
                BinListWriter.write(line + "\n");
            }
            BinListWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            FileWriter StartkaliWriter = new FileWriter(String.format("%s/info/startkali.txt", WorkDirPath));
            for (String line : StartkaliConfig) {
                StartkaliWriter.write(line + "\n");
            }
            StartkaliWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            FileWriter XstartupWriter = new FileWriter(String.format("%s/info/xstartup.txt", WorkDirPath));
            for (String line : VNCXstartup) {
                XstartupWriter.write(line + "\n");
            }
            XstartupWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // end
        panel.removeComponent(label);
        label.setText("正在打包...");
        panel.addComponent(label);
        try {
            gui.updateScreen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            Process PackProcess = runtime.exec(String.format("tar -zcf %s/DeviceInformationByheStudio.tar.gz %s/info", WorkDirPath, WorkDirPath));
            while (true) {
                if (PackProcess.waitFor() == 0) {
                    break;
                }
            }
        } catch (IOException | InterruptedException e) {
            panel.removeComponent(label);
            label.setText(String.format("打包失败，你可以到 %s 手动打包并提交到 hestudio@hestudio.net", WorkDirPath));
            panel.addComponent(label);
            try {
                gui.updateScreen();
            } catch (IOException ex) {
                throw new RuntimeException(e);
            }

            throw new RuntimeException(e);
        }

        panel.removeComponent(label);
        label.setText("正在复制到 Download 目录...");
        panel.addComponent(label);
        try {
            gui.updateScreen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            Process CopyProcess = runtime.exec(String.format("cp %s/DeviceInformationByheStudio.tar.gz /sdcard/Download/", WorkDirPath));
            while (true) {
                if (CopyProcess.waitFor() == 0) {
                    break;
                }
            }
        } catch (IOException | InterruptedException e) {
            panel.removeComponent(label);
            label.setText(String.format("复制失败，你可以到 %s 手动提交到 hestudio@hestudio.net", WorkDirPath));
            panel.addComponent(label);
            try {
                gui.updateScreen();
            } catch (IOException ex) {
                throw new RuntimeException(e);
            }
            throw new RuntimeException(e);
        }

        panel.removeComponent(label);
        label.setText("正在清理运行时文件...");
        panel.addComponent(label);
        try {
            gui.updateScreen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (InfoDir.exists()) {
            DirectoryDelete.Main(String.format("%s/info", WorkDirPath));
        }

        File TarPath = new File(String.format("%s/DeviceInformationByheStudio.tar.gz", WorkDirPath));
        if (TarPath.exists()) {
            TarPath.delete();
        }

        final WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
        MessageDialog.showMessageDialog(textGUI, AppName, "完成，你可以在手机Download目录查看 DeviceInformationByheStudio.tar.gz 文件。");

        System.exit(0);
    }

    private static void ifPermissionRefuse() {
        Screen screen;
        try {
            Terminal terminal = new DefaultTerminalFactory().createTerminal();
            screen = new TerminalScreen(terminal);
            screen.startScreen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
        MessageDialog.showMessageDialog(textGUI, AppName, "按照你的意愿，我们将清理目录并马上关闭本程序。再见！");

        File WorkDir = new File(WorkDirPath);
        if (WorkDir.exists()) {
            DirectoryDelete.Main(WorkDirPath);
        }


        try {
            screen.stopScreen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.exit(0);
    }

    private static void PermissionVerify() {
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

        panel.addComponent(new Label("免责声明："));
        panel.addComponent(new Label("本工具用于heStudio快速诊断你所遇到的问题，我们会读取系统敏感配置，包括但不限于系统硬件信息，Android版本，Termux配置，系统进程，存储内容。\n我们需要征得你的同意才会读取这些信息。"));
        panel.addComponent(new Button(" 同意", () -> {
            MainProcess();
            gui.removeWindow(window);
            window.close();
        }));
        panel.addComponent(new Button(" 不同意", () -> {
            ifPermissionRefuse();
            gui.removeWindow(window);
            window.close();
        }));

        window.setTitle(AppName);
        window.setComponent(panel);
        window.setHints(List.of(Window.Hint.FULL_SCREEN));

        gui.addWindowAndWait(window);
    }

    public static void main(String[] args) {
        // environment verify
        File InfoDir = new File(String.format("%s/info", WorkDirPath));
        if (InfoDir.exists()) {
            DirectoryDelete.Main(String.format("%s/info", WorkDirPath));
        }

        // get permission verify
        PermissionVerify();
    }
}