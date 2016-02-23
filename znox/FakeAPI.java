package znox;

import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_7_R4.PacketPlayOutNamedEntitySpawn;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * <h1>FakeAPI</h1>
 * <h2>Fake API criada na versao 1_7_R4 - Spigot</h2>
 * @author zNoX_
 * @version 1.0
 */
public abstract class FakeAPI {

    // Variavel de jogador
    private Player player;

    // Variavel de estado
    private FakeEnum state;

    // Variavel de nickname
    private String name;

    // Construcao...
    public FakeAPI(Player player, String name) {
        this.player = player;
        state = FakeEnum.No;
    }

    // Construcao...
    public FakeAPI(Player player) {
        this.player = player;
        state = FakeEnum.No;
    }

    // Construcao...
    public FakeAPI() {
        state = FakeEnum.No;
    }

    // Retorna a variavel do jogador
    public Player getPlayer() { return player; }

    // Reetorna a variavel do nome falso
    public String getName() { return name; }

    // Retorna a variavel de estado
    public FakeEnum getState() { return state; }

    // Aplica um jogador a variavel
    public Player setPlayer(Player player) { return this.player = player; }

    // Aplica o nome falso a variavel
    public String setName(String name) { return this.name = name; }

    // Aplica um novo estado
    public FakeEnum setState(FakeEnum state) { return this.state = state; }

    // Aplica o fake ao jogador especificado
    public FakeAPI setFake() {
        // Checa se o jogador ja esta usando Fake
        if (isFake() == FakeEnum.No) {
            // Retorna todos os jogadores presentes no servidor
            Collection<? extends Player> collection = Bukkit.getServer().getOnlinePlayers();

            // Cria uma variavel para todos os jogadores
            for (Player players : collection) {

                // Checa se o nome requisitado nao esta em uso
                if (!players.getName().equalsIgnoreCase(getName())) {
                    // Retorna uma EntityPlayer (class) de todos os jogadores
                    EntityPlayer entityPlayers = ((CraftPlayer) players).getHandle();

                    // Retorna uma EntityPlayer (class) do jogador
                    EntityPlayer entityPlayer = ((CraftPlayer) getPlayer()).getHandle();
                    // Muda o nickname visual do player para o nome falso
                    entityPlayer.displayName = getName();

                    // Packets para destruicao e respawn
                    PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(entityPlayer.getId());
                    PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(entityPlayer);

                    // Metodo para recuperar o acesso a variaveis declaradas privadas
                    try {
                        Field field = spawn.getClass().getDeclaredField("b");
                        field.setAccessible(true);
                        Object profile = field.get(spawn);
                        Field name = profile.getClass().getDeclaredField("name");
                        name.setAccessible(true);
                        // Muda o nome antigo para o nome falso
                        name.set(profile, getName());
                    } catch (Exception e) {
                        throw new Error("Erro ao acessar a GameProfile. Erro: " + e);
                    }

                    if (players != getPlayer()) {
                        // Manda os pacotes para todos os jogadores, com excessao do mesmo
                        entityPlayers.playerConnection.sendPacket(destroy);
                        entityPlayers.playerConnection.sendPacket(spawn);

                    }
                } else {
                    //Erros...
                    throw new Error("O nome requisitado ja possui um jogador mestre.");
                }
            }
        } else {
            // Erros...
            throw new Error("O jogador definido ja esta utilizando um Fake.");
        }

        return this;
    }

    // Retorna a variavel de estado do
    public FakeEnum isFake() {
        return getName() != null ? FakeEnum.No : FakeEnum.Yes;
    }
    
    // Enum de estado
    public enum FakeEnum {
        No, Yes;
    }

}
