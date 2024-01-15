package org.hestudio.deviceinformationget.Get;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import org.hestudio.deviceinformationget.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CPUInfo {
    public static String[] Main() {
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("cat /proc/cpuinfo");
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String[] CPUInfo;
            String outputLine;
            StringBuilder Info = new StringBuilder();

            while ((outputLine = stdInput.readLine()) != null) {
                Info.append(outputLine).append("\n");
            }
            CPUInfo = Info.toString().split("\n");
            return CPUInfo;

        } catch (IOException e) {
            Screen screen = null;
            try {
                Terminal terminal = new DefaultTerminalFactory().createTerminal();
                screen = new TerminalScreen(terminal);
                screen.startScreen();

            } catch (IOException ex) {
                throw new RuntimeException(e);
            }
            final WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
            MessageDialog.showMessageDialog(textGUI, Main.AppName, "Error: 程序在获取CPU信息时遇到错误，请联系heStudio处理。");
            System.exit(-1);
        }
        return new String[0];
    }
}
