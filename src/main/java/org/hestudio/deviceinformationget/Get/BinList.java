package org.hestudio.deviceinformationget.Get;

import org.hestudio.deviceinformationget.Main;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BinList {
    public static String[] Main() {
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("ls -la /data/data/com.termux/files/usr/bin/");
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String[] BinList;
            String outputLine;
            StringBuilder Info = new StringBuilder();

            while ((outputLine = stdInput.readLine()) != null) {
                Info.append(outputLine).append("\n");
            }
            BinList = Info.toString().split("\n");
            return BinList;

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
            MessageDialog.showMessageDialog(textGUI, Main.AppName, "Error: 程序在获取bin信息时遇到错误，请联系heStudio处理。");
            System.exit(-1);
        }
        return new String[0];
    }
}
