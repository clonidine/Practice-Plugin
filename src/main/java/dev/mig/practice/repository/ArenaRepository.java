package dev.mig.practice.repository;

import com.databridge.mig.database.DatabaseProvider;
import com.databridge.mig.repository.Repository;
import dev.mig.practice.PracticePlugin;
import dev.mig.practice.api.model.arena.Arena;
import dev.mig.practice.api.model.arena.positions.Position;
import dev.mig.practice.model.ArenaImpl;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public final class ArenaRepository implements Repository<Arena> {

    private final DatabaseProvider databaseProvider;
    private static final String TABLE_NAME = "arenas";
    private final Repository<Position> positionsRepository;

    public ArenaRepository(PracticePlugin plugin) {
        databaseProvider = plugin.getDatabaseProvider();
        positionsRepository = plugin.getPositionsRepository();

        createTable();
    }

    @Override
    public List<Arena> findAll() {
        return null;
    }

    @Override
    public boolean save(Arena arena) {

        try (final Connection connection = databaseProvider.getConnection()) {

            final String commandToExecute = String.format("INSERT INTO %s VALUES (?, ?)", TABLE_NAME);

            final PreparedStatement statement = connection.prepareStatement(commandToExecute);

            statement.setString(1, arena.getName());
            statement.setBoolean(2, arena.isActive());

            statement.executeUpdate();

            return true;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean delete(Arena arena) {
        return false;
    }

    @Override
    public @NotNull Optional<Arena> findOne(String filter, String value) {

        try (final Connection connection = databaseProvider.getConnection()) {

            final String commandToExecute = String.format("SELECT * FROM %s WHERE %s = ? LIMIT 1", TABLE_NAME, filter);

            final PreparedStatement statement = connection.prepareStatement(commandToExecute);

            statement.setString(1, value);

            final ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                final String arenaName = resultSet.getString("Name");

                final boolean active = resultSet.getBoolean("Active");

                final List<Position> positions = positionsRepository.findAllOfId("Id", arenaName);

                final Arena arena = new ArenaImpl(arenaName, active);

                arena.setPositions(positions);

                return Optional.of(arena);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public void createTable() {

        try (final Connection connection = databaseProvider.getConnection()) {

            final Statement statement = connection.createStatement();

            final String commandToExecute = String.format(
                    "CREATE TABLE IF NOT EXISTS %s (Name VARCHAR(255), PRIMARY KEY(Name), Active BOOLEAN)", TABLE_NAME
            );

            statement.executeUpdate(commandToExecute);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(String columnFilterName, String columnToUpdate, String filter, String newValue) {

        try (final Connection connection = databaseProvider.getConnection()) {

            final String commandToExecute = String.format("UPDATE %s SET %s = ? WHERE %s = ?", TABLE_NAME, columnToUpdate, columnFilterName);

            final PreparedStatement statement = connection.prepareStatement(commandToExecute);

            statement.setBoolean(1, Boolean.parseBoolean(newValue));
            statement.setString(2, filter);

            statement.executeUpdate();

            return true;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return false;
    }
    @Override
    public <V> List<Arena> findAllOfId(String s, V v) {
        return null;
    }
}