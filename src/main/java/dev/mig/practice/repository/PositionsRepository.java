package dev.mig.practice.repository;

import com.databridge.mig.database.DatabaseProvider;
import com.databridge.mig.repository.Repository;
import dev.mig.practice.PracticePlugin;
import dev.mig.practice.api.model.arena.positions.Position;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class PositionsRepository implements Repository<Position> {

    private final DatabaseProvider databaseProvider;
    private static final String TABLE_NAME = "positions";

    public PositionsRepository(PracticePlugin plugin) {
        databaseProvider = plugin.getDatabaseProvider();

        createTable();
    }

    @Override
    public List<Position> findAll() {
        return null;
    }

    @Override
    public boolean save(Position position) {

        try (final Connection connection = databaseProvider.getConnection()) {

            final String commandToExecute = String.format("INSERT INTO %s VALUES (?, ?, ?, ?, ?, ?)", TABLE_NAME);

            final PreparedStatement statement = connection.prepareStatement(commandToExecute);

            statement.setString(1, position.getId());

            statement.setDouble(2, position.getX());
            statement.setDouble(3, position.getY());
            statement.setDouble(4, position.getZ());

            statement.setFloat(5, position.getYaw());
            statement.setFloat(6, position.getPitch());

            statement.executeUpdate();

            return true;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean delete(Position position) {
        return false;
    }

    @Override
    public @NotNull Optional<Position> findOne(String s, String s1) {
        return Optional.empty();
    }

    @Override
    public void createTable() {

        try (final Connection connection = databaseProvider.getConnection()) {

            final Statement statement = connection.createStatement();

            final String commandToExecute = String.format(
                    "CREATE TABLE IF NOT EXISTS %s ("
                            + "Id VARCHAR(255),"
                            + "X DOUBLE,"
                            + "Y DOUBLE,"
                            + "Z DOUBLE,"
                            + "Yaw FLOAT,"
                            + "Pitch FLOAT)", TABLE_NAME);

            statement.executeUpdate(commandToExecute);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(String columnFilterName, String columnToUpdate, String filter, String newValue) {
        return Repository.super.update(columnFilterName, columnToUpdate, filter, newValue);
    }

    @Override
    public <V> List<Position> findAllOfId(String id, V v) {

        final List<Position> positions = new ArrayList<>();

        try (final Connection connection = databaseProvider.getConnection()) {

            final String commandToExecute = String.format("SELECT * FROM %s WHERE %s = ?", TABLE_NAME, id);

            final PreparedStatement statement = connection.prepareStatement(commandToExecute);

            statement.setString(1, v.toString());

            final ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                final String positionId = resultSet.getString("Id");

                final double x = resultSet.getDouble("X");
                final double y = resultSet.getDouble("Y");
                final double z = resultSet.getDouble("Z");

                final float yaw = resultSet.getFloat("Yaw");
                final float pitch = resultSet.getFloat("Pitch");

                final Position position = new Position(positionId, x, y, z, yaw, pitch);

                positions.add(position);

            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return positions;
    }
}
