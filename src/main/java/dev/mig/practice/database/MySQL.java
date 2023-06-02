package dev.mig.practice.database;


import com.databridge.mig.database.DatabaseProvider;
import dev.mig.practice.PracticePlugin;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Getter
@RequiredArgsConstructor
public class MySQL implements DatabaseProvider {

    private Connection connection;
    private final PracticePlugin plugin;

    @Override
    public Connection getConnection() {
        final FileConfiguration configuration = plugin.getConfig();

        final String url = configuration.getString("MySQL.url");
        final String user = configuration.getString("MySQL.user");
        final String password = configuration.getString("MySQL.password");

        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return connection;
    }

    @Override
    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
