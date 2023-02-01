package de.littleprogrammer.lpmcbans;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class UUIDConverter {

    public String UUID(String playerName) {

        String uuid;

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName).openStream()));
            uuid = (((JsonObject)new JsonParser().parse(in)).get("id")).toString().replaceAll("\"", "");
            uuid = uuid.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
            in.close();

        } catch (Exception e) {
            System.out.println("Uable to get UUID of: " + playerName + "!");
            uuid = "er";
        }
        return uuid;
    }

    public String NAME(String uuid){

        String name;

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.replace("-", "")).openStream()));
            name = (((JsonObject)new JsonParser().parse(in)).get("name")).toString().replaceAll("\"", "");
            in.close();

        }catch (Exception e){
            System.out.println("Uable to get Name of: " + uuid + "!");
            name = "er";
        }

        return name;
    }

}
