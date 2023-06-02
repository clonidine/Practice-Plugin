package dev.mig.practice.repository;

import com.databridge.mig.database.DatabaseProvider;
import com.databridge.mig.repository.Repository;
import dev.mig.practice.PracticePlugin;
import dev.mig.practice.api.model.fighter.rank.Rank;
import dev.mig.practice.model.RankImpl;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.List;
import java.util.Optional;


public class RankRepository implements Repository<Rank> {

    private final DatabaseProvider provider;
    private final String TABLE_NAME = "ranks";

    public RankRepository(PracticePlugin plugin) {
        provider = plugin.getDatabaseProvider();

        createTable();
    }

    @Override
    public List<Rank> findAll() {
        return null;
    }

    @Override
    public void save(Rank rank) {

        try (final Connection connection = provider.getConnection()) {

            final String commandToExecute = String.format("INSERT INTO %s VALUES (?, ?, ?)", TABLE_NAME);

            PreparedStatement statement =  connection.prepareStatement(commandToExecute);

            statement.setString(1, rank.getName());
            statement.setString(2, rank.getColor().name());
            statement.setString(3, rank.getFormat());

            statement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void delete(Rank rank) {

    }

    @Override
    public @NotNull Optional<Rank> findOne(String field, String value) {

        try (Connection connection = provider.getConnection()) {

            final String commandToExecute = String.format("SELECT * FROM %s WHERE %s = ?", TABLE_NAME, field);

            final PreparedStatement statement = connection.prepareStatement(commandToExecute);

            statement.setString(1, value);

            final ResultSet rankResultSet = statement.executeQuery();

            final boolean rankFound = rankResultSet.next();

            if (rankFound) {

                final String rankName = rankResultSet.getString("Name");

                final ChatColor color = ChatColor.valueOf(rankResultSet.getString("Color"));

                final String format = rankResultSet.getString("Format");

                final Rank rank = new RankImpl(rankName, color, format);

                return Optional.of(rank);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void createTable() {

        try (final Connection connection = provider.getConnection()) {

            final Statement statement = connection.createStatement();

            final String commandToExecute = String.format(
                    "CREATE TABLE IF NOT EXISTS %s ("
                            + "Name varchar(255),"
                            + "Color varchar(255), "
                            + "Format varchar(255), PRIMARY KEY(Name));", TABLE_NAME);

            statement.executeUpdate(commandToExecute);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(String columnFilterName, String columnToUpdate, String filter, String newValue) {
        return false;
    }
}
