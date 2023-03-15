package de.littleprogrammer.lpmcbans;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CustomePlayer {
    private UUID uuid;
    private byte mute;
    private byte ban;
    private byte online;
    private String rank;
    private String name;
    private Timestamp muteTimestamp;
    private Timestamp banTimestamp;
    private Timestamp now = Timestamp.valueOf(LocalDateTime.now());
    private List<String> playersInDB = new ArrayList<>();
    private List<String> playerNamesInDB = new ArrayList<>();
    private List<String> playersBanned = new ArrayList<>();
    private List<String> playerMuted = new ArrayList<>();

    public CustomePlayer(UUID uuid) throws SQLException {

        this.uuid = uuid;

        //Getting normal values
        PreparedStatement statement = null;

        statement = Main.getInstance().getDatabase().getConnection().prepareStatement("SELECT NAME, RANK, MUTE, MUTEON, BAN, BANON, ONLINE FROM players WHERE UUID = ?");
        statement.setString(1, uuid.toString());

        ResultSet rs = statement.executeQuery();
        if (rs.next()){
            name = rs.getString("NAME");
            rank = rs.getString("RANK");
            mute = rs.getByte("MUTE");
            muteTimestamp = rs.getTimestamp("MUTEON");
            ban = rs.getByte("BAN");
            banTimestamp = rs.getTimestamp("BANON");
            online = rs.getByte("ONLINE");
        }else {
            mute = 0;
            muteTimestamp = now;
            banTimestamp = now;
            ban = 0;
            rank = "SPIELER";
            PreparedStatement statement1 = Main.getInstance().getDatabase().getConnection().prepareStatement("INSERT INTO players (ID, UUID, RANK, MUTE, MUTEON, BAN, BANON) VALUES (" + "default," + "'" + uuid + "'," + "'" + rank + "'," + "'" + mute + "'," + "'" + muteTimestamp + "'," + "'" + mute + "'" + ");");
            statement1.executeUpdate();
        }

        //AllPlayers
        PreparedStatement statement1 = null;

        statement1 = Main.getInstance().getDatabase().getConnection().prepareStatement("SELECT UUID FROM players");

        ResultSet rs1 = statement1.executeQuery();
        while (rs1.next()){
            playersInDB.add(rs1.getString("UUID"));
        }

        //AllPLayerNames
        PreparedStatement statement4 = null;

        statement4 = Main.getInstance().getDatabase().getConnection().prepareStatement("SELECT NAME FROM players");

        ResultSet rs4 = statement4.executeQuery();
        while (rs4.next()){
            playerNamesInDB.add(rs4.getString("NAME"));
        }

        //BannedPlayers
        PreparedStatement statement2 = null;

        statement2 = Main.getInstance().getDatabase().getConnection().prepareStatement("SELECT UUID FROM players WHERE BAN = ?");
        statement2.setInt(1, 1);

        ResultSet rs2 = statement2.executeQuery();
        while (rs2.next()){
            playersBanned.add(rs2.getString("UUID"));
        }

        //MutedPlayers
        PreparedStatement statement3 = null;

        statement3 = Main.getInstance().getDatabase().getConnection().prepareStatement("SELECT UUID FROM players WHERE MUTE = ?");
        statement3.setInt(1, 1);

        ResultSet rs3 = statement3.executeQuery();
        while (rs3.next()){
            playerMuted.add(rs3.getString("UUID"));
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws SQLException {
        this.name = name;

        PreparedStatement statement = Main.getInstance().getDatabase().getConnection().prepareStatement("UPDATE players SET NAME = '" + name + "' WHERE UUID = '" + uuid + "';");
        statement.executeUpdate();
    }

    public byte getOnline() {
        return online;
    }

    public void setOnline(byte online) throws SQLException {
        this.online = online;

        PreparedStatement statement = Main.getInstance().getDatabase().getConnection().prepareStatement("UPDATE players SET ONLINE = '" + online + "' WHERE UUID = '" + uuid + "';");
        statement.executeUpdate();
    }

    public byte getMute() {
        return mute;
    }

    public byte getBan() {
        return ban;
    }

    public Timestamp getMuteTimestamp() {
        return muteTimestamp;
    }

    public Timestamp getBanTimestamp(){
        return banTimestamp;
    }

    public String getRank() {
        return rank;
    }

    public List<String> getPlayersInDB() {
        return playersInDB;
    }

    public List<String> getPlayerNamesInDB() {
        return playerNamesInDB;
    }

    public List<String> getPlayersBanned() {
        return playersBanned;
    }

    public List<String> getPlayerMuted() {
        return playerMuted;
    }

    public void setMute(byte mute) throws SQLException {
        this.mute = mute;

        PreparedStatement statement = Main.getInstance().getDatabase().getConnection().prepareStatement("UPDATE players SET MUTE = '" + mute + "' WHERE UUID = '" + uuid + "';");
        statement.executeUpdate();
    }

    public void setBan(byte ban) throws SQLException {
        this.ban = ban;

        PreparedStatement statement = Main.getInstance().getDatabase().getConnection().prepareStatement("UPDATE players SET BAN = '" + ban + "' WHERE UUID = '" + uuid + "';");
        statement.executeUpdate();
    }

    public void setMuteTimestamp(Timestamp muteTimestamp) throws SQLException {
        this.muteTimestamp = muteTimestamp;

        PreparedStatement statement = Main.getInstance().getDatabase().getConnection().prepareStatement("UPDATE players SET MUTEON = '" + muteTimestamp + "' WHERE UUID = '" + uuid + "';");
        statement.executeUpdate();
    }

    public void setBanTimestamp(Timestamp banTimestamp) throws SQLException {
        this.banTimestamp = banTimestamp;

        PreparedStatement statement = Main.getInstance().getDatabase().getConnection().prepareStatement("UPDATE players SET BANON = '" + banTimestamp + "' WHERE UUID = '" + uuid + "';");
        statement.executeUpdate();
    }
}
