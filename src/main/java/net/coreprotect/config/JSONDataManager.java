package net.coreprotect.config;

import net.coreprotect.CoreProtect;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class JSONDataManager {
    public static JSONObject TWLangData = null;
    private static final String sourcePath = "/zh_tw.json";
    private static final String destination = System.getProperty("user.dir") + "/plugins/CoreProtect";
    private static final String files = destination + "/" + new File(sourcePath).getName();

    public static void initTWLangData(){
        new BukkitRunnable(){
            @Override
            public void run() {
                InputStream inputStream = CoreProtect.class.getResourceAsStream(sourcePath);
                try {
                    if (inputStream != null) {
                        File file = new File(destination);
                        if (!file.exists()) {
                            file.mkdir();
                        }
                        File destinationFile = new File(files);
                        if (!destinationFile.exists()) {
                            Files.copy(inputStream, destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        }

                        String json = new String(Files.readAllBytes(Paths.get(files)));
                        TWLangData = new JSONObject(json);
                    }
                }catch (IOException exception){
                    exception.printStackTrace();
                }
            }
        }.runTaskAsynchronously(CoreProtect.getInstance());
    }

    public static void saveClaimLimitData(boolean async){
        if (async){
            new BukkitRunnable(){
                @Override
                public void run() {
                    saveConfig();
                }
            }.runTaskAsynchronously(CoreProtect.getInstance());
            return;
        }
        saveConfig();
    }

    private static void saveConfig(){
        try {
            FileWriter file = new FileWriter(files);
            file.write(TWLangData.toString());
            file.flush();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}