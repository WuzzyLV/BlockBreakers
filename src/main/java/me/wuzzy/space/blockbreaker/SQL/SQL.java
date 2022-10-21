package me.wuzzy.space.blockbreaker.SQL;

import me.wuzzy.space.blockbreaker.BlockBreaker;
import me.wuzzy.space.blockbreaker.BlockBreakerObject;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.h2.jdbcx.JdbcConnectionPool;

import java.sql.*;

public class SQL {
    static BlockBreaker instance;
    public SQL(BlockBreaker inst){
        instance=inst;
    }

    public static Connection connection;
    public void initializeDatabase(){
        try{
            JdbcConnectionPool cp = JdbcConnectionPool.
                    create("jdbc:h2:"+instance.getDataFolder().getAbsolutePath()+"/data/database;AUTO_SERVER=TRUE", "sa", "sa"    );
            connection = cp.getConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Connection getConnection(){
        return connection;
    }
    public void closeConnection(){
        try {
            connection.close();
            instance.getLogger().info("Connection Closed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createIfDoesntExist(){
        try {
            String sqlQuery="CREATE TABLE IF NOT EXISTS `BlockBreakers` (" +
                    "`ID` INT NOT NULL AUTO_INCREMENT," +
                    "`location` VARCHAR," +
                    "`level` INTEGER" +
                    ");";

            getConnection().createStatement().executeUpdate(sqlQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void printDatabase(){
        try {

            // SQL command data stored in String datatype
            String sql = "select * from BlockBreakers";

            ResultSet rs = getConnection().prepareStatement(sql).executeQuery();

            // Printing ID, name, email of customers
            // of the SQL command above
            System.out.println("id\t\tlocation\t\tlevel");

            // Condition check
            while (rs.next()) {

                int id = rs.getInt("id");
                String location = rs.getString("location");
                int level = rs.getInt("level");
                System.out.println(id + "\t\t" + location
                        + "\t\t" + level);
            }
        }

        // Catch block to handle exception
        catch (SQLException e) {

            // Print exception pop-up on screen
            System.out.println(e);
        }
    }
    public void sendQuery(String query) {
        try {
            getConnection().createStatement().executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addLocation(Block block, int level){
        sendQuery("INSERT INTO BlockBreakers(location, level) VALUES ('"+blockToString(block)+"', "+level+");");
    }

    public void removeLocation(Block block){
        sendQuery("DELETE FROM BlockBreakers WHERE location= '"+blockToString(block)+"';");
    }

    public void loadFromDataBase(){
        try {
            String sql = "select * from BlockBreakers";
            ResultSet rs = getConnection().prepareStatement(sql).executeQuery();

            while (rs.next()) {
                Location location=stringToBlock(rs.getString("location"));
                Block block= Bukkit.getWorld(location.getWorld().getName()).getBlockAt(location);
                int level = rs.getInt("level");

                instance.addToHashMap(block, new BlockBreakerObject(block, level, instance));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String blockToString(Block b){
        return b.getWorld().getName()+","+b.getX()+","+b.getY()+","+b.getZ();
    }
    public Location stringToBlock(String b){
        String[] data=b.split(",");
        return new Location(Bukkit.getWorld(data[0]), Double.parseDouble(data[1]),Double.parseDouble(data[2]),Double.parseDouble(data[3]));
    }

}
