package org.hestudio.deviceinformationget.Tool;

import java.io.File;

public class DirectoryDelete {
    public static void Main(String folderPath) {
        // 创建File对象
        File folder = new File(folderPath);

        // 如果文件夹不为空
        if (folder.isDirectory()) {
            // 获取文件夹下的所有子文件夹和文件
            File[] files = folder.listFiles();

            // 遍历所有子文件夹和文件，递归删除
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        if (!file.delete()) {
                            System.err.print("删除文件夹时遇到错误。");
                        }
                    } else if (file.isDirectory()) {
                        deleteFolder(file);
                    }
                }
            }

            // 删除文件夹
            if (!folder.delete()) {
                System.err.print("删除文件夹时遇到错误。");
            }
        } else {
            System.out.println("给定的路径不是一个文件夹。");
        }
    }

    // 递归删除文件夹
    private static void deleteFolder(File folder) {
        // 获取文件夹下的所有子文件夹和文件
        File[] files = folder.listFiles();

        // 遍历所有子文件夹和文件，递归删除
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!file.delete()) {
                        System.err.print("删除文件夹时遇到错误。");
                    }
                } else if (file.isDirectory()) {
                    deleteFolder(file);
                }
            }
        }

        // 删除文件夹
        if (!folder.delete()) {
            System.err.print("删除文件夹时遇到错误。");
        }
    }
}
