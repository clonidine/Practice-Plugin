package dev.mig.practice.repository;

import com.databridge.mig.database.DatabaseProvider;
import com.databridge.mig.repository.Repository;
import dev.mig.practice.PracticePlugin;
import dev.mig.practice.api.exception.ObjectNotFoundException;
import dev.mig.practice.api.model.fighter.Fighter;
import dev.mig.practice.api.model.fighter.rank.Rank;
import dev.mig.practice.api.model.fighter.stats.Statistics;
import dev.mig.practice.model.FighterImpl;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class FighterRepository implements Repository<Fighter> {

    private final DatabaseProvider databaseProvider;
    private final Repository<Rank> rankRepository;
    private final String TABLE_NAME = "fighters";

    public FighterRepository(PracticePlugin plugin) {

        databaseProvider = plugin.getDatabaseProvider();
        rankRepository = plugin.getRankRepository();

        createTable();
    }

    @Override
    public List<Fighter> findAll() {

        final List<Fighter> fighters = new ArrayList<>();

        try (final Connection connection = databaseProvider.getConnection()) {

            final String commandToExecute = String.format("SELECT * FROM %s", TABLE_NAME);

            final PreparedStatement statement = connection.prepareStatement(commandToExecute);

            final ResultSet resultSet = statement.executeQuery();

            do {

                final UUID id = UUID.fromString(resultSet.getString("id"));

                final int kills = resultSet.getInt("kills");
                final int deaths = resultSet.getInt("deaths");

                final float kdr = resultSet.getFloat("kdr");

                final String rankName = resultSet.getString("rank");

                final Optional<Rank> rankToFind = rankRepository.findOne("name", rankName);

                rankToFind.ifPresent(rank -> {

                    final Statistics statistics = new Statistics(kills, deaths, kdr);
                    final Fighter fighter = new FighterImpl(id, statistics, rank);

                    fighters.add(fighter);
                });

            } while (resultSet.next());

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return fighters;
    }

    @Override
    public boolean save(Fighter obj) {

        try (final Connection connection = databaseProvider.getConnection()) {

            final String commandToExecute = String.format("INSERT INTO %s VALUES (?, ?, ?, ?, ?)", TABLE_NAME);

            final PreparedStatement statement = connection.prepareStatement(commandToExecute);

            final Statistics statistics = obj.getStats();

            statement.setString(1, String.valueOf(obj.getUuid()));
            statement.setInt(2, statistics.getKills());
            statement.setInt(3, statistics.getDeaths());
            statement.setFloat(4, statistics.getKdr());
            statement.setString(5, obj.getRank().getName());

            statement.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean delete(Fighter object) {
        return false;
    }

    @Override
    public @NotNull Optional<Fighter> findOne(String field, String value) {

        try (final Connection connection = databaseProvider.getConnection()) {

            final String commandToExecute = String.format("SELECT * FROM %s WHERE %s = ? LIMIT 1", TABLE_NAME, field);
            final PreparedStatement statement = connection.prepareStatement(commandToExecute);

            statement.setString(1, value);

            final ResultSet fighterResultSet = statement.executeQuery();
            final boolean hasFound = fighterResultSet.next();

            if (hasFound) {

                final String rankName = fighterResultSet.getString("Rank");
                final Optional<Rank> rankToFind = rankRepository.findOne("Name", rankName);

                final boolean rankPresent = rankToFind.isPresent();

                if (rankPresent) {

                    final UUID id = UUID.fromString(fighterResultSet.getString("id"));

                    final int kills = fighterResultSet.getInt("kills");
                    final int deaths = fighterResultSet.getInt("deaths");

                    final float kdr = fighterResultSet.getFloat("kdr");

                    final Statistics statistics = new Statistics(kills, deaths, kdr);
                    final Rank rank = rankToFind.get();
                    final Fighter fighter = new FighterImpl(id, statistics, rank);

                    return Optional.of(fighter);
                } else {
                    throw new ObjectNotFoundException("Fighter's rank not found. Please, set a rank for this fighter.");
                }

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
                    "CREATE TABLE IF NOT EXISTS %s ("
                            + "Id VARCHAR(255), PRIMARY KEY(Id),"
                            + "Kills INT,"
                            + "Deaths INT,"
                            + "Kdr FLOAT,"
                            + "Rank VARCHAR(255))", TABLE_NAME);

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

            statement.setString(1, newValue);
            statement.setString(2, filter);

            statement.executeUpdate();

            return true;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return false;
    }

    @Override
    public <V> List<Fighter> findAllOfId(String s, V v) {
        return null;
    }
}
