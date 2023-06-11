package dev.mig.practice;

import com.databridge.mig.database.DatabaseProvider;
import com.databridge.mig.repository.Repository;
import dev.mig.practice.api.manager.ObjectManager;
import dev.mig.practice.api.model.arena.Arena;
import dev.mig.practice.api.model.arena.positions.Position;
import dev.mig.practice.api.model.fighter.Fighter;
import dev.mig.practice.api.model.fighter.rank.Rank;
import dev.mig.practice.command.ArenaCommand;
import dev.mig.practice.command.FighterStatisticsCommand;
import dev.mig.practice.database.MySQL;
import dev.mig.practice.listener.PlayerChatListener;
import dev.mig.practice.listener.PlayerJoinListener;
import dev.mig.practice.manager.FighterManager;
import dev.mig.practice.repository.ArenaRepository;
import dev.mig.practice.repository.FighterRepository;
import dev.mig.practice.repository.PositionsRepository;
import dev.mig.practice.repository.RankRepository;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

@Getter
public final class PracticePlugin extends JavaPlugin {


    // MANAGERS

    private ObjectManager<UUID> fighterObjectManager;

    // DATABASE

    private DatabaseProvider databaseProvider;

    // REPOSITORIES

    private Repository<Fighter> fighterRepository;
    private Repository<Rank> rankRepository;
    private Repository<Arena> arenaRepository;
    private Repository<Position> positionsRepository;

    @Override
    public void onEnable() {
        connectDatabase();
        initializeRepositories();

        createManager();

        registerListener();
        registerCommand();

        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Practice plugin enabled");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Practice plugin disabled");
    }

    private void connectDatabase() {
        databaseProvider = new MySQL(this);
    }

    private void registerListener() {
        new PlayerJoinListener(this);
        new PlayerChatListener(this);
    }

    private void createManager() {
        fighterObjectManager = new FighterManager();
    }

    private void initializeRepositories() {
        rankRepository = new RankRepository(this);
        fighterRepository = new FighterRepository(this);
        positionsRepository = new PositionsRepository(this);
        arenaRepository = new ArenaRepository(this);
    }

    private void registerCommand() {
        new FighterStatisticsCommand(this);
        new ArenaCommand(this);
    }
}