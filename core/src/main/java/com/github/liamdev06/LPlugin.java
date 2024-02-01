package com.github.liamdev06;

import com.github.liamdev06.command.CommandManager;
import com.github.liamdev06.component.Component;
import com.github.liamdev06.component.ComponentManager;
import com.github.liamdev06.configuration.ConfigurationManager;
import com.github.liamdev06.registry.RegistryFactory;
import com.github.liamdev06.scheduler.BukkitSchedulerAdapter;
import com.github.liamdev06.scheduler.handler.SchedulerHandlerManager;
import com.github.liamdev06.scheduler.interfaces.SchedulerAdapter;
import com.github.liamdev06.utils.bukkit.ListenerRegistryFactory;
import com.github.liamdev06.utils.java.LoggerUtil;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.reflections.Reflections;
import org.slf4j.Logger;

import java.net.URISyntaxException;

/**
 * Represents the plugin entry point for a plugin using this framework/library.
 * <p>
 * By using this as the entry point and not {@link JavaPlugin}, the library
 * can add logic to the plugin start-up and shutdown as well as initiate the utilities
 * and framework provided by this library.
 */
public abstract class LPlugin extends JavaPlugin {

    private static final @NonNull String PACKAGE = LPlugin.class.getPackageName();
    public static Logger LOG;

    private final @NonNull Class<? extends LPlugin> parentPluginClass;
    private final @NonNull String parentPluginIdentifier;
    private final @NonNull Reflections reflections;
    private final @NonNull RegistryFactory registryFactory;
    private final @NonNull ConfigurationManager configurationManager;

    private ComponentManager componentManager;
    private CommandManager commandManager;
    private SchedulerAdapter schedulerAdapter;
    private SchedulerHandlerManager schedulerHandlerManager;

    public LPlugin() {
        this.parentPluginClass = this.getClass();
        this.parentPluginIdentifier = this.getPluginMeta().getName();
        LOG = LoggerUtil.createLogger(this.parentPluginIdentifier);

        this.reflections = new Reflections(PACKAGE);
        this.registryFactory = new RegistryFactory(this.reflections, this);

        try {
            this.configurationManager = new ConfigurationManager(this);
        } catch (URISyntaxException exception) {
            throw new RuntimeException("Could not set up ConfigurationManager!", exception);
        }
    }

    /**
     * Called when plugin loads.
     */
    public void onPreLoad() { }

    /**
     * Called when the plugin starts up.
     */
    public abstract void onStartup();

