package de.littleprogrammer.lpmcbans;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BadWords {

    private List<String> badWords = new ArrayList<>();

    public BadWords() throws SQLException {

        PreparedStatement statement = null;
        statement = Main.getInstance().getDatabase().getConnection().prepareStatement("SELECT WORD FROM badwords");

        ResultSet rs = statement.executeQuery();
        while (rs.next()){
            badWords.add(rs.getString("WORD"));
        }
    }
    public List<String> getBadWords() {
        return badWords;
    }

    public void addBadWord(String word) throws SQLException {

        PreparedStatement statement2 = null;
        statement2 = Main.getInstance().getDatabase().getConnection().prepareStatement("INSERT INTO badwords (ID, WORD) VALUES (" + "default," + "'" + word + "'" + ");");
        statement2.executeUpdate();
    }

    public void removeBadWord(String word) throws SQLException {

        PreparedStatement statement3;
        statement3 = Main.getInstance().getDatabase().getConnection().prepareStatement("DELETE FROM badwords WHERE word='" + word + "';");
        statement3.executeUpdate();
    }
}