    /**
     * Called when the plugin shuts down.
     */
    public abstract void onShutdown();

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this).silentLogs(true));
        this.onPreLoad();
    }

    @Override
    public void onEnable() {
        final long timeAtStart = System.currentTimeMillis();
        this.onStartup();

        this.componentManager = new ComponentManager(this, this.registryFactory);
        this.commandManager = new CommandManager(this);
        this.schedulerAdapter = new BukkitSchedulerAdapter(this);
        this.schedulerHandlerManager = new SchedulerHandlerManager(this.registryFactory);

        this.registryFactory.executeAllAutoRegistering();
        this.componentManager.enableAllComponents();

        CommandAPI.onEnable();
        new ListenerRegistryFactory(this).registerAllListeners();
        this.schedulerHandlerManager.startAllAutoSchedulers();

        long finishedTime = System.currentTimeMillis() - timeAtStart;
        this.logStartupInformationDone(finishedTime);
    }

    @Override
    public void onDisable() {
        final long timeAtStart = System.currentTimeMillis();
        LOG.info("Starting shutdown of " + this.parentPluginIdentifier + "...");

        this.onShutdown();
        CommandAPI.onDisable();

        if (this.componentManager != null) {
            this.componentManager.disableAllComponents();
        }
        if (this.schedulerAdapter != null) {
            this.schedulerAdapter.shutdown();
        }

        long finishedTime = System.currentTimeMillis() - timeAtStart;
        LOG.info(this.parentPluginIdentifier + " shutdown in " + finishedTime + "ms.");
    }

    private void logStartupInformationStart(@NonNull Server server) {
        final String version = server.getVersion()
                .replace("(", "")
                .replace(")", "");

        LOG.info("#-----------------------------------#");
        LOG.info("    Plugin Startup Information       ");
        LOG.info("     " + this.getName());
        LOG.info("                                     ");
        LOG.info("Internal ID: " + this.parentPluginIdentifier);
        LOG.info("Found Main Class: " + this.parentPluginClass.getSimpleName() + " (" + this.parentPluginClass.getPackageName() + ")");
        LOG.info("Version: v" + this.getPluginMeta().getVersion() + " (" + version + ", " + server.getBukkitVersion() + ")");
        LOG.info("#-----------------------------------#");
    }

    private void logStartupInformationDone(long finishedMilliseconds) {
        LOG.info("#-----------------------------------#");
        LOG.info("    Plugin Successfully Loaded       ");
        LOG.info("                                     ");
        LOG.info("Comment: '" + this.parentPluginIdentifier + " has successfully loaded without issues.'");
        LOG.info("Load Time: " + finishedMilliseconds + "ms");
        LOG.info("#-----------------------------------#");
    }

    /**
     * @return Instance of the plugin entry point.
     */
    public static @NonNull LPlugin getInstance() {
        return getPlugin(LPlugin.class);
    }

    /**
     * @return The identifier for this plugin.
     */
    public @NonNull String getPluginIdentifier() {
        return this.parentPluginIdentifier;
    }

    /**
     * @return The {@link Reflections} instance for this plugin using the package of {@link LPlugin#PACKAGE}.
     */
    public @NonNull Reflections getReflections() {
        return this.reflections;
    }

    /**
     * @return The {@link RegistryFactory} responsible for class instance registering and instantiation.
     */
    public @NonNull RegistryFactory getRegistryFactory() {
        return this.registryFactory;
    }

    /**
     * @return The {@link ConfigurationManager} responsible for storing and managing configurations.
     */
    public @NonNull ConfigurationManager getConfigurationManager() {
        return this.configurationManager;
    }

    /**
     * @return The {@link ComponentManager} responsible for managing and holding all modules.
     */
    public @NonNull ComponentManager getComponentManager() {
        return this.componentManager;
    }

    /**
     * @return The {@link CommandManager} that provides custom integration extension to the {@link CommandAPI}.
     */
    public @NonNull CommandManager getCommandManager() {
        return this.commandManager;
    }

    /**
     * @return The {@link SchedulerAdapter} used to work with synchronous and asynchronous tasks.
     */
    public @NonNull SchedulerAdapter getSchedulerAdapter() {
        return this.schedulerAdapter;
    }

    /**
     * @return The {@link SchedulerHandlerManager} used to abstract task logic to separate classes.
     */
    public @NonNull SchedulerHandlerManager getSchedulerHandlerManager() {
        return this.schedulerHandlerManager;
    }

    /**
     * Gets the instance of a registered component from the {@link ComponentManager}.
     * <p>
     * This method is unchecked meaning if a component is null, an {@link NullPointerException} will be thrown.
     *
     * @param componentClass Class of the component.
     * @return Instance of a registered {@link Component}.
     * @see {@link LPlugin#getComponent(Class)}
     * @throws NullPointerException If the component is not registered within the {@link ComponentManager}.
     */
    public <T extends Component> @NonNull T getRegisteredComponent(@NonNull Class<T> componentClass) {
        T module = this.getComponentManager().getComponent(componentClass);
        if (module == null) {
            throw new NullPointerException("The component with class " + componentClass.getSimpleName() + " is null");
        }
        return module;
    }

    /**
     * Static method to get an instance of a registered component.
     *
     * @param componentClass Class of the component.
     * @return Instance of a registered {@link Component}.
     */
    public static <T extends Component> @NonNull T getComponent(@NonNull Class<T> componentClass) {
        return LPlugin.getInstance().getRegisteredComponent(componentClass);
    }
}